'use strict';

adminModule.controller('SystemSettingsController', ['$scope', '$state', 'environment','SystemSettingsService','$filter',
  'environment', 'SessionService', function($scope, $state, environment, SystemSettingsService, $filter) {

    $scope.systemSettings = {};
    $scope.systemSettings.name = "SystemSettings1";
    $scope.verificationTypes = [
      {"name":"OCSP"},
      {"name":"CRL"}
    ];
    SystemSettingsService.getSystemSettings({}, GetSystemSettingsSuccess, GetSystemSettingsError);

    $scope.isSaving = false;

    $scope.save=function(){
      $scope.isSaving = true;
      SystemSettingsService.saveSystemSettings($scope.systemSettings, SaveSystemSettingsSuccess, SaveSystemSettingsError)
    };

    function GetSystemSettingsSuccess(data){

      $scope.systemSettings = data;
    };

    function GetSystemSettingsError(data){

    };

    function SaveSystemSettingsSuccess(data){

      $scope.isSaving = false;
      var message =  $filter('translate')('SYSTEM_SETTING_SUCCESS_SAVING');
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 10000
      });
    };

    function SaveSystemSettingsError(data){

      $scope.isSaving = false;
      var message =  $filter('translate')('SYSTEM_SETTING_ERROR_SAVING');
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 10000
      });
    };

  }]);


