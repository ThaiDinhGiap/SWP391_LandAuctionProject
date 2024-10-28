package com.se1858.group4.Land_Auction_SWP391.security;

//import com.se1858.group4.Land_Auction_SWP391.googleLoginHandler.OAuth2SuccessHandler;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;



@EnableWebSecurity
@Configuration
public class SecurityConfig {




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




//
//    @Autowired
//    private OAuth2SuccessHandler oAuth2SuccessHandler;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(configurer -> configurer
//                        .requestMatchers("/customer/**").hasRole("Customer")
//                        .requestMatchers("/admin/**").hasRole("Admin")
//                        .requestMatchers("/property-agent/**").hasRole("Property_Agent")
//                        .requestMatchers("/autioneer/**").hasRole("Autioneer")
//                        .requestMatchers("/customer-care/**").hasRole("Customer_Care")
//                        .requestMatchers("/news-writer/**").hasRole("News_Writer")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/showMyLoginPage")
//                        .permitAll()
//                        .defaultSuccessUrl("/default", true)
//                )
//                .oauth2Login(oauth2login -> oauth2login
//                        .loginPage("/showMyLoginPage")
//                        .successHandler(oAuth2SuccessHandler)  // Use custom success handler
//                )
//                .logout(LogoutConfigurer::permitAll)
//                .exceptionHandling(configurer -> configurer
//                        .accessDeniedPage("/access-denied")
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/css/**", "/js/**", "/image/**","/assets_CustomerSide/**","/assets_CustomerSide/webfonts").permitAll()
                        // Allow unrestricted access to the homepage, login page, and error page
                        .requestMatchers("/", "/showMyLoginPage", "/access-denied","/register","/verify-otp","/resend-otp","/otp-success","/forgot-password","/customer/get_all_auction","/customer/get_all_asset","/customer/get_all_news").permitAll()
                        // Define role-based access for other URLs
                        .requestMatchers("/customer/**").hasRole("Customer")
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/property_agent/**").hasRole("Property_Agent")
                        .requestMatchers("/auctioneer/**").hasRole("Auctioneer")
                        .requestMatchers("/customer_care/**").hasRole("Customer_Care")
                        .requestMatchers("/news_writer/**").hasRole("News_Writer")
                        // Any other request should be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage") // Define custom login page
                        .loginProcessingUrl("/authenticateTheUser") // Login form POST URL
                        .failureUrl("/showMyLoginPage?error=true") // Redirect to login page with error on failure
                        .permitAll()
                        .defaultSuccessUrl("/default", true) // Where to go after successful login
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")
                )
                .csrf(AbstractHttpConfigurer::disable); // Disable CSRF for simplicity (optional)

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
