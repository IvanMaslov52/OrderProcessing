package com.example.userservice.service.impl;

import com.example.userservice.dto.TokenResponse;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl {
    private final Keycloak keycloak;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String createUser(String email, String name, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(name);
        user.setEmailVerified(true);
        user.setEmail(email);
        user.setEnabled(true);
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        if(response.getStatus() == 409) {
            throw new RuntimeException("User already exist");
        }

        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public TokenResponse getToken(String name, String password) {
        WebClient webClient = WebClient.create(serverUrl);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", name);
        body.add("password", password);

        Map response = webClient.post()
                .uri("/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> {
                    throw new RuntimeException("Invalid credentials");
                })
                .bodyToMono(Map.class)
                .block();
        return TokenResponse.builder()
                .accessToken((String) response.get("access_token"))
                .refreshToken((String) response.get("refresh_token"))
                .expiresIn(((Number) response.get("expires_in")).longValue())
                .build();
    }
}
