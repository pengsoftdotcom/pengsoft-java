package com.pengsoft.ss.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project safety check
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class SafetyCheckFile extends EntityImpl {

    private static final long serialVersionUID = -6689260643319631444L;

	@NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private SafetyCheck check;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Asset file;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem type;

    public SafetyCheckFile(SafetyCheck check, Asset file, DictionaryItem type) {
        setCheck(check);
        setFile(file);
        setType(type);
    }

}
