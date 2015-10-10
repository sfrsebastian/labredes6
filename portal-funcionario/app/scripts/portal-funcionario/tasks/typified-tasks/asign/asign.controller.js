/**
 * Created by LeanFactory on 9/03/15.
 */

'use strict';

ventanillaUnicaModule.controller('AssingController', ['$scope', 'AssignService', '$timeout', 'UserListService', 'MessageService', '$window', 
  '$state', 'SessionService',
  function($scope, AssignService, $timeout, UserListService, MessageService, $window, $state, SessionService) {

    var userListQueryParams = {
      roleId: $scope.$parent.currentTask.certiParams.CERTI_ROLE,
      tenantId: SessionService.getUserOrganization()
    };

    //$scope.userSelected={"selected":null};
    $scope.UserList = UserListService.getUsersByRoleId(userListQueryParams);

    function assignSuccess(){

      var message = 'La tarea fue asignada correctamente';
      MessageService.setMessage(message);

      $state.go('portal-funcionario-home');

    }

    function assignError(){
      var message = '';
      console.error('RequestError: ' + JSON.stringify(data));

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte. Error al hacer conexión con workFlowManager';

      } else if(data.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = data.data.exception.errorMessage;
        $timeout(function() {
          $scope.showflag = false;
        },3000)
      }

      MessageService.setErrorMessage(message);
    }

    $scope.assign = function() {

      var finalDocumentList = [];
      for(var i = 0; i < $scope.$parent.documentsList.length; i++) {

        var temporalDoc = $scope.$parent.documentsList[i];
        if(temporalDoc.temporalId) {

          finalDocumentList.push(temporalDoc);

        }

      }

      var assignServiceQueryParams = {
        taskId: $scope.$parent.currentTask.id
      };

      var jsonParams = {};
      $scope.$parent.formProperties.forEach(function(entry) {
        jsonParams[entry.id] = entry.value;
      });

      jsonParams[$scope.$parent.currentTask.certiParams.CERTI_ASSIGNEE] = $scope.userSelected;

      var jsonCompleteTaskJson = {

        requestId: $scope.$parent.currentRequest.id,
        taskName: $scope.$parent.currentTask.taskNameWhereProcessIs,
        formValues: jsonParams,
        documentsList: finalDocumentList,
        signDocuments: $scope.signDocuments

      };

      AssignService.assignService(assignServiceQueryParams, jsonCompleteTaskJson, assignSuccess, assignError)

    }

  }]);
