'use strict';

ventanillaUnicaModule.controller('RequestRejectionController', ['$scope', '$state', 'SessionService', 'StateService', 'DocumentsService',
  'FileService', 'MessageService', 'RequestsService', 'ngDialog', 'environment', 'requestId', '$filter',
  function($scope, $state, SessionService, StateService, DocumentsService, FileService, MessageService, RequestsService,
           ngDialog, environment, requestId, $filter) {

    $scope.currentRequestId = requestId;
    $scope.isLoading = false;
    $scope.loadingMessage = "Loading ...";
    $scope.showButtons = true;

    function createTemporalFileWithHashSuccess(response) {

      $scope.isLoading = true;

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

        $scope.loadingMessage = $filter('translate')('REJECTION_WAITING_CERTIFICATE_SELECTION');
        var applet = document.getElementById('appletfirma');
        applet.Sign(response.pdfHash, "archivoDist", false);

      } catch(e) {

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

      }
    }

    function createTemporalFile() {//TODO all these steps should be done in single window

      FileService.uploadTemporalFile($scope.documentAsByteArray, createTemporalFileSuccess, uploadTemporalFileError);

    }

    function createTemporalFileSuccess(data) {

      FileService.createODTWithQRFooter(data.temporalFileId, createODTWithQRFooterSuccess, uploadTemporalFileError);

    }

    function createODTWithQRFooterSuccess(data) {

      FileService.createTemporalFileWithHash(data.temporalFileId, createTemporalFileWithHashSuccess, uploadTemporalFileError);

    }

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

    function updatesuccess(response) {

      var requestId = "";
      if($scope.currentRequestId.contains('_')){
        requestId = $scope.currentRequestId.split('_')[0];
      }else{
        requestId = $scope.currentRequestId;
      }

      var message = $filter('translate')('REJECTION_SUCESS') + ": " + requestId;
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 10000
      });
      $state.go('ventanilla-unica-home');
    }

    function updateError(response) {
    }

    $scope.$watch('valueChanged', function(newValue, oldValue) {
      if(newValue) {

        var documentJson = {

          name: 'rejection-' + requestId,
          caseId: '',
          requestId: requestId,
          temporalFileHashId: $scope.temporalFileHashId,
          signedFileHash: $scope.signedHash
        };
        $scope.loadingMessage = $filter('translate')('REJECTION_SIGNING_REJECTION');
        RequestsService.rejectRequest({ requestId: requestId }, documentJson, updatesuccess, updateError);
      }
    });

    function rejectRequestSuccess(response) {

      console.debug('rejectRequestSuccess: ' + response);
      $state.go("home");
    }

    function rejectRequestError(response) {

      console.error('rejectRequestError: ' + response);
    }

    function uploadTemporalFileError(response) {
      console.error('uploadTemporalFileError: ' + response);
    }

    function getDocumentAsByteArrayFromEditor(eventContext, newDocumentByteArray) {

      console.debug('getDocumentAsByteArrayFromEditor');
      console.debug(newDocumentByteArray);

      if (newDocumentByteArray.length > 0) {

        $scope.documentAsByteArray = newDocumentByteArray;

        dtjava.addOnloadCallback(createTemporalFile);

      }
    }

    $scope.$on('close-document', getDocumentAsByteArrayFromEditor);

    $scope.handleSignedRequestRejection = function() {

      $scope.isLoading = true;
      $scope.loadingMessage = $filter('translate')('REJECTION_LOADING_APPLET');
      console.debug('handleSignedRequestRejection: ' + requestId);
      /*
       * When we change this value to true, the EditorController saves the document as byte array and store that array into the EditorService.
       * When the documentAsByteArray value at EditorService changes, then the function getDocumentAsByteArrayFromService activates.
       */
      // EditorService.setCloseDocument(true);
      $scope.$broadcast('close-editor', true);
    };
  }]);
