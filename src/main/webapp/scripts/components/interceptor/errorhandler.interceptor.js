'use strict';

angular.module('jepretApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('jepretApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });