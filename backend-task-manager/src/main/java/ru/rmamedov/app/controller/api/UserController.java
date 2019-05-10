package ru.rmamedov.app.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.UserNotFoundException;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/current")
    public ResponseEntity<UserDetails> findCurrentUser(final Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>((AppUserPrincipal) authentication.getPrincipal(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        try {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable final String id) {
        if (id == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody final User user) {
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(userService.saveWithSelfInjection(user), HttpStatus.OK);
        } catch (ProjectAlreadyExistsException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody final User user) {
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @PatchMapping("/patch")
    public ResponseEntity<User> patch(@RequestBody final User user) {
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(userService.patch(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable final String id) {
        if (id == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        userService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
