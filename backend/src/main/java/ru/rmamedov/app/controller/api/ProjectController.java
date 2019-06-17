package ru.rmamedov.app.controller.api;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.BadRequestException;
import ru.rmamedov.app.exception.UserNotAuthorizedException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.IUserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Data
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final IProjectService projectService;

    private final IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<Project>> findAll() {
        return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/all-of-user")
    public ResponseEntity<List<Project>> findAllOfCurrentUser(@AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        return new ResponseEntity<>(projectService.findAllOfCurrentUser(userPrincipal.getUser().getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable final String id) {
        return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Project> save(@Valid @RequestBody final Project project, @AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        return new ResponseEntity<>(userService.addProjectAndUpdate(project, userPrincipal.getUsername()), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> update(@Valid @RequestBody final Project project) {
        if (project.getName() != null) {
            return new ResponseEntity<>(projectService.update(project), HttpStatus.OK);
        }
        throw new BadRequestException("Updating project should't be null!");
    }

    @PatchMapping("/patch")
    public ResponseEntity<Project> patch(@Valid @RequestBody final Project project) {
        if (project.getName() != null) {
            return new ResponseEntity<>(projectService.patch(project), HttpStatus.OK);
        }
        throw new BadRequestException("Updating project should't be null!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Project> delete(@PathVariable final String id, @AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        boolean deleted = projectService.deleteByIdUnderUser(id, userPrincipal.getUser().getUsername());
        if (deleted) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }
}
