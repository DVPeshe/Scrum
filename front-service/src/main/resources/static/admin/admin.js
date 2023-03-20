angular.module('market').controller('adminController', function ($scope, $location) {

    $scope.listUsers = function () {
        $location.path('/users')
    }

    $scope.listProducts = function () {
        $location.path('/products')
    }
});
