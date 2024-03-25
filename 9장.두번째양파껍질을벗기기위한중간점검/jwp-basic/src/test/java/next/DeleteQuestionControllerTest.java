package next;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.controller.UserSessionUtils;
import next.controller.qna.DeleteQuestionController;
import next.exception.CannotDeleteException;
import next.model.User;
import next.service.QnaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteQuestionControllerTest {

    DeleteQuestionController deleteQuestionController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession httpSession;

    @Mock
    private QnaService qnaService;

    private User user;

    @BeforeEach
    void setup() {
        deleteQuestionController = new DeleteQuestionController(qnaService);
        MockitoAnnotations.openMocks(this);
        user = new User("user", "password", "name", "email");
        httpSession = new MockHttpSession();
        httpSession.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
    }

    @Test
    public void testDeleteQuestionNotLoggedIn() throws Exception {
        // given
        httpSession = new MockHttpSession();
        when(request.getSession()).thenReturn(httpSession);
        when(request.getParameter("questionId")).thenReturn("1");

        // when
        ModelAndView mv = deleteQuestionController.execute(request, response);
        JspView view = (JspView) mv.getView();
        System.out.println("꺄호홍😁😁" + view.getViewName());

        // then
        Assertions.assertThat("redirect:/users/loginForm").isEqualTo(view.getViewName());
    }

    @Test
    public void DeleteQuestionSuccess() throws Exception {
        // given
        when(request.getParameter("questionId")).thenReturn("1");
        when(request.getSession()).thenReturn(httpSession);
//        doNothing().when(qnaService).deleteQuestion(1L, user);
        // when
        ModelAndView mv = deleteQuestionController.execute(request, response);
        JspView view = (JspView) mv.getView();
        System.out.println("요호호😁😁" + view.getViewName());
        // then
        Assertions.assertThat("redirect:/").isEqualTo(view.getViewName());
    }

    @Test
    public void throwDeleteQuestion() throws Exception {
        //given
        when(request.getParameter("questionId")).thenReturn("1");
        when(request.getSession()).thenReturn(httpSession);
        //when
        doThrow(CannotDeleteException.class).when(qnaService).deleteQuestion(1L, user);

        //then
        ModelAndView mv = deleteQuestionController.execute(request, response);
        JspView view = (JspView) mv.getView();
        System.out.println("설마!!😁😁" + view.getViewName());
        Assertions.assertThat("/qna/errorHandling.jsp").isEqualTo(view.getViewName());
    }

}
