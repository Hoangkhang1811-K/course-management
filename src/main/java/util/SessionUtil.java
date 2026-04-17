package util;

import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtil {
    public static final String LOGGED_IN_USER = "loggedInUser";
    public static final String SUCCESS_MESSAGE = "successMsg";
    public static final String ERROR_MESSAGE = "error";

    private SessionUtil() {
    }

    public static void setLoggedInUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(LOGGED_IN_USER, user);
    }

    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute(LOGGED_IN_USER);
        if (userObject instanceof User) {
            return (User) userObject;
        }

        return null;
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    public static boolean hasRole(HttpServletRequest request, int roleId) {
        User user = getLoggedInUser(request);
        return user != null && user.getRoleId() == roleId;
    }

    public static void removeLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(LOGGED_IN_USER);
        }
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static void setSuccessMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS_MESSAGE, message);
    }

    public static void setErrorMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR_MESSAGE, message);
    }

    public static void moveFlashMessagesToRequest(HttpServletRequest request) {
        moveSessionAttributeToRequest(request, ERROR_MESSAGE);
        moveSessionAttributeToRequest(request, SUCCESS_MESSAGE);
    }

    private static void moveSessionAttributeToRequest(HttpServletRequest request, String attributeName) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        Object value = session.getAttribute(attributeName);
        if (value != null) {
            request.setAttribute(attributeName, value);
            session.removeAttribute(attributeName);
        }
    }
}
