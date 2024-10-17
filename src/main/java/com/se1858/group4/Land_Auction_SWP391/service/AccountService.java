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
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        Account existingAccount = accountRepository.findById(account.getAccountId()).orElse(null);
        if (existingAccount != null) {
            existingAccount.setUsername(account.getUsername());
            existingAccount.setEmail(account.getEmail());
            accountRepository.save(existingAccount);
        }
    }

  public void updateCustomerDetails(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId()).orElse(null);
        if (existingCustomer != null) {

            //Bank
            existingCustomer.setBankAccountNumber(existingCustomer.getBankAccountNumber());
            existingCustomer.setBankBranch(existingCustomer.getBankBranch());
            existingCustomer.setBankName( existingCustomer.getBankName());
            existingCustomer.setBankOwner(existingCustomer.getBankOwner());
            //Personal
            existingCustomer.setDateOfBirth(existingCustomer.getDateOfBirth());
            existingCustomer.setGender(existingCustomer.getGender());
            existingCustomer.setFullName( existingCustomer.getFullName());
            existingCustomer.setPhoneNumber(existingCustomer.getPhoneNumber());
            existingCustomer.setAddress(existingCustomer.getAddress());
            //ID
            existingCustomer.setCitizenIdentification(existingCustomer.getCitizenIdentification());
            existingCustomer.setIdCardBackImage(existingCustomer.getIdCardBackImage());
            existingCustomer.setIdCardFrontImage(existingCustomer.getIdCardFrontImage());
            existingCustomer.setIdIssuanceDate(existingCustomer.getIdIssuanceDate());
            existingCustomer.setIdIssuancePlace(existingCustomer.getIdIssuancePlace());


            customerRepository.save(existingCustomer);
        }
  }







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
}
