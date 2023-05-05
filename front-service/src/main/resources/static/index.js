(function () {
    angular
        .module('market', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/store', {
                templateUrl: 'store/store.html',
                controller: 'storeController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/registration', {
                templateUrl: 'registration/registration.html',
                controller: 'registrationController'
            })
            .when('/admin', {
                templateUrl: 'admin/admin.html',
                controller: 'adminController'
            })
            .when('/users', {
                templateUrl: 'users/users.html',
                controller: 'usersController'
            })
            .when('/products', {
                templateUrl: 'products/products.html',
                controller: 'productsController'
            })
            .when('/editrole', {
                templateUrl: 'editrole/editrole.html',
                controller: 'roleController'
            })
            .when('/registrationProduct', {
                templateUrl: 'registrationProduct/registrationProduct.html',
                controller: 'registrationProductController'
            })
            .when('/updateProduct', {
                templateUrl: 'updateProduct/updateProduct.html',
                controller: 'updateProductController'
            })
            .when('/personalAccount', {
                templateUrl: 'personalAccount/personalAccount.html',
                controller: 'personalAccountController'
            })
            .when('/productCard', {
                templateUrl: 'productCard/productCard.html',
                controller: 'productCardController'
            })
            .when('/favorite', {
                templateUrl: 'favorite/favorite.html',
                controller: 'favoriteController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if ($localStorage.mstMarketUser) {
            try {
                let jwt = $localStorage.mstMarketUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.marchMarketUser;
                    $http.defaults.headers.common.Authorization = '';
                }
            } catch (e) {
            }

            if ($localStorage.mstMarketUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.mstMarketUser.token;
                $rootScope.username = $localStorage.mstMarketUser.username;
            }
        }
        if (!$localStorage.mstMarketGuestCartId) {
            $http.get('http://localhost:5555/cart/api/v1/cart/generate_id')
                .then(function (response) {
                    $localStorage.mstMarketGuestCartId = response.data.value;
                });
        }
    }
})();

angular.module('market').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
    $scope.tryToAuth = function () {
        $http.post('http://localhost:5555/auth/authentication', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {

                    $localStorage.visibleAdmin = response.data.visibleAdministrationButton;
                    $localStorage.visibleProduct = response.data.visibleProductPanelButton;
                    $localStorage.visibleUser = response.data.visibleUserPanelButton;
                    $localStorage.visibleEditRole = response.data.visibleEditRoleButton;

                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.mstMarketUser = {username: $scope.user.username, token: response.data.token};
                    $rootScope.username = $scope.user.username;
                    $scope.user.username = '';
                    $scope.user.password = '';
                    alert('Привет ' + $localStorage.mstMarketUser.username + '!')

                    $location.path('/');
                }
            }, function errorCallback(response) {
                let me = response;
                console.log(me)
                alert(me.data.message)
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        $localStorage.visibleAdmin = false;
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.mstMarketUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isVisibleAdmin = function () {
        return $localStorage.visibleAdmin;
    }

    $scope.logInToYourPersonalAccount = function () {
        $location.path('/personalAccount');
    }

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.mstMarketUser) {
            return true;
        } else {
            return false;
        }
    };
});