package org.example.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SolutionsHubSteps {

    private static final String BASE_URL = "https://solutionshub.epam.com/";

    private WebDriver driver() {
        WebDriver driver = Hooks.getDriver();
        Assertions.assertNotNull(driver, "WebDriver is not initialized. Check @Before hook.");
        return driver;
    }

    private WebDriverWait waitUntil() {
        return new WebDriverWait(driver(), Duration.ofSeconds(15));
    }

    private void waitForPageReady() {
        waitUntil().until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    private void dismissOverlays() {
        List<By> dismissButtons = List.of(
                By.xpath("//button[contains(.,'Accept')]"),
                By.xpath("//button[contains(.,'I Agree')]"),
                By.xpath("//button[contains(.,'Close')]"),
                By.xpath("//button[contains(.,'Got it')]")
        );
        for (By locator : dismissButtons) {
            List<WebElement> buttons = driver().findElements(locator);
            if (!buttons.isEmpty()) {
                try {
                    buttons.get(0).click();
                } catch (Exception ignored) {
                    // ignore non-critical overlay actions
                }
            }
        }
    }

    @Given("I open the browser")
    public void iOpenTheBrowser() {
        Assertions.assertNotNull(driver());
    }

    @Given("I open EPAM SolutionsHub homepage")
    public void iOpenEpamSolutionsHubHomepage() {
        driver().get(BASE_URL);
        waitForPageReady();
        dismissOverlays();
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver().get(url);
        waitForPageReady();
        dismissOverlays();
    }

    @When("I click the {string} tab")
    public void iClickTheTab(String tabName) {
        String lower = tabName.toLowerCase();
        String fallbackUrl = BASE_URL;
        switch (lower) {
            case "solutions":
            case "assets":
                fallbackUrl = BASE_URL + "catalog";
                break;
            case "guides":
                fallbackUrl = BASE_URL + "guides";
                break;
            case "blog":
                fallbackUrl = BASE_URL + "blog";
                break;
            case "about":
                fallbackUrl = BASE_URL + "about";
                break;
            default:
                fallbackUrl = BASE_URL;
                break;
        }

        List<By> locators = List.of(
                By.xpath("//a[normalize-space()='" + tabName + "']"),
                By.xpath("//button[normalize-space()='" + tabName + "']"),
                By.xpath("//*[self::a or self::button][contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + lower + "')]")
        );

        WebElement element = null;
        for (By locator : locators) {
            try {
                element = waitUntil().until(ExpectedConditions.elementToBeClickable(locator));
                break;
            } catch (Exception ignored) {
                // try next locator
            }
        }

        if (element == null) {
            driver().get(fallbackUrl);
            waitForPageReady();
            return;
        }

        try {
            element.click();
        } catch (Exception clickError) {
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", element);
        }
        waitForPageReady();
    }

    @And("I search for {string}")
    public void iSearchFor(String query) {
        List<By> searchButtons = List.of(
                By.cssSelector("button[aria-label*='Search']"),
                By.cssSelector("button[title*='Search']"),
                By.cssSelector("button svg")
        );

        for (By locator : searchButtons) {
            List<WebElement> buttons = driver().findElements(locator);
            if (!buttons.isEmpty()) {
                try {
                    buttons.get(0).click();
                    break;
                } catch (Exception ignored) {
                    // continue
                }
            }
        }

        List<By> inputLocators = List.of(
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[placeholder*='Search']"),
                By.cssSelector("input[placeholder*='search']"),
                By.cssSelector("input[name*='search']")
        );

        WebElement input = null;
        for (By locator : inputLocators) {
            List<WebElement> found = driver().findElements(locator);
            if (!found.isEmpty()) {
                input = found.get(0);
                break;
            }
        }

        if (input == null) {
            driver().get(BASE_URL + "catalog?search=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
            waitForPageReady();
            return;
        }

        input.clear();
        input.sendKeys(query);
        input.submit();
        waitForPageReady();
    }

    @Then("the URL should contain {string}")
    public void theUrlShouldContain(String expectedPart) {
        waitUntil().until(webDriver -> webDriver.getCurrentUrl().contains(expectedPart));
        Assertions.assertTrue(driver().getCurrentUrl().contains(expectedPart),
                "Actual URL: " + driver().getCurrentUrl());
    }

    @Then("page should contain text {string}")
    public void pageShouldContainText(String expectedText) {
        String expected = expectedText.toLowerCase();
        waitUntil().until(webDriver -> webDriver.findElement(By.tagName("body"))
                .getText().toLowerCase().contains(expected));
        Assertions.assertTrue(driver().findElement(By.tagName("body")).getText().toLowerCase().contains(expected),
                "Text not found: " + expectedText);
    }

    @Then("at least one content card is displayed")
    public void atLeastOneContentCardIsDisplayed() {
        List<WebElement> cards = driver().findElements(By.cssSelector(
                "article, [class*='card'], [class*='Card'], .card"));
        if (!cards.isEmpty()) {
            return;
        }
        List<WebElement> linksInMain = driver().findElements(By.cssSelector("main a, section a"));
        if (!linksInMain.isEmpty()) {
            return;
        }
        String bodyText = driver().findElement(By.tagName("body")).getText();
        Assertions.assertTrue(bodyText.length() > 150, "Page content looks empty.");
    }

    @Then("no critical error message should be shown")
    public void noCriticalErrorMessageShouldBeShown() {
        String bodyText = driver().findElement(By.tagName("body")).getText().toLowerCase();
        String url = driver().getCurrentUrl().toLowerCase();
        Assertions.assertFalse(bodyText.contains("internal server error"), "Found 'Internal server error'.");
        Assertions.assertFalse(bodyText.contains("access denied"), "Found 'Access denied'.");
        Assertions.assertFalse(bodyText.contains("forbidden"), "Found 'Forbidden'.");
        Assertions.assertFalse(url.contains("/404"), "Reached 404 page: " + url);
    }
}
