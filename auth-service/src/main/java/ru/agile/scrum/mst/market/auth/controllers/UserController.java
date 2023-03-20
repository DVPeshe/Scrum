package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.api.UserDto;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.Specifications.UsersSpecifications;
import ru.agile.scrum.mst.market.auth.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/forAdmin/listUsers")
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

    @PostMapping("/forAdmin/roleEdit")
    public ResponseEntity<?> roleEdit(@RequestBody UserDto userDto) {
        userService.roleEdit(userDto);
        StringResponse stringResponse = new StringResponse("Права пользователя изменены");
        return ResponseEntity.ok(stringResponse);
    }

    @PostMapping("/forAdmin/banUser/{id}")
    public void banUser(@PathVariable Long id, @RequestParam(name = "access") Boolean access) {
        userService.updateAccessUser(id, access);
    }

    @GetMapping("/email/{username}")
    public ResponseEntity<?> getEmailUser(@PathVariable String username) {
        StringResponse stringResponse = new StringResponse(userService.getUserEmailByName(username));
        return ResponseEntity.ok(stringResponse);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> updateDataUser(@RequestBody RegistrationUserDto registrationUserDto) {
        userService.updateUser(registrationUserDto);
        StringResponse stringResponse = new StringResponse(String
                .format("Данные пользователя %s успешно обновлены", registrationUserDto.getUsername()));
        return ResponseEntity.ok(stringResponse);
    }
}
