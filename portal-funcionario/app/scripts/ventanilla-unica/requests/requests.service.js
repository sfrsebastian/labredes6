'use strict';

ventanillaUnicaModule.factory('RequestsService', ['environment', '$resource', function(environment, $resource) {

    var url = environment.singleWindow + '/requests';
    var defaultParams = {};

    var actions = {

        getRequest: { method: 'GET', isArray: true },

        getRequestAlerts: { method: 'GET', isArray: true, url: url + '/alerts'},

        getReviewedRequest: { method: 'GET', isArray: true, url: url + '/reviewed' },

        getRequestById: { method: 'GET', isArray: false, url: url + '/:requestId' },

        rejectRequest: { method: 'PUT', url: url + '/:requestId' },

        registerRequest: { method: 'POST' },

        approveRequest: { method: 'POST', url: url + '/:requestId' },

        getRequestByCaseId: { method: 'GET', url: url + '/caseid-requests/:caseId' }
    };

    return $resource(url, defaultParams, actions);
}]);
