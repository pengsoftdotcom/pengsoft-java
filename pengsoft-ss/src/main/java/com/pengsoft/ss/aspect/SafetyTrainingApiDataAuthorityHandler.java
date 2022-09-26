package com.pengsoft.ss.aspect;

import java.lang.reflect.Method;

import javax.inject.Named;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.security.aspect.AbstractApiDataAuthorityHandler;
import com.pengsoft.security.util.SecurityUtils;
import com.pengsoft.ss.domain.ConstructionProject;
import com.pengsoft.ss.domain.QSafetyTraining;
import com.pengsoft.ss.domain.QSafetyTrainingParticipant;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.support.util.QueryDslUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

/**
 * 安全培训API接口数据权限处理器
 */
@Named
public class SafetyTrainingApiDataAuthorityHandler extends AbstractApiDataAuthorityHandler<SafetyTraining> {

    @Override
    public boolean support(Class<?> entityClass) {
        return SafetyTraining.class.isAssignableFrom(entityClass);
    }

    @Override
    protected Predicate exchange(Class<SafetyTraining> entityClass, Method method, Predicate predicate) {
        final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
        final var root = QSafetyTraining.safetyTraining;
        final var project = root.project;
        final var staff = QStaff.staff;
        final var select = JPAExpressions.select(root);

        JPQLQuery<SafetyTraining> leftJoin = null;
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            leftJoin = select.leftJoin(project.ruManager, staff);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            leftJoin = select.leftJoin(project.ownerManager, staff);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            leftJoin = select.leftJoin(project.suManager, staff);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_QUALITY_INSPECTOR,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            leftJoin = select.leftJoin(project.buManager, staff);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_WORKER)) {
            final var participants = QSafetyTraining.safetyTraining.participants;
            final var participant = QSafetyTrainingParticipant.safetyTrainingParticipant;
            return QueryDslUtils.merge(predicate, JPAExpressions.selectOne().from(participants, participant)
                    .where(participant.staff.id.eq(SecurityUtilsExt.getStaffId())).exists());
        } else {
            predicate = Expressions.FALSE.isTrue();
        }
        if (leftJoin != null) {
            return QueryDslUtils.merge(predicate, leftJoin.where(staff.department.id.eq(departmentId)).exists());
        } else {
            return predicate;
        }
    }

    @Override
    protected boolean isAuthorized(Method method, SafetyTraining training) {
        final var departmentId = SecurityUtilsExt.getPrimaryDepartmentId();
        final var project = training.getProject();
        if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_RU_MANAGER)) {
            return project.getRuManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_OWNER_MANAGER)) {
            return project.getOwnerManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_SU_MANAGER,
                ConstructionProject.ROL_SUPERVISION_ENGINEER)) {
            return project.getSuManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_BU_MANAGER,
                ConstructionProject.ROL_QUALITY_INSPECTOR,
                ConstructionProject.ROL_SECURITY_OFFICER)) {
            return project.getBuManager().getDepartment().getId().equals(departmentId);
        } else if (SecurityUtils.hasAnyRole(ConstructionProject.ROL_WORKER)) {
            return training.getParticipants().stream()
                    .anyMatch(p -> p.getStaff().getId().equals(SecurityUtilsExt.getStaffId()));
        } else {
            return false;
        }
    }

}
