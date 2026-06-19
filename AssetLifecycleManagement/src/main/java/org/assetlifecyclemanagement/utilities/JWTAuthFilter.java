package org.assetlifecyclemanagement.utilities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final EmployeeDetailsService employeeDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;
        if (nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(token);
        }

        if (nonNull(userEmail) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = employeeDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.validateToken(userEmail, userDetails, token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
