package com.pengsoft.system.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pengsoft.security.service.UserService;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.SystemParam;
import com.pengsoft.system.service.CaptchaService;
import com.pengsoft.system.service.SystemParamService;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Named
public class CaptchaVerificationFilter extends OncePerRequestFilter {

    private static final String PARAM_USERNAME = "username";

    private static final String PARAM_CAPTCHA = "captcha";

    @Inject
    private SystemParamService systemParamService;

    @Inject
    private UserService userService;

    @Inject
    private CaptchaService captchaService;

    @Inject
    private MessageSource messageSource;

    @Inject
    private ObjectMapper objectMapper;

    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        HashMap<String, List<String>> errors = new HashMap<>();
        String username = req.getParameter(PARAM_USERNAME);
        String captcha = req.getParameter(PARAM_CAPTCHA);
        userService.findOneByMobile(username).ifPresentOrElse(user -> {
            if (!captchaService.isValid(user, captcha)) {
                final var superCaptchaEnabled = (boolean) systemParamService.findOneByCode("super_captcha")
                        .map(SystemParam::getParam)
                        .map(param -> param.get("value")).map(Boolean.class::cast).orElse(false);
                if (!superCaptchaEnabled || StringUtils.notEquals(captcha, "ps")) {
                    errors.put(PARAM_CAPTCHA, List.of(
                            messageSource.getMessage("captcha_invalid", null, LocaleContextHolder.getLocale())));
                }
            }
        }, () -> errors.put(PARAM_USERNAME,
                List.of(messageSource.getMessage("not_exists", null, LocaleContextHolder.getLocale()))));

        if (!errors.isEmpty()) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            objectMapper.writeValue(res.getWriter(), errors);
            return;
        }
        chain.doFilter(req, res);
    }

}
