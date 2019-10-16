package ru.rmamedov.taskmanager.model.DTO;

import ru.rmamedov.taskmanager.model.enums.Status;

/**
 * @author Rustam Mamedov
 */

public interface TaskProjection {

    String getId();

    String getName();

    String getDescription();

    Status getStatus();

}
