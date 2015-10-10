'use strict';

documentsModule.controller('EditorController', ['$scope', 'environment', 'SessionService', 'StateService','FileService',
    function($scope, environment, SessionService, StateService, FileService) {

    $scope.username = SessionService.getAuthorizationUserName();
    $scope.emptyDocumentUrl = '../../../files/welcome.odt';

    var editor = null;

    function closeEditorCallback() {

        editor.getDocumentAsByteArray(function (err, odtFileAsByteArray) {

            if (err) {

                alert(err);
                return;
            }

            console.debug(odtFileAsByteArray);
            StateService.setEditor(null);
            $scope.$emit('close-document', odtFileAsByteArray);
        });
    }

    function saveDocument() {

        editor.getDocumentAsByteArray(function (err, odtFileAsByteArray) {

            if (err) {

                alert(err);
                return;
            }

            console.debug(odtFileAsByteArray);
            $scope.$emit('save-document', odtFileAsByteArray);
        });
    }

    function closeEditor(eventContext, closeEditor) {

        if (editor && closeEditor) {

            editor.closeDocument(function() {});
            editor.destroy(closeEditorCallback);
        }
    };

    $scope.$on('close-editor', closeEditor);

    var editorOptions = {

        userData: {
            fullName: $scope.username,
            color: 'blue'
        },

        allFeaturesEnabled: true,
        saveCallback: saveDocument
    };

    function onEditorCreated(err, e) {

        editor = e;

        if (err) {

            // something failed unexpectedly, deal with it (here just a simple alert)
            console.error('err: ' + err);
            closeEditor({}, true);
            alert(err);
            return;
        }

        StateService.setEditor(editor);

        var docUrl = $scope.emptyDocumentUrl;
        var docName = $scope.docName;
        var docTemplate = $scope.templateName;

        $scope.$parent.docName = docName;


        if (docName != undefined) {
            docUrl = environment.documentManager + "/v2/process/" + $scope.$parent.caseId + "/" + docName + "/file"
          FileService.getProcessDocument(docTemplate, $scope.$parent.caseId, docName, getDocSuccess, getDocError);
        }else{
          editor.openDocumentFromUrl(docUrl, function(err) {

              if (err) {
                  // something failed unexpectedly, deal with it (here just a simple alert)
                  closeEditor({}, true);
                  alert('There was an error on opening the document: ' + err);
              }
          });
        }


    }

      function getDocSuccess(data){
        var file = new Blob([data], {type: 'application/pdf'});
        console.log(file);
        var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;
        var fileURL = urlCreator.createObjectURL(file);
        $scope.currentDocumentPath = fileURL;
        editor.openDocumentFromUrl(fileURL, function(err) {

            if (err) {
                // something failed unexpectedly, deal with it (here just a simple alert)
                closeEditor({}, true);
                alert('There was an error on opening the document: ' + err);
            }
        });

      };

      function getDocError(data){

        console.log('error: ' + data);
      };

    $scope.$on("$destroy", function() {

        editor = StateService.getEditor();

        if (editor) {
            closeEditor({}, true);
        }
    });

    Wodo.createTextEditor('editorContainer', editorOptions, onEditorCreated);



}]);
