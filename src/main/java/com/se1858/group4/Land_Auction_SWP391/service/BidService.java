package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.dto.BidRequestDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.BidResponseDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Bid;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionSessionRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRegisterRepository auctionRegisterRepository;

    @Autowired
    private AuctionSessionRepository auctionSessionRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Handle the logic for placing a bid and return the response information
     *
     * @param bidRequestDTO the bid information sent from the client
     * @return BidResponseDTO the response information about the processed bid
     */
    @Transactional
    public BidResponseDTO placeBid(BidRequestDTO bidRequestDTO) {

        Optional<AuctionRegister> optionalRegister = auctionRegisterRepository.findByAuction_AuctionIdAndBuyer_AccountId(bidRequestDTO.getAuctionId(), accountRepository.findByUsername(bidRequestDTO.getSender()).getAccountId());
        if (!optionalRegister.isPresent()) {
            throw new IllegalArgumentException("Invalid register ID.");
        }
        AuctionRegister auctionRegister = optionalRegister.get();


        Optional<AuctionSession> optionalAuctionSession= auctionSessionRepository.findById(bidRequestDTO.getAuctionId());
        AuctionSession auctionSession = optionalAuctionSession.get();

        // Validate the bid amount against the minimum increment
        Long currentHighestBid = auctionSession.getCurrentHighestPrice();
        Long minimumBidIncrement = auctionSession.getMinimumBidIncrement();

        if (bidRequestDTO.getContent() <= currentHighestBid + minimumBidIncrement) {
            throw new IllegalArgumentException("Bid amount is insufficient.");
        }

        // Create a new bid entity and save it
        Bid bid = new Bid();
        bid.setBidAmount(bidRequestDTO.getContent());
        bid.setTimeCreateBid(LocalDateTime.now());
        bid.setAuctionRegister(auctionRegister);

        Bid savedBid = bidRepository.save(bid);

        // Update the current highest price in the auction session
        auctionRegister.getAuction().setCurrentHighestPrice(savedBid.getBidAmount());

        return new BidResponseDTO(
                bidRequestDTO.getSender(),
                savedBid.getBidAmount(),
                savedBid.getTimeCreateBid(),
                "BID",
                auctionRegister.getAuction().getAuctionId()
        );
    }
}

