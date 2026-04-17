<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/11/2026
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${action == 'update' ? 'Cập nhật danh mục' : 'Thêm danh mục mới'} - Course Management"/>
<c:set var="activePage" value="admin-categories"/>
<c:set var="pageCss" value="/assets/css/category-form.css"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="category-form-page">
    <section class="page-heading">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="section-kicker">Quản trị danh mục</span>
                    <h1 class="mb-2">
                        <c:choose>
                            <c:when test="${action == 'update'}">Cập nhật danh mục</c:when>
                            <c:otherwise>Thêm danh mục mới</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-secondary mb-0">
                        Điền thông tin danh mục để sắp xếp khóa học rõ ràng và nhất quán trong hệ thống.
                    </p>
                </div>
                <div>
                    <a class="btn btn-outline-primary"
                       href="${pageContext.request.contextPath}/admin/categories">
                        Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="py-4 py-lg-5 bg-light">
        <div class="container">
            <div class="row g-4">
                <div class="col-12 col-lg-8">
                    <section class="form-panel">
                        <div class="panel-header">
                            <h2 class="h4 mb-1">
                                <c:choose>
                                    <c:when test="${action == 'update'}">Thông tin danh mục</c:when>
                                    <c:otherwise>Tạo danh mục mới</c:otherwise>
                                </c:choose>
                            </h2>
                            <p class="text-secondary mb-0">
                                Vui lòng nhập tên danh mục, mô tả và trạng thái hiển thị.
                            </p>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${error}"/>
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/admin/categories">
                            <input type="hidden" name="action" value="${action}"/>

                            <c:if test="${action == 'update'}">
                                <input type="hidden" name="id" value="${category.categoryId}"/>
                            </c:if>

                            <div class="mb-3">
                                <label for="name" class="form-label">Tên danh mục</label>
                                <input type="text"
                                       class="form-control"
                                       id="name"
                                       name="name"
                                       maxlength="50"
                                       value="<c:out value='${category.name}'/>"
                                       placeholder="Ví dụ: Front-end, Back-end, Database..."
                                       required>
                                <div class="form-text">Tối đa 50 ký tự.</div>
                            </div>

                            <div class="mb-4">
                                <label for="description" class="form-label">Mô tả</label>
                                <textarea class="form-control"
                                          id="description"
                                          name="description"
                                          rows="5"
                                          maxlength="500"
                                          placeholder="Nhập mô tả ngắn cho danh mục này..."><c:out
                                        value="${category.description}"/></textarea>
                                <div class="form-text">Tối đa 500 ký tự.</div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Trạng thái</label>
                                <div class="status-options">
                                    <label class="status-option">
                                        <input class="form-check-input mt-1"
                                               type="radio"
                                               name="status"
                                               value="1"
                                               <c:if test="${category.status == 1}">checked</c:if>>
                                        <div>
                                            <strong>Đang hoạt động</strong>
                                            <small>Danh mục hiển thị bình thường và có thể được sử dụng cho khóa
                                                học.</small>
                                        </div>
                                    </label>

                                    <label class="status-option">
                                        <input class="form-check-input mt-1"
                                               type="radio"
                                               name="status"
                                               value="0"
                                               <c:if test="${category.status == 0}">checked</c:if>>
                                        <div>
                                            <strong>Tạm khóa</strong>
                                            <small>Danh mục vẫn được lưu nhưng tạm thời không dùng cho hoạt động
                                                chính.</small>
                                        </div>
                                    </label>
                                </div>
                            </div>

                            <div class="d-flex flex-column flex-sm-row justify-content-end gap-2">
                                <a class="btn btn-outline-secondary"
                                   href="${pageContext.request.contextPath}/admin/categories">
                                    Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <c:choose>
                                        <c:when test="${action == 'update'}">Lưu thay đổi</c:when>
                                        <c:otherwise>Tạo danh mục</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </section>
                </div>

                <div class="col-12 col-lg-4">
                    <aside class="flow-panel">
                        <h2 class="h5 mb-3">Hướng dẫn nhanh</h2>
                        <ol class="flow-list">
                            <li>
                                <strong>Đặt tên rõ ràng</strong>
                                <span>Tên danh mục nên ngắn gọn, dễ hiểu và phản ánh đúng nhóm khóa học.</span>
                            </li>
                            <li>
                                <strong>Viết mô tả ngắn</strong>
                                <span>Mô tả giúp quản trị viên khác hiểu mục đích sử dụng của danh mục.</span>
                            </li>
                            <li>
                                <strong>Chọn trạng thái phù hợp</strong>
                                <span>Dùng “Đang hoạt động” khi danh mục đã sẵn sàng để áp dụng trong hệ thống.</span>
                            </li>
                        </ol>
                    </aside>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>

