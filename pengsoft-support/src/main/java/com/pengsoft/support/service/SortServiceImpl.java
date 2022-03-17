package com.pengsoft.support.service;

import javax.inject.Inject;

import com.pengsoft.support.domain.Sortable;
import com.pengsoft.support.exception.InvalidConfigurationException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

/**
 * The default implementer of {@link SortService}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class SortServiceImpl implements SortService {

    @Inject
    private Repositories repositories;

    @Override
    public <T extends Sortable> void sort(T entity) {
        final var repository = getRepository(entity.getClass());
        repository.save(entity);
    }

    @SuppressWarnings("unchecked")
    private <T extends Sortable> CrudRepository<T, String> getRepository(Class<?> entityClass) {
        return repositories.getRepositoryFor(entityClass).map(CrudRepository.class::cast)
                .orElseThrow(() -> new InvalidConfigurationException("No repository for " + entityClass.getName()));
    }

}
