'use strict';

angular.module('sheeperdApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


