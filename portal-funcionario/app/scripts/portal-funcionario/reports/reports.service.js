/**
 * Created by LeanFactory on 20/04/15.
 */

'use strict';

portalFuncionarioModule.factory('ReportsService', ['environment', '$http', function(environment, $http) {

  var url = environment.businessReports + '/request-report';

  return {

        getRequestReport: function(requestId, successFunction, errorFunction) {

            $http({
                method: 'GET',
                url: url + '?requestId=' + requestId,
                headers: {'Content-Type': 'application/json'}
            })
            .success(successFunction)
            .error(errorFunction);
        },
        getRequestReportPdf: function(requestId, successFunction, errorFunction) {

            $http({
                method: 'GET',
                url: url + '/' + requestId + '/pdf',
                responseType:'arraybuffer'
            })
            .success(successFunction)
            .error(errorFunction);
        },
        getRequestReportXlsx: function(requestId, successFunction, errorFunction) {

            $http({
                method: 'GET',
                url: url + '/' + requestId + '/xlsx',
                responseType:'arraybuffer'
            })
            .success(successFunction)
            .error(errorFunction);
        }
    };
}]);
