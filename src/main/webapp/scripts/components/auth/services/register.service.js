'use strict';

angular.module('jepretApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


