package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.*;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.exceptions.AccessForbiddenException;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.Specifications.UsersSpecifications;
import ru.agile.scrum.mst.market.auth.services.UserService;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/edit-role")
    public StringResponse editRole(@RequestBody UserDtoRoles userDtoRoles) {
        userService.editRole(userDtoRoles);
        return new StringResponse("Права пользователя изменены");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/banUser/{id}")
    public void banUser(@PathVariable Long id, @RequestParam(name = "access") Boolean access) {
        userService.updateAccessUser(id, access);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/email/{username}")
    public StringResponse getAnyEmailAddress(@PathVariable String username) {
        return new StringResponse(userService.getUserEmailByName(username));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/full-name/{username}")
    public StringResponse getAnyFullName(@PathVariable String username) {
        return new StringResponse(userService.getFullNameByName(username));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/my")
    public StringResponse updateAnyUserData(@RequestBody RegistrationUserDto registrationUserDto, Principal principal) {
        if (!Objects.equals(principal.getName(), registrationUserDto.getUsername())) {
            throw new AccessForbiddenException("Запрещено изменять чужие персональные данные.");
        }
        userService.updateUser(registrationUserDto);
        return new StringResponse(String
                .format("Данные пользователя %s успешно обновлены.", registrationUserDto.getUsername()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/personal-data/my")
    public UserPersonalAccount getUserPersonalData(Principal principal) {
        final String username = principal.getName();
        return UserPersonalAccount.builder()
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

    @GetMapping("/personal-email")
    public UserPersonalAccount getUserPersonalEmail(@RequestHeader String username) {
        UserPersonalAccount account = UserPersonalAccount.builder()
                .username(username)
                .email(userService.getUserEmailByName(username))
                .fullName(userService.getFullNameByName(username))
                .build();
        return account;
    }

}
