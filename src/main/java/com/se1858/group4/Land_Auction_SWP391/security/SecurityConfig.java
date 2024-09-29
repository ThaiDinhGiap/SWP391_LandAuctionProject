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

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        // Câu truy vấn để lấy thông tin tài khoản
//        // Chọn username, password, và status (để kiểm tra trạng thái tài khoản)
//        userDetailsManager.setUsersByUsernameQuery(
//                "SELECT username, password, status FROM Account WHERE username=?"
//        );
//
//        // Câu truy vấn để lấy thông tin vai trò
//        // Kết nối bảng Account với bảng Role qua role_id
//        userDetailsManager.setAuthoritiesByUsernameQuery(
//                "SELECT a.username, r.role_name AS authority " +
//                        "FROM Account a JOIN Role r ON a.role_id = r.role_id WHERE a.username=?"
//        );
//
//        return userDetailsManager;
//    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Câu truy vấn để lấy thông tin tài khoản
        // Sử dụng trường status (1 = enabled, 0 = disabled)
        userDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, status as enabled FROM Account WHERE username=?"
        );

        // Câu truy vấn để lấy thông tin vai trò
        // Kết nối bảng Account với bảng Role qua role_id
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT a.username, r.role_name AS authority " +
                        "FROM Account a " +
                        "JOIN Role r ON a.role_id = r.role_id " +
                        "WHERE a.username=?"
        );

        return userDetailsManager;
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/customer/**").hasRole("Customer")
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/property-agent/**").hasRole("Property_Agent")
                        .requestMatchers("/autioneer/**").hasRole("Autioneer")
                        .requestMatchers("/customer_care/**").hasRole("Customer_Care")
                        .requestMatchers("/news_writer/**").hasRole("News_Writer")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2login -> {
                    oauth2login
                            .loginPage("/showMyLoginPage")
                            .successHandler((request, response, authentication) -> response.sendRedirect("/customer/home"));
                })
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
