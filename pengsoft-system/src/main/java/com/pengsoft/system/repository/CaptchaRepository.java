package com.pengsoft.system.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.repository.EntityRepository;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.domain.QCaptcha;
import com.querydsl.core.types.dsl.TemporalExpression;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends EntityRepository<QCaptcha, Captcha, String> {

    @Override
    default void customize(QuerydslBindings bindings, QCaptcha root) {
        EntityRepository.super.customize(bindings, root);
        bindings.bind(root.expiredAt).first(TemporalExpression::before);
    }

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }, forCounting = false)
    List<Captcha> findAllByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(@NotBlank String userId,
            @NotNull LocalDateTime createdAt);
}
