'use strict';

angular.module('jepretApp')
    .controller('PhotoDetailController', function ($scope, $rootScope, $stateParams, entity, Photo) {
        $scope.photo = entity;
        $scope.load = function (id) {
            Photo.get({id: id}, function(result) {
                $scope.photo = result;
            });
        };
        var unsubscribe = $rootScope.$on('jepretApp:photoUpdate', function(event, result) {
            $scope.photo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
