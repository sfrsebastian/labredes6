/**
 * Created by LeanFactory on 10/06/15.
 */

adminModule.factory('NewTenantSettingsService', ['environment', '$http', function(environment, $http) {

  return {

    setNewTenantSettings: function(fileByteArray, successFunction, errorFunction) {

      $http.post(environment.customerSettings + '/stylesheets', fileByteArray, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      })
        .success(successFunction)
        .error(errorFunction);
    }
  }
}]);


adminModule.factory('OrganizationService2', ['environment', '$http', function(environment, $http) {

  return {

    createAdminWithPhoto: function(fileByteArray, successFunction, errorFunction) {

      $http.post(environment.autheo + '/organizations', fileByteArray, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      })
        .success(successFunction)
        .error(errorFunction);
    }
  }
}]);


adminModule.factory('TenantListService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/stylesheets/tenantList';
  var defaultParams = {};

  var actions = {

    getTenantList: { method: 'GET', isArray: true}
  };

  return $resource(url, defaultParams, actions);
}]);

adminModule.factory('TenantHexColorService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.customerSettings +'/stylesheets/hexColor';
  var defaultParams = {};

  var actions = {

    getHexColor: { method: 'GET', isArray: false}
  };

  return $resource(url, defaultParams, actions);
}]);

