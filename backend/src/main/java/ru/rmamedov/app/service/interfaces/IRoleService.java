package ru.rmamedov.app.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;
import ru.rmamedov.app.model.user.Role;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

public interface IRoleService {

    @NotNull
    List<Role> findAll() throws RoleNotFoundException;

    @NotNull
    Role findByName(@NotNull final String name) throws RoleNotFoundException;

    @NotNull
    Role findByNameWithEagerUsers(@NotNull final String name) throws RoleNotFoundException;

    @NotNull
    @Transactional
    Role save(@NotNull final Role role) throws RoleAlreadyExistsException;

    @NotNull
    Role saveWithSelfInjection(@NotNull final Role role) throws RoleAlreadyExistsException;

    @Transactional
    void deleteByName(@NotNull final String name);

    @Transactional
    void deleteAll();
}
