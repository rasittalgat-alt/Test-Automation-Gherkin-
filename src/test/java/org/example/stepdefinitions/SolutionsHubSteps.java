package org.example.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
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

    @Given("I open the browser")
    public void iOpenTheBrowser() {
        Assertions.assertNotNull(driver());
    }

    @Given("I open EPAM SolutionsHub homepage")
    public void iOpenEpamSolutionsHubHomepage() {
        driver().get(BASE_URL);
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver().get(url);
    }

    @When("I click the {string} tab")
    public void iClickTheTab(String tabName) {
        List<By> locators = List.of(
                By.xpath("//a[normalize-space()='" + tabName + "']"),
                By.xpath("//button[normalize-space()='" + tabName + "']"),
                By.xpath("//*[contains(@class,'nav')]//*[self::a or self::button][contains(normalize-space(), '" + tabName + "')]")
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
            throw new NoSuchElementException("Tab not found: " + tabName);
        }

        element.click();
    }

    @And("I search for {string}")
    public void iSearchFor(String query) {
        List<By> inputLocators = List.of(
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[placeholder*='Search']"),
                By.cssSelector("input[placeholder*='search']")
        );

        WebElement input = null;
        for (By locator : inputLocators) {
            List<WebElement> found = driver().findElements(locator);
            if (!found.isEmpty()) {
                input = found.get(0);
                break;
            }
        }

        Assertions.assertNotNull(input, "Search input is not found.");
        input.clear();
        input.sendKeys(query);
        input.submit();
    }

    @Then("the URL should contain {string}")
    public void theUrlShouldContain(String expectedPart) {
        waitUntil().until(webDriver -> webDriver.getCurrentUrl().contains(expectedPart));
        Assertions.assertTrue(driver().getCurrentUrl().contains(expectedPart),
                "Actual URL: " + driver().getCurrentUrl());
    }

    @Then("page should contain text {string}")
    public void pageShouldContainText(String expectedText) {
        waitUntil().until(webDriver -> webDriver.getPageSource().contains(expectedText));
        Assertions.assertTrue(driver().getPageSource().contains(expectedText),
                "Text not found: " + expectedText);
    }

    @Then("at least one content card is displayed")
    public void atLeastOneContentCardIsDisplayed() {
        List<WebElement> cards = driver().findElements(By.cssSelector(
                "article, [class*='card'], [class*='Card'], .card"));
        Assertions.assertFalse(cards.isEmpty(), "No visible content cards found.");
    }

    @Then("no critical error message should be shown")
    public void noCriticalErrorMessageShouldBeShown() {
        String page = driver().getPageSource().toLowerCase();
        Assertions.assertFalse(page.contains("something went wrong"), "Found 'Something went wrong'.");
        Assertions.assertFalse(page.contains("internal server error"), "Found 'Internal server error'.");
        Assertions.assertFalse(page.contains(" 500 "), "Found '500' in page content.");
    }
}
