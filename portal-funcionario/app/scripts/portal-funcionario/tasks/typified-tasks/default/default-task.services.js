/**
 * Created by LeanFactory on 9/03/15.
 */

'use strict';

adminModule.factory('DefaultTaskService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.portalFunctionary + '/task/complete';
  var defaultParams = {};

  var actions = {

    completeTaskService: { method:'POST', isArray:false }

  };

  return $resource(url, defaultParams, actions);
}]);
