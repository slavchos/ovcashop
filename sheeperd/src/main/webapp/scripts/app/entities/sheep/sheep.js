'use strict';

angular.module('sheeperdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sheep', {
                parent: 'entity',
                url: '/sheeps',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sheeps'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sheep/sheeps.html',
                        controller: 'SheepController'
                    }
                },
                resolve: {
                }
            })
            .state('sheep.detail', {
                parent: 'entity',
                url: '/sheep/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Sheep'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sheep/sheep-detail.html',
                        controller: 'SheepDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Sheep', function($stateParams, Sheep) {
                        return Sheep.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sheep.new', {
                parent: 'sheep',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sheep/sheep-dialog.html',
                        controller: 'SheepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    age: null,
                                    age_last_shaved: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sheep', null, { reload: true });
                    }, function() {
                        $state.go('sheep');
                    })
                }]
            })
            .state('sheep.edit', {
                parent: 'sheep',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sheep/sheep-dialog.html',
                        controller: 'SheepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sheep', function(Sheep) {
                                return Sheep.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sheep', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sheep.delete', {
                parent: 'sheep',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sheep/sheep-delete-dialog.html',
                        controller: 'SheepDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Sheep', function(Sheep) {
                                return Sheep.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sheep', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
