package websitetests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.time.Duration;
import java.util.HashMap;

public class SwagLabsTest {

    WebDriver driver;
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    CheckoutStepOnePage checkoutStepOnePage;
    CheckoutStepTwoPage checkoutStepTwoPage;
    CheckoutCompletePage checkoutCompletePage;

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.setExperimentalOption("prefs", new HashMap<String, Object>() {{
            put("credentials_enable_service", false);
            put("profile.password_manager_enabled", false);
        }});

        driver = new ChromeDriver(options);  // Updated with options
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/v1/index.html");

        loginPage = new LoginPage(driver);
    }

    @Test
    public void testCompleteCheckoutFlow() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        productsPage = loginPage.clickLoginButton();

        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");

        productsPage.addItemToCart();
        cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getCartTitle(), "Your Cart", "Cart title does not match");

        checkoutStepOnePage = cartPage.clickCheckoutButton();
        checkoutStepOnePage.fillCheckoutInfo("John", "Doe", "12345");
        checkoutStepTwoPage = checkoutStepOnePage.clickContinue();

        checkoutCompletePage = checkoutStepTwoPage.clickFinish();
        Assert.assertEquals(checkoutCompletePage.getThankYouMessage(), "THANK YOU FOR YOUR ORDER", "Checkout not completed properly");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
