package com.pengsoft.iot.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pengsoft.support.domain.EntityImpl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Many-to-many relationship between {@link Group} and {@link Device}.
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
@Table(indexes = {
        @Index(name = "group_device_group_id_device_id", columnList = "group_id, device_id", unique = true) })
public class GroupDevice extends EntityImpl {

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Group group;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Device device;

}
