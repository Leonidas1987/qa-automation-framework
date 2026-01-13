package tests;

import com.google.common.util.concurrent.Uninterruptibles;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SwitchAndNavigation {
    WebDriver driver;
    @BeforeClass
    public void startSession() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://atidcollege.co.il/Xamples/ex_switch_navigation.html");
    }
    @Test
    public void test01() {
        driver.findElement(By.id("btnAlert")).click();
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        System.out.println("Print alert text: "+alertText);
        String expectedAlertText = "Alert is gone.";
        String actualAlertText = driver.findElement(By.id("output")).getText();
        Assert.assertEquals(actualAlertText,expectedAlertText);
    }
    @Test
    public void test02VerifyPrompt() {
        // -------------  Handling Prompt  ---------------------------------
        String keyword="Selenium";
        driver.findElement(By.id("btnPrompt")).click();
        Alert prompt = driver.switchTo().alert();
        System.out.println("Prompt Tex: "+prompt.getText());
        prompt.sendKeys(keyword);
        prompt.accept();
        String actualPromptText = driver.findElement(By.id("output")).getText();
        Assert.assertEquals(actualPromptText,keyword);
    }
    @Test
    public void test03VerifyConfirm() {
        // -------------  Handling Confirm Box - Accept  ----------------------------
        driver.findElement(By.id("btnConfirm")).click();
        Alert popup = driver.switchTo().alert();
        popup.accept();
        String expectedMessage = "Confirmed.";
        String actualMessage = driver.findElement(By.id("output")).getText();
        Assert.assertEquals(actualMessage,expectedMessage);
    }
    @Test
    public void test04VerifyReject() {
        // -------------  Handling Confirm Box - Reject  ----------------------------
        driver.findElement(By.id("btnConfirm")).click();
        Alert popup = driver.switchTo().alert();
        popup.dismiss();
        String expectedMessage = "Rejected!";
        String actualMessage = driver.findElement(By.id("output")).getText();
        Assert.assertEquals(actualMessage,expectedMessage);
    }
    @Test
    public void test05VerifyTab() {
        driver.findElement(By.id("btnNewTab")).click();
        String originalTab=driver.getWindowHandle();
        //Move to new tab:
        for(String tab: driver.getWindowHandles()){
            driver.switchTo().window(tab);
        }
        String newTabText=driver.findElement(By.id("new_tab_container")).getText();
        System.out.println(newTabText);
        //Move back to original tab:
        driver.close();
        driver.switchTo().window(originalTab);
        String expectedHomeTitle="Switch and Navigate";
        String actualHomeTitle=driver.findElement(By.id("title")).getText();
        Assert.assertEquals(actualHomeTitle,expectedHomeTitle);
    }
    @Test
    public void test06VerifyWindowAnotherWay() {
        // -------- Move to new Window and Return to Old Window  -------
        String originalWindow = driver.getWindowHandle();
        driver.findElement(By.cssSelector("a[href='ex_switch_newWindow.html']")).click();

        // Option 2 - Cast to ArrayList and use get method
        ArrayList<String> newTab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(newTab.get(1));  // change focus to new tab
        String newWindowText=driver.findElement(By.id("new_window_container")).getText();
        System.out.println(newWindowText);
        //Move back to original window:
        driver.close();
        driver.switchTo().window(originalWindow);
        String expectedHomeTitle="Switch and Navigate";
        String actualHomeTitle=driver.findElement(By.id("title")).getText();
        Assert.assertEquals(actualHomeTitle,expectedHomeTitle);
    }
    @Test
    public void test07VerifyFrame() {
        // ------------ Go to iFrame and Return to Main Window----------------------
        //Enter Iframe:
        WebElement iframe=driver.findElement(By.xpath("//iframe[@src='ex_switch_newFrame.html']"));
        driver.switchTo().frame(iframe);
        String iframeText=driver.findElement(By.id("iframe_container")).getText();
        //Exit Iframe:
        driver.switchTo().defaultContent();
        System.out.println(iframeText);
        String expectedHomeTitle="Switch and Navigate";
        String actualHomeTitle=driver.findElement(By.id("title")).getText();
        Assert.assertEquals(actualHomeTitle,expectedHomeTitle);
    }
    @AfterClass
    public void closeSession() {
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        driver.quit();
    }
}
