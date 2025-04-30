package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutStepOnePage {
    WebDriver driver;

    // Locators
    By firstNameField = By.id("first-name");
    By lastNameField = By.id("last-name");
    By zipCodeField = By.id("postal-code");
    By continueButton = By.xpath("//input[@class='btn_primary cart_button']");

    // Constructor
    public CheckoutStepOnePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void fillCheckoutInfo(String firstName, String lastName, String zipCode) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(zipCodeField).sendKeys(zipCode);
    }

    public CheckoutStepTwoPage clickContinue() {
        driver.findElement(continueButton).click();
        return new CheckoutStepTwoPage(driver);
    }
}
