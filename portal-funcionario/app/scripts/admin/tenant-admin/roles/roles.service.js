adminModule.factory('RoleService', ['environment', '$resource', function(environment, $resource) {
  var url = environment.autheo + '/roles';
  var defaultParams = {};

  var actions = {
    getBusinessRoles:{method:'GET',isArray:true, url : url + '/organizations/:organizationId'},
    createBusinessRole : {method:'POST', isArray:false, url: url + '/businessRole'}
  };

  return $resource(url, defaultParams, actions);
}]);

