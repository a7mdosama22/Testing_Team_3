package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    WebDriver driver;

    // Locators
    By cartTitle = By.className("subheader");
    By checkoutButton = By.xpath("//a[@class='btn_action checkout_button']");

    // Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public String getCartTitle() {
        return driver.findElement(cartTitle).getText();
    }

    public CheckoutStepOnePage clickCheckoutButton() {
        driver.findElement(checkoutButton).click();
        return new CheckoutStepOnePage(driver);
    }
}
