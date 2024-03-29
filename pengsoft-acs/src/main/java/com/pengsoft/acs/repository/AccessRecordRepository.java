package com.pengsoft.acs.repository;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.domain.QAccessRecord;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link AccessRecord} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface AccessRecordRepository extends EntityRepository<QAccessRecord, AccessRecord, String> {

}
