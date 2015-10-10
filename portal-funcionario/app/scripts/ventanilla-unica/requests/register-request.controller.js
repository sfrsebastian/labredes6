'use strict';

ventanillaUnicaModule.controller('RegisterRequestController', ['$scope', '$state', 'RequestsService', 'FileService', 'MessageService',
  'environment', 
  function($scope, $state,  RequestsService, FileService, MessageService, environment) {

    $scope.currentRequest = {};
    $scope.currentRequest.procedure = {};
    $scope.currentRequest.procedure.name = "PQR";
    $scope.currentRequest.procedure.id  = 1;
    $scope.currentRequest.user = {};
    $scope.currentRequest.user.name = "juan perez";
    $scope.currentRequest.user.id = "10398478";
    $scope.currentRequest.newFile=null;
    $scope.currentRequest.docs = [];

    $scope.setFileToScope = function(file){
      this.currentRequest.currentFile = file;
    }

    $scope.uploadFile = function(){
      FileService.uploadTemporalFile(this.currentRequest.currentFile,success,error);
    }

    $scope.insertRequest = function(){

      RequestsService.registerRequest($scope.currentRequest);

    }

    function success(data){
      $scope.temporalId = data.temporalFileId;
      $scope.currentRequest.docs.push({id:data.temporalFileId,name:$scope.currentRequest.currentFile[0].name});
    }

    function error(data){
      alert('error uploading!!!');
    }


  }]);

