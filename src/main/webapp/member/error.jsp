<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Lỗi hệ thống</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <style>
            html, body { height: 100%; margin: 0; }
            body { background: #f8f9fa; font-size: 0.95rem; }
            .error-container { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 16px; }
            .error-card { background: #fff; border-radius: 10px; box-shadow: 0 12px 24px rgba(0,0,0,0.08); width: 100%; max-width: 720px; padding: 18px 20px; text-align: center; }
        </style>
    </head>
    <body>
        <div class="error-container">
            <div class="error-card">
                <div class="mb-2" style="font-size:48px;">⚠️</div>
                <h1 class="text-danger fs-4 mb-2">Đã xảy ra lỗi</h1>
                <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null && !errorMessage.isEmpty()) {
                %>
                    <div class="alert alert-warning mb-3">
                        <%= errorMessage %>
                    </div>
                <% } else { %>
                    <p class="text-muted mb-3">Hệ thống gặp sự cố khi xử lý yêu cầu của bạn.</p>
                <% } %>
                <div class="text-start border rounded p-3 bg-light">
                    <h6 class="mb-2">Chi tiết lỗi</h6>
                    <% 
                    Exception requestException = (Exception) request.getAttribute("exception");
                    Exception pageException = (Exception) pageContext.getException();                
                    Exception ex = requestException != null ? requestException : pageException;                   
                    if (ex != null) { 
                    %>
                        <p class="mb-1"><strong>Thông báo:</strong> <%= ex.getMessage() %></p>
                        <p class="mb-1"><strong>Loại:</strong> <%= ex.getClass().getSimpleName() %></p>
                        <% if (ex.getCause() != null) { %>
                            <p class="mb-1"><strong>Nguyên nhân:</strong> <%= ex.getCause().getMessage() %></p>
                        <% } %>
                    <% } else if (errorMessage == null || errorMessage.isEmpty()) { %>
                        <p class="mb-0">Không có thông tin lỗi cụ thể.</p>
                    <% } %>
                </div>                
                <div class="mt-3">
                    <a href="<%=request.getContextPath()%>/" class="btn btn-primary btn-sm">Trang chủ</a>
                    <a href="javascript:history.back()" class="btn btn-outline-secondary btn-sm">Quay lại</a>
                </div>
            </div>
        </div>
    </body>
</html>