package com.pengsoft.acs.service;

import com.pengsoft.acs.domain.AccessRecord;
import com.pengsoft.acs.repository.AccessRecordRepository;
import com.pengsoft.support.service.EntityServiceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link AccessRecord} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AccessRecordServiceImpl extends EntityServiceImpl<AccessRecordRepository, AccessRecord, String>
        implements AccessRecordService {

}
