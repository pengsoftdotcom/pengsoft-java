package com.pengsoft.ss.api;

import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.service.SafetyTrainingParticipantService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.DictionaryItem;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link SafetyTrainingParticipant}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/ss/safety-training-participant")
public class SafetyTrainingParticipantApi extends
        EntityApi<SafetyTrainingParticipantService, SafetyTrainingParticipant, String> {

    @PutMapping("confirm")
    public void confirm(@RequestParam("id") SafetyTrainingParticipant participant,
            @RequestParam("status.id") DictionaryItem status, String reason) {
        getService().confirm(participant, status, reason);
    }

}
