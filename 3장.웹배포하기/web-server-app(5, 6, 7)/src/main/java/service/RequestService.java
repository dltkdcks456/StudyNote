package service;

import com.google.common.base.Strings;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static response.HttpResponse.*;
import static response.HttpResponse.response302Header;

public class RequestService {
    private static final Logger log = LoggerFactory.getLogger(RequestService.class);

    public void getUserListRequest(BufferedReader br, DataOutputStream dos, String url) throws IOException {
        boolean status = checkLoginStatus(br);
        if (status) {
//            response302Header(dos, "/user/list.html", true, true);
            Collection<User> users =DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("<table border='1'>");
            for (User user : users) {
                sb.append("<tr>");
                sb.append("<td>").append(user.getUserId()).append("</td>");
                sb.append("<td>").append(user.getName()).append("</td>");
                sb.append("<td>").append(user.getEmail()).append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            byte[] body = sb.toString().getBytes();
            String type = checkType(url);
            response200Header(dos, body.length, null, type);
            responseBody(dos, body);
        } else {
            response302Header(dos, "/user/login.html", true, false);
        }
    }

    public void getGeneralRequest(String content, BufferedReader br, String url, byte[] body, DataOutputStream dos) throws IOException {
        String cookie = null;
        String type = checkType(url);

        if (!url.equals("/")) {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
        }

        while (true) {
            content = br.readLine();
            log.info("Input Data : {}", content);
            // URL을 body로 전달


            if (content.startsWith("Cookie")) {
                cookie = HttpRequestUtils.parseCookies(content).get("Cookie");
                log.info("cookie===> {}", cookie);
            }

            if (whileConditionCheck(content)) break;
        }
        response200Header(dos, body.length, cookie, type);
        responseBody(dos, body);
    }

    public void postRequest(String content, int contentLength, BufferedReader br, DataOutputStream dos) throws IOException {
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

        // String To Object
        User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
        DataBase.addUser(user);

        log.info("유저 탄생🥳 = {}", user);
        log.info("⭐유저 저장 완료⭐");

        response302Header(dos, "http://localhost:8080/index.html", false, false);
    }

    public void postLoginRequest(String content, BufferedReader br, DataOutputStream dos) throws IOException {
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

        // Request Body 파싱 작업
        Map<String, String> userData = new HashMap<>();
        String readData = IOUtils.readData(br, contentLength);
        String[] data = readData.split("&");
        Arrays.stream(data)
                .forEach(d -> {
                    String[] tokens = d.split("=");
                    userData.put(tokens[0], tokens[1]);
                });

        // DabaBase에 있는 유저 확인
        User user = DataBase.findUserById(userData.get("userId"));
        log.info("login form data: {}📌", readData);

        if (user == null || !user.getUserId().equals(userData.get("userId")) || !user.getPassword().equals(userData.get("password"))) {
            // 로그인 실패
            response302Header(dos, "/user/login_failed.html", true, false);
        } else {
            // 로그인 성공
            response302Header(dos, "/index.html", true, true);
        }
    }

    private String checkType(String url) {
        if (url.contains("css")) {
            return "CSS";
        } else if (url.contains("js")) {
            return "JS";
        } else {
            return "HTML";
        }
    }

    private static boolean whileConditionCheck(String content) {
        // 빈 내용을 받을 경우 while문 정지
        if ((content == null || content.isEmpty())) {
            return true;
        }
        return false;
    }


    private static boolean checkLoginStatus(BufferedReader br) throws IOException {
        String content;
        boolean status = false;

        // Cookie값을 통해 로그인 상태 확인
        while (true) {
            content = br.readLine();
            log.info("### input Data: {}", content);

            if (content.startsWith("Cookie")) {
                String[] parseCookie = content.split(":");
                Map<String, String> checkLogin = HttpRequestUtils.parseCookies(parseCookie[1]);
                String login = checkLogin.get("logined");
                status = Boolean.parseBoolean(login);
            }

            if (Strings.isNullOrEmpty(content)) {
                break;
            }
        }
        return status;
    }
}
