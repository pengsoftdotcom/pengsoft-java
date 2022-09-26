package com.pengsoft.ss.aspect;

import javax.inject.Named;

import com.pengsoft.oa.domain.PayrollRecord;

/**
 * 发薪记录API数据权限处理器
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Named
public class PayrollRecordApiDataAuthorityHandler extends ContractApiDataAuthorityHandler<PayrollRecord> {

    @Override
    public boolean support(Class<?> entityClass) {
        return PayrollRecord.class.isAssignableFrom(entityClass);
    }

}