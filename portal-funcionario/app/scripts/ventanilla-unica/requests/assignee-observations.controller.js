/*38709747dc110c518d74*
 * Created by LeanFactory on 6/03/15.
 */

ventanillaUnicaModule.controller('ObservationsController', ['$scope','ngDialog', function($scope,ngDialog) {

  $scope.confirm = function() {

    $scope.disableButtons = true;

    ngDialog.close();
    $scope.approveRequest($scope.observations);

  }
}]);
