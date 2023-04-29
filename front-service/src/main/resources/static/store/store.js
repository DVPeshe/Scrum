angular.module('market').controller('storeController', function ($scope, $http, $localStorage, $rootScope, $location) {

    const contextPathImg = 'http://localhost:5555/image/api/v1/images/'
    $scope.imageURLs = [];
    $scope.products = [];

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

            let products = response.data.content;
            $scope.products = [];
            for (let index = 0; index < products.length; index++) {
                getImageById(products[index].imageId, index);
                let product = products[index];
                product.imageIndex = index;
                $scope.products[index] = product;
            }

            $scope.generatePagesList($scope.productsPage.totalPages);
        });
    };

    $scope.showInfoById = function (id) {
        $location.path('/productCard').search('id=' + id);
    }

    $rootScope.addToCart = function (id) {
        $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
            .then(function (response) {
                $localStorage.currentCartUser = response.data;
                $scope.loadProducts();
            });
    }
    $scope.addToFavorite = function (id) {
        $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId + '/add/' + id)
            .then(function (response) {
                $rootScope.currentFavoriteUser = response.data;
            });
    }


    $rootScope.suchAProductAlreadyExists = function (id) {
        if ($localStorage.currentCartUser) {
            for (let i = 0; i < $localStorage.currentCartUser.items.length; i++) {
                let product = $localStorage.currentCartUser.items[i];
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
                $localStorage.currentCartUser = response.data;
            });
    };

    $scope.getCategories = function () {
        $http.get('http://localhost:5555/core/api/v1/categories/titles').then(function success(response) {
            $scope.categoryList = response.data.value
        });
    }
    $scope.loadFavorite = function () {
        $http.get('http://localhost:5555/favorite/api/v1/favorite/' + $localStorage.mstMarketGuestCartId)
            .then(function (response) {
                $scope.favorite = response.data;
                $rootScope.currentFavoriteUser = response.data;
            });
    };

    function getImageById(id, index) {
        $http.get(contextPathImg + id)
            .then(function success(response) {
                console.log(response.data)
                if (response.data) {
                    const image = response.data.image;
                    const binaryString = window.atob(image);
                    const bytes = new Uint8Array(binaryString.length);
                    const arrayBuffer = bytes.map((byte, i) => binaryString.charCodeAt(i));
                    $scope.imageURLs[index] = URL.createObjectURL(new Blob([arrayBuffer], {type: 'image/*'}));
                }
            });
    }

    $scope.subscribeBackToStock = function (id) {
        $http({
            url: 'http://localhost:5555/email/api/v1/subscription/my',
            method: 'POST',
            params: {
                productId: id
            }
        }).then(function (response) {
            alert('Вы получите оповещение на ваш email как только продукт снова появиться в продаже');
        });
    };

    $scope.loadCart();
    $scope.loadProducts();
    $scope.getCategories();
    $scope.loadFavorite();
});