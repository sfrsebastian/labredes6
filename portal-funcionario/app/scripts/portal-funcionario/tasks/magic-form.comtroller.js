/**
 * Created by LeanFactory on 16/03/15.
 */

'use strict';

ventanillaUnicaModule.controller('MagicFormController', ['$scope',
  function($scope) {

    $scope.getInputType = function (data) {

      var inputType = '';

      if(data == 'long') {

        inputType = "number";


      } else {

        inputType = data;
      }

      return inputType;

    }


    $scope.validateContent = function (type) {


      var inputRegex = '';

      if(type == 'long') {



        inputRegex = '/^[0-9]*$/';

      }

      console.log(inputRegex);
      return inputRegex;

    }

    $scope.today = function() {

      $scope.dt = new Date();

    };

    $scope.today();

    $scope.clear = function () {

      $scope.dt = null;

    };


    $scope.openCalendar = function($event) {

      console.log('open');
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;

    };



  }]);
