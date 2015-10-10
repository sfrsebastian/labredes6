ventanillaUnicaModule.factory('SaveFileService', ['environment', '$http', function(environment, $http) {

  return {

    saveFile: function(fileByteArray, caseId, docName, successFunction, errorFunction) {

      $http({
        method: 'POST',
        url: environment.documentManager + '/v2/process/' + caseId + '/' + docName + '/file',
        headers: {'Content-Type': 'application/octet-stream'},
        data: new Uint8Array(fileByteArray),
        transformRequest: []
      })
        .success(successFunction)
        .error(errorFunction);
    }
  };
}]);
