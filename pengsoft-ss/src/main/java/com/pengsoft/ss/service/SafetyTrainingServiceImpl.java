package com.pengsoft.ss.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import com.pengsoft.basedata.domain.QStaff;
import com.pengsoft.basedata.repository.StaffRepository;
import com.pengsoft.basedata.util.SecurityUtilsExt;
import com.pengsoft.ss.domain.SafetyTraining;
import com.pengsoft.ss.domain.SafetyTrainingFile;
import com.pengsoft.ss.domain.SafetyTrainingParticipant;
import com.pengsoft.ss.repository.SafetyTrainingFileRepository;
import com.pengsoft.ss.repository.SafetyTrainingParticipantRepository;
import com.pengsoft.ss.repository.SafetyTrainingRepository;
import com.pengsoft.support.exception.BusinessException;
import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.support.util.DateUtils;
import com.pengsoft.support.util.EntityUtils;
import com.pengsoft.system.domain.Asset;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link SafetyTrainingService} based on
 * JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class SafetyTrainingServiceImpl extends
        EntityServiceImpl<SafetyTrainingRepository, SafetyTraining, String>
        implements SafetyTrainingService {

    @Inject
    private SafetyTrainingFileRepository fileRepository;

    @Inject
    private SafetyTrainingParticipantRepository participantRepository;

    @Inject
    private StaffRepository staffRepository;

    @Override
    public SafetyTraining save(final SafetyTraining target) {
        findOneByCode(target.getCode()).ifPresent(source -> {
            if (EntityUtils.notEquals(source, target)) {
                throw getExceptions().constraintViolated("code", "exists", target.getCode());
            }
        });
        return super.save(target);
    }

    @Override
    public void saveAndSubmit(final SafetyTraining training) {
        submit(save(training));
    }

    @Override
    public void submit(final SafetyTraining training) {
        if (training.getSubmittedAt() != null) {
            throw new BusinessException("training.submit.already");
        }
        if (!participantRepository.existsByTrainingId(training.getId()) && training.isAllWorkers()) {
            final var root = QStaff.staff;
            final var staffs = staffRepository.findAll(
                    root.job.name.eq("工人").and(root.department.id.eq(SecurityUtilsExt.getPrimaryDepartmentId())));
            final var participants = StreamSupport.stream(staffs.spliterator(), false)
                    .map(staff -> new SafetyTrainingParticipant(training, staff)).toList();
            participantRepository.saveAll(participants);
            training.setParticipants(new ArrayList<>(participants));
        }
        if (participantRepository.existsByTrainingId(training.getId())) {
            training.setSubmittedAt(DateUtils.currentDateTime());
            save(training);
        } else {
            throw new BusinessException("training.submit.no-participants");
        }
    }

    @Override
    public void start(SafetyTraining training) {
        if (training.getSubmittedAt() == null) {
            throw new BusinessException("training.start.not-submitted");
        }
        if (training.getStartedAt() != null) {
            throw new BusinessException("training.start.already");
        }
        training.setStartedAt(DateUtils.currentDateTime());
        super.save(training);
    }

    @Override
    public void end(SafetyTraining training, List<Asset> files) {
        if (training.getStartedAt() == null) {
            throw new BusinessException("training.end.not-started");
        }
        if (training.getEndedAt() != null) {
            throw new BusinessException("training.end.already");
        }
        training.setEndedAt(DateUtils.currentDateTime());
        super.save(training);
        if (CollectionUtils.isNotEmpty(files)) {
            final var trainingFiles = files.stream()
                    .map(file -> new SafetyTrainingFile(training, file)).toList();
            fileRepository.saveAll(trainingFiles);
        }
    }

    @Override
    public Optional<SafetyTraining> findOneByCode(@NotBlank final String code) {
        return getRepository().findOneByCode(code);
    }

}
