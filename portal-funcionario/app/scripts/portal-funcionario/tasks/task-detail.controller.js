'use strict';

portalFuncionarioModule.controller('TaskDetailController', ['$scope', 'VersionsService', 'VersionsUrlService', 'ngDialog', '$stateParams', 'TaskService',
  'RequestsService', 'DocumentsService', '$filter',
  function($scope, VersionsService, VersionsUrlService, ngDialog, $stateParams, TaskService, RequestsService, DocumentsService, $filter) {

    var ARROW_DOWN_ICON = 'glyphicon glyphicon-chevron-down';
    var ARROW_RIGHT_ICON = 'glyphicon glyphicon-chevron-right';

    var isHistoric = $stateParams.isHistoric;
    $scope.isHistoric = isHistoric;
    var caseId = '';

    $scope.disableButtons = false;
    $scope.showTaskInfo = false;
    $scope.showDocumentList = false;
    $scope.showRequestView = false;
    $scope.showTaskInfoSpiner = true;
    $scope.showNotFoundTaskInfo = false;
    $scope.showProcessForm = false;

    $scope.showDocumentList = false;

    $scope.documentIconClass = ARROW_RIGHT_ICON;
    $scope.taskDetailInfoIconClass = ARROW_RIGHT_ICON;

    //Initialization for adding documents
    var noFileSelectedMessage = $filter('translate')('ADD_DOCUMENTS_NO_FILE_SELECTED');
    $scope.noFileSelectedMessage = noFileSelectedMessage;

    //The list of evidences used for the dynamic form
    $scope.evidences = [];
    $scope.evidences.push({name: noFileSelectedMessage});
    $scope.evidences.push({name: noFileSelectedMessage});
    $scope.evidences.push({name: noFileSelectedMessage});

    $scope.documentsList = [];

    TaskService.getTask({taskId: $stateParams.taskId, isHistoric: $stateParams.isHistoric }, getTaskSuccess, errorTransaction);

    function getTaskSuccess(task) {

      caseId = task.processInstanceId;

      $scope.showAddDocuments = task.certiParams.CERTI_ALLOW_UPLOAD_DOCUMENTS;
      $scope.signDocuments = task.certiParams.CERTI_SIGN_UPLOADED_DOCUMENTS;

      if(caseId != null) {

        RequestsService.getRequestByCaseId({caseId: caseId}, getRequestSuccess, errorTransaction);

      }

      function getRequestSuccess(request) {

        $scope.currentRequest = request;

        $scope.currentTask = task;

        $scope.formProperties = task.formPropertiesParams;

        if ($scope.currentTask.category != null  ) {

          $scope.urlTask = 'views/portalFunctionary/tasks/typified-tasks/' + $scope.currentTask.category + '.html';

        } else if ( $scope.formProperties.length != 0) {

          $scope.urlTask = 'views/portalFunctionary/tasks/typified-tasks/defaultTask.html';

        }
        $scope.showProcessForm = true;

      }

    }

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
      }
    };

    $scope.openLatestVersionPopUp = function(doc) {

      $scope.currentDocumentId = doc.id;
      VersionsUrlService.getVersionFileUrl(doc, getDocSuccess, getDocError);


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



    $scope.$on('ngDialog.opened', function (event, $dialog) {
      $dialog.find('.ngdialog-content').css('width', '80%');
    });

    $scope.showProcess = function() {
      ngDialog.open({
        template: '../../views/portalFunctionary/process-diagram.html',
        controller: 'CaseDiagramController',
        scope: $scope
      });
    };

    $scope.showShortRequestId = function (data) {

      var requestId = '';

      if(data) {

        requestId = data.split('_')[0];

      }

      return requestId;

    }

    $scope.getDocumentList = function (){

      if ($scope.documentIconClass == ARROW_DOWN_ICON) {

        $scope.documentIconClass = ARROW_RIGHT_ICON;

      } else if ($scope.documentIconClass == ARROW_RIGHT_ICON) {

        $scope.documentIconClass = ARROW_DOWN_ICON;

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
     * Called when a transaction service has failed
     * @param data
     */
    function errorTransaction(data) {

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


    $scope.getTaskInfo = function (){

      if ($scope.taskDetailInfoIconClass == ARROW_DOWN_ICON){

        $scope.taskDetailInfoIconClass = ARROW_RIGHT_ICON;

      } else if ($scope.taskDetailInfoIconClass == ARROW_RIGHT_ICON){

        $scope.taskDetailInfoIconClass = ARROW_DOWN_ICON;

      }

      if(caseId != '' && caseId != null && $scope.showTaskInfo == false) {

        $scope.showTaskInfo = true;
        $scope.showTaskInfoSpiner = false;

      } else if ($scope.showTaskInfo == false && $scope.showNotFoundTaskInfo == false) {

        $scope.showNotFoundTaskInfo = true;
        $scope.showTaskInfoSpiner = false;

      }
    };

}]);
