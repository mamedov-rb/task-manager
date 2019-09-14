package ru.rmamedov.taskmanager.model.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.rmamedov.taskmanager.model.Project;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

@Data
public class ProjectDTO {

    private String id;

    private String name;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime updated;

    private String createdBy;

    public static ProjectDTO of(final Project project) {
        final ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setCreated(project.getCreated());
        dto.setUpdated(project.getUpdated());
        dto.setCreatedBy(project.getCreatedBy().getUsername());
        return dto;
    }

}
