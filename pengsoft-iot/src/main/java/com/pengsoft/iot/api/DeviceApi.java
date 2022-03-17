package com.pengsoft.iot.api;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.iot.domain.Device;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.GroupDevice;
import com.pengsoft.iot.service.DeviceService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;

/**
 * The web api of {@link Device}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/iot/device")
public class DeviceApi extends EntityApi<DeviceService, Device, String> {

    @Inject
    private ObjectMapper objectMapper;

    @PostMapping("save-with-groups")
    public void saveWithGroups(@RequestBody final Device device, @NotBlank String groups) {
        getService().saveWithGroups(device, groups);
    }

    @GetMapping("find-one-with-groups")
    @SneakyThrows
    public Map<String, Object> findOneWithGroups(@RequestParam(value = "id", required = false) Device entity) {
        Map<String, Object> result = null;
        final var type = objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
        if (entity == null) {
            entity = getService().getEntityClass().getDeclaredConstructor().newInstance();
        }
        result = objectMapper.convertValue(entity, type);
        final var groups = entity.getGroupDevices().stream().map(GroupDevice::getGroup).map(Group::getId)
                .collect(Collectors.joining(StringUtils.COMMA));
        result.put("groups", groups);
        return result;
    }

    @GetMapping("find-page-by-group-and-name")
    public Page<Device> findPageByGroupAndName(@RequestParam("group.id") Group group, String name, Pageable pageable) {
        return getService().findPageByGroupAndName(group, name, pageable);
    }

}
