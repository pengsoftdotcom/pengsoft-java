package com.pengsoft.acs.api;

import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.domain.QAccessRecord;
import com.pengsoft.acs.service.AccessRecordService;
import com.pengsoft.acs.service.FaceRecognitionService;
import com.pengsoft.basedata.domain.QPerson;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.iot.domain.QDevice;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link AccessRecord}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/acs/access-record")
public class AccessRecordApi extends EntityApi<AccessRecordService, AccessRecord, String> {

    @Inject
    private Map<String, FaceRecognitionService> faceRecognitionServices;

    @RequestMapping("/sync")
    public Map<String, Object> sync(@RequestBody Map<String, Object> params, String service) {
        if (!faceRecognitionServices.containsKey(service)) {
            throw new BusinessException("service.not_found", service);
        }
        return faceRecognitionServices.get(service).syncAccessRecords(params);
    }

    @Override
    public Page<AccessRecord> findPage(Predicate predicate, Pageable pageable) {
        if (!SecurityUtils.hasAnyRole(Role.ADMIN)) {
            final var job = SecurityUtilsExt.getPrimaryJob();
            if (job != null) {
                QPerson person = QAccessRecord.accessRecord.person;
                QDevice device = QAccessRecord.accessRecord.device;
                if (job.isOrganizationChief()) {
                    QueryDslUtils.merge(predicate, device.belongsTo.eq(SecurityUtilsExt.getPrimaryOrganizationId()));
                } else if (job.isDepartmentChief()) {
                    QueryDslUtils.merge(predicate, device.controlledBy.eq(SecurityUtilsExt.getPrimaryDepartmentId()));
                } else {
                    QueryDslUtils.merge(predicate, person.id.eq(SecurityUtilsExt.getPersonId()));
                }
            }
        }
        return super.findPage(predicate, pageable);
    }

}
