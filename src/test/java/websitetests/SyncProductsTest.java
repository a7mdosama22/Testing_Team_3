package websitetests;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.util.List;
import java.util.ArrayList;
import java.time.Duration;
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
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
        loginAndAddAllItems(username, password);

        List<ProductData> allProducts = collectProductsData();

        checkEachProductDetails(allProducts);
    }

    @Step("Login as user: {0} and add all items to cart")
    public void loginAndAddAllItems(String username, String password) {
        productsPage = loginPage.loginAs(username, password);
        ScreenshotUtil.takeScreenshot(driver); // بعد تسجيل الدخول

        productsPage.addAllItems();
        ScreenshotUtil.takeScreenshot(driver); // بعد إضافة جميع المنتجات
    }

    @Step("Collect all products data in the listing page")
    public List<ProductData> collectProductsData() {
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        List<ProductData> allProducts = new ArrayList<>();
        for (WebElement item : items) {
            String name = item.findElement(By.cssSelector(".inventory_item_name")).getText();
            String price = item.findElement(By.cssSelector(".inventory_item_price")).getText();
            String button = item.findElement(By.cssSelector(".btn_inventory")).getText();
            String href = item.findElement(By.cssSelector("a[href^='./inventory-item.html']")).getAttribute("href");
            allProducts.add(new ProductData(name, price, button, href));
        }
        ScreenshotUtil.takeScreenshot(driver); // بعد جمع المنتجات
        return allProducts;
    }

    @Step("Check product details for each item")
    public void checkEachProductDetails(List<ProductData> allProducts) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for (ProductData product : allProducts) {
            driver.get(product.href);

            WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
            WebElement productPrice = driver.findElement(By.className("inventory_details_price"));
            WebElement detailButton = driver.findElement(By.cssSelector(".btn_inventory"));

            assertProductData(product, productTitle, productPrice, detailButton);

            // Return to the product list page
            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".inventory_item")));
        }
    }

    @Step("Assert product data matches in details page")
    public void assertProductData(ProductData product, WebElement productTitle, WebElement productPrice, WebElement detailButton) {
        Assert.assertEquals(productTitle.getText(), product.name, "The product name is not the same");
        Assert.assertEquals(productPrice.getText(), product.price, "The price of the product is not the same");
        Assert.assertEquals(detailButton.getText().trim().toLowerCase(), product.buttonText.trim().toLowerCase(), "The listproduct button is different from the details button.");
        ScreenshotUtil.takeScreenshot(driver); // بعد التحقق من المنتج في التفاصيل
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // سكرين شوت تلقائي عند الفشل فقط
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtil.takeScreenshot(driver);
        }
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