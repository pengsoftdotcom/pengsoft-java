package com.pengsoft.oa.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.ContractPicture;
import com.pengsoft.oa.domain.QContractPicture;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link ContractPicture} based on JPA
 *
 * @author peng.dang@pengsoft.com∏
 * @since 1.0.0
 */
@Repository
public interface ContractPictureRepository
        extends EntityRepository<QContractPicture, ContractPicture, String>, OwnedExtRepository {

    /**
     * Returns all {@link ContractPicture}s with the given contract id.
     * 
     * @param contractId The contract id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ContractPicture> findAllByContractId(@NotBlank String contractId);

}
