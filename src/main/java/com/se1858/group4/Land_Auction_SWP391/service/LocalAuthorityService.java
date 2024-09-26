package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.repository.LocalAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalAuthorityService {
    private LocalAuthorityRepository localAuthorityRepository;
    @Autowired
    public LocalAuthorityService(LocalAuthorityRepository localAuthorityRepository) {
        this.localAuthorityRepository = localAuthorityRepository;
    }
    public List<LocalAuthority> getAllLocalAuthority(){
        return localAuthorityRepository.findAll();
    }
    public Optional<LocalAuthority> getLocalAuthorityById(int id){
        return localAuthorityRepository.findById(id);
    }
}
