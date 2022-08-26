angular.module('w-app')
    .component('login', {
        templateUrl: '/app/template/login.html',
        controller: function($scope, AccountApi, $window) {
            $scope.login = function() {
                AccountApi.login($scope.loginRequest, function(authenticationResponse) {
                    if (authenticationResponse.code == 0) {
                        $window.location = '/home'
                    } else {
                        switch(authenticationResponse.code){
                            case 10:
                                toastr.info("password incorrect")
                                break;
                            case 11:
                                toastr.error("user not found");
                                break;
                        }
                    }
                });
            }

            $scope.init = function() {
                $scope.loginRequest = { };
            };

            $scope.init();
        }
    })