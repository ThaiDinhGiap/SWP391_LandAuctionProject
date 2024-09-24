package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
    public Asset registerAsset(Asset asset) {
        int id=2;
        asset.setAssetId(id);
        asset.setAssetStatus("Ban khong thanh cong");
        asset.setAuction_sessions(null);
        asset.setDocuments(null);
        asset.setImages(null);
        asset.setTags(null);
        return assetRepository.save(asset);
    }
    public List<Asset> getAllUnseccessfulSaleAsset() {
        List<Asset> list=assetRepository.findAll();
//        List<Asset> result=null;
//        System.out.println(list.size());
//        if(list.size()>0){
//            for(Asset asset:list){
//                if(asset.getAssetStatus().equals("Ban khong thanh cong")){
//                    result.add(asset);
//                }
//            }
//        }
        return list;
    }
}
