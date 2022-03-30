package com.pengsoft.system.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import javax.inject.Inject;

import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.service.AssetService;
import com.pengsoft.system.service.StorageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@RestController
@RequestMapping(Constant.API_PREFIX + "/system/asset")
public class AssetApi extends EntityApi<AssetService, Asset, String> {

    @Inject
    private StorageService storageService;

    @PostMapping({ "upload" })
    public List<Asset> upload(@RequestParam("file") List<MultipartFile> files,
            @RequestParam(defaultValue = "false") boolean locked, @RequestParam(defaultValue = "600") int width,
            @RequestParam(defaultValue = "600") int height) {
        return getService().save(this.storageService.upload(files, locked, width, height));
    }

    @GetMapping({ "download" })
    public String download(@RequestParam("id") Asset asset, @RequestParam(defaultValue = "600") int width,
            @RequestParam(defaultValue = "600") int height) {
        asset = this.storageService.download(asset);
        if (asset.getContentType().startsWith("image")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ByteArrayInputStream is = new ByteArrayInputStream(asset.getData());) {
                Thumbnails.of(is).outputFormat("jpg").size(width, height)
                        .toOutputStream(os);
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
            } catch (Exception e) {
                throw new BusinessException("asset.download.failed", e.getMessage());
            }
        }
        return Base64.getEncoder().encodeToString(asset.getData());
    }
}