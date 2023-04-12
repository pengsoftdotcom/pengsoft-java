package com.pengsoft.security.repository;

import com.pengsoft.security.domain.QRoleAuthority;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link RoleAuthority} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface RoleAuthorityRepository extends EntityRepository<QRoleAuthority, RoleAuthority, String> {

}
