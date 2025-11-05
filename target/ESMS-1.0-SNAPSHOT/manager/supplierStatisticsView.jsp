<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thống kê nhà cung cấp</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="h5 text-center mb-3">Thống kê nhà cung cấp</h1>
                    <form id="dateForm" class="row g-3 align-items-center" method="get" action="${pageContext.request.contextPath}/manager/supplierStatistics">
                        <div class="col-12 col-md-5">
                            <div class="d-flex align-items-center gap-3">
                                <label class="form-label mb-0 text-nowrap me-2" >Ngày bắt đầu</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" value="${startDate}">
                            </div>
                        </div>
                        <div class="col-12 col-md-5">
                            <div class="d-flex align-items-center gap-3">
                                <label class="form-label mb-0 text-nowrap me-2" >Ngày kết thúc</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" value="${endDate}">
                            </div>
                        </div>
                        <div class="col-12 col-md-2 d-flex justify-content-end">
                            <button type="submit" class="btn btn-primary w-75">Xem</button>
                        </div>
                    </form>
                    
                    <c:if test="${not empty stats}">
                        <div class="table-responsive mt-3">
                            <table class="table table-bordered align-middle text-center">
                                <thead class="table-light">
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên nhà cung cấp</th>
                                        <th>Tổng số lượng hàng nhập</th>
                                        <th>Tổng giá trị hàng nhập</th>
                                        <th>Số lần nhập</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="sumQty" value="0" />
                                    <c:set var="sumValue" value="0" />
                                    <c:set var="sumCount" value="0" />
                                    <c:forEach var="row" items="${stats}" varStatus="st">
                                        <tr>
                                            <td>${st.index + 1}</td>
                                            <td class="text-start">${row.supplierName}</td>
                                            <td>${row.totalQuantity}</td>
                                            <td>
                                                <fmt:setLocale value="de_DE" />
                                                <fmt:formatNumber value="${row.totalValue}" type="number" groupingUsed="true" var="fmtValue"/>
                                                ${fmtValue} VND
                                            </td>
                                            <td>${row.importCount}</td>
                                            <td>
                                                <a class="btn btn-sm btn-outline-primary"
                                                   href="${pageContext.request.contextPath}/manager/importedSupplier?supplierId=${row.supplierId}&startDate=${startDate}&endDate=${endDate}">Chọn</a>
                                            </td>
                                        </tr>
                                        <c:set var="sumQty" value="${sumQty + row.totalQuantity}" />
                                        <c:set var="sumValue" value="${sumValue + row.totalValue}" />
                                        <c:set var="sumCount" value="${sumCount + row.importCount}" />
                                    </c:forEach>
                                    <tr class="table-secondary">
                                        <td colspan="2" class="text-start"><strong>Tổng</strong></td>
                                        <td><strong>${sumQty}</strong></td>
                                        <td>
                                            <fmt:setLocale value="de_DE" />
                                            <fmt:formatNumber value="${sumValue}" type="number" groupingUsed="true" var="fmtSum"/>
                                            <strong>${fmtSum} VND</strong>
                                        </td>
                                        <td><strong>${sumCount}</strong></td>
                                        <td></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="text-center mt-3">
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/manager/selectStatisticsView.jsp">Quay lại</a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById('dateForm').addEventListener('submit', function(e) {
                const startDate = document.getElementById('startDate').value;
                const endDate = document.getElementById('endDate').value;
                
                // Kiểm tra người dùng đã nhập đầy đủ ngày chưa
                if (!startDate || !endDate) {
                    e.preventDefault();
                    alert('Bạn chưa nhập ngày!');
                    return false;
                }
                
                // Kiểm tra ngày bắt đầu có lớn hơn ngày kết thúc không
                if (startDate > endDate) {
                    e.preventDefault();
                    alert('Ngày bắt đầu không được lớn hơn ngày kết thúc!');
                    return false;
                }
            });
        </script>
    </body>
</html>
