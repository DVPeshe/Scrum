angular.module('market').controller('usersController', function ($scope, $http, $location, $localStorage, $rootScope) {
    const contextPath = 'http://localhost:5555/auth/api/v1/users';
    $localStorage.checkboxRolesListModel = null;

    $scope.loadUsers = function (page = 1) {
        $scope.lastClick = page;
        $http({
            url: contextPath + '/all',
            method: 'GET',
            params: {
                p: page,
                title_part: $scope.filter ? $scope.filter.title_part : null
            }
        }).then(function (response) {
            $scope.usersPage = response.data;
            $scope.generatePagesList($scope.usersPage.totalPages);
        });
    };

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }

    $scope.editRole = function (username, roles) {
        $rootScope.editUser = {username: username, roles: roles};
        $location.path('/editrole')
    }

    $scope.banUser = function (id, flag) {
        $http({
            url: contextPath + '/banUser/' + id,
            method: 'POST',
            params: {
                access: flag
            }
        }).then(function () {
            $scope.loadUsers($scope.lastClick);
        });
    }

    $scope.back = function () {
        $location.path('/admin')
    }

    $scope.showUserManagement = function (name) {
        return name === $localStorage.mstMarketUser.username;
    }

    $scope.loadUsers();
});
