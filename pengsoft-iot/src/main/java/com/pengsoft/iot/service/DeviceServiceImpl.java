package com.pengsoft.iot.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.GroupDevice;
import com.pengsoft.iot.repository.DeviceRepository;
import com.pengsoft.iot.repository.GroupDeviceRepository;
import com.pengsoft.iot.repository.GroupRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Device} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class DeviceServiceImpl extends EntityServiceImpl<DeviceRepository, Device, String>
        implements DeviceService {

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private GroupDeviceRepository groupDeviceRepository;

    @Override
    public Device save(Device target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public void saveWithGroups(Device device, String groups) {
        save(device);
        grantGroups(device, Arrays.stream(groups.split(StringUtils.COMMA)).map(groupRepository::getById).toList());
    }

    @Override
    public Optional<Device> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public void grantGroups(Device device, List<Group> groups) {
        final var source = device.getGroupDevices();
        final var target = groups.stream().map(group -> new GroupDevice(group, device)).toList();
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(t -> EntityUtils.equals(s.getGroup(), t.getGroup())
                        && EntityUtils.equals(s.getDevice(), t.getDevice())))
                .toList();
        groupDeviceRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(s -> EntityUtils.equals(t.getGroup(), s.getGroup())
                        && EntityUtils.equals(t.getDevice(), s.getDevice())))
                .toList();
        groupDeviceRepository.saveAll(created);
        source.addAll(created);
        super.save(device);
    }

    @Override
    public Page<Device> findPageByGroupAndName(Group group, String name, Pageable pageable) {
        if (StringUtils.isBlank(name)) {
            name = "%";
        } else {
            name = "%" + name + "%";
        }
        return getRepository().findPageByGroupAndName(group, name, pageable);
    }

}
