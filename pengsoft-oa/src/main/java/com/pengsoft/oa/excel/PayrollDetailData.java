package com.pengsoft.oa.excel;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * For reading payroll detail excel
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class PayrollDetailData {

    private String name;

    private String identityCardNumber;

    private String mobile;

    private BigDecimal grossPay;

    private BigDecimal netPay;

}