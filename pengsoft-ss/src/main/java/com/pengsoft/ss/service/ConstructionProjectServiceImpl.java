package com.pengsoft.ss.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.repository.ConstructionProjectRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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

}
