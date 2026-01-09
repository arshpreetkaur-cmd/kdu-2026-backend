package org.example.securecorporatetalentportal.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) { this.jwtService = jwtService; }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        //if header exists, extract the token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // If invalid, parseAndValidate throws exception → Security sends 401 (via entrypoint)
            jwtService.parseAndValidate(token);

            String username = jwtService.extractUsername(token);

            var authorities = jwtService.extractRoles(token).stream()
                    .map(SimpleGrantedAuthority::new) // converts "ROLE_ADMIN" → GrantedAuthority
                    .collect(Collectors.toList());

            // This object tells Spring Security: request is authenticated as this user
            var authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Stores the authentication for this request thread
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue to next filter/controller
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Don't require token for login endpoint
        return request.getRequestURI().equals("/login");
    }
}
