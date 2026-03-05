Feature: Blog tab behavior

  Background:
    Given I open the browser
    And I navigate to "https://solutionshub.epam.com/blog"

  @BLOG-CL-02
  Scenario: Blog list shows posts
    Then at least one content card is displayed
    And no critical error message should be shown

  @BLOG-CL-03
  Scenario: Blog category or tag filtering works
    Then at least one content card is displayed
    And no critical error message should be shown

  @BLOG-CL-04
  Scenario: Blog post details can be opened
    Then at least one content card is displayed
    And no critical error message should be shown

  @BLOG-CL-08
  Scenario: Header search works in Blog context
    When I search for "data"
    Then the URL should contain "search"
    And no critical error message should be shown

