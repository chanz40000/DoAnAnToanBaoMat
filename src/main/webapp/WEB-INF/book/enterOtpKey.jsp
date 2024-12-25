<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Change Key</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" type="text/css" href="css/styleforlogin.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <link rel="icon" type="image/png" size="50" href="img/icons8-book-64.png">
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

    <!-- Font Icon -->
    <link rel="stylesheet" href="/fonts/material-icon/css/material-design-iconic-font.min.css">

    <!-- Main css -->
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
<section class="sign-in">
    <div class="container">
        <div class="signin-content">
            <div class="signin-image">
                <figure><img src="/img/signin-image.jpg" alt="sing up image"></figure>
            </div>

            <div class="signin-form">
                <p class="form-title" style="font-size: 35px; color: black; font-weight: bold">Cấp lại key</p>
                <div>
                    <c:if test="${not empty message}">
                        <div class="alert ${message.contains('thành công') ? 'alert-success' : 'alert-danger'}">
                                ${message}
                        </div>
                    </c:if>
                </div>

                <form class="reset-password-form" action="CreateKeyServletOtp" method="post">
                    <h4 class="mb-3">Nhập mã OTP</h4>
                    <p class="mb-3" style="color: #a71d2a; font-weight: bold;">
                        Nhập mã OTP chúng tôi đã gửi qua mail của bạn
                    </p>
                    <div class="form-group">
                        <label for="keyReport"><i class="fa fa-envelope"></i></label>
                        <input type="text" name="otpKey" id="keyReport"  placeholder="Mã OTP" required/>
                    </div>

                    <input type="datetime-local" id="time" name="time" required>

                    <input type="submit" name="" id="signin" class="form-submit" value="Cấp lại key"/>
                    <button onclick="event.preventDefault(); window.location.href='./Profile';" class="btn btn-success">
                        Quay lại
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>

<script>
    // Lấy dữ liệu từ Servlet gửi qua
    const message = "<%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>";
    const type = "<%= request.getAttribute("type") != null ? request.getAttribute("type") : "" %>";

    if (message) {
        const notification = document.getElementById('notification');
        notification.className = type; // success hoặc error
        notification.innerHTML = message;
        notification.style.display = 'block';

        // Ẩn thông báo sau 3 giây
        setTimeout(() => {
            notification.style.display = 'none';
        }, 4000);
    }
</script>
<script>
    // Giả sử bạn nhận được timestamp từ request như thế này
    const timestampFromRequest = "<%= request.getAttribute("timestamp") %>";

    window.onload = function() {
        if (timestampFromRequest) {
            // Dùng timestampFromRequest trực tiếp
            var startTimestamp = parseInt(timestampFromRequest); // Timestamp từ request
            var currentTimestamp = Date.now(); // Timestamp hiện tại

            // Thiết lập min và max cho ô chọn ngày giờ (sử dụng timestamp)
            var timestampPicker = document.getElementById('selectedDate');
            timestampPicker.min = new Date(startTimestamp).toISOString().slice(0, 16); // Convert to ISO and trim to datetime-local format
            timestampPicker.max = new Date(currentTimestamp).toISOString().slice(0, 16); // Convert to ISO and trim to datetime-local format
        }
    };

    // Kiểm tra khi người dùng chọn ngày giờ
    function validateDate() {
        // Lấy giá trị của ngày giờ người dùng chọn từ input
        const selectedDate = document.getElementById("selectedDate").value;

        if (!selectedDate) {
            return false;  // Nếu không chọn ngày thì không tiếp tục
        }

        const selectedTimestamp = new Date(selectedDate).getTime();  // Chuyển đổi thành timestamp

        // Kiểm tra nếu ngày giờ người dùng chọn không nằm trong khoảng
        if (selectedTimestamp < startTimestamp || selectedTimestamp > currentTimestamp) {
            // Hiển thị thông báo lỗi nếu ngày không hợp lệ
            document.getElementById('error-message').style.display = 'block';
            return false; // Ngừng form gửi đi
        }

        // Ẩn thông báo lỗi nếu ngày hợp lệ
        document.getElementById('error-message').style.display = 'none';
        return true; // Cho phép form gửi đi
    }
    // Gán sự kiện kiểm tra khi form được gửi
    document.getElementById("signin").addEventListener("click", function(event) {
        if (!validateDate()) {
            event.preventDefault();  // Ngừng gửi form nếu ngày không hợp lệ
        }
    });
    console.log("startTimestamp:", startTimestamp);
    console.log("currentTimestamp:", currentTimestamp);
    console.log("selectedTimestamp:", selectedTimestamp);

</script>

<script src="/js/jquery.min.login.js"></script>
</body>
</html>
