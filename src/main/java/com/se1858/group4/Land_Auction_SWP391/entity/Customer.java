package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "address", columnDefinition = "NVARCHAR(MAX)")
    private String address;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "tax_identification_number", length = 50)
    private String taxIdentificationNumber;

    @Column(name = "citizen_identification", length = 50)
    private String citizenIdentification;

    @Column(name = "id_issuance_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate idIssuanceDate;

    @Column(name = "id_issuance_place", columnDefinition = "NVARCHAR(MAX)")
    private String idIssuancePlace;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "id_card_front_image_id", referencedColumnName = "image_id")
    private Image idCardFrontImage;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "id_card_back_image_id", referencedColumnName = "image_id")
    private Image idCardBackImage;

    @Column(name = "bank_account_number", length = 255)
    private String bankAccountNumber;

    @Column(name = "bank_name", columnDefinition = "NVARCHAR(MAX)")
    private String bankName;

    @Column(name = "bank_branch", columnDefinition = "NVARCHAR(MAX)")
    private String bankBranch;

    @Column(name = "bank_owner", length = 255)
    private String bankOwner;

    @Column(name = "update_status", length = 255)
    private String updateStatus;

    public Customer() {
    }

    public Customer(String bankOwner, String bankBranch, String bankName, String bankAccountNumber, Image idCardBackImage, Image idCardFrontImage, String idIssuancePlace, LocalDate idIssuanceDate, String citizenIdentification, String taxIdentificationNumber, String phoneNumber, String address, LocalDate dateOfBirth, String gender, String fullName, Account account, int customerId) {
        this.bankOwner = bankOwner;
        this.bankBranch = bankBranch;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.idCardBackImage = idCardBackImage;
        this.idCardFrontImage = idCardFrontImage;
        this.idIssuancePlace = idIssuancePlace;
        this.idIssuanceDate = idIssuanceDate;
        this.citizenIdentification = citizenIdentification;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.fullName = fullName;
        this.account = account;
        this.customerId = customerId;
    }

    public Customer(int customerId, Account account, String fullName, String gender, LocalDate dateOfBirth, String address, String phoneNumber, String taxIdentificationNumber, String citizenIdentification, LocalDate idIssuanceDate, String idIssuancePlace, Image idCardFrontImage, Image idCardBackImage, String bankAccountNumber, String bankName, String bankBranch, String bankOwner, String updateStatus) {
        this.customerId = customerId;
        this.account = account;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.citizenIdentification = citizenIdentification;
        this.idIssuanceDate = idIssuanceDate;
        this.idIssuancePlace = idIssuancePlace;
        this.idCardFrontImage = idCardFrontImage;
        this.idCardBackImage = idCardBackImage;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.bankBranch = bankBranch;
        this.bankOwner = bankOwner;
        this.updateStatus = updateStatus;
    }

    public Customer(Account account, String fullName, String gender, String phoneNumber, String address, LocalDate dateOfBirth, String citizenIdentification, Image idCardBackImage, Image idCardFrontImage, LocalDate idIssuanceDate, String idIssuancePlace, String taxIdentificationNumber, String bankOwner, String bankName, String bankBranch, String bankAccountNumber) {
        this.account = account;
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.citizenIdentification = citizenIdentification;
        this.idCardBackImage = idCardBackImage;
        this.idCardFrontImage = idCardFrontImage;
        this.idIssuanceDate = idIssuanceDate;
        this.idIssuancePlace = idIssuancePlace;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.bankOwner = bankOwner;
        this.bankName = bankName;
        this.bankBranch = bankBranch;
        this.bankAccountNumber = bankAccountNumber;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankOwner() {
        return bankOwner;
    }

    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner;
    }

    public String getCitizenIdentification() {
        return citizenIdentification;
    }

    public void setCitizenIdentification(String citizenIdentification) {
        this.citizenIdentification = citizenIdentification;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDateOfBirth() {

        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Image getIdCardBackImage() {
        return idCardBackImage;
    }

    public void setIdCardBackImage(Image idCardBackImage) {
        this.idCardBackImage = idCardBackImage;
    }

    public Image getIdCardFrontImage() {
        return idCardFrontImage;
    }

    public void setIdCardFrontImage(Image idCardFrontImage) {
        this.idCardFrontImage = idCardFrontImage;
    }

    public LocalDate getIdIssuanceDate() {
        return idIssuanceDate;
    }

    public void setIdIssuanceDate(LocalDate idIssuanceDate) {
        this.idIssuanceDate = idIssuanceDate;
    }

    public String getIdIssuancePlace() {
        return idIssuancePlace;
    }

    public void setIdIssuancePlace(String idIssuancePlace) {
        this.idIssuancePlace = idIssuancePlace;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }
public String getUpdateStatus() {
        return updateStatus;
    }
  public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "account=" + account +
                ", customerId=" + customerId +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", taxIdentificationNumber='" + taxIdentificationNumber + '\'' +
                ", citizenIdentification='" + citizenIdentification + '\'' +
                ", idIssuanceDate=" + idIssuanceDate +
                ", idIssuancePlace='" + idIssuancePlace + '\'' +
                ", idCardFrontImage=" + idCardFrontImage +
                ", idCardBackImage=" + idCardBackImage +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", bankOwner='" + bankOwner + '\'' +
                '}';
    }

    //add front image
    public void addFrontImage(Image image) {
        if (this.idCardFrontImage == null) {
            this.idCardFrontImage = image;
        }
    }

    //add back image
    public void addBackImage(Image image) {
        if (this.idCardBackImage == null) {
            this.idCardBackImage = image;
        }
    }
}