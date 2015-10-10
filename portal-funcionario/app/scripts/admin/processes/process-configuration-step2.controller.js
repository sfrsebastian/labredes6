'use strict';

adminModule.controller('ProcessConfigurationStep2Controller', ['$scope', '$filter', '$state','ProcessesService',
  function($scope, $filter, $state, ProcessesService ) {

    $scope.isLoading = false;
    $scope.$parent.currentStep = 2;

    $scope.next = function(){

      $state.go('process-configuration.step3');
    };

    $scope.uploadFile= function(file) {

      $scope.isLoading = true;
      var fd = new FormData();
      fd.append('file',file[0]);

      ProcessesService.uploadTemporalFile(fd, success, error);

      function success(data){

        $scope.isLoading = false;
        var message = $filter('translate')('PROCESS_UPLOAD_SUCCESS');
        $.notify({
          // options
          message: message
        },{
          // settings
          type: 'success',
          z_index : 9999,
          delay : 10000
        });

      }

      function error(data){

        $scope.isLoading = false;
        var message = $filter('translate')('PROCESS_UPLOAD_FAILED');
        $.notify({
          // options
          message: message
        },{
          // settings
          type: 'danger',
          z_index : 9999,
          delay : 10000
        });
      }
    }

  }]);

  

  
