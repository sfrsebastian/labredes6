/**
 * Created by LeanFactory on 18/02/15.
 */

'use strict';

// Draws a table of tasks
portalFuncionarioModule.directive('tasksTable', ['TaskService', '$filter','ngTableParams', '$state', 'SessionService', 
  function(TaskService, $filter, ngTableParams, $state, SessionService) {

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
      templateUrl: function(elem, attr){
        return '../../views/portalFunctionary/' + attr.htmlName + '.html';
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
                userName: SessionService.getAuthorizationUserName()
              };

              addFilters(queryParams, params.filter());
              addOrdering(queryParams, params.sorting());

              scope.showSpinner = true;

              var promise;
              if(scope.isAlerts === true) {

                promise  = TaskService.getOpenTasksAlerts(queryParams,getRequestsSuccess(), getRequestsError());

              } else {

                promise  = TaskService.getOpenTasks(queryParams,getRequestsSuccess(), getRequestsError());

              }

              promise.$promise.then(function(result) {

                $defer.resolve(result.tasksList);
                params.total(result.count);
                scope.showSpinner = false;

              }).catch(function(error) {

                scope.showSpinner = false;

              });

              function getRequestsSuccess() {

              }

              function getRequestsError() {

              }

            }
          });

        scope.viewDetail = function(task){
          $state.go('task-detail',{'taskId':task.id, 'isHistoric': false});
        };

      }
    };

  }]);
