'use strict';

// Draws a table of requests
ventanillaUnicaModule.directive('requestsTable', ['BusinessService', '$filter','ngTableParams', '$state',
  function(BusinessService, $filter, ngTableParams, $state) {

    function addFilters(queryParams, filterParams) {
      for(var paramKey in filterParams) {
        var paramValue = filterParams[paramKey];

        if(paramValue){
          queryParams[paramKey] = paramValue;
        }
      }
    }

    function addOrdering(queryParams, orderParams) {
      for(var paramKey in orderParams) {
        var paramValue = orderParams[paramKey];

        if(paramValue){
          queryParams.orderBy = paramKey;
          queryParams.orderDirection = paramValue === 'asc' ? 'ascending' : 'descending';
        }
      }
    }

    return {
      restrict: 'E',
      scope: {
        isAlerts: '=',
        status: '='
      },
      templateUrl: function(elem, attr) {
        return '../../views/ventanilla-unica/requests/' + attr.htmlName + '.html';
      },
      
      link: function (scope, element, attrs) {

        scope.tableParams = new ngTableParams({
            page: 1,            // show first page
            count: 10
          },
          {
            //total:data.length, // length of data
            getData: function($defer, params) {

              var queryParams = {
                limit: params.count(),
                skip: (params.page() - 1) * params.count(),
                requestStatus: attrs.status
              };

              addFilters(queryParams, params.filter());
              addOrdering(queryParams, params.sorting());

              scope.showSpinner = true;

              var promise  = BusinessService.getRequests(queryParams);

              promise.$promise.then(function(result) {

                $defer.resolve(result.requestsList);
                params.total(result.size);
                scope.showSpinner = false;

              }).catch(function(error) {

                scope.showSpinner = false;

              });

            }
          });

        scope.viewDetail = function(request) {
          $state.go('request-detail', {'requestId':request.requestId});
        };

        scope.sendSelectedRequest = function(requestId) {

          StateService.set(requestId);

        };

        scope.showRequestStatus =  function (data) {


          if (data == 'PENDING') {

            return $filter('translate')('REQUEST_DETAIL_STATUS_PENDING');

          } else if (data == 'REJECTED') {

            return $filter('translate')('REQUEST_DETAIL_STATUS_REJECTED');

          } else if (data == 'APPROVED') {

            return $filter('translate')('REQUEST_DETAIL_STATUS_APPROVED');

          } else if (data == 'WAITING_FOR_APPROVAL') {

            return $filter('translate')('REQUEST_DETAIL_STATUS_APPROVED');

          } else if (data == 'FINISHED') {

            return $filter('translate')('TASK_DETAIL_STATUS_FINISHED');

          }

        };

        scope.showShortRequestId = function (data){

          var requestId = '';

          if(data) {

            requestId = data.split('_')[0];

          }

          return requestId;

        }

      }
    };

  }]);
