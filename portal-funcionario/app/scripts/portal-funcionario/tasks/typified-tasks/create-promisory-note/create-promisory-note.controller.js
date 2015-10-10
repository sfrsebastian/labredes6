'use strict';

ventanillaUnicaModule.controller('CreatePromisoryNoteController', ['$scope', '$state', 'AssignService',
  '$timeout', 'ngDialog', 'FileService', 'DocumentsService',
  function($scope, $state, AssignService, $timeout, ngDialog, FileService, DocumentsService) {

    $scope.isLoading = false;
    $scope.invoiceReady = false;

    $scope.generatePromisoryNote = function(){

      $scope.isLoading = true;
      $timeout(function(){$scope.invoiceReady = true;$scope.isLoading = false;}, 3000);
    };

    $scope.continue = function(){

      var xhr = new XMLHttpRequest();
      xhr.open('GET', '/files/pagare.pdf', true);
      xhr.responseType = 'blob';

      xhr.onload = function(e) {

        //var uInt8Array = new Uint8Array(this.response);
        var blob = new Blob([this.response], {type: 'application/pdf'});

        //var fileByteArray = uInt8Array;
        var formData = new FormData();
        formData.append('file', blob);
        FileService.uploadTemporalPdfFile(formData,'pdf', uploadTempSuccess, uploadTempError);

      };
      xhr.send();

    };

    function uploadTempSuccess(data){

      console.log(data);
      var createDoc = [{
        name : "pagare.pdf",
        requestId : $scope.$parent.$parent.currentRequest.id,
        caseId :  $scope.$parent.$parent.currentRequest.idCase,
        temporalFileId : data.temporalFileId,
        fileType: 'pdf',
        ownerName: 'victoru',
        ownerRol: 'estudiante',
        documentType:'evidence0'
      }];

      DocumentsService.postCitizenDocument(createDoc, createDocSuccess, createDocError);


    };

    function completeTaskSuccess(){

      console.log("generate invoice task completed");
      $state.go('portal-funcionario-home');
    };

    function completeTaskError(){
      console.log("generate invoice task error");
    };

    function createDocSuccess(data){

      var jsonParams = {};

      var assignServiceQueryParams = {
        taskId: $scope.$parent.currentTask.id
      };

      var completeTaskDTO = {
        requestId : 0,
        taskName : '',
        formValues: jsonParams
      }

      AssignService.assignService(assignServiceQueryParams, completeTaskDTO, completeTaskSuccess, completeTaskError);
      console.log(data);
    };

    function createDocError(data){
      console.log(data);
    };

    function uploadTempError(data){

      console.log(data);
    };


  }]);
