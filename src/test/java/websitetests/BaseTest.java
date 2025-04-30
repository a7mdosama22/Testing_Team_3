package websitetests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BaseTest {
    WebDriver driver;

    @BeforeClass
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-popup");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/login");
    }
    //Screenshot method
    public static void captureScreenshot(WebDriver driver, String filePath) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(screenshot.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot to " + filePath, e);
        }
    }

    @AfterClass
    public void tearDown(){
//        if(driver!=null)
//        driver.quit();
    }
}
