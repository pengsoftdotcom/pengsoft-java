package com.pengsoft.ss.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.basedata.domain.Staff;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
@Table(indexes = {
        @Index(name = "safety_training_participant_1", columnList = "training_id, staff_id", unique = true)
})
public class SafetyTrainingParticipant extends OwnedExtEntityImpl {

    private static final long serialVersionUID = -875092997387906731L;

	@NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private SafetyTraining training;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Staff staff;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem status;

    private LocalDateTime confirmedAt;

    @Size(max = 255)
    private String reason;

    public SafetyTrainingParticipant(SafetyTraining training, Staff staff) {
        setTraining(training);
        setStaff(staff);
    }

}
