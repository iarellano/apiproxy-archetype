@apiproxy
Feature: API proxy endpoints

  @status
  Scenario: Test /status endpoint for client_credentials flow
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    When I GET /status
    Then response code should be 200
    And response body path $.host should be `domain`

  @status
  Scenario: Test /status endpoint for client_credentials flow
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    When I GET /ping
    Then response code should be 200
    And response body path $.host should be `domain`

  @transaccion-previa
  @transaccion-previa-1
  Scenario: Test /transaccion-previa for tipo de operacion verificar-transaccion-previa @transaccion-previa-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-transaccion-previa.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to transaccion-previa-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /transaccion-previa
    Then response code should be 200
    And response body should contain _meta
    And response body should contain idAfiliacion

  @transaccion-previa
  @transaccion-previa-3
  Scenario: Test /transaccion-previa for tipo de operacion verificar-transaccion-previa @transaccion-previa-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-transaccion-previa.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to transaccion-previa-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /transaccion-previa
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.


  @tarjeta-habiente
  @tarjeta-habiente-1
  Scenario: Test /tarjeta-habiente for tipo de operacion verificar-tarjeta-habiente @tarjeta-habiente-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-tarjeta-habiente.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to tarjeta-habiente-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /tarjeta-habiente
    Then response code should be 200
    And response body should contain _meta

  @tarjeta-habiente
  @tarjeta-habiente-2
  Scenario: Test /tarjeta-habiente for tipo de operacion verificar-tarjeta-habiente @tarjeta-habiente-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-tarjeta-habiente.json to body
    And I remove request body path $.tipoDeOperacion
    And I remove request body path $.datos.datosFactura.nombreFactura
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to tarjeta-habiente-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /tarjeta-habiente
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be No se proporciono un tipo de operacion.

  @tarjeta-habiente
  @tarjeta-habiente-3
  Scenario: Test /tarjeta-habiente for tipo de operacion verificar-tarjeta-habiente @tarjeta-habiente-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-tarjeta-habiente.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to tarjeta-habiente-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /tarjeta-habiente
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.


  @venta-promocion
  @venta-promocion-1
  Scenario: Test /venta-promocion for tipo de operacion verificar-venta-promocion @venta-promocion-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-promocion.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-promocion-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-promocion
    Then response code should be 200
    And response body should contain _meta

  @venta-promocion
  @venta-promocion-2
  Scenario: Test /venta-promocion for tipo de operacion verificar-venta-promocion @venta-promocion-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-promocion.json to body
    And I remove request body path $.tipoDeOperacion
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-promocion-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-promocion
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be No se proporciono un tipo de operacion.

  @venta-promocion
  @venta-promocion-3
  Scenario: Test /venta-promocion for tipo de operacion verificar-venta-promocion @venta-promocion-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-promocion.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-promocion-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-promocion
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @venta-normal
  @venta-normal-1
  Scenario: Test /venta-normal for tipo de operacion verificar-venta-normal @venta-normal-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-normal.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-normal-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-normal
    Then response code should be 200
    And response body should contain _meta

  @venta-normal
  @venta-normal-3
  Scenario: Test /venta-normal for tipo de operacion verificar-venta-normal @venta-normal-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-normal.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-normal-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-normal
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @alta-usuario
  @alta-usuario-1
  Scenario: Test /usuarios for tipo de operacion alta-usuario @alta-usuario-1
    Given I have a client credentials access_token using app transacciones-v1-usuarios-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-alta-usuario.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to alta-usuario-1
    When I use HMAC for application transacciones-v1-usuarios-app-cicd and POST to /usuarios
    Then response code should be 200

  @alta-usuario
  @alta-usuario-2
  Scenario: Test /usuarios for tipo de operacion alta-usuario @alta-usuario-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-alta-usuario.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to alta-usuario-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /usuarios
    Then response code should be 403

  @alta-usuario
  @alta-usuario-3
  Scenario: Test /usuarios for tipo de operacion alta-usuario @alta-usuario-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-alta-usuario.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to alta-usuario-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /usuarios
    Then response code should be 403

#----------------------------------------------------------------------------------------------------------------
  @postautorizar-pago
  @postautorizar-pago-1
  Scenario: Test /postautorizar-pago for tipo de operacion verificar-postautorizar-pago @postautorizar-pago-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-postautorizar-pago.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to postautorizar-pago-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /postautorizar-pago
    Then response code should be 200
    And response body should contain _meta

  @postautorizar-pago
  @postautorizar-pago-2
  Scenario: Test /postautorizar-pago for tipo de operacion verificar-postautorizar-pago @postautorizar-pago-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-postautorizar-pago.json to body
    And I remove request body path $.tipoDeOperacion
    And I remove request body path $.datos.importeTotal
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to postautorizar-pago-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /postautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be No se proporciono un tipo de operacion.

  @postautorizar-pago
  @postautorizar-pago-3
  Scenario: Test /postautorizar-pago for tipo de operacion verificar-postautorizar-pago @postautorizar-pago-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-postautorizar-pago.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to postautorizar-pago-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /postautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @preautorizar-pago
  @preautorizar-pago-1
  Scenario: Test /preautorizar-pago for tipo de operacion verificar-preautorizar-pago @preautorizar-pago-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-pago.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-pago-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-pago
    Then response code should be 200
    And response body should contain _meta

  @preautorizar-pago
  @preautorizar-pago-2
  Scenario: Test /preautorizar-pago for tipo de operacion verificar-preautorizar-pago @preautorizar-pago-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-pago.json to body
    And I remove request body path $.tipoDeOperacion
    And I remove request body path $.datos.modoVenta
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-pago-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be No se proporciono un tipo de operacion.

  @preautorizar-pago
  @preautorizar-pago-3
  Scenario: Test /preautorizar-pago for tipo de operacion verificar-preautorizar-pago @preautorizar-pago-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-pago.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-pago-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @preautorizar-promocion
  @preautorizar-promocion-1
  Scenario: Test /preautorizar-promocion for tipo de operacion verificar-preautorizar-promocion @preautorizar-promocion-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-promocion.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-promocion-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-promocion
    Then response code should be 200
    And response body should contain _meta

  @preautorizar-promocion
  @preautorizar-promocion-2
  Scenario: Test /preautorizar-promocion for tipo de operacion verificar-preautorizar-promocion @preautorizar-promocion-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-promocion.json to body
    And I remove request body path $.datos.numeroPlastico
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-promocion-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-promocion
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @preautorizar-promocion
  @preautorizar-promocion-3
  Scenario: Test /preautorizar-promocion for tipo de operacion verificar-preautorizar-promocion @preautorizar-promocion-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-preautorizar-promocion.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to preautorizar-promocion-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /preautorizar-promocion
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @reautorizar-pago
  @reautorizar-pago-1
  Scenario: Test /reautorizar-pago for tipo de operacion verificar-reautorizar-pago @reautorizar-pago-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-reautorizar-pago.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reautorizar-pago-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reautorizar-pago
    Then response code should be 200
    And response body should contain _meta

  @reautorizar-pago
  @reautorizar-pago-2
  Scenario: Test /reautorizar-pago for tipo de operacion verificar-reautorizar-pago @reautorizar-pago-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-reautorizar-pago.json to body
    And I remove request body path $.datos.numeroReferencia
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reautorizar-pago-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @reautorizar-pago
  @reautorizar-pago-3
  Scenario: Test /reautorizar-pago for tipo de operacion verificar-reautorizar-pago @reautorizar-pago-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-reautorizar-pago.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reautorizar-pago-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reautorizar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.

  @venta-forzada
  @venta-forzada-1
  Scenario: Test /venta-forzada for tipo de operacion verificar-venta-forzada @venta-forzada-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-forzada.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-forzada-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-forzada
    Then response code should be 200
    And response body should contain _meta

  @venta-forzada
  @venta-forzada-2
  Scenario: Test /venta-forzada for tipo de operacion verificar-venta-forzada @venta-forzada-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-forzada.json to body
    And I remove request body path $.tipoDeOperacion
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-forzada-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-forzada
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @venta-forzada
  @venta-forzada-3
  Scenario: Test /venta-forzada for tipo de operacion verificar-venta-forzada @venta-forzada-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/verificar-venta-forzada.json to body
    And I set request body path $.tipoDeOperacion to venta-igualitaria
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-forzada-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-forzada
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: venta-igualitaria.
    
#----------------------------------------------------------------------------------------------------------------
  @venta-normal
  @venta-normal-cancelar
  Scenario: Test /venta-normal for tipo de operacion verificar-venta-normal @venta-normal-cancelar
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/realizar-venta-normal.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-normal-cancelar
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-normal
    Then response code should be 200
    And response body should contain _meta
    And I store the value of body path $.numeroReferencia as numeroReferenciaCancelar in global scope
    
  @cancelar-pago
  @cancelar-pago-1
  Scenario: Test /cancelar-pago for tipo de operacion cancelar-pago @cancelar-pago-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/cancelar-pago.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to cancelar-pago-1
    And I set body parameters numeroReferencia and numeroReferenciaCancelar
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /cancelar-pago
    Then response code should be 200
    And response body should contain _meta

  @cancelar-pago
  @cancelar-pago-2
  Scenario: Test /cancelar-pago for tipo de operacion cancelar-pago @cancelar-pago-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/cancelar-pago.json to body
    And I remove request body path $.datos.numeroReferencia
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to cancelar-pago-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /cancelar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @cancelar-pago
  @cancelar-pago-3
  Scenario: Test /cancelar-pago for tipo de operacion cancelar-pago @cancelar-pago-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/cancelar-pago.json to body
    And I set request body path $.tipoDeOperacion to cancelar-pagos
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to cancelar-pago-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /cancelar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: cancelar-pago.

  @venta-normal
  @venta-normal-reversar
  Scenario: Test /venta-normal for tipo de operacion verificar-venta-normal @venta-normal-reversar
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/realizar-venta-normal.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-normal-reversar
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-normal
    Then response code should be 200
    And response body should contain _meta
    And I store the value of body path $.numeroReferencia as numeroReferenciaReversar in global scope

    
  @reversar-pago
  @reversar-pago-1
  Scenario: Test /reversar-pago for tipo de operacion reversar-pago @reversar-pago-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/reversar-pago.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reversar-pago-1
    And I set body parameters numeroReferencia and numeroReferenciaReversar
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reversar-pago
    Then response code should be 200
    And response body should contain _meta

  @reversar-pago
  @reversar-pago-2
  Scenario: Test /reversar-pago for tipo de operacion reversar-pago @reversar-pago-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/reversar-pago.json to body
    And I remove request body path $.datos.numeroReferencia
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reversar-pago-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reversar-pago
    Then response code should be 400
    And response body path $.code should be 400.10.01
       
  @reversar-pago
  @reversar-pago-3
  Scenario: Test /reversar-pago for tipo de operacion reversar-pago @reversar-pago-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/reversar-pago.json to body
    And I set request body path $.tipoDeOperacion to reversar-pagos
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to reversar-pago-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /reversar-pago
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: reversar-pago.    

  @venta-normal
  @venta-normal-referenciada
  Scenario: Test /venta-normal for tipo de operacion verificar-venta-normal @venta-normal-referenciada
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/realizar-venta-normal.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to venta-normal-referenciada
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /venta-normal
    Then response code should be 200
    And response body should contain _meta
    And I store the value of body path $.numeroReferencia as numeroReferenciaDevolucionReferenciada in global scope

  @devolucion-referenciada
  @devolucion-referenciada-1
  Scenario: Test /devolucion-referenciada for tipo de operacion devolucion-referenciada @devolucion-referenciada-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-referenciada.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-referenciada-1
    And I set body parameters numeroReferencia and numeroReferenciaDevolucionReferenciada
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-referenciada
    Then response code should be 200
    And response body should contain _meta

  @devolucion-referenciada
  @devolucion-referenciada-2
  Scenario: Test /devolucion-referenciada for tipo de operacion devolucion-referenciada @devolucion-referenciada-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-referenciada.json to body
    And I remove request body path $.datos.numeroReferencia
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-referenciada-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-referenciada
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @devolucion-referenciada
  @devolucion-referenciada-3
  Scenario: Test /devolucion-referenciada for tipo de operacion devolucion-referenciada @devolucion-referenciada-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-referenciada.json to body
    And I set request body path $.tipoDeOperacion to devolucion-referenciadas
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-referenciada-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-referenciada
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: devolucion-referenciada.  
    
  @devolucion-sinrestriccion
  @devolucion-sinrestriccion-1
  Scenario: Test /devolucion-sinrestriccion for tipo de operacion devolucion-sinrestriccion @devolucion-sinrestriccion-1
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-sinrestricion.json to body
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-sinrestriccion-1
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-sinrestriccion
    Then response code should be 200
    And response body should contain _meta

  @devolucion-sinrestriccion
  @devolucion-sinrestriccion-2
  Scenario: Test /devolucion-sinrestriccion for tipo de operacion devolucion-sinrestriccion @devolucion-sinrestriccion-2
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-sinrestricion.json to body
    And I remove request body path $.datos.numeroReferencia
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-sinrestriccion-2
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-sinrestriccion
    Then response code should be 400
    And response body path $.code should be 400.06.01

  @devolucion-sinrestriccion
  @devolucion-sinrestriccion-3
  Scenario: Test /devolucion-sinrestriccion for tipo de operacion devolucion-sinrestriccion @devolucion-sinrestriccion-3
    Given I have a client credentials access_token using app transacciones-v1-tpp-app-cicd
    And I pipe contents of file test/integration/sample-payloads/devolucion-sinrestricion.json to body
    And I set request body path $.tipoDeOperacion to devolucion-sinrestricciones
    And I set Content-Type header to application/json
    And I set X-Integration-Tag header to devolucion-sinrestriccion-3
    When I use HMAC for application transacciones-v1-tpp-app-cicd and POST to /devolucion-sinrestriccion
    Then response code should be 400
    And response body path $.code should be 400.06.01
    And response body path $.message should be Tipo de operacion invalida: devolucion-sinrestriccion.
 