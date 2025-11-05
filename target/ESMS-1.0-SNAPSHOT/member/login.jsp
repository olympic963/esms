<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body class="bg-light">
    <jsp:include page="/member/navbar.jsp" />
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-12 col-md-6 col-lg-5">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h1 class="h4 mb-3 text-center">Đăng nhập</h1>
                        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
                        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
                        <form action="${pageContext.request.contextPath}/auth/login" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">Tên đăng nhập</label>
                                <input type="text" class="form-control" id="username" name="username" value="${username}" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Mật khẩu</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/auth/showRegister">Chưa có tài khoản? Đăng ký</a>
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