angular.module('market').controller('roleController', function ($scope, $http, $rootScope, $location, $localStorage) {
    const contextPathUsers = 'http://localhost:5555/auth/api/v1/users';
    const contextPathRoles = 'http://localhost:5555/auth/api/v1/roles';
    const userRole = 'покупатель';
    $scope.user.roles = [];

    $scope.editRole = function () {
        $localStorage.lastEditUser.roles = $localStorage.checkboxRolesListModel;
        $scope.user.roles = $localStorage.lastEditUser.roles;
        $http.put(contextPathUsers + '/' + $localStorage.lastEditUser.id + '/roles', $scope.user)
            .then(function success() {
                alert('Права пользователя изменены.');
                $location.path('/users');
            }, function error(response) {
                let me = response;
                console.log(me);
                alert(me.data.message);
                $location.path('/users');
            });
    }

    $scope.back = function () {
        $location.path('/users')
    }

    $scope.getRoles = function () {
        $http.get(contextPathRoles + '/titles').then(function success(response) {
            console.log(response)
            $scope.roleList = response.data.values;
        });
    }

    $scope.getDataUser = function () {
        if ($rootScope.editUser !== undefined) {
            $localStorage.lastEditUser = $rootScope.editUser;
        }
        $scope.currentUserName = $localStorage.lastEditUser.username;
        if ($localStorage.checkboxRolesListModel === null) {
            $localStorage.checkboxRolesListModel = copyArray($localStorage.lastEditUser.roles);
        }
    }

    $scope.editListRole = function (role) {
        let index = searchRole(role, $localStorage.checkboxRolesListModel);
        if (index !== null) {
            $localStorage.checkboxRolesListModel.splice(index, 1)
            return;
        }
        $localStorage.checkboxRolesListModel.push(role);
    }

    $scope.checkRole = function (role) {
        return searchRole(role, $localStorage.checkboxRolesListModel) !== null;
    }

    $scope.equalsListsRoles = function () {
        let oldListRoles = $localStorage.lastEditUser.roles;
        let currentListRoles = $localStorage.checkboxRolesListModel;

        if (oldListRoles.length !== currentListRoles.length) return false;

        let index;
        for (let i = 0; i < oldListRoles.length; i++) {
            index = searchRole(oldListRoles[i], currentListRoles);
            if (index === null) return false;
        }

        return true;
    }

    function searchRole(role, listRole) {
        for (let i = 0; i < listRole.length; i++) {
            let roleUser = listRole[i];
            if (roleUser === role) return i;
        }
        return null;
    }

    function copyArray(array) {
        let newArray = [];
        for (let i = 0; i < array.length; i++) {
            newArray.push(array[i]);
        }
        return newArray;
    }

    $scope.isUser = function (role) {
        return role === userRole;
    }

    $scope.getRoles();
    $scope.getDataUser();
});
