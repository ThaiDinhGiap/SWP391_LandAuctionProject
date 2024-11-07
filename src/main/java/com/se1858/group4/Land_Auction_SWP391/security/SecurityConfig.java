package com.se1858.group4.Land_Auction_SWP391.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer

                        .requestMatchers("/css/**", "/js/**", "/image/**", "/assets_CustomerSide/**", "/assets_CustomerSide/webfonts").permitAll()


                        .requestMatchers("/", "/showMyLoginPage", "/access-denied",
                                "/register", "/verify-otp", "/otp-success",
                                "/otp-forgot-password", "/resend-otp",
                                "/forgot-password", "/verify-otp-forgot-password",
                                "/customer/get_all_auction", "/customer/get_all_asset",
                                "/customer/get_all_news", "/customer/viewNewsDetail",
                                "/customer/viewAssetDetail", "/customer/viewAuctionDetail",
                                "/customer/aboutus", "/customer/contact").permitAll()

                        // Cấu hình quyền truy cập cho các vai trò khác nhau
                        .requestMatchers("/customer/**").hasRole("Customer")
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/property_agent/**").hasRole("Property_Agent")
                        .requestMatchers("/auctioneer/**").hasRole("Auctioneer")
                        .requestMatchers("/customercare/**").hasRole("Customer_Care")
                        .requestMatchers("/news_writer/**").hasRole("News_Writer")

                        // Yêu cầu xác thực cho tất cả các yêu cầu còn lại
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")            // Đường dẫn đến trang đăng nhập
                        .loginProcessingUrl("/authenticateTheUser") // URL xử lý đăng nhập
                        .failureUrl("/showMyLoginPage?error=true")  // Đường dẫn khi đăng nhập thất bại
                        .permitAll()                               // Cho phép truy cập công khai vào trang đăng nhập
                        .defaultSuccessUrl("/default", true)       // Đường dẫn sau khi đăng nhập thành công
                )
                .logout(logout -> logout.permitAll())           // Cho phép đăng xuất công khai
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")         // Trang hiển thị khi bị từ chối truy cập
                )
                .csrf(csrf -> csrf.disable());                  // Vô hiệu hóa CSRF (tuỳ chọn)

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
