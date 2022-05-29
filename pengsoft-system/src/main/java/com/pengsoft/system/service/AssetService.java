package com.pengsoft.system.service;

import javax.validation.constraints.NotNull;

import com.pengsoft.support.service.EntityService;
import com.pengsoft.system.domain.Asset;

public interface AssetService extends EntityService<Asset, String> {

    Asset download(@NotNull Asset asset);

}
