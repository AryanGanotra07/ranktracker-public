package com.codingee.ranked.ranktracker.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.codingee.ranked.ranktracker.security.JWTHelper;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import com.codingee.ranked.ranktracker.util.exceptions.UnauthenticatedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final Set<String> PERMITTED_ENDPOINTS = new HashSet<>(Arrays.asList("/api/auth/login", "/api/client/create", "/api/ping-back/task", "/api/auth/forgot-password", "/api/auth/reset-password"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, UnauthenticatedException {
        if (PERMITTED_ENDPOINTS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    DecodedJWT decodedJWT = JWTHelper.verifyJWT(token);
                    Long clientId = Long.valueOf(decodedJWT.getSubject());
                    log.info("Attempting to login {}", clientId);
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(clientId, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    request.setAttribute("clientId", clientId);
                    filterChain.doFilter(request, response);
                }
                catch (Exception e) {
                    log.info("Login failed {}", e.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), BaseResponse.unauthorized("Unauthorized request"));
                    throw new UnauthenticatedException(e.getMessage());
                }
            } else {
                log.info("Unauthenticated request {}", request.getServletPath());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                new ObjectMapper().writeValue(response.getOutputStream(), BaseResponse.unauthorized("Unauthorized request"));
                throw new UnauthenticatedException("Unauthenticated request");
            }
        }
    }
}
