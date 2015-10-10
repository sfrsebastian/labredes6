/**
 * Created by LeanFactory on 4/06/15.
 */

'use strict';

adminModule.controller('NewTenantSettingsController', ['$scope','NewTenantSettingsService','StateService','$state','OrganizationAdminWithPhotoService',
  function($scope, NewTenantSettingsService, StateService, $state, OrganizationAdminWithPhotoService) {

    $scope.tenant = null;
    $scope.password = null;
    $scope.passwordConfirmation = null;

    /**
     * Saves the user in autheo service
     * @param tenant
     */
    $scope.saveTenant = function(tenant) {


      var bitArray = sjcl.hash.sha256.hash(password);
      var hashedPassword = sjcl.codec.hex.fromBits(bitArray);
      tenant.password = hashedPassword;

      var formData = new FormData();
      formData.append('file', tenant.adminPhoto);
      formData.append('tenantInfo',JSON.stringify($scope.tenant));

      OrganizationAdminWithPhotoService.createAdminWithPhoto(formData, createUserWithPhotoSuccess, createUserWithPhotoError);

    };

    $scope.adminPhotoSelected = function (files) {

      $scope.tenant.adminPhoto = files[0];
      $scope.adminPhotoUrl = URL.createObjectURL(files[0]);
      $scope.$apply();
    };

    function createUserWithPhotoSuccess(data){

      $state.go('assign-tenant-color',{"organizationId": $scope.tenant.organizationId});

    }

    /**
     * When the tenant is saved successfully
     * @param data
     */
    function setNewTenantSettingsSuccess(data) {

      $scope.isSaving = false;
      var message = "La organización ha sido creada correctamente correctamente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 20000
      });
    }

    /**
     * When the tenant saving has failed
     * @param data
     */
    function setNewTenantSettingsError(data){
      handleError(data);
    }

    function createUserWithPhotoError(data){
      handleError(data);
    }

    function handleError(data){

      $scope.isSaving = false;
      var message = "Error al crear la nueva organización en el sistema. Verifica los datos.";
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
