package com.se1858.group4.Land_Auction_SWP391.security;

import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final String REMEMBER_ME_KEY = "rememberMeKey123";
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

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
    public TokenBasedRememberMeServices rememberMeServices(UserDetailsManager userDetailsManager) {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsManager);
        rememberMeServices.setCookieName("remember-me-cookie");
        rememberMeServices.setTokenValiditySeconds(604800); // Valid for 7 days
        return rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenBasedRememberMeServices rememberMeServices) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**", "/js/**", "/image/**", "/assets_CustomerSide/**", "/assets_CustomerSide/webfonts").permitAll()
                        .requestMatchers("/", "/showMyLoginPage", "/access-denied", "/register", "/verify-otp", "/resend-otp", "/otp-success", "/forgot-password",
                                "/customer/get_all_auction", "/customer/get_all_asset", "/customer/get_all_news",
                                "/customer/viewNewsDetail", "/customer/viewAssetDetail", "/customer/viewAuctionDetail").permitAll()
                        .requestMatchers("/customer/**").hasRole("Customer")
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/property_agent/**").hasRole("Property_Agent")
                        .requestMatchers("/auctioneer/**").hasRole("Auctioneer")
                        .requestMatchers("/customer_care/**").hasRole("Customer_Care")
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
                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .rememberMeServices(rememberMeServices)
                        .key(REMEMBER_ME_KEY)
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
