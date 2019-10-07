package ru.rmamedov.taskmanager.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rmamedov.taskmanager.model.Task;

import javax.validation.Valid;

/**
 * @author Rustam Mamedov
 */

@Valid
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskRequest {

    @Valid
    private Task task;

    private String projectId;

    private String assignTo;

}
