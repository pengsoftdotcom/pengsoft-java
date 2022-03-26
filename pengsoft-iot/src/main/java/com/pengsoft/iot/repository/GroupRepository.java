package com.pengsoft.iot.repository;

import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.QGroup;
import com.pengsoft.support.repository.TreeEntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Group} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface GroupRepository extends TreeEntityRepository<QGroup, Group, String>, OwnedExtRepository {

    /**
     * Returns an {@link Optional} of a {@link Group} with the given code.
     *
     * @param code {@link Group}'s code
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Group> findOneByCode(@NotBlank String code);

}
