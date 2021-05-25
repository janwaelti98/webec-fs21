package webeng.contactlist.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Page {

    private static final String URL = "http://localhost:%d%s";

    public static void login(WebDriver driver, int port, String username, String password) {
        driver.get(URL.formatted(port, "/login"));
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type=submit]")).submit();
    }

    public static Page create(WebDriver driver, int port, String path) {
        driver.get(URL.formatted(port, path));
        return PageFactory.initElements(driver, Page.class);
    }

    @FindBy(css = "header nav a")
    private List<WebElement> menuLinks;

    public List<WebElement> getMenuLinks() {
        return menuLinks;
    }
}
