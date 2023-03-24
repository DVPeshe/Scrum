angular.module('market').controller('storeController', function ($scope, $http, $localStorage, $rootScope,$location,$routeParams) {
    $scope.loadProducts = function (page = 1) {
        $http({
            url: 'http://localhost:5555/core/api/v1/products',
            method: 'GET',
            params: {
                p: page,
                title_part: $scope.filter ? $scope.filter.title_part : null,
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null,
                category_title: $scope.filter ? $scope.filter.category_title : null
            }
        }).then(function (response) {
            $scope.productsPage = response.data;
            for (let p of $scope.productsPage.content) {
                console.log(p)
            }
            $scope.generatePagesList($scope.productsPage.totalPages);
        });
    };

    $scope.showInfoById= function (id) {
           $location.path('/productCard').search('id=' + id);
    }

    $scope.addToCart = function (id) {
        $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
            .then(function (response) {
                $rootScope.currentCartUser = response.data;
            });
    }
    $scope.addToFavorite = function (id) {
        $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
            .then(function (response) {
                $rootScope.currentFavoriteUser = response.data;
            });
    }

    $scope.suchAProductAlreadyExists = function (id) {
        if ($rootScope.currentCartUser) {
            for (let i = 0; i < $rootScope.currentCartUser.items.length; i++) {
                let product = $rootScope.currentCartUser.items[i];
                console.log(product);
                if (product.productId === id) return false;
            }
        }
        return true;
    }

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }

    $scope.loadCart = function () {
        $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId)
            .then(function (response) {
                $scope.cart = response.data;
                $rootScope.currentCartUser = response.data;
            });
    };
    $scope.getCategories = function () {
        $http.get('http://localhost:5555/core/api/v1/categories').then(function success(response) {
            $scope.categoryList = response.data
        });
    }

    $scope.loadCart();
    $scope.loadProducts();
    $scope.getCategories();
});