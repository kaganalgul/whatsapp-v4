angular.module('w-app')
    .component('session', {
        templateUrl: '/app/template/session.html',
        controller: function($scope, MessageApi, SessionApi, $routeParams, SocketService) {

            $scope.sendMessage = function() {
                MessageApi.send({sessionId: $scope.sessionId, friendId: $scope.friendId, message: $scope.message});
                $scope.message = '';
            }

            $scope.typing = function() {
                $scope.userStatus = 'typing..'
            }

            $scope.init = function() {
                $scope.friendId = $routeParams.userId;
                $scope.sender = current_user;
                $scope.title = $routeParams.name;
                $scope.sessionId = $routeParams.id;
                $scope.messages = MessageApi.list({id :$scope.sessionId});
                $scope.userAvatar = $routeParams.userAvatar;
                $scope.userStatus = $routeParams.userStatus;
                console.log($scope.userAvatar);
                console.log($scope.userStatus);
                let connect = SocketService.connect();
                
                connect.then(function() {
                        SocketService.subscribe("/whatsapp/send-message/" + $scope.sessionId, function(data) {
                            $scope.messages.push(data);
                            $scope.$apply();
                    });
                })
            };

            $scope.init();
        }
    })