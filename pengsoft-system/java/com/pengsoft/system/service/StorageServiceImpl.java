package com.pengsoft.system.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

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
 * Aliyun object storage service.
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
        return properties.getPublicAccessPathPrefix();
    }

    public String getLockedAccessPathPrefix() {
        return properties.getLockedAccessPathPrefix();
    }

    private OSSClient getPublicBucketClient() {
        return (OSSClient) new OSSClientBuilder().build(properties.getPublicBucketEndpoint(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

    private OSS getLockedBucketClient() {
        return new OSSClientBuilder().build(properties.getLockedBucketEndpoint(), properties.getAccessKeyId(),
                properties.getAccessKeySecret());
    }

    private OSS getClient(final boolean locked) {
        return locked ? getLockedBucketClient() : getPublicBucketClient();
    }

    private String getBucket(final boolean locked) {
        return locked ? properties.getLockedBucket() : properties.getPublicBucket();
    }

    private String getAccessPathPrefix(final boolean locked) {
        String prefix = locked ? getLockedAccessPathPrefix() : getPublicAccessPathPrefix();
        if (!prefix.endsWith(StringUtils.FILE_SEPARATOR)) {
            prefix += StringUtils.FILE_SEPARATOR;
        }
        return prefix;
    }

    @SneakyThrows
    @Override
    public Asset upload(@NotNull final MultipartFile file, @NotNull final boolean locked, final int width,
            final int height) {
        var is = file.getInputStream();
        var os = new ByteArrayOutputStream();
        final var bucket = getBucket(locked);
        final var asset = new Asset();
        final var contentType = file.getContentType();
        final var parts = Optional.ofNullable(contentType).orElse("unkown/unkown").split("/");
        if ("image".equals(parts[0])) {
            Thumbnails.of(is).outputFormat(parts[1]).size(width, height).toOutputStream(os);
            final var bytes = os.toByteArray();
            is = new ByteArrayInputStream(bytes);
            asset.setContentLength(bytes.length);
        } else {
            asset.setContentLength(file.getSize());
        }
        asset.setContentType(contentType);
        asset.setLocked(locked);
        asset.setOriginalName(file.getOriginalFilename());
        asset.setPresentName(getPresentName(asset));
        final var accessPathPrefix = new StringBuilder(getAccessPathPrefix(locked));
        final var accessPathSuffix = getAccessPathSuffix(asset);
        asset.setAccessPath(accessPathPrefix.append(accessPathSuffix).toString());
        asset.setStoragePath(bucket + StringUtils.GLOBAL_SEPARATOR + accessPathSuffix.toString());

        final var client = getClient(locked);
        try {
            client.putObject(bucket, accessPathSuffix.toString(), is);
        } catch (final Exception e) {
            throw new BusinessException("asset.upload.failed", e.getMessage());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    private String getPresentName(final Asset asset) {
        return UUID.randomUUID().toString() + StringUtils.PACKAGE_SEPARATOR
                + StringUtils.substringAfterLast(asset.getOriginalName(), StringUtils.PACKAGE_SEPARATOR)
                        .toLowerCase();
    }

    private StringBuilder getAccessPathSuffix(final Asset asset) {
        final var accessPathSuffix = new StringBuilder();
        accessPathSuffix.append("user");
        accessPathSuffix.append(StringUtils.FILE_SEPARATOR);
        accessPathSuffix.append(SecurityUtils.getUserId());
        accessPathSuffix.append(StringUtils.FILE_SEPARATOR);
        accessPathSuffix.append(asset.getPresentName());
        return accessPathSuffix;
    }

    @Override
    public Asset download(@NotNull final Asset asset) {
        final var client = getClient(asset.isLocked());
        try {
            final var bucket = getBucket(asset.isLocked());
            String key = asset.getStoragePath().split(StringUtils.GLOBAL_SEPARATOR)[1];
            asset.setData(IOUtils.readStreamAsByteArray(client.getObject(bucket, key).getObjectContent()));
        } catch (Exception e) {
            throw new BusinessException("asset.download.failed", HttpStatus.NOT_FOUND, asset.getStoragePath());
        } finally {
            client.shutdown();
        }
        return asset;
    }

    @Override
    public void delete(@NotNull final Asset asset) {
        final var client = getClient(asset.isLocked());
        try {
            final var bucket = getBucket(asset.isLocked());
            final var ossKey = new StringBuilder();
            ossKey.append("user");
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(SecurityUtils.getUserId());
            ossKey.append(StringUtils.FILE_SEPARATOR);
            ossKey.append(asset.getPresentName());
            client.deleteObject(bucket, ossKey.toString());
        } finally {
            client.shutdown();
        }
    }

}
