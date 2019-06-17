package ru.rmamedov.app.controller.api;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.BadRequestException;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.UserNotAuthorizedException;
import ru.rmamedov.app.exception.UserNotFoundException;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IUserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Data
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    @PutMapping("/drop-project/{id}")
    public ResponseEntity dropProject(@PathVariable String id, @AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        boolean updated = userService.removeProjectAndUpdate(id, userPrincipal.getUsername());
        if (updated) {
            return new ResponseEntity(null, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/current")
    public ResponseEntity<UserDetails> findCurrentUser(@AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        return new ResponseEntity<>(userPrincipal, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable final String id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@Valid @RequestBody final User user) {
        if (user.getUsername() != null) {
            return new ResponseEntity<>(userService.saveWithSelfInjection(user), HttpStatus.OK);
        }
        throw new BadRequestException("Saving User should't be null!");
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody final User user) {
        if (user.getUsername() != null) {
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        }
        throw new BadRequestException("Updating User should't be null!");
    }

    @PatchMapping("/patch")
    public ResponseEntity<User> patch(@Valid @RequestBody final User user) {
        if (user.getUsername() != null) {
            return new ResponseEntity<>(userService.patch(user), HttpStatus.OK);
        }
        throw new BadRequestException("Updating User should't be null!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable final String id) { // TODO: before this, remove record from users_projects
        userService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
