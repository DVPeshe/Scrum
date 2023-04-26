package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.*;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.Specifications.UsersSpecifications;
import ru.agile.scrum.mst.market.auth.services.UserService;
import ru.agile.scrum.mst.market.auth.validation.JsonUserRegistrationFormValidationRulesEngine;
import ru.agile.scrum.mst.market.auth.validation.JsonUserUpdateFormValidationRulesEngine;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JsonUserUpdateFormValidationRulesEngine userUpdateFormValidationRulesEngine;
    private final JsonUserRegistrationFormValidationRulesEngine userRegistrationValidationRulesEngine;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public Page<UserDto> getAllUsers(
            @RequestParam(name = "p", defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "title_part", required = false) String titlePart
    ) {
        if (page < 1) {
            page = 1;
        }
        Specification<User> spec = Specification.where(null);
        if (titlePart != null) {
            spec = spec.and(UsersSpecifications.titleLike(titlePart));
        }
        return userService.findAll(page - 1, pageSize, spec).map(userMapper::mapUserToUserDto);
    }

    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
    @PutMapping("/{id}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editRole(@PathVariable Long id, @RequestBody UserDtoRoles userDtoRoles) {
        userService.editRole(userDtoRoles, id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/ban")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void banUser(@PathVariable Long id) {
        userService.updateAccessUser(id, false);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/unban")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unbanUser(@PathVariable Long id) {
        userService.updateAccessUser(id, true);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/my")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserData(@RequestBody UserPersonalAccountRequest form, Principal principal) {
        userUpdateFormValidationRulesEngine.check(form);
        userService.updateUser(form, principal.getName());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/personal-data/my")
    public UserPersonalAccountResponse getUserPersonalData(Principal principal) {
        final String username = principal.getName();
        return UserPersonalAccountResponse.builder()
                .username(username)
                .email(userService.getUserEmailByName(username))
                .fullName(userService.getFullNameByName(username))
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/role-titles/my")
    public RoleTitlesResponse getUserRoles(Principal principal) {
        return userService.getUserRoles(principal.getName());
    }

    @PostMapping
    public JwtResponse createAuthToken(@RequestBody RegistrationUserDto form) {
        userRegistrationValidationRulesEngine.check(form);
        userService.reg(form);
        UserDetails userDetails = userService.loadUserByUsername(form.getUsername());
        return JwtResponse.builder()
                .token(userService.getToken(userDetails))
                .build();
    }
}
