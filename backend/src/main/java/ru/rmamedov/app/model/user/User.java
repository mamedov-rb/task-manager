package ru.rmamedov.app.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.rmamedov.app.model.Comment;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.Role;
import ru.rmamedov.app.model.Task;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "app_user")
@Entity
@ToString(of = {"comments", "tasks", "projects", "roles", "password"})
@EqualsAndHashCode()
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Version
    private int version;

    @Size(
            min = 3,
            max = 30,
            message = "Username should be not less than '3' and more than '30' characters!"
    )
    @Column(
            name = "username",
            nullable = false,
            unique = true,
            length = 30
    )
    @NotBlank
    private String username;

    @Size(
            min = 3,
            max = 60,
            message = "Password should be not less than '3' and more than '60' characters!"
    )
    @Column(
            name = "password",
            nullable = false
    )
    @NotBlank
    @JsonIgnore
    private String password;

    @Size(
            min = 3,
            max = 100,
            message = "Full name should be not less than '3' and more than '100' characters!"
    )
    @Column(
            name = "full_name",
            nullable = false,
            length = 100
    )
    @NotBlank
    private String fullName;

    @Column(
            name = "age",
            nullable = false
    )
    @Positive
    private int age;

    @Column(
            name = "phone",
            nullable = false
    )
    @NotBlank
    @Pattern(
            message = "Phone should be correct!",
            regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")
    private String phone;

    @Column(name = "email", nullable = false)
    @Email(message = "Email should be correct")
    @NotBlank
    private String email;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy HH:mm:ss")
    @Column(name = "registered")
    private LocalDateTime registered;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy HH:mm:ss")
    @Column(name = "updated")
    private LocalDateTime updated;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            })
    @JoinTable(
            name = "users_roles",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"user_id", "role_id"})
            },
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            })
    @JoinTable(
            name = "users_projects",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {
                            "user_id",
                            "project_id"
                    })
            },
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            }
    )
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(
            mappedBy = "owner",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            }
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    public User(String username,
                String password, String fullName,
                int age, String phone, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    public User(String username,
                String password, String fullName,
                int age, String phone,
                String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
    }

}
