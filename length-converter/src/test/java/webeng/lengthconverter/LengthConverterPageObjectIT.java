package webeng.lengthconverter;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class LengthConverterPageObjectIT {

    @LocalServerPort
    int port;

    @Test
    void allFormFieldsPresent() {
        WebDriver driver = new HtmlUnitDriver();
        var formPage = FormPage.create(driver, port);

        assertThat(formPage.getFeetField()).isPresent();
        assertEquals("feet", formPage.getFeetField().get().getAttribute("name"));

        assertThat(formPage.getInchesField()).isPresent();
        assertEquals("inches", formPage.getInchesField().get().getAttribute("name"));

        assertThat(formPage.getSubmitButton()).isPresent();
        assertEquals("Convert", formPage.getSubmitButton().get().getAttribute("value"));
    }
}
