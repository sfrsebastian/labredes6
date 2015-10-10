/**
 * Created by LeanFactory on 5/06/15.
 */
'use strict';

adminModule.controller('AssignTenantColorController', ['$scope','$stateParams','NewTenantSettingsService','TenantHexColorService',
  function($scope, $stateParams, NewTenantSettingsService, TenantHexColorService) {

    $scope.tenant = {};
    $scope.disableOrganizaiton = true;
    $scope.colorExist = false;
    $scope.styleDiv = ['solid','1px','black','none'];

    TenantHexColorService.getHexColor({tenantId: $stateParams.organizationId}, getHexColorSuccess, getHexColorError);

    function getHexColorSuccess(data){

      $scope.styleDiv = ['solid','1px','black','#' + data.hexColor];

      $scope.colorExist = false;
    }

    function getHexColorError(data){

      var message = "Hubo un error al cargar el color existente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 5000
      });
    }

    if ($stateParams.organizationId) {

      $scope.tenant.tenantId = $stateParams.organizationId;


    } else {

      var message = "Hubo un error por favor intentelo de nuevo";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 5000
      });

    }
    $scope.tenantLogoSelected = function (files) {

      $scope.logo = files[0];
      $scope.tenantLogoUrl = URL.createObjectURL(files[0]);
      $scope.$apply();
    };

    $scope.createTenantTheme = function (data){

      //Delete '#' special character form query param.
      var color = $scope.tenant.hexColor;
      $scope.tenant.hexColor = color.replace('#','');

      var formData = new FormData();

      formData.append('logoFile', $scope.logo);
      formData.append('tenantTheme', JSON.stringify($scope.tenant));

      NewTenantSettingsService.setNewTenantSettings(formData, setNewTenantSettingsSuccess, setNewTenantSettingsError);
    };

    function setNewTenantSettingsSuccess(data){

      var message = "El nuevo tema fue asiganado correctamente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 5000
      });

    }

    function setNewTenantSettingsError(data){

      var message = "No se pudo asignar el nuevo tema";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 5000
      });
    }
  }]);
