import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpMethod;
import webserver.request.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpRequestTest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final String testDirectory = "./src/test/resources/";

    @Test
    @DisplayName("GET 요청에 대한 처리")
    public void request_GET() throws IOException {
        File file = new File(testDirectory + "Http_GET.txt");
        InputStream in = new FileInputStream(file);
        HttpRequest httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getHeaders("Host")).isEqualTo("localhost:8080");
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeaders("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParams("userId")).isEqualTo("sc0825.lee");
    }

    @Test
    @DisplayName("POST 요청에 대한 처리")
    public void request_POST() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeaders("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParams("name")).isEqualTo("SangChan");

    }

}
