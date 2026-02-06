package com.zetta.todo.common.config;

import com.zetta.todo.modules.usuario.domain.User;
import com.zetta.todo.modules.usuario.repository.UserRepository;
import com.zetta.todo.modules.usuario.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            var token = recoverToken(request);

            if (token != null) {
                var login = tokenService.validateToken(token);

                if (login != null && !login.isEmpty()) {
                    User user = userRepository.findByEmail(login)
                            .orElse(null);

                    if (user != null) {
                        var authentication = new UsernamePasswordAuthenticationToken(user, null,
                                Collections.emptyList());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na validação do token (ignorado para rotas públicas): " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        if (!authHeader.startsWith("Bearer "))
            return null;

        return authHeader.replace("Bearer ", "");
    }
}