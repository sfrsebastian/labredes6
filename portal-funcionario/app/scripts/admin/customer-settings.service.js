'use strict';

adminModule.factory('SettingsService', ['environment', '$resource','SessionService', function(environment, $resource,SessionService) {

    var url = environment.customerSettings + '/settings/tenants/';
    var defaultParams = {};

    var actions = {
        getSettings: { method: 'GET', isArray: false, url: url },
        saveSettings: { method:'POST',isArray:false }
    };

    return $resource(url, defaultParams, actions);
}]);

adminModule.factory('SystemSettingsService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/systemSettings';
  var defaultParams = {};

  var actions = {
    getSystemSettings: { method: 'GET', isArray: false},
    saveSystemSettings: {method: 'POST', isArray : false}
  };

  return $resource(url, defaultParams, actions);

}]);

adminModule.factory('ProcessSettingsService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/procedureSettings';
  var defaultParams = {};

  var actions = {
    saveProcedureSettings: { method: 'POST', isArray: false},
    getAllProcedureSettings : {method:'GET', isArray: true, url: url + '/procedures'},
    getProcedureSettings: { method: 'GET', isArray: false, url: url + '/:procedureName'}
  };

  return $resource(url, defaultParams, actions);

}]);

adminModule.factory('TenantSettingService', ['environment', '$http', function(environment, $http) {

  return {

    saveTenantSettings: function(fileByteArray, successFunction, errorFunction) {

      $http.post(environment.customerSettings + '/settings/tenants', fileByteArray, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      })
        .success(successFunction)
        .error(errorFunction);
    }
  }
}]);

