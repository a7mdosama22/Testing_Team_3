package websitetests;

import handlers.ElementWaits;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

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

    @Test
    public void testCompleteCheckoutFlow() {

        if (waits.waitforElementVisiablity(driver, loginPage.usernameField)) {
            loginPage.enterUsername("standard_user");
        }

        if (waits.waitforElementVisiablity(driver, loginPage.passwordField)) {
            loginPage.enterPassword("secret_sauce");
        }

        if (waits.waitforElementVisiablity(driver, loginPage.loginButton)) {
            productsPage = loginPage.clickLoginButton();
        }

        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");

        if (waits.waitforElementVisiablity(driver, productsPage.addToCartButton)) {
            productsPage.addItemToCart();
        }

        cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getCartTitle(), "Your Cart", "Cart title does not match");

        checkoutStepOnePage = cartPage.clickCheckoutButton();
        checkoutStepOnePage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutStepTwoPage = checkoutStepOnePage.clickContinue();

        checkoutCompletePage = checkoutStepTwoPage.clickFinish();
        Assert.assertEquals(checkoutCompletePage.getThankYouMessage(),
                "THANK YOU FOR YOUR ORDER", "Checkout not completed properly");
    }

    @AfterMethod
    public void tearDown() {
        String filePath = "C:\\Users\\ms\\Desktop\\Programing\\DEPIR2-POM\\src\\test\\resources\\screenshot.png" ;
        captureScreenshot(driver,filePath );
        super.tearDown();
    }
}