function applyAuctionSearch(page = 1) {
    // Lấy giá trị từ form tìm kiếm
    const keyword = $('input[name="keyword"]').val();
    const fromDate = $('#fromDate').val();
    const toDate = $('#toDate').val();

    // Lấy trạng thái phiên đấu giá
    const status = $('input[name="status"]:checked').val();

    // Gửi yêu cầu AJAX để lấy danh sách phiên đấu giá đã lọc
    $.ajax({
        url: '/customer/filter_auctions', // URL của controller xử lý lọc và tìm kiếm
        type: 'GET',
        data: {
            keyword: keyword,
            fromDate: fromDate,
            toDate: toDate,
            status: status,
            page: page - 1 // Truyền trang hiện tại (trừ 1 vì trang đầu tiên là 0)
        },
        success: function (response) {
            // Cập nhật danh sách phiên đấu giá trong phần tử có id 'auctionListContainer'
            $('#auctionListContainer').html($(response).find('#auctionListContainer').html());

            // Lấy totalPages và currentPage từ các thẻ ẩn
            const totalPages = parseInt($(response).find('#totalPages').val());
            const currentPage = parseInt($(response).find('#currentPage').val());

            // Cập nhật thanh phân trang
            updatePagination(totalPages, currentPage);
        },
        error: function (xhr, status, error) {
            console.error('Error occurred:', error);
        }
    });
}

function updatePagination(totalPages, currentPage) {
    const paginationWrapper = $('#pagination'); // Đảm bảo có phần tử với id "pagination"
    paginationWrapper.html(''); // Xóa thanh phân trang cũ

    // Tạo danh sách số trang
    for (let i = 1; i <= totalPages; i++) {
        const pageItem = `<li><a class="page-numbers ${i === currentPage ? "current" : ""}" href="javascript:void(0);" onclick="applyAuctionSearch(${i})">${i}</a></li>`;
        paginationWrapper.append(pageItem);
    }

    // Thêm nút "Next" nếu chưa ở trang cuối
    if (currentPage < totalPages) {
        const nextItem = `<li><a class="next" href="javascript:void(0);" onclick="applyAuctionSearch(${currentPage + 1})">Next <span class="fa fa-angle-right"></span></a></li>`;
        paginationWrapper.append(nextItem);
    }
}

function clearSearchAuctionsFilters() {
    // Xóa giá trị trong form tìm kiếm
    document.getElementById("searchForm").reset();
    applyAuctionSearch();
}

function clearStatusFilters() {
    // Xóa giá trị trong form trạng thái
    document.getElementById("statusFilterForm").reset();
    applyAuctionSearch();
}

// Tải trang đầu tiên khi mở trang
$(document).ready(function () {
    applyAuctionSearch();
});
