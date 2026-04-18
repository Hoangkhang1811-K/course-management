<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>

<%
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
%>

<c:set var="pageTitle" value="Không tìm thấy trang - Course Management"/>
<c:set var="activePage" value="error"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="bg-light">
    <section class="py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-8 col-xl-7">
                    <section class="bg-white border rounded-3 shadow-sm p-4 p-lg-5 text-center">
                        <div class="display-1 fw-bold text-primary mb-3">404</div>
                        <h1 class="h3 mb-3">Không tìm thấy trang</h1>
                        <p class="text-secondary mb-4">
                            Đường dẫn bạn mở không tồn tại hoặc nội dung đã được di chuyển.
                        </p>

                        <c:if test="${not empty requestScope['javax.servlet.error.request_uri']}">
                            <div class="alert alert-light border text-start mb-4" role="alert">
                                Đường dẫn lỗi:
                                <strong><c:out value="${requestScope['javax.servlet.error.request_uri']}"/></strong>
                            </div>
                        </c:if>

                        <div class="d-grid d-sm-flex justify-content-center gap-2">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">
                                Về trang chủ
                            </a>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">
                                Xem khóa học
                            </a>
                            <c:if test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.roleId == 1}">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">
                                    Trang quản trị
                                </a>
                            </c:if>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
