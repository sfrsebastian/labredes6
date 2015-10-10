'use strict';

/**
 * Created by mauricio on 26/01/15.
 */
ventanillaUnicaModule.config(['$stateProvider','$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/revisor-home');

  $stateProvider

    .state('ventanilla-unica-home', {
      url: '/ventanilla-unica-home',
      views: {
        'mainView': {
          controller: 'VentanillaUnicaHomeController',
          templateUrl: '../../views/ventanilla-unica/home.html'
        }
      }
    })

    .state('requests',{
      url: '/requests',
      views:{
        'mainView': {
          template: '<requests-table html-name="requests" is-alerts="false" status="PENDING"></requests-table>'
        }
      }
    })

    .state('alerts',{
      url: '/alerts',
      views:{
        'mainView': {
            template: '<requests-table html-name="alerts" is-alerts="true" status="PENDING"></requests-table>'
        }
      }
    })

    .state('reviewed',{
      url: '/reviewed',
      views:{
        'mainView': {
          controller: 'ReviewedRequestsController',
          templateUrl: '../../views/ventanilla-unica/requests/reviewed-requests.html'
        }
      }
    })

    .state('pdf-viewer', {
        url: '/pdf-viewer',
        views: {
            'mainView': {
                controller: 'PdfViewerController',
                templateUrl: 'views/documents/pdf-viewer/pdf-viewer.html'
            }
        }
    })

    .state('no-credentials', {
      url: '/no-credentials',
      views: {
        'mainView': {
          controller: 'VentanillaUnicaHomeController',
          templateUrl: '../../views/security/no-credentials.html'
        }
      }
    });
}]);
