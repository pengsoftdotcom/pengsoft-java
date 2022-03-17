package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.IdentityCard;
import com.pengsoft.basedata.service.IdentityCardService;
import com.pengsoft.basedata.service.RecognitionService;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.support.util.StringUtils;
import com.pengsoft.system.domain.Asset;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link IdentityCard}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/identity-card")
public class IdentityCardApi extends EntityApi<IdentityCardService, IdentityCard, String> {

    private RecognitionService recognitionService;

    public IdentityCardApi(ApplicationContext context) {
        if (context.containsBean("recognitionService")) {
            this.recognitionService = context.getBean(RecognitionService.class);
        }
    }

    @PostMapping("recognize")
    public IdentityCard recognize(@RequestParam("id") Asset asset, @RequestParam(defaultValue = "face") String side) {
        return StringUtils.equals(side, "face") ? recognitionService.identityCardFace(asset)
                : recognitionService.identityCardEmblem(asset);

    }

}
