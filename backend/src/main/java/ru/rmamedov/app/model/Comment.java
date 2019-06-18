package ru.rmamedov.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.rmamedov.app.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "comment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @Size(
            min = 3,
            max = 30,
            message = "Comment name should be not less than '3' and more than '30' characters!"
    )
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 30
    )
    @NotBlank
    private String name;

    @Size(
            min = 3,
            message = "Comment text should be not less than '3'!"
    )
    @Column(columnDefinition = "TEXT",
            name = "description",
            nullable = false
    )
    @NotBlank
    private String text;

    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime created;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return name.equals(comment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", created=" + created +
                '}';
    }
}
