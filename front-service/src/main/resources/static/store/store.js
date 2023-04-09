angular.module('market').controller('storeController', function ($scope, $http, $localStorage, $rootScope, $location, $routeParams) {

    const contextPathImg = 'http://localhost:5555/image/api/v1/images/'

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
            $scope.generatePagesList($scope.productsPage.totalPages);
        });
    };

    $scope.showInfoById = function (id) {
        $location.path('/productCard').search('id=' + id);
    }

    $rootScope.addToCart = function (id) {
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


    $rootScope.suchAProductAlreadyExists = function (id) {
        if ($rootScope.currentCartUser) {
            for (let i = 0; i < $rootScope.currentCartUser.items.length; i++) {
                let product = $rootScope.currentCartUser.items[i];
                if (product.productId === id) return false;
            }
        }
        return true;
    }
    $scope.deleteFromFavorite = function (id) {
        $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId + '/delete/' + id)
            .then(function (response) {
                $scope.loadFavorite();
            });
    }

    $rootScope.inFavorite = function (id) {
        if ($rootScope.currentFavoriteUser) {
            for (let i = 0; i < $rootScope.currentFavoriteUser.items.length; i++) {
                let favProduct = $rootScope.currentFavoriteUser.items[i];
                if (favProduct.productId === id) return true;
            }
        }
        return false;
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
                $scope.getImageById()
            });
    };
    $scope.getCategories = function () {
        $http.get('http://localhost:5555/core/api/v1/categories').then(function success(response) {
            $scope.categoryList = response.data
        });
    }
    $scope.loadFavorite = function () {
        $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId)
            .then(function (response) {
                $scope.favorite = response.data;
                $rootScope.currentFavoriteUser = response.data;
            });
    };

    $scope.getImageById = function () {
        $http.get(contextPathImg + '6426a26deadb6c2a4764b738')
            .then(function success(response) {
                console.log(response.data)
                if (response.data) {
                    const image = response.data.image;
                    const binaryString = window.atob(image);
                    const bytes = new Uint8Array(binaryString.length);
                    const arrayBuffer = bytes.map((byte, i) => binaryString.charCodeAt(i));
                    $scope.image = URL.createObjectURL(new Blob([arrayBuffer], {type: 'image/*'}));
                }
            });
    }

    $scope.loadCart();
    $scope.loadProducts();
    $scope.getCategories();
    $scope.loadFavorite();
});