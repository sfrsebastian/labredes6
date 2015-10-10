'use strict';

securityModule.controller('RecoverPasswordController', ['$scope', '$modalInstance', 'LoginService', '$state', 'SessionService', 'ErrorTranslateService',
  function($scope, $modalInstance, LoginService, $state, SessionService, ErrorTranslateService) {

    $scope.disableButton = false;

    $scope.close = function () {

      $modalInstance.dismiss('cancel');

    };

    $scope.changePassword = function() {

      $scope.disableButton = true;

      var bitArray = sjcl.hash.sha256.hash($scope.password);
      var hashedPassword = sjcl.codec.hex.fromBits(bitArray);

      LoginService.recoverPassword(
        {
          passwordToken: $state.params.token,
          hashedNewPassword: hashedPassword,
          organizationId: SessionService.getUserOrganization()
        },
        passwordWasChanged,
        passwordChangeError);

      function passwordWasChanged() {

        var message = 'La contraseña fue modificada correctamente';

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

        $modalInstance.close();

      }

      function passwordChangeError(data) {

        var messageError = ErrorTranslateService.translateErrorMessage(data);

        $.notify({
          // options
          message: messageError

        }, {
          // settings
          type: 'danger',
          z_index : 9999,
          delay: 10000

        });

        $modalInstance.close();

      }

    };

}]);
