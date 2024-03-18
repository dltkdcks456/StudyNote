package next;

import next.controller.qna.ShowController;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiThreadTest {
    @Test
    public void multiThreadTest() throws Exception {

        QuestionDao questionDaoMock = mock(QuestionDao.class);
        AnswerDao answerDaoMock = mock(AnswerDao.class);

        Question mockQuestionA = new Question("상찬", "코딩", "공부");
        Question mockQuestionB = new Question("초코", "간식", "먹자");

        List<Answer> answerListA = Arrays.asList(new Answer("상찬", "공부 실패", 0));
        List<Answer> answerListB = Arrays.asList(new Answer("초코", "비만 성공", 1));

        when(questionDaoMock.findById(0)).thenReturn(mockQuestionA);
        when(questionDaoMock.findById(1)).thenReturn(mockQuestionB);
        when(answerDaoMock.findAllByQuestionId(0)).thenReturn(answerListA);
        when(answerDaoMock.findAllByQuestionId(1)).thenReturn(answerListB);

        final ShowController controller = new ShowController(questionDaoMock, answerDaoMock);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 100; i++) {
            int questionId = i % 2;
            executor.submit(() -> {
                try {
                    HttpServletRequest request = mock(HttpServletRequest.class);
                    HttpServletResponse response = mock(HttpServletResponse.class);

                    when(request.getParameter("questionId")).thenReturn(String.valueOf(questionId));

                    controller.execute(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

    }
}
