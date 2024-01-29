package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

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

            byte[] body = "재배포 완료!!🥳".getBytes();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            boolean flag = true;
            while (true) {
                String content = br.readLine();
                log.info("Input Data : {}", content);

                if (content == null || content.equals("")) {
                    break;
                }

                if (flag) {
                    log.debug("request line : {}", content);
                    String[] tokens = content.split(" ");
                    String url = Arrays.asList(tokens).get(1);
                    if (url.startsWith("/user/create")) {
                        Map<String, String> userData = new HashMap<>();

                        Map<String, String> stringStringMap = HttpRequestUtils.parseQueryString(url);
                        for(Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (key.contains("/user/create?")) {
                                String[] temp = key.split("\\?");
                                key = temp[1];
                            }
                            userData.put(key, value);
                        }

                        User user = new User(userData.get("userId"),
                                userData.get("password"),
                                userData.get("name"),
                                userData.get("email"));
                        log.info("###😁유저 탄생 : {}", user);

                    } else if (!url.equals("/")) {
                        body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    }
                    flag = false;
                }
            }
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
