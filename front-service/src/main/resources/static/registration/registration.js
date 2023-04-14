angular.module('market').controller('registrationController', function ($scope,$rootScope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/auth/';
    const avatarContextPath = 'http://localhost:5555/auth/api/v1/avatars';
    const input = document.querySelector('#image_uploads');
    const element = document.querySelector('#image');
    const reader = new FileReader();

    const fileTypes = [
        "image/apng",
        "image/bmp",
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/png",
        "image/svg+xml",
        "image/tiff",
        "image/webp",
        "image/x-icon"
    ];

    $scope.reguser = {fullName: null, username: null, email: null, password: null, confirmPassword: null};
    $scope.userAvatar = {username: null, avatar: null};

    $scope.functionRegistration = function () {
        $http.post(contextPath + 'register', $scope.reguser).then(function success(response) {
            if (response.data.token) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                $localStorage.mstMarketUser = {username: $scope.reguser.username, token: response.data.token};
                $rootScope.userEmail = $scope.reguser.email;
                alert('Привет ' + $localStorage.mstMarketUser.username + '!')
                $location.path("/");

                if ($scope.userAvatar.avatar) {
                    URL.revokeObjectURL(element.src);
                    $scope.userAvatar.username = $scope.reguser.username;
                    $http.post(avatarContextPath, $scope.userAvatar).then(null, function error(response) {
                        alert('Не удалось сохранить аватар!');
                    });
                }
            }
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
        });
    }

    function updateImageDisplay() {
        const curFiles = input.files;
        if (curFiles.length > 0) {
            for (const file of curFiles) {
                if (validFileType(file)) {
                    if (element.src) {
                        URL.revokeObjectURL(element.src);
                        $scope.userAvatar.avatar = null;
                    }
                    element.src = URL.createObjectURL(file);
                    reader.readAsDataURL(file);
                }
            }
        }
    }

    reader.onload = function () {
        const base64 = reader.result.split(',')[1];
        $scope.userAvatar.avatar = base64;
    }

    function validFileType(file) {
        return fileTypes.includes(file.type);
    }

    input.addEventListener('change', updateImageDisplay);
});
