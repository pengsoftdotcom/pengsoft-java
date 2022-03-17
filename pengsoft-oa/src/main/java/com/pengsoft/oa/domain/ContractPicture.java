package com.pengsoft.oa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.system.domain.Asset;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract picture
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class ContractPicture extends OwnedExtEntityImpl {

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Contract contract;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset asset;

}
