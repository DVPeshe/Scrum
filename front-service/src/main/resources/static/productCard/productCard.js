angular.module('market').controller('productCardController', function ($scope, $http, $localStorage, $rootScope, $location, $routeParams) {

    const contextPath = 'http://localhost:5555/core/api/v1/products'
    const contextPathImg = 'http://localhost:5555/image/api/v1/images/'

    $scope.comment = {user: null, product: null, description: null, estimation: null};
    $scope.productImage = {id : null, title : null, image : null};

    const element = document.querySelector('#image');
    const input = document.querySelector('#image_uploads');
    const reader = new FileReader();

    const imageTypes = [
        "image/jpg",
        "image/jpeg",
        "image/png",
    ];

    $scope.getEstimation = function (product = $scope.productCard) {
        $http({
            url: 'http://localhost:5555/comment/api/v1/comments/estimation',
            method: 'GET',
            params: {
                product: product.title
            }
        }).then(function (response) {
            $scope.avgEstimation = response.data;
        });
    }

    $scope.createComment = function () {
        $http.post('http://localhost:5555/comment/api/v1/comments/create', $scope.comment).then(function success(response) {
            alert(response.data.value);
            $scope.loadComments();
        }, function error(response) {
            let me = response;
            console.log(me);
            alert(me.data.message);
        });
    }

    $scope.getProductCardById = function () {
        $http.get(contextPath + '/card/' + $routeParams.id)
            .then(function success(response) {
                $scope.productCard = response.data;
                $scope.getImageById();
                $scope.loadComments();
                $scope.getDataComment();
            });
    }

    $scope.isVisibleAdmin = function () {
        return $localStorage.visibleAdmin;
    }

    $scope.visibleAddCard = function () {
        const gh = $routeParams.flag
        if (gh !== undefined) return false;
        return true;
    }
    $scope.getUser = function (){
        console.log($localStorage.mstMarketUser.username)
        $scope.username = $localStorage.mstMarketUser.username;
    }
    $scope.loadComments = function (page = 1, product = $scope.productCard) {
        $http({
            url: 'http://localhost:5555/comment/api/v1/comments',
            method: 'GET',
            params: {
                p: page,
                product: product.title
            }
        }).then(function (response) {
            $scope.commentsPage = response.data;
            $scope.generatePagesList($scope.commentsPage.totalPages);
            $scope.getEstimation();
        });
    };

    $scope.generatePagesList = function (totalPages) {
        out = [];
        for (let i = 0; i < totalPages; i++) {
            out.push(i + 1);
        }
        $scope.pagesList = out;
    }
    $scope.getDataComment = function () {
        $scope.comment.user = $localStorage.mstMarketUser.username;
        $scope.comment.product = $scope.productCard.title;

    }

    $scope.deleteComment = function (id){
        $http({
            url: 'http://localhost:5555/comment/api/v1/comments/'+id,
            method: 'DELETE',

        }).then(function (response) {
            $scope.loadComments()
        });
    }

    $scope.getImageById = function () {
        $http.get(contextPathImg + $scope.productCard.imageId)
            .then(function success(response) {
                console.log(response.data)
                if (response.data) {
                    const image = response.data.image;
                    const binaryString = window.atob(image);
                    const bytes = new Uint8Array(binaryString.length);
                    const arrayBuffer = bytes.map((byte, i) => binaryString.charCodeAt(i));
                    if (element.src) {
                        URL.revokeObjectURL(element.src);
                    }
                    element.src = URL.createObjectURL(new Blob([arrayBuffer], {type: 'image/*'}));
                }
            });
    }

    $scope.uploadImage = function () {
        if ($scope.productImage.image) {
            URL.revokeObjectURL(element.src);
            $http.post(contextPathImg + 'upload', $scope.productImage)
                .then(function success() {
                        alert('Удалось сохранить изображение!');
                    },
                    function error(response) {
                        alert('Не удалось сохранить изображение!');
                        let me = response;
                        console.log(me);
                        alert(me.data.message);
                    });
        }
    }

    $scope.deleteImage = function (productId) {
        if ($scope.productCard.imageId) {
            $scope.productImage.productId = productId;
            console.log($scope.productImage.productId);
            $http.delete(contextPathImg + $scope.productCard.imageId,
                {data: $scope.productImage, headers: {'Content-Type': 'application/json;charset=utf-8'}})
                .then(function success() {
                        alert('Удалось удалить изображение!');
                        $scope.getProductCardById();
                    },
                    function error(response) {
                        alert('Не удалось удалить изображение!');
                        let me = response;
                        console.log(me);
                        alert(me.data.message);
                    });
        }
    }

    function updateImage() {
        const curFiles = input.files;
        if (curFiles.length > 0) {
            for (const file of curFiles) {
                if (valiImageType(file)) {
                    if (element.src) {
                        URL.revokeObjectURL(element.src);
                        $scope.productImage.image = null;
                    }
                    element.src = URL.createObjectURL(file);
                    reader.readAsDataURL(file);
                    $scope.productImage.title = file.name;
                    $scope.productImage.productId = $scope.productCard.id;
                }
            }
        }
    }

    function valiImageType(file) {
        return imageTypes.includes(file.type);
    }

    reader.onload = function () {
        $scope.productImage.image = reader.result.split(',')[1];
    }

    input.addEventListener('change', updateImage);

    $scope.getProductCardById();

});
