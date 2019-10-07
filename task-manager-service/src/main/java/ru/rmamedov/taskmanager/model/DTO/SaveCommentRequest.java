package ru.rmamedov.taskmanager.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rmamedov.taskmanager.model.Comment;

import javax.validation.Valid;

/**
 * @author Rustam Mamedov
 */

@Valid
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveCommentRequest {

    @Valid
    private Comment comment;

    private String taskId;

}
