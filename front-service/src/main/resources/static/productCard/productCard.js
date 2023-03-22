angular.module('market').controller('productCardController', function ($scope,$http,$location,$routeParams) {

 const contextPath = 'http://localhost:5555/core/api/v1/products'

 $scope.productId = $routeParams.id;

//TODO функция получает с бэка всю необходимую информацию о продукте по его id -> отобразить эту информацию в productCard.html
 $scope.getProductById = function () {
         $http.get(contextPath + '/products/'+ $scope.productId)
            .then(function success(response) {
                alert(responce.data);
                });
    }

 $scope.getProductById();
});
