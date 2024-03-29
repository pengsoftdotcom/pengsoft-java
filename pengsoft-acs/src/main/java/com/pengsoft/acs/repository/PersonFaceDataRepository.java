package com.pengsoft.acs.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;

import com.pengsoft.acs.domain.PersonFaceData;
import com.pengsoft.acs.domain.QPersonFaceData;
import com.pengsoft.security.repository.OwnedRepository;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link PersonFaceData} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonFaceDataRepository
        extends EntityRepository<QPersonFaceData, PersonFaceData, String>, OwnedRepository {

    /**
     * 查询身份证号不为空人脸分页数据
     * 
     * @param syncedAt 设备同步时间
     * @param pageable {@link Pageable}
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Page<PersonFaceData> findPageByPersonIdentityCardNumberNotNull(Pageable pageable);

    /**
     * 根据人员ID查询所有人员人脸数据
     * 
     * @param personIds 人员ID
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    List<PersonFaceData> findAllByPersonIdIn(List<String> personIds);

    /**
     * 根据给定的身份证号查询人脸数据
     * 
     * @param identityCardNumber 身份证号
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"), forCounting = false)
    Optional<PersonFaceData> findOneByPersonIdentityCardNumber(@NotBlank String identityCardNumber);

}
