'use strict';

/**
 * Created by steven on 06/02/15.
 */
ventanillaUnicaModule.controller('ReviewedRequestsController', ['$scope','$filter', 'ngTableParams', 'BusinessService', '$state',
  function($scope,$filter, ngTableParams, BusinessService, $state) {

    $scope.showSpinner = false;
    $scope.showTable = false;

    $scope.open = function($event, elementName) {

      $event.preventDefault();
      $event.stopPropagation();

      if(elementName === 'from') {

        $scope.fromOpened = true;
        $scope.toOpened = false;

      } else {

        $scope.fromOpened = false;
        $scope.toOpened = true;

      }

    };

    $scope.queryJson = {

    };

    $scope.typesOfDocuments = ['', 'Cédula', 'NIT'];

    $scope.viewDetail = function(request) {

      $state.go('request-detail',{'requestId':request.requestId});

    };

    var queryNames = ['requestId', 'procedureName', 'caseId', 'assignedPerson', 'userName', 'userNameLastActor', 'userDocumentType', 'userDocumentNumber'];

    var data = [];
    $scope.tableParams = new ngTableParams({

        page: 1,            // show first page
        count: 10,           // count per page
        sorting: {

          id: 'asc'     // initial sorting

        }
      },
      {
        total:data.length, // length of data
        getData: function ($defer, params) {

          var queryParams = {
            limit: params.count(),
            skip: (params.page() - 1) * params.count(),
            requestStatus: ['APPROVED', 'REJECTED', 'FINISHED']
          };

          buildFilterQueryParams(queryParams);
          addOrdering(queryParams, params.sorting());

          var queryPromise = BusinessService.getRequests(queryParams);

          queryPromise.$promise.then(function(requestsFound) {

            params.total(requestsFound.size);
            $defer.resolve(requestsFound.requestsList);
            $scope.showSpinner = false;
            
          });

        },

        $scope: $scope

    });

    $scope.findRequests = function() {

      $scope.showSpinner = true;
      $scope.showTable = true;  

      $scope.tableParams.reload();

    };

    function buildFilterQueryParams(queryParams) {

      var filterKeys = Object.keys($scope.queryJson);

      for(var i = 0; i < filterKeys.length; i++) {

        var key = filterKeys[i];

        var value = $scope.queryJson[key];

        if(value && value !== '') {

          queryParams[key] = value;

        }

      }

      var assignedDateFrom = $scope.assignedDateFrom;

      if(assignedDateFrom) {

          queryParams.assignedDateFrom = assignedDateFrom;

      } else {

        queryParams.assignedDateFrom = null;

      }

      var assignedDateTo = $scope.assignedDateTo;

      if(assignedDateTo) {

        queryParams.assignedDateTo = assignedDateTo;

      } else {

        queryParams.assignedDateTo = null;

      }

    }

    /**
     * Method that returns a readable status of the request
     * @param data
     */
    $scope.showRequestStatus =  function (data) {

      if (data == 'PENDING') {

        return $filter('translate')('TASK_DETAIL_STATUS_PENDING');

      } else if (data == 'REJECTED') {

        return $filter('translate')('TASK_DETAIL_STATUS_REJECTED');

      } else if (data == 'APPROVED') {

        return $filter('translate')('TASK_DETAIL_STATUS_APPROVED');

      } else if (data == 'WAITING_FOR_APPROVAL') {

        return $filter('translate')('TASK_DETAIL_STATUS_APPROVED');

      } else if (data == 'FINISHED') {

        return $filter('translate')('TASK_DETAIL_STATUS_FINISHED');

      }

    };

    function addOrdering(queryParams, orderParams) {

      for(var paramKey in orderParams) {

        var paramValue = orderParams[paramKey];

        if(paramValue) {

          queryParams.orderBy = paramKey;
          queryParams.orderDirection = paramValue === 'asc' ? 'ascending' : 'descending';

        }
      }

    }

  }]);

