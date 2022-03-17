package com.pengsoft.support.service;

import javax.inject.Inject;

import com.pengsoft.support.domain.Trashable;
import com.pengsoft.support.exception.InvalidConfigurationException;
import com.pengsoft.support.util.DateUtils;

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
public class TrashServiceImpl implements TrashService {

    @Inject
    private Repositories repositories;

    @Override
    public <T extends Trashable> T trash(final T entity) {
        entity.setTrashedAt(DateUtils.currentDateTime());
        return getRepository(entity.getClass()).save(entity);
    }

    @Override
    public <T extends Trashable> T restore(final T entity) {
        entity.setTrashedAt(null);
        return getRepository(entity.getClass()).save(entity);
    }

    @SuppressWarnings("unchecked")
    private <T extends Trashable> CrudRepository<T, String> getRepository(Class<?> entityClass) {
        return repositories.getRepositoryFor(entityClass).map(CrudRepository.class::cast)
                .orElseThrow(() -> new InvalidConfigurationException("No repository for " + entityClass.getName()));
    }

}
