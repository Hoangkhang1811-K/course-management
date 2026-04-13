<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Đăng ký tài khoản - Course Management"/>
<c:set var="pageCss" value="/assets/css/register.css"/>
<c:set var="activePage" value="register"/>
<%@ include file="/views/common/header.jsp" %>
<%@ include file="/views/common/navbar.jsp" %>

<main>
    <section class="register-hero">
        <div class="container py-5">
            <div class="hero-content">
                <span class="hero-kicker">Tài khoản học viên</span>
                <h1>Tạo tài khoản để bắt đầu học tập</h1>
                <p class="lead mb-0">
                    Email của bạn sẽ được dùng làm tên đăng nhập cho hệ thống.
                </p>
            </div>
        </div>
    </section>

    <%@ include file="/views/common/message.jsp" %>

    <section class="register-section pb-5">
        <div class="container">
            <div class="row g-4 align-items-stretch">
                <div class="col-12 col-lg-5">
                    <div class="info-panel p-4 p-lg-5 h-100">
                        <h2 class="section-title mb-3">Quyền lợi học viên</h2>
                        <ul class="benefit-list">
                            <li>Đăng ký các khóa học đang mở.</li>
                            <li>Theo dõi danh sách khóa học của tôi.</li>
                            <li>Xem bài học và trạng thái học tập.</li>
                            <li>Quản lý lộ trình học tập bằng email đăng nhập.</li>
                        </ul>
                        <div class="support-box mt-4">
                            Đã có tài khoản?
                            <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-lg-7">
                    <div class="register-panel p-4 p-lg-5">
                        <div class="mb-4">
                            <h2 class="section-title mb-2">Thông tin đăng ký</h2>
                            <p class="text-secondary mb-0">
                                Vui lòng nhập thông tin chính xác để tạo tài khoản học viên.
                            </p>
                        </div>

                        <form action="${pageContext.request.contextPath}/register" method="post" class="row g-3">
                            <div class="col-12">
                                <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       id="fullName"
                                       name="fullName"
                                       value="${fn:escapeXml(fullName)}"
                                       placeholder="Nhập họ và tên"
                                       maxlength="100"
                                       required>
                            </div>

                            <div class="col-12 col-md-6">
                                <label for="email" class="form-label">Email đăng nhập <span class="text-danger">*</span></label>
                                <input type="email"
                                       class="form-control"
                                       id="email"
                                       name="email"
                                       value="${fn:escapeXml(email)}"
                                       placeholder="example@email.com"
                                       maxlength="100"
                                       required>
                            </div>

                            <div class="col-12 col-md-6">
                                <label for="phone" class="form-label">Số điện thoại</label>
                                <input type="tel"
                                       class="form-control"
                                       id="phone"
                                       name="phone"
                                       value="${fn:escapeXml(phone)}"
                                       placeholder="Nhập số điện thoại"
                                       maxlength="15">
                            </div>

                            <div class="col-12 col-md-6">
                                <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
                                <input type="password"
                                       class="form-control"
                                       id="password"
                                       name="password"
                                       placeholder="Tối thiểu 6 ký tự"
                                       minlength="6"
                                       maxlength="100"
                                       required>
                            </div>

                            <div class="col-12 col-md-6">
                                <label for="confirmPassword" class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
                                <input type="password"
                                       class="form-control"
                                       id="confirmPassword"
                                       name="confirmPassword"
                                       placeholder="Nhập lại mật khẩu"
                                       minlength="6"
                                       maxlength="100"
                                       required>
                            </div>

                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="agreeTerms" required>
                                    <label class="form-check-label" for="agreeTerms">
                                        Tôi xác nhận thông tin đăng ký là chính xác.
                                    </label>
                                </div>
                            </div>

                            <div class="col-12 d-grid d-md-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-lg">Tạo tài khoản</button>
                                <a class="btn btn-outline-primary btn-lg" href="${pageContext.request.contextPath}/login">
                                    Đã có tài khoản
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="/views/common/footer.jsp" %>
