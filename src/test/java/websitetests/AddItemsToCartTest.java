package websitetests;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import java.util.List;

public class AddItemsToCartTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;


    @BeforeMethod
    public void setup() {
        super.setUp();
        loginPage = new LoginPage(driver);
    }

    @Test(dataProvider = "loginData")
    public void addItems(String username, String password) {
        // This should return a ProductsPage object if successful
        loginPage.loginFunction(username, password);

        // Initialize ProductsPage after login
        productsPage = new ProductsPage(driver);

        // Check if we successfully navigated to the Products page
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");

        // Add all items to cart
        productsPage.addAllItems();

        // Navigate to Cart page
        cartPage = productsPage.goToCart();

        int productsCount = productsPage.getProductsCount();
        int cartItemsCount = cartPage.getCartCount();
        int cartIconNum = productsPage.getCartItemCount();

        // Verify count from cart page and icon equal the products count
        Assert.assertEquals(cartItemsCount, productsCount,
                "Number of items in cart doesn't match number of products added");

        Assert.assertEquals(cartIconNum, productsCount,
                "Cart icon number doesn't match number of products added");

        List<String> productNames = productsPage.getAllProductNames();
        List<String> cartItemNames = cartPage.getAllCartItemNames();

        Assert.assertTrue(cartItemNames.containsAll(productNames),
                "Not all products are present in the cart");
    }

    @AfterMethod
    public void tearDown() {
        String filePath = "C:\\Users\\ms\\Desktop\\Programing\\DEPIR2-POM\\src\\test\\resources\\screenshot2.png";
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