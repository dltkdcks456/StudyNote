package next.controller;

import next.model.User;

import javax.servlet.http.HttpSession;

public class UserSessionUtils {
    public final static String USER_SESSION_KEY = "user";

    public static boolean isSameUser(HttpSession session, User user) {

        if (user == null) {
            return false;
        }

        if (!isLogined(session)) {
            return false;
        }

        return user.isSameUser(getUserFromSession(session));
    }

    private static boolean isLogined(HttpSession session) {
        if (getUserFromSession(session) == null) {
            return false;
        }
        return true;
    }

    private static User getUserFromSession(HttpSession session) {
        Object user = session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            return null;
        }
        return (User) user;
    }
}
