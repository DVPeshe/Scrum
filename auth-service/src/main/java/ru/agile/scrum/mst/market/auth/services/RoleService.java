package ru.agile.scrum.mst.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.auth.entities.Role;
import ru.agile.scrum.mst.market.auth.repositories.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN").get();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<String> getAllRolesStr() {
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }

    public List<String> getAllRoleTitles() {
        return roleRepository.findAll().stream().map(Role::getTitle).toList();
    }
}
