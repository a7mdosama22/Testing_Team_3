package websitetests;
import handlers.ElementWaits ;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import java.util.List;


public class AddItemsToCartTest extends BaseTest{
    LoginPage loginPage;
    ProductsPage productsPage;
    CartPage cartPage;
    ElementWaits waits = new ElementWaits();
    @BeforeMethod
    public void setup(){
        super.setUp();
        loginPage = new LoginPage(driver);
    }
    @Test
    public void addItems (){
        // Login
        if (waits.waitforElementVisiablity(driver,loginPage.usernameField))
        {
            loginPage.enterUsername("standard_user");
        }


        if (waits.waitforElementVisiablity(driver,loginPage.passwordField))
        {
            loginPage.enterPassword("secret_sauce");
        }


        if (waits.waitforElementVisiablity(driver,loginPage.loginButton))
        {
            productsPage = loginPage.clickLoginButton();
        }

        // If the title on the new page is not "Products" , it means that the login failed.
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Login failed or incorrect page title");

        // Add all items to cart
        productsPage.addAllItems();

        // Navigate to Cart page
        cartPage = productsPage.goToCart();

        int productsCount =productsPage.getProductsCount();
        int cartItemsCount = cartPage.getCartCount();

        Assert.assertEquals(cartItemsCount, productsCount,
                "Number of items in cart doesn't match number of products added");

        List<String> productNames = productsPage.getAllProductNames();
        List<String> cartItemNames = cartPage.getAllCartItemNames();

        Assert.assertTrue(cartItemNames.containsAll(productNames),
                "Not all products are present in the cart");

    }
    @AfterMethod
    public void tearDown() {
        String filePath = "C:\\Users\\ms\\Desktop\\Programing\\DEPIR2-POM\\src\\test\\resources\\screenshot2.png" ;
        captureScreenshot(driver,filePath );
        super.tearDown();
    }
}
