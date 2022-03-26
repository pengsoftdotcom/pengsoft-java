package com.pengsoft.system.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pengsoft.security.domain.User;
import com.pengsoft.security.service.UserService;
import com.pengsoft.support.json.ObjectMapper;
import com.pengsoft.system.service.CaptchaService;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Named
public class CaptchaVerificationFilter extends OncePerRequestFilter {

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
        String username = req.getParameter("username");
        String captcha = req.getParameter("captcha");
        Optional<User> optional = this.userService.findOneByMobile(username);
        if (optional.isPresent()) {
            if (!this.captchaService.isValid(optional.get(), captcha)) {
                errors.put("captcha", List
                        .of(this.messageSource.getMessage("captcha_invalid", null, LocaleContextHolder.getLocale())));
            }
        } else {
            errors.put("username",
                    List.of(this.messageSource.getMessage("not_exists", null, LocaleContextHolder.getLocale())));
        }
        if (!errors.isEmpty()) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            this.objectMapper.writeValue(res.getWriter(), errors);
            return;
        }
        chain.doFilter((ServletRequest) req, (ServletResponse) res);
    }

}
