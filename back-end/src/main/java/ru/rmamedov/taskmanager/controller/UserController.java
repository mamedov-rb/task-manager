package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.model.DTO.UserDTO;
import ru.rmamedov.taskmanager.service.UserProjectService;
import ru.rmamedov.taskmanager.service.UserService;

import javax.validation.Valid;

/**
 * @author Rustam Mamedov
 */

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserProjectService userProjectService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity create(@RequestBody @Valid final User user) {
        userService.save(user);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping(value = "/find/{username}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDTO> findByUsername(@PathVariable final String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @PatchMapping("/assign-to-project/username/{username}/id/{id}")
    public ResponseEntity assignToUser(@PathVariable final String username, @PathVariable String id) {
        final boolean added = userProjectService.assignUserToProject(username, id);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

}
