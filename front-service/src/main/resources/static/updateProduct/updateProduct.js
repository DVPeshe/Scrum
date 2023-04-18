angular.module('market').controller('updateProductController', function ($scope, $http, $location, $localStorage) {
    $scope.upProduct = {id: null, title: null, price: null, categoryTitle: null, quantity: null, description: null};
    const contextPath = 'http://localhost:5555/core/api/v1';

    $scope.functionUpdateProduct = function () {
        $http.put(contextPath + '/products', $scope.upProduct).then(function success(response) {
            alert(response.data.value);
            $location.path('/products');
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
        });
    }

    $scope.back = function () {
        $location.path('/products')
    }

    $scope.getCategories = function () {
        $http.get(contextPath + '/categories').then(function success(response) {
            $scope.categoryList = response.data
        });
    }
    $scope.getCategories();

    $scope.getDataProduct = function () {
        $scope.upProduct.id = $localStorage.updateProductData.id;
        $scope.upProduct.title = $localStorage.updateProductData.title;
        $scope.upProduct.price = $localStorage.updateProductData.price;
        $scope.upProduct.categoryTitle = $localStorage.updateProductData.categoryTitle;
        $scope.upProduct.quantity = $localStorage.updateProductData.quantity;
        $scope.upProduct.description = $localStorage.updateProductData.description;
    }
    $scope.getDataProduct();
});
