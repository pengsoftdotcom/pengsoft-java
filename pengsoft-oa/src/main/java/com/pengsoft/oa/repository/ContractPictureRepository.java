package com.pengsoft.oa.repository;

import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.domain.QContractPicture;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link ContractPicture} based on JPA
 *
 * @author peng.dang@pengsoft.com∏
 * @since 1.0.0
 */
@Repository
public interface ContractPictureRepository
        extends EntityRepository<QContractPicture, ContractPicture, String>, OwnedRepository {

}
