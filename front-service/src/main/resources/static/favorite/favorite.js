angular.module('market').controller('favoriteController',
    function ($scope, $http, $localStorage, $rootScope, $location, $routeParams) {

        $scope.loadFavorite = function () {
            $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId)
                .then(function (response) {
                    $scope.favorite = response.data;
                    $rootScope.currentFavoriteUser = response.data;
                });
        };

        $scope.clearFavorite = function () {
            $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId + '/clear')
                .then(function (response) {
                    $scope.loadFavorite();
                });
        };


        $scope.deleteFromFavorite = function (id) {
            $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId + '/delete/' + id)
                .then(function (response) {
                    $scope.loadFavorite();
                });
        }
        $rootScope.addToCart = function (id) {
            $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
                .then(function (response) {
                    $rootScope.currentCartUser = response.data;
                });
        }


        $scope.showInfoById= function (id) {
            $location.path('/productCard').search('id=' + id);
        }

        $scope.loadFavorite();

    });
