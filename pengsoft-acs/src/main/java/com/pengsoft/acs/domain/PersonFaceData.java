package com.pengsoft.acs.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.basedata.domain.Person;
import com.pengsoft.basedata.json.ImageJsonDeserializer;
import com.pengsoft.basedata.json.ImageJsonSerializer;
import com.pengsoft.security.domain.OwnedEntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Person face data
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class PersonFaceData extends OwnedEntityImpl {

    @NotNull
    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Person person;

    @NotEmpty
    @JsonSerialize(using = ImageJsonSerializer.class)
    @JsonDeserialize(using = ImageJsonDeserializer.class)
    private byte[] face;

    @NotBlank
    @Size(max = 255)
    private String duration = "00:00:00,23:59:59";

}
