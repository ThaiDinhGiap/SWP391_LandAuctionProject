package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.repository.LocalAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocalAuthorityService {
    private LocalAuthorityRepository localAuthorityRepository;
    @Autowired
    public LocalAuthorityService(LocalAuthorityRepository localAuthorityRepository) {
        this.localAuthorityRepository = localAuthorityRepository;
    }
    public List<LocalAuthority> findAll() {
        return localAuthorityRepository.findAll();
    }

    public List<LocalAuthority> getAllLocalAuthorities() {
        return localAuthorityRepository.findAll();
    }

    public List<LocalAuthority> getAllLocalAuthority(){
        return localAuthorityRepository.findAll();
    }

    public LocalAuthority getLocalAuthorityById(int id){
        return localAuthorityRepository.findById(id).get();
    }

    public void saveAuthority(LocalAuthority authority) {
        authority.setCreatedDate(LocalDateTime.now()); // Gán ngày tạo
        localAuthorityRepository.save(authority);
    }

    public LocalAuthority findById(Integer id) {
        return localAuthorityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid authority Id:" + id));
    }
}
