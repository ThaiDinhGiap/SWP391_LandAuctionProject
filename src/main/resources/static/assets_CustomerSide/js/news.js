function applyNewsSearch() {
    // Lấy giá trị từ form tìm kiếm
    const keyword = $('input[name="keyword"]').val();

    // Lấy giá trị từ form lọc tag
    const selectedTags = [];
    $('#tagFilterNewsForm input[name="tagNewsIds"]:checked').each(function () {
        selectedTags.push($(this).val());
    });

    // Gửi yêu cầu AJAX để lấy danh sách phiên đấu giá đã lọc
    $.ajax({
        url: '/customer/filter_news', // URL của controller xử lý lọc và tìm kiếm
        type: 'GET',
        data: {
            tagIds: selectedTags,
            keyword: keyword
        },
        success: function (response) {
            // Cập nhật danh sách phiên đấu giá trong phần tử có id 'auctionListContainer'
            $('#newsListContainer').html(response);
        },
        error: function (xhr, status, error) {
            console.error('Error occurred:', error);
        }
    });
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