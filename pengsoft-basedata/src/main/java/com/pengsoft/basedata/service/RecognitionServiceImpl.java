package com.pengsoft.basedata.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.aliyun.ocr20191230.Client;
import com.aliyun.ocr20191230.models.RecognizeBusinessLicenseRequest;
import com.aliyun.ocr20191230.models.RecognizeIdentityCardRequest;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import com.pengsoft.basedata.config.properties.RecognitionServiceProperties;
import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.config.properties.StorageServiceProperties;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.service.RegionService;

import lombok.SneakyThrows;

/**
 * Aliyun OCR service.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class RecognitionServiceImpl implements RecognitionService {

    private StorageServiceProperties storageServiceProperties;

    private RecognitionServiceProperties recognitionServiceProperties;

    private List<DictionaryItem> genders;

    private List<DictionaryItem> nationalities;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private RegionService regionService;

    public RecognitionServiceImpl(StorageServiceProperties storageServiceProperties,
            RecognitionServiceProperties recognitionServiceProperties, List<DictionaryItem> genders,
            List<DictionaryItem> nationalities, RegionService regionService) {
        this.storageServiceProperties = storageServiceProperties;
        this.recognitionServiceProperties = recognitionServiceProperties;
        this.genders = genders;
        this.nationalities = nationalities;
        this.regionService = regionService;
    }

    @SneakyThrows
    private Client getOcrClient() {
        final var config = new Config().setAccessKeyId(storageServiceProperties.getAccessKeyId())
                .setAccessKeySecret(storageServiceProperties.getAccessKeySecret());
        config.endpoint = recognitionServiceProperties.getEndpoint();
        return new Client(config);
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(storageServiceProperties.getLockedBucketEndpoint(),
                storageServiceProperties.getAccessKeyId(), storageServiceProperties.getAccessKeySecret());
    }

    private String getImageUrl(String storagePath) {
        final var arr = storagePath.split(StringUtils.GLOBAL_SEPARATOR);
        var client = getOssClient();
        try {
            var expiration = new Date(new Date().getTime() + 3600 * 1000);
            return client.generatePresignedUrl(arr[0], arr[1], expiration).toString();
        } finally {
            client.shutdown();
        }
    }

    @SneakyThrows
    @Override
    public IdentityCard identityCardFace(Asset face) {
        final var req = new RecognizeIdentityCardRequest();
        req.setImageURL(getImageUrl(face.getStoragePath()));
        req.setSide("face");
        final var client = getOcrClient();
        final var result = client.recognizeIdentityCard(req).body.getData().getFrontResult();
        final var identityCard = new IdentityCard();
        identityCard.setName(result.name);
        genders.stream().filter(gender -> gender.getName().equals(result.getGender())).findAny()
                .ifPresent(identityCard::setGender);
        nationalities.stream().filter(nationality -> nationality.getName().equals(result.getNationality())).findAny()
                .ifPresent(identityCard::setNationality);
        identityCard.setBirthday(LocalDate.from(formatter.parse(result.getBirthDate())));
        identityCard.setAddress(regionService.getAddress(result.getAddress()));
        identityCard.setNumber(result.getIDNumber());
        identityCard.setFace(face);
        return identityCard;
    }

    @SneakyThrows
    @Override
    public IdentityCard identityCardEmblem(Asset emblem) {
        final var req = new RecognizeIdentityCardRequest();
        req.setImageURL(getImageUrl(emblem.getStoragePath()));
        req.setSide("back");
        final var client = getOcrClient();
        final var result = client.recognizeIdentityCard(req).body.getData().getBackResult();
        final var identityCard = new IdentityCard();
        identityCard.setStartDate(LocalDate.from(formatter.parse(result.getStartDate())));
        identityCard.setEndDate(LocalDate.from(formatter.parse(result.getEndDate())));
        identityCard.setIssue(result.getIssue());
        identityCard.setEmblem(emblem);
        return identityCard;
    }

    @SneakyThrows
    @Override
    public BusinessLicense businessLicense(Asset asset) {
        final var req = new RecognizeBusinessLicenseRequest();
        req.setImageURL(getImageUrl(asset.getStoragePath()));
        final var client = getOcrClient();
        final var result = client.recognizeBusinessLicense(req).body.data;
        final var businessLicense = new BusinessLicense();
        businessLicense.setAddress(regionService.getAddress(result.getAddress()));
        businessLicense.setBusiness(result.getBusiness());
        businessLicense.setCapital(result.getCapital());
        businessLicense.setEstablishDate(LocalDate.from(formatter.parse(result.getEstablishDate())));
        businessLicense.setLegalPerson(result.getLegalPerson());
        businessLicense.setName(result.getName());
        businessLicense.setRegisterNumber(result.getRegisterNumber());
        businessLicense.setType(result.getType());
        businessLicense.setValidPeriod(LocalDate.from(formatter.parse(result.getValidPeriod())));
        businessLicense.setAsset(asset);
        return businessLicense;
    }

}
