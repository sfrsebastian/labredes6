'use strict';

adminModule.controller('UsersController', ['$scope', 'StateService', 'UserService', 'MessageService','environment','SessionService','RoleService', 'UserWithPhotoService','$filter',
  function($scope, StateService, UserService, MessageService, environment, SessionService, RoleService, UserWithPhotoService, $filter) {

    $scope.documentNumberValidating = false;
    $scope.existsUserMessage = "";
    $scope.businessRoles = [];
    $scope.selectedBusinessRole = '';
    $scope.isSaving;
    $scope.documentTypes = [
      {name : 'Cedula', value: 'Cédula de ciudadanía'},
      {name:'CedulaExtranjeria', value:'Cédula de extranjería'},
      {name:'Pasaporte', value:'Pasaporte'},
      {name: 'TarjetaIdentidad', value:'Tarjeta de Identidad'}
    ];
    $scope.selectedDocumentType = '';

    RoleService.getBusinessRoles({organizationId: SessionService.getUserOrganization()}, GetBusinessRolesSuccess, GetBusinessRolesError);

    /**
     * Handles the change of a document number
     * @param documentNumber
     */
    $scope.documentNumberChanged = function(documentNumber) {

      $scope.existsUserMessage = "";
      $scope.documentExists = true;

      if(documentNumber && documentNumber.length > 3) {

        $scope.documentNumberValidating = true;
        UserService.userDocumentExists({documentNumber:documentNumber}, existsUserSuccess, existsUserError);
      }
    }

    /**
     * Handles the change of a document number
     * @param documentNumber
     */
    $scope.usernameChanged = function(username) {

      $scope.existsUserNameMessage = "";
      $scope.usernameExists = true;

      if(username) {

        $scope.usernameValidating = true;
        UserService.userUsernameExists({username:username}, existsUsernameSuccess, existsUsernameError);
      }
    };

    /**
     * Saves the user in autheo service
     * @param user
     */
    $scope.saveUser = function(user) {

      $scope.isSaving = true;

      var bitArray = sjcl.hash.sha256.hash(user.password);
      var hashedPassword = sjcl.codec.hex.fromBits(bitArray);

      user.password = hashedPassword;
      user.organizationId = SessionService.getUserOrganization();
      user.apiClient = "false";
      user.roleId = $scope.selectedBusinessRole.id;
      user.businessRoleId = $scope.selectedBusinessRole.businessRole;
      user.systemRoleId = $scope.selectedBusinessRole.systemRole;
      user.documentType = $scope.selectedDocumentType.value;

      var formData = new FormData();
      formData.append('file', $scope.userPhoto);
      formData.append('user',JSON.stringify(user));

      UserWithPhotoService.createUserWithPhoto(formData, saveSuccess, saveError);
    };

    $scope.fileSelected = function (files) {

      $scope.userPhoto = files[0];
      var preview = document.getElementById('preview');
      var url = URL.createObjectURL(files[0]);
      preview.setAttribute('src', url);
      $scope.$apply();
    };

    /**
     * When the user is saved successfully
     * @param data
     */
    function saveSuccess(data) {

      $scope.user = {};
      $scope.confirmPassword = '';
      $scope.createUserForm.$setPristine();
      $scope.selectedBusinessRole = '';

      $scope.existsUserMessage = '';
      $scope.existsUsernameMessage = '';

      $scope.isSaving = false;
      var message = "El usuario ha sido creado correctamente";
      $.notify({
        // options
        message: message
      },{
        // settings
        type: 'success',
        z_index : 9999,
        delay : 10000
      });
    }

    /**
     * When the user saving has failed
     * @param data
     */
    function saveError(data) {

      $scope.user.password = '';
      $scope.confirmPassword = '';
      $scope.isSaving = false;
      var message = "Error al crear usuario en el sistema. Verifica los datos del usuario.";
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

    /**
     * executes post operations after exists service success
     * @param data
     */
    function existsUserSuccess(data) {

      if(data.documentExists) {

        $scope.existsUserMessage = $filter('translate')('USERS_INVALID_IDENTITY');

      } else {

        $scope.existsUserMessage = $filter('translate')('USERS_VALID_IDENTITY');
      }

      $scope.documentExists = data.documentExists;
      $scope.documentNumberValidating = false;

    }

    /**
     * executes post operations after exists service failed
     * @param data
     */
    function existsUserError(data) {

      alert(data);
      $scope.documentNumberValidating = false;
    }

    /**
     * executes post operations after username exists service success
     * @param data
     */
    function existsUsernameSuccess(data) {

      if(data.usernameExists) {

        $scope.existsUsernameMessage = $filter('translate')('USERS_USERNAME_EXISTS');

      } else {

        $scope.existsUsernameMessage = $filter('translate')('USERS_USERNAME_VALID');
      }

      $scope.usernameExists = data.usernameExists;
      $scope.usernameValidating = false;
    }

    /**
     * executes post operations after username exists service failed
     * @param data
     */
    function existsUsernameError(data) {

      alert(data);
      $scope.usernameValidating = false;
    }

    /**
     * Set the businessRole list
     * @param data
     * @constructor
     */
    function GetBusinessRolesSuccess(data) {

      $scope.businessRoles = data;
    }

    /**
     * Shows a message notifying an error
     * @param data
     * @constructor
     */
    function GetBusinessRolesError(data) {

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
    }

  }]);


