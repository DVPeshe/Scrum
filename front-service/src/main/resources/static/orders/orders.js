angular.module('market').controller('ordersController', function ($scope, $http,$location,$routeParams) {
    $scope.loadOrders = function () {
        $http.get('http://localhost:5555/core/api/v1/orders')
            .then(function (response) {
                $scope.orders = response.data;
            });
    };

    $scope.clearOrders = function () {
        $http.get('http://localhost:5555/core/api/v1/orders/clear')
            .then(function (response) {
                $scope.orders = response.data;
            });
    };

    $scope.showInfoById= function (id) {
        const bov = 3;
        $location.path('/productCard').search({id: id, flag: bov});
    }

    $scope.loadOrders();
});