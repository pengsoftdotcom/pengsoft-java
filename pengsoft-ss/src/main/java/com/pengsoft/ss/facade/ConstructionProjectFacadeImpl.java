package com.pengsoft.ss.facade;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.alibaba.excel.EasyExcel;
import com.pengsoft.basedata.domain.Department;
import com.pengsoft.basedata.domain.Job;
import com.pengsoft.basedata.domain.Organization;
import com.pengsoft.basedata.domain.Post;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.basedata.service.JobService;
import com.pengsoft.basedata.service.PostService;
import com.pengsoft.basedata.service.StaffService;
import com.pengsoft.oa.domain.PayrollRecord;
import com.pengsoft.oa.service.PayrollRecordService;
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
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.support.util.StringUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
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
    private PayrollRecordService payrollRecordService;

    @Inject
    private StaffService staffService;

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
        if (supervisionUnit != null) {
            final var suDepartment = constructionProject.getSuManager().getDepartment();
            final var suManagerJob = constructionProject.getSuManager().getJob();

            final var supervisionEngineerPostName = "监理";
            final var supervisionEngineerJobName = "监理";
            final var supervisionEngineerRoleCode = "supervision_engineer";
            final var supervisionEngineerJob = getJob(supervisionUnit, suDepartment, suManagerJob,
                    supervisionEngineerPostName, supervisionEngineerJobName, supervisionEngineerRoleCode);
            supervisionEngineerDataReadListener.setJob(supervisionEngineerJob);

            EasyExcel.read(new ByteArrayInputStream(bytes), SupervisionEngineerData.class,
                    supervisionEngineerDataReadListener).sheet(1).doRead();
        }
    }

    private void handleBuildingUnitRelatedData(final byte[] bytes, final ConstructionProject constructionProject) {
        final var buildingUnit = constructionProject.getBuildingUnit();
        if (buildingUnit != null) {
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
            EasyExcel.read(new ByteArrayInputStream(bytes), CashierData.class, cashierDataReadListener).sheet(3)
                    .doRead();

            final var workerPostName = "工人";
            final var workerJobName = "工人";
            final var workerRoleCode = "worker";
            final var workerJob = getJob(buildingUnit, buDepartment, buManagerJob, workerPostName, workerJobName,
                    workerRoleCode);
            workerDataReadListener.setJob(workerJob);
            EasyExcel.read(new ByteArrayInputStream(bytes), WorkerData.class, workerDataReadListener).sheet(4).doRead();
        }
    }

    private Job getJob(final Organization buildingUnit, final Department department, final Job managerJob,
            final String postName, final String jobName, final String roleCode) {
        final var post = postService.findOneByOrganizationAndName(buildingUnit, postName)
                .orElse(new Post());
        if (StringUtils.isBlank(post.getId())) {
            post.setOrganization(buildingUnit);
            post.setName(postName);
            postService.save(post);
        }

        var job = jobService
                .findAllByName(jobName)
                .stream().filter(j -> EntityUtils.equals(j.getDepartment(), department)).findAny().orElse(new Job());
        if (StringUtils.isBlank(job.getId())) {
            job.setDepartment(department);
            job.setParent(managerJob);
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

    @Override
    public List<Map<String, Object>> statisticByStatus() {
        return getService().statisticByStatus();
    }

    @Scheduled(cron = "0 53 10 * * ?")
    @Override
    public void generatePayrollRecords() {
        getService().findAll().stream().forEach(project -> {
            final var payday = project.getBuildingUnit().getPayday();
            final var now = DateUtils.currentDateTime();
            final var year = now.getYear();
            final var month = now.getMonthValue();
            final var day = now.getDayOfMonth();
            // if (payday == day) {
            final var payrollRecord = payrollRecordService
                    .findOneByYearAndMonthAndBelongsTo(year, month, project.getBuildingUnit().getId())
                    .orElse(new PayrollRecord());
            if (StringUtils.isBlank(payrollRecord.getId())) {
                List<Staff> cashiers = staffService.findAllByDepartmentsAndRoleCodes(
                        List.of(project.getBuManager().getDepartment()),
                        List.of("cashier"));
                if (CollectionUtils.isNotEmpty(cashiers)) {
                    final var cashier = cashiers.get(0);
                    payrollRecord.setCreatedBy(cashier.getPerson().getUser().getId());
                    payrollRecord.setCreatedAt(now);
                    payrollRecord.setUpdatedBy(payrollRecord.getCreatedBy());
                    payrollRecord.setUpdatedAt(payrollRecord.getCreatedAt());
                    payrollRecord.setControlledBy(cashier.getDepartment().getId());
                    payrollRecord.setBelongsTo(cashier.getOrganization().getId());
                    payrollRecord.setYear(year);
                    payrollRecord.setMonth(month);
                    payrollRecordService.save(payrollRecord);
                }
            }
            // }

        });
    }

}
