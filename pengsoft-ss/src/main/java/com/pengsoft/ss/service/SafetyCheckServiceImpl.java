package com.pengsoft.ss.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.ss.domain.SafetyCheck;
import com.pengsoft.ss.domain.SafetyCheckFile;
import com.pengsoft.ss.repository.SafetyCheckFileRepository;
import com.pengsoft.ss.repository.SafetyCheckRepository;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.repository.DictionaryItemRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SafetyCheckService} based on
 * JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyCheckServiceImpl extends EntityServiceImpl<SafetyCheckRepository, SafetyCheck, String>
        implements SafetyCheckService {

    @Inject
    private SafetyCheckFileRepository safetyCheckFileRepository;

    @Inject
    private DictionaryItemRepository dictionaryItemRepository;

    @Override
    public SafetyCheck save(final SafetyCheck target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public void submit(final SafetyCheck check, List<Asset> assets) {
        if (check.getSubmittedAt() != null) {
            throw new BusinessException("check.submit.already");
        }
        check.setSubmittedAt(DateUtils.currentDateTime());
        save(check);
        final var files = assets.stream()
                .map(asset -> new SafetyCheckFile(check, asset, getSafetyCheckFileType("submit"))).toList();
        safetyCheckFileRepository.saveAll(files);
    }

    private DictionaryItem getSafetyCheckFileType(String typeCode) {
        return dictionaryItemRepository.findOneByTypeCodeAndParentIdAndCode("safety_check_file_type", null, typeCode)
                .orElseThrow(() -> getExceptions().entityNotExists(DictionaryItem.class, "typeCode"));
    }

    @Override
    public void handle(SafetyCheck check, String result, List<Asset> assets) {
        if (check.getHandledAt() != null) {
            throw new BusinessException("check.handle.already");
        }
        check.setResult(result);
        check.setHandledAt(DateUtils.currentDateTime());
        final var files = assets.stream()
                .map(asset -> new SafetyCheckFile(check, asset, getSafetyCheckFileType("handle"))).toList();
        safetyCheckFileRepository.saveAll(files);
        check.setFiles(files);
        super.save(check);
    }

    @Override
    public Optional<SafetyCheck> findOneByCode(@NotBlank final String code) {
        return getRepository().findOneByCode(code);
    }

}
