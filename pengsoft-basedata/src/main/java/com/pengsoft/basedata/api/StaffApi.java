package com.pengsoft.basedata.api;

import java.util.List;

import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.facade.StaffFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Staff}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/staff")
public class StaffApi extends EntityApi<StaffFacade, Staff, String> {

    @GetMapping("/find-all-by-department-and-role-codes")
    public List<Staff> findAllByDepartmentAndRoleCodes(@RequestParam("department.id") List<Department> departments,
            @RequestParam("role.code") List<String> roleCodes) {
        return getService().findAllByDepartmentsAndRoleCodes(departments, roleCodes);
    }

}
