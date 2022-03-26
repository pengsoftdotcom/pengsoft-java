package com.pengsoft.ss.excel;

import com.pengsoft.basedata.domain.Staff;

import lombok.Getter;
import lombok.Setter;

/**
 * Import data for {@link Staff} named worker.
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class WorkerData {

    private String name;

    private String mobile;

    private String identityCardNumber;

}
