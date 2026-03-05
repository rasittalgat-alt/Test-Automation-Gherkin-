Feature: About tab behavior

  Background:
    Given I open the browser
    And I navigate to "https://solutionshub.epam.com/about"

  @ABT-TC-401 @ABOUT-CL-02
  Scenario: About page opens and URL is correct
    Then the URL should contain "/about"
    And no critical error message should be shown

  @ABT-TC-402 @ABOUT-CL-03
  Scenario: Refresh keeps user on About without broken state
    When I navigate to "https://solutionshub.epam.com/about"
    Then the URL should contain "/about"
    And no critical error message should be shown

  @ABT-TC-403
  Scenario: Header search works on About page
    When I search for "AI"
    Then the URL should contain "search"
    And no critical error message should be shown

  @ABT-TC-404
  Scenario: Invalid search input is handled gracefully
    When I search for "!!!@@@"
    Then no critical error message should be shown

  @ABT-TC-405
  Scenario: About content is loaded and readable
    Then page should contain text "About"
    And no critical error message should be shown

  @ABT-TC-406
  Scenario: FAQ section works
    Then page should contain text "FAQ"
    And no critical error message should be shown

  @ABT-TC-407
  Scenario: Footer policy links are present
    Then page should contain text "Privacy"
    And page should contain text "Terms"
    And no critical error message should be shown

  @ABT-TC-408
  Scenario: Contact Us entry point is available
    Then page should contain text "Contact"
    And no critical error message should be shown
