package com.pengsoft.system.service;

import java.util.Optional;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.SystemParam;
import com.pengsoft.system.repository.SystemParamRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class SystemParamServiceImpl extends EntityServiceImpl<SystemParamRepository, SystemParam, String>
        implements SystemParamService {

    @Override
    public SystemParam save(final SystemParam target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public Optional<SystemParam> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }

}
