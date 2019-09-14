package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.ProjectDTO;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.service.ProjectService;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity create(@RequestBody @Valid final Project project,
                                 @Nullable @AuthenticationPrincipal Authentication authentication) {

        projectService.create(project, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ProjectDTO> findById(@NotNull @PathVariable final String id) {
        return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/find/all/by/user/{username}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<ProjectDTO>> findAllByUserId(@NotNull @PathVariable final String username) {
        return new ResponseEntity<>(projectService.findAllByUsername(username), HttpStatus.OK);
    }

    @PatchMapping("/assign-to-user/{id}")
    public ResponseEntity assignToUser(@PathVariable String id,
                                       @Nullable @AuthenticationPrincipal Authentication authentication) {

        projectService.assignToUserById(id, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
