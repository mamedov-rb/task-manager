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
import ru.rmamedov.taskmanager.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(
        name = "task",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"name", "description"})
)
@Entity
@ToString(exclude = {"createdBy", "assignedTo", "project", "comments"})
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "description"})
public class Task {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Version
    private int version;

    @Size(min = 3, max = 30, message = "Task name should be not less than '3' and more than '30' characters!")
    @Column(name = "name", nullable = false, length = 30)
    @NotBlank
    private String name;

    @Size(min = 3, max = 2500, message = "Task description should be not less than '3' and more than '2500' characters!")
    @Column(name = "description", nullable = false, length = 3000)
    @NotBlank
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "updated")
    @UpdateTimestamp
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo;

    @OneToMany(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Comment> comments = new HashSet<>();

    @Builder
    private Task(String name,
                String description,
                Status status) {

        this.name = name;
        this.description = description;
        this.status = status;
    }

    public boolean addComment(final Comment comment) {
        comment.setTask(this);
        return comments.add(comment);
    }

    public boolean removeComment(final Comment comment) {
        comment.setCommentator(null);
        return comments.remove(comment);
    }
}
