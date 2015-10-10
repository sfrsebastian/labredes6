'use strict';

adminModule.factory('UserService', ['environment', '$resource','SessionService', function(environment, $resource,SessionService) {
  var url = environment.autheo + '/users';
  var defaultParams = {};

  var actions = {
    saveUser:{method:'POST', isArray:false, url: url + '/photo'},
    userDocumentExists:{method:'GET', isArray:false, url: url+'/documents/:documentNumber'},
    userUsernameExists:{method:'GET', isArray:false, url: url+'/usernames/:username'}
  };

  return $resource(url, defaultParams, actions);
}]);


adminModule.factory('UserWithPhotoService', ['environment', '$http', function(environment, $http) {

  return {

    createUserWithPhoto: function(fileByteArray, successFunction, errorFunction) {

      $http.post(environment.autheo + '/users/photo', fileByteArray, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      })
        .success(successFunction)
        .error(errorFunction);
    }
  }
}]);

adminModule.factory('OrganizationAdminWithPhotoService', ['environment', '$http', function(environment, $http) {

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


