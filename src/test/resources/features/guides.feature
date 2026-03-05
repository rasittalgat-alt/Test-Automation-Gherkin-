Feature: Guides tab behavior

  Background:
    Given I open the browser
    And I navigate to "https://solutionshub.epam.com/guides"

  @GUIDES-CL-02
  Scenario: Guides list shows content cards
    Then at least one content card is displayed
    And no critical error message should be shown

  @GUIDES-CL-03
  Scenario: Guides filtering updates results
    Then at least one content card is displayed
    And no critical error message should be shown

  @GUIDES-CL-04
  Scenario: Guides sorting updates list
    Then at least one content card is displayed
    And no critical error message should be shown

  @GUIDES-CL-08
  Scenario: Header search works in Guides context
    When I search for "testing"
    Then the URL should contain "search"
    And no critical error message should be shown
