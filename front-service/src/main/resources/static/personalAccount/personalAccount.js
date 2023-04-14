angular.module('market').controller('personalAccountController', function ($scope, $http, $location, $localStorage) {
    const userContextPath = 'http://localhost:5555/auth/api/v1/users';
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
    $scope.upUser = {username: null, password: null, confirmPassword: null, email: null, fullName: null};
    $scope.userAvatar = {avatar: null};
    $scope.userRoles = [];

    $scope.functionUpdateUser = function () {
        $http.put(userContextPath + '/edit-user', $scope.upUser).then(function success(response) {
            alert(response.data.value);
            $scope.upUser.password = null;
            $scope.upUser.confirmPassword = null;
            $scope.getUserData();
            $scope.getUserAvatar();
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
            $scope.upUser.password = null;
            $scope.upUser.confirmPassword = null;
            $scope.getUserData();
            $scope.getUserAvatar();
        });
    }

    $scope.updateAvatar = function (file) {
        reader.readAsDataURL(file);
    }

    reader.onload = function () {
        $scope.userAvatar.avatar = reader.result.split(',')[1];
        $http.put(avatarContextPath + '/my', $scope.userAvatar).then(function success(response) {
            $scope.getUserAvatar();
        }, function error(response) {
            $scope.getUserAvatar();
        });
    }

    $scope.getUserData = function () {
        $http.get(userContextPath + '/personal-data').then(function success(response) {
            console.log(response.data)
            if (response.data) {
                $scope.upUser.username = response.data.username;
                $scope.upUser.email = response.data.email;
                $scope.upUser.fullName = response.data.fullName;
            }
        });
        $http.get(userContextPath + '/role-titles').then(function success(response) {
            console.log(response.data)
            if (response.data) {
                $scope.userRoles = response.data;
            }
        });
    }

    $scope.getUserAvatar = function () {
        $http.get(avatarContextPath + '/my').then(function success(response) {
            console.log(response.data)
            if (response.data) {
                const avatar = response.data.avatar;
                const binaryString = window.atob(avatar);
                const bytes = new Uint8Array(binaryString.length);
                const arrayBuffer = bytes.map((byte, i) => binaryString.charCodeAt(i));
                if (element.src) {
                    URL.revokeObjectURL(element.src);
                }
                element.src = URL.createObjectURL(new Blob([arrayBuffer], {type: 'image/*'}));
            }
        });
    }

    function updateImageDisplay() {
        const curFiles = input.files;
        if (curFiles.length > 0) {
            for (const file of curFiles) {
                if (validFileType(file)) {
                    $scope.updateAvatar(file);
                }
            }
        }
    }

    function validFileType(file) {
        return fileTypes.includes(file.type);
    }

    input.addEventListener('change', updateImageDisplay);
    $scope.getUserData();
    $scope.getUserAvatar();
});
