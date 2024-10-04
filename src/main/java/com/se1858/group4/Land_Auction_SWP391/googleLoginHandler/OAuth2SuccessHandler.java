//package com.se1858.group4.Land_Auction_SWP391.googleLoginHandler;
//
//import com.se1858.group4.Land_Auction_SWP391.entity.Account;
//import com.se1858.group4.Land_Auction_SWP391.entity.Role;
//import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
//import com.se1858.group4.Land_Auction_SWP391.repository.RoleRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.factory.annotation.Autowired;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@Component
//public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final AccountRepository accountRepository;  // Final for immutability
//    private final RoleRepository roleRepository;
//
//    @Autowired
//    public OAuth2SuccessHandler(AccountRepository accountRepository, RoleRepository roleRepository) {
//        this.accountRepository = accountRepository;
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//        String email = oauthToken.getPrincipal().getAttribute("email");
//
//        if (email == null || email.isEmpty()) {
//            // Handle case where email is not retrieved
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to retrieve email from OAuth provider");
//            return;
//        }
//
//        // Check if the user already exists in the database
//        Account account = accountRepository.findByEmail(email);
//
//        if (account == null) {
//            // Fetch the default role, with a fallback if it doesn't exist
//            Role defaultRole = roleRepository.findByRoleName("ROLE_Customer");
//            if (defaultRole == null) {
//                // Handle case where default role is not found
//                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Default role not found");
//                return;
//            }
//
//            // If user does not exist, create a new user in the database
//            Account newAccount = new Account();
//            newAccount.setEmail(email);
//            newAccount.setRole(defaultRole);  // Assign the fetched role
//            newAccount.setStatus(1);  // Set status as enabled
//            newAccount.setVerify(1);  // Set account verification status
//            accountRepository.save(newAccount);
//        }
//
//        // Proceed with the default Spring Security behavior
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//}
