/**
 * Created by christianrodriguez on 5/02/15.
 */

'use strict';

ventanillaUnicaModule.controller('PdfViewerController', ['$scope', 'FileService', function($scope, FileService) {

  $scope.pdfName = 'Example';
  $scope.pdfUrl = $scope.currentDocumentPath;
  $scope.isLoadingSigners = false;

  $scope.getSigners = function(){

    $scope.isLoadingSigners = true;
    FileService.getDocumentSigners(this.$parent.currentDocumentId, getSignersSuccess, getSignersError);
  };

  function getSignersSuccess(data){

    $scope.isLoadingSigners = false;
    $scope.signatures = data;
    console.log(data);
  };

  function getSignersError(data){

    $scope.isLoadingSigners = false;
    console.log(data);
  };
}]);
