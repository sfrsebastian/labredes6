'use strict';

adminModule.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/home');

  $stateProvider

    .state('settings', {
      url: '/settings',
      views: {
        'mainView': {
          controller: 'SettingsController',
          templateUrl: '../../views/admin/tenant-admin/tenant-settings.html'
        }
      }
    })

    .state('process-configuration', {
      url: '/process-configuration',
      views: {
        'mainView': {
          controller: 'ProcessConfigurationController',
          templateUrl: '../../views/admin/processes/process-configuration.html'
        }
      }
    })

    .state('process-configuration.step1', {
      views: {
        'processConfigurationWizardView': {
          controller: 'ProcessConfigurationStep1Controller',
          templateUrl: '../../views/admin/processes/process-configuration-step1.html'
        }
      }
    })
    .state('process-configuration.step2', {
      views: {
        'processConfigurationWizardView': {
          controller: 'ProcessConfigurationStep2Controller',
          templateUrl: '../../views/admin/processes/process-configuration-step2.html'
        }
      }
    })
    .state('process-configuration.step3', {
      views: {
        'processConfigurationWizardView': {
          controller: 'ProcessConfigurationStep3Controller',
          templateUrl: '../../views/admin/processes/process-configuration-step3.html'
        }
      }
    })

    .state('process-configuration.no-step', {
      views: {
        'processConfigurationWizardView': {
        }
      }
    })

    .state('system-settings', {
      url: '/system-settings',
      views: {
        'mainView': {
          controller: 'SystemSettingsController',
          templateUrl: '../../views/admin/systemSettings/system-settings.html'
        }
      }
    })

    .state('edit-tenant-settings', {
      url: '/edit-tenant-settings',
      views: {
        'mainView': {
          controller: 'SystemSettingsController',
          templateUrl: '../../views/admin/systemSettings/system-settings.html'
        }
      }
    })

    .state('tenant-list', {
      url: '/tenant-list',
      views: {
        'mainView': {
          controller: 'TenantListController',
          templateUrl: '../../views/admin/super-admin/tenant-list.html'
        }
      }
    })

    .state('undelivered-messages', {
      url: '/undelivered-messages',
      views: {
        'mainView': {
          controller: 'UndeliveredMessagesController',
          templateUrl: '../../views/admin/systemSettings/undelivered-messages/undeliveredMessages.html'
        }
      }
    })
}]);
