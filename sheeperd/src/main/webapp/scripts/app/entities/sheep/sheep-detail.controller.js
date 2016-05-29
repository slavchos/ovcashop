'use strict';

angular.module('sheeperdApp')
    .controller('SheepDetailController', function ($scope, $rootScope, $stateParams, entity, Sheep) {
        $scope.sheep = entity;
        $scope.load = function (id) {
            Sheep.get({id: id}, function(result) {
                $scope.sheep = result;
            });
        };
        var unsubscribe = $rootScope.$on('sheeperdApp:sheepUpdate', function(event, result) {
            $scope.sheep = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
