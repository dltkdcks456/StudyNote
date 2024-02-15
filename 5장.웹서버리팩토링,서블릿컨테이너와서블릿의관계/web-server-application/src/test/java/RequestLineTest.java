import org.assertj.core.api.Assertions;
import org.junit.Test;
import webserver.RequestLine;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLineTest {

    @Test
    public void create_method() {
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(line.getMethod()).isEqualTo("GET");
        assertThat(line.getPath()).isEqualTo("/index.html");
    }

    @Test
    public void create_path_params() {
        RequestLine line = new RequestLine("GET /user/create?userId=sc0825.lee&password=password&name=sangchan HTTP/1.1");
        assertThat(line.getMethod()).isEqualTo("GET");
        assertThat(line.getPath()).isEqualTo("/user/create");
        assertThat(line.getParams().get("userId")).isEqualTo("sc0825.lee");
        assertThat(line.getParams().size()).isEqualTo(3);
    }
}
