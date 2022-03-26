package com.pengsoft.system.service;

import java.util.Optional;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.CompositeMessageTemplate;
import com.pengsoft.system.repository.CompositeMessageTemplateRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link CompositeMessageTemplateService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class CompositeMessageTemplateServiceImpl
        extends EntityServiceImpl<CompositeMessageTemplateRepository, CompositeMessageTemplate, String>
        implements CompositeMessageTemplateService {

    @Override
    public CompositeMessageTemplate save(CompositeMessageTemplate target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public Optional<CompositeMessageTemplate> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }

}
