package websitetests;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.*;
import handlers.ElementWaits;
import org.testng.Assert;
import pages.*;

@Listeners({AllureTestNg.class})
public class SwagLabsTest extends BaseTest {

    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutStepOnePage checkoutStepOnePage;
    CheckoutStepTwoPage checkoutStepTwoPage;
    CheckoutCompletePage checkoutCompletePage;
    ElementWaits waits = new ElementWaits();

    @BeforeMethod
    public void setup() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void testCompleteCheckoutFlow(String username, String password) {
        Allure.step("Login as user: " + username, () -> {
            productsPage = loginPage.loginAs(username, password);
        });

        Allure.step("Verify that we are on the Products page", () -> {
            Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");
        });

        Allure.step("Add first product to cart if 'Add to cart' button is visible", () -> {
            if (waits.waitforElementVisiablity(driver, productsPage.addToCartButton)) {
                productsPage.addItemToCart();
            }
        });

        Allure.step("Go to cart page and verify cart title", () -> {
            cartPage = productsPage.goToCart();
            Assert.assertEquals(cartPage.getCartTitle(), "Your Cart", "Cart title does not match");
        });

        Allure.step("Fill in checkout information", () -> {
            checkoutStepOnePage = cartPage.clickCheckoutButton();
            checkoutStepOnePage.fillCheckoutInfo("John", "Doe", "12345");
            checkoutStepTwoPage = checkoutStepOnePage.clickContinue();
        });

        Allure.step("Finish checkout and verify thank you message", () -> {
            checkoutCompletePage = checkoutStepTwoPage.clickFinish();
            Assert.assertEquals(checkoutCompletePage.getThankYouMessage(),
                    "THANK YOU FOR YOUR ORDER", "Checkout not completed properly");
        });
    }

    @AfterMethod
    public void tearDown() {
        String filePath = "src/test/resources/screenshot.png";
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