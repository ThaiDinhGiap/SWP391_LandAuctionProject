function applyFilters() {
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
        url: '/customer/filter_assets', // URL của controller xử lý lọc và tìm kiếm
        type: 'GET',
        data: {
            tagIds: selectedTags,
            keyword: keyword,
            fromDate: fromDate,
            toDate: toDate
        },
        success: function (response) {
            // Cập nhật danh sách tài sản trong phần tử có id 'assetListContainer'
            $('#assetListContainer').html(response);
        },
        error: function (xhr, status, error) {
            console.error('Error occurred:', error);
        }
    });
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
