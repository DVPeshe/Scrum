angular.module('market').controller('cartController',
    function ($scope, $http, $localStorage, $rootScope, $location, $routeParams) {

        $scope.loadCart = function () {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId)
                .then(function (response) {
                    $scope.cart = response.data;
                    $rootScope.currentCartUser = response.data;
                });
        };

        $scope.clearCart = function () {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/clear')
                .then(function (response) {
                    $scope.loadCart();
                });
        };

        $scope.createOrder = function () {
            $http.post('http://localhost:5555/core/api/v1/orders')
                .then(function (response) {
                    $scope.loadCart();
                });
        }

        $scope.guestCreateOrder = function () {
            alert('Для оформления заказа необходимо войти в учетную запись');
        }

        $scope.deleteItemCart = function (id) {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/delete/' + id)
                .then(function (response) {
                    $scope.loadCart();
                });
        }

        $scope.addToCart = function (id) {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
                .then(function (response) {
                    $scope.loadCart();
                });
        }

        $scope.decrementQuantity = function (id) {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/decrement/' + id)
                .then(function (response) {
                    $scope.loadCart();
                });
        }

        $scope.showInfoById= function (id) {
            $location.path('/productCard').search('id=' + id);
        }

        $scope.loadCart();

    });
