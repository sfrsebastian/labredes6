'use strict';

securityModule.factory('LoginService', ['environment', '$resource', function(environment, $resource) {

    var url = environment.autheo + '/users';
    var defaultParams = {};

    var actions = {
        authenticateFunctionary: { method: 'POST', url: url + '/:username/tokens' },
        getUserImage: { method: 'GET', url: url + '/:username/:tokenValue/image' },
        recoverPassword: { method: 'PUT', url: url + '/password-tokens' },
        requestRecoverPassword: { method: 'POST', url: url + '/password-tokens' }
    };

    return $resource(url, defaultParams, actions);
}]);

/**
 * Authenticates the user with certicate
 */
securityModule.factory('LoginWithCertificateService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.autheo + '/users/certificateTokens/:organization';
  var defaultParams = {};

  var actions = {
    authenticate: { method: 'POST' }
  };

  return $resource(url, defaultParams, actions);
}]);

/**
 * Authenticates the user with credentials
 */
securityModule.factory('LoginOutService', ['environment', '$resource', function(environment, $resource) {

  var url = environment.autheo + '/tokens/:tokenValue/cache';
  var defaultParams = {};

  var actions = {
    logOutUser: { method: 'DELETE' }
  };

  return $resource(url, defaultParams, actions);
}]);

