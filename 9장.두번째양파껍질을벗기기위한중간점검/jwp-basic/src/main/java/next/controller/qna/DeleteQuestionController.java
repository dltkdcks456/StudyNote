package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.exception.CannotDeleteException;
import next.model.User;
import next.service.QnaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteQuestionController extends AbstractController {
    private final QnaService qnaService = QnaService.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User user = UserSessionUtils.getUserFromSession(session);

        if (!UserSessionUtils.isLogined(session)) {
            return jspView("redirect:/users/loginForm");
        }

        try {
            qnaService.deleteQuestion(questionId, user);
            return jspView("redirect:/");
        } catch (CannotDeleteException e) {
            return jspView("/qna/errorHandling.jsp").addObject("errorMessage", e.getMessage());
        }
    }
}
