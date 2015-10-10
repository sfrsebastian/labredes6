/**
 * Created by LeanFactory on 12/06/15.
 */

'use strict';

ventanillaUnicaModule.factory('StyleSheetService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/stylesheets';
  var defaultParams = {};

  var actions = {
    getStyleSheet: { method: 'GET', isArray: false }
  };

  return $resource(url, defaultParams, actions);
}]);

adminModule.factory('TenantLogoService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/stylesheets/logo';
  var defaultParams = {};

  var actions = {

    getTenantLogo: { method: 'GET'}
  };

  return $resource(url, defaultParams, actions);
}]);
