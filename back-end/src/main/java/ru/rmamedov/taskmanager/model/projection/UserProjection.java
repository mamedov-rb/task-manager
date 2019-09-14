package ru.rmamedov.taskmanager.model.projection;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

public interface UserProjection {

    String getId();

    String getUsername();

    String getFirstName();

    String getLastName();

    String getPhone();

    String getEmail();

    LocalDateTime getCreated();

    LocalDateTime getUpdated();

}
