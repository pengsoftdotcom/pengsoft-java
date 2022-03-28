package com.pengsoft.ss.excel;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.DepartmentService;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.PostService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.util.StringUtils;

import lombok.Getter;

/**
 * {@link ConstructionProjectData} read lisener
 * 
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class ConstructionProjectDataReadListener implements ReadListener<ConstructionProjectData> {

    @Inject
    private OrganizationService organizationService;

    @Inject
    private DepartmentService departmentService;

    @Inject
    private PostService postService;

    @Inject
    private JobService jobService;

    @Inject
    private RoleService roleService;

    @Inject
    private PersonService personService;

    @Inject
    private StaffService staffService;

    @Inject
    private ConstructionProjectService constructionProjectService;

    @Getter
    private ConstructionProject constructionProject;

    @Override
    public void invoke(ConstructionProjectData data, AnalysisContext context) {
        constructionProject = new ConstructionProject();
        constructionProject.setName(StringUtils.replace(data.getName(), "\s", ""));
        constructionProject.setOwnerManager(StringUtils.replace(data.getOwnerManager(), "\s", ""));
        if (StringUtils.isBlank(constructionProject.getCode())) {
            constructionProject.setCode(UUID.randomUUID().toString());
        }

        handleRegulatoryUnitRelatedData(data);
        handleOwnerRelatedData(data);
        handleSupervisionUnitRelatedData(data);
        handleBuildingUnitRelatedData(data);

        constructionProject = constructionProjectService.save(constructionProject);
    }

    private void handleRegulatoryUnitRelatedData(ConstructionProjectData data) {
        String regulatoryUnitName = StringUtils.replace(data.getRegulatoryUnit(), "\s", "");
        final var regulatoryUnit = organizationService.findOneByName(regulatoryUnitName).orElse(new Organization());
        if (StringUtils.isBlank(regulatoryUnit.getId())) {
            regulatoryUnit.setName(regulatoryUnitName);
            organizationService.save(regulatoryUnit);
        }
        constructionProject.setRegulatoryUnit(regulatoryUnit);

        final var ruDepartmentName = constructionProject.getName() + "项目部";
        final var ruDepartment = departmentService.findOneByOrganizationAndParentAndName(regulatoryUnit, null,
                ruDepartmentName).orElse(new Department());
        if (StringUtils.isBlank(ruDepartment.getId())) {
            ruDepartment.setOrganization(regulatoryUnit);
            ruDepartment.setName(ruDepartmentName);
            departmentService.save(ruDepartment);
        }
    }

    private void handleOwnerRelatedData(ConstructionProjectData data) {
        String ownerName = StringUtils.replace(data.getOwner(), "\s", "");
        final var owner = organizationService.findOneByName(ownerName).orElse(new Organization());
        if (StringUtils.isBlank(owner.getId())) {
            owner.setName(ownerName);
            organizationService.save(owner);
        }
        constructionProject.setOwner(owner);

        final var ownerDepartmentName = constructionProject.getName() + "项目部";
        final var ownerDepartment = departmentService.findOneByOrganizationAndParentAndName(owner, null,
                ownerDepartmentName).orElse(new Department());
        if (StringUtils.isBlank(ownerDepartment.getId())) {
            ownerDepartment.setOrganization(owner);
            ownerDepartment.setName(ownerDepartmentName);
            departmentService.save(ownerDepartment);
        }
    }

    private void handleSupervisionUnitRelatedData(ConstructionProjectData data) {
        String supervisionUnitName = data.getSupervisionUnit().replace("\s", "");
        final var supervisionUnit = organizationService.findOneByName(supervisionUnitName)
                .orElse(new Organization());
        if (StringUtils.isBlank(supervisionUnit.getId())) {
            supervisionUnit.setName(supervisionUnitName);
            organizationService.save(supervisionUnit);
        }
        constructionProject.setSupervisionUnit(supervisionUnit);

        final var suDepartmentName = constructionProject.getName() + "项目部";
        final var suDepartment = departmentService.findOneByOrganizationAndParentAndName(supervisionUnit, null,
                suDepartmentName).orElse(new Department());
        if (StringUtils.isBlank(suDepartment.getId())) {
            suDepartment.setOrganization(supervisionUnit);
            suDepartment.setName(suDepartmentName);
            departmentService.save(suDepartment);
        }

        final var suManagerPostName = "总监理工程师";
        final var suManagerPost = postService.findOneByOrganizationAndName(supervisionUnit, suManagerPostName)
                .orElse(new Post());
        if (StringUtils.isBlank(suManagerPost.getId())) {
            suManagerPost.setOrganization(supervisionUnit);
            suManagerPost.setName(suManagerPostName);
            postService.save(suManagerPost);
        }

        final var suManagerJobName = "总监理工程师";
        final var suManagerJob = jobService
                .findOneByDepartmentAndParentAndName(suDepartment, null, suManagerJobName)
                .orElse(new Job());
        if (StringUtils.isBlank(suManagerJob.getId())) {
            suManagerJob.setPost(suManagerPost);
            suManagerJob.setDepartment(suDepartment);
            suManagerJob.setName(suManagerJobName);
            suManagerJob.setDepartmentChief(true);
            jobService.save(suManagerJob);
            roleService.findOneByCode("su_manager").map(List::of)
                    .ifPresent(roles -> jobService.grantRoles(suManagerJob, roles));
        }

        final var suManagerName = StringUtils.replace(data.getSuManager(), "\s", "");
        // final var suManagerMobile =
        // StringUtils.replace(data.getSuManagerMobile(),"\s", "");
        final var person = new Person();
        // final var person = personService.findOneByMobile(suManagerMobile).orElse(new
        // Person());
        // if (StringUtils.isBlank(person.getId())) {
        person.setName(suManagerName);
        // person.setMobile(suManagerMobile);
        personService.save(person);
        // }

        final var suManager = staffService.findOneByPersonAndJob(person, suManagerJob)
                .orElse(new Staff());
        if (StringUtils.isBlank(suManager.getId())) {
            suManager.setPerson(person);
            suManager.setJob(suManagerJob);
            staffService.save(suManager);
        }
        constructionProject.setSuManager(suManager);
    }

    private void handleBuildingUnitRelatedData(ConstructionProjectData data) {
        String buildingUnitName = data.getBuildingUnit().replace("\s", "");
        final var buildingUnit = organizationService.findOneByName(buildingUnitName)
                .orElse(new Organization());
        if (StringUtils.isBlank(buildingUnit.getId())) {
            buildingUnit.setName(buildingUnitName);
            organizationService.save(buildingUnit);
        }
        constructionProject.setBuildingUnit(buildingUnit);

        final var buDepartmentName = constructionProject.getName() + "项目部";
        final var buDepartment = departmentService.findOneByOrganizationAndParentAndName(buildingUnit, null,
                buDepartmentName).orElse(new Department());
        if (StringUtils.isBlank(buDepartment.getId())) {
            buDepartment.setOrganization(buildingUnit);
            buDepartment.setName(buDepartmentName);
            departmentService.save(buDepartment);
        }

        final var buManagerPostName = "项目经理";
        final var buManagerPost = postService.findOneByOrganizationAndName(buildingUnit, buManagerPostName)
                .orElse(new Post());
        if (StringUtils.isBlank(buManagerPost.getId())) {
            buManagerPost.setOrganization(buildingUnit);
            buManagerPost.setName(buManagerPostName);
            postService.save(buManagerPost);
        }

        final var buManagerJobName = "项目经理";
        final var buManagerJob = jobService
                .findOneByDepartmentAndParentAndName(buDepartment, null, buManagerJobName)
                .orElse(new Job());
        if (StringUtils.isBlank(buManagerJob.getId())) {
            buManagerJob.setPost(buManagerPost);
            buManagerJob.setDepartment(buDepartment);
            buManagerJob.setName(buManagerJobName);
            buManagerJob.setDepartmentChief(true);
            jobService.save(buManagerJob);
            roleService.findOneByCode("bu_manager").map(List::of)
                    .ifPresent(roles -> jobService.grantRoles(buManagerJob, roles));
        }

        final var buManagerName = StringUtils.replace(data.getBuManager(), "\s", "");
        final var buManagerMobile = data.getBuManagerMobile().replace("\s", "");
        final var person = personService.findOneByMobile(buManagerMobile).orElse(new Person());
        if (StringUtils.isBlank(person.getId())) {
            person.setName(buManagerName);
            person.setMobile(buManagerMobile);
            personService.save(person);
        }

        final var buManager = staffService.findOneByPersonAndJob(person, buManagerJob)
                .orElse(new Staff());
        if (StringUtils.isBlank(buManager.getId())) {
            buManager.setPerson(person);
            buManager.setJob(buManagerJob);
            staffService.save(buManager);
        }
        constructionProject.setBuManager(buManager);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing to do
    }

}
