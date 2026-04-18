<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="${action == 'update' ? 'Cập nhật bài học' : 'Thêm bài học mới'} - Course Management"/>
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
                    <h1 class="mb-2">
                        <c:choose>
                            <c:when test="${action == 'update'}">Cập nhật bài học</c:when>
                            <c:otherwise>Thêm bài học mới</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-secondary mb-0">
                        Khóa học: <strong><c:out value="${course.title}"/></strong>
                    </p>
                </div>
                <a class="btn btn-outline-primary"
                   href="${pageContext.request.contextPath}/admin/lessons?courseId=${course.courseId}">
                    Quay lại danh sách bài học
                </a>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="row g-4">
                <div class="col-12 col-lg-8">
                    <section class="filter-panel">
                        <h2 class="h4 mb-1">Thông tin bài học</h2>
                        <p class="text-secondary mb-4">Các trường có dấu * là bắt buộc.</p>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${error}"/>
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/admin/lessons">
                            <input type="hidden" name="action" value="${action}">

                            <c:if test="${action == 'update'}">
                                <input type="hidden" name="id" value="${lesson.lessonId}">
                            </c:if>

                            <div class="mb-3">
                                <label for="courseId" class="form-label">Khóa học <span class="text-danger">*</span></label>
                                <select class="form-select" id="courseId" name="courseId" required>
                                    <c:forEach var="item" items="${courses}">
                                        <option value="${item.courseId}" ${lesson.courseId == item.courseId ? 'selected' : ''}>
                                            <c:out value="${item.title}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="form-text">Đổi khóa học sẽ chuyển bài học sang khóa học được chọn.</div>
                            </div>

                            <div class="mb-3">
                                <label for="title" class="form-label">Tên bài học <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       id="title"
                                       name="title"
                                       maxlength="150"
                                       value="${fn:escapeXml(lesson.title)}"
                                       placeholder="Ví dụ: Giới thiệu JSP Servlet"
                                       required>
                                <div class="form-text">Tối đa 150 ký tự.</div>
                            </div>

                            <div class="row g-3">
                                <div class="col-12 col-md-4">
                                    <label for="lessonOrder" class="form-label">Thứ tự <span class="text-danger">*</span></label>
                                    <input type="number"
                                           class="form-control"
                                           id="lessonOrder"
                                           name="lessonOrder"
                                           min="1"
                                           value="${lesson.lessonOrder}"
                                           required>
                                </div>

                                <div class="col-12 col-md-4">
                                    <label for="contentType" class="form-label">Loại nội dung <span class="text-danger">*</span></label>
                                    <select class="form-select" id="contentType" name="contentType" required>
                                        <c:forEach var="type" items="${contentTypes}">
                                            <option value="${type}" ${lesson.contentType == type ? 'selected' : ''}>
                                                <c:choose>
                                                    <c:when test="${type == 'VIDEO'}">Video</c:when>
                                                    <c:otherwise>Text</c:otherwise>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-12 col-md-4">
                                    <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="1" ${lesson.status == 1 ? 'selected' : ''}>Đang mở</option>
                                        <option value="0" ${lesson.status == 0 ? 'selected' : ''}>Tạm khóa</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3 mt-3">
                                <label for="contentValue" class="form-label">Nội dung bài học</label>
                                <textarea class="form-control"
                                          id="contentValue"
                                          name="contentValue"
                                          rows="10"
                                          maxlength="5000"
                                          placeholder="Nhập nội dung bài học hoặc URL video"><c:out value="${lesson.contentValue}"/></textarea>
                                <div class="form-text">
                                    Với loại Video, nhập URL video. Với loại Text, nhập nội dung bài học. Tối đa 5000 ký tự.
                                </div>
                            </div>

                            <div class="d-flex flex-column flex-sm-row justify-content-end gap-2 mt-4">
                                <a class="btn btn-outline-secondary"
                                   href="${pageContext.request.contextPath}/admin/lessons?courseId=${course.courseId}">
                                    Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <c:choose>
                                        <c:when test="${action == 'update'}">Lưu thay đổi</c:when>
                                        <c:otherwise>Thêm bài học</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </section>
                </div>

                <div class="col-12 col-lg-4">
                    <aside class="filter-panel">
                        <h2 class="h5 mb-3">Quy tắc bài học</h2>
                        <ul class="text-secondary mb-0">
                            <li>Thứ tự bài học phải lớn hơn 0.</li>
                            <li>Mỗi khóa học không được có hai bài học cùng thứ tự.</li>
                            <li>Loại nội dung chỉ nhận Text hoặc Video.</li>
                            <li>Bài học tạm khóa sẽ không nên hiển thị cho học viên.</li>
                        </ul>
                    </aside>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
