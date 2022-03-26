package com.pengsoft.system.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.Codeable;
import com.pengsoft.support.domain.Sortable;
import com.pengsoft.support.domain.TreeEntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "dictionary_item_type_id", columnList = "type_id, parent_id, code", unique = true) })
public class DictionaryItem extends TreeEntityImpl<DictionaryItem> implements Codeable, Sortable {

    private static final long serialVersionUID = -3387204701275785327L;

    @NotBlank
    @Size(max = 255)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    private long sequence;

    @Size(max = 255)
    private String remark;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryType type;

}
