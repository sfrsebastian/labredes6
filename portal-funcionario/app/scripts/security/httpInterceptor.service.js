'use strict';

securityModule.factory('HttpInterceptorService', ['SessionService', function(SessionService) {

    return {
        request: function (config) {

            config.headers = config.headers || {};

            if (SessionService.hasAuthorizationToken()) {
                config.headers.Authorization = SessionService.getAuthorizationToken();
            }else{
              config.headers.Authorization = 'anonymous';
            }

            return config;
        }
    };
}]);
