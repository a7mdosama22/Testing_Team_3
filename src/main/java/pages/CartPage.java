package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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

    public int getCartCount() {
        return driver.findElements(By.cssSelector(".cart_item")).size();

    }

    public List<String> getAllCartItemNames() {
        List<WebElement> allCartItemNames =  driver.findElements(By.cssSelector(".inventory_item_name"));
        return allCartItemNames.stream().map(WebElement::getText).toList();
    }
}
