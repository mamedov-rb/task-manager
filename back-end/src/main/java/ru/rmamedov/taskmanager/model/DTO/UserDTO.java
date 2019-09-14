package ru.rmamedov.taskmanager.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.rmamedov.taskmanager.model.User;

import java.time.LocalDateTime;

/**
 * @author Rustam Mamedov
 */

@Data
public class UserDTO {

    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime updated;

    public static UserDTO of(final User user) {
        final UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCreated(user.getCreated());
        dto.setUpdated(user.getUpdated());
        return dto;
    }

}
