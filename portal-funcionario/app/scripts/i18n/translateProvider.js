'use strict';

portalFuncionarioApp.config(function ($translateProvider) {
  $translateProvider.translations('es', {

    //Index.html
    INDEX_HOME_BUTTON: 'Home',
    INDEX_LOGIN_BUTTON: 'Login',
    INDEX_REPORTS_BUTTON: 'Reportes',
    INDEX_CERTICAMARA_FOOTER_NAME: 'Certicámara',
    INDEX_SETTINGS_MENU_SETTINGS: 'Configuración',
    INDEX_SETTINGS_MENU_USER: 'Administrar Usuarios',
    INDEX_SETTINGS_MENU_PROCESS: 'Cargar Nuevo Proceso',

    //login.html
    LOGIN_TITLE: 'Inicie Sesión',
    LOGIN_USERNAME_INPUT: 'Nombre de usuario',
    LOGIN_PASSWORD_INPUT: 'Contraseña',
    LOGIN_REMEMBER_ME_CHECKBOX: 'Recordarme',
    LOGIN_LOGIN_BUTTON: 'Usuario',
    LOGIN_LOGIN_WITH_CERTIFICATE: 'Certificado',
    LOGIN_FORGOT_PASSWORD_LINK: '¿Olvidó su contraseña?',
    LOGIN_TEXT: 'Contenido del Login',
    LOGIN_TITTLE: 'Cómo funciona',
    LOGIN_TITTLE_2: 'certisolución para empresas',
    LOGIN_NOT_AUTHORIZED: 'Usted no tiene credenciales en Portal Funcionario. Por favor iniciar sesión en Sede Electrónica',
    LOGIN_AUTHENTICATING: 'Iniciando Sesión',
    LOGIN_LOADING_APPLET: 'Cargando componente de firma',
    LOGIN_WAITING_CERTIFICATE_SELECTION: 'Esperando selección del certificado',
    LOGIN_APPLET_ERROR: 'Ha ocurrido un error al cargar el componente de firma. Por favor intente de nuevo.',

    //request-recover-password.html
    REQUEST_RECOVER_PASSWORD_TITLE: 'Por favor ingrese su correo electrónico',
    REQUEST_RECOVER_PASSWORD_EMAIL_INPUT: 'Correo electrónico',
    REQUEST_RECOVER_PASSWORD_EMAIL_INPUT_REQUIRED: 'El correo electrónico es requerido',
    REQUEST_RECOVER_PASSWORD_EMAIL_INPUT_INVALID: 'El correo electrónico no es valido',
    REQUEST_RECOVER_PASSWORD_REQUEST_CHANGE_BUTTON: 'Solicitar cambio',
    REQUEST_RECOVER_PASSWORD_CANCEL_BUTTON: 'Cancelar',
    REQUEST_RECOVER_PASSWORD_SUCCESS_MESSAGE: 'La solicitud de una nueva contraseña ha sido creada. Por favor revise su correo electrónico.',

    //recover-password.html
    RECOVER_PASSWORD_TITLE: 'Por favor ingrese la nueva contraseña',
    RECOVER_PASSWORD_PASSWORD_INPUT: 'Contraseña',
    RECOVER_PASSWORD_CONFIRM_PASSWORD_INPUT: 'Confirmar contraseña',
    RECOVER_PASSWORD_PASSWORD_INPUT_REQUIRED: 'La contraseña es requerida',
    RECOVER_PASSWORD_CONFIRM_PASSWORD_INPUT_REQUIRED: 'La confirmación de la contraseña es requerida',
    RECOVER_PASSWORD_CONFIRM_PASSWORD_INPUT_MISMATCH: 'La confirmación de la contraseña no coincide con la contraseña',
    RECOVER_PASSWORD_CHANGE_BUTTON: 'Cambiar',
    RECOVER_PASSWORD_CANCEL_BUTTON: 'Cancelar',
    //Header.controller.js
    HEADER_CONTROLLER_LOGOUT_ERROR: 'Error al hacer logout con el servidor',
    HEADER_CONTROLLER_USER_IMAGE_ERROR: 'Error al cargar la imagen del usuario',

    //tasks-list.html
    TASKS_LIST_TITLE: 'Tareas Abiertas',
    TASKS_LIST_CASE_ID: 'Número Caso',
    TASKS_LIST_PROCESS: 'Trámite',
    TASKS_LIST_SUBJECT: 'Asunto',
    TASKS_LIST_DATE_RECEIVED: 'Fecha recibido',

    //alert-tasks.html
    ALERT_TASKS_TITLE: 'Alertas',
    ALERT_TASKS_CASE_ID: 'Número Caso',
    ALERT_TASKS_PROCESS: 'Trámite',
    ALERT_TASKS_SUBJECT: 'Asunto',
    ALERT_TASKS_DATE_RECEIVED: 'Fecha recibido',
    ALERT_TASKS_STATE: 'Estado',

    //bossList.html
    BOSSLIST_TITLE: 'Lista del Jefes',

    //messages.hmtl
    MESSAGES_ERROR_MESSAGE: 'Error: ',

    //alerts.html
    ALERTS_TITLE: 'Alertas',
    ALERTS_FILED: 'Radicado',
    ALERTS_PROCESS: 'Trámite',
    ALERTS_SUBJECT: 'Asunto',
    ALERTS_FILED_DATE: 'Fecha radicación',
    ALERTS_USER: 'Usuario',
    ALERTS_STATE: 'Estado',
    ALERTS_LOADING_DATA: 'Se están cargando los datos',

    //request-details.html
    REQUEST_DETAILS_ASSIGN_TO: 'Asignar a:',
    REQUEST_DETAILS_APPROVE: 'Aprobar',
    REQUEST_DETAILS_REJECT: 'Rechazar ',
    REQUEST_DETAILS_SELECT: 'Seleccionar ',
    REQUEST_DETAILS_TITTLE: 'Detalle del Radicado',
    REQUEST_DETAILS_LOADING_DATA: 'Se están cargando los datos',

    //reject-request.html
    REJECT_REQUEST_TITTLE: 'Rechazar Radicado',
    REJECT_REQUEST_LABEL: 'Por favor, escriba los argumentos de su rechazo.',
    REJECT_REQUEST_SEND: 'Enviar',
    REJECTION_SIGNING_REJECTION: 'Firmando el rechazo',
    REJECTION_LOADING_APPLET: 'Cargando applet de firma',
    REJECTION_WAITING_CERTIFICATE_SELECTION: 'Esperando selección de certificado',
    REJECTION_SUCESS: 'Ha rechazado la solicitud',

    //documentList.html
    DOCUMENTLIST_NAME: 'Nombre',
    DOCUMENTLIST_TYPE: 'Tipo',
    DOCUMENTLIST_SIZE: 'Tamaño',
    DOCUMENTLIST_VERSION: 'Version',
    DOCUMENTLIST_TITLE: 'Documentos',
    DOCUMENTLIST_LOADING_DOCUMENTS: 'Se están cargando los documentos',

    //add-documents.html
    ADD_DOCUMENTS_TITLE: 'Adicionar Documentos',
    ADD_DOCUMENTS_NO_FILE_SELECTED: 'No se ha seleccionado archivo.',
    ADD_DOCUMENTS_FILE_SIZE_VALIDATION: 'El archivo supera el tamaño máximo de 2MB.',
    ADD_DOCUMENTS_FILE_TYPE_VALIDATION: 'El tipo de archivo no es permitido (Formatos permitidos .pdf, .jpg, .tif, y .doc ó .docx).',
    ADD_DOCUMENTS_UPLOAD_FILE_SUCCESS: 'El archivo fue adjuntado correctamente.',
    ADD_DOCUMENTS_BROWSE_BUTTON: 'Seleccionar',
    ADD_DOCUMENTS_EVIDENCE_LABEL: 'Evidencia',
    ADD_DOCUMENTS_CLEAR_BUTTON: 'Borrar',
    ADD_DOCUMENTS_DOWNLOAD_BUTTON: 'Descargar',
    ADD_DOCUMENTS_ADD_EVIDENCE_BUTTON: 'Agregar',

    //Home.html
    HOME_TITTLE: 'Certisolución',
    HOME_DESCRIPTION: 'Sistema de automatización de procesos de gestión documental.',
    HOME_ABIERTAS: 'Abiertas',
    HOME_PENDIENTES: 'pendientes',
    HOME_USUARIO: 'Usuario: ',
    HOME_ALERTAS: 'Alertas',
    HOME_REVISADAS: 'Revisadas',

    //successSaving.html
    SUCCESS_SAVING_DESCRIPTION: 'La configuración ha sido guardada de manera correcta.',

    //tenentSettings.html
    TENANT_SETTINGS_NAME: 'Nombre Organización',
    TENANT_SETTINGS_ALERT_TIME: 'Tiempo de alerta en minutos',
    TENANT_SETTINGS_SAVE: 'Guardar',
    TENANT_SETTINGS_MAX_ROWS: 'Máximo número de registros en las búsquedas',
    TENANT_SETTINGS_TITLE: 'Configuración del tenant',
    TENANT_SETTINGS_SIGNING_CERTIFICATE: 'Certificado para firma',
    TENANT_SETTINGS_SIGNING_PASSWORD: 'Contraseña para el certificado de firma',
    TENANT_SETTINGS_STAMPING_CERTIFICATE: 'Certificado para estampa',
    TENANT_SETTINGS_STAMPING_PASSWORD: 'Contraseña para el certificado de estampa',
    TENANT_SETTINGS_SIGNING_IMAGE: 'Imagen de la firma',
    TENANT_SETTINGS_CHANGE: 'Cambiar',
    TENANT_SETTINGS_SUCCESS_SAVING: 'Configuración guardada correctamente',
    TENANT_SETTINGS_ERROR_SAVING: 'Ha ocurrido un error al guardar la configuración',
    TENANT_SETTINGS_ERROR_GETTING: 'No se ha encontrado la configuración',

    //revisor-home.html
    REVISOR_HOME_SHOW_MORE: 'Ver más.',

    //pdfViewerTemplate.html
    PDF_VIEWER_TEMPLATE_BACK: 'Atrás',
    PDF_VIEWER_TEMPLATE_NEXT: 'Siguiente',
    PDF_VIEWER_TEMPLATE_ZOOM_IN: '+',
    PDF_VIEWER_TEMPLATE_ZOOM_OUT: '-',
    PDF_VIEWER_TEMPLATE_ROTATE: 'Rotar 90°',
    PDF_VIEWER_TEMPLATE_PAGE: 'Página: ',
    PDF_VIEWER_VIEW_SIGNATURES: 'Firmas Digitales',

    //request-detail.html
    REQUEST_DETAIL_REQUEST_INFO: 'Información de radicado',
    REQUEST_DETAIL_NO_RADICADO: 'No. Radicado',
    REQUEST_DETAIL_DATE: 'Fecha de Radicación',
    REQUEST_DETAIL_DUE_DATE: 'Fecha de Vencimiento',
    REQUEST_DETAIL_STEP: 'Trámite',
    REQUEST_DETAIL_SUBJECT: 'Asunto',
    REQUEST_DETAIL_REMITTER: 'Solicitante',
    REQUEST_DETAIL_EMAIL: 'Email Solicitante',
    REQUEST_DETAIL_ID_REMITTER: 'Identificación Solicitante',
    REQUEST_DETAIL_TYPE: 'Tipo de documento',
    REQUEST_DETAIL_ID_CASE: 'Número del caso',
    REQUEST_DETAIL_INFO: 'Información del Trámite',
    REQUEST_DETAIL_OWNER: 'Nombre del Responsable',
    REQUEST_DETAIL_OBSERVATIONS: 'Observaciones',
    REQUEST_DETAIL_REQUEST_STATUS: 'Estado de la Radicación',
    REQUEST_DETAIL_STATUS: 'Estado del trámite',
    REQUEST_DETAIL_WAITING_FOR_APPROVAL: 'En aprobación',
    REQUEST_DETAIL_RESPONSIBLE: 'Nombre del responsable',
    REQUEST_DETAILS_CONFIRM: 'Confirmar',
    REQUEST_DETAIL_LOADING_INFO: 'Se está cargando la información',

    //request.html
    REQUESTS_TITLE: 'Solicitudes',
    REQUESTS_FILED: 'Radicado',
    REQUESTS_PROCESS: 'Trámite',
    REQUESTS_SUBJECT: 'Asunto',
    REQUESTS_FILED_DATE: 'Fecha radicación',
    REQUESTS_EXPECTED_FINISH_DATE: 'Fecha de vencimiento',
    REQUESTS_USER: 'Usuario',
    REQUESTS_STATE: 'Estado',
    REQUESTS_LOADING_DATA: 'Se están cargando los datos',

    //magicform.html
    MAGIC_FORM_TITTLE: 'Información Adicional',

    //reviewed-requests.html
    REVIEWED_REQUESTS_TITLE: 'Tareas Revisadas',
    REVIEWED_REQUESTS_SEARCH: 'Buscar',
    REVIEWED_REQUESTS_PROCESS_NAME: 'Nombre del trámite',
    REVIEWED_REQUESTS_FILED_NUMBER: 'Número de radicado',
    REVIEWED_REQUESTS_CASE_NUMBER: 'Número de caso',
    REVIEWED_REQUESTS_BOSS_NAME: 'Nombre del jefe',
    REVIEWED_REQUESTS_CITIZEN_NAME: 'Nombre del ciudadano',
    REVIEWED_REQUESTS_CITIZEN_DOCUMENT_NUMBER: 'Número de documento del ciudadano',
    REVIEWED_REQUESTS_ASSIGNED_DATE_FROM: 'Desde: Fecha de asignado',
    REVIEWED_REQUESTS_ASSIGNED_DATE_TO: 'Hasta: Fecha de asignado',
    REVIEWED_REQUESTS_TABLE_FILED: 'Radicado',
    REVIEWED_REQUESTS_TABLE_PROCESS: 'Trámite',
    REVIEWED_REQUESTS_TABLE_CASE: 'Caso',
    REVIEWED_REQUESTS_TABLE_ASSIGNED_DATE: 'Fecha asignación',
    REVIEWED_REQUESTS_TABLE_ASSIGNED_USER: 'Usuario asignado',
    REVIEWED_REQUESTS_TABLE_USER: 'Usuario',
    REVIEWED_REQUESTS_TABLE_USER_DOCUMENT_TYPE: 'Tipo Doc Usuario',
    REVIEWED_REQUESTS_TABLE_USER_DOCUMENT_NUMBER: 'Número Doc Usuario',
    REVIEWED_REQUESTS_TABLE_ACTIVITY: 'Actividad',
    REVIEWED_REQUEST_LOADING_DATA: 'Los datos se están cargando',

    //userDetail.html
    USER_TITLE: 'Creación de usuarios',
    USERS_NAME: 'Nombre',
    USERS_DOCUMENT_TYPE: 'Tipo de documento',
    USERS_DOCUMENT_NUMBER: 'Número del documento',
    USERS_EMAIL: 'Correo Electrónico',
    USERS_TELEPHONE_NUMBER: 'Teléfono',
    USERS_ADDRESS: 'Dirección',
    USERS_USERNAME: 'Nombre de usuario',
    USERS_PASSWORD: 'Contraseña',
    USERS_CONFIRM_PASSWORD: 'Confirmar contraseña',
    USERS_ROLE: 'Rol',
    USERS_SAVE: 'Guardar',
    USERS_PHOTO: 'Foto',
    USERS_NAME_REQUIRED: 'El nombre es requerido',
    USERS_DOCUMENT_TYPE_REQUIRED: 'El tipo de documento es requerido',
    USERS_DOCUMENT_NUMBER_REQUIRED: 'El número de documento es requerido',
    USERS_DOCUMENT_NUMBER_INVALID: 'La longitud mínima del número de documento es de 4',
    USERS_DOCUMENT_NUMBER_FORMAT: 'El formato del documento de identidad es inválido',
    USERS_EMAIL_REQUIRED: 'El correo electrónico es requerido',
    USERS_EMAIL_INVALID: 'El correo electrónico no es valido',
    USERS_TELEPHONE_NUMBER_REQUIRED: 'El teléfono es requerido',
    USERS_TELEPHONE_NUMBER_FORMAT: 'El teléfono es incorrecto',
    USERS_TELEPHONE_MINLENGTH: 'El teléfono es muy corto',
    USERS_ADDRESS_REQUIRED: 'La dirección es requerida',
    USERS_USERNAME_REQUIRED: 'El nombre de usuario es requerido',
    USERS_PASSWORD_REQUIRED: 'La contraseña es requerida',
    USERS_CONFIRM_PASSWORD_REQUIRED: 'La confirmación de la contraseña es requerida',
    USERS_CONFIRM_PASSWORD_MISMATCH: 'La confirmación de la contraseña no coincide con la contraseña',
    USERS_INVALID_IDENTITY: 'El número de identificación ya existe en el sistema, por favor verifique e intente con otro número',
    USERS_VALID_IDENTITY: 'El número de identificación es válido',
    USERS_SELECT_ROLE: 'Seleccionar Rol',
    USERS_USERNAME_VALID: 'El nombre de usuario es válido',
    USERS_USERNAME_EXISTS: 'El nombre de usuario ya existe en el sistema',
    USERS_SELECT_DOCUMENT_TYPE: 'Seleccione el tipo de documento',

    //assigneeObservations.html
    ASSIGNEE_OBSERVATIONS_TITLE: 'Observaciones',
    ASSIGNEE_OBSERVATIONS_DETAILS: 'Digite las observaciones',

    //defaultTask.html
    COMPLETE_TASK_BUTTON: 'Terminar',

    //assign_to_user.html
    ASSIGN_TO_USER_ASSIGN_TO: 'Asignar a:',
    ASSIGN_TO_USER_CHOOSE_USER: 'Seleccionar:',
    ASSIGN_TO_USER_ASSIGN: 'Asignar',

    //Redact document
    REDACT_COMPLETE_TASK: 'Terminar Redacción',
    REDACT_CONFIRMATION_DIALOG_TITLE: '¿Esta seguro de de enviar el oficio?',
    REDACT_CONFIRMATION_DIALOG_ACCEPT_BUTTON: 'Enviar',
    REDACT_CONFIRMATION_DIALOG_CANCEL_BUTTON: 'Cancelar',
    REDACT_WITH_SIGNATURE_APPROVE: 'Aprobar y firmar',
    REDACT_WITH_SIGNATURE_REJECT: 'Rechazar',

    //completed-tasks.html
    COMPLETED_TASKS_TITLE: 'Tareas Completadas',
    COMPLETED_TASKS_PROCESS_NAME: 'Nombre del trámite',
    COMPLETED_TASKS_CASE_NUMBER: 'Número de caso',
    COMPLETED_TASKS_COMPLETED_DATE_FROM: 'Desde: Fecha Completada',
    COMPLETED_TASKS_COMPLETED_DATE_TO: 'Hasta: Fecha Completada',
    COMPLETED_TASKS_ASSIGNED_NAME: 'Nombre a quien se le asignó la tarea',
    COMPLETED_TASKS_CITIZEN_NAME: 'Nombre del ciudadano',
    COMPLETED_TASKS_CITIZEN_DOCUMENT_NUMBER: 'Número de documento del ciudadano',
    COMPLETED_TASKS_SEARCH: 'Buscar',
    COMPLETED_TASKS_TABLE_PROCESS: 'Trámite',
    COMPLETED_TASKS_TABLE_CASE: 'Caso',
    COMPLETED_TASKS_TABLE_ASSIGNED_DATE: 'Fecha asignación',
    COMPLETED_TASKS_TABLE_ASSIGNED_USER: 'Usuario asignado',
    COMPLETED_TASKS_TABLE_USER: 'Usuario',
    COMPLETED_TASKS_TABLE_USER_DOCUMENT_TYPE: 'Tipo Doc Usuario',
    COMPLETED_TASKS_TABLE_USER_DOCUMENT_NUMBER: 'Número Doc Usuario',
    COMPLETED_TASKS_TABLE_ACTIVITY: 'Actividad',

    //task-details.html

    TASK_DETAIL_VIEW_CASE: 'Ver Estado del Proceso',

    //task-details.html
    TASK_DETAIL_FILED_NUMBER: 'No. Radicado',
    TASK_DETAIL_FILED_DATE: 'Fecha de Radicación',
    TASK_DETAIL_TITTLE: 'Información de la Tarea',
    TASK_DETAIL_DUE_DATE: 'Fecha de Vencimiento',
    TASK_DETAIL_STEP: 'Trámite',
    TASK_DETAIL_SUBJECT: 'Asunto',
    TASK_DETAIL_REMITTER: 'Solicitante',
    TASK_DETAIL_EMAIL: 'Email Solicitante',
    TASK_DETAIL_ID_REMITTER: 'Identificación Solicitante',
    TASK_DETAIL_TYPE: 'Tipo de documento',
    TASK_DETAIL_ID_CASE: 'Número del caso',
    TASK_DETAIL_STATUS: 'Estado del trámite',
    TASK_DETAIL_RESPONSIBLE: 'Nombre del responsable',
    TASK_DETAIL_TASK_DUEDATE: 'Fecha máxima para finalizar tarea',
    TASK_DETAIL_REVIEWER_OBSERVATIONS: 'Observaciones del revisor',
    TASK_DETAIL_STATUS_PENDING: 'Pendiente',
    TASK_DETAIL_STATUS_REJECTED: 'Rechazada',
    TASK_DETAIL_STATUS_APPROVED: 'Aprobada',
    TASK_DETAIL_STATUS_FINISHED: 'Trámite finalizado',
    TASK_DETAIL_STATUS_WAITING_FOR_APPROVAL: 'En Aprobación',

    //process-diagram.html
    PROCESS_DIAGRAM_TITLE: 'Estado actual del Proceso',

    //process-upload.html
    PROCESS_UPLOAD_TITLE: 'Por favor seleccione el proceso a subir',
    PROCESS_LIST_TITLE: 'Configuración del Proceso',
    PROCESS_SETTINGS_PROCESS_INFO: 'Describir',
    PROCESS_SETTINGS_UPLOAD: 'Subir',
    PROCESS_SETTINGS_FORM: 'Generar Formulario',
    PROCESS_SETTINGS_NEW: 'Nuevo',
    PROCESS_SETTINGS_NEXT: 'Siguiente',
    PROCESS_SETTINGS_FINISH: 'Terminar',
    PROCESS_SETTINGS_NAME: 'Nombre',
    PROCESS_SETTINGS_DESCRIPTION: 'Descripción',
    PROCESS_SETTINGS_TIME_EXPIRE_REQUEST: 'Tiempo máximo de respuesta para la solicitud (minutos)',
    PROCESS_SETTINGS_TIME_EXPIRE_PROCESS: 'Tiempo máximo de respuesta para el proceso (minutos)',
    PROCESS_SETTINGS_TIME_ALERT_REQUEST: 'Tiempo de alerta para dar respuesta a la solicitud (minutos)',
    PROCESS_SETTINGS_TIME_ALERT_PROCESS: 'Tiempo de alerta para las tareas del proceso (minutos)',
    PROCESS_SETTINGS_ALLOW_REVISOR_UPLOAD_DOCUMENTS: 'Permitir revisor adjuntar documentos',
    PROCESS_SETTINGS_SIGN_UPLOADED_DOCUMENTS: 'Firmar documentos adjuntados por el revisor',
    PROCESS_SETTINGS_SAVING_SUCCESS: 'La configuración del proceso ha sido guardada correctamente',
    PROCESS_SETTINGS_SAVING_ERROR: 'No se ha podido guardar la configuración del proceso',
    PROCESS_SETTINGS_GETPROCEDURES_ERROR: 'No se pudieron obtener los procesos existentes',


    //request-trace-reports.html
    REQUEST_TRACE_REPORTS_TITLE: 'Reporte Radicación',
    REQUEST_TRACE_REPORTS_FILED_NUMBER_INPUT_SEARCH: 'Número de Radicación',
    REQUEST_TRACE_REPORTS_SEARCH_BUTTON: 'Buscar',
    REQUEST_TRACE_REPORTS_FILED_NUMBER: 'Número Radicación',
    REQUEST_TRACE_REPORTS_CASE_NUMBER: 'Número Caso',
    REQUEST_TRACE_REPORTS_FILED_DATE: 'Fecha Radicación',
    REQUEST_TRACE_REPORTS_EXPECTED_FINISH_DATE: 'Fecha esperada de finalización',
    REQUEST_TRACE_REPORTS_REAL_FINISH_DATE: 'Fecha real de finalización',
    REQUEST_TRACE_REPORTS_EVENTS_TITLE: 'Eventos',
    REQUEST_TRACE_REPORTS_DOWNLOAD_PDF_BUTTON: 'Descargar PDF',
    REQUEST_TRACE_REPORTS_DOWNLOAD_XLSX_BUTTON: 'Descargar Xlsx',

    //pdf-viewer-template.html
    PDF_VIEWER_TEMPLATE_LOADING_DOCUMENT: 'Se está cargando el documento',

    //role management
    ROLE_NAME: 'Nombre',
    ROLE_NAME_PLACEHOLDER: 'Ingrese el nombre del rol',
    ROLE_DESCRIPTION: 'Descripción',
    ROLE_DESCRIPTION_PLACEHOLDER: 'Ingrese una descripción para el rol',
    ROLE_AUTHORIZATION: 'Nivel de Autorización',
    ROLE_SELECT_AUTHORIZATION_LEVEL: 'Seleccione un nivel de autorización',
    ROLE_SAVE: 'Guardar Rol',
    ROLE_AVAILABLE_ROLES: 'Roles Disponibles',
    ROLE_TITLE: 'Creación de Roles',

    //process management
    PROCESS_UPLOAD_SUCCESS: 'Proceso cargado exitosamente',
    PROCESS_UPLOAD_FAILED: 'El proceso no pudo ser cargado. Intente de nuevo o contacte al administrador',
    PROCESSES_TITLE: 'Carga de nuevos procesos',

    // Customer-settings errors codes
    CS_GET_TENANT_CONFIG_BUSINESS_EXCEPTION: 'No existe configuración para el tenant correspondiente',
    CS_GET_SYSTEM_CONFIG_BUSINESS_EXCEPTION: 'No se ha encontrado la configuración requerida del sistema',

    //Autheo errors
    AUTHEO_GET_USERNAME_BY_PASSWORD_TOKEN_EXCEPTION: 'No se encontro el usuario requerido en el sistema. El tiempo de recuperación de la contraseña pudo haber expirado.',
    AUTHEO_GET_USER_EXCEPTION: 'No se encontro el usuario requerido en el sistema',
    AUTHEO_CREATE_PASSWORD_TOKEN_EXCEPTION: 'No se pudo generar la solicitud de recuperación de la contraseña',
    AUTHEO_DELETE_PASSWORD_TOKEN_EXCEPTION: 'No se pudo cambiar la contraseña',
    AUTHEO_UPDATE_USER_PASSWORD_EXCEPTION: 'No se pudo cambiar la contraseña',

    //System Settings
    SYSTEM_SIGNATURE_SETTINGS: 'Configuración del sistema',
    SYSTEM_SIGNATURE_KEYSTORE_PATH: 'Ruta del Keystore',
    SYSTEM_SIGNATURE_TSA_URL: 'Url de la TSA',
    SYSTEM_SIGNATURE_CRL_PATH: 'Ruta de la CRL',
    SYSTEM_SIGNATURE_TSA_POLICY: 'Política de la TSA',
    SYSTEM_SIGNATURE_SETTINGS_SAVE: 'Guardar',
    SYSTEM_DOMAIN: 'Dominio',
    SYSTEM_SETTING_ERROR_SAVING: 'No se pudo guardar la configuración del sistema',
    SYSTEM_SETTING_SUCCESS_SAVING: 'Se ha guardado la configuración del sistema correctamente',
    SYSTEM_OCSP_SERVER: 'OCSP Url',
    SYSTEM_VERIFICATION_TYPE: 'Tipo de Verificación',
    // new-tenant-settings.html
    NEW_TENANT_SETTINGS_TITTLE: 'Crear nueva organización',

    //assign-tenant-color.html
    ASSIGN_TENANT_COLOR_TITTLE: 'Datos de la organización',
    ASSIGN_TENANT_COLOR_TENANT_NAME: 'Nombre o razón social',
    ASSIGN_TENANT_COLOR_TENANT_COLOR: 'Color corporativo (HEX)',
    ASSIGN_TENANT_COLOR_NO_CURRENT_COLOR: 'Color no asignado',
    ASSIGN_TENANT_COLOR_CURRENT_COLOR: 'Color actual',
    ASSIGN_TENANT_COLOR_TENANT_LOGO: 'Logo',

    //create-tenant-admin.html
    CREATE_TENANT_ADMIN_TITTLE: 'Datos del administrador',
    CREATE_TENANT_ADMIN_PHOTO: 'Foto',
    CREATE_TENANT_ORGANIZATION_NAME:'Organization',
    CREATE_TENANT_ADMIN_NAME:'Nombre',
    CREATE_TENANT_ADMIN_DOCUMENT_TYPE:'Tipo de documento',
    CREATE_TENANT_ADMIN_DOCUMENT_NUMBER:'Número de documento',
    CREATE_TENANT_ADMIN_EMAIL:'Correo electrónico',
    CREATE_TENANT_USER_NAME:'Nombre de usuario',
    CREATE_TENANT_ADDRESS:'Dirección',
    CREATE_TENANT_TELEPHONE:'Teléfono',
    CREATE_TENANT_PASSWORD:'Contraseña',
    CREATE_TENANT_PASSWORD_CONFIRM:'Confirmación Contraseña',
    CREATE_TENANT_PASSWORD_NO_MATCH:'La contraseña y la confirmación no coinciden.',

    //tenant-list.html
    TENANT_LIST_TITTLE: 'Organizaciones',
    TENANT_LIST_LOADING_DATA: 'Cargando datos',
    TENANT_LIST_NAME: 'Nombre de la organización',

    //undeliveredMessages.html
    UNDELIVERED_MESSAGES_TITTLE: 'Transsaciones en espera',
    UNDELIVERED_MESSAGES_LOADING_DATA: 'Cargando datos',
    UNDELIVERED_MESSAGES_SELECTION: 'Selección',
    UNDELIVERED_MESSAGES_USER: 'Usuario',
    UNDELIVERED_MESSAGES_PROCESS: 'Proceso',
    UNDELIVERED_MESSAGES_UNSELECTED_MESSAGE: 'Haga click en un mensaje para ver su contenido'

  });
  $translateProvider.preferredLanguage('es');
});
