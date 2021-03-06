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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.ProjectDetailsProjection;
import ru.rmamedov.taskmanager.model.DTO.ProjectProjection;
import ru.rmamedov.taskmanager.service.ProjectService;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ProjectDetailsProjection> findById(@NotNull @PathVariable final String id) {
        return new ResponseEntity<>(projectService.findDTOById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<ProjectProjection>> findAllByUserId(@Nullable @AuthenticationPrincipal Authentication authentication) {
        return new ResponseEntity<>(projectService.findAllOfCurrentUser(authentication), HttpStatus.OK);
    }

    @GetMapping("/is-member")
    public String containsUser(@Nullable @AuthenticationPrincipal Authentication authentication) {
        final boolean memberOf = projectService.isMemberOf(authentication);
        if (memberOf) {
            return Boolean.TRUE.toString();
        }
        return Boolean.FALSE.toString();
    }

}
