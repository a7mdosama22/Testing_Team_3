package websitetests;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;

import java.util.List;
@Listeners({io.qameta.allure.testng.AllureTestNg.class, websitetests.TestListener.class})
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
        loginToSite(username, password);
        pressAllAddToCartButtons();
        validateCartCountAndIcon();
    }

    @Step("Login as {0}")
    public void loginToSite(String username, String password) {
        productsPage = loginPage.loginAs(username, password);
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Click every Add to cart button and check it turns to Remove")
    public void pressAllAddToCartButtons() {
        // Press all buttons (يفترض أنهم كلهم في حالة "Add to cart" في البداية)
        productsPage.addAllItems();
        ScreenshotUtil.takeScreenshot(driver);

        // Check all buttons are turned to Remove
        List<WebElement> buttons = driver.findElements(productsPage.addToCartButton);
        for (WebElement button : buttons){
            String buttonTextBefore = button.getText();
            button.click();
            String buttonTextAfter = button.getText();
            Assert.assertNotEquals(buttonTextBefore, buttonTextAfter, "Button text did not change after click");
            Assert.assertEquals(buttonTextAfter, "Remove", "Button text did not change to 'Remove'");
        }
        ScreenshotUtil.takeScreenshot(driver);
    }

    @Step("Check cart icon number matches products count")
    public void validateCartCountAndIcon() {
        int cartIconNum = productsPage.getCartItemCount();
        int productsCount = productsPage.getProductsCount();
        Assert.assertEquals(cartIconNum, productsCount,
                "cartIconNum doesn't match number of products added");
        ScreenshotUtil.takeScreenshot(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtil.takeScreenshot(driver);
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