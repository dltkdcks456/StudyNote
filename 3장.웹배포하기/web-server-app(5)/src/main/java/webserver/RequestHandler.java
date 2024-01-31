package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            int contentLength = 0;

            // 첫 번째 Request 내용에서 type과 url 구분하기
            String content = br.readLine();
            log.debug("First line : {}", content);

            String[] tokens = content.split(" ");
            String type = tokens[0];
            String url = tokens[1];

            switch (type) {
                case "GET":
                    getGeneralRequest(content, br, url, body, dos);
                    break;
                case "POST":
                    if (url.equals("/user/login")) {
                        postLoginRequest(content, br, dos);
                    } else {
                        postRequest(content, contentLength, br, dos);
                    }
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void postLoginRequest(String content, BufferedReader br, DataOutputStream dos) throws IOException {
        int contentLength = 0;
        while (true) {
            log.info("Input Data : {}", content);
            content = br.readLine();

            // Content-Length 추출
            if (content != null && content.contains("Content-Length")) {
                String[] tokens = content.split(" ");
                contentLength = Integer.parseInt(tokens[1]);
            }

            if (whileConditionCheck(content)) break;
        }

        Map<String, String> userData = new HashMap<>();
        String readData = IOUtils.readData(br, contentLength);
        String[] data = readData.split("&");
        Arrays.stream(data)
                .forEach(d -> {
                    String[] tokens = d.split("=");
                    userData.put(tokens[0], tokens[1]);
                });
        User user = DataBase.findUserById(userData.get("userId"));
        log.info("login form data: {}📌", readData);
        if (user == null || !user.getUserId().equals(userData.get("userId")) || !user.getPassword().equals(userData.get("password"))) {
            // 로그인 실패
            log.info("로그인 실패!!");
            byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
            response401LoginFailHeader(dos, body.length);
            responseBody(dos, body);
        } else {
            // 로그인 성공
            // body에는 내용이 없어도 되나?
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response302LoginSuccessHeader(dos);
        }
    }

    private void getGeneralRequest(String content, BufferedReader br, String url, byte[] body, DataOutputStream dos) throws IOException {
        while (true) {
            log.info("Input Data : {}", content);
            content = br.readLine();
            // URL을 body로 전달
            if (!url.equals("/")) {
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
            }

            if (whileConditionCheck(content)) break;
        }
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private static boolean whileConditionCheck(String content) {
        // 빈 내용을 받을 경우 while문 정지
        if ((content == null || content.isEmpty())) {
            return true;
        }
        return false;
    }

    private void postRequest(String content, int contentLength, BufferedReader br, DataOutputStream dos) throws IOException {
        while (true) {
            log.info("Input Data : {}", content);
            content = br.readLine();

            // Content-Length 추출
            if (content != null && content.contains("Content-Length")) {
                String[] tokens = content.split(" ");
                contentLength = Integer.parseInt(tokens[1]);
            }
            // 빈 내용을 받을 경우 while문 정지
            if (whileConditionCheck(content)) break;
        }

        // Post 요청 시 Body 데이터 추출
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
        DataBase.addUser(user);
        log.info("⭐유저 저장 완료⭐");
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        String redirectUrl = "http://localhost:8080/index.html";
        response302Header(dos, body.length, redirectUrl);
    }

    private void response302Header(DataOutputStream dos, int lengthOfBodyContent, String newUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + newUrl + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
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

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=true\r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response401LoginFailHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 401 Unauthorized \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined=false\r\n");
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
