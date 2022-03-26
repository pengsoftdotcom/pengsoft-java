package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.service.TreeEntityService;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;

public interface DictionaryItemService extends TreeEntityService<DictionaryItem, String> {

    List<DictionaryItem> findAllByTypeCode(@NotBlank String code);

    Optional<DictionaryItem> findOneByTypeAndParentAndCode(@NotNull DictionaryType type, DictionaryItem parent,
            @NotBlank String code);

}
