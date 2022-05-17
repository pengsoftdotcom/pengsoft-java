package com.pengsoft.system.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.IOUtils;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.config.properties.StorageServiceProperties;
import com.pengsoft.system.domain.Asset;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 基于阿里云OSS实现的存储服务
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class StorageServiceImpl implements StorageService {

    private StorageServiceProperties properties;

    public StorageServiceImpl(StorageServiceProperties properties) {
        this.properties = properties;
    }

    public String getPublicAccessPathPrefix() {
        return this.properties.getPublicAccessPathPrefix();
    }

    public String getLockedAccessPathPrefix() {
        return this.properties.getLockedAccessPathPrefix();
    }

    private OSSClient getPublicBucketClient() {
        return (OSSClient) (new OSSClientBuilder()).build(this.properties.getPublicBucketEndpoint(), this.properties
                .getAccessKeyId(), this.properties.getAccessKeySecret());
    }

    private OSS getLockedBucketClient() {
        return (new OSSClientBuilder()).build(this.properties.getLockedBucketEndpoint(),
                this.properties.getAccessKeyId(), this.properties
                        .getAccessKeySecret());
    }

    private OSS getClient(boolean locked) {
        return locked ? getLockedBucketClient() : (OSS) getPublicBucketClient();
    }

    private String getBucket(boolean locked) {
        return locked ? this.properties.getLockedBucket() : this.properties.getPublicBucket();
    }

    private String getAccessPathPrefix(boolean locked) {
        String prefix = locked ? getLockedAccessPathPrefix() : getPublicAccessPathPrefix();
        if (!prefix.endsWith("/")) {
            prefix = prefix + "/";
        }
        return prefix;
    }

    @SneakyThrows
    @Override
    public Asset upload(MultipartFile file, boolean locked, boolean zoomed, int width, int height) {
        var is = file.getInputStream();
        final var os = new ByteArrayOutputStream();
        final var bucket = getBucket(locked);
        final var asset = new Asset();
        final var contentType = file.getContentType();
        final var parts = Optional.ofNullable(contentType).orElse("unkown/unkown").split("/");
        final var isImage = "image".equals(parts[0]);
        final var extension = parts[1];
        asset.setContentLength(file.getSize());
        if (isImage && zoomed) {
            final var originalImage = ImageIO.read(is);
            final var originalWidth = originalImage.getWidth();
            final var originalHeight = originalImage.getHeight();
            if (originalWidth > width || originalHeight > height) {
                Thumbnails.of(originalImage).outputFormat(extension).size(width, height).toOutputStream(os);
            } else {
                ImageIO.write(originalImage, extension, os);
            }
            final var bytes = os.toByteArray();
            is = new ByteArrayInputStream(bytes);
            asset.setContentLength(bytes.length);
        }
        asset.setContentType(contentType);
        asset.setLocked(locked);
        asset.setOriginalName(file.getOriginalFilename());
        asset.setPresentName(getPresentName(asset, isImage, extension));
        StringBuilder accessPathPrefix = new StringBuilder(getAccessPathPrefix(locked));
        StringBuilder accessPathSuffix = getAccessPathSuffix(asset);
        asset.setAccessPath(accessPathPrefix.append(accessPathSuffix).toString());
        asset.setStoragePath(bucket + "::" + accessPathSuffix);

        OSS client = getClient(locked);
        try {
            client.putObject(bucket, accessPathSuffix.toString(), is);
        } catch (Exception e) {
            throw new BusinessException("asset.upload.failed", e.getMessage());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    private String getPresentName(Asset asset, boolean isImage, String extension) {
        final var presentName = new StringBuilder();
        presentName.append(UUID.randomUUID().toString());
        presentName.append(StringUtils.PACKAGE_SEPARATOR);
        if (isImage) {
            presentName.append(extension);
        } else {
            presentName.append(StringUtils.substringAfterLast(asset.getOriginalName(), StringUtils.PACKAGE_SEPARATOR));
        }
        return presentName.toString();
    }

    private StringBuilder getAccessPathSuffix(Asset asset) {
        StringBuilder accessPathSuffix = new StringBuilder();
        accessPathSuffix.append("user");
        accessPathSuffix.append("/");
        accessPathSuffix.append(SecurityUtils.getUserId());
        accessPathSuffix.append("/");
        accessPathSuffix.append(asset.getPresentName());
        return accessPathSuffix;
    }

    @Override
    public Asset download(Asset asset) {
        OSS client = getClient(asset.isLocked());
        try {
            String bucket = getBucket(asset.isLocked());
            String key = asset.getStoragePath().split("::")[1];
            asset.setData(IOUtils.readStreamAsByteArray(client.getObject(bucket, key).getObjectContent()));
        } catch (Exception e) {
            throw new BusinessException("asset.download.failed", HttpStatus.NOT_FOUND, asset.getStoragePath());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    @Override
    public void delete(Asset asset) {
        OSS client = getClient(asset.isLocked());
        try {
            String bucket = getBucket(asset.isLocked());
            StringBuilder ossKey = new StringBuilder();
            ossKey.append("user");
            ossKey.append("/");
            ossKey.append(SecurityUtils.getUserId());
            ossKey.append("/");
            ossKey.append(asset.getPresentName());
            client.deleteObject(bucket, ossKey.toString());
        } finally {
            client.shutdown();
        }
    }

}
