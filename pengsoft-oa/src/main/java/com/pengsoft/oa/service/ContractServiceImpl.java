package com.pengsoft.oa.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.repository.ContractRepository;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.system.domain.DictionaryItem;
import com.pengsoft.system.repository.DictionaryItemRepository;

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

    @Inject
    private DictionaryItemRepository dictionaryItemRepository;

    @Override
    public void confirm(Contract contract) {
        if (contract.getConfirmedAt() != null) {
            throw new BusinessException("contract.confirm.already");
        }
        contract.setStatus(dictionaryItemRepository
                .findOneByTypeCodeAndParentIdAndCode("contract_status", null, "confirmed").orElseThrow(
                        () -> getExceptions().entityNotExists(DictionaryItem.class, "contract_status::confirmed")));
        contract.setConfirmedAt(DateUtils.currentDateTime());
        contract.setConfirmedBy(SecurityUtils.getUserId());
        save(contract);
    }

    @Override
    public List<Map<String, Object>> statisticByDepartment(@NotEmpty List<Department> departments) {
        return getRepository().statisticByDepartmentId(departments.stream().map(Department::getId).toList());
    }

    @Override
    public Optional<Contract> findOneByPartyAIdAndPartyBId(@NotBlank String partyAId, @NotBlank String partyBId) {
        return getRepository().findOneByPartyAIdAndPartyBId(partyAId, partyBId);
    }

}
