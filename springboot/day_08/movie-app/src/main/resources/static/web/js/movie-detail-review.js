// 1. Xóa review
const deleteReview = (reviewId) => {
    console.log(reviewId)
    // Hỏi người dùng có chắc chắn xóa không?
    const isConfirmed = window.confirm('Bạn có chắc chắn muốn xóa review này?')
    if (!isConfirmed) return

    // Gọi API để xóa review (fetch, axios, ajax, ...)
    const url = `/api/reviews/${reviewId}`
    axios.delete(url)
        .then(function (response) {
            alert('Xóa review thành công')

            // Xóa trên giao diện (recommended) / reload lại trang
            window.location.reload()
        })
        .catch(function (error) {
            console.log(error);
        });
}

// 2. Tạo mới review

// 3. Cập nhật review
const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

let currentRating = 0;

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}

// Hàm để tạo mới review
const createReview = () => {
    // Lấy giá trị đánh giá và nội dung từ modal
    const ratingValueElement = document.getElementById('rating-value');
    const contentElement = document.getElementById('exampleFormControlTextarea1');

    // Lấy giá trị từ các phần tử HTML
    const ratingValue = parseInt(ratingValueElement.innerText);
    const content = contentElement.value;
// Hàm để lấy movieId từ URL
    const getMovieIdFromUrl = () => {
        // Lấy đường dẫn URL hiện tại
        const currentUrl = window.location.href;

        // Sử dụng biểu thức chính quy để lấy số sau cùng trong đường dẫn URL
        const match = currentUrl.match(/\/phim\/(\d+)\/.*/);

        // Kiểm tra xem có trùng khớp hay không
        if (match && match[1]) {
            // match[1] chứa giá trị của movieId
            return parseInt(match[1]);
        }

        // Trả về null nếu không tìm thấy movieId
        return null;
    };

// Sử dụng hàm để lấy movieId
    const movieId = getMovieIdFromUrl();


    // Gọi API để tạo review (axios, fetch, ajax, ...)
    const url = "/api/reviews";
    const data = {
        rating: ratingValue,
        content: content,
        movieId: movieId
    };

    axios.post(url, data)
        .then(function (response) {
            // Xử lý phản hồi thành công nếu cần
            console.log('API Response:', response.data);
            alert('Đánh giá đã được tạo thành công');

            // Đóng modal sau khi tạo đánh giá thành công (nếu sử dụng modal)
            $('#modal-create-review').modal('hide');

            // Có thể cập nhật giao diện người dùng mà không cần tải lại trang
        })
        .catch(function (error) {
            // Xử lý lỗi nếu cần
            console.error('API Error:', error);

            // Không hiển thị thông báo lỗi, chỉ cần đóng modal (nếu sử dụng modal)
            // $('#modal-create-review').modal('hide');
        });
};

// Hàm mở modal cập nhật


const updateReview = (reviewId) => {
    // Lấy giá trị đánh giá và nội dung từ modal
    const updatedRatingValueElement = document.getElementById('rating-value');
    const updatedContentElement = document.getElementById('exampleFormControlTextarea1');

    // Lấy giá trị từ các phần tử HTML
    const updatedRatingValue = parseInt(updatedRatingValueElement.innerText);
    const updatedContent = updatedContentElement.value;

    // Gọi API để lấy thông tin review hiện tại
    const getReviewDetails = (reviewId) => {
        const url = `/api/reviews/${reviewId}`;

        axios.get(url)
            .then(function (response) {
                // Xử lý phản hồi thành công
                const reviewDetails = response.data;

                // Hiển thị thông tin trong modal cập nhật
                updatedRatingValueElement.innerText = reviewDetails.rating;
                updatedContentElement.value = reviewDetails.content;

                // Hiển thị modal cập nhật
                $('#modal-create-review').modal('show');
            })
            .catch(function (error) {
                // Xử lý lỗi nếu cần
                console.error('API Error:', error);
            });
    };

    // Gọi hàm để lấy thông tin review và mở modal cập nhật
    getReviewDetails(reviewId);

    // Lắng nghe sự kiện click cho nút "Hoàn tất" trong modal cập nhật
    const btnUpdate = document.getElementById('btnUpdate');
    btnUpdate.addEventListener('click', function () {
        // Gọi API để cập nhật review với thông tin đã chỉnh sửa
        const url = `/api/reviews/${reviewId}`;
        const data = {
            rating: updatedRatingValue,
            content: updatedContent
        };

        axios.put(url, data)
            .then(function (response) {
                // Xử lý phản hồi thành công nếu cần
                console.log('API Response:', response.data);
                alert('Đánh giá đã được cập nhật thành công');

                // Đóng modal sau khi cập nhật đánh giá thành công (nếu sử dụng modal)
                $('#modal-update-review').modal('hide');

                // Có thể cập nhật giao diện người dùng mà không cần tải lại trang
            })
            .catch(function (error) {
                // Xử lý lỗi nếu cần
                console.error('API Error:', error);

                // Không hiển thị thông báo lỗi, chỉ cần đóng modal (nếu sử dụng modal)
                // $('#modal-update-review').modal('hide');
            });
    });
};