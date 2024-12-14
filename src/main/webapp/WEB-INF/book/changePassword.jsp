<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Đăng nhập</title>

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
                <h2 class="form-title">Thay đổi mật khẩu cho bạn</h2>
                <div style="text-align: center;color: red" class="red" id="baoLoi1">${errorBean.getError()} </div>
                <div style="text-align: center;color: red" class="red" id="baoLoi2">${errorBean.getError()} </div>

                <form action="ChangePassword" method="post" class="register-form" id="login-form">
                    <div class="form-group">
                        <label for="usernamelo"><i class="zmdi zmdi-account material-icons-name"></i></label>
                        <input type="text" name="username" id="usernamelo" placeholder="Nhập tên của bạn"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordlo"><i class="zmdi zmdi-lock"></i></label>
                        <input type="password" name="password" id="passwordlo" placeholder="Mật khẩu cũ"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordlo"><i class="zmdi zmdi-lock"></i></label>
                        <input type="password" name="newpassword" placeholder="Mật khẩu mới"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordlo"><i class="zmdi zmdi-lock"></i></label>
                        <input type="password" name="enterThePassword" placeholder=" Nhập lại mật khẩu mới"/>
                    </div>
                    <div class="form-group">
                        <input type="checkbox" name="remember-me" id="remember-me" class="agree-term" />
                        <label for="remember-me" class="label-agree-term"><span><span></span></span>Ghi nhớ tài khoản ?</label>

                    </div>
                    <div class="form-group form-button">
                        <input type="submit" name="signin" id="signin" class="form-submit" value="Đổi mật khẩu"/>
                    </div>

                </form>

            </div>
        </div>
    </div>
</section>

<%--</div>--%>

<!-- JS -->
<script src="/js/jquery.min.login.js"></script>
</body>
</html>