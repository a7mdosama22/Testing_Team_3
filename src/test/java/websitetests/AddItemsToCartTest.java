package websitetests;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.util.List;

@Listeners({AllureTestNg.class})
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
        Allure.step("Login as user: " + username, () -> {
            productsPage = loginPage.loginAs(username, password);
        });

        Allure.step("Verify we are on the Products page", () -> {
            Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");
        });

        int productsCount = Allure.step("Add all products to the cart and get products count", () -> {
            int count = productsPage.getProductsCount();
            productsPage.addAllItems();
            return count;
        });

        int cartIconNum = Allure.step("Get cart icon item count", () -> productsPage.getCartItemCount());

        Allure.step("Navigate to cart page", () -> {
            cartPage = productsPage.goToCart();
        });

        int cartItemsCount = Allure.step("Get item count from cart page", () -> cartPage.getCartCount());

        Allure.step("Verify cart counts match the number of products added", () -> {
            Assert.assertEquals(cartItemsCount, productsCount, "Number of items in cart doesn't match number of products added");
            Assert.assertEquals(cartIconNum, productsCount, "Cart icon number doesn't match number of products added");
        });

        Allure.step("Verify all added products are present in the cart", () -> {
            List<String> productNames = productsPage.getAllProductNames();
            List<String> cartItemNames = cartPage.getAllCartItemNames();

            Assert.assertTrue(cartItemNames.containsAll(productNames), "Not all products are present in the cart");
        });
    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            AllureAttachments.screenshot(driver);
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