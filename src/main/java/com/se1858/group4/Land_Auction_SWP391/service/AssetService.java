package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
    public Asset registerAsset(Asset asset) {
        asset.setAssetStatus("Waiting for Auction Scheduling");
        asset.setCreatedDate(LocalDateTime.now());
        return assetRepository.save(asset);
    }
    public Asset getAssetById(int id) {
        return assetRepository.findById(id).get();
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
    public Asset cancelAssetById(int id) {
        Asset asset = assetRepository.findById(id).get();
        if(asset!=null){
            asset.setAssetStatus("Canceled");
        }
        return updateAsset(asset);
    }
}
