function applyNewsSearch(page = 1) {
    // Lấy giá trị từ form tìm kiếm
    const keyword = $('input[name="keyword"]').val();

    // Lấy giá trị từ form lọc tag
    const selectedTags = [];
    $('#tagFilterNewsForm input[name="tagNewsIds"]:checked').each(function () {
        selectedTags.push($(this).val());
    });

    // Gửi yêu cầu AJAX để lấy danh sách tin tức đã lọc
    $.ajax({
        url: '/customer/filter_news', // URL của controller xử lý lọc và tìm kiếm
        type: 'GET',
        data: {
            tagIds: selectedTags,
            keyword: keyword,
            page: page - 1 // Truyền trang hiện tại (trừ 1 vì trang đầu tiên là 0)
        },
        success: function (response) {
            // Cập nhật danh sách tin tức trong phần tử có id 'newsListContainer'
            $('#newsListContainer').html($(response).find('#newsListContainer').html());

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
        const pageItem = `<li><a class="page-numbers ${i === currentPage ? "current" : ""}" href="javascript:void(0);" onclick="applyNewsSearch(${i})">${i}</a></li>`;
        paginationWrapper.append(pageItem);
    }

    // Thêm nút "Next" nếu chưa ở trang cuối
    if (currentPage < totalPages) {
        const nextItem = `<li><a class="next" href="javascript:void(0);" onclick="applyNewsSearch(${currentPage + 1})">Next <span class="fa fa-angle-right"></span></a></li>`;
        paginationWrapper.append(nextItem);
    }
}

function clearSearchNewsFilters() {
    // Xóa giá trị trong form tìm kiếm
    document.getElementById("searchForm").reset();
    applyNewsSearch();
}

function clearTagNewsFilters() {
    // Xóa giá trị trong form tag
    document.getElementById("tagFilterNewsForm").reset();
    applyNewsSearch();
}

// Tải trang đầu tiên khi mở trang
$(document).ready(function () {
    applyNewsSearch();
});
