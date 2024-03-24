package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class QuestionListController extends AbstractController {
    private final QuestionDao questionDao = QuestionDao.getInstance();
    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Question> questions = questionDao.findAll();
        return jsonView().addObject("questions", questions);
    }
}
