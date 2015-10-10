/**
 * Created by LeanFactory on 11/02/15.
 */
'use strict';

ventanillaUnicaModule.factory('VersionsService', ['environment', '$resource', function(environment, $resource) {

    var url = environment.documentManager + '/v2/signed-documents/:documentId/versions';
    var defaultParams = {};

    var actions = {

        getDocumentVersions: { method: 'GET', isArray: true },
        getDocumentVersionByVersionNumber: { method: 'GET', url: url + '/:versionNumber' },
        getDocumentVersionFileHashByVersionNumber: { method: 'GET', url: url + '/:versionNumber/hash' },
        putSignedHashIntoVersionFile: { method: 'PUT', url: url + '/:versionNumber/file' },
    };

    return $resource(url, defaultParams, actions);
}]);
