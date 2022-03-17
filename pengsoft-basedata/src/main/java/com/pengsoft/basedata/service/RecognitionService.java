package com.pengsoft.basedata.service;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.system.domain.Asset;

/**
 * The recognition service interface.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface RecognitionService {

    /**
     * Recognize identity card face.
     * 
     * @param face
     */
    IdentityCard identityCardFace(Asset face);

    /**
     * Recognize identity card emblem.
     * 
     * @param emblem
     */
    IdentityCard identityCardEmblem(Asset emblem);

    /**
     * Recognize business license.
     * 
     * @param asset
     */
    BusinessLicense businessLicense(Asset asset);

}
