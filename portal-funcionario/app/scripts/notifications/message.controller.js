/**
 * Created by LeanFactory on 26/02/15.
 */

'use strict';

notificationsModule.controller('MessageController', ['$scope', 'MessageService', '$timeout', function($scope, MessageService,$timeout) {

  $scope.showflag = true;
  $scope.fadeOut=false;

  function getMessage() {
    return MessageService.getMessage();
  }

  function updateMessage(message) {
    $scope.Message = message;
    if ($scope.Message){
      $scope.fadeOut=true;
      $timeout(function(){
        $scope.showflag = false;
      },3000)
    }
  }

  function getErrorMessage() {
    return MessageService.getErrorMessage();
  }

  function updateErrorMessage(message) {
    $scope.errorMessage = message;
    if ($scope.errorMessage){
      $scope.fadeOut=true;
      $timeout(function(){
        $scope.showflag = false;
      },3000)
    }
  }

  $scope.$watch(getErrorMessage, updateErrorMessage);
  $scope.$watch(getMessage, updateMessage);

}]);
