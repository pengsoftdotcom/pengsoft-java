package com.pengsoft.system.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.QSystemParam;
import com.pengsoft.system.domain.SystemParam;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link SystemParam} based on JPA
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface SystemParamRepository extends EntityRepository<QSystemParam, SystemParam, String> {

    /**
     * 根据编码查询单个{@link SystemParam}
     * 
     * @param code 编码
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    Optional<SystemParam> findOneByCode(@NotBlank String code);

}
