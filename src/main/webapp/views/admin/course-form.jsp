<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="${action == 'update' ? 'Cập nhật khóa học' : 'Thêm khóa học mới'} - Course Management"/>
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
                    <h1 class="mb-2">
                        <c:choose>
                            <c:when test="${action == 'update'}">Cập nhật khóa học</c:when>
                            <c:otherwise>Thêm khóa học mới</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-secondary mb-0">
                        Điền thông tin khóa học, danh mục, giá và trạng thái hiển thị.
                    </p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/courses">
                    Quay lại danh sách
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
                        <h2 class="h4 mb-1">Thông tin khóa học</h2>
                        <p class="text-secondary mb-4">Các trường có dấu * là bắt buộc.</p>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${error}"/>
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/admin/courses">
                            <input type="hidden" name="action" value="${action}"/>

                            <c:if test="${action == 'update'}">
                                <input type="hidden" name="id" value="${course.courseId}"/>
                            </c:if>

                            <div class="mb-3">
                                <label for="categoryId" class="form-label">Danh mục <span class="text-danger">*</span></label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="">Chọn danh mục</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}" ${course.categoryId == category.categoryId ? 'selected' : ''}>
                                            <c:out value="${category.name}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="title" class="form-label">Tên khóa học <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       id="title"
                                       name="title"
                                       maxlength="150"
                                       value="${fn:escapeXml(course.title)}"
                                       placeholder="Ví dụ: Java Web cơ bản"
                                       required>
                                <div class="form-text">Tối đa 150 ký tự.</div>
                            </div>

                            <div class="mb-3">
                                <label for="shortDescription" class="form-label">Mô tả ngắn</label>
                                <textarea class="form-control"
                                          id="shortDescription"
                                          name="shortDescription"
                                          rows="3"
                                          maxlength="255"
                                          placeholder="Mô tả ngắn hiển thị trong danh sách khóa học"><c:out value="${course.shortDescription}"/></textarea>
                                <div class="form-text">Tối đa 255 ký tự.</div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Mô tả chi tiết</label>
                                <textarea class="form-control"
                                          id="description"
                                          name="description"
                                          rows="6"
                                          placeholder="Nội dung chi tiết của khóa học"><c:out value="${course.description}"/></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="thumbnailUrl" class="form-label">Ảnh đại diện</label>
                                <input type="text"
                                       class="form-control"
                                       id="thumbnailUrl"
                                       name="thumbnailUrl"
                                       maxlength="500"
                                       value="${fn:escapeXml(course.thumbnailUrl)}"
                                       placeholder="Nhập URL ảnh hoặc đường dẫn trong webapp">
                                <div class="form-text">Tối đa 500 ký tự.</div>
                            </div>

                            <div class="row g-3">
                                <div class="col-12 col-md-4">
                                    <label for="level" class="form-label">Trình độ</label>
                                    <input type="text"
                                           class="form-control"
                                           id="level"
                                           name="level"
                                           maxlength="50"
                                           value="${fn:escapeXml(course.level)}"
                                           placeholder="Beginner">
                                </div>

                                <div class="col-12 col-md-4">
                                    <label for="price" class="form-label">Giá <span class="text-danger">*</span></label>
                                    <input type="number"
                                           class="form-control"
                                           id="price"
                                           name="price"
                                           min="0"
                                           step="1000"
                                           value="${course.price}"
                                           required>
                                </div>

                                <div class="col-12 col-md-4">
                                    <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="1" ${course.status == 1 ? 'selected' : ''}>Đang mở</option>
                                        <option value="0" ${course.status == 0 ? 'selected' : ''}>Tạm khóa</option>
                                    </select>
                                </div>
                            </div>

                            <div class="d-flex flex-column flex-sm-row justify-content-end gap-2 mt-4">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/courses">
                                    Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <c:choose>
                                        <c:when test="${action == 'update'}">Lưu thay đổi</c:when>
                                        <c:otherwise>Thêm khóa học</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </section>
                </div>

                <div class="col-12 col-lg-4">
                    <aside class="filter-panel">
                        <h2 class="h5 mb-3">Gợi ý nhập liệu</h2>
                        <ul class="text-secondary mb-0">
                            <li>Chọn đúng danh mục để học viên dễ tìm khóa học.</li>
                            <li>Mô tả ngắn nên súc tích và rõ lợi ích chính.</li>
                            <li>Giá nhập bằng số, dùng 0 cho khóa học miễn phí.</li>
                            <li>Đặt trạng thái đang mở khi khóa học sẵn sàng hiển thị.</li>
                        </ul>
                    </aside>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
