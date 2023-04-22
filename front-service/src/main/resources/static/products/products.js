angular.module('market').controller('productsController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/core/api/v1/products';
    $scope.loadProducts = function (page = 1) {
        $scope.lastClickPage = page;
        $http({
            url: contextPath + '/cards',
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

    $scope.updateProduct = function (id, title, price, categoryTitle, quantity, description) {
        $localStorage.updateProductData = {
            id: id, title: title, price: price,
            categoryTitle: categoryTitle,
            quantity: quantity,
            description: description
        };
        $location.path('/updateProduct');
    }

    $scope.doVisibleProduct = function (id) {
        $http({
            url: contextPath + '/' + id + '/visualize',
            method: 'PUT'
        }).then(function (response) {
            $scope.loadProducts($scope.lastClickPage);
        });
    }

        $scope.doUnVisibleProduct = function (id) {
            $http({
                url: contextPath + '/' + id + '/unvisualize',
                method: 'PUT'
            }).then(function (response) {
                $scope.loadProducts($scope.lastClickPage);
            });
        }

    $scope.back = function () {
        $location.path('/admin')
    }

    $scope.showInfoById= function (id) {
         const bov = 3;
         $location.path('/productCard').search({id: id, flag: bov});
    }


    $scope.sendEmails = function (id) {
         $http.get('http://localhost:5555/email/api/v1/emails/sendEmailBackToStock/'+id).then(function success(response) {
             alert(response.data.value);
         });
     }

    $scope.loadProducts();
});
