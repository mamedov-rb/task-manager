package ru.rmamedov.app.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;
import ru.rmamedov.app.model.user.Role;
import ru.rmamedov.app.repository.IRoleRepository;
import ru.rmamedov.app.service.interfaces.IRoleService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleService roleService;

    private IRoleRepository roleRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public List<Role> findAll() throws RoleNotFoundException {
        final List<Role> roles = roleRepository.findAll();
        if (roles == null || roles.isEmpty()) {
            throw new RoleNotFoundException("There are is no any roles!");
        }
        return Collections.unmodifiableList(roles);
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public Role findByName(@NotNull String name) throws RoleNotFoundException {
        final Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        throw new RoleNotFoundException("Role with name: '" + name + "' - Not Found!");
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public Role findByNameWithEagerUsers(@NotNull String name) throws RoleNotFoundException {
        final Optional<Role> optionalRole = roleRepository.findByNameWithEagerUsers(name);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        throw new RoleNotFoundException("Role with name: '" + name + "' - Not Found!");
    }

    @NotNull
    @Override
    @Transactional
    public Role save(@NotNull Role role) throws RoleAlreadyExistsException {
        return roleRepository.save(role);
    }

    @NotNull
    @Override
    public Role saveWithSelfInjection(@NotNull Role role) throws RoleAlreadyExistsException {
        try {
            return roleService.save(role);
        } catch (DataIntegrityViolationException ex) {
            throw new RoleAlreadyExistsException("Role with name: '" + role.getName() + "' - Not saved!");
        }
    }

    @Override
    @Transactional
    public void deleteByName(@NotNull String name) {
        roleRepository.delete(findByName(name));
    }

    @Override
    @Transactional
    public void deleteAll() {
        roleRepository.deleteAll();
    }
}
