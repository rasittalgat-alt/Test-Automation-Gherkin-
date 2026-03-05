# Test-Automation-Gherkin-

BDD automation project for https://solutionshub.epam.com/.

## Stack
- Java 11
- Gradle (Wrapper)
- JUnit 5
- Cucumber (Gherkin)
- Selenium WebDriver

## Run tests

### Windows (PowerShell)
```powershell
.\gradlew.bat test
```

### Optional headless mode
```powershell
.\gradlew.bat test -Dheadless=true
```

## Reports and screenshots
- Cucumber HTML report: `build/reports/cucumber/cucumber.html`
- Scenario screenshots (PASSED/FAILED): `build/reports/screenshots/`

Each scenario saves one screenshot after execution. You can use these images for assignment evidence.

## Suggested flow for assignment screenshots
1. Run tests with `.\gradlew.bat test`.
2. Open `build/reports/cucumber/cucumber.html` and take screenshots of scenario results.
3. Open `build/reports/screenshots/` and include several `PASSED_*.png` files.
