package ru.agile.scrum.mst.market.comment.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.api.UserDto;

@Component
@RequiredArgsConstructor
public class AuthServiceIntegration {
    private final WebClient authServiceWebClient;

    public UserDto findById(Long id) {
        return authServiceWebClient.get()
                .uri("/api/v1/users/" + id)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

}
