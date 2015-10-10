/**
 * Created by LeanFactory on 9/03/15.
 */

'use strict';

ventanillaUnicaModule.controller('DefaultTaskController', ['$scope', 'DefaultTaskService','SessionService','MessageService','$state',
  function($scope, DefaultService, SessionService, MessageService, $state) {


    $scope.completeTask = function(){

      var defaultTaskServiceQueryParams = {

        taskId: $scope.$parent.currentTask.id

      };

      var jsonParams = {};

      $scope.$parent.formProperties.forEach(function(entry) {

          jsonParams[entry.id] = entry.value;

      });

      var jsonCompleteTaskJson = {

        requestId: $scope.$parent.currentRequest.id,
        taskName: $scope.$parent.currentTask.taskNameWhereProcessIs,
        formValues: jsonParams

      };

      DefaultService.completeTaskService ( defaultTaskServiceQueryParams, jsonCompleteTaskJson, completeSuccess, completeError )

    }

    function completeSuccess() {

      var message = 'La tarea fue asignada correctamente';

      MessageService.setMessage(message);

      $state.go('portal-funcionario-home');

    }

    function completeError(){

      var message = '';

      console.error('RequestError: ' + JSON.stringify(data));

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if (data.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = data.data.exception.errorMessage;

        $timeout(function(){

          $scope.showflag = false;

        },3000)
      }

      MessageService.setErrorMessage(message);
    }




  }]);
