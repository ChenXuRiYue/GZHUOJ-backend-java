package com.gzhuoj.judgeserver.config;

import com.gzhuoj.judgeserver.judge.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class WebSocketClientConfig {

    @Bean
    public WebSocketClient webSocketClient() throws Exception {
        URI serverUri = new URI("ws://localhost:5050/ws");
        WebSocketClient client = new WebSocketClient(serverUri);
        client.connectBlocking(); // Blocking until the connection is established
        return client;
    }
}
