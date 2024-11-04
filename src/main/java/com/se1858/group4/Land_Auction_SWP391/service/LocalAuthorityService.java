package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.repository.LocalAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    public List<LocalAuthority> getAllLocalAuthority() {
        return localAuthorityRepository.findAll();
    }

    public LocalAuthority getLocalAuthorityById(int id) {
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

    public Page<LocalAuthority> findAllWithPaginationAndSorting(int page, int size, String sortField, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size, sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        if (keyword != null && !keyword.isEmpty()) {
            return localAuthorityRepository.findByKeyword(keyword, pageable);
        }

        return localAuthorityRepository.findAll(pageable);
    }

}
