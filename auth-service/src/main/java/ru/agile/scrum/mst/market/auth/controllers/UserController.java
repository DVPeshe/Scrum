package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.api.UserDto;
import ru.agile.scrum.mst.market.api.UserPersonalAccount;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.exceptions.AccessForbiddenException;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.Specifications.UsersSpecifications;
import ru.agile.scrum.mst.market.auth.services.UserService;

import java.util.List;
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
    @PostMapping("/roleEdit")
    public ResponseEntity<?> roleEdit(@RequestBody UserDto userDto) {
        userService.roleEdit(userDto);
        StringResponse stringResponse = new StringResponse("Права пользователя изменены");
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/banUser/{id}")
    public void banUser(@PathVariable Long id, @RequestParam(name = "access") Boolean access) {
        userService.updateAccessUser(id, access);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/email/{username}")
    public ResponseEntity<?> getAnyEmailAddress(@PathVariable String username) {
        StringResponse stringResponse = new StringResponse(userService.getUserEmailByName(username));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/email")
    public ResponseEntity<?> getUserEmailAddress(@RequestHeader String username) {
        StringResponse stringResponse = new StringResponse(userService.getUserEmailByName(username));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/full-name/{username}")
    public ResponseEntity<?> getAnyFullName(@PathVariable String username) {
        StringResponse stringResponse = new StringResponse(userService.getFullNameByName(username));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/full-name")
    public ResponseEntity<?> getUserFullName(@RequestHeader String username) {
        StringResponse stringResponse = new StringResponse(userService.getFullNameByName(username));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/updateUser")
    public ResponseEntity<?> updateAnyUserData(@RequestBody RegistrationUserDto registrationUserDto, @RequestHeader String username) {
        if (!Objects.equals(username, registrationUserDto.getUsername())) {
            throw new AccessForbiddenException("Запрещено изменять чужие персональные данные.");
        }
        userService.updateUser(registrationUserDto);
        StringResponse stringResponse = new StringResponse(String
                .format("Данные пользователя %s успешно обновлены.", registrationUserDto.getUsername()));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/personal-data")
    public ResponseEntity<UserPersonalAccount> getUserPersonalData(@RequestHeader String username) {
        UserPersonalAccount account = UserPersonalAccount.builder()
                .username(username)
                .email(userService.getUserEmailByName(username))
                .fullName(userService.getFullNameByName(username))
                .build();
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/role-titles")
    public ResponseEntity<List<String>> getUserRoles(@RequestHeader String username) {
        return ResponseEntity.ok(userService.getUserRoles(username));
    }
}
