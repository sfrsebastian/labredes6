'use strict';

ventanillaUnicaModule.factory('DocumentsService', ['environment', '$resource', function(environment, $resource) {

    var url = environment.documentManager + '/v2/signed-documents';
    var defaultParams = {};

    var actions = {
        getDocuments: { method: 'GET', isArray: true },
        postCitizenDocument : {method:'POST', isArray:true, url:url + '/citizen-documents'}
    };

    return $resource(url, defaultParams, actions);
}]);

ventanillaUnicaModule.factory('FileService', ['environment', '$http', function(environment, $http) {

    return {

        uploadTemporalFile: function(fileByteArray, successFunction, errorFunction) {

            $http({
                method: 'POST',
                url: environment.documentManager + '/v2/temporals',
                headers: {'Content-Type': 'application/octet-stream'},
                data: new Uint8Array(fileByteArray),
                transformRequest: []
            })
            .success(successFunction)
            .error(errorFunction);
        },
        uploadTemporalPdfFile: function(fileByteArray, fileType, successFunction, errorFunction) {

          $http.post(environment.documentManager + '/v2/temporals/pdf'+'?fileType='+fileType, fileByteArray, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
          })
            .success(successFunction)
            .error(errorFunction);
        },
        createTemporalFileWithHash: function(temporalId, successFunction, errorFunction) {

            $http({
                method: 'POST',
                url: environment.documentManager + '/v2/temporals/' + temporalId + '/pfdHash',
                headers: {'Content-Type': 'application/json'},
                transformRequest: []
            })
            .success(successFunction)
            .error(errorFunction);
        },
        createODTWithQRFooter: function(temporalId, successFunction, errorFunction) {

            $http({
                method: 'POST',
                url: environment.documentManager + '/v2/temporals/' + temporalId + '/qr-footer',
                headers: {'Content-Type': 'application/json'},
                transformRequest: []
            })
            .success(successFunction)
            .error(errorFunction);
        },
        downloadTempFile: function(temporalId, successFunction, errorFunction) {

            $http({
                method: 'GET',
                url: environment.documentManager + '/v2/temporals/' + temporalId,
                responseType:'arraybuffer'
            })
            .success(successFunction)
            .error(errorFunction);
        },
        getProcessDocument : function(templateName, caseId, docName, successFunction, errorFunction){

          $http({
            method: 'GET',
            url: environment.documentManager + '/v2/process/'+ caseId+'/'+templateName+'/'+docName+'/file',
            responseType:'arraybuffer'
          })
            .success(successFunction)
            .error(errorFunction);
        },

        getDocumentSigners: function(documentId, successFunction, errorFunction){

          $http({

            method: 'GET',
            url: environment.documentManager + '/v2/signed-documents/'+ documentId +'/signers'
          })
          .success(successFunction)
          .error(errorFunction);
      }

    };
}]);
