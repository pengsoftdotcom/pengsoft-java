package com.pengsoft.oa.repository;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.QContract;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link Contract} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface ContractRepository extends EntityRepository<QContract, Contract, String>, OwnedExtRepository {

}
