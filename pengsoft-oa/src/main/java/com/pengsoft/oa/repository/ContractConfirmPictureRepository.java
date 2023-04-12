package com.pengsoft.oa.repository;

import java.util.List;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.ContractConfirmPicture;
import com.pengsoft.oa.domain.QContractConfirmPicture;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link ContractConfirmPicture} based on JPA
 *
 * @author peng.dang@pengsoft.com∏
 * @since 1.0.0
 */
public interface ContractConfirmPictureRepository
        extends EntityRepository<QContractConfirmPicture, ContractConfirmPicture, String>, OwnedExtRepository {

    /**
     * Returns all {@link ContractConfirmPicture}s with the given contract id.
     * 
     * @param contractId The contract id.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<ContractConfirmPicture> findAllByContractId(@NotBlank String contractId);

}
