portalFuncionarioModule.controller('FunctionaryHomeController', ['$scope', '$state', 'SessionService', 'TaskService', 'MessageService',

  function($scope, $state, SessionService, TaskService, MessageService) {

    TaskService.getMultipleTasks({ username: SessionService.getAuthorizationUserName() }, getMultipleTasksSuccess, getMultipleTasksError);
    TaskService.getMultipleTasksAlerts({ username: SessionService.getAuthorizationUserName() }, getMultipleTasksAlertsSuccess, getMultipleTasksAlertsError);
    TaskService.getHistoricTasks({ }, getHistoricTasksSuccess, getHistoricTasksError);

    function getMultipleTasksSuccess(data) {

      $scope.taskList = data;
    }

    function getMultipleTasksError(response) {

      var message = '';

      if (response.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if(response.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = response.data.errorMessage;
      }

      MessageService.setErrorMessage(message);
    }

    function getMultipleTasksAlertsSuccess(data) {

      $scope.alertList = data;

    }

    function getMultipleTasksAlertsError(response) {

      var message = '';
      console.error('settingsSaveError: ' + JSON.stringify(response));

      if (response.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if(response.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = response.data.errorMessage;
      }

      MessageService.setErrorMessage(message);
    }

    function getHistoricTasksSuccess(data) {

      $scope.completedTasks = data;

    }

    function getHistoricTasksError(response) {

      var message = '';

      if (response.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if(response.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = response.data.errorMessage;
      }

      MessageService.setErrorMessage(message);
    }

  }]);
