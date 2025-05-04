package websitetests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import java.util.List;
import java.util.ArrayList;
import java.time.Duration;

public class SyncProductsTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;

    class ProductData {
        String name;
        String price;
        String buttonText;
        String href;
        ProductData(String name, String price, String buttonText, String href) {
            this.name = name;
            this.price = price;
            this.buttonText = buttonText;
            this.href = href;
        }
    }

    @BeforeMethod
    public void setup() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void addItems(String username, String password) {
        productsPage = loginPage.loginAs(username, password);


        productsPage.addAllItems();

        // Collect product data from the product page
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        List<ProductData> allProducts = new ArrayList<>();
        for (WebElement item : items) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            String price = item.findElement(By.cssSelector(".inventory_item_price")).getText();
            String button = item.findElement(By.cssSelector(".btn_inventory")).getText();
            String href = item.findElement(By.cssSelector("a[href^='./inventory-item.html']")).getAttribute("href");
            allProducts.add(new ProductData(name, price, button, href));
        }


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for (ProductData product : allProducts) {
            driver.get(product.href);

            WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
            WebElement productPrice = driver.findElement(By.className("inventory_details_price"));
            WebElement detailButton = driver.findElement(By.cssSelector(".btn_inventory"));

            Assert.assertEquals(productTitle.getText(), product.name, "The product name is not the same");
            Assert.assertEquals(productPrice.getText(), product.price, "The price of the product is not the same");
            Assert.assertEquals(detailButton.getText().trim().toLowerCase(), product.buttonText.trim().toLowerCase(), "The listproduct button is different from the details button.");

            // Return to the product list page
            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item")));
        }
    }

    @AfterMethod
    public void tearDown() {
        String filePath = "src/test/resources/screenshot5.png";
        captureScreenshot(driver, filePath);
        super.tearDown();
    }

    @DataProvider(name = "loginData")
    public Object[][] LoginDataProvider() {
        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"}
        };
    }
}