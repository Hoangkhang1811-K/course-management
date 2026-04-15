<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section class="py-4">
    <div class="container">
        <c:if test="${not empty requestScope.error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${requestScope.error}"/>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${sessionScope.error}"/>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <c:if test="${not empty requestScope.successMsg}">
            <div class="alert alert-success" role="alert">
                <c:out value="${requestScope.successMsg}"/>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success" role="alert">
                <c:out value="${sessionScope.successMsg}"/>
            </div>
            <c:remove var="successMsg" scope="session"/>
        </c:if>

        <c:if test="${not empty requestScope.message}">
            <div class="alert alert-info" role="alert">
                <c:out value="${requestScope.message}"/>
            </div>
        </c:if>
    </div>
</section>
