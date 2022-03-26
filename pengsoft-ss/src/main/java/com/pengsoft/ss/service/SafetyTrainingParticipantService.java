package com.pengsoft.ss.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.DictionaryItem;

/**
 * The service interface of
 * {@link SafetyTrainingParticipant}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface SafetyTrainingParticipantService
        extends EntityService<SafetyTrainingParticipant, String> {

    void confirm(@NotNull SafetyTrainingParticipant participant, @NotNull DictionaryItem status, String reason);

}
