<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body class="bg-light">
    <jsp:include page="/member/navbar.jsp" />
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-12 col-md-8 col-lg-6">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h1 class="h4 mb-3 text-center">Đăng ký tài khoản</h1>
                        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
                        <form action="${pageContext.request.contextPath}/auth/register" method="post">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label" for="name">Họ tên</label>
                                    <input class="form-control" id="name" name="name" value="${name}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="phoneNumber">Số điện thoại</label>
                                    <input class="form-control" id="phoneNumber" name="phoneNumber" value="${phoneNumber}">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="email">Email</label>
                                    <input type="email" class="form-control" id="email" name="email" value="${email}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="birthDate">Ngày sinh</label>
                                    <input type="date" class="form-control" id="birthDate" name="birthDate" value="${birthDate}">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="username">Tên đăng nhập</label>
                                    <input class="form-control" id="username" name="username" value="${username}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="password">Mật khẩu</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label" for="confirmPassword">Xác nhận mật khẩu</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 mt-3">Đăng ký</button>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/auth/showLogin">Đã có tài khoản? Đăng nhập</a>
                        </div>
                        <button type="button" class="btn btn-outline-secondary w-100 mt-3" onclick="location.href='${pageContext.request.contextPath}/'"><i class="fas fa-home me-1"></i>Trang chủ</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        (function(){
            var buttons = document.querySelectorAll('.toggle-password');
            Array.prototype.forEach.call(buttons, function(btn){
                btn.addEventListener('click', function(){
                    var targetId = btn.getAttribute('data-target');
                    var input = document.getElementById(targetId);
                    if (!input) return;
                    var isPwd = input.getAttribute('type') === 'password';
                    input.setAttribute('type', isPwd ? 'text' : 'password');
                    btn.textContent = isPwd ? 'Hide' : 'Show';
                });
            });
        })();
    </script>
</body>
</html> 