/**
 * Created by LeanFactory on 6/03/15.
 */
'use strict';

ventanillaUnicaModule.factory('TaskService', ['environment', '$resource', function(environment, $resource) {

    var url = environment.portalFunctionary + '/task';
    var defaultParams = {taskId:'@taskId'};

    var actions = {

        getTask: { method: 'GET', isArray: false, url: environment.workFlowManager + '/task/:taskId?isHistoric=:isHistoric' },
        getMultipleTasks: { method: 'GET', isArray: true },
        getMultipleTasksAlerts: { method: 'GET', isArray: true, url: url + '/alerts' },
        getHistoricTasks: {method: 'GET', isArray: false, url: environment.workFlowManager + '/historic-tasks'},
        getOpenTasks: {method: 'GET', isArray: false, url: environment.workFlowManager + '/task/open-tasks'},
        getOpenTasksAlerts: {method: 'GET', isArray: false, url: environment.workFlowManager + '/task/open-tasks/alerts'}
    };

    return $resource(url, defaultParams, actions);
}]);
