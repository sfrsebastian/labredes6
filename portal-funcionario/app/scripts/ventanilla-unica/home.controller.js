/**
 * Created by mauricio on 26/01/15.
 */
'use strict';

ventanillaUnicaModule.controller('VentanillaUnicaHomeController', ['$scope', '$state', 'StateService', 'RequestsService',
  function($scope, $state, StateService, RequestsService) {

    RequestsService.getRequest({},getRequestSuccess, getRequestError);

    RequestsService.getRequestAlerts({},getRequestAlertsSuccess, getRequestAlertsError);

    RequestsService.getReviewedRequest({},getReviewedRequestSuccess, getReviewedRequestError);


    $scope.viewDetail = function(request){

      $state.go('request-detail',{'requestId':request.id});

    };

    function getRequestSuccess(data){

      $scope.requestList = data;

    }

    function getRequestError(data){

      //TODO hadle error!
    }

    function getRequestAlertsSuccess(data){

      $scope.alertList = data;
    }

    function getRequestAlertsError(data){

      //TODO hadle error!
    }

    function getReviewedRequestSuccess(data){

      $scope.reviewedList = data;
    }

    function getReviewedRequestError(data){

      //TODO hadle error!
    }

    function getReviewedRequestError(data){

      //TODO hadle error!
    }


    $scope.showShortSubject = function (data) {

      var shortSubject = '';

      if(data) {

        shortSubject = data.substring(0,30);

      }

      return shortSubject;

    };

    $scope.showShortRequestId = function (data) {

      var requestId = '';

      if(data) {

        requestId = data.split('_')[0];

      }

      return requestId;

    };

  }]);
