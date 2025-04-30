package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepTwoPage {
    WebDriver driver;

    // Locators
    By finishButton = By.xpath("//a[@class='btn_action cart_button']");

    // Constructor
    public CheckoutStepTwoPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public CheckoutCompletePage clickFinish() {
        driver.findElement(finishButton).click();
        return new CheckoutCompletePage(driver);
    }
}
