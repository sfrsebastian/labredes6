/**
 * Created by LeanFactory on 23/02/15.
 */

'use strict';

ventanillaUnicaModule.factory('ProcessInstanceService', ['environment', '$resource', function(environment, $resource) {
  var url = environment.workFlowManager +'/process-instances';
  var defaultParams = {};

  var actions = {
    getCurrentTask: { method: 'GET', isArray: true , url : url + '/:processInstanceId/currentTask'},
    getProcessDiagram: { method: 'GET', isArray: false, url: url + '/:processInstanceId/diagram'}
  };

  return $resource(url, defaultParams, actions);
}]);
