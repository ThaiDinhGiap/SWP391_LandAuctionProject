package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Local_authority;
import com.se1858.group4.Land_Auction_SWP391.repository.LocalAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalAuthorityService {

    @Autowired
    private LocalAuthorityRepository localAuthorityRepository;

    public List<Local_authority> getAllLocalAuthorities() {
        return localAuthorityRepository.findAll();
    }

    public Local_authority getLocalAuthorityById(int id) {
        Optional<Local_authority> optionalLocalAuthority = localAuthorityRepository.findById(id);
        return optionalLocalAuthority.orElse(null);
    }

    public Local_authority createLocalAuthority(Local_authority localAuthority) {
        return localAuthorityRepository.save(localAuthority);
    }

    public Local_authority updateLocalAuthority(int id, Local_authority localAuthority) {
        if (localAuthorityRepository.existsById(id)) {
            localAuthority.setLocalAuthorityId(id);
            return localAuthorityRepository.save(localAuthority);
        }
        return null;
    }

    public boolean deleteLocalAuthority(int id) {
        if (localAuthorityRepository.existsById(id)) {
            localAuthorityRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
