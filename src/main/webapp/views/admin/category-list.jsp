<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/11/2026
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Quản lý danh mục - Course Management"/>
<c:set var="activePage" value="admin-categories"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="category-list-page bg-light">
    <section class="page-heading">
        <div class="container">
            <div class="row g-3 align-items-center justify-content-between">
                <div class="col-lg-8">
                    <h1 class="mb-2">Danh sách danh mục</h1>
                    <p class="text-secondary mb-0">
                        Quản lý các danh mục khóa học trong hệ thống, theo dõi trạng thái hoạt động và cập nhật nhanh khi cần.
                    </p>
                </div>
<%--                <div class="col-lg-auto">--%>
<%--                    <a class="btn btn-primary px-4"--%>
<%--                       href="${pageContext.request.contextPath}/admin/categories?action=create">--%>
<%--                        + Thêm danh mục--%>
<%--                    </a>--%>
<%--                </div>--%>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="list-toolbar card border-0 shadow-sm rounded-4 mb-4">
                <div class="card-body p-4">
                    <div class="row g-3 align-items-center justify-content-between">
                        <div class="col-md">
                            <h2 class="h4 mb-1">Quản lý nhanh</h2>
                            <p class="text-secondary mb-0">
                                Hiện có tổng cộng
                                <strong><c:out value="${empty categories ? 0 : fn:length(categories)}"/></strong>
                                danh mục trong hệ thống.
                            </p>
                        </div>
                        <div class="col-md-auto d-flex gap-2">
                            <a class="btn btn-outline-secondary"
                               href="${pageContext.request.contextPath}/admin/dashboard">
                                Về tổng quan
                            </a>
                            <a class="btn btn-outline-primary"
                               href="${pageContext.request.contextPath}/admin/categories?action=create">
                                Tạo mới
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty categories}">
                    <div class="card border-0 shadow-sm rounded-4 empty-panel">
                        <div class="card-body text-center py-5">
                            <h2 class="h4 mb-2">Chưa có danh mục nào</h2>
                            <p class="text-secondary mb-4">
                                Hãy tạo danh mục đầu tiên để bắt đầu quản lý khóa học dễ dàng hơn.
                            </p>
                            <a class="btn btn-primary"
                               href="${pageContext.request.contextPath}/admin/categories?action=create">
                                Thêm danh mục
                            </a>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0 category-table">
                                <thead>
                                <tr>
                                    <th class="ps-4">Mã</th>
                                    <th>Tên danh mục</th>
                                    <th>Mô tả</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="category" items="${categories}">
                                    <tr>
                                        <td class="ps-4 fw-semibold text-secondary">
                                            #<c:out value="${category.categoryId}"/>
                                        </td>

                                        <td>
                                            <div class="fw-semibold category-name">
                                                <c:out value="${category.name}"/>
                                            </div>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty category.description}">
                                                    <span class="text-secondary category-description">
                                                        <c:out value="${category.description}"/>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa có mô tả</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${category.status == 1}">
                                                    <span class="status-badge is-active">Đang hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge is-inactive">Tạm khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td class="text-end pe-4">
                                            <div class="d-inline-flex flex-wrap justify-content-end gap-2">
                                                <a class="btn btn-sm btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/admin/categories?action=edit&id=${category.categoryId}">
                                                    Sửa
                                                </a>

                                                <a class="btn btn-sm btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/admin/categories?action=delete&id=${category.categoryId}"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?');">
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