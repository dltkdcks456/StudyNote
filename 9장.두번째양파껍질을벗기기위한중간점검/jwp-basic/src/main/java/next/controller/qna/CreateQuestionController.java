package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateQuestionController extends AbstractController {
    static final Logger log = LoggerFactory.getLogger(CreateQuestionController.class);

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        User user = UserSessionUtils.getUserFromSession(session);
        if (user == null) {
            return jspView("redirect:/users/loginForm");
        }
        log.info("user : {}", user);

        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        log.info("{} {} {}", user.getUserId(), title, contents);

        Question question = new Question(user.getUserId(), title, contents);

        QuestionDao questionDao = new QuestionDao();
        questionDao.insert(question);
        return jspView("redirect:/");
    }
}
