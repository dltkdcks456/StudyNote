package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.RequestService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final RequestService requestService = new RequestService();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            int contentLength = 0;

            // 첫 번째 Request 내용에서 type과 url 구분하기
            String content = br.readLine();
            log.debug("request line : {}", content);

            String[] tokens = content.split(" ");
            String type = tokens[0], url = tokens[1];
            byte[] body = "기본 데이터!!🥳".getBytes();

            switch (type) {
                case "GET":
                    if (url.equals("/user/list")) {
                        requestService.getUserListRequest(br, dos, url);
                    } else {
                        requestService.getGeneralRequest(content, br, url, body, dos);
                    }
                    break;
                case "POST":
                    if (url.equals("/user/login")) {
                        requestService.postLoginRequest(content, br, dos);
                    } else {
                        requestService.postRequest(content, contentLength, br, dos);
                    }
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
