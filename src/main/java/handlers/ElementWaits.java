package handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementWaits {


    public Boolean waitforElementVisiablity(WebDriver driver, By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try{
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
