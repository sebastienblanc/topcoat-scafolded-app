
angular.module('tada').controller('NewPostController', function ($scope, $location, locationParser, PostResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.post = $scope.post || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Posts');
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        PostResource.save($scope.post, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Posts");
    };
});