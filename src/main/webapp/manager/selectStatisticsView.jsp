<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Xem thống kê</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-5">
            <div class="mx-auto card shadow-sm" style="max-width: 520px;">
                <div class="card-body text-center">
                    <h1 class="h4 mb-4 text-dark">Xem thống kê</h1>
                    <div class="d-grid gap-2">
                        <a class="btn btn-primary w-100" href="${pageContext.request.contextPath}/manager/supplierStatisticsView.jsp">
                           Thống kê nhà cung cấp
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
