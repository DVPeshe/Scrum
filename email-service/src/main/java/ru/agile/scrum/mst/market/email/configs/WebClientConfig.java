package ru.agile.scrum.mst.market.email.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import ru.agile.scrum.mst.market.email.integrations.UserServiceIntegration;
import ru.agile.scrum.mst.market.email.properties.ProductServiceIntegrationProperties;
import ru.agile.scrum.mst.market.email.properties.UserServiceIntegrationProperties;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(
        {ProductServiceIntegrationProperties.class, UserServiceIntegrationProperties.class}
)
public class WebClientConfig {
    private final ProductServiceIntegrationProperties productServiceIntegrationProperties;
    private final UserServiceIntegrationProperties userServiceIntegrationProperties;

    @Bean
    public WebClient productServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, productServiceIntegrationProperties.getConnectTimeout())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(productServiceIntegrationProperties.getReadTimeout(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(productServiceIntegrationProperties.getWriteTimeout(), TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(productServiceIntegrationProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    @Bean
    public WebClient userServiceWebClient() {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, userServiceIntegrationProperties.getConnectTimeout())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(userServiceIntegrationProperties.getReadTimeout(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(userServiceIntegrationProperties.getWriteTimeout(), TimeUnit.MILLISECONDS));
                });

        return WebClient
                .builder()
                .baseUrl(userServiceIntegrationProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

}
