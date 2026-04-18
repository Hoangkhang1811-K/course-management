<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Tổng quan quản trị - Course Management"/>
<c:set var="pageCss" value="/assets/css/dashboard.css"/>
<c:set var="activePage" value="admin-dashboard"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="dashboard-page">
    <section class="dashboard-heading">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <%--                    <span class="section-kicker">Quản trị hệ thống</span>--%>
                    <h1 class="mb-2">Tổng quan quản lý khóa học</h1>
                    <p class="text-secondary mb-0">
                        Theo dõi khóa học, danh mục, học viên và các hoạt động mới nhất trong hệ thống.
                    </p>
                </div>
                <div class="d-grid d-sm-flex gap-2">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/courses?action=create">Thêm
                        khóa học</a>
                    <a class="btn btn-outline-primary"
                       href="${pageContext.request.contextPath}/admin/categories?action=create">Thêm danh mục</a>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="row g-4 mb-4">
                <div class="col-12 col-sm-6 col-xl-3">
                    <article class="stat-card">
                        <span class="stat-label">Tổng khóa học</span>
                        <%--                        <strong><c:out value="${totalCourses}"/></strong>--%>
                        <%--                        <small><c:out value="${activeCourses}"/> khóa học đang mở</small>--%>
                        <h2 class="display-6 fw-bold mb-1">${totalCourses}</h2>
                        <p class="mb-0 text-secondary">
                            Đang hoạt động: <strong>${activeCourses}</strong>
                        </p>
                    </article>
                </div>
                <div class="col-12 col-sm-6 col-xl-3">
                    <article class="stat-card">
                        <span class="stat-label">Danh mục</span>
                        <%--                        <strong><c:out value="${totalCategories}"/></strong>--%>
                        <%--                        <small><c:out value="${activeCategories}"/> danh mục hoạt động</small>--%>
                        <h2 class="display-6 fw-bold mb-1">${totalCategories}</h2>
                        <p class="mb-0 text-secondary">
                            Đang hoạt động: <strong>${activeCategories}</strong>
                        </p>
                    </article>
                </div>
                <div class="col-12 col-sm-6 col-xl-3">
                    <article class="stat-card">
                        <span class="stat-label">Người dùng</span>
                        <%--                        <strong><c:out value="${totalUsers}"/></strong>--%>
                        <%--                        <small>Tất cả tài khoản trong hệ thống</small>--%>
                        <%--                        <p class="text-secondary mb-2">Tổng người dùng</p>--%>
                        <h2 class="display-6 fw-bold mb-1">${totalUsers}</h2>
                        <p class="mb-0 text-secondary">
                            Học viên: <strong>${totalStudents}</strong>
                        </p>
                    </article>
                </div>
                <div class="col-12 col-sm-6 col-xl-3">
                    <article class="stat-card">
                        <span class="stat-label">Tổng học viên</span>
<%--                        <strong><c:out value="${totalStudents}"/></strong>--%>
<%--                        <small>Tài khoản có vai trò học viên</small>--%>
                        <h2 class="display-6 fw-bold mb-1">${totalUsers}</h2>
                        <p class="mb-0 text-secondary">
                            Học viên: <strong>${totalStudents}</strong>
                        </p>
                    </article>
                </div>
            </div>

            <div class="row g-4">
                <div class="col-12 col-xl-7">
                    <section class="dashboard-panel">
                        <div class="panel-header d-flex flex-column flex-md-row justify-content-between gap-2">
                            <div>
                                <h2 class="h4 mb-1">Khóa học mới nhất</h2>
                                <p class="text-secondary mb-0">Các khóa học vừa được cập nhật trong hệ thống.</p>
                            </div>
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/courses">Quản
                                lý khóa học</a>
                        </div>

                        <c:choose>
                            <c:when test="${empty recentCourses}">
                                <div class="empty-state">Chưa có khóa học nào.</div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table align-middle mb-0">
                                        <thead>
                                        <tr>
                                            <th>Tên khóa học</th>
                                            <th>Trình độ</th>
                                            <th>Giá</th>
                                            <th>Trạng thái</th>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="course" items="${recentCourses}">
                                            <tr>
                                                <td>
                                                    <strong><c:out value="${course.title}"/></strong>
                                                    <div class="text-secondary small">
                                                        Mã khóa học: <c:out value="${course.courseId}"/>
                                                    </div>
                                                </td>
                                                <td><c:out
                                                        value="${empty course.level ? 'Đang cập nhật' : course.level}"/></td>
                                                <td><fmt:formatNumber value="${course.price}" type="number"/> đ</td>
                                                <td>
                                                    <span class="status-badge ${course.status == 1 ? 'is-active' : 'is-inactive'}">
                                                        <c:choose>
                                                            <c:when test="${course.status == 1}">Đang mở</c:when>
                                                            <c:otherwise>Tạm khóa</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </td>
                                                <td class="text-end">
                                                    <a class="btn btn-sm btn-outline-primary"
                                                       href="${pageContext.request.contextPath}/admin/courses?action=edit&id=${course.courseId}">
                                                        Sửa
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </div>

                <div class="col-12 col-xl-5">
                    <section class="dashboard-panel">
                        <div class="panel-header d-flex flex-column flex-md-row justify-content-between gap-2">
                            <div>
                                <h2 class="h4 mb-1">Học viên mới</h2>
                                <p class="text-secondary mb-0">Danh sách tài khoản học viên gần đây.</p>
                            </div>
                            <a class="btn btn-outline-primary"
                               href="${pageContext.request.contextPath}/admin/enrollments">Đăng ký học</a>
                        </div>

                        <c:choose>
                            <c:when test="${empty recentStudents}">
                                <div class="empty-state">Chưa có học viên nào.</div>
                            </c:when>
                            <c:otherwise>
                                <div class="student-list">
                                    <c:forEach var="student" items="${recentStudents}">
                                        <article class="student-item">
                                            <div class="student-avatar">
                                                <c:choose>
                                                    <c:when test="${empty student.fullName}">?</c:when>
                                                    <c:otherwise><c:out
                                                            value="${fn:substring(student.fullName, 0, 1)}"/></c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="student-info">
                                                <strong class="student-name">
                                                    <c:out value="${student.fullName}"/>
                                                </strong>
                                                <div class="student-email">
                                                    <c:out value="${student.email}"/>
                                                </div>
                                            </div>
                                            <span class="status-badge ${student.status == 1 ? 'is-active' : 'is-inactive'}">
                                                <c:choose>
                                                    <c:when test="${student.status == 1}">Hoạt động</c:when>
                                                    <c:otherwise>Bị khóa</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </article>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>

                    <section class="quick-panel mt-4">
                        <h2 class="h5 mb-3">Tác vụ nhanh</h2>
                        <div class="quick-links">
                            <a href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục</a>
                            <a href="${pageContext.request.contextPath}/admin/courses">Quản lý khóa học</a>
                            <a href="${pageContext.request.contextPath}/admin/enrollments">Quản lý đăng ký học</a>
                            <a href="${pageContext.request.contextPath}/admin/users">Quản lý người dùng</a>
                            <a href="${pageContext.request.contextPath}/courses">Xem trang khóa học</a>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
