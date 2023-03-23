angular.module('market').controller('productCardController', function ($scope, $http, $rootScope, $location, $routeParams) {

    const contextPath = 'http://localhost:5555/core/api/v1/products'

    $scope.getProductCardById = function () {
        $http.get(contextPath + '/card/' + $routeParams.id)
            .then(function success(response) {
                $scope.productCard = response.data;
            });
    }

    $scope.getProductCardById();
});
