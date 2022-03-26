package com.pengsoft.system.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.StorageService;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * The web api of {@link Asset}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(Constant.API_PREFIX + "/system/asset")
public class AssetApi extends EntityApi<AssetService, Asset, String> {

    private static final String SERVICE_NAME = "storageService";

    private StorageService storageService;

    public AssetApi(ApplicationContext context) {
        if (context.containsBean(SERVICE_NAME)) {
            this.storageService = context.getBean(SERVICE_NAME, StorageService.class);
        } else {
            log.warn("storage service is not up");
        }
    }

    @PostMapping("upload")
    public List<Asset> upload(@RequestParam("file") final List<MultipartFile> files,
            @RequestParam(defaultValue = "false") final boolean locked,
            @RequestParam(defaultValue = "600") final int width, @RequestParam(defaultValue = "600") final int height) {
        return getService().save(storageService.upload(files, locked, width, height));
    }

    @GetMapping("download")
    public String download(@RequestParam("id") Asset asset, @RequestParam(defaultValue = "600") final int width,
            @RequestParam(defaultValue = "600") final int height) {
        asset = storageService.download(asset);
        if (asset.getContentType().startsWith("image")) {
            try (var os = new ByteArrayOutputStream(); var is = new ByteArrayInputStream(asset.getData())) {
                Thumbnails.of(is).outputFormat("jpg").size(width, height).toOutputStream(os);
                return "data:image/jpeg;base64," + Base64Utils.encodeToString(os.toByteArray());
            } catch (Exception e) {
                throw new BusinessException("asset.download.failed", e.getMessage());
            }
        } else {
            return Base64Utils.encodeToString(asset.getData());
        }
    }

}
