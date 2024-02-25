import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpSession;

import java.util.Enumeration;

public class SessionTest {
    Logger log = LoggerFactory.getLogger(SessionTest.class);
    @Test
    public void Session() {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("logined", true);
        Enumeration<String> attributeNames = session.getAttributeNames();
        while(attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            log.info("attributeName : {}, Value: {}", attributeName, session.getAttribute(attributeName));
        }
    }
}
