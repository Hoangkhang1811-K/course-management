<nav class="navbar navbar-expand-lg sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-dark" href="${pageContext.request.contextPath}/home">Course Management</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
                aria-controls="mainNavbar" aria-expanded="false" aria-label="Mở menu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                <li class="nav-item">
                    <a class="nav-link ${activePage == 'home' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activePage == 'courses' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/courses">Khóa học</a>
                </li>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <li class="nav-item">
                            <a class="nav-link ${activePage == 'my-courses' ? 'active' : ''}"
                               href="${pageContext.request.contextPath}/my-courses">Khóa học của tôi</a>
                        </li>
                        <c:if test="${sessionScope.loggedInUser.roleId == 1}">
                            <li class="nav-item">
                                <a class="nav-link"
                                   href="${pageContext.request.contextPath}/admin/dashboard">Quản trị</a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <span class="nav-link text-secondary">
                                Xin chào, <c:out value="${sessionScope.loggedInUser.fullName}"/>
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
