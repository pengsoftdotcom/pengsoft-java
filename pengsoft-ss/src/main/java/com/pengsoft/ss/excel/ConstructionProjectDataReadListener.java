package com.pengsoft.ss.excel;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.pengsoft.basedata.domain.CodingRule;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.QJobRole;
import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.generator.CodingGenerator;
import com.pengsoft.basedata.repository.CodingRuleRepository;
import com.pengsoft.basedata.service.DepartmentService;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.OrganizationService;
import com.pengsoft.basedata.service.PersonService;
import com.pengsoft.basedata.service.PostService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.exception.Exceptions;
import com.pengsoft.support.util.StringUtils;
import com.querydsl.jpa.JPAExpressions;

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

    @Inject
    private CodingRuleRepository codingRuleRepository;

    @Inject
    private Exceptions exceptions;

    @Inject
    private ApplicationContext applicationContext;

    @Override
    public void invoke(ConstructionProjectData data, AnalysisContext context) {
        if (context.readRowHolder().getRowIndex() == 3) {
            final var name = StringUtils.replace(data.getName(), "\s", "");
            constructionProject = constructionProjectService.findOneByName(name).orElse(new ConstructionProject(name));

            final var code = StringUtils.replace(data.getCode(), "\s", "");
            if (StringUtils.isBlank(code)) {
                final var entityName = ConstructionProject.class.getSimpleName();
                final var codingRule = codingRuleRepository
                        .findOneByEntityAndControlledByAndBelongsTo(entityName, null, null)
                        .orElseThrow(() -> exceptions.entityNotExists(CodingRule.class, entityName));
                final var generator = applicationContext.getBean(codingRule.getGenerator(), CodingGenerator.class);
                constructionProject.setCode(generator.generate(codingRule));
            } else {
                constructionProject = constructionProjectService.findOneByCode(code)
                        .orElse(new ConstructionProject(code, name));
            }

            handleRegulatoryUnitRelatedData(data);
            handleOwnerRelatedData(data);
            handleSupervisionUnitRelatedData(data);
            handleBuildingUnitRelatedData(data);

            constructionProject = constructionProjectService.save(constructionProject);
        }
    }

    private void handleRegulatoryUnitRelatedData(ConstructionProjectData data) {
        final var regulatoryUnitName = StringUtils.replace(data.getRegulatoryUnit(), "\s", "");
        if (StringUtils.isNotBlank(regulatoryUnitName)) {
            final var regulatoryUnit = saveOrganization(regulatoryUnitName);
            constructionProject.setRegulatoryUnit(regulatoryUnit);

            final var ruManagerName = StringUtils.replace(data.getRuManager(), "\s", "");
            final var ruManagerMobile = StringUtils.replace(data.getRuManagerMobile(), "\s", "");
            final var person = savePerson(ruManagerName, ruManagerMobile);

            final var root = QStaff.staff;
            final var jobRole = QJobRole.jobRole;
            final var ruManager = staffService
                    .findOne(root.person.id.eq(person.getId())
                            .and(JPAExpressions.selectOne().from(root.job.jobRoles, jobRole)
                                    .where(jobRole.role.code.eq("ru_manager")).exists()))
                    .orElseThrow(() -> exceptions.entityNotExists(Staff.class, "ru_manager"));
            constructionProject.setRuManager(ruManager);
        }
    }

    private void handleOwnerRelatedData(ConstructionProjectData data) {
        final var ownerName = StringUtils.replace(data.getOwner(), "\s", "");
        if (StringUtils.isNotBlank(ownerName)) {
            final var owner = saveOrganization(ownerName);
            constructionProject.setOwner(owner);

            final var ownerManagerPostName = "项目负责人";
            final var ownerManagerPost = savePost(owner, ownerManagerPostName);

            final var ownerDepartmentName = constructionProject.getName() + "项目部";
            final var ownerDepartment = saveDepartment(owner, ownerDepartmentName);

            final var ownerManagerJobName = "项目负责人";
            final var ownerManagerJob = saveJob(ownerManagerPost, ownerDepartment, ownerManagerJobName, "owner_manager");

            final var ownerManagerName = StringUtils.replace(data.getOwnerManager(), "\s", "");
            final var ownerManagerMobile = StringUtils.replace(data.getOwnerManagerMobile(), "\s", "");
            final var person = savePerson(ownerManagerName, ownerManagerMobile);
            final var ownerManager = saveStaff(person, ownerManagerJob);
            constructionProject.setOwnerManager(ownerManager);
        }
    }

    private void handleSupervisionUnitRelatedData(ConstructionProjectData data) {
        final var supervisionUnitName = StringUtils.replace(data.getSupervisionUnit(), "\s", "");
        if (StringUtils.isNotBlank(supervisionUnitName)) {
            final var supervisionUnit = saveOrganization(supervisionUnitName);
            constructionProject.setSupervisionUnit(supervisionUnit);

            final var suManagerPostName = "监理总监";
            final var suManagerPost = savePost(supervisionUnit, suManagerPostName);

            final var suDepartmentName = constructionProject.getName() + "项目部";
            final var suDepartment = saveDepartment(supervisionUnit, suDepartmentName);

            final var suManagerJobName = "监理总监";
            final var suManagerJob = saveJob(suManagerPost, suDepartment, suManagerJobName, "su_manager");

            final var suManagerName = StringUtils.replace(data.getSuManager(), "\s", "");
            final var suManagerMobile = StringUtils.replace(data.getSuManagerMobile(), "\s", "");
            final var person = savePerson(suManagerName, suManagerMobile);
            final var suManager = saveStaff(person, suManagerJob);
            constructionProject.setSuManager(suManager);
        }
    }

    private void handleBuildingUnitRelatedData(ConstructionProjectData data) {
        final var buildingUnitName = StringUtils.replace(data.getBuildingUnit(), "\s", "");
        final var buildingUnit = saveOrganization(buildingUnitName);
        constructionProject.setBuildingUnit(buildingUnit);

        final var buManagerPostName = "项目经理";
        final var buManagerPost = savePost(buildingUnit, buManagerPostName);

        final var buDepartmentName = constructionProject.getName() + "项目部";
        final var buDepartment = saveDepartment(buildingUnit, buDepartmentName);

        final var buManagerJobName = "项目经理";
        final var buManagerJob = saveJob(buManagerPost, buDepartment, buManagerJobName, "bu_manager");

        final var buManagerName = StringUtils.replace(data.getBuManager(), "\s", "");
        final var buManagerMobile = StringUtils.isNotBlank(data.getBuManagerMobile()) ? data.getBuManagerMobile().replace("\s", "") : null;
        final var person = savePerson(buManagerName, buManagerMobile);
        final var buManager = saveStaff(person, buManagerJob);
        constructionProject.setBuManager(buManager);
    }

    private Organization saveOrganization(String name) {
        final var organization = organizationService.findOneByName(name)
                .orElse(new Organization());
        if (StringUtils.isBlank(organization.getId())) {
            organization.setName(name);
            organizationService.save(organization);
        }
        return organization;
    }

    private Department saveDepartment(Organization organization, String name) {
        final var department = departmentService.findOneByOrganizationAndParentAndName(organization, null,
                name).orElse(new Department());
        if (StringUtils.isBlank(department.getId())) {
            department.setOrganization(organization);
            department.setName(name);
            departmentService.save(department);
        }
        return department;
    }

    private Post savePost(Organization organization, String name) {
        final var post = postService.findOneByOrganizationAndName(organization, name)
                .orElse(new Post());
        if (StringUtils.isBlank(post.getId())) {
            post.setOrganization(organization);
            post.setName(name);
            postService.save(post);
        }
        return post;
    }

    private Job saveJob(Post post, Department department, String name, String roleCode) {
        final var job = jobService
                .findOneByDepartmentAndParentAndName(department, null, name)
                .orElse(new Job());
        if (StringUtils.isBlank(job.getId())) {
            job.setPost(post);
            job.setDepartment(department);
            job.setName(name);
            job.setDepartmentChief(true);
            jobService.save(job);
            roleService.findOneByCode(roleCode).map(List::of)
                    .ifPresent(roles -> jobService.grantRoles(job, roles));
        }
        return job;
    }

    private Person savePerson(String name, String mobile) {
        final var person = StringUtils.isBlank(mobile) ? new Person()
                : personService.findOneByMobile(mobile).orElse(new Person());
        if (StringUtils.isBlank(person.getId())) {
            person.setName(name);
            person.setMobile(mobile);
            personService.save(person);
        }
        return person;
    }

    private Staff saveStaff(Person person, Job job) {
        final var staff = staffService.findOneByPersonAndJob(person, job)
                .orElse(new Staff());
        if (StringUtils.isBlank(staff.getId())) {
            staff.setPerson(person);
            staff.setJob(job);
            staffService.save(staff);
        }
        return staff;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing to do
    }

}
