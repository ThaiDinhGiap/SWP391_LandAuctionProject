package com.se1858.group4.Land_Auction_SWP391.utility;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Document;
import com.se1858.group4.Land_Auction_SWP391.entity.Image;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UploadFile {
    private AssetService assetService;
    @Autowired
    public UploadFile(AssetService assetService) {
        this.assetService = assetService;
    }
    //ham upload anh
    public void UploadImages(List<MultipartFile> images, Asset asset){
        String imageUploadDir = "src/main/resources/static/image/";
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    // Lấy tên file gốc
                    String originalFileName = image.getOriginalFilename();
                    if (originalFileName == null) continue;
                    // Tách tên file và phần mở rộng
                    String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                    // Tạo đường dẫn file
                    String imgName = "Asset_" + fileName + fileExtension;
                    Path path = Paths.get(imageUploadDir + imgName);
                    // Kiểm tra file đã tồn tại hay chưa, nếu có thì thêm số phiên bản vào
                    int version = 1;
                    while (Files.exists(path)) {
                        imgName = "Asset_" + fileName + "(" + version + ")" + fileExtension;
                        path = Paths.get(imageUploadDir + imgName);
                        version++;
                    }
                    // Lưu tệp vào thư mục
                    byte[] bytes = image.getBytes();
                    Files.write(path, bytes);
                    // Tạo đối tượng Image và liên kết với Asset
                    Image img = new Image();
                    img.setUploadDate(LocalDateTime.now());
                    img.setAsset(asset);
                    img.setPath("/image/" + imgName);
                    asset.addImage(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //ham upload folder
    public void UploadDocuments(List<MultipartFile> documents, Asset asset){
        String documentUploadDir = "src/main/resources/static/document/";
        for (MultipartFile document : documents) {
            if (!document.isEmpty()) {
                try {
                    // Lấy tên file gốc
                    String originalFileName = document.getOriginalFilename();
                    if (originalFileName == null) continue;
                    // Tách tên file và phần mở rộng
                    String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                    // Tạo đường dẫn file
                    String documentName = "Asset_" + fileName + fileExtension;
                    Path path = Paths.get(documentUploadDir + documentName);
                    // Kiểm tra file đã tồn tại hay chưa, nếu có thì thêm số phiên bản vào
                    int version = 1;
                    while (Files.exists(path)) {
                        documentName = "Asset_" + fileName + "(" + version + ")" + fileExtension;
                        path = Paths.get(documentUploadDir + documentName);
                        version++;
                    }
                    // Lưu tệp vào thư mục
                    byte[] bytes = document.getBytes();
                    Files.write(path, bytes);
                    // Tạo đối tượng Image và liên kết với Asset
                    Document doc = new Document();
                    doc.setUploadDate(LocalDateTime.now());
                    doc.setAsset(asset);
                    doc.setPath("/document/" + documentName);
                    asset.addDocument(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
