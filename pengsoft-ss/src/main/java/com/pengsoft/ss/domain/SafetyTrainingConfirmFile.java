package com.pengsoft.ss.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.system.domain.Asset;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project safety training confirm file
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SafetyTrainingConfirmFile extends EntityImpl {

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private SafetyTraining training;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset file;

    public SafetyTrainingConfirmFile(SafetyTraining training, Asset file) {
        setTraining(training);
        setFile(file);
    }

}
