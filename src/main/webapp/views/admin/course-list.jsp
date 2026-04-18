<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Quản lý khóa học - Course Management"/>
<c:set var="pageCss" value="/assets/css/admin-course-list.css"/>
<c:set var="activePage" value="admin-courses"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="admin-course-page">
    <section class="page-heading">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="section-kicker">Quản trị khóa học</span>
                    <h1 class="mb-2">Danh sách khóa học</h1>
                    <p class="text-secondary mb-0">
                        Theo dõi, tìm kiếm, lọc và quản lý các khóa học đang có trong hệ thống.
                    </p>
                </div>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/courses?action=create">
                    Thêm khóa học
                </a>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="filter-panel">
                <form action="${pageContext.request.contextPath}/admin/courses" method="get"
                      class="row g-3 align-items-end">
                    <div class="col-12 col-lg-4">
                        <label for="keyword" class="form-label">Tìm kiếm khóa học</label>
                        <input type="text"
                               class="form-control"
                               id="keyword"
                               name="keyword"
                               value="${fn:escapeXml(keyword)}"
                               placeholder="Nhập tên khóa học">
                    </div>

                    <div class="col-12 col-md-6 col-lg-3">
                        <label for="categoryId" class="form-label">Danh mục</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">Tất cả danh mục</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.categoryId}" ${selectedCategoryId == category.categoryId ? 'selected' : ''}>
                                    <c:out value="${category.name}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12 col-md-6 col-lg-2">
                        <label for="status" class="form-label">Trạng thái</label>
                        <select class="form-select" id="status" name="status">
                            <option value="">Tất cả</option>
                            <option value="1" ${selectedStatus == 1 ? 'selected' : ''}>Đang mở</option>
                            <option value="0" ${selectedStatus == 0 ? 'selected' : ''}>Tạm khóa</option>
                        </select>
                    </div>

                    <div class="col-12 col-lg-3 d-grid d-sm-flex gap-2">
                        <button type="submit" class="btn btn-primary flex-fill">Lọc</button>
                        <a class="btn btn-outline-primary flex-fill" href="${pageContext.request.contextPath}/admin/courses">
                            Xóa lọc
                        </a>
                    </div>
                </form>
            </div>

            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 my-4">
                <div>
                    <h2 class="section-title mb-1">Khóa học trong hệ thống</h2>
                    <p class="text-secondary mb-0">
                        Có <strong><c:out value="${empty courses ? 0 : fn:length(courses)}"/></strong> khóa học phù hợp.
                    </p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/dashboard">
                    Về tổng quan
                </a>
            </div>

            <c:choose>
                <c:when test="${empty courses}">
                    <div class="empty-state">
                        <h2 class="h4 mb-2">Chưa có khóa học phù hợp</h2>
                        <p class="text-secondary mb-4">
                            Hãy thêm khóa học mới hoặc thay đổi điều kiện lọc để xem dữ liệu khác.
                        </p>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/courses?action=create">
                            Thêm khóa học
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="course-table-panel">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead>
                                <tr>
                                    <th>Khóa học</th>
                                    <th>Danh mục</th>
                                    <th>Trình độ</th>
                                    <th>Giá</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="course" items="${courses}">
                                    <tr>
                                        <td>
                                            <div class="course-info">
                                                <c:choose>
                                                    <c:when test="${not empty course.thumbnailUrl}">
                                                        <c:choose>
                                                            <c:when test="${fn:startsWith(course.thumbnailUrl, 'http://') || fn:startsWith(course.thumbnailUrl, 'https://') || fn:startsWith(course.thumbnailUrl, 'data:')}">
                                                                <c:set var="courseImageSrc" value="${course.thumbnailUrl}"/>
                                                            </c:when>
                                                            <c:when test="${fn:startsWith(course.thumbnailUrl, '/')}">
                                                                <c:set var="courseImageSrc" value="${pageContext.request.contextPath}${course.thumbnailUrl}"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:set var="courseImageSrc" value="${pageContext.request.contextPath}/${course.thumbnailUrl}"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <img class="course-thumb"
                                                             src="${fn:escapeXml(courseImageSrc)}"
                                                             onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80';"
                                                             alt="${fn:escapeXml(course.title)}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="course-thumb course-thumb-empty" aria-hidden="true">
                                                            <span>CM</span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div>
                                                    <strong class="course-title"><c:out value="${course.title}"/></strong>
                                                    <div class="text-secondary small">
                                                        Mã khóa học: <c:out value="${course.courseId}"/>
                                                    </div>
                                                    <c:if test="${not empty course.shortDescription}">
                                                        <div class="course-description">
                                                            <c:out value="${course.shortDescription}"/>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <c:set var="categoryName" value="Chưa xác định"/>
                                            <c:forEach var="category" items="${categories}">
                                                <c:if test="${category.categoryId == course.categoryId}">
                                                    <c:set var="categoryName" value="${category.name}"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:out value="${categoryName}"/>
                                        </td>
                                        <td><c:out value="${empty course.level ? 'Đang cập nhật' : course.level}"/></td>
                                        <td><fmt:formatNumber value="${course.price}" type="number"/> đ</td>
                                        <td>
                                            <span class="status-badge ${course.status == 1 ? 'is-active' : 'is-inactive'}">
                                                <c:choose>
                                                    <c:when test="${course.status == 1}">Đang mở</c:when>
                                                    <c:otherwise>Tạm khóa</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="action-group">
                                                <a class="btn btn-sm btn-outline-primary"
                                                   href="${pageContext.request.contextPath}/course-detail?id=${course.courseId}">
                                                    Xem
                                                </a>
                                                <a class="btn btn-sm btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/admin/courses?action=edit&id=${course.courseId}">
                                                    Sửa
                                                </a>
                                                <a class="btn btn-sm btn-outline-info"
                                                   href="${pageContext.request.contextPath}/admin/lessons?courseId=${course.courseId}">
                                                    Bài học
                                                </a>
                                                <a class="btn btn-sm btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/admin/courses?action=delete&id=${course.courseId}"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa khóa học này?');">
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
