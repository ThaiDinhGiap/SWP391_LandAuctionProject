package com.se1858.group4.Land_Auction_SWP391.dto;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private int staffId;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;

    private Staff staff;
    private Account account;
    //constructor
    public StaffDTO(int staffId, String fullName, String gender, String phoneNumber, String email) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.gender= gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Staff getStaff() {
        return staff;
    }

    public Account getAccount() {
        return account;
    }


}
