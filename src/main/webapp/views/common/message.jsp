<section class="py-4">
    <div class="container">
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${error}"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${sessionScope.error}"/>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <c:if test="${not empty successMsg}">
            <div class="alert alert-success" role="alert">
                <c:out value="${successMsg}"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success" role="alert">
                <c:out value="${sessionScope.successMsg}"/>
            </div>
            <c:remove var="successMsg" scope="session"/>
        </c:if>

        <c:if test="${not empty message}">
            <div class="alert alert-info" role="alert">
                <c:out value="${message}"/>
            </div>
        </c:if>
    </div>
</section>
