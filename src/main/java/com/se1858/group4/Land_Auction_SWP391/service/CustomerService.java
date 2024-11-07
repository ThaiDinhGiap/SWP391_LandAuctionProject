package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerService() {
    }
//
//    public void updateCustomerDetails(Customer customer) {
//        Optional<Customer> existingCustomerOpt = customerRepository.findById(customer.getCustomerId());
//        if (existingCustomerOpt.isPresent()) {
//            Customer existingCustomer = existingCustomerOpt.get();
//
//            // Bank details
//            existingCustomer.setBankAccountNumber(customer.getBankAccountNumber());
//            existingCustomer.setBankBranch(customer.getBankBranch());
//            existingCustomer.setBankName(customer.getBankName());
//            existingCustomer.setBankOwner(customer.getBankOwner());
//
//            // Personal details
//            existingCustomer.setDateOfBirth(customer.getDateOfBirth());
//            existingCustomer.setGender(customer.getGender());
//            existingCustomer.setFullName(customer.getFullName());
//            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
//            existingCustomer.setAddress(customer.getAddress());
//
//            // ID details
//            existingCustomer.setCitizenIdentification(customer.getCitizenIdentification());
//            existingCustomer.setIdCardBackImage(customer.getIdCardBackImage());
//            existingCustomer.setIdCardFrontImage(customer.getIdCardFrontImage());
//            existingCustomer.setIdIssuanceDate(customer.getIdIssuanceDate());
//            existingCustomer.setIdIssuancePlace(customer.getIdIssuancePlace());
//            existingCustomer.setUpdateStatus("completed");
//
//            customerRepository.save(existingCustomer);
//        } else {
//            // Xử lý trường hợp không tìm thấy khách hàng
//            throw new NoSuchElementException("Customer not found for ID: " + customer.getCustomerId());
//        }
//    }


    public void updateCustomerDetails(Customer customer) {

        // Bank details
        customer.setUpdateStatus("completed");

        // Lưu lại customer đã được cập nhật vào database
        customerRepository.save(customer);
    }

    public List<Customer> findCustomerByUpdateStatus(String updateStatus) {
        return customerRepository.findByUpdateStatus(updateStatus);
    }
    public Customer findCustomerByID(int id) {
        return customerRepository.findById(id).get();
    }
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
