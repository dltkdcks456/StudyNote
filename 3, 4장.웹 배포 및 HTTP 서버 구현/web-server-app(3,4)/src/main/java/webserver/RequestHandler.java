package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            boolean flag = true, checkPostMethod = false;
            int contentLength = 0;

            while (true) {
                String content = br.readLine();
                log.info("Input Data : {}", content);
                // 요청 첫 줄 추출 후 분석
                if (flag) {
                    log.debug("First line : {}", content);
                    String[] tokens = content.split(" ");
                    String url = Arrays.asList(tokens).get(1);

                    if (tokens[0].equals("POST")) {
                        log.info("Post 요청");
                        checkPostMethod = true;
                    }

                    if (!(url.equals("/") || checkPostMethod)) {
                        body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    }

                    flag = false;
                }

                // Content-Length 추출
                if (content != null && content.contains("Content-Length")) {
                    String[] tokens = content.split(" ");
                    contentLength = Integer.parseInt(tokens[1]);
                }

                // 빈 내용을 받을 경우 while문 정지
                if ((content == null || content.isEmpty())) {
                    break;
                }
            }

            // Post 요청 시 Body 데이터 추출
            if (checkPostMethod) {
                String readData = IOUtils.readData(br, contentLength);
                readData = URLDecoder.decode(readData, StandardCharsets.UTF_8);
                Map<String, String> userData = new HashMap<>();
                String[] data = readData.split("&");
                Arrays.stream(data)
                        .forEach(d -> {
                    String[] split = d.split("=");
                            userData.put(split[0], split[1]);
                });

                User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
                log.info("유저 탄생🥳 = {}", user);
                body = Files.readAllBytes(new File("./webapp/index.html").toPath());
                response300Header(dos, body.length);
                responseBody(dos, body);
            } else {
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response300Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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
