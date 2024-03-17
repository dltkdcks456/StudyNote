package core.mvc;

import next.controller.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ForwardController extends AbstractController {
    private String forwardUrl;
    private static Logger log = LoggerFactory.getLogger(ForwardController.class);
    public ForwardController(String forwardUrl) {
        if (forwardUrl == null) {
            throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
        }
        this.forwardUrl = forwardUrl;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/qna/form") && !UserSessionUtils.isLogined(session)) {
            log.info("😥😥😥");
            return jspView("redirect:/users/loginForm");
        }
        return jspView(forwardUrl);
    }

}
