package ru.rmamedov.taskmanager.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.rmamedov.taskmanager.model.enums.Status;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

@Data
public class TaskDTO {

    private String name;

    private String description;

    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime endDate;

}
