package com.pengsoft.iot.api;

import com.pengsoft.iot.domain.Product;
import com.pengsoft.iot.service.ProductService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Product}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/iot/product")
public class ProductApi extends EntityApi<ProductService, Product, String> {

}
