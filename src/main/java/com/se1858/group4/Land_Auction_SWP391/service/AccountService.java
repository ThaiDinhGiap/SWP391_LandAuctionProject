package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.entity.Role;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.CustomerRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;




    private String storedOtp; // for simplicity, store OTP temporarily in memory or use a database.
    private String storedEmail; // Store the email temporarily for verification
    private Account temporaryAccount; // Temporarily hold account until OTP is verified

//
//    public Account findAccountByAccountId(int accountId) {
//        return accountRepository.findById(accountId).orElse(null);
//    }
//
//    public Customer findCustomerByAccountId(int accountId) {
//        return customerRepository.findByAccountId(accountId);
//    }

//    public void registerUser(String username, String password, String email) {
//        Account account = new Account();
//        account.setUsername(username);
//        account.setPassword(passwordEncoder.encode(password));
//        account.setEmail(email);
//        account.setVerify(0);  // Not verified yet
//        account.setStatus(1);  // Disabled until OTP verification
//        account.setRegistrationDate(LocalDateTime.now());
//
//        Role role = roleRepository.findByRoleName("ROLE_Customer");
//        account.setRole(role);
//
//        accountRepository.save(account);  // Save the account with status = 0
//
//        storedOtp = generateOTP();  // Generate OTP
//        storedEmail = email;  // Store email for verification
//        temporaryAccount = account; // Temporarily store account info
//        sendOtpEmail(email, storedOtp);  // Send OTP email
//    }
//





    public void registerUser(String username, String password, String email) {
        Account account = new Account();
        account.setUsername(username);

        // Encrypt password
        account.setPassword(passwordEncoder.encode(password));

        account.setEmail(email);
        account.setVerify(0);  // Not verified yet
        account.setStatus(1);
        account.setRegistrationDate(LocalDateTime.now());

        // Fetch the role from the database
        Role role = roleRepository.findByRoleName("ROLE_Customer");
        if (role == null) {
            throw new RuntimeException("Role not found");
        }

        account.setRole(role);



        temporaryAccount = account; // Temporarily store account info
        temporaryAccount.setRole(account.getRole());

        storedOtp = generateOTP();  // Generate OTP
        storedEmail = email;  // Store email for verification
        sendOtpEmail(email, storedOtp);  // Send OTP email
    }


    public boolean verifyOtp(String otp) {
        System.out.println("Pla");
        if (otp.equals(storedOtp)) {
            System.out.println("Hi");
            if (temporaryAccount != null && temporaryAccount.getEmail().equals(storedEmail)) {
                System.out.println("Hello");
                temporaryAccount.setStatus(1);  // Enable the account
                accountRepository.save(temporaryAccount);  // Save the account to the database
                temporaryAccount = null;  // Clear temporary account after saving
                return true;
            }
        }
        return false;  // OTP verification failed
    }



    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP Verification");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void resendOtp() {
        storedOtp = generateOTP();  // Generate a new OTP
        sendOtpEmail(storedEmail, storedOtp);  // Resend OTP to the user's email
    }



//    @Scheduled(cron = "0 0 0 * * ?")  // Runs daily at midnight
//    public void removeUnverifiedAccounts() {
//        List<Account> unverifiedAccounts = accountRepository.findByStatus(0);
//        for (Account account : unverifiedAccounts) {
//            accountRepository.delete(account);
//        }
//    }



    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public Account findAccountById(int id) {
        return accountRepository.findById(id).get();
    }



    public List<Staff> findAllStaffsByRole(String role) {
        List<Account> listAccount = accountRepository.findAll();
        List<Staff> result = null;
        for(Account account : listAccount) {
            if(account.getRole().getRoleName().equals(role)) {
                if(result == null) {
                    result = new ArrayList<Staff>();
                }
                result.add(account.getStaff());
            }
        }
        return result;
    }
}
