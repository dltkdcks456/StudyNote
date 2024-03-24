package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateQuestionController extends AbstractController {
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        questionDao.update(title, contents, questionId);
        return jspView("redirect:/qna/show?questionId=" + questionId);
    }
}
