package ru.rmamedov.taskmanager.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.rmamedov.taskmanager.model.User;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

public interface CommentProjection {

    String getId();

    String getText();

//    User getCommentator();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime getCreated();

}
