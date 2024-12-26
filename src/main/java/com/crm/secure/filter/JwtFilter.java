package com.crm.secure.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crm.secure.model.CustomUserDetails;
import com.crm.secure.service.CustomUserDetailsService;
import com.crm.secure.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // Bearer token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        /* extracting token value. */
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsernameFrom(token);
        }

        /* verify values from the extracted token value. */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // need user details from the db.
            UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

            // then validate the token along with user details.
            if (jwtService.validate(token, userDetails)) {
                UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authtoken);
                filterChain.doFilter(request, response);
            }
        }
    }
}
