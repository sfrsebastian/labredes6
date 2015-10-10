'use strict';

adminModule.controller('ProcessConfigurationController', ['$scope', '$filter', 'ProcessSettingsService','$state',
  function($scope, $filter, ProcessSettingsService, $state ) {

    $scope.processList = [];
    ProcessSettingsService.getAllProcedureSettings({}, GetProcedureSettingsSuccess, GetProcedureSettingsError);

    //Vars
    $scope.processInfo = {};
    $scope.processUploaded = false;
    $scope.processFormSchema = '';

    $scope.selectedProcess = "";
    $scope.currentStep = 0;

    $scope.selectProcess = function(process){

      $scope.selectedProcess = process;
      $scope.$broadcast("processSelected",{process:process});
    };

    $scope.$on('configurationFinished', function (event, args) {
      
      var procedureSettings = {
        clientId : '',
        description : $scope.processInfo.description,
        _id : $scope.processInfo.name,
        timeToExpireRequest : $scope.processInfo.timeToExpireRequest,
        timeToExpireProcess : $scope.processInfo.timeToExpireProcess,
        timeToAlertRequest : $scope.processInfo.timeToAlertRequest,
        timeToAlertProcess : $scope.processInfo.timeToAlertProcess,
        allowRevisorUploadDocuments : $scope.processInfo.allowRevisorUploadDocuments,
        signUploadedDocuments : $scope.processInfo.signUploadedDocuments,
        processFormSchema: $scope.processFormSchema
      };

      ProcessSettingsService.saveProcedureSettings(procedureSettings, SaveProcedureSettingsSuccess, SaveProcedureSettingsError);
      $scope.selectedProcess = "";
      $scope.currentStep = 0;
    });

    $scope.newProcessSettings = function() {

      $state.go('process-configuration.no-step');
      $scope.processList.push({name:'new Process', processFormSchema:'{}'});
      $scope.selectedProcess = "";
      $scope.currentStep = 0;
    };


    function SaveProcedureSettingsSuccess(data) {

      $state.go('process-configuration');
      ProcessSettingsService.getAllProcedureSettings({}, GetProcedureSettingsSuccess, GetProcedureSettingsError);
      var message = $filter('translate')('PROCESS_SETTINGS_SAVING_SUCCESS');
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

    function SaveProcedureSettingsError(data) {

      var message = $filter('translate')('PROCESS_SETTINGS_SAVING_ERROR');
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

    function GetProcedureSettingsSuccess(data) {

      $scope.processList = data;
    };

    function GetProcedureSettingsError(data) {

      var message = $filter('translate')('PROCESS_SETTINGS_GETPROCEDURES_ERROR');
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



