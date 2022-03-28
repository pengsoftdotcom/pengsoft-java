package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.DictionaryItem;

public interface DictionaryItemService extends TreeEntityService<DictionaryItem, String> {

    List<DictionaryItem> findAllByTypeCode(@NotBlank String code);

    Optional<DictionaryItem> findOneByTypeCodeAndParentAndCode(@NotBlank String typeCode, DictionaryItem parent,
            @NotBlank String code);

}
