<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="util.FormatCurrency"%>
<%@page isELIgnored="false" %>
<html lang="zxx">

<head>
    <meta charset="UTF-8">
    <meta name="description" content="Ogani Template">
    <meta name="keywords" content="Ogani, unica, creative, html">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Ogani | Template</title>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">

    <!-- Css Styles -->
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
    <link rel="stylesheet" href="css/nice-select.css" type="text/css">
    <link rel="stylesheet" href="css/jquery-ui.min.css" type="text/css">
    <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
    <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <style>
        .submit-button {
            border: none;
            background: none;
            padding: 0;
            font-size: 0; /* Ẩn văn bản trong nút */
            cursor: pointer;
        }

        .submit-button .fa {
            font-size: 24px; /* Kích thước biểu tượng */
            color: #000; /* Màu của biểu tượng */
        }
        button#toggle-dark-mode{
            background: none;
            color: inherit;
            border: none;
            position: absolute;
            font: inherit;
            cursor: pointer;
            outline: inherit;
            top: 12px;





        }
        .fa-regular.fa-sun{
            font-size: 20px;
            color: #f5f56d;

        }
        .fa-regular.fa-moon{
            font-size: 20px;

        }

        .sss{
            background-color: #f1f1f1;
        }
        .content{
            padding-top: 20px;

        }

        .fromOrder{
            margin-top: 20px;
            width: 100%;
            max-width: 1000px;
            /*background-color: white;*/
            /*box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.4);*/
            border-radius: 10px;
            /*padding: 10px;*/
            margin-left: 0;
        }
        @media (max-width: 900px) {
            .fromOrder {
                margin-left: 0; /* Bỏ margin-left khi màn hình nhỏ */
            }
        }
        .orderDetailProduct{
            display: flex;
            flex-direction: row;
        }
        .productDetail{
            display: flex;
            flex-direction: column;
            margin-left: 20px;
            width: 100%;
            justify-content: space-between;
        }
        .priceDetail{
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }
        .tabLef{
            padding-top: 20px;

        }
        .totalOrder{
            color: #a71d2a;
            font-size: 25px;
            font-weight: bold;
        }
        .information span{
            font-weight: bold;
            color: black;
        }
        .information div{
            font-size: 18px;
            padding-bottom: 10px;
        }
        .order{
            padding-top: 20px;
            padding-left: 80px;
        }
        .custom-list {
            list-style-type: none; /* Loại bỏ dấu chấm mặc định */
            padding-top: 10px;
        }

        .custom-list li {
            font-size: 20px; /* Kích thước văn bản của nội dung */
            margin-bottom: 10px; /* Khoảng cách giữa các phần tử */
        }

        .custom-list li::before {
            content: counter(item) ". "; /* Tự động thêm số thứ tự */
            counter-increment: item; /* Tăng biến đếm */
            font-size: 24px; /* Tăng kích thước của số */
            font-weight: bold; /* Làm đậm số */
            color: red; /* Màu đỏ cho số */
        }

        .custom-list {
            counter-reset: item; /* Đặt lại bộ đếm */
        }
        .linkdown {
            color: #a71d2a; /* Màu chữ mặc định */
            text-decoration: none; /* Bỏ gạch chân nếu muốn */
        }

        .linkdown:hover {
            color: #a71d2a; /* Màu chữ khi hover */
            text-decoration: underline; /* Gạch chân khi hover nếu muốn */
        }

        .linkdown:active,
        .linkdown:focus,
        .linkdown:visited {
            color: #a71d2a; /* Giữ nguyên màu chữ ở các trạng thái khác */
            text-decoration: none; /* Bỏ gạch chân nếu có */
        }

    </style>
</head>

<body>
<!-- Page Preloder -->
<div id="preloder">
    <div class="loader"></div>
</div>


<jsp:include page="navbar.jsp"/>


<!-- Breadcrumb Section Begin -->
<section class="breadcrumb-section set-bg" data-setbg="img/hinhnen.png">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <div class="breadcrumb__text">
                    <h2>Xác nhận chữ ký</h2>
                    <div class="breadcrumb__option">
                        <a href="Index">Home</a>
                        <span>Xác nhận chữ ký</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<section class="sss">
    <div class="d-flex justify-content-center" style="padding-top: 30px">
        <p style="font-size: 50px; font-weight: bold; color: black">Xác nhận chữ ký</p>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-lg-5 left-tab">
                <div class="tabLef">
                    <div class="information">
                        <h4 style="color: #a71d2a; font-weight: bold">Thông tin người nhận</h4>
                        <hr>
                        <div class="name"><span>Tên:</span> ${order.nameConsignee}</div>
                        <div><span>Địa chỉ:</span> ${order.address}</div>
                        <div><span>Sđt:</span> ${order.phone}</div>
                        <div><span>email:</span> ${order.user.email}</div>
                        <div><span>Note:</span> ${order.note}</div>
                        <div><span>Thanh toán bằng:</span> ${order.payment.paymentName}</div>
                        <c:if test="${order.status.statusId == 1}">
                            <div style="color: #ef8640; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #ef8640" class="fa-solid fa-check"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 2}">
                            <div style="color: #b98d3c; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #b98d3c" class="fa-solid fa-boxes-packing"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 3 || order.status.statusId == 7}">
                            <div style="color: #1c67d7; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #1c67d7" class="fa-solid fa-truck-fast"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 4}">
                            <div style="color: #38bc10; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #38bc10" class="fa-solid fa-truck-ramp-box"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 5}">
                            <div style="color: #c31625; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #c31625" class="fa-solid fa-ban"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 6}">
                            <div style="color: #ff0018; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName}</div>
                        </c:if>
                        <c:if test="${order.status.statusId == 8}">
                            <div style="color: #c31625; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #c31625;" class="fa-solid fa-arrow-right-arrow-left"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 10}">
                            <div style="color: #c264ff; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #c264ff" class="fa-solid fa-box-open"></i></div>
                        </c:if>
                        <c:if test="${order.status.statusId == 11}">
                            <div style="color: #c264ff; font-weight: bold"><span>Trạng thái:</span> ${order.status.statusName} <i style="color: #f6b422" class="fa-solid fa-clock"></i></div>
                        </c:if>
                    </div>
                    <hr>
                    <div class="content">
                        <h3>Thông tin sản phẩm của đơn hàng ${order.orderId}</h3>
                        <c:forEach var="orderDetail" items="${orderDetailList}">
                            <div class="fromOrder">

                                <div class="orderDetailProduct">
                                    <div class="img">
                                        <img  width="100px" height="130px" src="/image/${orderDetail.product.image}" alt="">
                                    </div>
                                    <div class="productDetail">
                                        <div class="productName">
                                            <h3>${orderDetail.product.product_name} <span style="font-size: 20px">x ${orderDetail.quantity}</span></h3>
                                        </div>
                                        <div class="category">
                                            <p style="font-size: 15px">Thể loại: ${orderDetail.product.category.categoryName}<br>ID sản phẩm: ${orderDetail.product.productId}</p>
                                        </div>

                                        <div class="priceDetail" style="display: flex" >
                                            <div class="productPrice">
                                                <h4>${FormatCurrency.formatCurrency(orderDetail.product.price)}</h4>
                                            </div>
                                            <div class="productPriceTotal">
                                                <c:set var="total" value="${orderDetail.quantity * orderDetail.product.price}"/>
                                                <h4 style="color: #ce0f21; font-weight: bold">Tổng: ${FormatCurrency.formatCurrency(total)}</h4>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <hr>
                            </div>
                        </c:forEach>
                        <div class="totalOrder">
                            Tổng tiền: ${FormatCurrency.formatCurrency(order.totalPrice)}
                        </div>
                    </div>
                </div>
                <br><br>

            </div>
            <div class="col-lg-7 ">
                <div class="order">
                    <h4 style="color: #a71d2a; font-weight: bold">Hướng dẫn lấy chữ ký</h4>
                    <ul class="custom-list">
                        <li>Tải tool của chúng tôi để tạo ra chữ ký của bạn. <a class="linkdown" href="DownLoadTool">Tại đây</a></li>
                        <li>Kiểm tra email của bạn! Lấy mã chúng tôi gửi và tiến hành ký tên</li>
                        <li>Lấy chữ ký và nhập vào ô dưới đây (Hướng dẫn xài tool)</li>
                    </ul>
                    <div class="signature">
                        <textarea id="signatureInput" name="signature" rows="4" cols="50" placeholder="Nhập chữ ký của bạn ở đây..."></textarea>
                        <br>
                        <button id="verifyButton" type="button" class="btn btn-primary" style="width: 100%; font-size: 18px; font-weight: bold; background-color: #a71d2a; border: none;">
                            Xác nhận đơn hàng
                        </button>
                        <div id="verifyResult" style="margin-top: 10px; font-size: 16px; color: red;"></div>

                    </div>

                </div>
            </div>
        </div>
    </div>
</section>
<%--                    <div class="information">--%>
<%--                        <h4 style="color: #a71d2a; font-weight: bold">Nhập chữ ký</h4>--%>
<%--                        <hr>--%>


<%--                    </div>--%>
<%--                    <div class="confirmation">--%>

<%--                    </div>--%>

<!-- Product Section End -->

<!-- Footer Section Begin -->
<jsp:include page="footer.jsp"/>
<!-- Footer Section End -->

<!-- Js Plugins -->
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.nice-select.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery.slicknav.js"></script>
<script src="js/mixitup.min.js"></script>
<script src="js/owl.carousel.min.js"></script>
<script src="js/main.js"></script>
<script src="https://cdn.jsdelivr.net/npm/darkreader@4.9.80/darkreader.min.js"></script>
<script>
    document.getElementById("verifyButton").addEventListener("click", function () {
        const signature = document.getElementById("signatureInput").value; // Lấy chữ ký từ ô nhập
        const resultDiv = document.getElementById("verifyResult");

        if (!signature) {
            resultDiv.textContent = "Vui lòng nhập chữ ký!";
            return;
        }

        // Gửi yêu cầu đến servlet VerifySignature
        fetch("/VerifySignature?signature=" + encodeURIComponent(signature), {
            method: "GET",
        })
            .then(response => response.text())
            .then(data => {
                // Hiển thị kết quả
                if (data.includes("hợp lệ")) {
                    alert("Chữ ký hợp lệ! Đơn hàng đã được xác nhận."); // Hiển thị hộp thông báo
                    resultDiv.style.color = "green";
                    resultDiv.textContent = data; // Hiển thị thông báo thành công
                } else {
                    resultDiv.style.color = "red";
                    resultDiv.textContent = data; // Hiển thị lỗi
                }
            })
            .catch(error => {
                resultDiv.textContent = "Đã xảy ra lỗi: " + error.message; // Hiển thị lỗi kết nối
            });
    });
</script>

<script>
    const toggleDarkModeButton = document.getElementById("toggle-dark-mode");
    const icondarklight = document.getElementById('icontype');

    // Initially disable Dark Reader
    DarkReader.disable();

    toggleDarkModeButton.addEventListener("click", () => {
        if (DarkReader.isEnabled()) {
            DarkReader.disable();
            icondarklight.classList.replace("fa-sun", "fa-moon");
        } else {
            DarkReader.enable({
                brightness: 100,
                contrast: 90,
                sepia: 10
            });
            icondarklight.classList.replace("fa-moon", "fa-sun");
        }
    });

    // Set the initial button text and Font Awesome icon


    // Enable Dark Reader when the page loads

</script>


</body>

</html>