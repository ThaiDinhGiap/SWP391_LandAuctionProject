package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
    public Asset registerAsset(Asset asset) {
        asset.setAssetStatus("Dang xac minh");
        return assetRepository.save(asset);
    }
    public Optional<Asset> getAssetById(int id) {
        return assetRepository.findById(id);
    }
    public List<Asset> getAllUnseccessfulSaleAsset() {
        List<Asset> list=assetRepository.findAll();
        List<Asset> result=null;
        if(list.size()>0){
            result=new ArrayList<Asset>();
            for(Asset asset:list){
                if(asset.getAssetStatus().equals("Ban khong thanh cong")){
                    result.add(asset);
                }
            }
        }
        return result;
    }
    public List<Asset> getAllVerifiedAsset() {
        List<Asset> list=assetRepository.findAll();
        List<Asset> result=null;
        if(list.size()>0){
            result=new ArrayList<Asset>();
            for(Asset asset:list){
                if(asset.getAssetStatus().equals("Dang xac minh")){
                    result.add(asset);
                }
            }
        }
        return result;
    }
}
