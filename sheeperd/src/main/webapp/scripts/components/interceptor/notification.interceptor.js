 'use strict';

angular.module('sheeperdApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-sheeperdApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-sheeperdApp-params')});
                }
                return response;
            }
        };
    });
