<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
    <c:when test="${not empty course}">
        <c:set var="pageTitle" value="${course.title} - Course Management"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="Chi tiết khóa học - Course Management"/>
    </c:otherwise>
</c:choose>
<c:set var="pageCss" value="/assets/css/course-detail.css"/>
<c:set var="activePage" value="courses"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main>
    <section class="detail-hero">
        <div class="container py-5">
            <c:choose>
                <c:when test="${empty course}">
                    <div class="alert alert-danger" role="alert">Không tìm thấy thông tin khóa học.</div>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/courses">
                        Quay lại danh sách khóa học
                    </a>
                </c:when>
                <c:otherwise>
                    <div class="row g-4 align-items-center">
                        <div class="col-12 col-lg-7">
                            <div class="mb-3">
                                <a class="back-link" href="${pageContext.request.contextPath}/courses">Tất cả khóa học</a>
                                <c:if test="${not empty category}">
                                    <span class="mx-2 text-white-50">/</span>
                                    <a class="back-link"
                                       href="${pageContext.request.contextPath}/courses?categoryId=${category.categoryId}">
                                        <c:out value="${category.name}"/>
                                    </a>
                                </c:if>
                            </div>
                            <span class="badge badge-level mb-3">
                                <c:out value="${empty course.level ? 'Cơ bản' : course.level}"/>
                            </span>
                            <h1 class="detail-title"><c:out value="${course.title}"/></h1>
                            <p class="lead detail-summary"><c:out value="${course.shortDescription}"/></p>
                            <div class="d-flex flex-wrap gap-3 mt-4">
                                <div class="meta-pill">
                                    <strong><c:out value="${totalLessons}"/></strong>
                                    <span>bài học</span>
                                </div>
                                <div class="meta-pill">
                                    <strong><fmt:formatNumber value="${course.price}" type="number"/> đ</strong>
                                    <span>học phí</span>
                                </div>
                                <c:if test="${not empty category}">
                                    <div class="meta-pill">
                                        <strong><c:out value="${category.name}"/></strong>
                                        <span>danh mục</span>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="col-12 col-lg-5">
                            <div class="cover-box">
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
                                        <img class="course-cover" src="${fn:escapeXml(courseImageSrc)}"
                                             onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80';"
                                             alt="${fn:escapeXml(course.title)}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="course-cover-placeholder" role="img" aria-label="Khóa học trực tuyến"></div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <c:if test="${not empty course}">
        <%@ include file="/views/common/message.jsp" %>

        <section class="pb-5">
            <div class="container">
                <div class="row g-4">
                    <div class="col-12 col-lg-8">
                        <article class="content-panel p-4 p-lg-5 mb-4">
                            <h2 class="section-title">Giới thiệu khóa học</h2>
                            <p class="detail-text mb-0">
                                <c:choose>
                                    <c:when test="${not empty course.description}">
                                        <c:out value="${course.description}"/>
                                    </c:when>
                                    <c:otherwise>Khóa học này đang được cập nhật nội dung mô tả chi tiết.</c:otherwise>
                                </c:choose>
                            </p>
                        </article>

                        <article class="content-panel p-4 p-lg-5">
                            <div class="d-flex flex-column flex-md-row justify-content-between gap-2 mb-4">
                                <div>
                                    <h2 class="section-title mb-1">Nội dung bài học</h2>
                                    <p class="text-secondary mb-0">Danh sách bài học trong khóa học này.</p>
                                </div>
                                <span class="lesson-count"><c:out value="${totalLessons}"/> bài học</span>
                            </div>

                            <c:choose>
                                <c:when test="${empty lessons}">
                                    <div class="alert alert-info mb-0" role="alert">
                                        Khóa học này chưa có bài học nào được mở.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="lesson-list">
                                        <c:forEach var="lesson" items="${lessons}" varStatus="loop">
                                            <div class="lesson-item">
                                                <div class="lesson-order"><c:out value="${loop.index + 1}"/></div>
                                                <div class="flex-grow-1">
                                                    <h3 class="h6 mb-1"><c:out value="${lesson.title}"/></h3>
                                                    <div class="text-secondary small">
                                                        Loại nội dung:
                                                        <c:out value="${empty lesson.contentType ? 'Đang cập nhật' : lesson.contentType}"/>
                                                    </div>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${enrolled || sessionScope.loggedInUser.roleId == 1}">
                                                        <span class="badge text-bg-success">Đã mở</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge text-bg-secondary">Cần đăng ký</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </div>

                    <aside class="col-12 col-lg-4">
                        <div class="enroll-panel p-4 mb-4">
                            <div class="price mb-3">
                                <fmt:formatNumber value="${course.price}" type="number"/> đ
                            </div>
                            <c:choose>
                                <c:when test="${canEnroll}">
                                    <form action="${enrollUrl}" method="post">
                                        <input type="hidden" name="courseId" value="${course.courseId}">
                                        <button type="submit" class="btn btn-primary btn-lg w-100">Đăng ký khóa học</button>
                                    </form>
                                </c:when>
                                <c:when test="${not loggedIn}">
                                    <a class="btn btn-primary btn-lg w-100" href="${loginUrl}">Đăng nhập để đăng ký</a>
                                </c:when>
                                <c:when test="${enrolled}">
                                    <div class="alert alert-success mb-3" role="alert">Bạn đã đăng ký khóa học này.</div>
                                    <a class="btn btn-outline-primary w-100"
                                       href="${pageContext.request.contextPath}/my-courses">Xem khóa học của tôi</a>
                                </c:when>
                                <c:when test="${sessionScope.loggedInUser.roleId == 1}">
                                    <a class="btn btn-outline-primary w-100"
                                       href="${pageContext.request.contextPath}/admin/courses?action=edit&id=${course.courseId}">
                                        Quản lý khóa học
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning mb-0" role="alert">
                                        Hiện chưa thể đăng ký khóa học này.
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <hr>
                            <ul class="course-info-list">
                                <li><span>Trình độ</span><strong><c:out value="${empty course.level ? 'Cơ bản' : course.level}"/></strong></li>
                                <li><span>Số bài học</span><strong><c:out value="${totalLessons}"/></strong></li>
                                <li><span>Trạng thái</span><strong>Đang mở</strong></li>
                            </ul>
                        </div>
                    </aside>
                </div>
            </div>
        </section>

        <section class="related-section py-5">
            <div class="container">
                <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-3 mb-4">
                    <div>
                        <h2 class="section-title mb-2">Khóa học liên quan</h2>
                        <p class="text-secondary mb-0">Các khóa học cùng danh mục bạn có thể quan tâm.</p>
                    </div>
                    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">Xem tất cả</a>
                </div>

                <c:choose>
                    <c:when test="${empty relatedCourses}">
                        <div class="alert alert-info" role="alert">Hiện chưa có khóa học liên quan.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-4">
                            <c:forEach var="relatedCourse" items="${relatedCourses}">
                                <div class="col-12 col-md-6 col-lg-3">
                                    <article class="related-card">
                                        <c:choose>
                                            <c:when test="${not empty relatedCourse.thumbnailUrl}">
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(relatedCourse.thumbnailUrl, 'http://') || fn:startsWith(relatedCourse.thumbnailUrl, 'https://') || fn:startsWith(relatedCourse.thumbnailUrl, 'data:')}">
                                                        <c:set var="relatedCourseImageSrc" value="${relatedCourse.thumbnailUrl}"/>
                                                    </c:when>
                                                    <c:when test="${fn:startsWith(relatedCourse.thumbnailUrl, '/')}">
                                                        <c:set var="relatedCourseImageSrc" value="${pageContext.request.contextPath}${relatedCourse.thumbnailUrl}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="relatedCourseImageSrc" value="${pageContext.request.contextPath}/${relatedCourse.thumbnailUrl}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <img src="${fn:escapeXml(relatedCourseImageSrc)}"
                                                     onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80';"
                                                     alt="${fn:escapeXml(relatedCourse.title)}">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="related-cover-placeholder" role="img" aria-label="Khóa học liên quan"></div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="p-3">
                                            <span class="badge badge-level mb-2">
                                                <c:out value="${empty relatedCourse.level ? 'Cơ bản' : relatedCourse.level}"/>
                                            </span>
                                            <h3 class="h6 related-title"><c:out value="${relatedCourse.title}"/></h3>
                                            <div class="fw-bold mb-3">
                                                <fmt:formatNumber value="${relatedCourse.price}" type="number"/> đ
                                            </div>
                                            <a class="btn btn-outline-primary w-100"
                                               href="${pageContext.request.contextPath}/course-detail?id=${relatedCourse.courseId}">
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
    </c:if>
</main>

<%@ include file="/views/common/footer.jsp" %>
