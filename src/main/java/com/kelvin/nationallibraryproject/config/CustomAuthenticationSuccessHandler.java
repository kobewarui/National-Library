package com.kelvin.nationallibraryproject.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        if (roles.contains("ROLE_ADMIN")) {
            redirectStrategy.sendRedirect(request, response, "/home/admin");
        } else if (roles.contains("ROLE_STUDENT")) {
            redirectStrategy.sendRedirect(request, response, "/home/student");
        } else if (roles.contains("ROLE_PROFESSIONAL")) {
            redirectStrategy.sendRedirect(request, response, "/home/professional");
        } else {
            // Handle other roles or default case
            redirectStrategy.sendRedirect(request, response, "/home");
        }
    }

	
}
