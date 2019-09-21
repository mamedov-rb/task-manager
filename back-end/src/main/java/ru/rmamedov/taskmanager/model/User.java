package ru.rmamedov.taskmanager.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "app_user")
@Entity
@ToString(of = {"projects", "roles", "password"})
@NoArgsConstructor
@EqualsAndHashCode(of = {"username"})
public class User implements UserDetails {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Version
    private int version;

    @Size(min = 3, max = 30, message = "Username should be not less than '3' and more than '30' characters!")
    @Column(name = "assignTo", nullable = false, unique = true, length = 30)
    @NotBlank
    private String username;

    @Size(min = 3, message = "Password should be not less than '3' characters!")
    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Size(min = 3, max = 50, message = "First name should be not less than '3' and more than '50' characters!")
    @Column(name = "first_name", nullable = false, length = 100)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 50, message = "Last name should be not less than '3' and more than '50' characters!")
    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank
    private String lastName;

    @Column(name = "phone", nullable = false)
    @NotBlank
    @Pattern(message = "Phone should be in correct format!",
            regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")
    private String phone;

    @Column(name = "email", nullable = false)
    @Email(message = "Email address should be in correct format")
    @NotBlank
    private String email;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "updated")
    @UpdateTimestamp
    private LocalDateTime updated;

    @JoinTable(
            name = "users_roles",
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})},
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @JoinTable(
            name = "users_projects",
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "project_id"})},
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")
    )
    @ManyToMany
    private Set<Project> projects = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final HashSet<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    private User(String username,
                 String password,
                 String firstName,
                 String lastName,
                 String phone,
                 String email) {

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public boolean addProject(final Project project) {
        return project.addUser(this) && projects.add(project);
    }

}
