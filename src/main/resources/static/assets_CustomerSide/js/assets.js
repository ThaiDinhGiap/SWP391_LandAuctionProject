function applyFilters(page = 1) {
    // Lấy giá trị từ form tìm kiếm
    const keyword = $('input[name="keyword"]').val();
    const fromDate = $('#fromDate').val();
    const toDate = $('#toDate').val();

    // Lấy giá trị từ form lọc tag
    const selectedTags = [];
    $('#tagFilterForm input[name="tagIds"]:checked').each(function () {
        selectedTags.push($(this).val());
    });

    // Gửi yêu cầu AJAX để lấy danh sách tài sản đã lọc
    $.ajax({
        url: '/customer/filter_assets',
        type: 'GET',
        data: {
            tagIds: selectedTags,
            keyword: keyword,
            fromDate: fromDate,
            toDate: toDate,
            page: page - 1
        },
        success: function (response) {
            // Cập nhật danh sách tài sản trong phần tử có id 'assetListContainer'
            $('#assetListContainer').html($(response).find('#assetListContainer').html());

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
        const pageItem = `<li><a class="page-numbers ${i === currentPage ? "current" : ""}" href="javascript:void(0);" onclick="applyFilters(${i})">${i}</a></li>`;
        paginationWrapper.append(pageItem);
    }

    // Thêm nút "Next" nếu chưa ở trang cuối
    if (currentPage < totalPages) {
        const nextItem = `<li><a class="next" href="javascript:void(0);" onclick="applyFilters(${currentPage + 1})">Next <span class="fa fa-angle-right"></span></a></li>`;
        paginationWrapper.append(nextItem);
    }
}

function clearSearchFilters() {
    // Xóa giá trị trong form tìm kiếm
    document.getElementById("searchForm").reset();
    applyFilters();
}

function clearTagFilters() {
    // Xóa giá trị trong form tag
    document.getElementById("tagFilterForm").reset();
    applyFilters();
}

// Tải trang đầu tiên khi mở trang
$(document).ready(function () {
    applyFilters();
});
