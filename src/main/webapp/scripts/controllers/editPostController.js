

angular.module('tada').controller('EditPostController', function($scope, $routeParams, $location, PostResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.post = new PostResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/Posts");
        };
        PostResource.get({PostId:$routeParams.PostId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.post);
    };

    $scope.save = function() {
        var successCallback = function(){
            $location.path("/Posts");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.post.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Posts");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Posts");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.post.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});