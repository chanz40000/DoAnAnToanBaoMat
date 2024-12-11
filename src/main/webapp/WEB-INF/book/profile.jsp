<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page isELIgnored="false" %>
<%@ page import="util.FormatCurrency"%>

<!DOCTYPE html>
<html>
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
<style type="text/css">
	.avatar-container {
		position: relative;
		display: inline-block;
	}

	#editButton {
		position: absolute;
		top: 0;
		right: 0;
		display: none;
		cursor: pointer;
	}

	.avatar-container:hover #editButton {
		display: block;
	}
	.img-account-profile {
		height: 10rem;
	}
	.rounded-circle {
		border-radius: 50% !important;
	}
	.card {
		box-shadow: 0 0.15rem 1.75rem 0 rgb(33 40 50 / 15%);
	}
	.card .card-header {
		font-weight: 500;
	}
	.card-header:first-child {
		border-radius: 0.35rem 0.35rem 0 0;
	}
	.card-header {
		padding: 1rem 1.35rem;
		margin-bottom: 0;
		background-color: rgba(33, 40, 50, 0.03);
		border-bottom: 1px solid rgba(33, 40, 50, 0.125);
	}
	.form-control, .dataTable-input {
		display: block;
		width: 100%;
		padding: 0.875rem 1.125rem;
		font-size: 0.875rem;
		font-weight: 400;
		line-height: 1;
		color: #69707a;
		background-color: #fff;
		background-clip: padding-box;
		border: 1px solid #c5ccd6;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		border-radius: 0.35rem;
		transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
	}

	.nav-borders .nav-link.active {
		color: #0061f2;
		border-bottom-color: #0061f2;
	}
	.nav-borders .nav-link {
		color: #69707a;
		border-bottom-width: 0.125rem;
		border-bottom-style: solid;
		border-bottom-color: transparent;
		padding-top: 0.5rem;
		padding-bottom: 0.5rem;
		padding-left: 0;
		padding-right: 0;
		margin-left: 1rem;
		margin-right: 1rem;
	}
	label{
		font-size: 30px;
	}
	table {
		width: 100%;
		border-collapse: collapse;
	}
	th, td {
		padding: 10px;
		text-align: left;
		border: 1px solid #ddd;
	}
</style>

</head>
<body>
<!-- Page Preloder -->
<div id="preloder">
	<div class="loader"></div>
</div>

<jsp:include page="navbar.jsp"/>
<jsp:useBean id="keyUserDAO" class="database.KeyUserDAO"/>
<div class="full_container">
	<div class="inner_container">
		<div id="content">

			<div class="midde_cont">
				<div class="container-fluid">
					<div class="row column_title">
						<div class="col-md-12">
							<div class="page_title">
								<h2 style="text-align: center; font-size: 70px">Hồ Sơ</h2>
							</div>
						</div>
					</div>
					<div class="container-xl px-4 mt-4">
						<hr class="mt-0 mb-4">
						<div class="row">
							<div class="col-xl-4">
								<div class="card mb-4 mb-xl-0">
									<div style="font-size: 30px" class="card-header">Thông tin cá nhân</div>
									<div class="card-body text-center">
										<img class="img-account-profile rounded-circle mb-2" src="${userC.avatar}" alt="">
										<div class="small font-italic text-muted mb-4">JPG hoặc PNG không quá 5 MB</div>
										<form action="ChangeAvartar" method="post" enctype="multipart/form-data">
											<input type="file" name="avatar" accept="image/*" required>
											<button class="btn btn-primary" type="submit">Chọn ảnh</button>
										</form>
									</div>
								</div>
							</div>
							<div class="col-xl-8">
								<div class="card mb-4">
									<div style="font-size: 30px" class="card-header">Thông tin chi tiết</div>
									<div class="card-body">
										<form>
											<div class="mb-3">
												<label style="font-size: 22px" class="small mb-1">Tên tài khoản: ${userC.username}</label>
											</div>
											<div class="row gx-3 mb-3">
												<div class="col-md-6">
													<label style="font-size: 22px" class="small mb-1">Tên: ${userC.name}</label>
												</div>
												<div class="col-md-6">
													<label style="font-size: 22px" class="small mb-1">Giới tính: ${userC.sexual}</label>
												</div>
											</div>

											<div class="mb-3">
												<label style="font-size: 22px" class="small mb-1">Email: ${userC.email}</label>
											</div>
											<div class="row gx-3 mb-3">
												<div class="col-md-6">
													<label style="font-size: 22px" class="small mb-1">Số điện thoại: ${userC.phone}</label>
												</div>
												<div class="col-md-6">
													<label style="font-size: 22px" class="small mb-1">Sinh nhật: ${userC.birthday}</label>
												</div>
											</div>

											<!-- Thêm thông tin khóa và chữ ký điện tử -->
											<div class="mb-3">
												<label style="font-size: 22px" class="small mb-1">Khóa riêng tư:</label>
												<input type="file" class="form-control" name="privateKey" accept=".txt" required />
											</div>
											<div class="mb-3">
												<label style="font-size: 22px" class="small mb-1">Khóa công khai:</label>
												<input type="file" class="form-control" name="publicKey" accept=".txt" required />
											</div>
											<div class="mb-3">
												<label style="font-size: 22px" class="small mb-1">Chữ ký điện tử:</label>
												<input type="file" class="form-control" name="digitalSignature" accept=".txt" />
											</div>

											<div class="mb-3">
												<button class="btn btn-primary" type="submit">Cập nhật thông tin chữ ký</button>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>

						<!-- Liệt kê các khóa xác thực -->
						<div class="row mt-4">
							<div class="col-xl-12">
								<div class="card">
									<div class="card-header" style="font-size: 30px">
										Dưới đây là các khóa xác thực của bạn
									</div>
									<div class="card-body">
										<p>Bạn có thể tạo nhiều khóa nhưng chỉ chọn 1 khóa để sử dụng, hãy xóa những khóa bạn không sử dụng.</p>
										<table>
											<thead>
											<tr>
												<th>Tên khóa</th>
												<th>Ngày hết hạn</th>
												<th>Hành động</th>
											</tr>
											</thead>
											<tbody>
											<c:set var="id" value="${sessionScope.userC.userId}"/>
											<c:set var="counter" value="1" scope="page" /> <!-- Khởi tạo biến đếm -->
                                            <c:forEach var="key" items="${keyUserDAO.selectByUser(id)}">
											<tr>
												<td>Khóa ${counter}</td>
												<td><fmt:formatDate value="${key.getCreate_at()}" pattern="dd-MM-yyyy" /></td>
												<td>
													<button class="btn btn-warning">Lộ khóa</button>
													<button class="btn btn-danger">Xóa khóa</button>
													<button class="btn btn-success">Sử dụng</button>
												</td>
											</tr>
												<c:set var="counter" value="${counter + 1}" /> <!-- Tăng biến đếm -->
											<!-- Thêm nhiều khóa khác -->
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

	<footer class="footer spad">
		<jsp:include page="footer.jsp" />
	</footer>

	<!-- loader -->
	<div id="ftco-loader" class="show fullscreen">
		<svg class="circular" width="48px" height="48px">
			<circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee" />
			<circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00" /></svg>
	</div>
</div>

	<script src="js/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.nice-select.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script src="js/jquery.slicknav.js"></script>
	<script src="js/mixitup.min.js"></script>
	<script src="js/owl.carousel.min.js"></script>
	<script src="js/main.js"></script>


</body>
</html>
