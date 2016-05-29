'use strict';

angular.module('sheeperdApp').controller('SheepDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sheep',
        function($scope, $stateParams, $uibModalInstance, entity, Sheep) {

        $scope.sheep = entity;
        $scope.load = function(id) {
            Sheep.get({id : id}, function(result) {
                $scope.sheep = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('sheeperdApp:sheepUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sheep.id != null) {
                Sheep.update($scope.sheep, onSaveSuccess, onSaveError);
            } else {
                Sheep.save($scope.sheep, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
