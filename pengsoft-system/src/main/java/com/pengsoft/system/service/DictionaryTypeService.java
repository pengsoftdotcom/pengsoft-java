package com.pengsoft.system.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.DictionaryType;

public interface DictionaryTypeService extends EntityService<DictionaryType, String> {

    Optional<DictionaryType> findOneByCode(@NotBlank String code);

}
