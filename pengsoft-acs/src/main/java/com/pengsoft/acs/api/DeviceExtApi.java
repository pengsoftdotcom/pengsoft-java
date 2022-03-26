package com.pengsoft.acs.api;

import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.acs.service.FaceRecognitionService;
import com.pengsoft.iot.domain.Device;
import com.pengsoft.support.Constant;
import com.pengsoft.support.exception.BusinessException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ext web api of {@link Device}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/iot/device")
public class DeviceExtApi {

    @Inject
    private Map<String, FaceRecognitionService> faceRecognitionServices;

    @RequestMapping("/heartbeat")
    public Map<String, Object> heartbeat(@RequestParam Map<String, Object> params, String service) {
        if (!faceRecognitionServices.containsKey(service)) {
            throw new BusinessException("service.not_found", service);
        }
        return faceRecognitionServices.get(service).heartbeat(params);
    }

}
