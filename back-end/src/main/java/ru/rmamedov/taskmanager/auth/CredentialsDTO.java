package ru.rmamedov.taskmanager.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Rustam Mamedov
 */

@Getter
@NoArgsConstructor
class CredentialsDTO {

    private String username;

    private String password;

}
