<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết mặt hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="h5 text-center mb-3">Chi tiết mặt hàng</h1>
                    <c:choose>
                        <c:when test="${empty item}">
                            <div class="alert alert-warning mb-0">Không tìm thấy thông tin sản phẩm.</div>
                        </c:when>
                        <c:otherwise>
                            <fmt:setLocale value="de_DE"/>
                            <div>
                                <div class="mb-3">
                                    <strong>Tên sản phẩm:</strong> ${item.name}
                                </div>
                                <div class="mb-3">
                                    <strong>Giá:</strong>
                                    <fmt:formatNumber value="${item.salePrice}" type="number" groupingUsed="true" var="formattedPrice"/>
                                    ${formattedPrice} VND
                                </div>
                                <div class="mb-3">
                                    <strong>Mô tả:</strong>
                                    <c:choose>
                                        <c:when test="${not empty item.description}">
                                            <%
                                                com.mycompany.esms.model.Item it = (com.mycompany.esms.model.Item) request.getAttribute("item");
                                                if (it != null && it.getDescription() != null) {
                                                    String desc = it.getDescription();
                                                    String[] lines = desc.split("[\r\n]+");
                                                    for (String line : lines) {
                                                        if (!line.trim().isEmpty()) {
                                            %>
                                                            <div class="ms-3 mt-1"><%= line %></div>
                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="ms-3 mt-1 text-muted">Không có mô tả</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="mb-3">
                                    <strong>Thời gian bảo hành:</strong> ${item.warranty} tháng
                                </div>
                                <div class="mb-3">
                                    <strong>Còn lại:</strong> ${item.stockQuantity} sản phẩm
                                </div>
                                <div class="mb-3 d-flex align-items-center gap-2">
                                    <strong>Số lượng:</strong>
                                    <input type="number" class="form-control" id="quantity" name="quantity" value="1" min="1" max="${item.stockQuantity}" style="width: 100px;">
                                    <button type="button" class="btn btn-primary" id="buy" onclick="addToCart()">Thêm vào giỏ hàng</button>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function addToCart() {
                alert('Tính năng này chưa hoàn thành');
            }
        </script>
    </body>
</html>
