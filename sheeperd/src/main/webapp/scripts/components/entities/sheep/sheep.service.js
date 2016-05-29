'use strict';

angular.module('sheeperdApp')
    .factory('Sheep', function ($resource, DateUtils) {
        return $resource('api/sheeps/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('SheepImport', function ($resource) {
        return $resource('api/sheeps/readfile', {}, {
        	  'readFile': {
                  method: 'GET'
              },
        });
    })
