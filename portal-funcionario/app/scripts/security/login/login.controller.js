'use strict';

securityModule.controller('LoginController', ['$scope', '$state', 'LoginService', 'SessionService','LoginWithCertificateService', '$filter', '$modal',
  function($scope, $state, LoginService, SessionService, LoginWithCertificateService, $filter, $modal) {

    $scope.isLoading = false;
    $scope.loadingMessage = "";

    /**
     * Authenticates a user with credentials
     * @param username
     * @param password
     */
    $scope.authenticateFunctionary = function(username, password) {

      if (username && password) {
        $scope.isLoading = true;
        $scope.loadingMessage = $filter('translate')('LOGIN_AUTHENTICATING');
        var bitArray = sjcl.hash.sha256.hash(password);
        var hashedPassword = sjcl.codec.hex.fromBits(bitArray);
        var organization = SessionService.getUserOrganization();
        LoginService.authenticateFunctionary({ username: username }, { username: username, password: hashedPassword, organizationId:organization }, authenticateSuccess, authenticateError);
      }
    };

    /**
     * Shows the applet to sign. The user will select a certicate and will proceed with the
     * authentication process.
     */
    $scope.authenticateWithCerticate = function(){

      $scope.isLoading = true;
      $scope.loadingMessage = $filter('translate')('LOGIN_LOADING_APPLET');
      try{
        dtjava.embed(
          {
            id: 'appletfirma',
            url : '/lib/FXWebFormSign4J.jnlp',
            placeholder : 'javafxappplaceholder',
            width : 1,
            height : 1
          },
          {
            javafx : '2.2+'
          },
          {}
        );

        var applet = document.getElementById('appletfirma');
        var showPopup = false;

        applet.Sign("NoMattterContentToSign", "archivoDist", showPopup);
        $scope.loadingMessage = $filter('translate')('LOGIN_WAITING_CERTIFICATE_SELECTION');
      }catch(e){

        $scope.isLoading = false;

        var message = $filter('translate')('LOGIN_APPLET_ERROR');
        $.notify({
          // options
          message: message
        },{
          // settings
          type: 'danger',
          z_index : 9999,
          delay : 10000,
          placement:{
            align:'center'
          }
        });
        //$scope.loadingMessage = "Espere un momento por favor";
        //setTimeout(function(){ $scope.authenticateWithCerticate(); }, 3000);

      }
    };

    /**
     * Executes a function passed as parameter. It is used when the applet finished signing
     * @param fn : Function to execute
     */
    $scope.safeApply = function( fn ) {
      var phase = this.$root.$$phase;
      if(phase == '$apply' || phase == '$digest') {
        if(fn) {
          fn();
        }
      } else {
        this.$apply(fn);
      }
    };

    /**
     * Watches the valueChanged variable to call the service. The variable change indicates
     * that the sign process has finished and the signed data is in scope
     */
    $scope.$watch('valueChanged', function(newValue, oldValue) {
      if(newValue) {
        $scope.valueChanged = false;
        $scope.loadingMessage = $filter('translate')('LOGIN_AUTHENTICATING');
        LoginWithCertificateService.authenticate({organization:SessionService.getUserOrganization()},$scope.signedChunk,authenticateSuccess,authenticateError);
      }
    });

    $scope.openRequestRecoverPopUp = function() {

      $modal.open({
          controller: 'RequestRecoverPasswordController',
          templateUrl: '../../../views/security/request-recover-password.html'
      });

    }

    /**
     * Function executed after the service authenticate has resulted successful
     * @param data
     */
    function authenticateSuccess(data) {

      $scope.isLoading = false;

      if (data) {

        SessionService.setAuthorizationToken(data.tokenValue);
        SessionService.setAuthorizationUserName(data.username);
        SessionService.setAuthorizationUserOrganization(data.userOrganization);
        SessionService.setAuthorizationUserRole(data.businessRole);
        SessionService.setAuthorizationSystemRole(data.systemRole);
        SessionService.setAuthorizationImage(data.image);

        var systemRole = data.systemRole;

        if(systemRole == "funcionario") {

          $state.go('portal-funcionario-home');

        } else if(systemRole == "revisor") {

          $state.go('ventanilla-unica-home');

        } else if(systemRole == "admin") {

          $state.go('tenant-admin-home');

        } else if(systemRole == "superAdmin") {

          $state.go('super-admin-home');

        } else if(systemRole == "ciudadano") {

          $state.go('no-credentials');
          SessionService.removeCookie();

        }

      }
    }

    /**
     * Function executed after the service authenticate has resulted failed
     * @param data
     */
    function authenticateError(data) {

      $scope.isLoading = false;
      $scope.password = '';

      var message = '';

      if (data.status == 0) {

        message = 'Error de conexión, por favor verifique su acceso a internet o contacte a soporte.';

      } else {

        if(data.data.type == "business") {
          message = data.data.errorMessage;
        }else {
          message = "Ha ocurrido un error"
        }
      }
      
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 10000
      });
    }
  }]);
