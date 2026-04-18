<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Danh sách khóa học - Course Management"/>
<c:set var="pageCss" value="/assets/css/course-list.css"/>
<c:set var="activePage" value="courses"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main>
    <section class="course-list-hero">
        <div class="container py-5">
            <div class="hero-content">
                <span class="hero-kicker">Khóa học</span>
                <h1>Chọn khóa học phù hợp với mục tiêu của bạn</h1>
                <p class="lead mb-0">
                    Tìm kiếm, lọc theo danh mục và bắt đầu học với các khóa học đang mở.
                </p>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="filter-panel p-4 mb-4">
                <form action="${pageContext.request.contextPath}/courses" method="get" class="row g-3 align-items-end">
                    <div class="col-12 col-lg-5">
                        <label for="keyword" class="form-label">Tìm kiếm khóa học</label>
                        <input type="text" class="form-control" id="keyword" name="keyword"
                               value="${fn:escapeXml(keyword)}" placeholder="Nhập tên khóa học">
                    </div>
                    <div class="col-12 col-lg-4">
                        <label for="categoryId" class="form-label">Danh mục</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">Tất cả danh mục</option>
                            <c:forEach var="category" items="${categories}">
                                <c:choose>
                                    <c:when test="${selectedCategoryId == category.categoryId}">
                                        <option value="${category.categoryId}" selected>
                                            <c:out value="${category.name}"/>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${category.categoryId}">
                                            <c:out value="${category.name}"/>
                                        </option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-12 col-lg-3 d-grid d-sm-flex gap-2">
                        <button type="submit" class="btn btn-primary flex-fill">Lọc khóa học</button>
                        <a class="btn btn-outline-primary flex-fill" href="${pageContext.request.contextPath}/courses">Xóa lọc</a>
                    </div>
                </form>
            </div>

            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 mb-4">
                <div>
                    <h2 class="section-title mb-1">Danh sách khóa học</h2>
                    <p class="text-secondary mb-0">
                        Có <strong><c:out value="${totalCourses}"/></strong> khóa học phù hợp.
                    </p>
                </div>
                <c:if test="${not empty selectedCategory}">
                    <div class="selected-filter">
                        Danh mục: <strong><c:out value="${selectedCategory.name}"/></strong>
                    </div>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${empty courses}">
                    <div class="empty-state p-5 text-center">
                        <div class="empty-icon"></div>
                        <h2 class="h4 mt-4">Không tìm thấy khóa học phù hợp</h2>
                        <p class="text-secondary mb-4">
                            Hãy thử tìm kiếm bằng từ khóa khác hoặc chọn lại danh mục.
                        </p>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/courses">Xem tất cả khóa học</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-4">
                        <c:forEach var="course" items="${courses}">
                            <div class="col-12 col-md-6 col-lg-4">
                                <article class="course-card">
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
                                            <img src="${fn:escapeXml(courseImageSrc)}"
                                                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80';"
                                                 alt="${fn:escapeXml(course.title)}">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="course-cover-placeholder" role="img" aria-label="Khóa học trực tuyến"></div>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="p-4">
                                        <div class="d-flex justify-content-between align-items-start gap-2 mb-3">
                                            <span class="badge badge-level">
                                                <c:out value="${empty course.level ? 'Cơ bản' : course.level}"/>
                                            </span>
                                            <span class="course-price">
                                                <fmt:formatNumber value="${course.price}" type="number"/> đ
                                            </span>
                                        </div>
                                        <h3 class="h5 course-title"><c:out value="${course.title}"/></h3>
                                        <p class="text-secondary course-description">
                                            <c:choose>
                                                <c:when test="${not empty course.shortDescription}">
                                                    <c:out value="${course.shortDescription}"/>
                                                </c:when>
                                                <c:otherwise>Khóa học đang được cập nhật mô tả ngắn.</c:otherwise>
                                            </c:choose>
                                        </p>
                                        <div class="d-grid gap-2">
                                            <a class="btn btn-primary"
                                               href="${pageContext.request.contextPath}/course-detail?id=${course.courseId}">
                                                Xem chi tiết
                                            </a>
                                            <c:if test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.roleId == 1}">
                                                <a class="btn btn-outline-primary"
                                                   href="${pageContext.request.contextPath}/admin/courses?action=edit&id=${course.courseId}">
                                                    Quản lý khóa học
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </article>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
