<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Quản lý người dùng - Course Management"/>
<c:set var="activePage" value="admin-users"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="bg-light">
    <section class="page-heading py-5 bg-white border-bottom">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="text-uppercase text-primary fw-semibold small">Quản trị người dùng</span>
                    <h1 class="mb-2">Danh sách người dùng</h1>
                    <p class="text-secondary mb-0">
                        Theo dõi tài khoản quản trị viên và học viên trong hệ thống.
                    </p>
                </div>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/users?action=create">
                    Thêm người dùng
                </a>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="pb-5">
        <div class="container">
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/admin/users" method="get"
                          class="row g-3 align-items-end">
                        <div class="col-12 col-lg-4">
                            <label for="keyword" class="form-label">Tìm kiếm</label>
                            <input type="text"
                                   class="form-control"
                                   id="keyword"
                                   name="keyword"
                                   value="${fn:escapeXml(keyword)}"
                                   placeholder="Nhập tên, email hoặc số điện thoại">
                        </div>

                        <div class="col-12 col-md-6 col-lg-3">
                            <label for="roleId" class="form-label">Vai trò</label>
                            <select class="form-select" id="roleId" name="roleId">
                                <option value="">Tất cả vai trò</option>
                                <option value="${adminRoleId}" ${selectedRoleId == adminRoleId ? 'selected' : ''}>
                                    Quản trị viên
                                </option>
                                <option value="${studentRoleId}" ${selectedRoleId == studentRoleId ? 'selected' : ''}>
                                    Học viên
                                </option>
                            </select>
                        </div>

                        <div class="col-12 col-md-6 col-lg-2">
                            <label for="status" class="form-label">Trạng thái</label>
                            <select class="form-select" id="status" name="status">
                                <option value="">Tất cả</option>
                                <option value="1" ${selectedStatus == 1 ? 'selected' : ''}>Hoạt động</option>
                                <option value="0" ${selectedStatus == 0 ? 'selected' : ''}>Bị khóa</option>
                            </select>
                        </div>

                        <div class="col-12 col-lg-3 d-grid d-sm-flex gap-2">
                            <button type="submit" class="btn btn-primary flex-fill">Lọc</button>
                            <a class="btn btn-outline-primary flex-fill"
                               href="${pageContext.request.contextPath}/admin/users">
                                Xóa lọc
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 mb-4">
                <div>
                    <h2 class="h4 mb-1">Tài khoản trong hệ thống</h2>
                    <p class="text-secondary mb-0">
                        Có <strong><c:out value="${empty users ? 0 : fn:length(users)}"/></strong> người dùng phù hợp.
                    </p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/dashboard">
                    Về tổng quan
                </a>
            </div>

            <c:choose>
                <c:when test="${empty users}">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body text-center py-5">
                            <h2 class="h4 mb-2">Chưa có người dùng phù hợp</h2>
                            <p class="text-secondary mb-4">
                                Hãy thêm người dùng mới hoặc thay đổi điều kiện lọc.
                            </p>
                            <a class="btn btn-primary"
                               href="${pageContext.request.contextPath}/admin/users?action=create">
                                Thêm người dùng
                            </a>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="card border-0 shadow-sm overflow-hidden">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead>
                                <tr>
                                    <th class="ps-4">Người dùng</th>
                                    <th>Vai trò</th>
                                    <th>Số điện thoại</th>
                                    <th>Ngày tạo</th>
                                    <th>Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${users}">
                                    <tr>
                                        <td class="ps-4">
                                            <strong><c:out value="${item.fullName}"/></strong>
                                            <div class="text-secondary small">
                                                <c:out value="${item.email}"/>
                                            </div>
                                            <div class="text-muted small">
                                                Mã người dùng: #<c:out value="${item.userId}"/>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.roleId == adminRoleId}">
                                                    <span class="badge text-bg-primary">Quản trị viên</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-secondary">Học viên</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.phone}">
                                                    <c:out value="${item.phone}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa cập nhật</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.createdAt}">
                                                    <fmt:formatDate value="${item.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.status == 1}">
                                                    <span class="badge text-bg-success">Hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-danger">Bị khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="d-inline-flex flex-wrap justify-content-end gap-2">
                                                <a class="btn btn-sm btn-outline-secondary"
                                                   href="${pageContext.request.contextPath}/admin/users?action=edit&id=${item.userId}">
                                                    Sửa
                                                </a>

                                                <c:choose>
                                                    <c:when test="${item.status == 1}">
                                                        <a class="btn btn-sm btn-outline-warning"
                                                           href="${pageContext.request.contextPath}/admin/users?action=lock&id=${item.userId}"
                                                           onclick="return confirm('Bạn có chắc chắn muốn khóa người dùng này?');">
                                                            Khóa
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="btn btn-sm btn-outline-success"
                                                           href="${pageContext.request.contextPath}/admin/users?action=unlock&id=${item.userId}">
                                                            Mở khóa
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>

                                                <a class="btn btn-sm btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/admin/users?action=delete&id=${item.userId}"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">
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
