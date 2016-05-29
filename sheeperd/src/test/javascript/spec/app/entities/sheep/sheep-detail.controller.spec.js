'use strict';

describe('Controller Tests', function() {

    describe('Sheep Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSheep;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSheep = jasmine.createSpy('MockSheep');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Sheep': MockSheep
            };
            createController = function() {
                $injector.get('$controller')("SheepDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sheeperdApp:sheepUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
