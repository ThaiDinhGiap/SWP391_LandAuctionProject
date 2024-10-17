package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Image;
import com.se1858.group4.Land_Auction_SWP391.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    public Image findImageById(int id) {
        return imageRepository.findById(id).get();
    }
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
}
