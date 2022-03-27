package com.pengsoft.system.api;

import java.util.List;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.TreeEntityApi;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.DictionaryItemService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/dictionary-item")
public class DictionaryItemApi extends TreeEntityApi<DictionaryItemService, DictionaryItem, String> {

    @GetMapping({ "find-all-by-type-code" })
    public List<DictionaryItem> findAllByTypeCode(String code) {
        return ((DictionaryItemService) getService()).findAllByTypeCode(code);
    }

}
