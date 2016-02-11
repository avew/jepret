'use strict';

angular.module('jepretApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('photo', {
                parent: 'entity',
                url: '/photos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jepretApp.photo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photo/photos.html',
                        controller: 'PhotoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('photo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('photo.detail', {
                parent: 'entity',
                url: '/photo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jepretApp.photo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/photo/photo-detail.html',
                        controller: 'PhotoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('photo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Photo', function($stateParams, Photo) {
                        return Photo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('photo.new', {
                parent: 'photo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/photo/photo-dialog.html',
                        controller: 'PhotoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    location: null,
                                    rating: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('photo', null, { reload: true });
                    }, function() {
                        $state.go('photo');
                    })
                }]
            })
            .state('photo.edit', {
                parent: 'photo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/photo/photo-dialog.html',
                        controller: 'PhotoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Photo', function(Photo) {
                                return Photo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('photo.delete', {
                parent: 'photo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/photo/photo-delete-dialog.html',
                        controller: 'PhotoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Photo', function(Photo) {
                                return Photo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('photo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
