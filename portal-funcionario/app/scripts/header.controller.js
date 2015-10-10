portalFuncionarioModule.controller('HeaderController', ['$scope', 'SessionService', 'LoginService', '$state', 'LoginOutService','$filter','StyleSheetService','TenantLogoService',
  function($scope, SessionService, LoginService, $state, LoginOutService, $filter, StyleSheetService, TenantLogoService) {

    StyleSheetService.getStyleSheet({tenantId: SessionService.getUserOrganization()},getStyleSheetSucces, getStyleSheetError);

    TenantLogoService.getTenantLogo({tenantId: SessionService.getUserOrganization()}, getTenantLogoSucces, getTenantLogoError);

    function getStyleSheetSucces(data){

      var css  = data.css;

      var blob = new Blob([css], { type: 'text/css' });

      var url = URL.createObjectURL(blob);

      $scope.tenantCssPath = url;
    }

    function getTenantLogoSucces(data){

      $scope.logo = data.logo;
    }

    function getStyleSheetError(data){

      console.log(data);

    }

    function getTenantLogoError(data){

      console.log(data);

    }

    $scope.isInSession = function() {

      SessionService.getAuthorizationToken();

      if (SessionService.getAuthorizationToken()) {

        if($scope.username == null){

          $scope.username = SessionService.getAuthorizationUserName();
          $scope.userRole = SessionService.getAuthorizationmUserRole();

        }
        return true;

      } else {

        return false;
      }
    };

    $scope.isAdmin = function (){

      var userRole = SessionService.getAuthorizationSystemRole();

      var isCurrentAdmin = false;

      if(userRole == "admin"){

        isCurrentAdmin = true;

      }

      return isCurrentAdmin;
    };

    $scope.isSuperAdmin = function (){

      var userRole = SessionService.getAuthorizationSystemRole();

      var isCurrentAdmin = false;

      if(userRole == "superAdmin"){

        isCurrentAdmin = true;

      }

      return isCurrentAdmin;
    };

    $scope.goHome = function() {

      var userRole = SessionService.getAuthorizationSystemRole();

      if (userRole == 'revisor'){

        $state.go('ventanilla-unica-home');

      } else if (userRole == 'funcionario'){

        $state.go('portal-funcionario-home');

      }
    };

    $scope.goOpen = function() {

      var userRole = SessionService.getAuthorizationSystemRole();

      if (userRole == 'revisor'){

        $state.go('requests');

      } else if (userRole == 'funcionario'){

        $state.go('tasks');

      }
    };

    $scope.goAlerts = function() {

      var userRole = SessionService.getAuthorizationSystemRole();

      if (userRole == 'revisor'){

        $state.go('alerts');

      } else if (userRole == 'funcionario'){

        $state.go('alert-tasks');

      }
    };

    $scope.goReviewed = function() {

      var userRole = SessionService.getAuthorizationSystemRole();

      if (userRole == 'revisor'){

        $state.go('reviewed');

      } else if (userRole == 'funcionario'){

        $state.go('completed-tasks');

      }
    };

    function watchHasAuthorization(scope) {

      return SessionService.hasAuthorizationToken();

    }

    function updateHeader(newValue, oldValue) {

      var tokenValue = SessionService.getAuthorizationToken();
      var username = SessionService.getAuthorizationUserName();

      if(SessionService.getAuthorizationImage()) {

        $scope.image = SessionService.getAuthorizationImage();

      } else if(tokenValue && username) {

        LoginService.getUserImage({ username: username, tokenValue: tokenValue }, getUserImageSuccess, getUserImageError);

      }

      $scope.username = username;
      $scope.userRole = SessionService.getAuthorizationmUserRole();

      $scope.showReports = newValue;

    }

    $scope.$watch(watchHasAuthorization, updateHeader);

    function getUserImageSuccess(data) {

      $scope.image = data.image;

    }

    function getUserImageError(error) {

      $.notify({
        // options
        message: $filter('translate')('HEADER_CONTROLLER_USER_IMAGE_ERROR')
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 5000
      });

    }

    $scope.logOutUser = function(){

      LoginOutService.logOutUser({tokenValue: SessionService.getAuthorizationToken()},{},logOutSuccess,logOutError);
      $state.go('login');
    };

    function logOutSuccess(){

      SessionService.removeCookie();
      $scope.requestList = null;
      $scope.alertList = null;
      $scope.reviewedList = null;
    }

    function logOutError(){

      SessionService.removeCookie();
      $scope.requestList = null;
      $scope.alertList = null;
      $scope.reviewedList = null;

      $.notify({
        // options
        message: $filter('translate')('HEADER_CONTROLLER_LOGOUT_ERROR')
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 5000
      });
    }
  }]);
