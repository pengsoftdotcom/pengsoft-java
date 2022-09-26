package com.pengsoft.security.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.security.domain.Authority;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.domain.RoleAuthority;
import com.pengsoft.security.repository.RoleAuthorityRepository;
import com.pengsoft.security.repository.RoleRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.domain.Entity;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link RoleService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class RoleServiceImpl extends TreeEntityServiceImpl<RoleRepository, Role, String> implements RoleService {

    @Inject
    private RoleAuthorityRepository roleAuthorityRepository;

    @Override
    public Role save(final Role target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public <T extends Entity<ID>, ID extends Serializable> Role saveEntityAdmin(final Class<T> entityClass) {
        final var admin = createRoleIfNotExists(null, Role.ADMIN);
        final var moduleAdminCode = SecurityUtils.getModuleAdminRoleCode(entityClass);
        final var moduleAdmin = createRoleIfNotExists(admin, moduleAdminCode);
        final var entityAdminCode = SecurityUtils.getEntityAdminRoleCode(entityClass);
        return createRoleIfNotExists(moduleAdmin, entityAdminCode);
    }

    private Role createRoleIfNotExists(final Role parent, final String code) {
        final Optional<Role> optional = findOneByCode(code);
        if (optional.isEmpty()) {
            return super.save(new Role(parent, code));
        } else {
            return optional.get();
        }
    }

    @Override
    public void copyAuthorities(Role source, Role target) {
        final var authorities = source.getRoleAuthorities().stream().map(RoleAuthority::getAuthority).toList();
        grantAuthorities(target, authorities);
    }

    @Override
    public void grantAuthorities(final Role role, final List<Authority> authorities) {
        final var source = role.getRoleAuthorities();
        final var target = authorities.stream().map(authority -> new RoleAuthority(role, authority)).toList();
        final var deleted = source.stream()
                .filter(s -> target.stream().noneMatch(t -> EntityUtils.equals(s.getRole(), t.getRole())
                        && EntityUtils.equals(s.getAuthority(), t.getAuthority())))
                .toList();
        roleAuthorityRepository.deleteAll(deleted);
        source.removeAll(deleted);
        final var created = target.stream()
                .filter(t -> source.stream().noneMatch(s -> EntityUtils.equals(t.getRole(), s.getRole())
                        && EntityUtils.equals(t.getAuthority(), s.getAuthority())))
                .toList();
        roleAuthorityRepository.saveAll(created);
        source.addAll(created);
        super.save(role);
    }

    @Override
    public Optional<Role> findOneByCode(final String code) {
        return getRepository().findOneByCode(code);
    }

}
