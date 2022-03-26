package com.pengsoft.system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.pengsoft.security.domain.User;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.Captcha;
import com.pengsoft.system.repository.CaptchaRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CaptchaServiceImpl extends EntityServiceImpl<CaptchaRepository, Captcha, String>
        implements CaptchaService {

    private static final int CAPTCHA_GENERATION_INTERVAL = 60;

    private static final int CAPTCHA_GENERATION_MAX_COUNT = 5;

    private static final int CAPTCHA_EXPIRATION = 300;

    @Override
    public Captcha generate(User user) {
        List<Captcha> captchas = ((CaptchaRepository) getRepository())
                .findAllByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(user.getId(),
                        DateUtils.currentDate().atStartOfDay());
        if (captchas.size() >= CAPTCHA_GENERATION_MAX_COUNT) {
            throw new BusinessException("captcha.generate.exceeded", Integer.valueOf(5));
        }

        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        if (CollectionUtils.isNotEmpty(captchas)) {
            Captcha captcha1 = captchas.get(0);
            LocalDateTime allowAt = captcha1.getCreatedAt().plus(CAPTCHA_GENERATION_INTERVAL, ChronoUnit.SECONDS);
            if (allowAt.isAfter(currentDateTime))
                throw new BusinessException("captcha.generate.forbidden",
                        Long.valueOf(Duration.between(currentDateTime, allowAt).toSeconds()));
            if (captcha1.getExpiredAt().isAfter(currentDateTime)) {
                captcha1.setExpiredAt(DateUtils.currentDateTime().plus(CAPTCHA_EXPIRATION, ChronoUnit.SECONDS));
                return (Captcha) save(captcha1);
            }
        }
        Captcha captcha = new Captcha();
        captcha.setUser(user);
        captcha.setCode(RandomStringUtils.randomNumeric(6));
        captcha.setExpiredAt(DateUtils.currentDateTime().plus(CAPTCHA_EXPIRATION, ChronoUnit.SECONDS));
        return save(captcha);
    }

    @Override
    public boolean isValid(User user, String code) {
        List<Captcha> captchas = ((CaptchaRepository) getRepository())
                .findAllByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(user.getId(),
                        DateUtils.currentDate().atStartOfDay());
        if (CollectionUtils.isNotEmpty(captchas)) {
            Captcha captcha = captchas.get(0);
            if (captcha.getExpiredAt().isAfter(DateUtils.currentDateTime()) &&
                    StringUtils.equals(captcha.getCode(), code)) {
                delete(captchas);
                return true;
            }
        }
        return false;
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by(Sort.Direction.DESC, new String[] { "createdAt" });
    }

}
