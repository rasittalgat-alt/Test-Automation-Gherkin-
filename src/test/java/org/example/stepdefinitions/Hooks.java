package org.example.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Hooks {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            saveScenarioScreenshot(driver, scenario);
            driver.quit();
            DRIVER.remove();
        }
    }

    private void saveScenarioScreenshot(WebDriver driver, Scenario scenario) {
        if (!(driver instanceof TakesScreenshot)) {
            return;
        }

        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String status = scenario.isFailed() ? "FAILED" : "PASSED";
            String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_");
            String fileName = String.format("%s_%s_%s.png", status, safeName, TS_FORMAT.format(LocalDateTime.now()));
            Path dir = Paths.get("build", "reports", "screenshots");
            Files.createDirectories(dir);
            Files.write(dir.resolve(fileName), bytes);
            scenario.attach(bytes, "image/png", fileName);
        } catch (IOException ignored) {
            // Keep test flow stable even if screenshot saving fails.
        }
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }
}
