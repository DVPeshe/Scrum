package ru.agile.scrum.mst.market.stock.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.JwtRequest;
import ru.agile.scrum.mst.market.api.JwtResponse;

@Component
@RequiredArgsConstructor
public class AuthServiceIntegration {
    private final WebClient authServiceWebClient;

    public JwtResponse tryAuth(String username, String password) {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(username);
        jwtRequest.setPassword(password);
        JwtResponse jwtResponse = authServiceWebClient.post()
                .uri("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jwtRequest)
                .retrieve()
                .bodyToMono(JwtResponse.class)
                .block();
        System.out.println(jwtResponse.getToken());
        return jwtResponse;
    }
}
