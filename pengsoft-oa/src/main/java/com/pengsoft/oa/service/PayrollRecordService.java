package com.pengsoft.oa.service;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link PayrollRecord}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PayrollRecordService extends EntityService<PayrollRecord, String> {

    /**
     * Returns an {@link Optional} of a {@link PayrollRecord} with the given code.
     * 
     * @param code The coding rule's code
     */
    Optional<PayrollRecord> findOneByCode(@NotBlank String code);

}