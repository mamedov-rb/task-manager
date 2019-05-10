package ru.rmamedov.app.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.*;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.model.user.Role;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.repository.IUserRepository;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.IRoleService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;
    private PasswordEncoder encoder;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @NotNull
    @Override
    @Transactional
    public Project addProjectAndUpdate(@NotNull final Project project, @NotNull final String username) throws ProjectAlreadyExistsException {
        final Project savedProjectWithUsers = projectService.saveWithSelfInjection(project);

        final User user = findByUsernameWithEagerProjects(username);
        final Set<Project> projectsOfUser = user.getProjects();
        final Set<User> usersOfProject = savedProjectWithUsers.getUsers();

        if (projectsOfUser.contains(savedProjectWithUsers) || usersOfProject.contains(user)) {
            throw new ProjectAlreadyExistsException("User with username: '" + user.getUsername() +
                    "' - Already has Project with ID: '" + savedProjectWithUsers.getId() + "'!");
        }
        projectsOfUser.add(savedProjectWithUsers);
        usersOfProject.add(user);
        update(user);
        return savedProjectWithUsers;
    }

    @Override
    @Transactional
    public void removeProjectAndUpdate(@NotNull final String projectId, @NotNull final String username) throws ProjectAlreadyExistsException {
        final Project project = projectService.findByIdWithEagerUsers(projectId);
        final User user = findByUsernameWithEagerProjects(username);

        final Set<Project> projectsOfUser = user.getProjects();
        final Set<User> usersOfProject = project.getUsers();

        if (!projectsOfUser.contains(project) || !usersOfProject.contains(user)) {
            throw new ProjectNotFoundException("User with username: '" + user.getUsername() + "' - Has no Project with ID: '" + project.getId() + "'!");
        }
        projectsOfUser.remove(project);
        usersOfProject.remove(user);
        update(user);
    }

    @Override
    @Transactional
    public void addRoleAndUpdate(@NotNull final String roleName, @NotNull final String username) throws ProjectAlreadyExistsException {
        final Role role = roleService.findByNameWithEagerUsers(roleName);
        final User user = findByUsernameWithEagerRoles(username);

        final Set<Role> rolesOfUser = user.getRoles();
        final Set<User> usersOfRole = role.getUsers();

        if (rolesOfUser.contains(role) || usersOfRole.contains(user)) {
            throw new RoleAlreadyExistsException("User with username: '" + username +
                    "' - Already has Role with Name: '" + roleName + "'!");
        }
        rolesOfUser.add(role);
        usersOfRole.add(user);
        update(user);
    }

    @Override
    @Transactional
    public void removeRoleAndUpdate(@NotNull final String roleName, @NotNull final String username) throws ProjectAlreadyExistsException {
        final Role role = roleService.findByNameWithEagerUsers(roleName);
        final User user = findByUsernameWithEagerRoles(username);

        final Set<Role> rolesOfUser = user.getRoles();
        final Set<User> usersOfRole = role.getUsers();

        if (!rolesOfUser.contains(role) || !usersOfRole.contains(user)) {
            throw new RoleNotFoundException("User with username: '" + username + "' - Has no Role with Name: '" + roleName + "'!");
        }
        if (rolesOfUser.size() > 1) {
            rolesOfUser.remove(role);
            usersOfRole.remove(user);
            update(user);
        } else {
            throw new UserCantBeWithoutRoleException("Can't remove last Role '" + roleName
                    + "' from User - '" + username + "'!");
        }
    }

    @NotNull
    @Override
    public AppUserPrincipal loadUserByUsername(@NotNull final String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.loadByUsernameWithEagerRoles(username);
        if (optionalUser.isPresent()) {
            return new AppUserPrincipal(optionalUser.get());
        }
        throw new UsernameNotFoundException("Username: '" + username + "' - Not Found!");
    }

    @NotNull
    @Override
    public User findById(@NotNull final String id) throws UserNotFoundException {
        final Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("User with ID: '" + id + "' - Not Found!");
    }

    @NotNull
    @Override
    public User findByUsernameWithEagerRoles(@NotNull final String username) throws UserNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsernameWithEagerRoles(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("User with username: '" + username + "' - Not Found!");
    }

    @NotNull
    @Override
    public User findByUsernameWithEagerProjects(@NotNull String username) throws UserNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsernameWithEagerProjects(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("User with username: '" + username + "' - Not Found!");
    }

    @NotNull
    @Override
    public User findByUsernameWithEagerTasks(@NotNull String username) throws UserNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsernameWithEagerTasks(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("User with username: '" + username + "' - Not Found!");
    }

    @NotNull
    @Override
    public List<User> findAll() throws UserNotFoundException {
        final List<User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("There are is no any users!");
        }
        return Collections.unmodifiableList(users);
    }

    @NotNull
    @Override
    @Transactional
    public User save(@NotNull final User user) throws DataIntegrityViolationException {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @NotNull
    @Override
    public User saveWithSelfInjection(@NotNull final User user) throws UserAlreadyExistsException {
        try {
            return userService.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistsException("User with username: '" + user.getUsername() + "' - Not saved!");
        }
    }

    @Override
    @Transactional
    public void deleteById(@NotNull final String id) {
        userRepository.delete(findById(id));
    }

    @Override
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @NotNull
    @Override
    @Transactional
    public User update(@NotNull User user) {
        return userRepository.saveAndFlush(user);
    }

    @NotNull
    @Override
    @Transactional
    public User patch(@NotNull final User updated) {
        final User fromDb = findById(updated.getId());
        if (updated.getUsername() != null && !updated.getUsername().equals(fromDb.getUsername())) {
            fromDb.setUsername(updated.getUsername());
        }
        if (updated.getPassword() != null && !updated.getPassword().equals(fromDb.getPassword())) {
            fromDb.setPassword(updated.getPassword());
        }
        if (updated.getFullName() != null && !updated.getFullName().equals(fromDb.getFullName())) {
            fromDb.setFullName(updated.getFullName());
        }
        if (updated.getAge() > 0 && updated.getAge() != fromDb.getAge()) {
            fromDb.setAge(updated.getAge());
        }
        if (updated.getPhone() != null && !updated.getPhone().equals(fromDb.getPhone())) {
            fromDb.setPhone(updated.getPhone());
        }
        if (updated.getEmail() != null && !updated.getEmail().equals(fromDb.getEmail())) {
            fromDb.setEmail(updated.getEmail());
        }
        return userRepository.saveAndFlush(fromDb);
    }
}
