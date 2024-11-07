package com.se1858.group4.Land_Auction_SWP391.exception;

public class BidAmountInsufficientException extends RuntimeException {
    private final String username;

    public BidAmountInsufficientException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
