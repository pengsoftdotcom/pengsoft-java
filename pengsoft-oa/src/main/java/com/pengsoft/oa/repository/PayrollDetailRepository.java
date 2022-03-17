package com.pengsoft.oa.repository;

import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.domain.QPayrollDetail;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link PayrollDetail} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface PayrollDetailRepository
        extends EntityRepository<QPayrollDetail, PayrollDetail, String>, OwnedRepository {

}
