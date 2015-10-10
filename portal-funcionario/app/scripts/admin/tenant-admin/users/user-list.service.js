/**
 * Created by LeanFactory on 23/02/15.
 */

'use strict';

ventanillaUnicaModule.factory('UserListService', ['environment', '$resource', function(environment, $resource) {
  var url = environment.autheo +'/users';
  var defaultParams = {};

  var actions = {
    getUsersByRoleId: { method: 'GET', isArray: true, headers:{'Content-Type':'application/octet-stream'} }
  };

  return $resource(url, defaultParams, actions);
}]);
