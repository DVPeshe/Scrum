angular.module('market').controller('productCardController', function ($scope, $http, $rootScope, $location, $routeParams) {

    const contextPath = 'http://localhost:5555/core/api/v1/products'

    $scope.getProductCardById = function () {
        $http.get(contextPath + '/card/' + $routeParams.id)
            .then(function success(response) {
                $scope.productCard = response.data;
            });
    }

    $scope.visibleAddCard = function () {
        const gh = $routeParams.flag
        if (gh !== undefined) return false;
        return true;
    }
    $scope.loadComments = function (page = 1, productId = $routeParams.id) {
        $http({
            url: 'http://localhost:5555/comment/api/v1/comments',
            method: 'GET',
            params: {
                p: page,
                product_id: productId
            }
        }).then(function (response) {
            $scope.commentsPage = response.data;
            $scope.generatePagesList($scope.commentsPage.totalPages);
        });
    };

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }

    $scope.getProductCardById();
    $scope.loadComments();
});
