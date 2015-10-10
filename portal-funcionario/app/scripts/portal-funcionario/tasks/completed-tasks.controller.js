'use strict';

/**
 * Created by steven on 16/03/15.
 */
portalFuncionarioModule.controller('CompletedTasksController', ['$scope','$filter', 'ngTableParams', 'BusinessEventService', '$state','SessionService',
  function($scope,$filter, ngTableParams, BusinessEventService, $state, SessionService) {

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

    $scope.viewDetail = function(task) {

      $state.go('task-detail',{'taskId':task.taskId, 'isHistoric':true});

    };

    //-------------------------------------

    var queryNames = ['procedureName', 'caseId', 'assignedPerson', 'userName', 'userDocumentType', 'userDocumentNumber'];

    var data = [];

    $scope.tableParams = new ngTableParams({
      page: 1,            // show first page
      count: 10,           // count per page
      sorting: {
        id: 'asc'     // initial sorting
      }
    }, {
      total:data.length, // length of data
      getData: function ($defer, params) {

        var queryParams = {
          limit: params.count(),
          skip: (params.page() - 1) * params.count(),
          requestStatus: ['APPROVED', 'FINISHED'],
          userNameLastActor: SessionService.getAuthorizationUserName()
        };

        buildFilterQueryParams(queryParams);
        addOrdering(queryParams, params.sorting());

        var queryPromise = BusinessEventService.getRequests(queryParams);

        queryPromise.$promise.then(function(requestEventsFound) {

          params.total(requestEventsFound.size);
          $defer.resolve(requestEventsFound.requestEventsList);
          $scope.showSpinner = false;
          
        });
      },
      $scope: $scope
    });

    $scope.findTasks = function() {

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

      var completedDateFrom = $scope.completedDateFrom;

      if(completedDateFrom) {

        queryParams.dateCreatedLastEventFrom = completedDateFrom;

      } else {

        $scope.queryJson.dateCreatedLastEventFrom = null;

      }

      var completedDateTo = $scope.completedDateTo;

      if(completedDateTo) {

        $scope.queryJson.dateCreatedLastEventTo = completedDateTo;

      } else {

        $scope.queryJson.dateCreatedLastEventTo = null;

      }

    }

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

