package com.pengsoft.ss.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
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

    /**
     * 按建筑项目、参与状态统计
     * 
     * @param projectIds 建筑项目ID列表
     * @param startTime  开始时间
     * @param endTime    结束时间
     */
    List<Map<String, Object>> statistic(@NotEmpty List<String> projectIds, @NotNull LocalDateTime startTime,
            @NotNull LocalDateTime endTime);

}
