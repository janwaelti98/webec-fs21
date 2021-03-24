package webeng.lengthconverter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Optional;

public class FormPage {

    private static final String BASE_URL = "http://localhost:";

    public static FormPage create(WebDriver driver, int port) {
        driver.get(BASE_URL + port + "/");
        return PageFactory.initElements(driver, FormPage.class);
    }

    @FindBy(id = "feet")
    private List<WebElement> feetField;
    @FindBy(id = "inches")
    private List<WebElement> inchesField;
    @FindBy(css = "input[type=submit]")
    private List<WebElement> submitButton;

    public Optional<WebElement> getFeetField() {
        return feetField.stream().findFirst();
    }

    public Optional<WebElement> getInchesField() {
        return inchesField.stream().findFirst();
    }

    public Optional<WebElement> getSubmitButton() {
        return submitButton.stream().findFirst();
    }
}
