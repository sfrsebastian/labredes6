'use strict';

ventanillaUnicaModule.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/revisor-home');

    $stateProvider.state('request-detail', {

        url: '/request-detail/:requestId',

        views: {
            'mainView': {
                controller: 'RequestDetailController',
                templateUrl: '../../views/ventanilla-unica/requests/request-details.html'
            }
        }
    })

    .state('reject-request', {

        url: '/reject-request/:requestId',

        views: {
            'mainView': {

                resolve: {

                    requestId: function($stateParams) {
                        return $stateParams.requestId;
                    }
                },

                controller: 'RequestRejectionController',
                templateUrl: '../../views/ventanilla-unica/requests/reject-request.html'
            }
        }
    })
      .state('register-request', {

        url: '/register-request',

        views: {
          'mainView': {
            controller: 'RegisterRequestController',
            templateUrl: '../../../views/ventanilla-unica/requests/register-request.html'
          }
        }
      });

}]);
