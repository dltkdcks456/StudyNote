package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Answer;

public class AddAnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    private final AnswerDao answerDao = AnswerDao.getInstance();
    private final QuestionDao questionDao = QuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(req.getParameter("questionId"));
        Answer answer = new Answer(req.getParameter("writer"), req.getParameter("contents"),
                questionId);
        log.debug("answer : {}", answer);
        questionDao.updateCountOfAnswer(questionId, 1);
        Question question = questionDao.findById(questionId);
        Answer savedAnswer = answerDao.insert(answer);
        return jsonView().addObject("answer", savedAnswer).addObject("countOfAnswer", question.getCountOfComment());
    }
}
