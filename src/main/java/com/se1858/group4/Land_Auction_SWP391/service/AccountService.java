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
import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.repository.BanLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
    @Autowired
    private BanLogRepository banLogRepository;


    private String storedOtp; // for simplicity, store OTP temporarily in memory or use a database.
    private String storedEmail; // Store the email temporarily for verification
//    private Account temporaryAccount = new Account(); // Temporarily hold account until OTP is verified
Account account = new Account();
    public AccountService() {

    }

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



//    public void registerUser(String username, String password, String email) {
//
//        account.setUsername(username);
//        if (accountRepository.findByUsername(username) != null) {
//            throw new RuntimeException("Username already exists.");
//        }
//        // Encrypt password
//        account.setPassword(passwordEncoder.encode(password));
//        account.setEmail(email);
//        account.setVerify(0);  // Not verified yet
//        account.setStatus(0);  // Disabled until OTP verification
//        account.setRegistrationDate(LocalDateTime.now());
//
//        // Generate OTP and send to email
//        storedOtp = generateOTP();
//        storedEmail = email;
//        sendOtpEmail(email, storedOtp);
//    }


    public String registerUser(String username, String password, String email, Model model) {
        if (accountRepository.findByUsername(username) != null) {
            model.addAttribute("errorMessage", "Username already exists. Please choose another one.");
            return "register";  // Return the user back to the register page
        }

        // Continue with user registration
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setEmail(email);
        account.setVerify(0);
        account.setStatus(0);
        account.setRegistrationDate(LocalDateTime.now());

        storedOtp = generateOTP();
        storedEmail = email;
        sendOtpEmail(email, storedOtp);
        return "redirect:/verify-otp";  // Redirect to OTP verification
    }



    public boolean checkUsernameExists(String username) {
        return accountRepository.findByUsername(username) != null;
    }

    public boolean checkEmailExists(String email) {
        return accountRepository.findByEmail(email) != null;
    }


    public boolean verifyOtp(String otp) {

        if (otp.equals(storedOtp)) {
            System.out.println("Hi");
            if (account != null && account.getEmail().equals(storedEmail)) {
                account.setRole(roleRepository.findByRoleName("ROLE_Customer"));
                account.setStatus(1);  // Enable the account
                accountRepository.save(account);  // Save the account to the database
                account = null;  // Clear temporary account after saving
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
        for (Account account : listAccount) {
            if (account.getRole().getRoleName().equals(role)) {
                if (result == null) {
                    result = new ArrayList<Staff>();
                }
                result.add(account.getStaff());
            }
        }
        return result;
    }

    public List<Account> findAllAccount() {
        return accountRepository.findAll();
    }

    public List<Account> findALlAccountByStatus(String status) {
        List<Account> listAccount = accountRepository.findAll();
        List<Account> result = null;
        for (Account account : listAccount) {
            if(account.getStatus() == 1){
                if (result == null) {
                    result = new ArrayList<Account>();
                }
                result.add(account);
            }
        }
        return result;
    }

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // Set current time
        account.setRegistrationDate(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void update(Account adminId, Account accountId, String reason) {
        Account existingAccount = accountRepository.findById(accountId.getAccountId()).
                orElseThrow(() -> new IllegalArgumentException("Account not found"));

        List<String> changedFields = new ArrayList<>();
        if (existingAccount.getStatus() == (accountId.getStatus())) {
            changedFields.add("status");
        }
        // Cập nhật account
        accountRepository.save(accountId);

        // Ghi log nếu có thay đổi
        if (!changedFields.isEmpty()) {
            BanLog log = new BanLog();
            log.setAdmin(adminId);
            log.setAccount(accountId);
            log.setTimestamp(LocalDateTime.now());
            log.setReason(reason);

            banLogRepository.save(log);
        }
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}