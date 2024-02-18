package net.slipp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// 서블릿으로 톰캣서버가 인식하도록 하려면 HttpServlet을 상속 받아야한다.
@WebServlet("/hello")
public class HelloWroldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        // 정적인 웨페이지가 아닌 자바 코드를 통해 동적인 웹 페이지를 만들 수 있다.
        out.print("<h1>Hello World!</h1>");
    }
}
