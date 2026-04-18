<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="${action == 'update' ? 'Cập nhật người dùng' : 'Thêm người dùng mới'} - Course Management"/>
<c:set var="activePage" value="admin-users"/>

<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main class="bg-light">
    <section class="page-heading py-5 bg-white border-bottom">
        <div class="container">
            <div class="d-flex flex-column flex-lg-row justify-content-between align-items-lg-end gap-3">
                <div>
                    <span class="text-uppercase text-primary fw-semibold small">Quản trị người dùng</span>
                    <h1 class="mb-2">
                        <c:choose>
                            <c:when test="${action == 'update'}">Cập nhật người dùng</c:when>
                            <c:otherwise>Thêm người dùng mới</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="text-secondary mb-0">
                        Quản lý thông tin đăng nhập, vai trò và trạng thái tài khoản.
                    </p>
                </div>
                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/admin/users">
                    Quay lại danh sách
                </a>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="py-4 py-lg-5">
        <div class="container">
            <div class="row g-4">
                <div class="col-12 col-lg-8">
                    <section class="card border-0 shadow-sm">
                        <div class="card-body p-4 p-lg-5">
                            <h2 class="h4 mb-1">Thông tin tài khoản</h2>
                            <p class="text-secondary mb-4">
                                Các trường có dấu * là bắt buộc.
                            </p>

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger" role="alert">
                                    <c:out value="${error}"/>
                                </div>
                            </c:if>

                            <form method="post" action="${pageContext.request.contextPath}/admin/users">
                                <input type="hidden" name="action" value="${action}"/>

                                <c:if test="${action == 'update'}">
                                    <input type="hidden" name="id" value="${user.userId}"/>
                                </c:if>

                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                                    <input type="text"
                                           class="form-control"
                                           id="fullName"
                                           name="fullName"
                                           maxlength="100"
                                           value="${fn:escapeXml(user.fullName)}"
                                           placeholder="Nhập họ và tên"
                                           required>
                                    <div class="form-text">Tối đa 100 ký tự.</div>
                                </div>

                                <div class="mb-3">
                                    <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                                    <input type="email"
                                           class="form-control"
                                           id="email"
                                           name="email"
                                           maxlength="100"
                                           value="${fn:escapeXml(user.email)}"
                                           placeholder="example@email.com"
                                           required>
                                </div>

                                <div class="mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="text"
                                           class="form-control"
                                           id="phone"
                                           name="phone"
                                           maxlength="10"
                                           value="${fn:escapeXml(user.phone)}"
                                           placeholder="Ví dụ: 0901234567">
                                    <div class="form-text">Bỏ trống hoặc nhập đúng 10 chữ số.</div>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">
                                        Mật khẩu
                                        <c:if test="${action != 'update'}">
                                            <span class="text-danger">*</span>
                                        </c:if>
                                    </label>
                                    <input type="password"
                                           class="form-control"
                                           id="password"
                                           name="password"
                                           maxlength="100"
                                           placeholder="${action == 'update' ? 'Bỏ trống nếu không đổi mật khẩu' : 'Nhập mật khẩu'}"
                                           <c:if test="${action != 'update'}">required</c:if>>
                                    <div class="form-text">Mật khẩu cần có ít nhất 6 ký tự.</div>
                                </div>

                                <div class="row g-3">
                                    <div class="col-12 col-md-6">
                                        <label for="roleId" class="form-label">Vai trò <span class="text-danger">*</span></label>
                                        <select class="form-select" id="roleId" name="roleId" required>
                                            <option value="${studentRoleId}" ${user.roleId == studentRoleId ? 'selected' : ''}>
                                                Học viên
                                            </option>
                                            <option value="${adminRoleId}" ${user.roleId == adminRoleId ? 'selected' : ''}>
                                                Quản trị viên
                                            </option>
                                        </select>
                                    </div>

                                    <div class="col-12 col-md-6">
                                        <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="1" ${user.status == 1 ? 'selected' : ''}>Hoạt động</option>
                                            <option value="0" ${user.status == 0 ? 'selected' : ''}>Bị khóa</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="d-flex flex-column flex-sm-row justify-content-end gap-2 mt-4">
                                    <a class="btn btn-outline-secondary"
                                       href="${pageContext.request.contextPath}/admin/users">
                                        Hủy
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <c:choose>
                                            <c:when test="${action == 'update'}">Lưu thay đổi</c:when>
                                            <c:otherwise>Thêm người dùng</c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </section>
                </div>

                <div class="col-12 col-lg-4">
                    <aside class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <h2 class="h5 mb-3">Quy tắc tài khoản</h2>
                            <ul class="mb-0 text-secondary">
                                <li>Email dùng để đăng nhập và không được trùng.</li>
                                <li>Học viên có thể đăng ký và học khóa học.</li>
                                <li>Quản trị viên có quyền truy cập khu vực quản trị.</li>
                                <li>Tài khoản bị khóa sẽ không đăng nhập được.</li>
                            </ul>
                        </div>
                    </aside>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
