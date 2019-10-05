package ru.rmamedov.taskmanager.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.rmamedov.taskmanager.model.enums.Status;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

public interface TaskDetailsProjection {

    String getName();

    String getDescription();

    Status getStatus();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime getCreated();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime getUpdated();

}
