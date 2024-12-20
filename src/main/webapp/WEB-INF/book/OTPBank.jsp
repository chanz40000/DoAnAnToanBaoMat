<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="util.FormatCurrency" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VietQR.io - Demo API Tra Cứu STK Ngân Hàng</title>
    <link rel="stylesheet" href="css/styleforbankaccount.css">
    <link rel="preconnect" href="https://fonts.googleapis.com/">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400&amp;display=swap" rel="stylesheet">
</head>

<body>
<!-- Form to verify OTP -->
<form method="post" action="VerifyOTPBank">
    <p class="title">Nhập mã OTP</p>

    <!-- Input for OTP value -->
    <div class="item">
        <label>Nhập mã OTP</label>
        <div style="width:60%">
            <input type="text" id="otpnhap" class="input" name="otpnhap" placeholder="Nhập mã OTP" required>
        </div>
    </div>

    <!-- Hidden input for OTP value -->
    <input type="hidden" id="otpbank" name="otpbank" value="${requestScope.otpbank}">

    <!-- Display error message if there is any -->
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>

    <button type="submit">Xác nhận</button>
</form>

<script src="./js/api7b30.js"></script>
</body>
</html>
