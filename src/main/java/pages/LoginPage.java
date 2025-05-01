package pages;

import handlers.ElementWaits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    // Locators
    public By usernameField = By.id("user-name");
    public By passwordField = By.id("password");
    public By loginButton = By.id("login-button");
    public By loginErrorMessage = By.cssSelector("h3[data-test='error']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public ProductsPage clickLoginButton() {
        driver.findElement(loginButton).click();
        return new ProductsPage(driver);
    }

    public String getLoginErrorMessage() {
        return driver.findElement(loginErrorMessage).getText();
    }
    public void loginFunction(String username , String password){
        // Login
        ElementWaits waits = new ElementWaits();
        LoginPage loginPage =new LoginPage(driver);
        if (waits.waitforElementVisiablity(driver,loginPage.usernameField))
        {
            loginPage.enterUsername(username);
        }


        if (waits.waitforElementVisiablity(driver,loginPage.passwordField))
        {
            loginPage.enterPassword(password);
        }


        if (waits.waitforElementVisiablity(driver,loginPage.loginButton))
        {
            ProductsPage productpage = clickLoginButton();
        }

    }
}
