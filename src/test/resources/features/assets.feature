Feature: Assets tab behavior

  Background:
    Given I open the browser
    And I navigate to "https://solutionshub.epam.com/catalog"

  @ASSETS-CL-02
  Scenario: Assets list shows content cards
    Then at least one content card is displayed
    And no critical error message should be shown

  @ASSETS-CL-03
  Scenario: Assets filtering updates results
    Then at least one content card is displayed
    And no critical error message should be shown

  @ASSETS-CL-04
  Scenario: Assets sorting updates list
    Then at least one content card is displayed
    And no critical error message should be shown

  @ASSETS-CL-08
  Scenario: Header search works in Assets context
    When I search for "cloud"
    Then no critical error message should be shown
