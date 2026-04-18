<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Quản lý bài học - Course Management"/>
<c:set var="pageCss" value="/assets/css/admin-course-list.css"/>
<c:set var="activePage" value="admin-lessons"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="admin-course-page">
    <section class="page-heading">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="section-kicker">Quản trị bài học</span>
                    <h1 class="mb-2">Bài học của khóa học</h1>
                    <p class="text-secondary mb-0">
                        <strong><c:out value="${course.title}"/></strong>
                    </p>
                </div>
                <div class="d-grid d-sm-flex gap-2">
                    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/courses">
                        Quay lại khóa học
                    </a>
                    <a class="btn btn-primary"
                       href="${pageContext.request.contextPath}/admin/lessons?action=create&courseId=${course.courseId}">
                        Thêm bài học
                    </a>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 my-4">
                <div>
                    <h2 class="section-title mb-1">Danh sách bài học</h2>
                    <p class="text-secondary mb-0">
                        Có <strong><c:out value="${totalLessons}"/></strong> bài học trong khóa học này.
                    </p>
                </div>
                <a class="btn btn-outline-primary"
                   href="${pageContext.request.contextPath}/course-detail?id=${course.courseId}">
                    Xem trang khóa học
                </a>
            </div>

            <c:choose>
                <c:when test="${empty lessons}">
                    <div class="empty-state">
                        <h2 class="h4 mb-2">Chưa có bài học nào</h2>
                        <p class="text-secondary mb-4">
                            Hãy thêm bài học đầu tiên cho khóa học này.
                        </p>
                        <a class="btn btn-primary"
                           href="${pageContext.request.contextPath}/admin/lessons?action=create&courseId=${course.courseId}">
                            Thêm bài học
                        </a>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="course-table-panel">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead>
                                <tr>
                                    <th class="ps-4">Thứ tự</th>
                                    <th>Bài học</th>
                                    <th>Loại nội dung</th>
                                    <th>Nội dung</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="lesson" items="${lessons}">
                                    <tr>
                                        <td class="ps-4 fw-semibold text-secondary">
                                            <c:out value="${lesson.lessonOrder}"/>
                                        </td>
                                        <td>
                                            <strong><c:out value="${lesson.title}"/></strong>
                                            <div class="text-secondary small">
                                                Mã bài học: #<c:out value="${lesson.lessonId}"/>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${lesson.contentType == 'VIDEO'}">
                                                    <span class="badge text-bg-info">Video</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-secondary">Text</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty lesson.contentValue}">
                                                    <span class="text-secondary d-inline-block" style="max-width: 420px;">
                                                        <c:choose>
                                                            <c:when test="${fn:length(lesson.contentValue) > 120}">
                                                                <c:out value="${fn:substring(lesson.contentValue, 0, 120)}"/>...
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:out value="${lesson.contentValue}"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa có nội dung</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${lesson.status == 1}">
                                                    <span class="status-badge is-active">Đang mở</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge is-inactive">Tạm khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="action-group">
                                                <a class="btn btn-sm btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/admin/lessons?action=edit&id=${lesson.lessonId}">
                                                    Sửa
                                                </a>
                                                <a class="btn btn-sm btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/admin/lessons?action=delete&id=${lesson.lessonId}"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa bài học này?');">
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
