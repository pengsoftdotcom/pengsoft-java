package com.pengsoft.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.security.validation.Password;
import com.pengsoft.security.validation.Username;
import com.pengsoft.support.domain.Enable;
import com.pengsoft.support.domain.EntityImpl;
import com.pengsoft.security.json.MobileJsonSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Basic user account information.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(indexes = { @Index(name = "user_username", columnList = "username", unique = true),
        @Index(name = "user_mobile", columnList = "username", unique = true),
        @Index(name = "user_email", columnList = "email", unique = true),
        @Index(name = "user_weixin_mp_openId", columnList = "weixinMpOpenId", unique = true),
        @Index(name = "user_expired_at", columnList = "expiredAt") })
public class User extends EntityImpl implements Enable {

    private static final long serialVersionUID = -6068580816386465274L;

    public interface Create {
    }

    @Username(groups = Create.class)
    @Column(updatable = false)
    private String username;

    @Size(max = 255)
    @JsonSerialize(using = MobileJsonSerializer.class)
    @Column(updatable = false)
    private String mobile;

    @Email
    @Size(max = 255)
    @Column(updatable = false)
    private String email;

    @Size(max = 255)
    @Column(updatable = false)
    private String weixinMpOpenId;

    @Password(groups = Create.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(updatable = false)
    private String password;

    @Size(max = 255)
    private String locale = Locale.SIMPLIFIED_CHINESE.toString();

    private LocalDateTime signedInAt;

    @Min(0)
    private long signInFailureCount;

    private LocalDateTime expiredAt;

    @Column(updatable = false)
    private boolean enabled = true;

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<UserRole> userRoles = new ArrayList<>();

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

}
