<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="util.FormatCurrency"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
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
    <link rel="stylesheet" href="/css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="/css/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="/css/elegant-icons.css" type="text/css">
    <link rel="stylesheet" href="/css/nice-select.css" type="text/css">
    <link rel="stylesheet" href="/css/jquery-ui.min.css" type="text/css">
    <link rel="stylesheet" href="/css/owl.carousel.min.css" type="text/css">
    <link rel="stylesheet" href="/css/slicknav.min.css" type="text/css">
    <link rel="stylesheet" href="/css/style.css" type="text/css">
    <link rel="stylesheet" src="https://cdn.datatables.net/2.0.8/css/dataTables.dataTables.css">
    <style>
        .cart-btn, .primary-btn{
            border-radius: 15px;
            transition: transform 0.3s ease;
            background-color: #ffa97d;
        }

        .cart-btn:hover {
            background-color: #f86e21;
            color: white;
            transform: scale(1.1);
        }
        .primary-btn:hover{
            background-color: #f86e21;
            color: white;
            transform: scale(1.1);
        }
    </style>
</head>
<style>
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
</style>
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
                    <h2>Bình luận</h2>

                </div>
            </div>
        </div>
    </div>
</section>
<!-- Breadcrumb Section End -->

<!-- Shoping Cart Section Begin -->
<section class="shoping-cart spad">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="shoping__cart__table">
                    <c:choose>
                        <c:when test="${not empty comments}">
                            <table id="example">
                                <thead>
                                <tr>
                                    <th class="shoping__product">Tên sản phẩm</th>
                                    <th>Số sao</th>
                                    <th>Lời đánh giá</th>
                                    <th>Bình luận</th>
                                    <th>Thời gian bình luận</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="co" items="${comments}">
                                    <tr>
                                        <td class="shoping__cart__item">
                                            <img src="/image/${co.product.image}" alt="" width="50px" height="70px">

                                            <h5>${co.product.product_name}</h5>
                                        </td>
                                        <td >
                                            <fmt:formatNumber value="${co.ratingstar}" type="number" pattern="#"/>
                                        </td>
                                        <td>
                                                ${co.ratingtext}
                                        </td>
                                        <td >
                                                ${co.detailComment}
                                        </td>
                                        <td >
                                                ${co.getFormattedDateComment()}
                                        </td>


                                    </tr>

                                </c:forEach>



                                </tbody>
                            </table>
                        </c:when>

                    </c:choose>
                </div>
            </div>
        </div>

    </div>
</section>
<!-- Shoping Cart Section End -->

<!-- Footer Section Begin -->
<footer class="footer spad">
    <jsp:include page="footer.jsp"/>
</footer>
<!-- Footer Section End -->

<!-- Js Plugins -->
<script src="/js/jquery-3.3.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/jquery.nice-select.min.js"></script>
<script src="/js/jquery-ui.min.js"></script>
<script src="/js/jquery.slicknav.js"></script>
<script src="/js/mixitup.min.js"></script>
<script src="/js/owl.carousel.min.js"></script>
<script src="/js/main.js"></script>
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
</script>
// Set the initial button text and Font Awesome icon


// Enable Dark Reader when the page loads

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script src="https://cdn.datatables.net/2.0.8/js/dataTables.js"></script>
<script>
    $(document).ready(function() {
        var table = new DataTable('#example', {
            pagingType: 'simple_numbers'
        });
        table.rows().reverse();
    });

</script>
<script src="https://cdn.botpress.cloud/webchat/v2/inject.js"></script>
<script src="https://mediafiles.botpress.cloud/1d0997ec-87ba-4ea8-8a5c-c2fba00d5019/webchat/v2/config.js"></script>

</body>

</html>
