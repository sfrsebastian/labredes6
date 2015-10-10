'use strict';

securityModule.controller('RequestRecoverPasswordController', ['$scope', 'LoginService', 'SessionService', 'ErrorTranslateService', '$modalInstance',
    '$filter', 
  function($scope, LoginService, SessionService, ErrorTranslateService, $modalInstance, $filter) {

    $scope.closeModal = function () {

      $modalInstance.dismiss('cancel');

    };

    $scope.requestRecoverPassword = function() {

      LoginService.requestRecoverPassword({
        email: $scope.email,
        organizationId: SessionService.getUserOrganization(),
        isCitizen: false
      },
      requestRecoverPasswordSuccess,
      requestRecoverPasswordError);

      $modalInstance.close();

      function requestRecoverPasswordSuccess() {

        var message = $filter('translate')('REQUEST_RECOVER_PASSWORD_SUCCESS_MESSAGE');

        $.notify({
          // options
          message: message
        }, {
          // settings
          type: 'success',
          z_index : 9999,
          delay : 10000,
          placement:{
            align:'center'
          }
        });

      }

      function requestRecoverPasswordError(data) {

        var message = '';
        var messageType = 'success';
        //If a business exception is returned, a success message is show for security reasons
        //(To not allow an attacker to know if the email exists or not)
        if(data.status == 418) {

          message = $filter('translate')('REQUEST_RECOVER_PASSWORD_SUCCESS_MESSAGE');

        } else {

          message = ErrorTranslateService.translateErrorMessage(data);
          messageType = 'danger';

        }

        $.notify({
          // options
          message: message

        }, {
          // settings
          type: messageType,
          z_index : 9999,
          delay: 60000,
          placement:{
            align:'center'
          }

        });

      }

    };

}]);
