package ru.rmamedov.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.rmamedov.app.config.json.CustomLocalDateDeserializer;
import ru.rmamedov.app.config.json.CustomLocalDateSerializer;
import ru.rmamedov.app.model.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Data
@Table(name = "task")
@Entity
@ToString(exclude = {"user", "project", "comments"})
@EqualsAndHashCode(of = {"name", "created"})
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

    @Size(
            min = 3,
            max = 30,
            message = "Task name should be not less than '3' and more than '30' characters!"
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
            max = 2500,
            message = "Task description should be not less than '3' and more than '2500' characters!"
    )
    @Column(
            name = "description",
            nullable = false,
            length = 2500
    )
    @NotBlank
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PLANNED;

    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updated;

    @Column(name = "start_date")
    @FutureOrPresent
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate startDate;

    @Column(name = "end_date")
    @Future
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    public Task(String name,
                String description,
                LocalDate startDate,
                LocalDate endDate) {

        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
