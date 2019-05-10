package ru.rmamedov.app.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.UserAlreadyExistsException;
import ru.rmamedov.app.exception.UserNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.User;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

public interface IUserService extends UserDetailsService, IBaseService<User> {

    @Transactional
    void addRoleAndUpdate(@NotNull final String roleName, @NotNull final String username) throws ProjectAlreadyExistsException;

    @Transactional
    void removeRoleAndUpdate(@NotNull final String roleName, @NotNull final String username) throws ProjectAlreadyExistsException;

    @NotNull
    @Transactional
    Project addProjectAndUpdate(@NotNull final Project projectId, @NotNull final String username) throws ProjectAlreadyExistsException;

    @Transactional
    void removeProjectAndUpdate(@NotNull final String projectId, @NotNull final String username) throws ProjectAlreadyExistsException;

    @NotNull
    @Override
    UserDetails loadUserByUsername(@NotNull final String username) throws UsernameNotFoundException;

    @NotNull
    @Override
    User findById(@NotNull final String id) throws UserNotFoundException;

    @NotNull
    User findByUsernameWithEagerRoles(@NotNull final String username) throws UserNotFoundException;

    @NotNull
    User findByUsernameWithEagerProjects(@NotNull final String username) throws UserNotFoundException;

    @NotNull
    User findByUsernameWithEagerTasks(@NotNull final String username) throws UserNotFoundException;

    @NotNull
    @Override
    List<User> findAll() throws UserNotFoundException;

    @NotNull
    @Override
    @Transactional
    User save(@NotNull final User user) throws DataIntegrityViolationException;

    @NotNull
    User saveWithSelfInjection(@NotNull final User user) throws UserAlreadyExistsException;

    @Override
    @Transactional
    void deleteById(@NotNull final String id);

    @Override
    @Transactional
    void deleteAll();

    @NotNull
    @Override
    @Transactional
    User update(@NotNull final User user);

    @NotNull
    @Override
    @Transactional
    User patch(@NotNull final User user);
}
