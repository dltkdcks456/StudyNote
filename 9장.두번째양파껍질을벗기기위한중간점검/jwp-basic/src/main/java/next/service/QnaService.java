package next.service;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import java.util.List;

public class QnaService {
    private static final QnaService qnaService = new QnaService(QuestionDao.getInstance(), AnswerDao.getInstance());

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public static QnaService getInstance() {
        return qnaService;
    }

    public void deleteQuestion(long questionId, User user) throws CannotDeleteException {
        Question question = questionDao.findById(questionId);
        if (question == null) {
            throw new CannotDeleteException("존재하지 않는 질문입니다.");
        }

        if (!question.isSameUser(user)) {
            throw new CannotDeleteException("해당 게시글의 작성자가 아닙니다.");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        if (answers == null || answers.isEmpty()) {
            questionDao.delete(questionId);
            return;
        }

        boolean check = true;
        for (Answer answer : answers) {
            if (!answer.isSameUser(user)) {
                check = false;
                break;
            }
        }

        if (check) {
            questionDao.delete(questionId);
            answerDao.deleteAllByQuestionId(questionId);
            return;
        }

        throw new CannotDeleteException("해당 게시글은 다른 사용자의 답변이 존재하여 지울 수 없습니다.");
    }

}
