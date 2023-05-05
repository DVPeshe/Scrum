package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.AvatarPersonalAccount;
import ru.agile.scrum.mst.market.auth.mappers.AvatarMapper;
import ru.agile.scrum.mst.market.auth.services.AvatarService;
import ru.agile.scrum.mst.market.auth.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/avatars")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;
    private final UserService userService;
    private final AvatarMapper avatarMapper;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/my")
    public AvatarPersonalAccount getUserAvatar(Principal principal) {
        return avatarMapper.map(avatarService.findByUsername(principal.getName()));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/my")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserAvatar(@RequestBody AvatarPersonalAccount avatar, Principal principal) {
        avatarService.update(avatarMapper.map(avatar, userService.getUserByName(principal.getName())));
    }
}
