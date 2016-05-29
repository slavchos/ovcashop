'use strict';

angular.module('sheeperdApp')
    .controller('SheepController', function ($scope, $state, Sheep, SheepImport, ParseLinks) {

        $scope.sheeps = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Sheep.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.sheeps = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };
        
        $scope.readFile = function () {
        	console.log('read file');
        	SheepImport.readFile();
            $scope.loadAll();
        };

        $scope.clear = function () {
            $scope.sheep = {
                name: null,
                age: null,
                age_last_shaved: null,
                id: null
            };
        };
    });
