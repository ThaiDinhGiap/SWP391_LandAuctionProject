package com.se1858.group4.Land_Auction_SWP391.utility;

import org.springframework.stereotype.Component;

@Component
public class QrCode {
    private String QrUrl;
    private final String bankId="vietinbank";
    private final String accountNo="103875765833";
    private final String template="compact2";
    private String amount;
    private String description;
    private final String acoountName="Bui Minh Chien";

    public QrCode(){
        amount="0";
        description="";
    }

    public QrCode(String amount, String description) {
        this.amount = amount;
        this.description = convertToUrlFormat(description);
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getTemplate() {
        return template;
    }

    public void setQrUrl(String qrUrl) {
        QrUrl = qrUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankId() {
        return bankId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAcoountName() {
        return acoountName;
    }

    public String convertToUrlFormat(String input) {
        String urlFormattedString = input.trim().replaceAll("\\s+", "%20");
        return urlFormattedString;
    }

    public String getQrUrl() {
        QrUrl="https://img.vietqr.io/image/"+bankId+"-"+accountNo+"-"+template+".jpg?"+"amount="+amount+"&addInfo="+description+"&accountName="+convertToUrlFormat(acoountName);
        return QrUrl;
    }

}
