'use strict';

ventanillaUnicaModule.factory('VersionsUrlService', ['environment', '$http', function(environment, $http) {

  return  {

    getVersionFileUrl: function(doc, successFunction, errorFunction) {

      $http({
        method: 'GET',
        url: environment.documentManager + '/v2/signed-documents/' + doc.id + '/versions/' + doc.id + '/file',
        responseType:'arraybuffer'
      })
        .success(successFunction)
        .error(errorFunction);
    }
  };

}]);
