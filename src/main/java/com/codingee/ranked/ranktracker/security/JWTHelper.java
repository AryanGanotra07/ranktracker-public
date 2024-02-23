package com.codingee.ranked.ranktracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codingee.ranked.ranktracker.model.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

@Configuration
public class JWTHelper {

    @Value("${jwt.secret}") private static String JWT_SECRET = "rank-tracker";

    private static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET.getBytes());
    }

    private static JWTCreator.Builder getJWTBuilder(String issuer, Client client) {
        return JWT.create()
                .withSubject(client.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 * 1000))
                .withIssuer(issuer);
    }

    public static String getAccessToken(String issuer, Client client) {
        return getJWTBuilder(issuer, client)
                .withClaim("roles", client.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(getAlgorithm());
    }

    public static String getRefreshToken(String issuer, Client client) {
        return getJWTBuilder(issuer, client).sign(getAlgorithm());
    }


    public static DecodedJWT verifyJWT(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

}
