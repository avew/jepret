'use strict';

angular.module('jepretApp').controller('PhotoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Photo',
        function($scope, $stateParams, $uibModalInstance, entity, Photo) {

        $scope.photo = entity;
        $scope.load = function(id) {
            Photo.get({id : id}, function(result) {
                $scope.photo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jepretApp:photoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.photo.id != null) {
                Photo.update($scope.photo, onSaveSuccess, onSaveError);
            } else {
                Photo.save($scope.photo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
