package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.agile.scrum.mst.market.auth.services.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles/forAdmin")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<String> getAllRolesUser() {
        return roleService.getAllRolesStr();
    }
}
