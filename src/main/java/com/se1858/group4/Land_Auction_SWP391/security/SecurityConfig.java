package com.se1858.group4.Land_Auction_SWP391.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, status as enabled FROM Account WHERE username=?"
        );
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT a.username, r.role_name AS authority " +
                        "FROM Account a " +
                        "JOIN Role r ON a.role_id = r.role_id " +
                        "WHERE a.username=?"
        );

        return userDetailsManager;
    }

    // Configure Remember Me service

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**", "/js/**", "/image/**", "/assets_CustomerSide/**", "/assets_CustomerSide/webfonts").permitAll()
                        .requestMatchers("/", "/showMyLoginPage", "/access-denied", "/register", "/verify-otp",
                                "/resend-otp", "/otp-success", "/forgot-password",
                                "/customer/get_all_auction", "/customer/get_all_asset", "/customer/get_all_news",
                                "/customer/viewNewsDetail", "/customer/viewAssetDetail", "/customer/viewAuctionDetail",
                                "/customer/aboutus","/customer/contact").permitAll()


                        .requestMatchers("/customer/**").hasRole("Customer")
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/property_agent/**").hasRole("Property_Agent")
                        .requestMatchers("/auctioneer/**").hasRole("Auctioneer")
                        .requestMatchers("/customercare/**").hasRole("Customer_Care")
                        .requestMatchers("/news_writer/**").hasRole("News_Writer")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .failureUrl("/showMyLoginPage?error=true")
                        .permitAll()
                        .defaultSuccessUrl("/default", true)
                )

                .logout(logout -> logout.permitAll())

                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")
                )
                .csrf(csrf -> csrf.disable()); // Optional CSRF disabling

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
