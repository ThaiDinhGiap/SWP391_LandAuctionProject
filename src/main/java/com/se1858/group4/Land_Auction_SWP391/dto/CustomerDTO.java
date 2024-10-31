package com.se1858.group4.Land_Auction_SWP391.dto;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;

public class CustomerDTO {
    private Customer customer;
    private Account account;

    public CustomerDTO() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


}
