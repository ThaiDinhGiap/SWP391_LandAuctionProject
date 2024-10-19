function applyAuctionSearch() {
    // Lấy giá trị từ form tìm kiếm
    const keyword = $('input[name="keyword"]').val();
    const fromDate = $('#fromDate').val();
    const toDate = $('#toDate').val();


    // Gửi yêu cầu AJAX để lấy danh sách phiên đấu giá đã lọc
    $.ajax({
        url: '/customer/filter_auctions', // URL của controller xử lý lọc và tìm kiếm
        type: 'GET',
        data: {
            keyword: keyword,
            fromDate: fromDate,
            toDate: toDate
        },
        success: function (response) {
            // Cập nhật danh sách phiên đấu giá trong phần tử có id 'auctionListContainer'
            $('#auctionListContainer').html(response);
        },
        error: function (xhr, status, error) {
            console.error('Error occurred:', error);
        }
    });
}


function clearSearchAuctionsFilters() {
    // Xóa giá trị trong form tìm kiếm
    document.getElementById("searchForm").reset();
    applyAuctionSearch();
}



