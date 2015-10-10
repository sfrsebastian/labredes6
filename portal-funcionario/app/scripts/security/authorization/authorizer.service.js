ventanillaUnicaModule.constant('APP_PERMISSIONS',{
  "revisor-home" : "revisor",
  "functionary-home" : "funcionario"

});

ventanillaUnicaModule.constant('USER_ROLES',{
  admin : "admin",
  revisor: "revisor",
  funcionario: "funcionario"
});

var authorizerService = ventanillaUnicaModule.factory('AuthorizerService',['APP_PERMISSIONS', 'USER_ROLES', 'SessionService', 
  function(APP_PERMISIONS, USER_ROLES, SessionService) {
  return function(state) {

      if(!APP_PERMISIONS[state.name]){

        throw "Bad permission value";
      }
      var userRole = SessionService.getAuthorizationUserRole();
      if(userRole){
        return APP_PERMISIONS[state.name] == userRole;
      }

    return false;
  }
}]);
