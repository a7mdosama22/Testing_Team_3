package websitetests;
import org.testng.ITestResult;
import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.util.List;

@Listeners({AllureTestNg.class})
public class AddButtonTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void addButtonFunction(String username, String password) {
        Allure.step("Login as user: " + username, () -> {
            productsPage = loginPage.loginAs(username, password);
        });

        Allure.step("Press all 'Add to cart' buttons and verify they change to 'Remove'", () -> {
            List<WebElement> buttons = driver.findElements(productsPage.addToCartButton);
            for (WebElement button : buttons) {
                String buttonTextBefore = button.getText();
                button.click();
                String buttonTextAfter = button.getText();

                Allure.step("Verify button text changed after clicking", () -> {
                    Assert.assertNotEquals(buttonTextBefore, buttonTextAfter, "Button text did not change after click");
                    Assert.assertTrue(
                            buttonTextAfter.equalsIgnoreCase("Remove"),
                            "Button text did not change to 'Remove' (case-insensitive check)"
                    );
                });
            }
        });

        Allure.step("Verify that the cart icon count matches the number of products", () -> {
            int cartIconNum = productsPage.getCartItemCount();
            int productsCount = productsPage.getProductsCount();

            Assert.assertEquals(cartIconNum, productsCount,
                    "Cart icon number doesn't match number of products added");
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