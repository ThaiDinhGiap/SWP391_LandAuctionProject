package com.se1858.group4.Land_Auction_SWP391.security;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.ArrayList;

@Configuration
public class SecurityConfig {


//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        jdbcUserDetailsManager.setUsersByUsernameQuery(
////                "SELECT username, password, status, email, avatar_image_id,role_id, registration_date  FROM Account WHERE username=?"
//                "SELECT username, password, email FROM Account WHERE username=?"
//        );
//
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
////                "SELECT a.username,a.password,a.status ,a.email , a.avatar_image_id, a.role_id,a.registration_date, r.role_name  FROM Account a " +
////                        "JOIN Role r ON a.role_id = r.role_id WHERE a.username=?"
//                "SELECT a.username, r.role_name  FROM Account a " +
//                        "JOIN Role r ON a.role_id = r.role_id WHERE a.username=?"
//        );
////        AccountRepository accountRepository;
////        @Autowired
////        jdbcUserDetailsManager.loadUserByUsername(username -> {
////            Account account = accountRepository.findByUsername(username);
////            if (account == null || account.getStatus().equals("unverified")) {
////                throw new UsernameNotFoundException("User not found or account is disabled");
////            }
////            return new User(
////                    account.getUsername(),
////                    account.getPassword(),
////                    new ArrayList<>() // Authorities
////            );
////        });
////
//
//        return jdbcUserDetailsManager;
//    }


//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        //dung jdbc de tim den bang users va bang authorities
//        //mac dinh 2 bang chua account va role trong spring security se phai co cau truc nhu sau:
//        // + users: username, password, enabled
//        // + authorities: username, authority
//        //neu follow theo cau truc nay thi ta chi can return la xong
////        return new JdbcUserDetailsManager(dataSource);
//
//        //neu 2 bang tren khong theo cau truc nhu vay, ta se phai viet ma sql de spring security biet
//        //cach tim den bang account va bang role cua chung ta
//        //va duoi day la cac cong viec phai lam
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        //xac dinh bang chua accounts, lenh truy van thi viet theo cac thuoc tinh trong database
//        //tuy nhien chi co the select 3 truong va bat buoc phai co cau lenh where de tim row co username duoc nhap vao
//        userDetailsManager.setUsersByUsernameQuery("select username, password, active from members where username=?");
//        //xac dinh bang chua roles, lenh truy van thi viet theo cac thuoc tinh trong database
//        //tuy nhien chi co the select 3 truong va bat buoc phai co cau lenh where de tim row co username duoc nhap vao
//        userDetailsManager.setAuthoritiesByUsernameQuery("select username, role from roles where username=?");
//        return userDetailsManager;
//    }
//
//
//



    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Câu truy vấn để lấy thông tin tài khoản
        // Chỉ cần username, password và trường status (để xác định xem tài khoản có hoạt động hay không)
        userDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, CASE WHEN status = 1 THEN 1 ELSE 0 END AS enabled " +
                        "FROM Account WHERE username=?"
        );

        // Câu truy vấn để lấy thông tin vai trò
        // Kết nối bảng Account với bảng Role qua role_id
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT a.username, r.role_name AS authority " +
                        "FROM Account a JOIN Role r ON a.role_id = r.role_id WHERE a.username=?"
        );

        return userDetailsManager;
    }





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}