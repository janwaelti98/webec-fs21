package webeng.lengthconverter;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class LengthConverterIT {

    @LocalServerPort
    int port;

    @BeforeAll
    public static void setupClass() {
        // This test requires Firefox to be installed! To use a different
        // Browser, change the pom file, the line below, and all references
        // to FirefoxDriver accordingly.
        WebDriverManager.firefoxdriver().setup();
    }

    @Test
    void allFormFieldsPresent() {
        WebDriver driver = new FirefoxDriver();
        driver.navigate().to("http://localhost:" + port);

        var feetField = driver.findElements(By.id("feet"));
        assertEquals(1, feetField.size());
        assertEquals("feet", feetField.get(0).getAttribute("name"));

        var inchesField = driver.findElements(By.id("inches"));
        assertEquals(1, inchesField.size());
        assertEquals("inches", inchesField.get(0).getAttribute("name"));

        var submitButton = driver.findElements(By.cssSelector("input[type=submit]"));
        assertEquals(1, submitButton.size());
        assertEquals("Convert", submitButton.get(0).getAttribute("value"));
    }

    @Test
    void submittingFormConvertsCorrectly() {
        WebDriver driver = new FirefoxDriver();
        driver.navigate().to("http://localhost:" + port);

        var feetField = driver.findElement(By.id("feet"));
        feetField.sendKeys("5");

        var inchesField = driver.findElement(By.id("inches"));
        inchesField.sendKeys("10");

        var submitButton = driver.findElement(By.cssSelector("input[type=submit]"));
        submitButton.click();

        driver.manage().timeouts().implicitlyWait(3, SECONDS);

        assertEquals("177", driver.findElement(By.id("cm-part")).getText());
        assertEquals("8", driver.findElement(By.id("mm-part")).getText());
    }
}
