package com.example.userservice.service;

import com.example.userservice.dto.TokenResponse;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.exception.InvalidCredentialsException;
import com.example.userservice.exception.UserKeyClockAlreadyExistException;
import com.example.userservice.exception.UserNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
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
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    private final WebClient webClient;

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

        Map<String, List<String>> attributes = new HashMap<>();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(name);
        user.setEmailVerified(true);
        user.setEmail(email);
        user.setEnabled(true);
        user.setAttributes(attributes);
        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm)
                .users()
                .create(user);

        if(response.getStatus() == 409) {
            throw new UserKeyClockAlreadyExistException(name, email);
        }

        String location = response.getHeaderString("Location");
        String keycloakId = location.substring(location.lastIndexOf("/") + 1);
        log.info("User created in Keycloak: keycloakId={}", keycloakId);

        return keycloakId;
    }

    public Mono<TokenResponse> getToken(String name, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", name);
        body.add("password", password);
        System.out.println(name);
        System.out.println(password);


        return webClient.post()
                .uri("/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r ->
                    Mono.error(new InvalidCredentialsException())
                )
                .bodyToMono(Map.class)
                .map(this::buildTokenResponse);
    }

    public void changePassword(String keycloakId, String newPassword) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .resetPassword(credential);
    }

    public void updateUsername(String keycloakId, String newUsername) {
        UserRepresentation user = keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .toRepresentation();

        user.setUsername(newUsername);

        keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .update(user);
    }

    public UserResponseDto getUserById(String keycloakId) {
        try {
            UserRepresentation userRepresentation = keycloak
                    .realm(realm)
                    .users()
                    .get(keycloakId)
                    .toRepresentation();

            return UserResponseDto.builder().keycloakId(keycloakId)
                    .email(userRepresentation.getEmail())
                    .name(userRepresentation.getUsername())
                    .build();
        } catch (NotFoundException ex) {
            throw new UserNotFoundException(keycloakId);
        }
    }

    private TokenResponse buildTokenResponse(Map<String, Object> response) {
        String accessToken = (String) response.get("access_token");
        String refreshToken = (String) response.get("refresh_token");
        long expiresIn = ((Number) response.get("expires_in")).longValue();
        String userId = extractUserId(accessToken);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .keycloakId(userId)
                .build();

    }

    private String extractUserId(String accessToken) {
        try {
            AccessToken token = TokenVerifier
                    .create(accessToken, AccessToken.class)
                    .getToken();
            return token.getSubject();
        } catch (VerificationException e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }
}
