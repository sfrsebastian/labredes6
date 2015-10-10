'use strict';

adminModule.controller('RoleDetailController', ['$scope', '$state', 'environment','SessionService','RoleService',
  'environment', 'SessionService', function($scope, $state, environment, SessionService, RoleService) {

    RoleService.getBusinessRoles({organizationId: SessionService.getUserOrganization()}, GetBusinessRolesSuccess, GetBusinessRolesError);

    $scope.systemRoles = [
      {
        id:'funcionario',
        name: 'Funcionario'
      },
      {
        id:'ciudadano',
        name: 'Ciudadano'
      },
      {
        id:'revisor',
        name: 'Revisor'
      },
      {
        id:'admin',
        name: 'Administrador'
      },
    ];
    $scope.isSaving = false;


    /**
     * Saves the role in autheo service
     * @param user
     */
    $scope.saveRole = function(role) {
      $scope.isSaving = true;
      role.organization = SessionService.getUserOrganization();
      RoleService.createBusinessRole(role, saveRoleSuccess, saveRoleError);

    };

    /**
     * shows notification when the role was saved
     * @param data
     */
    function saveRoleSuccess(data){
      $scope.isSaving = false;
      RoleService.getBusinessRoles({organizationId: SessionService.getUserOrganization()}, GetBusinessRolesSuccess, GetBusinessRolesError);
      var message = "El rol ha sido creado correctamente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 10000
      });

    };

    /**
     * shows notification when the role saving failed
     * @param data
     */
    function saveRoleError(data){
      $scope.isSaving = false;
      var message = "Error al guardar el rol. Por favor inténtelo de nuevo o contacte al administrador";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 10000
      });
    };

    /**
     * Set the businessRole list
     * @param data
     * @constructor
     */
    function GetBusinessRolesSuccess(data){
      console.log(data);
      $scope.businessRoles = data;
    };

    /**
     * Shows a message notifying an error
     * @param data
     * @constructor
     */
    function GetBusinessRolesError(data){
      var message = "Error al obtener los roles del sistema. Por favor inténtelo de nuevo o contacte al administrador";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'danger',
        z_index : 9999,
        delay : 10000
      });
    };

  }]);


