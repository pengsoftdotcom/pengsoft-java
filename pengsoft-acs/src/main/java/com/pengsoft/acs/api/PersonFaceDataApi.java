package com.pengsoft.acs.api;

import java.util.Map;

import javax.inject.Inject;

import com.pengsoft.acs.domain.PersonFaceData;
import com.pengsoft.acs.service.FaceRecognitionService;
import com.pengsoft.acs.service.PersonFaceDataService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link PersonFaceData}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/acs/person-face-data")
public class PersonFaceDataApi extends EntityApi<PersonFaceDataService, PersonFaceData, String> {

    @Inject
    private Map<String, FaceRecognitionService> faceRecognitionServices;

    @RequestMapping("/sync-page")
    public Map<String, Object> syncPage(@RequestParam Map<String, Object> params, String service) {
        if (!faceRecognitionServices.containsKey(service)) {
            throw new BusinessException("service.not_found", service);
        }
        return faceRecognitionServices.get(service).syncPersons(params);
    }

    @RequestMapping("/sync-one")
    public Map<String, Object> syncOne(@RequestParam Map<String, Object> params, String service) {
        if (!faceRecognitionServices.containsKey(service)) {
            throw new BusinessException("service.not_found", service);
        }
        return faceRecognitionServices.get(service).syncPerson(params);
    }

}
