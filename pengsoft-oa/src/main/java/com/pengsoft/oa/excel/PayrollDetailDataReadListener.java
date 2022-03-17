package com.pengsoft.oa.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

/**
 * Excel reader for {@link PayrollDetailData}
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class PayrollDetailDataReadListener implements ReadListener<PayrollDetailData> {

    @Override
    public void invoke(PayrollDetailData data, AnalysisContext context) {
        System.out.println(context.readRowHolder().getRowIndex());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}
