package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.BanLogRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.CustomerRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;

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


    private String storedOtp; // store OTP temporarily in memory or use a database.
    private String storedEmail; // Store the email temporarily for verification
    Account account = new Account();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        
    }



    public AccountService() {

    }



    public String registerUser(String username, String password, String email, Model model) {
        if (accountRepository.findByUsername(username) != null) {
            model.addAttribute("errorMessage", "Username already exists. Please choose another one.");
            return "register";
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
                accountRepository.save(account);
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


    public void processForgotPassword(String email, Model model) {
        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            model.addAttribute("errorMessage", "No account found with this email.");
            return;
        }

        String newPassword = generateRandomPassword();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        sendNewPasswordEmail(email, newPassword);

        model.addAttribute("message", "A new password has been sent to your email.");
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void sendNewPasswordEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your New Password");
        message.setText("Your new password is: " + newPassword + "\nPlease change it after logging in.");
        mailSender.send(message);
    }


    public void updateAccountDetails(Account account) {
        Optional<Account> existingAccountOpt = accountRepository.findById(account.getAccountId());
        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            existingAccount.setUsername(account.getUsername());
            existingAccount.setEmail(account.getEmail());
            existingAccount.setVerify(0);
            accountRepository.save(existingAccount);
        } else {
            throw new NoSuchElementException("Account not found for ID: " + account.getAccountId());
        }
    }

//
//    public void updateCustomerDetails(Customer customer) {
//        Customer existingCustomer = customerRepository.findById(customer.getCustomerId()).get();
//        if (existingCustomer != null) {
//
//            //Bank
//            existingCustomer.setBankAccountNumber(customer.getBankAccountNumber());
//            existingCustomer.setBankBranch(customer.getBankBranch());
//            existingCustomer.setBankName( customer.getBankName());
//            existingCustomer.setBankOwner(customer.getBankOwner());
//            //Personal
//            existingCustomer.setDateOfBirth(customer.getDateOfBirth());
//            existingCustomer.setGender(customer.getGender());
//            existingCustomer.setFullName( customer.getFullName());
//            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
//            existingCustomer.setAddress(customer.getAddress());
//            //ID
//            existingCustomer.setCitizenIdentification(customer.getCitizenIdentification());
//            existingCustomer.setIdCardBackImage(customer.getIdCardBackImage());
//            existingCustomer.setIdCardFrontImage(customer.getIdCardFrontImage());
//            existingCustomer.setIdIssuanceDate(customer.getIdIssuanceDate());
//            existingCustomer.setIdIssuancePlace(customer.getIdIssuancePlace());
//
//            customerRepository.save(existingCustomer);
//        }
//  }




//
//    @Scheduled(cron = "0 0 0 * * ?")  // Runs daily at midnight
//    public void removeUnverifiedAccounts() {
//        List<Account> unverifiedAccounts = accountRepository.findByStatus(0);
//        for (Account account : unverifiedAccounts) {
//            accountRepository.delete(account);
//        }
//    }


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
        account.setStatus(1);
        account.setVerify(1);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // Set current time
        account.setRegistrationDate(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Account ban(Account account) {
        account.setStatus(0);
        // Set current time
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


    public void changePassword(String username, String oldPassword, String newPassword) {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new NoSuchElementException("User not found.");
        }

        // Validate the old password
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        // Encode and set the new password
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }


}