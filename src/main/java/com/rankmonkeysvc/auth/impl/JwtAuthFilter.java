package com.rankmonkeysvc.auth.impl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import static com.rankmonkeysvc.messages.Endpoints.V1_AUTH;
import static com.rankmonkeysvc.messages.StaticMessages.*;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtSvcImpl jwtSvcImpl;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(
            JwtSvcImpl jwtSvcImpl,
            UserDetailsService userDetailsService
    ) {
        this.jwtSvcImpl = jwtSvcImpl;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal
            (@NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String email;

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(BEARER_INDEX);
        email = jwtSvcImpl.extractUsername(jwt);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains(V1_AUTH);
    }
}
