package org.springframework.blood_link_server.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketInterceptor webSocketInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/tcp", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        List<String> allowedOrigins = new ArrayList<>(List.of("http://localhost:4200"));
        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            String[] addressSplit = address.split("\\.");
            for (int i = 1; i < 255; i++) {
                allowedOrigins.add("http://" + addressSplit[0] + "." + addressSplit[1] + "." + addressSplit[2] + "." + i + ":4200");
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }



        registry.addEndpoint("/ws").setAllowedOrigins(String.join(",", allowedOrigins)).withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }
}
