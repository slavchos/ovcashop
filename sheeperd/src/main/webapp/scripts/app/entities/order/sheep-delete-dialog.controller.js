'use strict';

angular.module('sheeperdApp')
	.controller('SheepDeleteController', function($scope, $uibModalInstance, entity, Sheep) {

        $scope.sheep = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Sheep.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
