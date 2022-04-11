package com.pengsoft.oa.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.support.service.EntityService;

/**
 * The service interface of {@link Contract}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractService extends EntityService<Contract, String> {

    /**
     * 确认合同
     * 
     * @param contract {@link Contract}
     */
    void confirm(@NotNull Contract contract);

    /**
     * 查询指定部门的合同统计数据
     * 
     * @param departments 部门列表
     */
    List<Map<String, Object>> statisticByDepartment(@NotEmpty List<Department> departments);

}
