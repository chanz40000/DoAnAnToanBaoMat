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
            font-size: 22px;
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
                    <h2>Thank You</h2>
                    <div class="breadcrumb__option">
                        <a href="Index">Home</a>
                        <span>Thank You</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Breadcrumb Section End -->
<section class="sss">
    <div class="d-flex justify-content-center" style="padding-top: 30px">
        <p style="font-size: 50px; font-weight: bold; color: black">Chi tiết đơn hàng</p>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-lg-5 left-tab">
                <div class="tabLef">
                    <div class="information">
                        <h4 style="color: #a71d2a; font-weight: bold">Thông tin người nhận</h4>
                        <hr>
                        <div class="name"><span>Tên:</span> ${orderBooking.nameConsignee}</div>
                        <div><span>Địa chỉ:</span> ${orderBooking.address}</div>
                        <div><span>Sđt:</span> ${orderBooking.phone}</div>
                        <div><span>email:</span> ${orderBooking.user.email}</div>
                        <div><span>Note:</span> ${orderBooking.note}</div>
                        <div><span>Thanh toán bằng:</span> ${orderBooking.payment.paymentName}</div>
                            <div style="color: #ef8640; font-weight: bold"><span>Trạng thái:</span> ${orderBooking.status.statusName} <i style="color: #ef8640" class="fa-solid fa-check"></i></div>
                    </div>
                    <hr>
                    <div class="totalOrder">
                        Tổng tiền: ${FormatCurrency.formatCurrency(orderBooking.totalPrice)}
                    </div>
                    <hr>
                </div>

            </div>
            <div class="col-lg-7 ">
                <div class="order">
                    <div class="content">
                        <h3>Thông tin sản phẩm</h3>
                        <c:forEach var="orderDetail" items="${orderDetails}">
                            <div class="fromOrder">

                                <div class="orderDetailProduct">
                                    <div class="img">
                                        <img  width="80px" height="100px" src="/image/${orderDetail.product.image}" alt="">
                                    </div>
                                    <div class="productDetail">
                                        <div class="productName">
                                            <h3>${orderDetail.product.product_name} <span style="font-size: 20px">x ${orderDetail.quantity}</span></h3>
                                        </div>
                                        <div class="category">
                                            <p style="font-size: 15px">Thể loại: ${orderDetail.product.category.categoryName}</p>
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
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Product Section Begin -->


<!-- Product Section End -->

<!-- Footer Section Begin -->
<footer class="footer spad">
    <jsp:include page="footer.jsp"/>
</footer>
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