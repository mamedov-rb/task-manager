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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "project")
@Entity
@ToString(exclude = {"users", "createdBy"})
@EqualsAndHashCode(of = {"name", "description"})
@NoArgsConstructor
public class Project {

    @Id
    @Column(
            name = "id",
            updatable = false,
            nullable = false
    )
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Version
    private int version;

    @Size(min = 3, max = 30, message = "Project name should be not less than '3' and more than '30' characters!")
    @Column(name = "name", nullable = false, unique = true, length = 30)
    @NotBlank
    private String name;

    @Size(min = 3, max = 2500, message = "Project description should be not less than '3' and more than '2500' characters!")
    @Column(name = "description", nullable = false, length = 2500)
    @NotBlank
    private String description;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "updated")
    @UpdateTimestamp
    private LocalDateTime updated;

    @Column(name = "start_date")
    @FutureOrPresent
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @Future
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();

    @Builder
    public Project (String name,
                    String description,
                    LocalDateTime startDate,
                    LocalDateTime endDate,
                    User createdBy) {

        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
    }

}
