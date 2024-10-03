package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> getCustomerCareStaff() {
        List<Staff> staffList = staffRepository.findAllByAccount_Role_RoleName("ROLE_Customer_Care");
        System.out.println(staffList);
        return staffRepository.findAllByAccount_Role_RoleName("ROLE_Customer_Care");
    }

    // Convert Staff entity to StaffDTO
    public StaffDTO convertToDTO(Staff staff) {
        return new StaffDTO(
                staff.getStaffId(),
                staff.getFullName(),
                staff.getGender(),
                staff.getPhoneNumber(),
                staff.getAccount().getEmail()  // Lấy email từ account liên kết
        );
    }

    // Phương thức lấy danh sách staff dạng DTO
    public List<StaffDTO> getCustomerCareStaffDTOs() {
        List<Staff> staffList = getCustomerCareStaff();
        return staffList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
