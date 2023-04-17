package ru.agile.scrum.mst.market.email.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.UserPersonalAccount;

@Component
@RequiredArgsConstructor
public class UserServiceIntegration {


    private final WebClient userServiceWebClient;

    public UserPersonalAccount getPersonalData(String username, String tokenSecurity) {
        return userServiceWebClient.get()
                .uri("/api/v1/users/personal-email")
                .header("username", username)
                .header("roles", getRolesStringFromTokenSecurity(tokenSecurity))
                .retrieve()
                .bodyToMono(UserPersonalAccount.class)
                .block();
    }

    private String getRolesStringFromTokenSecurity(String tokenSecurity) {
        int start = tokenSecurity.indexOf("Granted Authorities");
        String str = tokenSecurity.substring(start);
        int end = str.indexOf("]") + 1;
        return str.substring(0, end).replace("Granted Authorities=", "");
    }

}
