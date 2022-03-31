package com.pengsoft.task.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pengsoft.security.domain.OwnedEntityImpl;
import com.pengsoft.support.domain.Param;
import com.pengsoft.system.domain.DictionaryItem;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Construction project
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Task extends OwnedEntityImpl {

    @Size(max = 255)
    @NotBlank
    private String name;

    @Size(max = 500)
    @NotBlank
    private String content;

    private String targetPath;

    private String targetId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Param targetParam;

    private String finishedBy;

    private LocalDateTime finishedAt;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem status;

    @NotNull
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private DictionaryItem priority;

}
