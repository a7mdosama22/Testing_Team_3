package websitetests;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;

import java.util.List;

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

    productsPage = loginPage.loginAs(username, password);

    // Press all buttons
    productsPage.addAllItems();

    //  Check all buttons are pressed ...
    List<WebElement> buttons = driver.findElements(productsPage.addToCartButton);
    for (WebElement button : buttons){
        // Store the button text before clicking
        String buttonTextBefore = button.getText();
        // Click the Add to Cart button
        button.click();

        // Verify the button text has changed
        String buttonTextAfter = button.getText();
        Assert.assertNotEquals(buttonTextBefore, buttonTextAfter, "Button text did not change after click");

        // Expectation: "Add to cart" changes to "Remove" or similar
        Assert.assertEquals(buttonTextAfter, "Remove", "Button text did not change to 'Remove'");

        }
    // check productsCount and cart icon num
    int cartIconNum =productsPage.getCartItemCount() ;
    int productsCount = productsPage.getProductsCount();

        Assert.assertEquals(cartIconNum, productsCount,
                "cartIconNum doesn't match number of products added");

    }
    @AfterMethod
    public void tearDown() {
        String filePath = "C:\\Users\\ms\\Desktop\\Programing\\DEPIR2-POM\\src\\test\\resources\\screenshot3.png";
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
