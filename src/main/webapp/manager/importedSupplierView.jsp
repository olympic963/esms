<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách lần nhập hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="h5 text-center mb-3">Danh sách lần nhập hàng</h1>
                    <fmt:parseDate value="${startDate}" pattern="yyyy-MM-dd" var="_sd"/>
                    <fmt:parseDate value="${endDate}" pattern="yyyy-MM-dd" var="_ed"/>
                    <div class="mb-3">
                        <div><strong>Nhà cung cấp:</strong> ${supplierName}</div>
                        <div class="row g-2 mt-2">
                            <div class="col-12 col-md-6"><strong>Ngày bắt đầu:</strong> <span><fmt:formatDate value="${_sd}" pattern="dd/MM/yyyy"/></span></div>
                            <div class="col-12 col-md-6"><strong>Ngày kết thúc:</strong> <span><fmt:formatDate value="${_ed}" pattern="dd/MM/yyyy"/></span></div>
                        </div>
                    </div>

                    <c:if test="${empty listImportInvoice}">
                        <div class="alert alert-info mb-0">Không có lần nhập hàng nào trong khoảng thời gian đã chọn.</div>
                    </c:if>
                    <c:if test="${not empty listImportInvoice}">
                        <div class="table-responsive">
                            <table id="tblImportInvoice" class="table table-bordered align-middle text-center">
                                <thead class="table-light">
                                    <tr>
                                        <th>STT</th>
                                        <th>Ngày nhập hàng</th>
                                        <th>Tổng số lượng hàng nhập</th>
                                        <th>Tổng giá trị hàng nhập</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="sumQty" value="0"/>
                                    <c:set var="sumValue" value="0"/>
                                    <c:forEach var="inv" items="${listImportInvoice}" varStatus="st">
                                        <tr>
                                            <td>${st.index + 1}</td>
                                            <td>
                                                <%
                                                    com.mycompany.esms.model.ImportInvoice invObj = (com.mycompany.esms.model.ImportInvoice) pageContext.findAttribute("inv");
                                                    if (invObj != null && invObj.getImportDate() != null) {
                                                        String d = invObj.getImportDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                        out.print(d);
                                                    }
                                                %>
                                            </td>
                                            <td>${inv.totalQuantity}</td>
                                            <td>
                                                <fmt:setLocale value="de_DE"/>
                                                <fmt:formatNumber value="${inv.totalValue}" type="number" groupingUsed="true" var="fmtValue"/>
                                                ${fmtValue} VND
                                            </td>
                                            <td><a id="showImportDetails" href="${pageContext.request.contextPath}/manager/importDetails?importInvoiceId=${inv.id}" class="btn btn-sm btn-outline-primary">Chọn</a></td>
                                        </tr>
                                        <c:set var="sumQty" value="${sumQty + inv.totalQuantity}"/>
                                        <c:set var="sumValue" value="${sumValue + inv.totalValue}"/>
                                    </c:forEach>
                                    <tr class="table-secondary">
                                        <td colspan="2" class="text-start"><strong>Tổng</strong></td>
                                        <td><strong>${sumQty}</strong></td>
                                        <td>
                                            <fmt:setLocale value="de_DE"/>
                                            <fmt:formatNumber value="${sumValue}" type="number" groupingUsed="true" var="fmtSum"/>
                                            <strong>${fmtSum} VND</strong>
                                        </td>
                                        <td></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                    <div class="text-center mt-3">
                        <a id="back" class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/manager/supplierStatistics?startDate=${startDate}&endDate=${endDate}">Quay lại</a>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
