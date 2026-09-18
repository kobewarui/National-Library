package com.kelvin.nationallibraryproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kelvin.nationallibraryproject.services.AdminService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	AdminService adminService;
	
	
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 http.csrf().disable()
                .authorizeHttpRequests()
                // Only the sign-up / sign-in screens, the error page and the static assets are public.
                // Every application page reads the logged-in account off the Authentication object,
                // so those pages must require authentication instead of being permitted anonymously.
                .requestMatchers("/user/register","/user/save","/user/login","/error",
                		"/css/**","/js/**","/img/**","/vendor/**","/user-photos/**")
                .permitAll()
                // Every screen below reads the librarian off the Authentication object and
                // would fail on a member account, so they are restricted to admins.
                .requestMatchers("/home/admin","/books","/authors","/borrows","/report","/profile",
                		"/newbook","/newauthor","/newborrow",
                		"/book/**","/author/**","/borrow/**","/profile/**")
                .hasRole("ADMIN")
                .requestMatchers("/home/student").hasRole("STUDENT")
                .requestMatchers("/home/professional").hasRole("PROFESSIONAL")
                .anyRequest().authenticated()
                .and()
                // A member who reaches for an admin URL goes back to their own dashboard
                // instead of a bare 403 page.
                .exceptionHandling(exception -> exception.accessDeniedPage("/home"))
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .successHandler(customAuthenticationSuccessHandler()) // sends each role to its own dashboard
                        .failureUrl("/user/login?error=true")
                        .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                );

        return http.build();
    }
    
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(adminService)
//                .passwordEncoder(passwordEncoder());
//    }
    
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

}