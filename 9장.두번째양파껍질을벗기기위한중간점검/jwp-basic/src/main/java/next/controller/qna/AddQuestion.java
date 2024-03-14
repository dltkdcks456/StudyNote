package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddQuestion extends AbstractController {
    static final Logger log = LoggerFactory.getLogger(AddQuestion.class);

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        log.info("{} {} {}", writer, title, contents);
        Question question = new Question(writer, title, contents);
        QuestionDao questionDao = new QuestionDao();
        questionDao.insert(question);
        return jspView("redirect:/");
    }
}
