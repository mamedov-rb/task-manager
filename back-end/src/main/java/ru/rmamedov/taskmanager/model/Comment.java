package ru.rmamedov.taskmanager.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "comment")
@Entity
@ToString(exclude = {"commentator"})
@EqualsAndHashCode(of = {"name", "text"})
public class Comment {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Version
    private int version;

    @Size(min = 3, max = 30, message = "Comment name should be not less than '3' and more than '30' characters!")
    @Column(name = "name", unique = true, nullable = false, length = 30)
    @NotBlank
    private String name;

    @Size(min = 3, message = "Comment text should be not less than '3'!")
    @Column(columnDefinition = "TEXT", name = "description", nullable = false)
    @NotBlank
    private String text;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id")
    private User commentator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
