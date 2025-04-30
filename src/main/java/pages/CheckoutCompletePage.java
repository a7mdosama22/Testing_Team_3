package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage {
    WebDriver driver;

    // Locators
    By thankYouMessage = By.className("complete-header");

    // Constructor
    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public String getThankYouMessage() {
        return driver.findElement(thankYouMessage).getText();
    }
}
