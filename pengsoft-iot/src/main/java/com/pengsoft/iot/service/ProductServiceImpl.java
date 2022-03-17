package com.pengsoft.iot.service;

import com.pengsoft.iot.domain.Product;
import com.pengsoft.iot.repository.ProductRepository;
import com.pengsoft.support.service.EntityServiceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link Product} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class ProductServiceImpl extends EntityServiceImpl<ProductRepository, Product, String>
        implements ProductService {

}
