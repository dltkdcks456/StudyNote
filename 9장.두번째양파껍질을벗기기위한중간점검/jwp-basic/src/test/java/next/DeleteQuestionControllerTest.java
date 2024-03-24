package next;

import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.controller.qna.DeleteQuestionController;
import next.model.User;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteQuestionControllerTest {

    DeleteQuestionController deleteQuestionController;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession httpSession;

    @BeforeEach
    void setup() {
        deleteQuestionController = new DeleteQuestionController();
        MockitoAnnotations.openMocks(this);
        User user = new User("user", "password", "name", "email");
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

        // then
//        Assertions.assertThat("redirect:/users/loginForm").isEqualTo(mv.getView());

    }

}
