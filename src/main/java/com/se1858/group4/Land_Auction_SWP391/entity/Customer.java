package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "address", columnDefinition = "NVARCHAR(MAX)")
    private String address;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "tax_identification_number", length = 50)
    private String taxIdentificationNumber;

    @Column(name = "citizen_identification", length = 50)
    private String citizenIdentification;

    @Column(name = "id_issuance_date")
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

    public Customer() {
    }

    public Customer(Account account, String fullName, String gender, String phoneNumber, String address, LocalDateTime dateOfBirth, String citizenIdentification, Image idCardBackImage, Image idCardFrontImage, LocalDate idIssuanceDate, String idIssuancePlace, String taxIdentificationNumber, String bankOwner, String bankName, String bankBranch, String bankAccountNumber) {
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

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
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
}