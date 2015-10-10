'use strict';

ventanillaUnicaModule.controller('RequestDetailController', ['$scope', '$state', 'DocumentsService', 'VersionsService',
  'VersionsUrlService', 'RequestsService', 'ngDialog', 'UserListService', 'ProcessInstanceService', '$filter', 'SessionService',
  'ProcessSettingsService',
  function($scope, $state, DocumentsService, VersionsService, VersionsUrlService, RequestsService, ngDialog, UserListService, ProcessInstanceService,
    $filter, SessionService, ProcessSettingsService ) {

    var caseId = '';

    $scope.disableButtons = false;
    $scope.showTaskInfo = false;
    $scope.showDocumentList = false;
    $scope.showRequestView = false;
    $scope.showTaskInfoSpiner = true;
    $scope.showNotFoundTaskInfo = false;
    $scope.currentTask = null;

    $scope.documentIconClass = 'glyphicon glyphicon-chevron-right';
    $scope.taskDetailInfoIconClass = 'glyphicon glyphicon-chevron-right';

    //Initialization for adding documents
    var noFileSelectedMessage = $filter('translate')('ADD_DOCUMENTS_NO_FILE_SELECTED');
    $scope.noFileSelectedMessage = noFileSelectedMessage;

    //The list of evidences used for the dynamic form
    $scope.evidences = [];
    $scope.evidences.push({name: noFileSelectedMessage});
    $scope.evidences.push({name: noFileSelectedMessage});
    $scope.evidences.push({name: noFileSelectedMessage});

    $scope.documentsList = [];

    RequestsService.getRequestById({requestId: $state.params["requestId"]}, requestSuccess, errorTransaction);

    /**
     * Object to identify the role to get the users for assigning
     * @type {{roleId: string}}
     */
    var bossQueryParams = {
      roleId: 'jefe',
      tenantId: SessionService.getUserOrganization()
    };

    $scope.bossSelected={"selected":null};

    /**
     * Gets the user list for assigning the case
     */
    $scope.bossList = UserListService.getUsersByRoleId(bossQueryParams);

    /**
     * Gets the version list of a single document
     * @param documentId
     * @param fileName
     */
    $scope.getVersionsList = function (documentId, fileName) {

      $scope.documentId = documentId;
      $scope.fileName = fileName;
      VersionsService.getDocumentVersions({ documentId: documentId }, getDocumentVersionsSuccess, getDocumentVersionsError);

      function getDocumentVersionsSuccess(documentVersionsList) {

        $scope.documentVersionsList = documentVersionsList;

        ngDialog.open({

          controller: 'VersionsController',
          template: '../../views/documents/versions/versions-list.html',
          scope: $scope
        });
      }

      function getDocumentVersionsError(data) {

        $scope.documentList = null;
        $scope.versions = null;

        errorTransaction(data);

      }
    };

    /**
     * Opens the latest version of a document in a popup window
     * @param doc
     */
    $scope.openLatestVersionPopUp = function(doc) {

      $scope.currentDocumentId = doc.id;
      VersionsUrlService.getVersionFileUrl(doc, getDocSuccess, getDocError);

    };

    /**
     * Handles the opened event for the popup
     */
    $scope.$on('ngDialog.opened', function (event, $dialog) {
      $dialog.find('.ngdialog-content').css('width', '80%');
    });

    /**
     * Opens a window for writing observations
     */
    $scope.openObservations = function () {

      if($scope.bossSelected.selected) {

        ngDialog.open({

          template: '../../views/ventanilla-unica/requests/assigneeObservations.html',
          controller: 'ObservationsController',
          scope: $scope
        });
      }
      else {
        var message = 'Debe seleccionar una persona para iniciar el trámite';
        $.notify({
          // options
          message: message
        },{
          // settings
          type: 'info',
          z_index : 9999,
          delay : 10000,
          placement:{
            align:'center'
          }
        });
      }
    };

    /**
     * Approve the request
     * @param observations
     */
    $scope.approveRequest = function (observations) {

      var finalDocumentList = [];
      for(var i = 0; i < $scope.documentsList.length; i++) {

        var temporalDoc = $scope.documentsList[i];
        if(temporalDoc.temporalId) {

          finalDocumentList.push(temporalDoc);

        }

      }

      RequestsService.approveRequest({requestId: $scope.currentRequest.id}, {
        idRequest: $scope.currentRequest.id,
        assignedPerson: $scope.bossSelected.selected.username,
        observations: observations,
        documentsList: finalDocumentList,
        signDocuments: $scope.signDocuments
      }, postSuccess, errorTransaction);
    };

    /**
     * Opens a window to reject the request
     * @param requestId
     */
    $scope.showRejectionDialog = function(requestId) {
      $scope.disableButtons = true;
      $state.go('reject-request', {requestId: requestId});
    };

    /**
     * Called when the request has been successful
     * @param data
     */
    function requestSuccess(data) {

      if (data != null) {

        caseId = data.idCase;
        $scope.isPending = data.requestStatus == "PENDING" ? true : false;

        $scope.currentRequest = data;


        ProcessSettingsService.getProcedureSettings({procedureName: data.procedure}, procedureSettingsSuccess, errorTransaction);

      }

      $scope.showRequestView = true;
    }

    /**
     * Called when the get procedure settings call has been successful
     * @param data
     */
    function procedureSettingsSuccess(data) {

      $scope.showAddDocuments = data.allowRevisorUploadDocuments;
      $scope.signDocuments = data.signUploadedDocuments;

    }

    $scope.getTaskInfo = function (){

      if ($scope.taskDetailInfoIconClass == 'glyphicon glyphicon-chevron-down'){

        $scope.taskDetailInfoIconClass = 'glyphicon glyphicon-chevron-right';

      } else if ($scope.taskDetailInfoIconClass == 'glyphicon glyphicon-chevron-right'){

        $scope.taskDetailInfoIconClass = 'glyphicon glyphicon-chevron-down';

      }

      if(caseId != '' && caseId != null && $scope.showTaskInfo == false){

        ProcessInstanceService.getCurrentTask({'processInstanceId':caseId}, getCurrentTaskSuccess, errorTransaction);

      } else if ($scope.showTaskInfo == false && $scope.showNotFoundTaskInfo == false){

        $scope.showNotFoundTaskInfo = true;
        $scope.showTaskInfoSpiner = false;

      }
    };

    $scope.getDocumentList = function (){

      if ($scope.documentIconClass == 'glyphicon glyphicon-chevron-down'){

        $scope.documentIconClass = 'glyphicon glyphicon-chevron-right';

      } else if ($scope.documentIconClass = 'glyphicon glyphicon-chevron-right'){

        $scope.documentIconClass = 'glyphicon glyphicon-chevron-down';

      }

      if  ($scope.documentList == null) {

        DocumentsService.getDocuments({requestId: $scope.currentRequest.id}, getDocumentsSuccess, errorTransaction);
      }
    };

    /**
     * Called when the request to documentList has been successful
     * @param documentList
     */
    function getDocumentsSuccess(documentList) {
      $scope.documentList = documentList;
      $scope.showDocumentList = true;
    }

    /**
     * Called when the request approval has been successful
     * @param data
     */
    function postSuccess(data) {

      var message = 'La tarea fue asignada correctamente';

      $scope.disableButtons = true;

      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 10000,
        placement:{
          align:'center'
        }
      });

      $state.go('ventanilla-unica-home');
    }

    /**
     * Called when the task of the current request has been successfully brought
     * @param data
     */
    function getCurrentTaskSuccess(data) {

      if(data=='') {
        $scope.currentTask = {};
        $scope.currentTask.taskNameWhereProcessIs = $filter('translate')('TASK_DETAIL_STATUS_FINISHED');
      } else {
        $scope.currentTask = data[0]; //Probar con tareas paralelas desde el bpm para habilitar la lista
      }
      $scope.showTaskInfo = true;

    }

    /**
     * Called when a transaction service has failed
     * @param data
     */
    function errorTransaction(data){

      var message = '';

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if(data.status == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {
        message = data.data.errorMessage;
      }

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

    /**
     * Method that returns a readable status of the request
     * @param data
     */
    $scope.showRequestStatus =  function (data) {

      if (data == 'PENDING') {

        return $filter('translate')('TASK_DETAIL_STATUS_PENDING');

      } else if (data == 'REJECTED') {

        return $filter('translate')('TASK_DETAIL_STATUS_REJECTED');

      } else if (data == 'APPROVED') {

        return $filter('translate')('TASK_DETAIL_STATUS_APPROVED');

      } else if (data == 'WAITING_FOR_APPROVAL') {

        return $filter('translate')('TASK_DETAIL_STATUS_APPROVED');

      } else if (data == 'FINISHED') {

        return $filter('translate')('TASK_DETAIL_STATUS_FINISHED');

      }

    };

    $scope.showShortRequestId = function (data) {

      var requestId = '';

      if(data) {

        requestId = data.split('_')[0];
      }

      return requestId;
    };

    function getDocSuccess(data){

      var file = new Blob([data], {type: 'application/pdf'});
      var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;
      var fileURL = urlCreator.createObjectURL(file);
      $scope.currentDocumentPath = fileURL;

      ngDialog.open({

        template: '../../views/documents/pdf-viewer/pdf-viewer.html',
        controller: 'PdfViewerController',
        scope: $scope
      });

    };

    function getDocError(data){
      console.log(data);
    };

  }]);
