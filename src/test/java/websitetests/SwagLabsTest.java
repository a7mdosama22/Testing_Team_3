package websitetests;

import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import handlers.ElementWaits;
@Listeners({io.qameta.allure.testng.AllureTestNg.class, websitetests.TestListener.class})
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
        loginAndCheckProductsPage(username, password);

        addFirstItemIfVisible();

        moveToCartAndCheckTitle();

        doCheckoutSteps();

        checkOrderCompletion();
    }

    @Step("Login as {0} and check products page title")
    public void loginAndCheckProductsPage(String username, String password) {
        productsPage = loginPage.loginAs(username, password);
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Add first item to cart if add button visible")
    public void addFirstItemIfVisible() {
        if (waits.waitforElementVisiablity(driver, productsPage.addToCartButton)) {
            productsPage.addItemToCart();
            ScreenshotUtil.takeScreenshot(driver);
        }
    }

    @Step("Go to cart page and check title")
    public void moveToCartAndCheckTitle() {
        cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getCartTitle(), "Your Cart", "Cart title does not match");
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Perform checkout steps with dummy information")
    public void doCheckoutSteps() {
        checkoutStepOnePage = cartPage.clickCheckoutButton();
        checkoutStepOnePage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutStepTwoPage = checkoutStepOnePage.clickContinue();
        ScreenshotUtil.takeScreenshot(driver);
        checkoutCompletePage = checkoutStepTwoPage.clickFinish();
    }

    @Step("Verify thank you message after finishing checkout")
    public void checkOrderCompletion() {
        Assert.assertEquals(checkoutCompletePage.getThankYouMessage(),
                "THANK YOU FOR YOUR ORDER", "Checkout not completed properly");
        ScreenshotUtil.takeScreenshot(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // سكرين شوت تلقائي عند الفشل
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtil.takeScreenshot(driver);
        }
        super.tearDown();
    }

    @DataProvider(name = "loginData")
    public Object[][] LoginDataProvider (){
        return new Object[][] {
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user","secret_sauce"}
        };
    }
}