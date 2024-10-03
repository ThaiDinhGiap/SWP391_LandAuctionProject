package com.se1858.group4.Land_Auction_SWP391.dto;

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
}
