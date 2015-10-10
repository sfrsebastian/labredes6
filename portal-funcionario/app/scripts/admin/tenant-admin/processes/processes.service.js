'use strict';

adminModule.factory('ProcessesService', ['environment','$http', function(environment,$http) {
  return {
    uploadTemporalFile: function (newFile, successFunction, errorFunction) {

      $http.post(environment.workFlowManager + '/process-definitions', newFile, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
      })
        .success(function() {

          successFunction();
        })
        .error(function() {

          errorFunction();
        });
    }
  }
}]);
