package websitetests;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;

import java.time.Duration;
@Listeners({io.qameta.allure.testng.AllureTestNg.class, websitetests.TestListener.class})
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
        productsPage = loginStep(username, password);

        closePopupIfExists();

        if (isValid) {
            validateSuccessfulLogin();
        } else {
            validateUnsuccessfulLogin();
        }
    }

    @Step("Login with username: {0} and password: {1}")
    public ProductsPage loginStep(String username, String password) {
        ProductsPage page = loginPage.loginAs(username, password);
        // توثيق صورة بعد الخطوة
        ScreenshotUtil.takeScreenshot(driver);
        return page;
    }

    public void closePopupIfExists() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Close']")));
            closePopup.click();
            System.out.println("Popup closed.");
        } catch (Exception e) {
            System.out.println("Popup not present or already closed.");
        }
    }

    @Step("Check that Products page is opened after login")
    public void validateSuccessfulLogin() {
        String actualTitle = productsPage.getPageTitle();
        Assert.assertEquals(actualTitle, "Products", "Login failed or incorrect landing page title.");
        // سكرين شوت للنجاح
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Check error message for invalid login")
    public void validateUnsuccessfulLogin() {
        WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Expected error message not displayed for invalid credentials.");
        // سكرين شوت للخطأ
        ScreenshotUtil.takeScreenshot(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // لقطة شاشة تلقائياً عند الفشل فقط
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtil.takeScreenshot(driver);
        }
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