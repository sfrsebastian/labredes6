'use strict';

stateModule.factory('StateService', [function() {

    var json = {};

    json.currentRequest = '';
    json.currentCaseId = '';
    json.editor = null;
    json.tenantThemeInfo = {};

    json.setCurrentRequest = function(request) {
        json.currentRequest = request;
    };

    json.setTenantThemeInfo = function(tenantThemeInfo) {
      json.tenantThemeInfo = tenantThemeInfo;
    };

    json.getTenantThemeInfo = function() {
      return json.tenantThemeInfo;
    };

    json.getCurrentRequest = function() {
      return json.currentRequest;
    };

    json.setCurrentCaseId = function(caseId){
        json.currentCaseId = caseId;
    };

    json.getCurrentCaseId = function(){
      return json.currentCaseId;
    };

    json.getEditor = function() {
        return json.editor;
    };

    json.setEditor = function(editor) {
        json.editor = editor;
    };

    return json;

}]);
