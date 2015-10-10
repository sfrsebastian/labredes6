'use strict';

portalFuncionarioModule.config(['$stateProvider','$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/functionary-home');

  $stateProvider

    .state('portal-funcionario-home', {
      url: '/portal-funcionario-home',
      views: {
        'mainView': {
          controller: 'FunctionaryHomeController',
          templateUrl: '../../views/portalFunctionary/home.html'
        }
      }
    })

    .state('tasks', {
      url: '/tasks',
      views:{
        'mainView': {
          template: '<tasks-table html-name="tasks-list" is-alerts="false"></tasks-table>'
        }
      }
    })

    .state('alert-tasks', {
      url: '/alert-tasks',
      views:{
        'mainView': {
          template: '<tasks-table html-name="alert-tasks" is-alerts="true"></tasks-table>'
        }
      }
    })

    .state('task-detail', {
      url: '/task-detail/:taskId?isHistoric',
      views:{
        'mainView': {
          controller: 'TaskDetailController',
          templateUrl: '../views/portalFunctionary/task-details.html'
        }
      }
    })

    .state('completed-tasks', {
      url: '/completed-tasks',
      views:{
        'mainView': {
          controller: 'CompletedTasksController',
          templateUrl: '../views/portalFunctionary/completed-tasks.html'
        }
      }
    })

    .state('request-trace-report', {
      url: '/request-trace-report',
      views:{
        'mainView': {
          controller: 'RequestTraceReport',
          templateUrl: '../views/portalFunctionary/reports/request-trace-report.html'
        }
      }
    });
}]);
