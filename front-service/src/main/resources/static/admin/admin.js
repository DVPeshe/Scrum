angular.module('market').controller('adminController', function ($scope, $location, $localStorage) {

    $scope.listUsers = function () {
        $location.path('/users')
    }

    $scope.listProducts = function () {
        $location.path('/products')
    }

    $scope.isVisibleProduct = function () {
        return $localStorage.visibleProduct;
    }

    $scope.isVisibleUser = function () {
        return $localStorage.visibleUser;
    }

});
