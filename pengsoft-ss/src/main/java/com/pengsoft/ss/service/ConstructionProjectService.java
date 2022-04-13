package com.pengsoft.ss.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link ConstructionProject}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ConstructionProjectService extends EntityService<ConstructionProject, String> {

    /**
     * Returns an {@link Optional} of a {@link ConstructionProject} with the given
     * code.
     *
     * @param code {@link ConstructionProject}'s code
     */
    Optional<ConstructionProject> findOneByCode(@NotBlank String code);

    /**
     * Returns an {@link Optional} of a {@link ConstructionProject} with the given
     * name.
     * 
     * @param name {@link ConstructionProject}'s name
     */
    Optional<ConstructionProject> findOneByName(@NotBlank String name);

    /**
     * 按状态统计
     */
    List<Map<String, Object>> statisticByStatus();

}
