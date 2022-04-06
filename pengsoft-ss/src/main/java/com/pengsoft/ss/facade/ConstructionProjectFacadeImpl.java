package com.pengsoft.ss.facade;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.alibaba.excel.EasyExcel;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.PostService;
import com.pengsoft.security.service.RoleService;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.excel.CashierData;
import com.pengsoft.ss.excel.CashierDataReadListener;
import com.pengsoft.ss.excel.ConstructionProjectData;
import com.pengsoft.ss.excel.ConstructionProjectDataReadListener;
import com.pengsoft.ss.excel.SecurityOfficerData;
import com.pengsoft.ss.excel.SecurityOfficerDataReadListener;
import com.pengsoft.ss.excel.SupervisionEngineerData;
import com.pengsoft.ss.excel.SupervisionEngineerDataReadListener;
import com.pengsoft.ss.excel.WorkerData;
import com.pengsoft.ss.excel.WorkerDataReadListener;
import com.pengsoft.ss.service.ConstructionProjectService;
import com.pengsoft.support.facade.EntityFacadeImpl;
import com.pengsoft.support.util.StringUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;

/**
 * The implementer of {@link ConstructionProjectFacade}
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Service
public class ConstructionProjectFacadeImpl extends
        EntityFacadeImpl<ConstructionProjectService, ConstructionProject, String> implements ConstructionProjectFacade {

    @Inject
    private PostService postService;

    @Inject
    private JobService jobService;

    @Inject
    private RoleService roleService;

    @Inject
    @Lazy
    private ConstructionProjectDataReadListener constructionProjectDataReadListener;

    @Inject
    @Lazy
    private SupervisionEngineerDataReadListener supervisionEngineerDataReadListener;

    @Inject
    @Lazy
    private SecurityOfficerDataReadListener securityOfficerDataReadListener;

    @Inject
    @Lazy
    private CashierDataReadListener cashierDataReadListener;

    @Inject
    @Lazy
    private WorkerDataReadListener workerDataReadListener;

    @Bean
    public ConstructionProjectDataReadListener constructionProjectDataReadListener() {
        return new ConstructionProjectDataReadListener();
    }

    @Bean
    public SupervisionEngineerDataReadListener supervisionEngineerDataReadListener() {
        return new SupervisionEngineerDataReadListener();
    }

    @Bean
    public SecurityOfficerDataReadListener securityOfficerDataReadListener() {
        return new SecurityOfficerDataReadListener();
    }

    @Bean
    public CashierDataReadListener cashierDataReadListener() {
        return new CashierDataReadListener();
    }

    @Bean
    public WorkerDataReadListener workerDataReadListener() {
        return new WorkerDataReadListener();
    }

    @SneakyThrows
    @Override
    public void importData(MultipartFile file) {
        final var bytes = file.getBytes();

        final var constructionProject = handleConstructionProjectData(bytes);
        handleSuperVisionUnitRelatedData(bytes, constructionProject);
        handleBuildingUnitRelatedData(bytes, constructionProject);
    }

    private ConstructionProject handleConstructionProjectData(final byte[] bytes) {
        EasyExcel.read(new ByteArrayInputStream(bytes), ConstructionProjectData.class,
                constructionProjectDataReadListener).sheet(0).doRead();

        return constructionProjectDataReadListener.getConstructionProject();
    }

    private void handleSuperVisionUnitRelatedData(final byte[] bytes, final ConstructionProject constructionProject) {
        final var supervisionUnit = constructionProject.getSupervisionUnit();
        final var suDepartment = constructionProject.getSuManager().getDepartment();
        final var suManagerJob = constructionProject.getSuManager().getJob();

        final var supervisionEngineerPostName = "监理工程师";
        final var supervisionEngineerJobName = "监理工程师";
        final var supervisionEngineerRoleCode = "supervision_engineer";
        final var supervisionEngineerJob = getJob(supervisionUnit, suDepartment, suManagerJob,
                supervisionEngineerPostName, supervisionEngineerJobName, supervisionEngineerRoleCode);
        supervisionEngineerDataReadListener.setJob(supervisionEngineerJob);

        EasyExcel.read(new ByteArrayInputStream(bytes), SupervisionEngineerData.class,
                supervisionEngineerDataReadListener).sheet(1).doRead();
    }

    private void handleBuildingUnitRelatedData(final byte[] bytes, final ConstructionProject constructionProject) {
        final var buildingUnit = constructionProject.getBuildingUnit();
        final var buDepartment = constructionProject.getBuManager().getDepartment();
        final var buManagerJob = constructionProject.getBuManager().getJob();

        final var securityOfficerPostName = "安全员";
        final var securityOfficerJobName = "安全员";
        final var securityOfficerRoleCode = "security_officer";
        final var securityOfficerJob = getJob(buildingUnit, buDepartment, buManagerJob, securityOfficerPostName,
                securityOfficerJobName, securityOfficerRoleCode);
        securityOfficerDataReadListener.setJob(securityOfficerJob);
        EasyExcel.read(new ByteArrayInputStream(bytes), SecurityOfficerData.class, securityOfficerDataReadListener)
                .sheet(2).doRead();

        final var cashierPostName = "发薪员";
        final var cashierJobName = "发薪员";
        final var cashierRoleCode = "cashier";
        final var cashierJob = getJob(buildingUnit, buDepartment, buManagerJob, cashierPostName, cashierJobName,
                cashierRoleCode);
        cashierDataReadListener.setJob(cashierJob);
        EasyExcel.read(new ByteArrayInputStream(bytes), CashierData.class, cashierDataReadListener).sheet(3).doRead();

        final var workerPostName = "工人";
        final var workerJobName = "工人";
        final var workerRoleCode = "worker";
        final var workerJob = getJob(buildingUnit, buDepartment, buManagerJob, workerPostName, workerJobName,
                workerRoleCode);
        workerDataReadListener.setJob(workerJob);
        EasyExcel.read(new ByteArrayInputStream(bytes), WorkerData.class, workerDataReadListener).sheet(4).doRead();
    }

    private Job getJob(final Organization buildingUnit, final Department buDepartment, final Job buManagerJob,
            final String postName, final String jobName, final String roleCode) {
        final var post = postService.findOneByOrganizationAndName(buildingUnit, postName)
                .orElse(new Post());
        if (StringUtils.isBlank(post.getId())) {
            post.setOrganization(buildingUnit);
            post.setName(postName);
            postService.save(post);
        }

        var job = jobService
                .findOneByDepartmentAndParentAndName(buDepartment, buManagerJob, jobName)
                .orElse(new Job());
        if (StringUtils.isBlank(job.getId())) {
            job.setDepartment(buDepartment);
            job.setParent(buManagerJob);
            job.setName(jobName);
            job.setPost(post);
            job.setQuantity(100);
            jobService.save(job);
            roleService.findOneByCode(roleCode).map(List::of).ifPresent(roles -> jobService.grantRoles(job, roles));
        }
        return job;
    }

    @Override
    public Optional<ConstructionProject> findOneByCode(String code) {
        return getService().findOneByCode(code);
    }

    @Override
    public Optional<ConstructionProject> findOneByName(@NotBlank String name) {
        return getService().findOneByName(name);
    }

}
