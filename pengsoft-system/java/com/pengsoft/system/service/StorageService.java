package com.pengsoft.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Asset;

import org.springframework.web.multipart.MultipartFile;

/**
 * The storage service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StorageService {

    /**
     * Upload a file.
     *
     * @param file   {@link MultipartFile}
     * @param locked Whether the asset's public reading is locked.
     * @param width  If the asset is a image, limit the width.
     * @param height If the asset is a image, limit the height.
     * @return The uploaded {@link Asset}.
     */
    Asset upload(@NotNull MultipartFile file, boolean locked, final int width, final int height);

    /**
     * Upload a public read and private write file.
     *
     * @param file   {@link MultipartFile}
     * @param width  If the asset is a image, limit the width.
     * @param height If the asset is a image, limit the height.
     * @return The uploaded {@link Asset}
     */
    default Asset upload(@NotNull final MultipartFile file, final int width, final int height) {
        return upload(file, false, width, height);
    }

    /**
     * Upload multiple files.
     *
     * @param files  The multiple {@link MultipartFile}s.
     * @param locked Whether the asset's public reading is locked.
     * @param width  If the asset is a image, limit the width.
     * @param height If the asset is a image, limit the height.
     * @return The uploaded {@link Asset}s
     */
    default List<Asset> upload(@NotEmpty final List<MultipartFile> files, final boolean locked, final int width,
            final int height) {
        final var assets = new ArrayList<Asset>();
        files.forEach(file -> assets.add(upload(file, locked, width, height)));
        return assets;
    }

    /**
     * Upload multiple public read and private write files.
     *
     * @param files  The multiple {@link MultipartFile}s.
     * @param width  If the asset is a image, limit the width.
     * @param height If the asset is a image, limit the height.
     * @return The uploaded {@link Asset}s
     */
    default List<Asset> upload(final List<MultipartFile> files, final int width, final int height) {
        return upload(files, false, width, height);
    }

    /**
     * Download the asset, read and set the input stream from the access path of the
     * asset.
     *
     * @param asset {@link Asset}
     * @return The asset that after input stream set.
     */
    Asset download(@NotNull Asset asset);

    /**
     * Download multiple assets.
     *
     * @param assets The {@link Asset}s
     * @return The assets that after input stream set.
     */
    default List<Asset> download(@NotEmpty final List<Asset> assets) {
        return assets.stream().map(this::download).toList();
    }

    /**
     * Delete the asset from the database and the object storage service.
     *
     * @param asset {@link Asset}
     */
    void delete(@NotNull Asset asset);

    /**
     * Delete multiple asset from the database and the object storage service.
     *
     * @param assets The {@link Asset}s
     */
    default void delete(@NotEmpty final List<Asset> assets) {
        assets.forEach(this::delete);
    }

}
