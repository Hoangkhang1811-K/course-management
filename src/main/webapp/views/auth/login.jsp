<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 4/11/2026
  Time: 2:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center align-items-center min-vh-100">
        <div class="col-12 col-sm-10 col-md-8 col-lg-5">
            <div class="card shadow-sm border-0 rounded-4">
                <div class="card-body p-4 p-md-5">
                    <h2 class="text-center mb-4">Login</h2>

                    <%
                        String error = (String) request.getAttribute("error");
                        String successMsg = (String) session.getAttribute("successMsg");
                        String email = (String) request.getAttribute("email");
                        if (email == null) {
                            email = "";
                        }
                    %>

                    <% if (error != null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= error %>
                    </div>
                    <% } %>

                    <% if (successMsg != null) { %>
                    <div class="alert alert-success" role="alert">
                        <%= successMsg %>
                    </div>
                    <%
                            session.removeAttribute("successMsg");
                        }
                    %>

                    <form action="<%= request.getContextPath() %>/login" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input
                                    type="email"
                                    class="form-control"
                                    id="email"
                                    name="email"
                                    value="<%= email %>"
                                    placeholder="Enter your email"
                                    required
                            >
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input
                                    type="password"
                                    class="form-control"
                                    id="password"
                                    name="password"
                                    placeholder="Enter your password"
                                    required
                            >
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>

                    <div class="text-center mt-3">
                        <span>Don't have an account?</span>
                        <a href="<%= request.getContextPath() %>/register">Register</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
