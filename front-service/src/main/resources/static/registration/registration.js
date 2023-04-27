angular.module('market').controller('registrationController', function ($scope, $rootScope, $http, $location, $localStorage) {
    const avatarContextPath = 'http://localhost:5555/auth/api/v1/avatars';
    const userContextPath = 'http://localhost:5555/auth/api/v1/users';
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

    const formLabelText = {
        'username': 'Имя пользователя',
        'password': 'Пароль',
        'confirmPassword': 'Подтверждение пароля',
        'email': 'Ваш email',
        'fullName': 'Ваше имя'
    };

    $scope.reguser = {fullName: null, username: null, email: null, password: null, confirmPassword: null};
    $scope.userAvatar = {avatar: null};

    $scope.functionRegistration = function () {
        $http.post(userContextPath, $scope.reguser).then(function success(response) {
            if (response.data.token) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                $localStorage.mstMarketUser = {username: $scope.reguser.username, token: response.data.token};
                $rootScope.userEmail = $scope.reguser.email;
                $rootScope.username = $localStorage.mstMarketUser.username;
                alert('Привет ' + $localStorage.mstMarketUser.username + '!')
                $location.path("/");

                if ($scope.userAvatar.avatar) {
                    URL.revokeObjectURL(element.src);
                    $http.put(avatarContextPath + '/my', $scope.userAvatar).then(null, function error(response) {
                        alert('Не удалось сохранить аватар!');
                    });
                }
            }
        }, function error(response) {
            console.log(response);
            let errors = response.data.values;
            let message = 'Ошибка заполнения формы регистрации.\n';
            for (const error of errors) {
                message = message + '\nПоле \'' + formLabelText[error.fieldName] + '\': ' + error.description + '.';
            }
            alert(message);
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
        $scope.userAvatar.avatar = reader.result.split(',')[1];
    }

    function validFileType(file) {
        return fileTypes.includes(file.type);
    }

    input.addEventListener('change', updateImageDisplay);
});
