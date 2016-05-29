'use strict';

angular.module('sheeperdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('order', {
                parent: 'entity',
                url: '/order',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Order'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/order/order.html',
                        controller: 'OrderController'
                    }
                },
                resolve: {
                }
            })
            .state('order.new', {
                parent: 'order',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/order/order-dialog.html',
                        controller: 'OrderDialogController',
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
                        $state.go('order', null, { reload: true });
                    }, function() {
                        $state.go('order');
                    })
                }]
            })
    });
