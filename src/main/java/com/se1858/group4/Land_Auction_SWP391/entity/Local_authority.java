package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Local_authority")
public class Local_authority {
    @Id
    @Column(name = "local_authority_id")
    private int localAuthorityId;

    @Column(name = "local_authority_name", nullable = false)
    private String localAuthorityName;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "local_authority_address")
    private String localAuthorityAddress;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public Local_authority() {
    }

    public Local_authority(int localAuthorityId, String localAuthorityName, String contactPerson, String phoneNumber, String email, String localAuthorityAddress, LocalDateTime createdDate) {
        this.localAuthorityId = localAuthorityId;
        this.localAuthorityName = localAuthorityName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.localAuthorityAddress = localAuthorityAddress;
        this.createdDate = createdDate;
    }

    public int getLocalAuthorityId() {
        return localAuthorityId;
    }

    public void setLocalAuthorityId(int localAuthorityId) {
        this.localAuthorityId = localAuthorityId;
    }

    public String getLocalAuthorityName() {
        return localAuthorityName;
    }

    public void setLocalAuthorityName(String localAuthorityName) {
        this.localAuthorityName = localAuthorityName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocalAuthorityAddress() {
        return localAuthorityAddress;
    }

    public void setLocalAuthorityAddress(String localAuthorityAddress) {
        this.localAuthorityAddress = localAuthorityAddress;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Local_authority{" +
                "localAuthorityId=" + localAuthorityId +
                ", localAuthorityName='" + localAuthorityName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", localAuthorityAddress='" + localAuthorityAddress + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}

