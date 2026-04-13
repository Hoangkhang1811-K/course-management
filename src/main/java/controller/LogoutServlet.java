package controller;

import service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name= "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    private AuthService authService;
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("logout".equals(action)) {
            HttpSession session = req.getSession();
            session.invalidate(); // Xóa sạch session
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("username", "");
        req.getRequestDispatcher("page/login.jsp").forward(req, resp);
    }

}
