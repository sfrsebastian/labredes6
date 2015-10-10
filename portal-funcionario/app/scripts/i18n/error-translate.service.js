/**
 * Created by LeanFactory on 16/04/15.
 */
portalFuncionarioApp.factory('ErrorTranslateService', ['$filter',  function($filter) {

  return {

    translateErrorMessage: function(data) {

      var message;

      if (data.status == 0) {

        message = $filter('translate')('VIRTUAL_OFFICE_CONNECTION_ERROR');

      } else if (data.status == 401) {

        message = $filter('translate')('VIRTUAL_OFFICE_AUTHORIZATION_ERROR');

      } else if (data.status == 403) {

        message = $filter('translate')('VIRTUAL_OFFICE_AUTHENTICATION_ERROR');

      } else if (data.status == 400) {

        message = $filter('translate')('VIRTUAL_OFFICE_TECHNICAL_ERROR');

      } else if (data.status == 500) {

        message = $filter('translate')('VIRTUAL_OFFICE_TECHNICAL_ERROR');

      } else if ((data.status == 418)) {

        message = $filter('translate')(data.data.errorMessage);

      } else if ((data.status == 418)) {

        message = data.data.errorMessage;
      }

      return message;

    }
  }
}]);



