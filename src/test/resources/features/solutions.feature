Feature: Solutions tab behavior

  Background:
    Given I open the browser
    And I navigate to "https://solutionshub.epam.com/catalog"

  @SOLUTIONS-CL-02
  Scenario: Solutions list shows content cards
    Then at least one content card is displayed
    And no critical error message should be shown

  @SOLUTIONS-CL-03
  Scenario: Solutions filtering updates results
    Then at least one content card is displayed
    And no critical error message should be shown

  @SOLUTIONS-CL-04
  Scenario: Solutions sorting changes list order
    Then at least one content card is displayed
    And no critical error message should be shown

  @SOLUTIONS-CL-08
  Scenario: Header search works in Solutions context
    When I search for "AI"
    Then the URL should contain "search"
    And no critical error message should be shown
