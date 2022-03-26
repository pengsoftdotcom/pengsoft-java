package com.pengsoft.iot.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.system.domain.DictionaryItem;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Device product
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class Product extends EntityImpl {

    private static final long serialVersionUID = -3381982816066160971L;

	@NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem catalog;

    @NotBlank
    @Size(max = 255)
    private String name;

}
