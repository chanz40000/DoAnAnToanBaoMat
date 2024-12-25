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
	/* Thông báo mặc định */
	#notification {
		display: none; /* Ẩn mặc định */
		position: fixed;
		top: 50%; /* Giữa màn hình theo chiều dọc */
		left: 50%; /* Giữa màn hình theo chiều ngang */
		transform: translate(-50%, -50%); /* Căn giữa hoàn toàn */
		padding: 20px 30px;
		font-size: 16px;
		border-radius: 8px;
		box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
		z-index: 1000;
		text-align: center;
		white-space: pre-line;
	}

	/* Kiểu lỗi */
	.error {
		background-color: #f8d7da;
		color: #721c24;
		border: 2px solid #f5c6cb;
	}

	/* Kiểu thành công */
	.success {
		background-color: #d4edda;
		color: #155724;
		border: 1px solid #c3e6cb;
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

										</form>

										<div>
											<label style="font-size: 22px" class="small mb-1">Upload Key</label>
											<button class="btn btn-warning" data-toggle="modal" data-target="#keyModal">Upload</button>

										</div>
									</div>
								</div>
							</div>
							<div class="modal fade" id="keyModal" tabindex="-1" aria-labelledby="keyModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title" id="keyModalLabel">Nhập khóa hoặc tải lên từ tệp</h5>
											<button type="button" class="close" data-dismiss="modal" aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
										<c:if test="${not empty message}">
											<div class="alert ${message.contains('thành công') ? 'alert-success' : 'alert-danger'}">
													${message}
											</div>
										</c:if>

										<div class="modal-body">

											<form id="keyForm2" method="post" action="UploadKeyServlet">
												<div class="form-group">
													<label for="password">Nhập pass của bạn:</label>
													<input type="password" name="password" id="password" placeholder="Mật khẩu"/>
												</div>
												<div class="form-group">
													<label for="inputKey">Nhập khóa của bạn:</label>
													<textarea class="form-control" name="keyContent" id="inputKey" rows="4" placeholder="Nhập khóa tại đây"></textarea>
												</div>
												<div class="form-group mt-3">
													<label for="uploadFile">Hoặc tải khóa từ tệp:</label>
													<input type="file" class="form-control-file"  id="uploadFile" accept=".txt" />
												</div>
												<button type="button" class="btn btn-primary mt-3" id="loadFileButton">Tải tệp</button>
												<button type="submit" class="btn btn-success mt-3">Xác nhận tạo khóa mới</button>
											</form>
										</div>
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
										<div id="notification"></div>
										<table>
											<thead>
											<tr>
												<th>Tên khóa</th>
												<th>Ngày tạo</th>
												<th>Ngày hết hạn</th>
												<th>Trạng thái</th>
												<th>Hành động</th>
											</tr>
											</thead>
											<tbody>
											<c:set var="id" value="${sessionScope.userC.userId}"/>
											<c:set var="counter" value="1" scope="page"/> <!-- Khởi tạo biến đếm -->
											<c:forEach var="key" items="${keyUserDAO.selectByUser(id)}">
												<tr>
													<td>Khóa ${counter}</td>
													<td><fmt:formatDate value="${key.getCreate_at()}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
													<td><fmt:formatDate value="${key.getExpired_at()}" pattern="dd-MM-yyyy HH:mm:ss" /></td>
													<td>${key.getStatus()}</td> <!-- Hiển thị trạng thái -->
													<td>
														<form id="keyForm" method="post" action="CreateKeyServlet">
															<button
																	class="btn ${key.getStatus() == 'ON' ? 'btn-warning' : 'btn-secondary'}"
																${key.getStatus() == 'ON' ? 'type="submit"' : ''}
																${key.getStatus() == 'ON' ? '' : 'disabled'}>
																Lộ khóa
															</button>

														</form>

													</td>
												</tr>
												<c:set var="counter" value="${counter + 1}" /> <!-- Tăng biến đếm -->
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
		}, 3000);
	}
</script>
<script>
	// Handle the file load event
	document.getElementById('loadFileButton').addEventListener('click', function() {
		const fileInput = document.getElementById('uploadFile');
		const file = fileInput.files[0];

		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const content = e.target.result;
				document.getElementById('inputKey').value = content; // Load content into the textarea
			};
			reader.readAsText(file);
		} else {
			alert('Vui lòng chọn một tệp!');
		}
	});

</script>

	<script src="js/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.nice-select.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script src="js/jquery.slicknav.js"></script>
	<script src="js/mixitup.min.js"></script>
	<script src="js/owl.carousel.min.js"></script>
	<script src="js/main.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">



</body>
</html>
