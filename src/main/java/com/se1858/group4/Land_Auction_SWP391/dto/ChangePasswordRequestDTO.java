package com.se1858.group4.Land_Auction_SWP391.dto;

public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;

    // Getters and setters
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
