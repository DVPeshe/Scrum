angular.module('market').controller('registrationProductController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/api/v1';
    $scope.regproduct = {title: null, price: null, categoryTitle: null, quantity: null, description: null};

    $scope.functionRegistrationProduct = function () {
        $http.post(contextPath + '/products/forAdmin/create', $scope.regproduct).then(function success(response) {
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
});
