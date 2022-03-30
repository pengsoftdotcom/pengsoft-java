package com.pengsoft.acs.service;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.type.MapLikeType;
import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.exception.WeikenException;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.JobRole;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.repository.JobRoleRepository;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.repository.GroupRepository;
import com.pengsoft.iot.service.DeviceService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The face recognition service by Weiken.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@Service
public class WeikenFaceRecognitionService implements FaceRecognitionService {

    private static final String RECOGNITION_TYPE = "1";

    private static final String DEVICE_KEY = "deviceKey";

    private static final String MD5 = "md5";

    private static final String ID_CARD_NUM = "idcardNum";

    private MapLikeType type;

    @Inject
    private Exceptions exceptions;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private DeviceService deviceService;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private StaffRepository staffRepository;

    @Inject
    private JobRoleRepository jobRoleRepository;

    @Inject
    private PersonService personService;

    @Inject
    private PersonFaceDataService personFaceDataService;

    @Inject
    private AccessRecordService accessRecordService;

    @Getter
    @Setter
    class Response {

        private int result = 1;

        private boolean success = true;

        private long total;

        private Object data;

        public Map<String, Object> toMap() {
            Map<String, Object> map = null;
            try {
                map = objectMapper.convertValue(this, type);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return map;
        }
    }

    @PostConstruct
    void setType() {
        this.type = objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
    }

    @Override
    public Map<String, Object> syncPersons(Map<String, Object> params) {
        final var response = new Response();
        try {
            final var groupId = (String) params.get("groupId");
            final var group = groupRepository.findOneByCode(groupId)
                    .orElseThrow(() -> exceptions.entityNotExists(Group.class, groupId));
            final var departmentId = group.getControlledBy();
            final var jobIds = jobRoleRepository.findAllByJobDepartmentIdAndRoleCode(departmentId, "worker").stream()
                    .map(JobRole::getJob).map(Job::getId).toList();
            if (jobIds.isEmpty()) {
                throw new InvalidConfigurationException("no job with role worker configed");
            }
            if (jobIds.size() > 1) {
                throw new InvalidConfigurationException("multiple jobs with role worker configed");
            }

            final var page = Integer.parseInt((String) params.get("page"));
            final var size = Integer.parseInt((String) params.get("pageSize"));
            final var result = staffRepository.findPageByJobIdIn(jobIds, PageRequest.of(page - 1, size));
            final var persons = result.getContent().stream().map(Staff::getPerson).toList();
            final var personFaceDatas = personFaceDataService.findAllByPersonIn(persons);
            response.setTotal(result.getTotalElements());
            response.setData(personFaceDatas.stream()
                    .map(personFaceData -> {
                        var md5 = new StringBuilder();
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getPerson().getName().getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getFace().getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(RECOGNITION_TYPE.getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getDuration().getBytes()));
                        md5 = new StringBuilder(DigestUtils.md5DigestAsHex(md5.toString().getBytes()));
                        return Map.of(ID_CARD_NUM, personFaceData.getPerson().getIdentityCardNumber(), MD5, md5);
                    })
                    .toList());
        } catch (Exception e) {
            log.error("sync persons error: {}", e.getMessage());
            throw new WeikenException(e);
        }
        return response.toMap();
    }

    @Override
    public Map<String, Object> syncPerson(Map<String, Object> params) {
        final var response = new Response();
        try {
            final var identityCardNumber = (String) params.get(ID_CARD_NUM);
            response.setData(personFaceDataService.findOneByPersonIdentityCardNumber(identityCardNumber)
                    .map(personFaceData -> {
                        var md5 = new StringBuilder();
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getPerson().getName().getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getFace().getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(RECOGNITION_TYPE.getBytes()));
                        md5.append(DigestUtils.md5DigestAsHex(personFaceData.getDuration().getBytes()));
                        md5 = new StringBuilder(DigestUtils.md5DigestAsHex(md5.toString().getBytes()));
                        return Map.of(
                                ID_CARD_NUM, personFaceData.getPerson().getIdentityCardNumber(),
                                "name", personFaceData.getPerson().getName(),
                                "imgBase64", personFaceData.getFace(),
                                "type", RECOGNITION_TYPE,
                                "passtime", personFaceData.getDuration(),
                                MD5, md5);
                    }));
        } catch (Exception e) {
            log.error("sync person error: {}", e.getMessage());
            throw new WeikenException(e);
        }
        return response.toMap();
    }

    @Override
    public Map<String, Object> syncAccessRecords(Map<String, Object> params) {
        final var response = new Response();
        try {
            final var deviceCode = (String) params.get(DEVICE_KEY);
            final var accessRecord = new AccessRecord();
            deviceService.findOneByCode(deviceCode).ifPresentOrElse(accessRecord::setDevice,
                    () -> exceptions.entityNotExists(Device.class, deviceCode));
            accessRecord.setPhoto((String) params.get("path"));
            final var identityCardNumber = (String) params.get(ID_CARD_NUM);
            // 识别成功
            if (StringUtils.isNotBlank(identityCardNumber)) {
                personService.findOneByIdentityCardNumber(identityCardNumber).ifPresentOrElse(accessRecord::setPerson,
                        () -> exceptions.entityNotExists(Person.class, identityCardNumber));
                final Map<String, Object> extra = objectMapper.readValue((String) params.get("extra"), type);
                accessRecord.setTemperature(Float.parseFloat((String) extra.get("bodyTemp")));
                accessRecordService.save(accessRecord);
            }
        } catch (Exception e) {
            log.error("create access record error: {}", e.getMessage());
            throw new WeikenException(e);
        }
        return response.toMap();
    }

    @Override
    public Map<String, Object> heartbeat(Map<String, Object> params) {
        final var response = new Response();
        try {
            final var code = (String) params.get(DEVICE_KEY);
            final var ip = (String) params.get("ip");
            deviceService.findOneByCode(code).ifPresentOrElse(
                    device -> {
                        device.setConnectedAt(DateUtils.currentDateTime());
                        device.setIp(ip);
                        deviceService.save(device);
                    },
                    () -> exceptions.entityNotExists(Device.class, code));
        } catch (Exception e) {
            log.error("create access record error: {}", e.getMessage());
            throw new WeikenException(e);
        }
        return response.toMap();
    }

}
