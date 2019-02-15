@apiproxy
Feature: API proxy endpoints

  @status
  @status-1
  Scenario: Test /status endpoint for client_credentials flow @status-1
    Given I set scenario variables from application ${apiproxy.canonicalName}-app and using prefix app1.
    And I set query parameters to
      | parameter  | value           |

      | apikey     | `app1.clientId` |
    When I GET /anything
    Then response code should be 200
    And response body path $.method should be GET