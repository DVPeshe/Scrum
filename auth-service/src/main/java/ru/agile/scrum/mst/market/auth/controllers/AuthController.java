package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.agile.scrum.mst.market.api.JwtRequest;
import ru.agile.scrum.mst.market.api.JwtResponse;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.services.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        userService.auth(authRequest);
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(userService.getToken(userDetails))
                .visibleAdminButton(userService.getAccessAdmin(authRequest.getUsername()))
                .build();
        userService.userFilter(authRequest.getUsername());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createAuthToken(@RequestBody RegistrationUserDto registrationUserDto) {
        userService.reg(registrationUserDto);
        UserDetails userDetails = userService.loadUserByUsername(registrationUserDto.getUsername());
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(userService.getToken(userDetails))
                .build();
        return ResponseEntity.ok(jwtResponse);
    }


}
