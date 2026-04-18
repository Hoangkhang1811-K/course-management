<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Trang chủ - Course Management"/>
<c:set var="pageCss" value="/assets/css/home.css"/>
<c:set var="activePage" value="home"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main>
    <section class="hero">
        <div class="container">
            <div class="hero-content py-5">
                <h1>Học kỹ năng mới với các khóa học thực tế</h1>
                <p class="lead mt-3 mb-4">
                    Chọn khóa học phù hợp, theo dõi bài học và quản lý lộ trình học tập của bạn.
                </p>
                <div class="d-flex flex-column flex-sm-row gap-3">
                    <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/courses">Xem khóa học</a>
                    <a class="btn btn-light btn-lg" href="#featuredCourses">Khóa học mới</a>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="row g-3">
                <div class="col-12 col-md-4">
                    <div class="stat-box p-4 h-100">
                        <div class="fs-2 fw-bold text-dark"><c:out value="${totalCourses}"/></div>
                        <div class="text-secondary">Khóa học đang mở</div>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="stat-box p-4 h-100">
                        <div class="fs-2 fw-bold text-dark"><c:out value="${totalCategories}"/></div>
                        <div class="text-secondary">Danh mục học tập</div>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="stat-box p-4 h-100">
                        <div class="fs-2 fw-bold text-dark"><c:out value="${totalStudents}"/></div>
                        <div class="text-secondary">Học viên đang hoạt động</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="py-5 bg-white" id="featuredCourses">
        <div class="container">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
                <div>
                    <h2 class="section-title mb-2">Khóa học mới nhất</h2>
                    <p class="text-secondary mb-0">Các khóa học đang mở đăng ký cho học viên.</p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">Xem tất cả</a>
            </div>

            <c:choose>
                <c:when test="${empty featuredCourses}">
                    <div class="alert alert-info" role="alert">Hiện chưa có khóa học nào đang mở.</div>
                </c:when>
                <c:otherwise>
                    <div class="row g-4">
                        <c:forEach var="course" items="${featuredCourses}">
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
                                            <img src="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80"
                                                 alt="Khóa học trực tuyến">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="p-4">
                                        <div class="d-flex justify-content-between align-items-start gap-2 mb-3">
                                            <span class="badge badge-level px-3 py-2">
                                                <c:out value="${empty course.level ? 'Cơ bản' : course.level}"/>
                                            </span>
                                            <span class="fw-bold text-dark">
                                                <fmt:formatNumber value="${course.price}" type="number"/> đ
                                            </span>
                                        </div>
                                        <h3 class="h5 course-title"><c:out value="${course.title}"/></h3>
                                        <p class="text-secondary course-description">
                                            <c:out value="${course.shortDescription}"/>
                                        </p>
                                        <a class="btn btn-primary w-100"
                                           href="${pageContext.request.contextPath}/course-detail?id=${course.courseId}">
                                            Xem chi tiết
                                        </a>
                                    </div>
                                </article>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <section class="py-5">
        <div class="container">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
                <div>
                    <h2 class="section-title mb-2">Danh mục nổi bật</h2>
                    <p class="text-secondary mb-0">Tìm khóa học theo nhóm kỹ năng bạn quan tâm.</p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">Khám phá</a>
            </div>

            <c:choose>
                <c:when test="${empty categories}">
                    <div class="alert alert-info" role="alert">
                        Hiện chưa có danh mục khóa học nào đang hoạt động.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-3">
                        <c:forEach var="category" items="${categories}">
                            <div class="col-12 col-sm-6 col-lg-3">
                                <a class="category-item"
                                   href="${pageContext.request.contextPath}/courses?categoryId=${category.categoryId}">
                                    <strong class="d-block mb-2"><c:out value="${category.name}"/></strong>
                                    <span class="text-secondary"><c:out value="${category.description}"/></span>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
