package com.pengsoft.system.service;

import java.util.Optional;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.repository.DictionaryTypeRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class DictionaryTypeServiceImpl extends EntityServiceImpl<DictionaryTypeRepository, DictionaryType, String>
        implements DictionaryTypeService {

    @Override
    public DictionaryType save(DictionaryType type) {
        findOneByCode(type.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, type)) {
                throw getExceptions().constraintViolated("code", "exists", type.getCode());
            }
        });
        return super.save(type);
    }

    public Optional<DictionaryType> findOneByCode(String code) {
        return getRepository().findOneByCode(code);
    }
}
