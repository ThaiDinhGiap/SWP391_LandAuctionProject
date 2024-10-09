package com.se1858.group4.Land_Auction_SWP391.utility;

import java.math.BigDecimal;

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

    public String convertToUrlFormat(String input) {
        String urlFormattedString = input.trim().replaceAll("\\s+", "%20");
        return urlFormattedString;
    }

    public String getQrUrl() {
        QrUrl="https://img.vietqr.io/image/"+bankId+"-"+accountNo+"-"+template+".jpg?"+"amount="+amount+"&addInfo="+description+"&accountName="+convertToUrlFormat(acoountName);
        return QrUrl;
    }

}
