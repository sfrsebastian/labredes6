'use strict';

ventanillaUnicaModule.controller('AddDocumentsController', ['$scope', '$state', '$filter', 'FileService', 'MessageService', '$timeout',
  function($scope, $state, $filter, FileService, MessageService, $timeout ) {

    $scope.spinners = {};

    var noFileSelectedMessage = $scope.$parent.noFileSelectedMessage;
    $scope.noFileSelectedMessage = noFileSelectedMessage;

    var mimeTypesAllowed = ['image/png', 'application/pdf', 'image/jpeg', 'image/tiff',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/vnd.oasis.opendocument.text'];

    $scope.addDocumentsIconClass = 'glyphicon glyphicon-chevron-right';

    //Attains the position of a document type within the given array. Returns -1 if it doesn't find the document type
    function fileSectionPosition(array, documentType) {
      var position = -1;

      for (var i = 0; i < array.length && position < 0; i++) {

        if (array[i].documentType === documentType) {
          position = i;
        }
      }
      return position;
    }

    function contains(array, object) {
      for (var i = 0; i < array.length; i++) {
        if (array[i] === object) {
          return true;
        }
      }
      return false;
    }

    //The list of evidences used for the dynamic documents
    $scope.evidences = $scope.$parent.evidences;

    //The JSON that contains the info of all the documents
    var documentsList = $scope.$parent.documentsList;

    //Uploads a file to the server and then places the temp id along with its info in the documentsList
    $scope.uploadFile = function(files, documentType, index) {

      MessageService.setErrorMessage('');
      $scope.$apply();

      //Validates the size of the file
      var fileSize = files[0].size;
      if(fileSize >= 2097152) {

        $.notify({
          // options
          message: $filter('translate')('ADD_DOCUMENTS_FILE_SIZE_VALIDATION')
        },{
          // settings
          type: 'danger',
          z_index : 9999,
          delay : 100000
        });

        $scope.$apply();//The apply is executed so that the watch in MessageController detects the change

      } else if(!contains(mimeTypesAllowed, files[0].type)) { //Validates that the file is a valid type

        $.notify({
          // options
          message: $filter('translate')('ADD_DOCUMENTS_FILE_TYPE_VALIDATION')
        },{
          // settings
          type: 'danger',
          z_index : 9999,
          delay : 100000
        });

        $scope.$apply();//The apply is executed so that the watch in MessageController detects the change

      } else {

        var formData = new FormData();
        formData.append('file', files[0]);

        var fileExtension = files[0].name.split('.').pop();

        //Calls the file service so that it uploads the temporal file
        $scope.spinners[documentType] = true;
        FileService.uploadTemporalPdfFile(formData, fileExtension, uploadTemporalFileSuccess, uploadTemporalFileError);

      }

      //The function is called when FileService.uploadTemporalPdfFile is done
      function uploadTemporalFileSuccess(data) {

        var position = fileSectionPosition(documentsList, documentType);
        $scope.spinners[documentType] = false;

        var fileName = files[0].name;
        var fileNameSplit = fileName.split(".");
        var fileExtension = fileNameSplit[fileNameSplit.length - 1];

        var documentInfoJson = {
          documentType : documentType,
          temporalId : data.temporalFileId,
          documentName : fileName,
          fileType : 'pdf'
        };

        if(position > -1) {

          documentsList[position] = documentInfoJson;

        } else {

          documentsList.push(documentInfoJson);
        }

        //Changes the name to be shown in the html
        var fileSizeKilobytes = Math.round( fileSize/1024 );
        var fileName = files[0].name + '   ' + fileSizeKilobytes + 'KB';

        $scope.evidences[index] = {
          name: fileName
        };

        updateParentData();

        $.notify({
          // options
          message: $filter('translate')('ADD_DOCUMENTS_UPLOAD_FILE_SUCCESS')
        },{
          // settings
          type: 'success',
          z_index : 9999,
          delay : 10000
        });
      }

      //The function is called when FileService.uploadTemporalPdfFile has errors
      function uploadTemporalFileError(data, statusCode) {

        $scope.spinners[documentType] = false;
        handleError(data, statusCode);
      }

    };

    //Removes a file from the list of evidences and from the documentsList
    $scope.removeFile = function(index) {

      $scope.evidences.splice(index, 1);

      var position = fileSectionPosition(documentsList, 'evidence' + index);
      if(position > -1) {
        documentsList.splice(position, 1);
      }

      //Corrects the document type of the evidence documents to avoid problems
      for (var i = index; i < $scope.evidences.length + 1; i++) {
        //Validates that the evidence in the specified index exists
        if(documentsList[i]) {
          var evidenceDocumentPosition = documentsList[i].documentType.replace('evidence','');
          var evidenceDocumentPositionNumber = Number(evidenceDocumentPosition);

          documentsList[i].documentType = 'evidence' + (evidenceDocumentPositionNumber - 1);
        }
      }

      updateParentData();
    };

    //Clears a file from documentsList and from evidences(if necessary).
    $scope.clearFile = function(documentType, index) {

      var position = fileSectionPosition(documentsList, documentType);
      if(position > -1) {

        //Leaves the values of the file in documentsList in blank
        documentsList[position].temporalId = '';
        documentsList[position].documentName = '';
        documentsList[position].fileType = '';

        //Changes the name of label of the file cleared
        $scope.evidences[index] = {
          name: noFileSelectedMessage
        };

        updateParentData();

      }
    };

    //Downloads the specified file
    $scope.downloadFile = function(fullDocumentName, documentType) {

      var position = fileSectionPosition(documentsList, documentType);

      FileService.downloadTempFile(documentsList[position].temporalId, successDownload, errorDownload);

      function successDownload(data, httpCode, headersGetterFunction) {

        var documentName = fullDocumentName.split('.')[0] + '.pdf';

        $timeout(function() {

          var contentType = 'application/pdf';

          if (navigator.msSaveBlob) {

            var blob = new Blob([data], { type: contentType });
            navigator.msSaveBlob(blob, documentName);
          } else {
            var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;

            if (urlCreator) {

              // Try to use a download link
              var link = document.createElement("a");

              if ("download" in link) {
                // Prepare a blob URL
                var blob = new Blob([data], { type: contentType });
                var url = urlCreator.createObjectURL(blob);

                link.setAttribute("href", url);
                link.setAttribute("download", documentName);

                // Simulate clicking the download link
                var event = document.createEvent('MouseEvents');
                event.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
                link.dispatchEvent(event);
              } else {

                // Prepare a blob URL
                // Use application/octet-stream when using window.location to force download
                var blob = new Blob([data], { type: octetStreamMime });
                var url = urlCreator.createObjectURL(blob);
                $window.location = url;
              }
            }
          }
        });
      }

      //The function thats called when FileService.downloadTempFile has errors
      function errorDownload(error, statusCode) {
        handleError(error, statusCode);
      }
    };

    function updateParentData() {
      $scope.$parent.evidences = $scope.evidences;
      $scope.$parent.documentsList = documentsList;
    }

    function handleError(data, statusCode) {

      var message;

      if (statusCode == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else {

        if(data.type == "business") {

          message = data.errorMessage;

        } else {

          message = "Ha ocurrido un error"
        }
      }
      
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

    $scope.changeIcon = function() {

      if ($scope.addDocumentsIconClass == 'glyphicon glyphicon-chevron-down'){

        $scope.addDocumentsIconClass = 'glyphicon glyphicon-chevron-right';

      } else if ($scope.addDocumentsIconClass = 'glyphicon glyphicon-chevron-right'){

        $scope.addDocumentsIconClass = 'glyphicon glyphicon-chevron-down';

      }

    }

  }]);
