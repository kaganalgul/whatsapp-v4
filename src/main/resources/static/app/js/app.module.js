var app = angular.module("w-app",
    [
        'ngRoute',
        'ngResource',
        'ui.bootstrap',
        'ngFileUpload',
        'timeago'
    ]);

    app.config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);

        $routeProvider
            .when('/login', {
                template: '<login></login>'
            })
            .when('/', {
                template: '<login></login>'
            })
            .when('/register', {
                template: '<register></register>'
            })
            .when('/home', {
                template: '<home></home>'
            })
            .when('/session', {
                template: '<session></session>'
            })
            .when('/open-exist-session', {
                template: '<open-exist-session></open-exist-session>'
            })
    })

    app.factory('AccountApi', ['$resource', function($resource) {
            var baseUrl = '/user';

            return $resource('/number', { number: '@number' }, {
                login: {
                    method: 'POST',
                    url: baseUrl + '/login'
                },
                register: {
                    method: 'POST',
                    url: baseUrl + "/register",
                    transformRequest: function (data) {
                        let user = data.user;
                        let file = data.file;
        
                        var fd = new FormData();
                        angular.forEach(user, function (value, key) {
                            if (value instanceof FileList) {
                                if (value.length == 1) {
                                    fd.append(key, value[0]);
                                } else {
                                    angular.forEach(value, function (file, index) {
                                        fd.append(key + '_' + index, file);
                                    });
                                }
                            } else {
                                fd.append(key, value);
                            }
                        });
        
                        fd.append("file", file);
        
                        return fd;
                    },
                    headers: {"Content-Type": undefined}
                },
                getFriends: {
                    method: 'GET',
                    url: baseUrl + '/list-friends',
                    isArray: true
                },
                addFriend: {
                    method: 'POST',
                    url: baseUrl + '/add-friend'
                }
            })
        }])

    app.factory('MessageApi', ['$resource', function($resource) {
                var baseUrl = '/message';

                return $resource('/id', { id : '@id' }, {
                    list: {
                        method: 'GET',
                        url: baseUrl + '/list/:id',
                        isArray: true
                    },
                    send: {
                        method: 'POST',
                        url: baseUrl + '/send',
                    }
                })
            }])

    app.factory('SessionApi', ['$resource', function($resource) {
                var baseUrl = '/session';

                return $resource('/', { }, {
                    list: {
                        method: 'GET',
                        url: baseUrl + '/list-sessions',
                        isArray: true
                    },
                    newSession: {
                        method: 'POST',
                        url: baseUrl + '/new-session'
                    },
                    openSession: {
                        method: 'POST',
                        url: baseUrl + '/open-exist-session'
                    }
                })
            }])
            
    app.factory("SocketService", function ($q) {

        let connected = false;

        function connect() {
            let deferred = $q.defer();

            let socket = new SockJS('/whatsapp-app');
            stompClient = Stomp.over(socket);
            //stompClient.debug = null;
            stompClient.connect({}, function (frame) {
                connected = true;
                deferred.resolve();
            });

            return deferred.promise;
        }

        function subscribe(destination, cb) {

            let deferred = $q.defer();

            if (connected) {
                deferred.resolve();
            } else {
                let promise = connect();
                promise.then(function () {
                    deferred.resolve();
                });
            }

            deferred.promise.then(function () {
                stompClient.subscribe(destination, function (message) {
                    let data = JSON.parse(message.body);
                    cb(data);
                });
            });

        }

        function unsubscribe(destination) {
            // TBD
        }

        function disconnect() {
            if(stompClient != null) {
                stompClient.disconnect();
            }
            connected = false;
            console.log("Disconnected");
        }

        return {
            connect: connect,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            disconnect: disconnect
        }
    });