package com.pengsoft.ss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.repository.SafetyTrainingParticipantRepository;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.DictionaryItem;

import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * The implementer of
 * {@link SafetyTrainingParticipantService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyTrainingParticipantServiceImpl extends
        EntityServiceImpl<SafetyTrainingParticipantRepository, SafetyTrainingParticipant, String>
        implements SafetyTrainingParticipantService {

    @Override
    public void confirm(SafetyTrainingParticipant participant, DictionaryItem status,
            String reason) {
        if (participant.getConfirmedAt() != null) {
            throw new BusinessException("training.confirm.already");
        }
        if (StringUtils.notEquals(SecurityUtilsExt.getPersonId(), participant.getStaff().getPerson().getId())) {
            throw new AccessDeniedException("not allowd");
        }
        participant.setStatus(status);
        participant.setReason(reason);
        participant.setConfirmedAt(DateUtils.currentDateTime());
        save(participant);
    }

    @Override
    public List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime) {
        return getRepository().statistic(projectIds, startTime, endTime);
    }

}
