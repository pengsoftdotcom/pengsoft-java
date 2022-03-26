package com.pengsoft.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Asset;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    default Asset upload(@NotNull MultipartFile file, int width, int height) {
        return upload(file, false, width, height);
    }

    default List<Asset> upload(@NotEmpty List<MultipartFile> files, boolean locked, int width, int height) {
        ArrayList<Asset> assets = new ArrayList<>();
        files.forEach(file -> assets.add(upload(file, locked, width, height)));
        return assets;
    }

    default List<Asset> upload(List<MultipartFile> files, int width, int height) {
        return upload(files, false, width, height);
    }

    default List<Asset> download(@NotEmpty List<Asset> assets) {
        return assets.stream().map(this::download).toList();
    }

    default void delete(@NotEmpty List<Asset> assets) {
        assets.forEach(this::delete);
    }

    Asset upload(@NotNull MultipartFile file, boolean locked, int width, int height);

    Asset download(@NotNull Asset asset);

    void delete(@NotNull Asset asset);

}
