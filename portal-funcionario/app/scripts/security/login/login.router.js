'use strict';

securityModule.config(function($stateProvider) {

  $stateProvider

  .state('login', {

    url: '/login',
    views: {
      'mainView': {
        controller: 'LoginController',
        templateUrl: '../views/security/login.html'
      }
    }
  })

  .state('recover-password', {

      url: '/recover-password?token',
      onEnter: ['$state', '$modal', function($state, $modal) {
        $modal.open({
            controller: 'RecoverPasswordController',
            templateUrl: '../../../views/security/recover-password.html'
        });
      }]
    });

});
