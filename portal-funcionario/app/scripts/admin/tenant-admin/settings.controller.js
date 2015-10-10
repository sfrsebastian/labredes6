'use strict';

 adminModule.controller('SettingsController', ['$scope', '$state', 'StateService', 'SettingsService', 'MessageService', 'environment',
  'SessionService', function($scope, $state, StateService, SettingsService, MessageService, environment,SessionService) {

   $scope.tenantSettings = SettingsService.getSettings({},getSettingsSuccess,getSettingsError);


   $scope.saveSettings = function(tenantSettings){
     SettingsService.saveSettings(tenantSettings, saveSuccess, saveError);

     function saveSuccess(data){
        alert("Guardado correctamente");
     };

     function saveError(data){
       $scope.tenantSettings = {};

       var message = '';
       console.error('settingsSaveError: ' + JSON.stringify(data));

       if (data.status == 0) {

         message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

       } else {

         message = data.data.message;
       }

       MessageService.setErrorMessage(message);
     };
   };

   function getSettingsSuccess(data){
     //do nothing
   };

   function getSettingsError(data){
     $scope.tenantSettings.name = SessionService.getUserOrganization();

     var message = '';
     console.error('settingsSaveError: ' + JSON.stringify(data));

     if (data.status == 0) {

       message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

     } else {

       message = data.data.message;
     }

     MessageService.setErrorMessage(message);
   };
}]);
