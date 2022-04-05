package com.pengsoft.ss.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.support.util.DateUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project safety training
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SafetyTraining extends EntityImpl implements Codeable {

    private static final long serialVersionUID = -7377239434296886716L;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private ConstructionProject project;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff trainer;

    @Size(max = 255)
    @NotBlank
    private String code;

    @Size(max = 255)
    @NotBlank
    private String subject = "例行安全培训";

    @NotBlank
    @Length(max = 255)
    private String address;

    private boolean allWorkers = true;

    @NotNull
    private LocalDateTime estimatedStartTime = DateUtils.atStartOfToday().plusHours(9);

    @NotNull
    private LocalDateTime estimatedEndTime = DateUtils.atStartOfToday().plusHours(10);

    private LocalDateTime submittedAt;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "training")
    private List<SafetyTrainingFile> files = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "training", cascade = CascadeType.REMOVE)
    private List<SafetyTrainingParticipant> participants = new ArrayList<>();

}
