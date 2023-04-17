package ru.agile.scrum.mst.market.email.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.UserPersonalAccount;

@Component
@RequiredArgsConstructor
public class UserServiceIntegration {


    private final WebClient userServiceWebClient;

    public UserPersonalAccount getPersonalData(String username) {
        return userServiceWebClient.get()
                .uri("/api/v1/users/personal-email")
                .header("username",username)
                .retrieve()
                .bodyToMono(UserPersonalAccount.class)
                .block();
    }

}
