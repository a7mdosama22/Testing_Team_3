package websitetests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;

import java.time.Duration;

public class LoginTest extends BaseTest {

    LoginPage loginPage;
    ProductsPage productsPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void loginValidationTest(String username, String password, boolean isValid) {
        productsPage = loginPage.loginAs(username, password);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Close']")));
            closePopup.click();
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("Popup not present or already closed.");
        }

        if (isValid) {
            String actualTitle = productsPage.getPageTitle();
            Assert.assertEquals(actualTitle, "Products", "Login failed or incorrect landing page title.");
        } else {
            WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
            Assert.assertTrue(errorMessage.isDisplayed(), "Expected error message not displayed for invalid credentials.");
        }
    }

    @AfterMethod
    public void tearDown() {
        String filePath = "src/test/resources/login_screenshot.png";
        captureScreenshot(driver, filePath);
        super.tearDown();
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"performance_glitch_user", "secret_sauce", true},
                {"problem_user", "secret_sauce", true},
                {"", "", false},
                {"invalid_user", "wrong_pass", false},
                {"standard_user", "", false},
                {"", "secret_sauce", false}
        };
    }
}
