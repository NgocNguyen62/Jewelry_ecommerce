function addToCart(productId) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/cart/add?id=" + productId, true);
    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            alert("Đã thêm vào giỏ hàng");
        } else {
            alert("Không còn đủ sản phẩm")
        }
    };
    xhr.onerror = function () {
        alert("Đã xảy ra lỗi khi thực hiện yêu cầu");
    };
    xhr.send();
}