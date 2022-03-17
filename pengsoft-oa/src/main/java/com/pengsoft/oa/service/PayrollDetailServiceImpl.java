package com.pengsoft.oa.service;

import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.oa.domain.PayrollDetail;
import com.pengsoft.oa.repository.PayrollDetailRepository;
import com.pengsoft.security.domain.Role;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link PayrollDetail} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class PayrollDetailServiceImpl extends EntityServiceImpl<PayrollDetailRepository, PayrollDetail, String>
        implements PayrollDetailService {

    @Override
    public void confirm(PayrollDetail payrollDetail) {
        if (!SecurityUtils.hasAnyRole(Role.ADMIN, Role.ORG_ADMIN)
                && StringUtils.notEquals(payrollDetail.getStaff().getPerson().getId(),
                        SecurityUtilsExt.getPersonId())) {
            throw new AccessDeniedException("Not authorized");
        }
        payrollDetail.setConfirmedAt(DateUtils.currentDateTime());
        payrollDetail.setConfirmedBy(SecurityUtils.getUserId());
        save(payrollDetail);
    }

}
