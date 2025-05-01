package websitetests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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

    @Test(dataProvider = "loginData")
    public void testCompleteCheckoutFlow(String username, String password) {

        loginPage.loginFunction(username, password);
        productsPage = new ProductsPage(driver);

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
    @DataProvider(name = "loginData")
    public Object[][] LoginDataProvider (){
        return new Object[][] {
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user","secret_sauce"}
        };
    }

}