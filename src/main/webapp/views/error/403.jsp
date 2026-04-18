<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>

<%
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
%>

<c:set var="pageTitle" value="Không có quyền truy cập - Course Management"/>
<c:set var="activePage" value="error"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="bg-light">
    <section class="py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-8 col-xl-7">
                    <section class="bg-white border rounded-3 shadow-sm p-4 p-lg-5 text-center">
                        <div class="display-1 fw-bold text-danger mb-3">403</div>
                        <h1 class="h3 mb-3">Bạn không có quyền truy cập trang này</h1>
                        <p class="text-secondary mb-4">
                            Tài khoản hiện tại không đủ quyền để mở khu vực này. Nếu bạn cần quyền quản trị, hãy đăng nhập bằng tài khoản admin.
                        </p>

                        <div class="d-grid d-sm-flex justify-content-center gap-2">
                            <c:choose>
                                <c:when test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.roleId == 1}">
                                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/dashboard">
                                        Về trang quản trị
                                    </a>
                                </c:when>
                                <c:when test="${not empty sessionScope.loggedInUser}">
                                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                                        Về trang chủ
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">
                                        Đăng nhập
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">
                                Xem khóa học
                            </a>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
