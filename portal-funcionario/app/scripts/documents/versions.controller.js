'use strict';

ventanillaUnicaModule.controller('VersionsController', ['$scope', 'VersionsService', 'VersionsUrlService', 'ngDialog', 
    function($scope, VersionsService, VersionsUrlService, ngDialog) {

    $scope.getDocumentVersionFileByVersionNumber = function(versionDocumentId) {

        VersionsUrlService.getVersionFileUrl({id: versionDocumentId}, getDocSuccess, getDocError);
    }

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
