package com.pengsoft.acs.api;

import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.service.AccessRecordService;
import com.pengsoft.acs.service.FaceRecognitionService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;

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

}
