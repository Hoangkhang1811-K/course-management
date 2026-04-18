<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Quản lý đăng ký học - Course Management"/>
<c:set var="pageCss" value="/assets/css/admin-course-list.css"/>
<c:set var="activePage" value="admin-enrollments"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="admin-course-page">
    <section class="page-heading">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="section-kicker">Quản trị đăng ký học</span>
                    <h1 class="mb-2">Danh sách đăng ký học</h1>
                    <p class="text-secondary mb-0">
                        Theo dõi học viên đã đăng ký khóa học, lọc dữ liệu và cập nhật trạng thái học tập.
                    </p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/dashboard">
                    Về tổng quan
                </a>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="filter-panel mb-4">
                <h2 class="h4 mb-3">Thêm đăng ký học</h2>
                <form action="${pageContext.request.contextPath}/admin/enrollments" method="post"
                      class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="create">

                    <div class="col-12 col-lg-4">
                        <label for="createUserId" class="form-label">Học viên</label>
                        <select class="form-select" id="createUserId" name="userId" required>
                            <option value="">Chọn học viên</option>
                            <c:forEach var="student" items="${students}">
                                <option value="${student.userId}">
                                    <c:out value="${student.fullName}"/> - <c:out value="${student.email}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-lg-4">
                        <label for="createCourseId" class="form-label">Khóa học</label>
                        <select class="form-select" id="createCourseId" name="courseId" required>
                            <option value="">Chọn khóa học</option>
                            <c:forEach var="course" items="${courses}">
                                <option value="${course.courseId}">
                                    <c:out value="${course.title}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-md-6 col-lg-2">
                        <label for="createStatus" class="form-label">Trạng thái</label>
                        <select class="form-select" id="createStatus" name="status">
                            <c:forEach var="status" items="${statuses}">
                                <option value="${status}" ${status == 'ENROLLED' ? 'selected' : ''}>
                                    <c:choose>
                                        <c:when test="${status == 'ENROLLED'}">Đang học</c:when>
                                        <c:when test="${status == 'COMPLETED'}">Hoàn thành</c:when>
                                        <c:when test="${status == 'CANCELLED'}">Đã hủy</c:when>
                                        <c:otherwise><c:out value="${status}"/></c:otherwise>
                                    </c:choose>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-md-6 col-lg-2 d-grid">
                        <button type="submit" class="btn btn-primary">Thêm đăng ký</button>
                    </div>
                </form>
            </div>

            <div class="filter-panel mb-4">
                <h2 class="h4 mb-3">Lọc đăng ký học</h2>
                <form action="${pageContext.request.contextPath}/admin/enrollments" method="get"
                      class="row g-3 align-items-end">
                    <div class="col-12 col-lg-3">
                        <label for="keyword" class="form-label">Tìm kiếm</label>
                        <input type="text"
                               class="form-control"
                               id="keyword"
                               name="keyword"
                               value="${fn:escapeXml(keyword)}"
                               placeholder="Tên học viên hoặc khóa học">
                    </div>

                    <div class="col-12 col-lg-3">
                        <label for="courseId" class="form-label">Khóa học</label>
                        <select class="form-select" id="courseId" name="courseId">
                            <option value="">Tất cả khóa học</option>
                            <c:forEach var="course" items="${courses}">
                                <option value="${course.courseId}" ${selectedCourseId == course.courseId ? 'selected' : ''}>
                                    <c:out value="${course.title}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-lg-3">
                        <label for="userId" class="form-label">Học viên</label>
                        <select class="form-select" id="userId" name="userId">
                            <option value="">Tất cả học viên</option>
                            <c:forEach var="student" items="${students}">
                                <option value="${student.userId}" ${selectedUserId == student.userId ? 'selected' : ''}>
                                    <c:out value="${student.fullName}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-md-6 col-lg-1">
                        <label for="status" class="form-label">Trạng thái</label>
                        <select class="form-select" id="status" name="status">
                            <option value="">Tất cả</option>
                            <c:forEach var="status" items="${statuses}">
                                <option value="${status}" ${selectedStatus == status ? 'selected' : ''}>
                                    <c:choose>
                                        <c:when test="${status == 'ENROLLED'}">Đang học</c:when>
                                        <c:when test="${status == 'COMPLETED'}">Hoàn thành</c:when>
                                        <c:when test="${status == 'CANCELLED'}">Đã hủy</c:when>
                                        <c:otherwise><c:out value="${status}"/></c:otherwise>
                                    </c:choose>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-md-6 col-lg-2 d-grid d-sm-flex gap-2">
                        <button type="submit" class="btn btn-primary flex-fill">Lọc</button>
                        <a class="btn btn-outline-primary flex-fill"
                           href="${pageContext.request.contextPath}/admin/enrollments">
                            Xóa lọc
                        </a>
                    </div>
                </form>
            </div>

            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 my-4">
                <div>
                    <h2 class="section-title mb-1">Đăng ký học trong hệ thống</h2>
                    <p class="text-secondary mb-0">
                        Có <strong><c:out value="${totalEnrollments}"/></strong> đăng ký phù hợp.
                    </p>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty enrollments}">
                    <div class="empty-state">
                        <h2 class="h4 mb-2">Chưa có đăng ký học phù hợp</h2>
                        <p class="text-secondary mb-0">
                            Hãy thêm đăng ký mới hoặc thay đổi điều kiện lọc.
                        </p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="course-table-panel">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead>
                                <tr>
                                    <th class="ps-4">Mã</th>
                                    <th>Học viên</th>
                                    <th>Khóa học</th>
                                    <th>Ngày đăng ký</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="enrollment" items="${enrollments}">
                                    <tr>
                                        <td class="ps-4 fw-semibold text-secondary">
                                            #<c:out value="${enrollment.enrollmentId}"/>
                                        </td>

                                        <td>
                                            <strong><c:out value="${enrollment.userFullName}"/></strong>
                                            <div class="text-secondary small">
                                                Mã học viên: <c:out value="${enrollment.userId}"/>
                                            </div>
                                        </td>

                                        <td>
                                            <strong><c:out value="${enrollment.courseTitle}"/></strong>
                                            <div class="text-secondary small">
                                                Mã khóa học: <c:out value="${enrollment.courseId}"/>
                                            </div>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty enrollment.enrolledAt}">
                                                    <fmt:formatDate value="${enrollment.enrolledAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${enrollment.status == 'ENROLLED'}">
                                                    <span class="status-badge is-active">Đang học</span>
                                                </c:when>
                                                <c:when test="${enrollment.status == 'COMPLETED'}">
                                                    <span class="status-badge is-active">Hoàn thành</span>
                                                </c:when>
                                                <c:when test="${enrollment.status == 'CANCELLED'}">
                                                    <span class="status-badge is-inactive">Đã hủy</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge is-inactive">
                                                        <c:out value="${enrollment.status}"/>
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td class="text-end pe-4">
                                            <div class="d-inline-flex flex-wrap justify-content-end gap-2">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/enrollments"
                                                      class="d-inline-flex gap-2">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="id" value="${enrollment.enrollmentId}">
                                                    <select class="form-select form-select-sm" name="status" aria-label="Cập nhật trạng thái">
                                                        <c:forEach var="status" items="${statuses}">
                                                            <option value="${status}" ${enrollment.status == status ? 'selected' : ''}>
                                                                <c:choose>
                                                                    <c:when test="${status == 'ENROLLED'}">Đang học</c:when>
                                                                    <c:when test="${status == 'COMPLETED'}">Hoàn thành</c:when>
                                                                    <c:when test="${status == 'CANCELLED'}">Đã hủy</c:when>
                                                                    <c:otherwise><c:out value="${status}"/></c:otherwise>
                                                                </c:choose>
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                    <button type="submit" class="btn btn-sm btn-outline-primary">
                                                        Lưu
                                                    </button>
                                                </form>

                                                <a class="btn btn-sm btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/admin/enrollments?action=delete&id=${enrollment.enrollmentId}"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa đăng ký học này?');">
                                                    Xóa
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
