package com.pengsoft.system.service;

import java.util.List;
import java.util.Optional;

import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.service.TreeEntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.repository.DictionaryItemRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class DictionaryItemServiceImpl extends TreeEntityServiceImpl<DictionaryItemRepository, DictionaryItem, String>
        implements DictionaryItemService {

    @Override
    public DictionaryItem save(DictionaryItem item) {
        findOneByTypeAndParentAndCode(item.getType(), (DictionaryItem) item.getParent(), item.getCode())
                .ifPresent(source -> {
                    if (EntityUtils.notEquals(source, item)) {
                        throw getExceptions().constraintViolated("code", "exists", item.getCode());
                    }
                });
        return super.save(item);
    }

    @Override
    public List<DictionaryItem> findAllByTypeCode(String code) {
        return getRepository().findAllByTypeCodeOrderByCode(code);
    }

    @Override
    public Optional<DictionaryItem> findOneByTypeAndParentAndCode(DictionaryType type, DictionaryItem parent,
            String code) {
        String parentId = Optional.<DictionaryItem>ofNullable(parent).map(EntityImpl::getId).orElse(null);
        return getRepository().findOneByTypeIdAndParentIdAndCode(type.getId(), parentId, code);
    }

}
