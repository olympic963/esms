<%-- 
    Document   : searchItemsView
    Created on : Oct 31, 2025, 3:11:18 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang tìm kiếm mặt hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="h4 mb-3">Trang tìm kiếm mặt hàng</h1>
                    <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/ItemServlet">
                        <input type="hidden" name="action" value="search" />
                        <div class="col-12 col-md-8">
                            <input type="text" class="form-control" name="keyword" placeholder="Nhập tên mặt hàng" value="${keyword}">
                        </div>
                        <div class="col-12 col-md-4 d-grid">
                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                        </div>
                    </form>

                    <c:if test="${searched}">
                        <c:choose>
                            <c:when test="${empty items}">
                                <div class="alert alert-info mb-0">Không tìm thấy kết quả phù hợp.</div>
                            </c:when>
                            <c:otherwise>
                                <fmt:setLocale value="de_DE"/>
                                <div class="table-responsive">
                                    <table class="table table-striped align-middle">
                                        <thead>
                                            <tr>
                                                <th style="width:80px;">STT</th>
                                                <th>Tên sản phẩm</th>
                                                <th style="width:160px;">Giá</th>
                                                <th style="width:140px;">Xem chi tiết</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="it" items="${items}" varStatus="status">
                                                <tr>
                                                    <td>${offset + status.index + 1}</td>
                                                    <td>${it.name}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${it.salePrice}" type="number" groupingUsed="true" var="formattedPrice"/>
                                                        <span class="badge bg-success">${formattedPrice} VND</span>
                                                    </td>
                                                    <td>
                                                        <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/ItemServlet?action=detail&id=${it.id}">Chọn</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <c:if test="${totalPages > 1}">
                                    <nav aria-label="Phân trang">
                                        <ul class="pagination justify-content-center">
                                            <c:if test="${currentPage > 1}">
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/ItemServlet?action=search&keyword=${keyword}&page=1">Trang đầu</a>
                                                </li>
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/ItemServlet?action=search&keyword=${keyword}&page=${currentPage-1}">Lùi</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${ellipsisFirst}">
                                                <li class="page-item disabled"><a class="page-link">...</a></li>
                                            </c:if>
                                            <c:forEach var="p" items="${pageRange}">
                                                <li class="page-item <c:if test='${p == currentPage}'>active</c:if>">
                                                    <a class="page-link <c:if test='${p == currentPage}'>bg-primary text-white fw-bold</c:if>" href="${pageContext.request.contextPath}/ItemServlet?action=search&keyword=${keyword}&page=${p}">${p}</a>
                                                </li>
                                            </c:forEach>
                                            <c:if test="${ellipsisEnd}">
                                                <li class="page-item disabled"><a class="page-link">...</a></li>
                                            </c:if>
                                            <c:if test="${currentPage < totalPages}">
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/ItemServlet?action=search&keyword=${keyword}&page=${currentPage+1}">Tiến</a>
                                                </li>
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/ItemServlet?action=search&keyword=${keyword}&page=${totalPages}">Trang cuối</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </nav>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
