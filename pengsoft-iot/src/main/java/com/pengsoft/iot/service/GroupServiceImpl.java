package com.pengsoft.iot.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.pengsoft.iot.domain.Group;
import com.pengsoft.iot.domain.QGroup;
import com.pengsoft.iot.repository.GroupRepository;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.QueryDslUtils;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.util.FieldUtils;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Group} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class GroupServiceImpl extends TreeEntityServiceImpl<GroupRepository, Group, String> implements GroupService {

    @Override
    public List<Group> findAll(Predicate predicate, Sort sort) {
        return super.findAll(exchange(predicate), sort);
    }

    @Override
    public List<Group> findAllByParent(Predicate predicate, Sort sort) {
        return super.findAllByParent(exchange(predicate), sort);
    }

    @Override
    public List<Group> findAllExcludeSelfAndItsChildren(Group self, Predicate predicate, Sort sort) {
        return super.findAllExcludeSelfAndItsChildren(self, exchange(predicate), sort);
    }

    @Override
    public List<Group> findAllExcludeSelfAndItsChildrenByParent(Group self, Predicate predicate, Sort sort) {
        return super.findAllExcludeSelfAndItsChildrenByParent(self, exchange(predicate), sort);
    }

    @Override
    public Optional<Group> findOne(Predicate predicate) {
        return super.findOne(exchange(predicate));
    }

    @Override
    public Page<Group> findPage(Predicate predicate, Pageable pageable) {
        return super.findPage(exchange(predicate), pageable);
    }

    @Override
    public boolean exists(Predicate predicate) {
        return super.exists(exchange(predicate));
    }

    @Override
    public long count(Predicate predicate) {
        return super.count(exchange(predicate));
    }

    /**
     * cause sql error for some unknown resean the alias is not correct, so exchange
     * it.
     */
    private Predicate exchange(Predicate predicate) {
        if (QueryDslUtils.isNotBlank(predicate)) {
            final var sql = predicate.toString();
            final var root = QGroup.group;
            final var builder = new BooleanBuilder();
            Arrays.stream(sql.split("&&")).map(String::trim).map(expession -> {
                if (expession.contains(" = ")) {
                    return expession.split(" = ");
                } else if (expession.contains(" in ")) {
                    return expession.split(" in ");
                } else {
                    throw new IllegalArgumentException("unrecognized expression: " + expession);
                }
            }).forEach(kv -> {
                var field = kv[0].split("\\.")[1];
                var value = kv[1];
                try {
                    var path = (StringPath) FieldUtils.getField(QGroup.class, field).get(root);
                    if (value.startsWith("[") && value.endsWith("]")) {
                        builder.and(path.in(Arrays.stream(StringUtils.substringBetween(value, "[", "]").split(","))
                                .map(String::trim).toList()));
                    } else {
                        builder.and(path.eq(value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            predicate = builder;
        }
        return predicate;
    }

}
