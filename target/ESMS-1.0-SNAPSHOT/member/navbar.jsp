<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser and sessionScope.currentUser.role eq 'manager'}">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/manager/managerHomeView.jsp">
                    Siêu thị PTIT
                </a>
            </c:when>
            <c:otherwise>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/customer/customerHomeView.jsp">
                    Siêu thị PTIT
                </a>
            </c:otherwise>
        </c:choose>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" 
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto align-items-center">
                <c:if test="${not empty sessionScope.currentUser}">
                    <li class="nav-item d-flex align-items-center">
                        <span class="navbar-text text-white me-3 mb-0">
                            Xin chào, <strong>${sessionScope.currentUser.name != null ? sessionScope.currentUser.name : sessionScope.username}</strong>
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/auth/logout">
                            Đăng xuất
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

