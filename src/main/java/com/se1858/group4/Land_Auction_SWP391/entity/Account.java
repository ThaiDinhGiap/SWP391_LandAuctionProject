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
    private String status;

    @Column(name = "email", length = 100)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "avatar_image_id", referencedColumnName = "image_id")
    private Image avatar_image;

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
    private List<Task> propertyAgent_tasks;

    @OneToMany(mappedBy = "auctioneer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Task> auctioneer_tasks;

    @OneToMany(mappedBy = "auctioneer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_session> auctioneer_auction_sessions;

    @OneToMany(mappedBy = "winner", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_session> winner_auction_sessions;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_register> auction_registers;

    // Constructors
    public Account() {}

    public Account(String accountId, String email, List<Notification> notifications, String password, LocalDateTime registrationDate, Role role, String status, String username) {
        this.accountId = accountId;
        this.email = email;
        this.notifications = notifications;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
        this.status = status;
        this.username = username;
    }

    public Account(String username, String status, Staff staff, Role role, LocalDateTime registrationDate, String password, List<Notification> notifications, String email, String accountId) {
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

    public Account(String accountId, String username, String status, Staff staff, Role role, LocalDateTime registrationDate, String password, List<Notification> notifications, String email, List<Ban_log> ban_logs) {
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

    public Account(String accountId, List<Ban_log> ban_logs, Customer customer, String email, List<Notification> notifications, String password, LocalDateTime registrationDate, Role role, Staff staff, String status, String username) {
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

    public Account(String accountId, List<Ban_log> ban_logs, Customer customer, String email, Image avatar_image, String password, LocalDateTime registrationDate, Role role, Staff staff, String status, String username) {
        this.accountId = accountId;
        this.ban_logs = ban_logs;
        this.customer = customer;
        this.email = email;
        this.avatar_image = avatar_image;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public List<Auction_register> getAuction_registers() {
        return auction_registers;
    }

    public void setAuction_registers(List<Auction_register> auction_registers) {
        this.auction_registers = auction_registers;
    }

    public List<Auction_session> getWinner_auction_sessions() {
        return winner_auction_sessions;
    }

    public void setWinner_auction_sessions(List<Auction_session> winner_auction_sessions) {
        this.winner_auction_sessions = winner_auction_sessions;
    }

    public List<Task> getPropertyAgent_tasks() {
        return propertyAgent_tasks;
    }

    public void setPropertyAgent_tasks(List<Task> propertyAgent_tasks) {
        this.propertyAgent_tasks = propertyAgent_tasks;
    }

    public List<Task> getAuctioneer_tasks() {
        return auctioneer_tasks;
    }

    public void setAuctioneer_tasks(List<Task> auctioneer_tasks) {
        this.auctioneer_tasks = auctioneer_tasks;
    }

    public List<Auction_session> getAuctioneer_auction_sessions() {
        return auctioneer_auction_sessions;
    }

    public void setAuctioneer_auction_sessions(List<Auction_session> auctioneer_auction_sessions) {
        this.auctioneer_auction_sessions = auctioneer_auction_sessions;
    }

    public Image getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(Image avatar_image) {
        this.avatar_image = avatar_image;
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
