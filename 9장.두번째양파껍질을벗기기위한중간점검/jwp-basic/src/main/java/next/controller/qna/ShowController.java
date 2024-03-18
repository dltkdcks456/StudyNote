package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowController extends AbstractController {
    private static Logger log = LoggerFactory.getLogger(ShowController.class);
    private QuestionDao questionDao;
    private AnswerDao answerDao;
    private Question question;
    private List<Answer> answers;

    public ShowController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
        Long questionId = Long.parseLong(req.getParameter("questionId"));
        question = questionDao.findById(questionId);
        answers = answerDao.findAllByQuestionId(questionId);
        for (Answer answer : answers) {
            log.info("questionId: {}, answer : {}", questionId, answer);
        }
        ModelAndView mav = jspView("/qna/show.jsp");
        mav.addObject("question", question);
        mav.addObject("answers", answers);
        return mav;
    }
}
