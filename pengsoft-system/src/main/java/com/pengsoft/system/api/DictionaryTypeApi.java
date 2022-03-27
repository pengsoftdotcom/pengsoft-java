package com.pengsoft.system.api;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.DictionaryType;
import com.pengsoft.system.service.DictionaryTypeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/dictionary-type")
public class DictionaryTypeApi extends EntityApi<DictionaryTypeService, DictionaryType, String> {
}
