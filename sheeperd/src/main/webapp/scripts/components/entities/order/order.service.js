'use strict';

angular.module('sheeperdApp')
    .factory('Order', function ($resource, DateUtils) {
    	console.log('resource ',$resource);
        return $resource('api/sheep-shop/order/days', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'save': { 
            	method:'POST',
        		transformRequest: function (data) {
        			console.log('DATA', data);
        	        data.stock.day = parseInt(data.stock.day);
        	        return angular.toJson(data);
        	    }
            }
        });
    });