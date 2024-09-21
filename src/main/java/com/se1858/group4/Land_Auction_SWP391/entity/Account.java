package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @Column(name = "account_id", length = 8)
    private String accountId;

    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "status")
    private Integer status;

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "Account_Notification",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    private List<Notification> notifications;

    @OneToOne(mappedBy = "account", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Staff staff;

    @OneToOne(mappedBy = "account", cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Customer customer;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Ban_log> ban_logs;

    @OneToMany(mappedBy = "propertyAgent", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Task> tasks;

    @OneToMany(mappedBy = "auctioneer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Task> tasks;

    @OneToMany(mappedBy = "auctioneer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_session> auction_sessions;

    @OneToMany(mappedBy = "winner", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_session> auction_sessions;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_register> Auction_register;

    // Constructors
    public Account() {}

    public Account(String accountId, String email, List<Notification> notifications, String password, LocalDateTime registrationDate, Role role, Integer status, String username) {
        this.accountId = accountId;
        this.email = email;
        this.notifications = notifications;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
        this.status = status;
        this.username = username;
    }

    public Account(String username, Integer status, Staff staff, Role role, LocalDateTime registrationDate, String password, List<Notification> notifications, String email, String accountId) {
        this.username = username;
        this.status = status;
        this.staff = staff;
        this.role = role;
        this.registrationDate = registrationDate;
        this.password = password;
        this.notifications = notifications;
        this.email = email;
        this.accountId = accountId;
    }

    public Account(String accountId, String username, Integer status, Staff staff, Role role, LocalDateTime registrationDate, String password, List<Notification> notifications, String email, List<Ban_log> ban_logs) {
        this.accountId = accountId;
        this.username = username;
        this.status = status;
        this.staff = staff;
        this.role = role;
        this.registrationDate = registrationDate;
        this.password = password;
        this.notifications = notifications;
        this.email = email;
        this.ban_logs = ban_logs;
    }

    public Account(String accountId, List<Ban_log> ban_logs, Customer customer, String email, List<Notification> notifications, String password, LocalDateTime registrationDate, Role role, Staff staff, Integer status, String username) {
        this.accountId = accountId;
        this.ban_logs = ban_logs;
        this.customer = customer;
        this.email = email;
        this.notifications = notifications;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
        this.staff = staff;
        this.status = status;
        this.username = username;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<Ban_log> getBan_logs() {
        return ban_logs;
    }

    public void setBan_logs(List<Ban_log> ban_logs) {
        this.ban_logs = ban_logs;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Auction_session> getAuction_sessions() {
        return auction_sessions;
    }

    public void setAuction_sessions(List<Auction_session> auction_sessions) {
        this.auction_sessions = auction_sessions;
    }

    public List<Auction_register> getAuction_register() {
        return Auction_register;
    }

    public void setAuction_register(List<Auction_register> auction_register) {
        Auction_register = auction_register;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
