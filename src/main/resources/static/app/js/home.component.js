angular.module('w-app')
    .component('home', {
        templateUrl: '/app/template/home.html',
        controller: function ($scope, AccountApi, $location, SocketService, SessionApi, $routeParams) {

            $scope.addNumber = function () {
                AccountApi.addFriend($scope.user.number);
            }

            $scope.add = function () {
                let receiverEmail = $scope.useremail;
                AccountApi.sendFriendRequest(receiverEmail, function (data) {
                    $uibModalInstance.close(data);
                });
            };

            $scope.newSession = function(friend) {
                let ret = SessionApi.newSession(friend.id);
                ret.$promise.then(function(data) {
                $location.path("/session").search({id: data.id, name: friend.name, userId: friend.id});
                })
            };

            $scope.openSession = function(session) {
                let ret = SessionApi.openSession(session.id);
                let users = session.users;
                let sender = current_user;
                let index = users.indexOf(sender);
                console.log(index);
                let user = users[0];
                ret.$promise.then(function(data) {
                $location.path("/session").search({id: data.id, name: session.title, userId: user.id, userAvatar: user.avatar, userStatus: user.status});
                })
            };

            $scope.init = function () {
                let id = $routeParams.id;
                $scope.friendList = AccountApi.getFriends();
                $scope.user = {};
                $scope.currentUser = current_user;
                $scope.session = {};
                $scope.sessionList = SessionApi.list();

                let connect = SocketService.connect();

                connect.then(function(){

                    SocketService.subscribe("/whatsapp/new-session/" + $scope.currentUser.id, function(data) {
                        $scope.sessionList.push(data);
                        $scope.$apply();
                    })

                    SocketService.subscribe("/whatsapp/list-friends/" + $scope.currentUser.id,  function(data) {
                        $scope.friendList.push(data);
                        $scope.$apply();
                    });
                    
                    $scope.friendList.forEach(element => {
                        SocketService.subscribe("/whatsapp/login/" + element.id, function(data) {
                            _.remove($scope.friendList, {id : data.id})
                            $scope.friendList.push(data);
                            $scope.$apply();
                        })
                    })

                    $scope.friendList.forEach(element => {
                        SocketService.subscribe("/whatsapp/logout/" + element.id, function(data) {
                            _.remove($scope.friendList, {id : data.id})
                            $scope.friendList.push(data);
                            $scope.$apply();
                        })
                    });  
                }) 
            };

            $scope.init();
        }
    });