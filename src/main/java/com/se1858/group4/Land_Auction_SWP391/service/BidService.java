package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.dto.BidRequestDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.BidResponseDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.Bid;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
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

    /**
     * Xử lý logic đặt bid và trả về thông tin phản hồi cho client
     * @param bidRequestDTO thông tin bid được gửi từ client
     * @return BidResponseDTO thông tin phản hồi về bid đã được xử lý
     */
    @Transactional
    public BidResponseDTO placeBid(BidRequestDTO bidRequestDTO) {
        // Lấy thông tin người tham gia đấu giá (Auction_register) từ repository
        Optional<AuctionRegister> optionalRegister = auctionRegisterRepository.findById(bidRequestDTO.getRegisterId());
        if (!optionalRegister.isPresent()) {
            throw new IllegalArgumentException("Invalid register ID.");
        }

        AuctionRegister auctionRegister = optionalRegister.get();

        // Kiểm tra bước giá tối thiểu (giả sử ta có thuộc tính minimumBidIncrement trong AuctionSession)
        if (bidRequestDTO.getBidAmount() <= auctionRegister.getAuction().getCurrentHighestPrice() + auctionRegister.getAuction().getMinimumBidIncrement()) {
            throw new IllegalArgumentException("Bid amount is not sufficient.");
        }

        // Tạo mới một bid entity
        Bid bid = new Bid();
        bid.setBidAmount(bidRequestDTO.getBidAmount());
        bid.setTimeCreateBid(bidRequestDTO.getBidTime() != null ? bidRequestDTO.getBidTime() : LocalDateTime.now());
        bid.setAuctionRegister(auctionRegister);

        // Lưu thông tin bid vào cơ sở dữ liệu
        Bid savedBid = bidRepository.save(bid);

        // Cập nhật giá cao nhất của phiên đấu giá
        auctionRegister.getAuction().setCurrentHighestPrice(bidRequestDTO.getBidAmount());

        return new BidResponseDTO(
                savedBid.getBidId(),
                savedBid.getBidAmount(),
                auctionRegister.getRegisterId(),
                savedBid.getTimeCreateBid()
        );
    }
}
