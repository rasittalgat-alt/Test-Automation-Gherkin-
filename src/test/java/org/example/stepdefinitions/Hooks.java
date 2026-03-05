package org.example.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Hooks {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    @After
    public void tearDown() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }
}
