package ru.agile.scrum.mst.market.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.agile.scrum.mst.market.api.JwtRequest;
import ru.agile.scrum.mst.market.api.JwtResponse;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.auth.services.UserService;
import ru.agile.scrum.mst.market.auth.validation.UserRegistrationFormValidationRulesEngine;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRegistrationFormValidationRulesEngine userRegistrationValidationRulesEngine;

    @PostMapping("/authenticate")
    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        userService.auth(authRequest);
        userService.userFilter(authRequest.getUsername());
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return JwtResponse.builder()
                .token(userService.getToken(userDetails))
                .visibleAdministrationButton(userService.getAccessAdmin(authRequest.getUsername()))
                .visibleUserPanelButton(userService.getAccessUserPanel(authRequest.getUsername()))
                .visibleProductPanelButton(userService.getAccessProductPanel(authRequest.getUsername()))
                .visibleEditRoleButton(userService.getAccessEditRole(authRequest.getUsername()))
                .build();
    }

    @PostMapping("/register")
    public JwtResponse createAuthToken(@RequestBody RegistrationUserDto form) {
        userRegistrationValidationRulesEngine.check(form);
        userService.reg(form);
        UserDetails userDetails = userService.loadUserByUsername(form.getUsername());
        return JwtResponse.builder()
                .token(userService.getToken(userDetails))
                .build();
    }
}
