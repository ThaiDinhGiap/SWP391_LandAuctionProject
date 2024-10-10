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
    private StaffRepository staffRepository;
    private WebSocketService webSocketService;

    @Autowired
    public StaffService(StaffRepository staffRepository, WebSocketService webSocketService) {
        this.staffRepository = staffRepository;
        this.webSocketService = webSocketService;
    }

    public List<Staff> getCustomerCareStaff() {
        return staffRepository.findAllByAccount_Role_RoleName("ROLE_Customer_Care");
    }

    public Staff findById(int id) {
        return staffRepository.findById(id).orElse(null);
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

    // Phương thức kiểm tra trạng thái của staff
    public boolean isStaffAvailable(Integer staffId) {
        // Lấy thông tin staff từ database
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new IllegalArgumentException("Staff không tồn tại"));

        // Trả về trạng thái của staff
        return staff.isAvailable();
    }

    // Cập nhật trạng thái của staff
    public void setStaffAvailability(Integer staffId, boolean isAvailable) {
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new IllegalArgumentException("Staff không tồn tại"));
        staff.setAvailable(isAvailable);
        staffRepository.save(staff); // Lưu lại trạng thái của staff trong DB
    }

    public void requestStaffConfirmation(Integer staffId, Integer clientId) {
        // Giả sử chúng ta có WebSocket hoặc hệ thống nhắn tin tới Staff
        webSocketService.requestConfirmationFromStaff(staffId, clientId);
    }

}
