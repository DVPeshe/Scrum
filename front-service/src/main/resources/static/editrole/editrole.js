angular.module('market').controller('roleController', function ($scope, $http, $rootScope, $location, $localStorage) {
    const contextPathUsers = 'http://localhost:5555/auth/api/v1/users';
    const contextPathRoles = 'http://localhost:5555/auth/api/v1/roles';

    $scope.editRole = function () {
        console.log($rootScope.edituser);
        $http.post(contextPathUsers + '/roleEdit', $rootScope.edituser).then(function succes(response) {
            if (response.data.value) {
                alert(response.data.value);
                $location.path('/users');
            }
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
        });
    }

    $scope.back = function () {
        $location.path('/users')
    }

    $scope.getRoles = function () {
        $http.get(contextPathRoles).then(function success(response) {
            $rootScope.edituser = $localStorage.lastEditUser;
            $scope.roleList = response.data
        });
    }

    $scope.getRoles();
});
