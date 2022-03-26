package com.pengsoft.system.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.system.domain.Region;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = RegionWrapperJsonSerializer.class)
public class RegionWrapper implements Serializable {

    private static final long serialVersionUID = 1105803452474874888L;

    private Region region;
    
    public RegionWrapper(Region region) {
    	setRegion(region);
    }

}
