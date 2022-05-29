package com.pengsoft.system.service;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.repository.AssetRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AssetServiceImpl extends EntityServiceImpl<AssetRepository, Asset, String> implements AssetService {

    @Inject
    private StorageService storageService;

    @Override
    public void delete(Asset asset) {
        super.delete(asset);
        this.storageService.delete(asset);
    }

    @Override
    public Asset download(@NotNull Asset asset) {
        return storageService.download(asset);
    }

}
