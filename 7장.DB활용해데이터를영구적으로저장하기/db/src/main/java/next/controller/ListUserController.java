package next.controller;

import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users")
public class ListUserController implements Controller {
    static final Logger log = LoggerFactory.getLogger(ListUserController.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return "redirect:/users/loginForm";
        }
        List<User> users = new ArrayList<>();
        try {
            UserDao userDao = new UserDao();
            users = userDao.findAll();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        request.setAttribute("users", users);
        return "/user/list.jsp";
    }
}
