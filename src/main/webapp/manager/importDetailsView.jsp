<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết mặt hàng nhập</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <jsp:include page="/member/navbar.jsp" />
        <div class="container py-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="h5 text-center mb-4">Chi tiết mặt hàng nhập</h1>
                    
                    <c:if test="${not empty invoice}">
                        <div class="mb-4">
                            <div class="row mb-2">
                                <div class="col-md-6">
                                    <strong>Nhà cung cấp:</strong> 
                                    <c:if test="${not empty invoice.supplier}">
                                        ${invoice.supplier.name}
                                    </c:if>
                                </div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-md-6">
                                    <strong>Nhân viên nhập:</strong> 
                                    <c:if test="${not empty invoice.warehouseEmp}">
                                        ${invoice.warehouseEmp.name}
                                    </c:if>
                                </div>
                                <div class="col-md-6 text-md-end">
                                    <strong>Mã nhân viên:</strong> 
                                    <c:if test="${not empty invoice.warehouseEmp}">
                                        ${invoice.warehouseEmp.id}
                                    </c:if>
                                </div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-md-6">
                                    <strong>Mã hóa đơn nhập:</strong> ${invoice.id}
                                </div>
                                <div class="col-md-6 text-md-end">
                                    <strong>Ngày nhập hàng:</strong> 
                                    <%
                                        com.mycompany.esms.model.ImportInvoice invObj = (com.mycompany.esms.model.ImportInvoice) pageContext.findAttribute("invoice");
                                        if (invObj != null && invObj.getImportDate() != null) {
                                            String d = invObj.getImportDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                            out.print(d);
                                        }
                                    %>
                                </div>
                            </div>
                        </div>
                        
                        <div class="table-responsive">
                            <table id="tblImportDetails" class="table table-bordered align-middle">
                                <thead class="table-light">
                                    <tr class="text-center">
                                        <th>STT</th>
                                        <th>Tên mặt hàng</th>
                                        <th class="text-end">Đơn giá</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="totalQty" value="0"/>
                                    <c:set var="totalAmount" value="0"/>
                                    <c:forEach var="detail" items="${listImportDetails}" varStatus="st">
                                        <tr>
                                            <td class="text-center">${st.index + 1}</td>
                                            <td>
                                                <c:if test="${not empty detail.importItem}">
                                                    ${detail.importItem.name}
                                                </c:if>
                                            </td>
                                            <td class="text-end">
                                                <fmt:setLocale value="de_DE"/>
                                                <fmt:formatNumber value="${detail.unitPrice}" type="number" groupingUsed="true" var="fmtPrice"/>
                                                ${fmtPrice} VND
                                            </td>
                                            <td class="text-center">${detail.quantity}</td>
                                            <td class="text-end">
                                                <c:set var="amount" value="${detail.quantity * detail.unitPrice}"/>
                                                <fmt:setLocale value="de_DE"/>
                                                <fmt:formatNumber value="${amount}" type="number" groupingUsed="true" var="fmtAmount"/>
                                                ${fmtAmount} VND
                                            </td>
                                        </tr>
                                        <c:set var="totalQty" value="${totalQty + detail.quantity}"/>
                                        <c:set var="totalAmount" value="${totalAmount + amount}"/>
                                    </c:forEach>
                                    <tr class="table-secondary">
                                        <td colspan="3" class="text-start"><strong>Tổng</strong></td>
                                        <td class="text-center"><strong>${totalQty}</strong></td>
                                        <td class="text-end">
                                            <fmt:setLocale value="de_DE"/>
                                            <fmt:formatNumber value="${totalAmount}" type="number" groupingUsed="true" var="fmtTotal"/>
                                            <strong>${fmtTotal} VND</strong>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                    
                    <c:if test="${empty invoice}">
                        <div class="alert alert-warning mb-0">Không tìm thấy thông tin hóa đơn nhập.</div>
                    </c:if>
                    
                    <div class="text-center mt-4">
                        <a id="back" class="btn btn-outline-secondary" href="javascript:history.back()">Quay lại</a>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
