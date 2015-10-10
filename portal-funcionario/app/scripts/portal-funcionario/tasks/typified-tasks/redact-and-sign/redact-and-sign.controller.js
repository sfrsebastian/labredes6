'use strict';

ventanillaUnicaModule.controller('RedactSignController', ['$scope', '$state', 'StateService', 'RequestsService', 'MessageService', 'SaveFileService',
  'AssignService', '$timeout', 'FileService', 'RedactSignService', 'SessionService',
  function($scope, $state, StateService, RequestsService, MessageService, SaveFileService, AssignService, $timeout, FileService,
    RedactSignService, SessionService) {

    $scope.caseId = $scope.$parent.currentTask.processInstanceId;
    $scope.certidocName =  $scope.$parent.currentTask.certiParams.CERTI_DOCNOMBRE;
    $scope.certiTemplateName = $scope.$parent.currentTask.certiParams.CERTI_TEMPLATE;
    $scope.isLoading = false;
    $scope.loadingMessage = "Cargando";
    $scope.showButtons = true;
    $scope.currentLoadAppletAttemps = 0; //the number of times the applet has tried to load when an error loading it ocurres

    console.debug('certidocName: ' + $scope.certidocName);

    $scope.completeTaskNoSign = function() {

      $scope.showButtons = false;
      var assignServiceQueryParams = {
        taskId: $scope.$parent.currentTask.id
      };

      var jsonParams = {};
      $scope.$parent.formProperties.forEach(function(entry) {
        jsonParams[entry.id] = entry.value;
      });

      jsonParams[$scope.$parent.currentTask.certiParams.CERTI_APPROVED] = "false";
      var completeTaskDTO = {
        requestId : 0,
        taskName : '',
        formValues: jsonParams
      }

      AssignService.assignService(assignServiceQueryParams, completeTaskDTO, completeTaskSuccess, completeTaskError);
    };

    $scope.completeTaskSigning = function(){

      $scope.showButtons = false;
      $scope.isLoading = true;
      $scope.loadingMessage = "Cargando componente de firma";
      $scope.$broadcast('close-editor', true);
    };

    $scope.$on('close-document', getDocumentAsByteArrayFromEditor);

    $scope.$on('save-document', getDocumentAsByteArrayFromEditorForSaving);

    $scope.$watch('valueChanged', function(newValue, oldValue) {

      if(newValue) {

        var finalDocumentList = [];
        for(var i = 0; i < $scope.$parent.documentsList.length; i++) {

          var temporalDoc = $scope.$parent.documentsList[i];
          if(temporalDoc.temporalId) {

            finalDocumentList.push(temporalDoc);

          }

        }

        var documentJson = {

          name: $scope.certidocName,
          caseId: $scope.caseId,
          requestId: $scope.$parent.$parent.currentRequest.id,
          temporalFileHashId: $scope.temporalFileHashId,
          signedFileHash: $scope.signedHash,
          taskName: $scope.$parent.currentTask.taskNameWhereProcessIs
        };

        var jsonParams = {};
        $scope.$parent.formProperties.forEach(function(entry) {
          jsonParams[entry.id] = String(entry.value);
        });

        jsonParams[$scope.$parent.currentTask.certiParams.CERTI_APPROVED] = "true";

        var redactSignDTO = {
          signedDocument : documentJson,
          formValues: jsonParams,
          documentsList: finalDocumentList,
          signDocuments: $scope.signDocuments
        }

        $scope.isLoading = true;
        $scope.loadingMessage = "Firmando Documento";
        RedactSignService.redactAndSign({taskId:$scope.$parent.$parent.currentTask.id}, redactSignDTO, completeTaskSuccess, completeTaskError);
      }
    });


    $scope.safeApply = function( fn ) {
      var phase = this.$root.$$phase;
      if(phase == '$apply' || phase == '$digest') {
        if(fn) {
          fn();
        }
      } else {
        this.$apply(fn);
      }
    };

    function createTemporalFileWithHashSuccess(response) {

      try {

        dtjava.embed(
          {
            id: 'appletfirma',
            url : '/lib/FXWebFormSign4J.jnlp',
            placeholder : 'javafxappplaceholder',
            width : 1,
            height : 1
          },
          {
            javafx : '2.2+'
          },
          {}
        );

        $scope.temporalFileHashId = response.temporalFileHashId;

        var applet = document.getElementById('appletfirma');
        applet.Sign(response.pdfHash, "archivoDist", false);
        $scope.loadingMessage = "Esperando selección de certificado";

      } catch(e) {

        if($scope.currentLoadAppletAttemps > 3 ){

          $scope.isLoading = false;
          var message = "Ha ocurrido un error al cargar el componente de firma. Por favor recargue la página.";
          $.notify({
            // options
            message: message
          },{
            // settings
            type: 'danger',
            z_index : 9999,
            delay : 10000,
            placement:{
              align:'center'
            }
          });
        }else{

          console.log("try # " + $scope.currentLoadAppletAttemps)
          $scope.currentLoadAppletAttemps ++;
          $scope.loadingMessage = "Espere un momento por favor";
          setTimeout(function(){ createTemporalFileWithHashSuccess(response)  }, 3000);
        }

      }

    }


    function uploadTemporalFileError(response) {
      console.log(response);
    }

    function createTemporalFile() {//TODO all these steps should be done in portal functionary

        FileService.uploadTemporalFile($scope.documentAsByteArray, createTemporalFileSuccess, uploadTemporalFileError);

    }

    function createTemporalFileSuccess(data) {

        FileService.createODTWithQRFooter(data.temporalFileId, createODTWithQRFooterSuccess, uploadTemporalFileError);

    }

    function createODTWithQRFooterSuccess(data) {

        FileService.createTemporalFileWithHash(data.temporalFileId, createTemporalFileWithHashSuccess, uploadTemporalFileError);

    }

    function getDocumentAsByteArrayFromEditor(eventContext, newDocumentByteArray) {

      $scope.documentAsByteArray = newDocumentByteArray;
      dtjava.addOnloadCallback(createTemporalFile);

    }

    function savingSuccess(data) {

      var message = 'Documento guardado exitosamente';
      MessageService.setMessage(message);
      $timeout(function(){
        MessageService.setMessage(null);
      },3005);

    }

    function savingError(data) {

      var message = 'La tarea fue completada correctamente';
      MessageService.setErrorMessage(message);
      $timeout(function(){
        MessageService.setErrorMessage(null);
      },3005);

    }

    function completeTaskSuccess() {

      var message = 'La tarea fue completada correctamente';
      MessageService.setMessage(message);

      $timeout(function() {
        MessageService.setMessage(null);
        $state.go('portal-funcionario-home');
      },3005);

    }

    function completeTaskError(data) {
      var message = '';
      console.error('RequestError: ' + JSON.stringify(data));

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte. Error al hacer conexión con workFlowManager';

      } else if(data.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = data.data.exception.errorMessage;
      }
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

  }]);
