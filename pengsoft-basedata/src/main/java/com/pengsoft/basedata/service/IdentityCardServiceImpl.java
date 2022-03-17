package com.pengsoft.basedata.service;

import java.util.Optional;

import javax.inject.Inject;

import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.basedata.repository.IdentityCardRepository;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.service.AssetService;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link IdentityCardService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class IdentityCardServiceImpl extends EntityServiceImpl<IdentityCardRepository, IdentityCard, String>
        implements IdentityCardService {

    @Inject
    private AssetService assetService;

    @Override
    public IdentityCard save(IdentityCard target) {
        findOneByNumber(target.getNumber()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("number", "exists", target.getNumber());
            }
        });
        return super.save(target);
    }

    @Override
    public void delete(IdentityCard identityCard) {
        super.delete(identityCard);
        assetService.delete(identityCard.getFace());
        assetService.delete(identityCard.getEmblem());
    }

    @Override
    public Optional<IdentityCard> findOneByNumber(String number) {
        return getRepository().findOneByNumber(number);
    }

}
