'use strict';

ventanillaUnicaModule.controller('RedactController', ['$scope', '$state', 'MessageService', 'SaveFileService', 
  '$timeout', 'ngDialog', 'RedactCompleteTaskService',
  function($scope, $state, MessageService, SaveFileService, $timeout, ngDialog, RedactCompleteTaskService) {

      $scope.caseId = $scope.$parent.currentTask.processInstanceId;
      $scope.certidocName = $scope.$parent.currentTask.certiParams.CERTI_DOCNOMBRE;
      $scope.certiTemplateName = $scope.$parent.currentTask.certiParams.CERTI_TEMPLATE;

      /**
       * Opens a window for confirming the redaction
       */
      $scope.openConfirmationDialog = function () {

        ngDialog.open({

          template: '../../views/portalFunctionary/typified-tasks/confirm-redaction.html',
          scope: $scope

        });

      };

      $scope.completeTask = function() {

        ngDialog.close();
        $scope.$broadcast('close-editor', true);

      }

      function getDocumentAsByteArrayFromEditorForSaving(eventContext, newDocumentByteArray) {

          SaveFileService.saveFile(newDocumentByteArray, eventContext.currentScope.caseId, eventContext.currentScope.certidocName, savingSuccess, 
            savingError);

          function savingSuccess(data){
            var message = 'Documento guardado exitosamente';
            MessageService.setMessage(message);
            $timeout(function(){
              MessageService.setMessage(null);
            },3005);
          }

          function savingError(data){
            var message = 'Error al guardar el documento';
            MessageService.setErrorMessage(message);
            $timeout(function(){
              MessageService.setErrorMessage(null);
            },3005);
          }
      }

      $scope.$on('close-document', getDocumentAsByteArrayFromEditor);

      function getDocumentAsByteArrayFromEditor(eventContext, newDocumentByteArray) {

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

        var jsonCompleteTaskJson = {

          requestId: $scope.$parent.currentRequest.id,
          caseId: eventContext.currentScope.caseId,
          taskName: $scope.$parent.currentTask.taskNameWhereProcessIs,
          certiDocumentName: eventContext.currentScope.certidocName,
          formValues: jsonParams,
          documentsList: finalDocumentList,
          signDocuments: $scope.signDocuments

        };

        // ArrayBuffer -> Blob
        var arrayBuffer = newDocumentByteArray.buffer;
        var blob        = new Blob([arrayBuffer]);

        var formData = new FormData();
        formData.append('file', blob);
        formData.append('taskDetails', JSON.stringify(jsonCompleteTaskJson));

        RedactCompleteTaskService.redactAndComplete(formData, $scope.$parent.$parent.currentTask.id, savingSuccess, savingError);

        function savingSuccess(data){
          var message = 'La tarea fue completada correctamente';
          MessageService.setMessage(message);
          $timeout(function(){
            MessageService.setMessage(null);
            $state.go('portal-funcionario-home');
          },3005);
        }

        function savingError(data){
          var message = 'Error al guardar el documento';
          MessageService.setErrorMessage(message);
          $timeout(function(){
            MessageService.setErrorMessage(null);
          },3005);
        }
      }

      $scope.$on('save-document', getDocumentAsByteArrayFromEditorForSaving);

}]);
