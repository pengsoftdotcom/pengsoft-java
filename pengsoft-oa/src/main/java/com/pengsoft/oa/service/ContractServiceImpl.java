package com.pengsoft.oa.service;

import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.repository.ContractRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Contract} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ContractServiceImpl extends EntityServiceImpl<ContractRepository, Contract, String>
        implements ContractService {

    @Override
    public void confirm(Contract contract) {
        if (contract.getConfirmedAt() != null) {
            throw new BusinessException("contract.confirm.already");
        }
        contract.setConfirmedAt(DateUtils.currentDateTime());
        contract.setConfirmedBy(SecurityUtils.getUserId());
        save(contract);
    }

}
