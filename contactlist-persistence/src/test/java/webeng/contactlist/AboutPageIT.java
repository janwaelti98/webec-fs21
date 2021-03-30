package webeng.contactlist;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AboutPageIT {

    @LocalServerPort
    int port;

    @Test
    void fhnwLink() {
        var driver = new HtmlUnitDriver();
        driver.navigate().to("http://localhost:%d/about".formatted(port));
        var fhnwLink = driver.findElementByPartialLinkText("FHNW");
        assertEquals("https://www.fhnw.ch/de/studium/technik", fhnwLink.getAttribute("href"));
    }

    @Test
    void statsTable() {
        var driver = new HtmlUnitDriver();
        driver.navigate().to("http://localhost:%d/about".formatted(port));
        var tables = driver.findElementsByTagName("table");
        assertEquals(1, tables.size());
        var table = tables.get(0);
        assertEquals("30", table.findElement(By.id("contact-count")).getText());
        assertEquals("49", table.findElement(By.id("phone-number-count")).getText());
        assertEquals("45", table.findElement(By.id("email-count")).getText());
    }
}
