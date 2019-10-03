package ru.rmamedov.taskmanager.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@EqualsAndHashCode(of = {"text"})
@NoArgsConstructor
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

    @Size(min = 5, message = "Comment text should be not less than '5'")
    @Column(columnDefinition = "TEXT", name = "text", nullable = false)
    @NotBlank
    private String text;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id", nullable = false)
    private User commentator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public Comment(String text) {
        this.text = text;
    }

}
