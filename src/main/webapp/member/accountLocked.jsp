<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tài khoản bị khóa</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="card shadow-sm">
                        <div class="card-body text-center">
                            <h1 class="h4 text-danger mb-3">Tài khoản tạm thời bị khóa</h1>
                            <p class="mb-3">Tài khoản <strong>${lockedUsername}</strong> đã bị khóa do đăng nhập sai mật khẩu quá 5 lần.</p>
                            <p class="text-muted mb-4">Vui lòng liên hệ quản trị viên để được cấp lại mật khẩu và kích hoạt tài khoản.</p>
                            <div class="d-grid gap-2">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/">Về trang chủ</a>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/auth/showLogin">Quay lại trang đăng nhập</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

