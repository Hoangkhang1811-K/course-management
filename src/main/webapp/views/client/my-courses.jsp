<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Khóa học của tôi - Course Management"/>
<c:set var="pageCss" value="/assets/css/my-courses.css"/>
<c:set var="activePage" value="my-courses"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main>
    <section class="my-course-hero">
        <div class="container py-5">
            <div class="hero-content">
                <span class="hero-kicker">Lộ trình học tập</span>
                <h1>Khóa học của tôi</h1>
                <p class="lead mb-0">
                    Theo dõi các khóa học đã đăng ký và tiếp tục quá trình học tập của bạn.
                </p>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <c:choose>
                <c:when test="${empty sessionScope.loggedInUser}">
                    <div class="empty-state p-5 text-center">
                        <div class="empty-icon"></div>
                        <h2 class="h4 mt-4">Vui lòng đăng nhập để xem khóa học của bạn</h2>
                        <p class="text-secondary mb-4">
                            Tài khoản học viên giúp bạn quản lý các khóa học đã đăng ký.
                        </p>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="filter-panel p-4 mb-4">
                        <form action="${pageContext.request.contextPath}/my-courses" method="get"
                              class="row g-3 align-items-end">
                            <div class="col-12 col-md-5">
                                <label for="status" class="form-label">Trạng thái học tập</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="">Tất cả trạng thái</option>
                                    <c:forEach var="statusItem" items="${statuses}">
                                        <c:choose>
                                            <c:when test="${selectedStatus == statusItem}">
                                                <option value="${statusItem}" selected>
                                                    <c:choose>
                                                        <c:when test="${statusItem == 'ENROLLED'}">Đang học</c:when>
                                                        <c:when test="${statusItem == 'COMPLETED'}">Đã hoàn thành</c:when>
                                                        <c:when test="${statusItem == 'CANCELLED'}">Đã hủy</c:when>
                                                        <c:otherwise><c:out value="${statusItem}"/></c:otherwise>
                                                    </c:choose>
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${statusItem}">
                                                    <c:choose>
                                                        <c:when test="${statusItem == 'ENROLLED'}">Đang học</c:when>
                                                        <c:when test="${statusItem == 'COMPLETED'}">Đã hoàn thành</c:when>
                                                        <c:when test="${statusItem == 'CANCELLED'}">Đã hủy</c:when>
                                                        <c:otherwise><c:out value="${statusItem}"/></c:otherwise>
                                                    </c:choose>
                                                </option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-12 col-md-4">
                                <button type="submit" class="btn btn-primary w-100">Lọc khóa học</button>
                            </div>
                            <div class="col-12 col-md-3">
                                <a class="btn btn-outline-primary w-100"
                                   href="${pageContext.request.contextPath}/my-courses">Xóa lọc</a>
                            </div>
                        </form>
                    </div>

                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 mb-4">
                        <div>
                            <h2 class="section-title mb-1">Danh sách đã đăng ký</h2>
                            <p class="text-secondary mb-0">
                                Có <strong><c:out value="${empty totalEnrollments ? 0 : totalEnrollments}"/></strong>
                                khóa học trong tài khoản của bạn.
                            </p>
                        </div>
                        <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/courses">
                            Tìm khóa học mới
                        </a>
                    </div>

                    <c:choose>
                        <c:when test="${empty enrollments}">
                            <div class="empty-state p-5 text-center">
                                <div class="empty-icon"></div>
                                <h2 class="h4 mt-4">Bạn chưa đăng ký khóa học nào</h2>
                                <p class="text-secondary mb-4">
                                    Hãy khám phá danh sách khóa học và bắt đầu lộ trình học tập của bạn.
                                </p>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/courses">
                                    Xem khóa học
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row g-4">
                                <c:forEach var="enrollment" items="${enrollments}">
                                    <div class="col-12 col-lg-6">
                                        <article class="my-course-card p-4">
                                            <div class="d-flex gap-3">
                                                <div class="course-thumb" aria-hidden="true"></div>
                                                <div class="flex-grow-1">
                                                    <div class="d-flex flex-wrap justify-content-between gap-2 mb-2">
                                                        <span class="status-badge status-${enrollment.status}">
                                                            <c:choose>
                                                                <c:when test="${enrollment.status == 'ENROLLED'}">Đang học</c:when>
                                                                <c:when test="${enrollment.status == 'COMPLETED'}">Đã hoàn thành</c:when>
                                                                <c:when test="${enrollment.status == 'CANCELLED'}">Đã hủy</c:when>
                                                                <c:otherwise><c:out value="${enrollment.status}"/></c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                        <span class="text-secondary small">
                                                            <fmt:formatDate value="${enrollment.enrolledAt}" pattern="dd/MM/yyyy"/>
                                                        </span>
                                                    </div>
                                                    <h3 class="h5 course-title">
                                                        <c:out value="${enrollment.courseTitle}"/>
                                                    </h3>
                                                    <p class="text-secondary mb-4">
                                                        Tiếp tục học tập và theo dõi tiến độ của khóa học này.
                                                    </p>
                                                    <div class="d-grid d-sm-flex gap-2">
                                                        <a class="btn btn-primary"
                                                           href="${pageContext.request.contextPath}/course-detail?id=${enrollment.courseId}">
                                                            Vào khóa học
                                                        </a>
                                                        <c:if test="${enrollment.status == 'COMPLETED'}">
                                                            <span class="btn btn-outline-primary disabled">Đã hoàn thành</span>
                                                        </c:if>
                                                        <c:if test="${enrollment.status == 'CANCELLED'}">
                                                            <span class="btn btn-outline-secondary disabled">Đăng ký đã hủy</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </article>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
