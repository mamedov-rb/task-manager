package ru.rmamedov.taskmanager.model.DTO;

import lombok.Getter;

/**
 * @author Rustam Mamedov
 */

@Getter
public class UserQuickPreview {

    private String id;

    private String fullName;

    public UserQuickPreview(String id, String firstName, String lastName) {
        this.id = id;
        this.fullName = String.format("%s %s", firstName, lastName);
    }

}
