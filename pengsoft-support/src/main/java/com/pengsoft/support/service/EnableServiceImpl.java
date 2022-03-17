package com.pengsoft.support.service;

import javax.inject.Inject;

import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.exception.InvalidConfigurationException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

/**
 * The default implementer of {@link EnableService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class EnableServiceImpl implements EnableService {

    @Inject
    private Repositories repositories;

    @Override
    public <T extends Enable> void enable(final T entity) {
        entity.setEnabled(true);
        getRepository(entity.getClass()).save(entity);
    }

    @Override
    public <T extends Enable> void disable(final T entity) {
        entity.setEnabled(false);
        getRepository(entity.getClass()).save(entity);
    }

    @SuppressWarnings("unchecked")
    private <T extends Enable> CrudRepository<T, String> getRepository(Class<?> entityClass) {
        return repositories.getRepositoryFor(entityClass).map(CrudRepository.class::cast)
                .orElseThrow(() -> new InvalidConfigurationException("No repository for " + entityClass.getName()));
    }

}
