angular.module('market').controller('personalAccountController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/auth/api/v1/users';
    $scope.upUser = {username: null, password: null, confirmPassword: null, email: null};

    $scope.functionUpdateUser = function () {
        $http.post(contextPath + '/updateUser', $scope.upUser).then(function success(response) {
            alert(response.data.value);
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
        });
    }

    $scope.getDataUser = function () {
        let username = $localStorage.mstMarketUser.username
        $scope.upUser.username = username;
        $http.get(contextPath + '/email/' + username).then(function success(response) {
            console.log(response.data)
            if (response.data.value) {
                $scope.upUser.email = response.data.value;
            }
        });
    }

    $scope.getDataUser();
});
