package pages;
import handlers.ElementWaits ;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductsPage {
    WebDriver driver;

    // Corrected locator (from 'title' to 'product_label')
    public By pageTitle = By.className("product_label");
    public By addToCartButton = By.xpath("//button[text()='ADD TO CART']");
    public By cartIcon = By.className("shopping_cart_link");
    public By allAddToCartButtons = By.cssSelector(".inventory_list .btn_inventory");
    // Constructor
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        return driver.findElement(pageTitle).getText();
    }

    public void addItemToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        driver.findElement(addToCartButton).click();
    }

    public CartPage goToCart() {
        driver.findElement(cartIcon).click();
        return new CartPage(driver);
    }

    public void addAllItems (){
        ElementWaits waits = new ElementWaits() ;
        waits.waitforElementVisiablity(driver,allAddToCartButtons);

        List<WebElement> buttons = driver.findElements(allAddToCartButtons);
        for(WebElement button : buttons) {
            if (button.isDisplayed()&& button.isEnabled()){
                button.click();
            }
        }
    }
    public int getProductsCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item")));
        System.out.println(driver.findElements(By.cssSelector(".inventory_item")).size());
        return driver.findElements(By.cssSelector(".inventory_item")).size();
    }

    public List<String> getAllProductNames() {
        List<WebElement> productNamesElements = driver.findElements(By.cssSelector(".inventory_item_name"));
        return productNamesElements.stream().map(WebElement::getText).toList();
    }

    public int getCartItemCount() {
        WebElement cartIconElement = driver.findElement(cartIcon);
        String cartCountText = cartIconElement.getText();

        // Parse cart count text to integer safely
        try {
            return Integer.parseInt(cartCountText);
        } catch (NumberFormatException e) {
            return 0; // return 0 indicating no count
        }
    }
}
