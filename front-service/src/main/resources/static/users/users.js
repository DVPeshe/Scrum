angular.module('market').controller('usersController', function ($scope, $http, $location, $localStorage, $rootScope) {
        const contextPath = 'http://localhost:5555/auth/api/v1/users';
        $localStorage.checkboxRolesListModel = null;

        $scope.loadUsers = function (page = 1) {
            $scope.lastClick = page;
            $http({
                url: contextPath,
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

        $scope.editRole = function (username, roles, id) {
            $rootScope.editUser = {username: username, roles: roles, id: id};
            $location.path('/editrole')
        }

        $scope.banUser = function (id) {
            $http.put(contextPath + '/' + id + '/ban').then(function () {
                $scope.loadUsers($scope.lastClick);
            }, function errorCallback(response) {
                alert(response.data.message);
            });
        }

        $scope.unbanUser = function (id) {
            $http.put(contextPath + '/' + id + '/unban').then(function () {
                $scope.loadUsers($scope.lastClick);
            }, function errorCallback(response) {
                alert(response.data.message);
            });
        }

        $scope.back = function () {
            $location.path('/admin')
        }

        $scope.showUserManagement = function (name) {
            return name === $localStorage.mstMarketUser.username;
        }

        $scope.isVisibleEditRole = function () {
            return $localStorage.visibleEditRole;
        }

        $scope.loadUsers();
    }
);
