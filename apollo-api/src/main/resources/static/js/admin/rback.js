var app = angular.module('RBackAdminApp', ['ui.bootstrap']);
app.controller('RBackAdminController', ['$scope', '$http', function ($scope, $http) {

        $scope.rbacRoles = [];
        $scope.rbacAuthorities = [];
        $scope.activeStatus = 1
        $scope.selectedRole = -1;

        $scope.tab = 7;

        $scope.setTab = function (newTab) {
            $scope.tab = newTab;
        };


        $scope.currentPage = 1;
        $scope.itemsPerPage = 10;
        $scope.maxSize = 5;
        $scope.noOfPages = $scope.users.length / $scope.itemsPerPage;

        $scope.revokeSelectedAuthority = (roleId, authorityId) => {
            _revokeAuthority(roleId, authorityId);

        };

        $scope.refreshRolesList = () => {
            _refreshActiveRolesData($scope.activeStatus);
        };

        $scope.refreshRolesList();

        $scope.filteredRbacRoles = [];

        $scope.filterRoleBasedRoles = function (criteria) {
            // if (criteria == 1) {
            $scope.filteredRbacRoles = $filter('filter')($scope.rbacRoles, { userRole: { id: criteria } });
            // } else {
            //     $scope.filteredTicket = $filter('filter')($scope.tickets, function(value){
            //       console.log(value);
            //       return value.assignee === null || value.assignee.id === 'ak';
            //     });
            //     console.log($scope.filteredTicket);
            // }


        };

        $scope.loadSelectedRoleAuthorities = (roleId) => {
            _refreshActiveAuthoritiesData(roleId, $scope.activeStatus);
        };

        _refreshActiveRolesData($scope.activeStatus);


        $scope.$watch('currentPage', function () {
            refreshPageData();
        });

        function refreshPageData() {

            var begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            var end = begin + $scope.itemsPerPage;

            $scope.paged = {

                rbacRoles: $scope.rbacRoles.slice(begin, end),
                authorities: $scope.authorities.slice(begin, end)
            }
        }

        function _revokeAuthority(roleId, authorityId) {
            var method = "POST";
            var url = "/api/system/admin/security/titles/" + roleId + "/" + authorityId;

            $http({
                method: method,
                url: url,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            }).then(_success, _error);
        }

        function _refreshActiveRolesData(status) {
            $http({
                method: 'GET',
                url: "/api/system/admin/security/rbac/fetch/" + status
            }).then(
                function (res) { // success
                    // console.log("Error: " + url + " : " + data);
                    $scope.rbacRoles = res.data;
                    refreshPageData();

                },
                function (res) { // error
                    alert("Error: " + res.status + ":" + res.data + ":" + res.header);
                    console.log("Error: " + res.status + " : " + res.data);
                }
            )
        }

        function _refreshActiveAuthoritiesData(roleId, status) {
            $http({
                method: 'GET',
                url: "/api/system/admin/security/authorities/load/" + roleId + "/" + status
            }).then(
                function (res) { // success
                    // console.log("Error: " + url + " : " + data);
                    $scope.authorities = res.data;
                    refreshPageData();

                },
                function (res) { // error

                    console.log("Error: " + res.status + " : " + res.data);
                    alert("Error: " + res.status + ":" + res.data + ":" + res.header);
                }
            )
        }
    }]);