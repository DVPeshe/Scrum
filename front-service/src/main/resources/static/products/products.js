angular.module('market').controller('productsController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/api/v1/products/forAdmin';
    $scope.loadProducts = function (page = 1) {
        $scope.lastClickPage = page;
        $http({
            url: contextPath + '/getProduct',
            method: 'GET',
            params: {
                p: page,
                title_part: $scope.filter ? $scope.filter.title_part : null
            }
        }).then(function (response) {
            $scope.productsPage = response.data;
            $scope.generatePagesList($scope.productsPage.totalPages);
        });
    };

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }

    $scope.createNewProduct = function () {
        $location.path('/registrationProduct');
    }

    $scope.updateProduct = function (id, title, price, categoryTitle) {
        $localStorage.updateProductData = {
            id: id, title: title, price: price,
            categoryTitle: categoryTitle
        };
        $location.path('/updateProduct');
    }

    $scope.editVisibleProduct = function (id, flag) {
        $http({
            url: contextPath + '/editVisible/' + id,
            method: 'POST',
            params: {
                visible: flag
            }
        }).then(function (response) {
            $scope.loadProducts($scope.lastClickPage);
        });
    }

    $scope.back = function () {
        $location.path('/admin')
    }

    $scope.loadProducts();
});
