package com.pengsoft.acs.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.json.ImageJsonDeserializer;
import com.pengsoft.basedata.json.ImageJsonSerializer;
import com.pengsoft.iot.domain.Device;
import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Access record
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class AccessRecord extends EntityImpl {

    private static final long serialVersionUID = 4643825005175238950L;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Person person;

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Device device;

    @JsonSerialize(using = ImageJsonSerializer.class)
    @JsonDeserialize(using = ImageJsonDeserializer.class)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    private float temperature;

}
