package com.pengsoft.iot.repository;

import com.pengsoft.iot.domain.GroupDevice;
import com.pengsoft.iot.domain.QGroupDevice;
import com.pengsoft.support.repository.EntityRepository;

import org.springframework.stereotype.Repository;

/**
 * The repository interface of {@link GroupDevice} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Repository
public interface GroupDeviceRepository extends EntityRepository<QGroupDevice, GroupDevice, String> {

}
