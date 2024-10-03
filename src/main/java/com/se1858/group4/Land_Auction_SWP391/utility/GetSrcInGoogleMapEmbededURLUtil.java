package com.se1858.group4.Land_Auction_SWP391.utility;

public class GetSrcInGoogleMapEmbededURL {
    public static String extractSrcFromIframe(String iframeCode) {
        // Tìm chỉ mục bắt đầu của thuộc tính src
        int startIndex = iframeCode.indexOf("src=\"") + 5;
        if (startIndex == -1) {
            return null; // Nếu không tìm thấy "src=\"", trả về null
        }
        // Tìm chỉ mục kết thúc của src
        int endIndex = iframeCode.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null; // Nếu không tìm thấy dấu " kết thúc, trả về null
        }
        // Lấy phần link src
        return iframeCode.substring(startIndex, endIndex);
    }
}
