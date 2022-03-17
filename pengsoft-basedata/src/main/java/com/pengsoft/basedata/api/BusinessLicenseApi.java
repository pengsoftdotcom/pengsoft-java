package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.BusinessLicense;
import com.pengsoft.basedata.service.BusinessLicenseService;
import com.pengsoft.basedata.service.RecognitionService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.Asset;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;

/**
 * The web api of {@link BusinessLicense}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/business-license")
public class BusinessLicenseApi extends EntityApi<BusinessLicenseService, BusinessLicense, String> {

    private RecognitionService recognitionService;

    public BusinessLicenseApi(ApplicationContext context) {
        if (context.containsBean("recognitionService")) {
            this.recognitionService = context.getBean(RecognitionService.class);
        }
    }

    @SneakyThrows
    @GetMapping("recognize")
    public BusinessLicense recognize(@RequestParam("id") Asset asset) {
        return recognitionService.businessLicense(asset);

    }

}
