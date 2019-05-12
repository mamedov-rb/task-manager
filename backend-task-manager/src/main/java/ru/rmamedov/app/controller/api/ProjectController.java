package ru.rmamedov.app.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private IProjectService projectService;

    private IUserService userService;

    @Autowired
    public ProjectController(IProjectService projectService, IUserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/all-of-user")
    public ResponseEntity<List<Project>> findAllOfCurrentUser(@AuthenticationPrincipal AppUserPrincipal userPrincipal) {
        final String userId = userPrincipal.getUser().getId();
        try {
            return new ResponseEntity<>(projectService.findAllOfCurrentUser(userId), HttpStatus.OK);
        } catch (ProjectNotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable final String id) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Project> save(@RequestBody final Project project, @AuthenticationPrincipal AppUserPrincipal userPrincipal) {
        if (project != null && userPrincipal.getUsername() != null) {
            try {
                return new ResponseEntity<>(userService.addProjectAndUpdate(project, userPrincipal.getUsername()), HttpStatus.OK);
            } catch (ProjectAlreadyExistsException ex) {
                ex.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> update(@RequestBody final Project project) {
        if (project == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(projectService.update(project), HttpStatus.OK);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Project> patch(@RequestBody final Project project) {
        if (project == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(projectService.patch(project), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Project> delete(@PathVariable final String id, final Authentication authentication) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        boolean deleted = projectService.deleteById(id, authentication.getName());
        if (deleted) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }
}
