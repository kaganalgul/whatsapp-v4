angular.module('w-app')
    .component('register', {
        templateUrl: '/app/template/register.html',
        controller: function($scope, AccountApi, $location) {

            $scope.register = function() {
                AccountApi.register({user:$scope.user, file:$scope.file}, function() {
                    $location.path('/login')
                });
            };

            $scope.init = function() {
                $scope.user = { };
            };

            $scope.init();
        }
    })