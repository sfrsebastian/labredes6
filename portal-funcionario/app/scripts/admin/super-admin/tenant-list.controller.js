/**
 * Created by LeanFactory on 24/06/15.
 */

adminModule.controller('TenantListController', ['$scope','TenantListService','$state',
  function($scope, TenantListService, $state) {

    $scope.showSpinner = true;
    TenantListService.getTenantList({},{},getTenantListSuccess, getTenantListError);

    function getTenantListSuccess(data){

      $scope.showSpinner = false;

      $scope.tenantList = data;

    }

    function getTenantListError(data){

      var message = "No se pudo consultar las organizaciones";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'error',
        z_index : 9999,
        delay : 20000
      });

    }

    $scope.editTenant = function (tenantName){

      $state.go('assign-tenant-color',{"organizationId": tenantName});

    };

  }]);
