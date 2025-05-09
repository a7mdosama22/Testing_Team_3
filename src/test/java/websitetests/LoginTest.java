package websitetests;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;



@Listeners({AllureTestNg.class})
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
        productsPage = Allure.step(
                "Login with username: " + username + " and password: " + password,
                () -> loginPage.loginAs(username, password)
        );

        if (isValid) {
            Allure.step("Check that Products page is opened after login", this::validateSuccessfulLogin);
        } else {
            Allure.step("Check error message for invalid login", this::validateUnsuccessfulLogin);
        }
    }

    public void validateSuccessfulLogin() {
        String actualTitle = productsPage.getPageTitle();
        Assert.assertEquals(actualTitle, "Products", "Login failed or incorrect landing page title.");
    }

    public void validateUnsuccessfulLogin() {
        WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Expected error message not displayed for invalid credentials.");
    }

    @AfterMethod
    public void tearDown() {
        AllureAttachments.screenshot(driver);
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