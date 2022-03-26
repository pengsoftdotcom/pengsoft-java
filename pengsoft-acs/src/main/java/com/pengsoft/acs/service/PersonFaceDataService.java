package com.pengsoft.acs.service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.pengsoft.acs.domain.PersonFaceData;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.support.service.EntityService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The service interface of {@link PersonFaceData}.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface PersonFaceDataService extends EntityService<PersonFaceData, String> {

    /**
     * 查询身份证号不为空人脸分页数据
     * 
     * @param syncedAt 设备同步时间
     * @param pageable {@link Pageable}
     */
    Page<PersonFaceData> findPageByPersonIdentityCardNumberNotNull(Pageable pageable);

    /**
     * 根据人员ID查询所有人员人脸数据
     * 
     * @param persons 人员ID
     */
    List<PersonFaceData> findAllByPersonIn(List<Person> persons);

    /**
     * 根据给定的身份证号查询人脸数据
     * 
     * @param pageable {@link Pageable}
     */
    Optional<PersonFaceData> findOneByPersonIdentityCardNumber(@NotBlank String identityCardNumber);

}
