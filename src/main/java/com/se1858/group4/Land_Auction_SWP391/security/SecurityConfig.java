package com.se1858.group4.Land_Auction_SWP391.security;

import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Query for fetching the user details
        userDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, active AS enabled FROM members WHERE username=?"
        );

        // Query for fetching the authorities (roles)
        // Here we assume roles are stored in the 'roles' table, and we prefix them with 'ROLE_'
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT username, CONCAT('ROLE_', role) AS authority FROM roles WHERE username=?"
        );

        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                        .defaultSuccessUrl("/default", true)
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
