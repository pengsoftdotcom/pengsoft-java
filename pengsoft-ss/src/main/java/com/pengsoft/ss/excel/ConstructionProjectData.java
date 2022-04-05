package com.pengsoft.ss.excel;

import lombok.Getter;
import lombok.Setter;

/**
 * Import data for {@link ContructionProject}
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
public class ConstructionProjectData {

    private String code;

    private String name;

    private String regulatoryUnit;

    private String ruManager;

    private String ruManagerMobile;

    private String owner;

    private String ownerManager;

    private String ownerManagerMobile;

    private String supervisionUnit;

    private String suManager;

    private String suManagerMobile;

    private String buildingUnit;

    private String buManager;

    private String buManagerMobile;

}
