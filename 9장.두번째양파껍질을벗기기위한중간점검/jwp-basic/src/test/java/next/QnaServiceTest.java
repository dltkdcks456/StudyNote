package next;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import next.service.QnaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QnaServiceTest {

    @Mock
    private QuestionDao questionDao;

    @Mock
    private AnswerDao answerDao;

    private QnaService qnaService;

    @BeforeEach
    public void setup() {
        qnaService = new QnaService(questionDao, answerDao);
    }

    @Test
    void 질문이_존재하지_않을_경우() {
        // given
        User user = Mockito.mock(User.class);
        // when
        when(questionDao.findById(anyLong())).thenReturn(null);

        // then
        Assertions.assertThrows(CannotDeleteException.class, () -> qnaService.deleteQuestion(1L, user));
    }

    @Test
    void 게시글의_제작자가_아닌_경우() {
        //given
        Question question = new Question("user", "title", "contents");
        User user = new User("user1", "password", "name", "email");
        //when
        when(questionDao.findById(anyLong())).thenReturn(question);

        //then
        Assertions.assertThrows(CannotDeleteException.class, () -> qnaService.deleteQuestion(1L, user));
    }

    @Test
    void 답변이_비었을_경우() throws CannotDeleteException {
        //given
        long questionId = 1L;
        Question question = new Question("user", "title", "contents");
        User user = new User("user", "password", "name", "email");
        //when
        when(questionDao.findById(questionId)).thenReturn(question);
        when(answerDao.findAllByQuestionId(questionId)).thenReturn(null);
        qnaService.deleteQuestion(1L, user);
        // then
        verify(questionDao).delete(questionId);
        verify(answerDao, never()).deleteAllByQuestionId(questionId);
    }
}
