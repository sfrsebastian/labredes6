'use strict';

adminModule.controller('ProcessConfigurationStep3Controller', ['$scope', 'ProcessesService', '$filter',
  function($scope, ProcessesService, $filter ) {

    $scope.$parent.currentStep = 3;
    $scope.userForm = JSON.parse($scope.$parent.selectedProcess.processFormSchema);

    $scope.finish = function() {

      $scope.$parent.processFormSchema = JSON.stringify($scope.userForm);
      $scope.$emit('configurationFinished', { });

    };

    $scope.schema = function() {

      console.log(JSON.stringify($scope.userForm))
    };

    $scope.$on("processSelected", function(event, args) {

      $scope.userForm = JSON.parse($scope.$parent.selectedProcess.processFormSchema);
    });


  }]);



