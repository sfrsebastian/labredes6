'use strict';

adminModule.controller('SettingsController', ['$scope', '$state', 'StateService', 'SettingsService', 'environment',
  'SessionService', 'TenantSettingService','$filter',
  function($scope, $state, StateService, SettingsService, environment, SessionService, TenantSettingService, $filter) {

    $scope.tenantSettings = SettingsService.getSettings({},getSettingsSuccess,getSettingsError);
    $scope.isSignatureImageSelected = false;
    $scope.isStampingFileSelected = false;
    $scope.isSigningFileSelected = false;

    $scope.signingFileSelected = function (files) {

      $scope.signingFile = files[0];
      $scope.tenantSettings.signingFileName = $scope.signingFile.name;
      console.log($scope.tenantSettings.signingFileName);
    };

    $scope.stampingFileSelected = function(files){

      $scope.stampingFile = files[0];
      $scope.tenantSettings.stampingFileName = $scope.stampingFile.name;
    };

    $scope.signatureImageSelected = function(files){

      $scope.signatureImage = files[0];
      $scope.tenantSettings.signatureImageName = $scope.signatureImage.name;
    };

    $scope.changeSigningFile = function(){

      $scope.isSigningFileSelected = false;
    };

    $scope.changeStampingFile = function(){

      $scope.isStampingFileSelected = false;
    };

    $scope.changeSignatureImage = function(){

      $scope.isSignatureImageSelected = false
    };


    $scope.saveSettings = function(tenantSettings){

      var formData = new FormData();
      if($scope.signingFile != null){
        formData.append('signingFile', $scope.signingFile);
      }
      if($scope.stampingFile != null){
        formData.append('stampingFile', $scope.stampingFile);
      }
      if($scope.signatureImage != null){
        formData.append('signatureImage', $scope.signatureImage);
      }
      formData.append('tenantSettings', JSON.stringify($scope.tenantSettings));
      TenantSettingService.saveTenantSettings(formData, saveSuccess, saveError);

      console.log($scope.tenantSettings);
    };

    function saveSuccess(data){

      var message = $filter('translate')('TENANT_SETTINGS_SUCCESS_SAVING');
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

    function saveError(data){
      $scope.tenantSettings = {};

      var message = $filter('translate')('TENANT_SETTINGS_ERROR_SAVING');
      console.error('settingsSaveError: ' + JSON.stringify(data));

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      }

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

    function getSettingsSuccess(data) {

      console.log(data);
      if (data.signingFileName != null) {
        $scope.isSigningFileSelected = true;
      }
      if (data.stampingFileName != null) {
        $scope.isStampingFileSelected = true;
      }
      if (data.signatureImageName != null) {
        $scope.isSignatureImageSelected = true;
      }
    };

    function getSettingsError(data){

      $scope.tenantSettings.name = SessionService.getUserOrganization();

      var message = $filter('translate')('TENANT_SETTINGS_ERROR_GETTING');
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
