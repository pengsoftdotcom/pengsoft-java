package com.pengsoft.oa.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pengsoft.basedata.domain.OwnedExtEntityImpl;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Contract
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class Contract extends OwnedExtEntityImpl {

    private static final long serialVersionUID = -2346858411026418246L;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem partyAType;

    @NotBlank
    private String partyAId;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem partyBType;

    @NotBlank
    private String partyBId;

    private LocalDate signedAt;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem status;

    private LocalDateTime confirmedAt;

    private String confirmedBy;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "contract")
    private List<ContractPicture> pictures = new ArrayList<>();

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "contract")
    private List<ContractConfirmPicture> confirmPictures = new ArrayList<>();

}
