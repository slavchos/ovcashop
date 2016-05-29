'use strict';

angular.module('sheeperdApp').controller('OrderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Order',
        function($scope, $stateParams, $uibModalInstance, entity, Order) {

        $scope.sheep = entity;
        $scope.load = function(id) {
        	Order.get({id : id}, function(result) {
                $scope.order = result;
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
//            if ($scope.sheep.id != null) {
//                Sheep.update($scope.sheep, onSaveSuccess, onSaveError);
//            } else {
//                Sheep.save($scope.sheep, onSaveSuccess, onSaveError);
//            }
            console.log('order ',$scope.order);
            Order.save($scope.order.stock.day.toString(), $scope.order, onSaveSuccess, onSaveError);
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
