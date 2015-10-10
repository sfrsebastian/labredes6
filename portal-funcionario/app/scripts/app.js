'use strict';

/**
 * @ngdoc overview
 * @name portalFuncionarioApp
 * @description
 * # portalFuncionarioApp
 *
 * Main module of the application.
 */
var portalFuncionarioModule = angular.module('portalFuncionarioModule', []);
var ventanillaUnicaModule = angular.module('ventanillaUnicaModule', []);
var securityModule = angular.module('securityModule', []);

var adminModule = angular.module('adminModule', ['colorpicker.module', 'fg']);

var documentsModule = angular.module('documentsModule', []);
var notificationsModule = angular.module('notificationsModule', []);
var directivesModule = angular.module('directivesModule', []);
var stateModule = angular.module('stateModule', []);

var portalFuncionarioApp = angular.module('portalFuncionarioApp', ['config', 'ngCookies', 'ngResource', 'ui.router', 'ngTable', 'notificationsModule',
  'portalFuncionarioModule', 'ventanillaUnicaModule', 'securityModule', 'documentsModule',
  'ngDialog', 'pdf', 'ui.bootstrap', 'angularMoment', 'pascalprecht.translate',
  'adminModule', 'directivesModule', 'stateModule', 'checklist-model','jsonFormatter']);

portalFuncionarioApp.config(function ($httpProvider, $stateProvider, $urlRouterProvider) {

  $httpProvider.interceptors.push('HttpInterceptorService');

  $urlRouterProvider.otherwise('/login');



});

portalFuncionarioApp.run(['$rootScope', '$state', '$location', 'MessageService', 'SessionService','SystemSettingsService', 'ErrorTranslateService',
  function ($rootScope, $state, $location, MessageService, SessionService, SystemSettingsService, ErrorTranslateService) {

  var publicStates = ['login', 'recover-password'];

  SystemSettingsService.getSystemSettings({},GetSystemSettingSuccess,GetSystemSettingsError);

  /**
   * Sets the subdomain as the user organization
   * @param data
   * @constructor
   */
  function GetSystemSettingSuccess(data){
    var domain = data.domain;
    var host = $location.host();
    var subdomainEndIndex = host.search( domain);
    var subdomainPart = host.substr(0,subdomainEndIndex-1);
    var periodIndex = subdomainPart.search(/\./);
    var subdomain = subdomainPart.substr(periodIndex+1,subdomainPart.length-periodIndex);
    if(subdomain==""){
      subdomain = "certicamara"; //Default organization name
    }
    SessionService.setAuthorizationUserOrganization(subdomain);
  }

  /**
   * Indicates the system settings have not been configured
   * @param data
   * @constructor
   */
  function GetSystemSettingsError(data){

    SessionService.setAuthorizationUserOrganization("certicamara");

    var message = ErrorTranslateService.translateErrorMessage(data);
    $.notify({
      // options
      message: message
    },{
      // settings
      type: 'danger',
      z_index : 9999,
      delay : 0
    });
  }

  function stateIsAPlubicState(state) {

    var isAPublicState = false;

    for (var i = 0; i < publicStates.length && !isAPublicState; i++) {

      isAPublicState = (state === publicStates[i]);
    }

    return isAPublicState;
  }

  $rootScope.$on('$stateChangeStart', function (event, toState, fromState, fromParams) {

    MessageService.setErrorMessage('');

    if (!SessionService.hasAuthorizationToken()) {

      if (!stateIsAPlubicState(toState.name)) {

        event.preventDefault();
        $state.transitionTo('login', {}, { reload: true, inherit: false, notify: true });
      }

    } else {

      if(toState.name =='home') {

        event.preventDefault();
        if(SessionService.getAuthorizationSystemRole() == 'revisor') {

          $state.go("ventanilla-unica-home");

        } else {

          $state.go("portal-funcionario-home");

        }
      }

    }
  });
}]);
