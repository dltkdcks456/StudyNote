package next.controller.qna;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class DeleteAnswerController implements Controller {
    final static Logger log = LoggerFactory.getLogger(DeleteAnswerController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = req.getReader();
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        log.info("{}", sb);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sb.toString());
        long answerId = jsonNode.get("answerId").asLong();
        log.info("jsonNode : {}", jsonNode);
        log.info("answerId : {}", answerId);

        AnswerDao answerDao = new AnswerDao();
        answerDao.delete(answerId);

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(mapper.writeValueAsString(Result.ok()));

        return null;
    }
}
