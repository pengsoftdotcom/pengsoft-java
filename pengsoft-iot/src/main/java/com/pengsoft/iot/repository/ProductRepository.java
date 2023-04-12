package com.pengsoft.iot.repository;

import com.pengsoft.iot.domain.Product;
import com.pengsoft.iot.domain.QProduct;
import com.pengsoft.support.repository.EntityRepository;

/**
 * The repository interface of {@link Product} based on JPA
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface ProductRepository extends EntityRepository<QProduct, Product, String> {

}
