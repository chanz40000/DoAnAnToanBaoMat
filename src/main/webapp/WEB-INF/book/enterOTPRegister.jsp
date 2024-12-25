<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Nhập mã OTP</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" type="text/css" href="css/styleforlogin.css">
    <link rel="stylesheet" href="ahttps://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <link rel="icon" type="image/png" size="50" href="img/icons8-book-64.png">
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
<section class="signup">
    <div class="container">
        <div class="signup-content">
            <div class="signup-form">
                <h2 class="form-title">Nhập mã OTP</h2>
                <form action="VerifyOTPRegister" method="post" class="register-form" id="register-form">
                    <!-- Hidden inputs to store user details using JSTL -->
                    <input type="hidden" name="usernamere" value="${requestScope.usernamere}" />
                    <input type="hidden" name="namere" value="${requestScope.namere}" />
                    <input type="hidden" name="emailre" value="${requestScope.emailre}" />
                    <input type="hidden" name="passwordre" value="${requestScope.passwordre}" />

                    <div class="form-group">
                        <label for="otp"><i class="zmdi zmdi-lock"></i></label>
                        <input type="text" name="otpregister" id="otp" placeholder="Nhập mã OTP của bạn" required/>
                    </div>
                    <div class="form-group form-button">
                        <input type="submit" name="submitOtp" id="submitOtp" class="form-submit" value="Xác nhận OTP"/>
                    </div>
                </form>
                <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger">
                        <c:out value="${requestScope.error}" />
                    </div>
                </c:if>
            </div>
            <div class="signup-image">
                <figure><img src="/img/signup-image.jpg" alt="sign up image"></figure>
                <a href="Register" class="signup-image-link">Quay lại đăng ký</a>
            </div>
        </div>
    </div>
</section>
<script src="/js/jquery.min.login.js"></script>
</body>
</html>
