/**
 * Created by LeanFactory on 6/05/15.
 */

'use strict';

portalFuncionarioModule.factory('BusinessService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.businessReports + '/requests';
  var defaultParams = {};

  var actions = {

    getRequests: { method: 'GET', isArray: false }

  };

  return $resource(url, defaultParams, actions);

}]);


portalFuncionarioModule.factory('BusinessEventService', ['environment', '$resource', function(environment, $resource) {

  var reviewedRequestsUrl = environment.businessReports + '/reviewed-requests';
  var defaultParams = {};

  var actions = {

    getRequests: { method: 'GET', isArray: false }

  };

  return $resource(reviewedRequestsUrl, defaultParams, actions);

}]);
