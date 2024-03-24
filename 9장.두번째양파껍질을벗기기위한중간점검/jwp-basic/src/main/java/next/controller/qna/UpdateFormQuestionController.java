package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateFormQuestionController extends AbstractController {
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        if (!UserSessionUtils.isLogined(session)) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        User user = UserSessionUtils.getUserFromSession(session);
        System.out.println(user);
        if (!question.isSameUser(user)) {
            return jspView("/qna/errorHandling.jsp").addObject("errorMessage", "다른 사용자가 쓴 글을 수정할 수 없습니다.");
//            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
        }
        return jspView("/qna/update.jsp").addObject("question", question);
    }
}
