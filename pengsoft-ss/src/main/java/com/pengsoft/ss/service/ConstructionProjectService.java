package com.pengsoft.ss.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Staff;
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
     * 根据监控单位负责人ID查询所有工程项目列表
     * 
     * @param ruManager 监控单位负责人
     */
    List<ConstructionProject> findAllByRuManager(@NotNull Staff ruManager);

    /**
     * 根据建设单位负责人ID查询所有工程项目列表
     * 
     * @param ownerManager 建设单位负责人
     */
    List<ConstructionProject> findAllByOwnerManager(@NotNull Staff ownerManager);

    /**
     * 根据监理单位负责人ID查询所有工程项目列表
     * 
     * @param suManager 监理单位负责人
     */
    List<ConstructionProject> findAllBySuManager(@NotNull Staff suManager);

    /**
     * 根据施工单位负责人ID查询所有工程项目列表
     * 
     * @param buManager 施工单位负责人
     */
    List<ConstructionProject> findAllByBuManager(@NotNull Staff buManager);

    /**
     * 按状态统计
     * 
     * @param department 项目部
     */
    List<Map<String, Object>> statisticByStatus(@NotNull Department department);

}
