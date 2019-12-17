package ru.rmamedov.taskmanager.model.DTO;

import ru.rmamedov.taskmanager.model.Role;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */

public interface UserPreviewDTO {

    String getFullName();

    String getUsername();

    Set<Role> getRoles();

}
