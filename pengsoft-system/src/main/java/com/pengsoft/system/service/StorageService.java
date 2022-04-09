package com.pengsoft.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.system.domain.Asset;

import org.springframework.web.multipart.MultipartFile;

/**
 * 存储服务
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface StorageService {

    /**
     * 是否图片
     * 
     * @param contentType mime类型
     */
    default boolean isImage(String contentType) {
        final var parts = Optional.ofNullable(contentType).orElse("unkown/unkown").split("/");
        return "image".equals(parts[0]);
    }

    /**
     * 上传文件
     * 
     * @param file   {@link MultipartFile}
     * @param locked 是否锁定
     * @param zoomed 是否缩放
     * @param width  宽度
     * @param height 高度
     */
    Asset upload(@NotNull MultipartFile file, boolean locked, boolean zoomed, int width, int height);

    /**
     * 上传文件
     * 
     * @param file   {@link MultipartFile}集合
     * @param locked 是否锁定
     * @param zoomed 是否缩放
     * @param width  宽度
     * @param height 高度
     */
    default List<Asset> upload(@NotEmpty List<MultipartFile> files, boolean locked, boolean zoomed, int width,
            int height) {
        ArrayList<Asset> assets = new ArrayList<>();
        files.forEach(file -> assets.add(upload(file, locked, zoomed, width, height)));
        return assets;
    }

    /**
     * 下载文件
     * 
     * @param asset {@link Asset}
     */
    Asset download(@NotNull Asset asset);

    /**
     * 下载文件
     * 
     * @param assets {@link Asset}集合
     */
    default List<Asset> download(@NotEmpty List<Asset> assets) {
        return assets.stream().map(this::download).toList();
    }

    /**
     * 删除文件
     * 
     * @param asset {@link Asset}
     */
    void delete(@NotNull Asset asset);

    /**
     * 删除文件
     * 
     * @param assets {@link Asset}集合
     */
    default void delete(@NotEmpty List<Asset> assets) {
        assets.forEach(this::delete);
    }

}
