package websitetests;

import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import java.util.List;
@Listeners({io.qameta.allure.testng.AllureTestNg.class, websitetests.TestListener.class})
public class AddItemsToCartTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;

    @BeforeMethod
    public void setup() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void addItems(String username, String password) {
        loginAndNavigateToProducts(username, password);

        int productsCount = getProductsCountAndAddAll();

        int cartIconNum = productsPage.getCartItemCount();

        moveToCartPage();

        int cartItemsCount = cartPage.getCartCount();

        // Verify counts
        assertCartAndProductCounts(cartItemsCount, productsCount, cartIconNum);

        // Verify all products present in cart
        assertAllProductsPresent();
    }

    @Step("Login as {0} and navigate to Products page")
    public void loginAndNavigateToProducts(String username, String password) {
        productsPage = loginPage.loginAs(username, password);
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Get products count and add all items to cart")
    public int getProductsCountAndAddAll() {
        int productsCount = productsPage.getProductsCount();
        productsPage.addAllItems();
        ScreenshotUtil.takeScreenshot(driver);
        return productsCount;
    }

    @Step("Navigate to Cart page")
    public void moveToCartPage() {
        cartPage = productsPage.goToCart();
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Assert cart counts: cartItemsCount={0}, productsCount={1}, cartIconNum={2}")
    public void assertCartAndProductCounts(int cartItemsCount, int productsCount, int cartIconNum) {
        Assert.assertEquals(cartItemsCount, productsCount,
                "Number of items in cart doesn't match number of products added");

        Assert.assertEquals(cartIconNum, productsCount,
                "Cart icon number doesn't match number of products added");
    }

    @Step("Assert all product names are present in the cart")
    public void assertAllProductsPresent() {
        List<String> productNames = productsPage.getAllProductNames();
        List<String> cartItemNames = cartPage.getAllCartItemNames();

        Assert.assertTrue(cartItemNames.containsAll(productNames),
                "Not all products are present in the cart");
        ScreenshotUtil.takeScreenshot(driver);
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