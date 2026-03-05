Feature: EPAM SolutionsHub Top Navigation

  Background:
    Given I open the browser
    And I open EPAM SolutionsHub homepage

  @SOLUTIONS-CL-01
  Scenario: Open Solutions tab from top navigation
    When I click the "Solutions" tab
    Then the URL should contain "/catalog"
    And no critical error message should be shown

  @ASSETS-CL-01
  Scenario: Open Assets tab from top navigation
    When I click the "Assets" tab
    Then the URL should contain "/assets"
    And no critical error message should be shown

  @GUIDES-CL-01
  Scenario: Open Guides tab from top navigation
    When I click the "Guides" tab
    Then the URL should contain "/guides"
    And no critical error message should be shown

  @BLOG-CL-01
  Scenario: Open Blog tab from top navigation
    When I click the "Blog" tab
    Then the URL should contain "/blog"
    And no critical error message should be shown

  @ABOUT-CL-01
  Scenario: Open About tab from top navigation
    When I click the "About" tab
    Then the URL should contain "/about"
    And no critical error message should be shown
