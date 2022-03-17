package com.pengsoft.basedata.api;

import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.facade.PersonFacade;
import com.pengsoft.support.Constant;
import com.pengsoft.support.api.EntityApi;
import com.pengsoft.system.domain.Asset;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The web api of {@link Person}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@RestController
@RequestMapping(Constant.API_PREFIX + "/basedata/person")
public class PersonApi extends EntityApi<PersonFacade, Person, String> {

    @DeleteMapping("delete-avatar-by-asset")
    public long deleteAvatarByAsset(@RequestParam(value = "id", required = false) Person person,
            @RequestParam("asset.id") Asset asset) {
        return getService().deleteAvatarByAsset(person, asset);
    }

}
