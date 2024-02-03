package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;


public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public static void response302Header(DataOutputStream dos, String url, boolean cookie, boolean loginStatus) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            if (cookie) {
                String cookieData = checkLogin(loginStatus);
                dos.writeBytes(cookieData);
            }
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String cookie, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if (type.equals("CSS")) {
                dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            } else if (type.equals("JS")) {
                dos.writeBytes("Content-Type: application/javascript;charset=utf-8\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if (cookie != null) {
                dos.writeBytes("Set-Cookie:" + cookie + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static String checkLogin(boolean loginStatus) {
        if (loginStatus) {
            return "Set-Cookie: logined=true; Path=/\r\n";
        } else {
            return "Set-Cookie: logined=false; Path=/\r\n";
        }
    }
}
