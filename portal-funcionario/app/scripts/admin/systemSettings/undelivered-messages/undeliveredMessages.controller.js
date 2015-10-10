/**
 * Created by LeanFactory on 23/07/15.
 */

'use strict';

adminModule.controller('UndeliveredMessagesController', ['$scope', '$state','UndeliveredMessagesService',
  function($scope, $state, UndeliveredMessagesService) {

    $scope.showSpinner = true;
    $scope.showMessageBoolean = false;

    UndeliveredMessagesService.getUndeliveredMessages({},getUndeliveredMessagesSucces, getUndeliveredMessagesError);

    function getUndeliveredMessagesSucces(data){
      $scope.showSpinner = false;
      $scope.undeliveredMessagesList = data;

      $scope.cheked = {};
    }

    $scope.showMessage = function(message){
      $scope.currentMessage = message;
      $scope.showMessageBoolean = true;
    };

    $scope.sendMessagesIds = function(){

      var Ids = [];
      for (var i = 0; i < $scope.cheked.messages.length; i++) {

        Ids.push($scope.cheked.messages[i]._id);
      }

      UndeliveredMessagesService.reinjectMessages({idList: Ids},{}, reinjectMessagesSucces, reinjectMessagesError);
    };

    function reinjectMessagesSucces(data){

      var message = "los mensajes se enviaron correctamente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'succes',
        z_index : 9999,
        delay : 20000
      });
    }

    function reinjectMessagesError(data){

      var message = "Error al enviar los mensajes";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 20000
      });
    }

    function getUndeliveredMessagesError(data){
      $scope.showSpinner = false;


      $scope.isSaving = false;
      var message = "Error al cargar las transacciones";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 20000
      });
    }

  }]);
