angular.module('market').controller('personalAccountController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:5555/auth/api/v1/users';
    const input = document.querySelector('#image_uploads');
    const image = document.querySelector('#image');

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
    $scope.upUser = {username: null, password: null, confirmPassword: null, email: null};
    $scope.avatar = null;

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

    function updateImageDisplay() {
        const curFiles = input.files;
        if (curFiles.length > 0) {
            for (const file of curFiles) {
                if (validFileType(file)) {
                    $scope.avatar = file;
                    image.src = URL.createObjectURL(file);
                }
            }
        }
    }

    function validFileType(file) {
        return fileTypes.includes(file.type);
    }

    input.addEventListener('change', updateImageDisplay);
    $scope.getDataUser();
});
