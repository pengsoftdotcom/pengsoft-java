package com.pengsoft.ss.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.repository.ConstructionProjectRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

/**
 * The implementer of {@link ConstructionProjectService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ConstructionProjectServiceImpl
        extends EntityServiceImpl<ConstructionProjectRepository, ConstructionProject, String>
        implements ConstructionProjectService {

    @Override
    public ConstructionProject save(final ConstructionProject target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        if (StringUtils.isBlank(target.getShortName())) {
            target.setShortName(target.getName());
        }
        return super.save(target);
    }

    @Override
    public Optional<ConstructionProject> findOneByCode(@NotBlank final String code) {
        return getRepository().findOneByCode(code);
    }

    @Override
    public Optional<ConstructionProject> findOneByName(@NotBlank String name) {
        return getRepository().findOneByName(name);
    }

    @Override
    public List<ConstructionProject> findAllByRuManager(@NotNull Staff ruManager) {
        return getRepository().findAllByRuManagerDepartmentId(ruManager.getDepartment().getId());
    }

    @Override
    public List<ConstructionProject> findAllByOwnerManager(@NotNull Staff ownerManager) {
        return getRepository().findAllByOwnerManagerDepartmentId(ownerManager.getDepartment().getId());
    }

    @Override
    public List<ConstructionProject> findAllBySuManager(@NotNull Staff suManager) {
        return getRepository().findAllBySuManagerDepartmentId(suManager.getDepartment().getId());
    }

    @Override
    public List<ConstructionProject> findAllByBuManager(@NotNull Staff buManager) {
        return getRepository().findAllByBuManagerDepartmentId(buManager.getDepartment().getId());
    }

    @Override
    public List<Map<String, Object>> statisticByStatus(Department department) {
        return getRepository().statisticByStatus(department.getId());
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Order.asc("status.code"), Order.desc("startedAt"));
    }

}
