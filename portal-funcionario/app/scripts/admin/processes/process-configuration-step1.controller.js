'use strict';

adminModule.controller('ProcessConfigurationStep1Controller', ['$scope', '$state',
  function($scope, $state ) {

    $scope.$parent.currentStep = 1;
    $scope.processInfo = $scope.$parent.selectedProcess;

    $scope.next = function(processInfo) {

      $scope.$parent.processInfo = processInfo;
      $state.go('process-configuration.step2');
      
    };

    $scope.$on("processSelected", function(event, args) {

      $scope.processInfo = $scope.$parent.selectedProcess;

    });

    $scope.setValidity = function() {

      $scope.processConfigStep1Form.allowRevisorUploadDocuments.$valid = true;
      $scope.processConfigStep1Form.signUploadedDocuments.$valid = true;

    };



  }]);



