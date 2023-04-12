package com.pengsoft.oa.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.basedata.repository.OwnedExtRepository;
import com.pengsoft.oa.domain.Contract;
import com.pengsoft.oa.domain.QContract;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link Contract} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ContractRepository extends EntityRepository<QContract, Contract, String>, OwnedExtRepository {

    /**
     * 查询指定部门的合同统计数据
     */
    @Query(value = """
            select
              controlled_by department,
              created_by cashier,
              b.code status,
              count(1) count
            from contract a
            left join dictionary_item b on a.status_id = b.id
            where a.controlled_by in ?1
            group by a.controlled_by, a.created_by, b.code
                """, nativeQuery = true)
    List<Map<String, Object>> statisticByDepartmentId(@NotEmpty List<String> departmentIds);

    /**
     * 根据甲方乙方查询单个合同
     * 
     * @param partyAId 甲方ID
     * @param partyBId 乙方ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<Contract> findOneByPartyAIdAndPartyBId(@NotBlank String partyAId, @NotBlank String partyBId);

}
