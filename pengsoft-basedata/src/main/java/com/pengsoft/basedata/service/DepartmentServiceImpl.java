package com.pengsoft.basedata.service;

import java.util.Optional;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.repository.DepartmentRepository;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link DepartmentService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class DepartmentServiceImpl extends TreeEntityServiceImpl<DepartmentRepository, Department, String>
        implements DepartmentService {

    @Override
    public Department save(final Department department) {
        final var organizationId = department.getOrganization().getId();
        final var parentId = Optional.ofNullable(department.getParent()).map(Department::getId).orElse(null);
        getRepository().findOneByOrganizationIdAndParentIdAndName(organizationId, parentId, department.getName())
                .ifPresent(source -> {
                    if (EntityUtils.notEquals(source, department)) {
                        throw getExceptions().constraintViolated("name", "exists", department.getName());
                    }
                });
        if (StringUtils.isBlank(department.getShortName())) {
            department.setShortName(department.getName());
        }
        return super.save(department);
    }

}
