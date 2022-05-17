package com.pengsoft.system.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
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

    private static final String DATA_IMAGE_JPEG_BASE64 = "data:image/jpeg;base64,";
    @Inject
    private StorageService storageService;

    @PostMapping("upload")
    public List<Asset> upload(@RequestParam("file") List<MultipartFile> files,
            @RequestParam(defaultValue = "false") boolean locked, @RequestParam(defaultValue = "true") boolean zoomed,
            @RequestParam(defaultValue = "600") int width, @RequestParam(defaultValue = "600") int height) {
        return getService().save(this.storageService.upload(files, locked, zoomed, width, height));
    }

    @GetMapping("download")
    public String download(@RequestParam("id") Asset asset, @RequestParam(defaultValue = "true") boolean zoomed,
            @RequestParam(defaultValue = "600") int width, @RequestParam(defaultValue = "600") int height) {
        asset = this.storageService.download(asset);
        if (storageService.isImage(asset.getContentType())) {
            if (zoomed) {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ByteArrayInputStream is = new ByteArrayInputStream(asset.getData());) {
                    final var originalImage = ImageIO.read(is);
                    final var originalWidth = originalImage.getWidth();
                    final var originalHeight = originalImage.getHeight();
                    if (originalWidth > width || originalHeight > height) {
                        Thumbnails.of(originalImage).outputFormat("jpeg").size(width, height).toOutputStream(os);
                        return DATA_IMAGE_JPEG_BASE64 + Base64.getEncoder().encodeToString(os.toByteArray());
                    }
                } catch (Exception e) {
                    throw new BusinessException("asset.download.failed", e.getMessage());
                }
            }
            return DATA_IMAGE_JPEG_BASE64 + Base64.getEncoder().encodeToString(asset.getData());
        }
        return Base64.getEncoder().encodeToString(asset.getData());
    }
}