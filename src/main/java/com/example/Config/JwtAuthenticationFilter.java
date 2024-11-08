package com.example.Config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.Services.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(

            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/auth") || path.startsWith("/ws") || path.startsWith("/stripe/webhook")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        if (cookies != null) {
            System.out.println("Här händer det grejer med JWTs");
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
        // }
        try {

            final String username = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token has expired");
        } catch (SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT signature");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println("Fel fel fel i JWT skiten");
            e.printStackTrace();
            response.getWriter().write("Internal server error: " + e.getMessage());
        }
    }

}
