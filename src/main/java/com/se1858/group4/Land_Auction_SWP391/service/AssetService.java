package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Tag;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    private TagRepository tagRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository, TagRepository tagRepository) {
        this.assetRepository = assetRepository;
        this.tagRepository = tagRepository;
    }
    public Asset registerAsset(Asset asset) {
        asset.setAssetStatus("Waiting for auction scheduling");
        asset.setCreatedDate(LocalDateTime.now());
        return assetRepository.save(asset);
    }
    public Asset getAssetById(int id) {
        Optional<Asset> asset = assetRepository.findById(id);
        if (asset.isPresent()) {
            return asset.get();
        }
        return null;
    }
    public Asset updateAsset(Asset newAsset) {
        Asset existingAsset = getAssetById(newAsset.getAssetId());
        if(existingAsset!=null) {
            existingAsset.setAssetStatus(newAsset.getAssetStatus());
            return assetRepository.save(existingAsset);
        }
        else return null;
    }
    public List<Asset> getAllAssetWithStatus(String status) {
        List<Asset> list=assetRepository.findAll();
        List<Asset> result=null;
        if(list.size()>0){
            result=new ArrayList<Asset>();
            for(Asset asset:list){
                if(asset.getAssetStatus().equals(status)){
                    result.add(asset);
                }
            }
        }
        return result;
    }
    public List<Asset> getAllAsset() {
        List<Asset> list=assetRepository.findAll();
        if(list.size()>0){
            return list;
        }
        return null;
    }
    public Asset cancelAssetById(int id) {
        Asset asset = assetRepository.findById(id).get();
        if(asset!=null){
            asset.setAssetStatus("Cancelled registration");
        }
        return updateAsset(asset);
    }

    public List<Asset> getAssetsByTagIds(List<Integer> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        Set<Asset> filteredAssets = new HashSet<>(tags.get(0).getAssets());
        for (Tag tag : tags) {
            filteredAssets.retainAll(tag.getAssets());
        }
        return new ArrayList<>(filteredAssets);
    }

    public Page<Asset> getAssets(int page) {
        Pageable pageable = PageRequest.of(page, 4); // Mỗi trang 6 bản ghi
        return assetRepository.findAll(pageable);
    }


    public Page<Asset> filterAssets(List<Integer> tagIds, String keyword, LocalDate fromDate, LocalDate toDate, int page) {
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : null;


        Pageable pageable = PageRequest.of(page, 4); // Mỗi trang 6 bản ghi
        return assetRepository.filterAssets(tagIds, keyword, fromDateTime, toDateTime, pageable);
    }

    public List<Asset> getTop3LastestAssets() {
        return assetRepository.findTop3ByOrderByCreatedDateDesc();
    }

    public boolean setSuccessSoldForAsset(Asset asset) {
        try {
            asset.setAssetStatus("Successfully sold");
            assetRepository.save(asset);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setFailedSoldForAsset(Asset asset) {
        try {
            asset.setAssetStatus("Failed sold");
            assetRepository.save(asset);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
