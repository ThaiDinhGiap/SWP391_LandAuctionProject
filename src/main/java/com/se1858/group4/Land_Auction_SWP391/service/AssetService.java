package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Tag;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        asset.setAssetStatus("Waiting for Auction Scheduling");
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
            asset.setAssetStatus("Cancelled");
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

    public List<Asset> filterAssets(List<Integer> tagIds, String keyword, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : null;

        System.out.println(assetRepository.filterAssets(tagIds, keyword, fromDateTime, toDateTime).toArray().length);
        return assetRepository.filterAssets(tagIds, keyword, fromDateTime, toDateTime);
    }
}
