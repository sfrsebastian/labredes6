/**
 * Created by LeanFactory on 18/04/15.
 */

'use strict';

portalFuncionarioModule.controller('RequestTraceReport', ['$scope', 'MessageService', 'ReportsService', '$timeout',
  function($scope, MessageService, ReportsService, $timeout) {

      $scope.findRequest = function() {

        $scope.request = {};
        $scope.showRequest = false;

        var requestId = $scope.searchRequestId;

        ReportsService.getRequestReport(requestId, getRequestReportSuccess, errorTransaction);

        function getRequestReportSuccess(data) {

          $scope.request = data[0];
          $scope.showRequest = true;

        }

      };

    //Downloads the pdf file of the specified request
    $scope.downloadPDFFile = function() {

      ReportsService.getRequestReportPdf($scope.request.requestId, successDownload, errorTransaction);

      function successDownload(data, httpCode, headersGetterFunction) {

        $timeout(function() {

          var contentType = 'application/pdf';
          var documentName = 'reporteRadicacion.pdf';

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
    };

    //Downloads the xlsx file of the specified request
    $scope.downloadXlsxFile = function() {

      ReportsService.getRequestReportXlsx($scope.request.requestId, successDownload, errorTransaction);

      function successDownload(data, httpCode, headersGetterFunction) {

          $timeout(function() {

            var contentType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            var documentName = 'reporteRadicacion.xlsx';

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
    };

        /**
     * Called when a transaction service has failed
     * @param data
     */
    function errorTransaction(data, statusCode) {

      var message = '';

      if (statusCode == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else if(statusCode == 401) {

        message = 'Error de autorización, por favor verifique sus permisos o contacte a soporte.';

      } else {

        message = data.errorMessage;
      }

      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 10000,
        placement:{
          align:'center'
        }
      });
    }

}]);
