'use strict';

adminModule.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/home');

  $stateProvider

    .state('user', {
      url: '/user',
      views: {
        'mainView': {
          controller: 'UsersController',
          templateUrl: '../../views/admin/tenant-admin/users/user-detail.html'
        }
      }
    })

    .state('processes', {
      url: '/processes',
      views: {
        'mainView': {
          controller: 'ProcessUploadController',
          templateUrl: '../../views/admin/tenant-admin/processes/process-upload.html'
        }
      }
    })

    .state('role', {
      url: '/role',
      views: {
        'mainView': {
          controller: 'RoleDetailController',
          templateUrl: '../../views/admin/tenant-admin/roles/role-detail.html'
        }
      }
    })

    .state('super-admin-home', {
      url: '/superAdminHome',
      views: {
        'mainView': {
          templateUrl: '../../views/admin/super-admin/home-super-admin.html'
        }
      }
    })

    .state('tenant-admin-home', {
      url: '/tenantAdminHome',
      views: {
        'mainView': {
          controller: 'HomeTenantAdminController',
          templateUrl: '../../views/admin/tenant-admin/home-tenant-admin.html'
        }
      }
    })

    .state('new-tenant-settings', {
      url: '/newTenantSettings',
      views: {
        'mainView': {
          controller: 'NewTenantSettingsController',
          templateUrl: '../../views/admin/super-admin/new-tenant-settings.html'
        }
      }
    })

    .state('assign-tenant-color', {
    url: '/assignTenantColor/:organizationId',
    views: {
      'mainView': {
        controller: 'AssignTenantColorController',
        templateUrl: '../../views/admin/super-admin/assign-tenant-color.html'
      }
    }
  });

}]);
