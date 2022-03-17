package com.pengsoft.iot.repository;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.QGroup;
import com.pengsoft.support.repository.TreeEntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Group} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface GroupRepository extends TreeEntityRepository<QGroup, Group, String>, OwnedExtRepository {

}
