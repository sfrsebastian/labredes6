'use strict';

portalFuncionarioModule.controller('CaseDiagramController', ['$scope', 'environment'
  ,'$timeout','$window','ProcessInstanceService',
  function($scope, environment, $timeout, $window, ProcessInstanceService) {
    ProcessInstanceService.getProcessDiagram({processInstanceId:$scope.$parent.currentTask.processInstanceId}, GetDiagramSuccess, GetDiagramError);

    function GetDiagramSuccess(data){

      $scope.diagramUrl = data.imageBase64;
    };

    function GetDiagramError(data){

    };
}]);
