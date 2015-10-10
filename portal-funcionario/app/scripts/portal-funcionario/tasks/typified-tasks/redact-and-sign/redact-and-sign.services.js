'use strict';

portalFuncionarioModule.factory('RedactSignService', ['environment', '$resource','SessionService', function(environment, $resource,SessionService) {

  var url = environment.portalFunctionary + '/task/';
  var defaultParams = {};

  var actions = {
    redactAndSign: { method:'POST', isArray:false, timeout:100000, url: url + 'SignAndCompleteTypeTask/:taskId' },
    redactAndComplete: { method:'POST', isArray:false, timeout:100000, 
    				url: url + 'RedactAndCompleteTypeTask/:taskId', headers: { 'Content-Type': 'multipart/form-data' },
    				transformRequest: angular.identity, }
  };

  return $resource(url, defaultParams, actions);
}]);

portalFuncionarioModule.factory('RedactCompleteTaskService', ['environment', '$http', function(environment, $http) {

	var url = environment.portalFunctionary + '/task/';

	return {

	    redactAndComplete: function(formData, taskId, successFunction, errorFunction) {

	      	$http.post(url + 'RedactAndCompleteTypeTask/' + taskId, formData, {
	        	transformRequest: angular.identity,
	        	headers: {'Content-Type': undefined}
	      	})
	        .success(successFunction)
	        .error(errorFunction);
	    }
	}
}]);
