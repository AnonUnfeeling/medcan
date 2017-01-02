package ua.softgroup.medreview.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.softgroup.medreview.web.config.WebMvcConfig;

import java.net.URL;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HtmlUnitTest {

    @Autowired
    private WebApplicationContext wac;

    private WebClient webClient;

    @Before
    public void setup() {
        webClient = MockMvcWebClientBuilder.webAppContextSetup(wac).build();
    }

    @Test
    public void givenAMessage_whenSent_thenItShows() throws Exception {
        String text = "Hello world!xxxxx";
        HtmlPage page;

        String url = "http://localhost/";
        WebRequest requestSettings = new WebRequest(new URL(url));
        ArrayList<NameValuePair> requestParameters = new ArrayList<>();
        requestParameters.add(new NameValuePair("username","user"));
        requestParameters.add(new NameValuePair("password","123"));
        requestSettings.setRequestParameters(requestParameters);
        requestSettings.setAdditionalHeader("X-CSRF-Token", "Fetch");
        page = webClient.getPage(requestSettings);

        HtmlTextInput messageText = page.getHtmlElementById("message");
        messageText.setValueAttribute(text);

        HtmlForm form = page.getForms().get(0);
        HtmlSubmitInput submit = form.getOneHtmlElementByAttribute(
                "input", "type", "submit");
        HtmlPage newPage = submit.click();

        String receivedText = newPage.getHtmlElementById("received")
                .getTextContent();

        Assert.assertEquals(receivedText, text);
    }

}
