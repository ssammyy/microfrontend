const app = angular.module('UserAdminApp', ['ui.bootstrap', 'ngRoute']);
app.value('tab', 0);
app.value('activeStatus', 1);
app.value('currentPage', 1);
app.value('itemsPerPage', 10);
app.value('maxSize', 5);
app.value('authoritiesUrl', '/api/v2/system/admin/security/authorities/load');
app
    .factory('dataFactory', ['$http', '$log', function ($http, $log) {

        const urlBase = '/api/v2/system/admin/security/';
        const masterDataBaseUrl = '/api/v2/system/admin/masters/';
        const authoritiesUri = urlBase + "authorities/loads";
        const allDesignationsUri = masterDataBaseUrl + "designations/load";
        const allRegionCountyTownsDesignationsUri = masterDataBaseUrl + "regionCountyTown/load";
        const allRegionSubRegionUri = masterDataBaseUrl + "regionSubRegion/load";
        const allDirectorateDesignationsUri = masterDataBaseUrl + "directorateDesignations/load";
        const allDirectorateToSubSectionL2Uri = masterDataBaseUrl + "directorateToSubSectionL2/load";
        const activeDesignationsUri = masterDataBaseUrl + "designations/loads";
        const allDepartmentsUri = masterDataBaseUrl + "departments/load";
        const activeDepartmentsUri = masterDataBaseUrl + "departments/loads";
        const allDivisionsUri = masterDataBaseUrl + "divisions/load";
        const activeDivisionsUri = masterDataBaseUrl + "divisions/loads";
        const allDirectorateUri = masterDataBaseUrl + "directorate/load";
        const activeDirectorateUri = masterDataBaseUrl + "directorate/loads";
        const allRegionsUri = masterDataBaseUrl + "regions/load";
        const activeRegionsUri = masterDataBaseUrl + "regions/loads";
        const allSectionsUri = masterDataBaseUrl + "sections/load";
        const activeSectionsUri = masterDataBaseUrl + "sections/loads";
        const allSubSectionsL1Uri = masterDataBaseUrl + "subsections/l1/load";
        const activeSubSectionsL1Uri = masterDataBaseUrl + "subsections/l1/loads";
        const allSubSectionsL2Uri = masterDataBaseUrl + "subsections/l2/load";
        const activeSubSectionsL2Uri = masterDataBaseUrl + "subsections/l2/loads";
        const allSubRegionsUri = masterDataBaseUrl + "subRegions/load";
        const activeSubRegionsUri = masterDataBaseUrl + "subRegions/loads";
        const allCountiesUri = masterDataBaseUrl + "counties/load";
        const activeCountiesUri = masterDataBaseUrl + "counties/loads";
        const allTownsUri = masterDataBaseUrl + "towns/load";
        const activeTownsUri = masterDataBaseUrl + "towns/loads";
        const rolesUri = urlBase + "roles/loads";
        const allRolesUri = urlBase + "roles/load";
        const allAuthoritiesUri = urlBase + "authorities/load";
        const activeTitlesUri = urlBase + "titles/loads";
        const activeUserTypesUri = urlBase + "userTypes/loads";
        const allUserTypesUri = urlBase + "userTypes/load";
        const titlesUri = urlBase + "titles/load";
        const usersUri = urlBase + "users/load";
        const searchUsersUri = urlBase + "users/search";
        const dataFactory = {};

        dataFactory.activeStatus = 1
        dataFactory.currentPage = 1;
        dataFactory.itemsPerPage = 10;
        dataFactory.maxSize = 5;
        dataFactory.titles = [];


        dataFactory.getAuthorities = status => $http.get(authoritiesUri + "/" + status);
        dataFactory.getDesignationsByStatus = status => $http.get(activeDesignationsUri + "/" + status);
        dataFactory.getAllDesignations = () => $http.get(allDesignationsUri);
        dataFactory.getAllRegionCountyTownsDesignationsUri = () => $http.get(allRegionCountyTownsDesignationsUri);
        dataFactory.getAllRegionSubRegion = () => $http.get(allRegionSubRegionUri);
        dataFactory.getDirectorateDesignationsListing = () => $http.get(allDirectorateDesignationsUri);
        dataFactory.getDirectorateToSubSectionL2Listing = () => $http.get(allDirectorateToSubSectionL2Uri);
        dataFactory.getDepartmentsByStatus = status => $http.get(activeDepartmentsUri + "/" + status);
        dataFactory.getAllDepartments = () => $http.get(allDepartmentsUri);
        dataFactory.getDivisionsByStatus = status => $http.get(activeDivisionsUri + "/" + status);
        dataFactory.getAllDivisions = () => $http.get(allDivisionsUri);
        dataFactory.getDirectoratesByStatus = status => $http.get(activeDirectorateUri + "/" + status);
        dataFactory.getAllDirectorates = () => $http.get(allDirectorateUri);
        dataFactory.getRegionsByStatus = status => $http.get(activeRegionsUri + "/" + status);
        dataFactory.getAllRegions = () => $http.get(allRegionsUri);
        dataFactory.getSubRegionsByStatus = status => $http.get(activeSubRegionsUri + "/" + status);
        dataFactory.getAllSubRegions = () => $http.get(allSubRegionsUri);
        dataFactory.getSectionsByStatus = status => $http.get(activeSectionsUri + "/" + status);
        dataFactory.getAllSections = () => $http.get(allSectionsUri);
        dataFactory.getSubSectionsL1ByStatus = status => $http.get(activeSubSectionsL1Uri + "/" + status);
        dataFactory.getAllSubSectionsL1 = () => $http.get(allSubSectionsL1Uri);
        dataFactory.getSubSectionsL2ByStatus = status => $http.get(activeSubSectionsL2Uri + "/" + status);
        dataFactory.getAllSubSectionsL2 = () => $http.get(allSubSectionsL2Uri);
        dataFactory.getCountiesByStatus = status => $http.get(activeCountiesUri + "/" + status);
        dataFactory.getAllCounties = () => $http.get(allCountiesUri);
        dataFactory.getTownsByStatus = status => $http.get(activeTownsUri + "/" + status);
        dataFactory.getAllTowns = () => $http.get(allTownsUri);

        dataFactory.getActiveRoles = status => $http.get(rolesUri + "/" + status)
        dataFactory.getAllUsers = () => $http.get(usersUri)


        dataFactory.getAllRoles = () => $http.get(allRolesUri)

        dataFactory.getAllAuthorities = () => $http.get(allAuthoritiesUri)

        dataFactory.getActiveTitles = status => $http.get(activeTitlesUri + "/" + status)

        dataFactory.getActiveUserTypes = status => $http.get(activeUserTypesUri + "/" + status)

        dataFactory.getAllTitles = () => $http.get(titlesUri)
        dataFactory.getAllUserTypes = () => $http.get(allUserTypesUri)

        dataFactory.errorAlert = res => {
            const data = res.data;
            const status = res.status;
            alert("Error: " + status + ":" + data);
        }


        dataFactory.assignAuthority = (roleId, authorityId, status) => {
            const method = "POST";
            const uri = urlBase + "rbac/assign/" + roleId + "/" + authorityId + "/" + status;
            return $http({
                method: method,
                url: uri,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }

        dataFactory._revokeAuthority = (roleId, authorityId, status) => {
            const method = "POST";
            const url = "/api/v2/system/admin/security/rbac/revoke/" + roleId + "/" + authorityId + "/" + status;

            return $http({
                method: method,
                url: url,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }

        dataFactory._refreshActiveRolesData = status => $http({
            method: 'GET',
            url: "/api/v2/system/admin/security/rbac/fetch/roles/" + status
        });

        dataFactory._refreshActiveAuthoritiesData = (roleId, status) => $http({
            method: 'GET',
            url: "/api/v2/system/admin/security/rbac/fetch/authorities/" + roleId + "/" + status
        });

        dataFactory.loadActiveRbacUsers = status => $http({
            method: 'GET',
            url: "/api/v2/system/admin/security/rbac/fetch/users/" + status
        })

        dataFactory.loadActiveRbacUserRoles = (userId, status) => $http({
            method: 'GET',
            url: "/api/v2/system/admin/security/rbac/fetch/user-roles/" + userId + "/" + status
        })

        dataFactory.assignRole = (userId, roleId, status) => {
            const method = "POST";
            const uri = urlBase + "rbac/role/assign/" + userId + "/" + roleId + "/" + status;
            return $http({
                method: method,
                url: uri,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }

        dataFactory.revokeRole = (userId, roleId, status) => {
            const method = "POST";
            const uri = urlBase + "rbac/role/revoke/" + userId + "/" + roleId + "/" + status;
            return $http({
                method: method,
                url: uri,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }

        dataFactory.submitUserTypeData = userTypeForm => {

            let method;
            let url = "/api/v2/system/admin/security/userTypes/";

            if (userTypeForm.id === -1) {
                method = "POST";

            } else {
                method = "PUT";


            }

            return $http({
                method: method,
                url: url,
                data: angular.toJson(userTypeForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        };

        dataFactory.submitTitleData = titleForm => {

            let method;
            let url = "/api/v2/system/admin/security/titles/";

            if (titleForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }

            return $http({
                method: method,
                url: url,
                data: angular.toJson(titleForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        };
        dataFactory.submitAuthorityData = authorityForm => {
            $log.log("Error " + authorityForm?.id)

            let method;
            let url = '/api/v2/system/admin/security/authorities/';

            if (authorityForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            $log.log("Error " + authorityForm?.name + " method: " + method)

            return $http({
                method: method,
                url: url,
                data: angular.toJson(authorityForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        };

        dataFactory.submitRoleData = roleForm => {
            $log.log("Issues :" + roleForm?.id)
            let method;
            let url;
            if (roleForm.id === -1) {
                method = "POST";
                url = "/api/v2/system/admin/security/roles/";
            } else {
                method = "PUT";
                url = "/api/v2/system/admin/security/roles/";
            }
            $log.log("Issues :" + roleForm?.roleName + " method:" + method)

            return $http({
                method: method,
                url: url,
                data: angular.toJson(roleForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });

        }

        dataFactory.filterUsers = searchForm => {
            let method = "POST"
            return $http({
                method: method,
                url: searchUsersUri,
                data: angular.toJson(searchForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            })
        }
        dataFactory.submitUserData = userForm => {

            let method;
            let url;

            if (userForm.id === -1) {
                method = "POST";
                url = "/api/v2/system/admin/security/users/";
            } else {
                method = "PUT";

                url = "/api/v2/system/admin/security/users/";
            }

            return $http({
                method: method,
                url: url,
                data: angular.toJson(userForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            })

        }
        dataFactory.submitDesignationsData = designationForm => {
            let method;
            let url = "/api/v2/system/admin/masters/designations/";
            if (designationForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(designationForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitDepartmentsData = departmentsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/departments/";
            if (departmentsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(departmentsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitDivisionsData = divisionsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/divisions/";
            if (divisionsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(divisionsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitDirectorateData = directorateForm => {
            let method;
            let url = "/api/v2/system/admin/masters/directorate/";
            if (directorateForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(directorateForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitRegionsData = regionsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/regions/";
            if (regionsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(regionsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitSubRegionsData = subRegionsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/subRegions/";
            if (subRegionsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(subRegionsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitSectionsData = sectionsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/sections/";
            if (sectionsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(sectionsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitSubSectionsL1Data = subSectionsL1Form => {
            let method;
            let url = "/api/v2/system/admin/masters/subsections/l1/";
            if (subSectionsL1Form.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(subSectionsL1Form),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitSubSectionsL2Data = subSectionsL2Form => {
            let method;
            let url = "/api/v2/system/admin/masters/subsections/l2/";
            if (subSectionsL2Form.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(subSectionsL2Form),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitCountiesData = countiesForm => {
            let method;
            let url = "/api/v2/system/admin/masters/counties";
            if (countiesForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(countiesForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }
        dataFactory.submitTownsData = townsForm => {
            let method;
            let url = "/api/v2/system/admin/masters/towns";
            if (townsForm.id === -1) {
                method = "POST";
            } else {
                method = "PUT";
            }
            return $http({
                method: method,
                url: url,
                data: angular.toJson(townsForm),
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                withCredentials: true
            });
        }


        return dataFactory;
    }])
    .controller('RBackAdminController', ['$rootScope', '$scope', '$http', '$filter', '$log', 'dataFactory', function ($rootScope, $scope, $http, $filter, $log, dataFactory) {


        $scope.message = "";

        $scope.rbacRoles = [];
        $scope.rbacAuthorities = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.authorities = [];
        $scope.selectedRole = -1;
        $scope.selectedAuthority = -1;


        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.max;
        $scope.noOfPages = $scope.rbacRoles.length / $scope.itemsPerPage;

        initializeUIData();

        function initializeUIData() {
            dataFactory.getAuthorities($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.authorities = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );

        }


        $scope.revokeSelectedAuthority = (roleId, authorityId, status) => {
            $scope.selectedAuthority = authorityId;
            dataFactory
                ._revokeAuthority(roleId, authorityId, status)
                .then(
                    function () {
                        $scope.refreshAuthorities();
                        $scope.message = "Authority Assign Success";
                    }, function (error) {
                        dataFactory.errorAlert(error)
                    }
                );

        };


        $scope.assignSelectedAuthority = (roleId, authorityId, status) => {
            if (authorityId <= 0 || roleId <= 0) {
                alert("Error: Invalid Authorization=" + authorityId + " /Role=" + roleId + " combination selected,  Try again");

            } else {
                dataFactory
                    .assignAuthority(roleId, authorityId, status)
                    .then(
                        function () {
                            $scope.refreshAuthorities();
                            $scope.message = "Authority Assign Success";
                        }, function (error) {
                            dataFactory.errorAlert(error)
                        }
                    );

            }

        };

        $scope.refreshRolesList = () => {
            dataFactory._refreshActiveRolesData($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.rbacRoles = res.data;
                        $scope.refreshAuthorities();
                    },
                    function (error) {
                        dataFactory.errorAlert(error)
                    }
                );
        };

        // $scope.refreshRolesList();

        $scope.refreshAuthorities = () => {
            dataFactory._refreshActiveAuthoritiesData($scope.selectedRole, $scope.activeStatus)
                .then(
                    function (res) {
                        $scope.rbacAuthorities = res.data;
                        initializeUIData();
                        refreshRbacRolesPageData();
                    },
                    function (res) {
                        dataFactory.errorAlert(res);
                    }
                );

        };
        $scope.loadSelectedRoleAuthorities = (roleId) => {
            $scope.selectedRole = roleId;
            $scope.refreshAuthorities();
        };


        $scope.$watch('currentPage', function () {
            refreshRbacRolesPageData();
        });

        function refreshRbacRolesPageData() {

            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;

            $scope.paged = {
                rbacRoles: $scope.rbacRoles.slice(begin, end)
                // ,
                // rbacAuthorities: $scope.rbacAuthorities.slice(begin, end)
            }
        }


    }])
    .controller('RbacUserRolesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.rbacUsers = [];
        $scope.rbacUserRoles = [];
        $scope.rbacActiveRoles = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.selectedUserRole = -1;
        $scope.selectedUser = -1;
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.rbacUsers.length / $scope.itemsPerPage;

        initializeUIData();


        function initializeUIData() {
            dataFactory.getActiveRoles($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.rbacActiveRoles = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );

        }

        $scope.$watch('currentPage', function () {
            refreshUsersTable();
        });

        function refreshUsersTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                rbacUsers: $scope.rbacUsers.slice(begin, end)
            }
        }

        $scope.loadInitialData = function () {
            $log.log("loadInitialData S:" + $scope.activeStatus);
            dataFactory.loadActiveRbacUsers($scope.activeStatus)
                .then(function (res) {
                        $scope.rbacUsers = res.data;
                        refreshUsersTable();
                        initializeUIData();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;


        }


        $scope.loadRolesForSelectedUser = (userId) => {
            $scope.selectedUser = userId;
            dataFactory.loadActiveRbacUserRoles(userId, $scope.activeStatus)
                .then(
                    function (res) {
                        $scope.rbacUserRoles = res.data;
                        $log.log(userId + ":" + $scope.rbacUserRoles.length);
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
        }

        $scope.assignSelectedRole = (roleId, status) => {

            if (roleId <= 0 || $scope.selectedUser <= 0) {

                alert("Error: Invalid Role=" + roleId + " User=" + $scope.selectedUser + " combination selected,  Try again");

            } else {
                dataFactory.assignRole($scope.selectedUser, roleId, status)
                    .then(
                        function () {
                            $scope.loadRolesForSelectedUser($scope.selectedUser);
                        },
                        function (error) {
                            dataFactory.errorAlert(error)
                        }
                    );

            }

        }

        $scope.revokeSelectedRole = (roleId, status) => {
            dataFactory.revokeRole($scope.selectedUser, roleId, status)
                .then(
                    function () {
                        $scope.loadRolesForSelectedUser($scope.selectedUser);
                    },
                    function (error) {
                        dataFactory.errorAlert(error)
                    }
                )
        }


    }])

    .controller('SysAdminRolesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.roles = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.roles.length / $scope.itemsPerPage;

        $scope.roleForm = {
            id: -1,
            roleName: "",
            descriptions: "",
            status: 0
        };

        $scope.$watch('currentPage', function () {
            refreshRolesTable();
        });

        function refreshRolesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                roles: $scope.roles.slice(begin, end)
            }
        }

        $scope.loadInitialData = function () {

            dataFactory.getAllRoles()
                .then(function (res) {
                        $scope.roles = res.data;
                        refreshRolesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;

        }

        $scope.editRole = (role) => {
            $scope.roleForm.id = role.id;
            $scope.roleForm.roleName = role.roleName;
            $scope.roleForm.descriptions = role.descriptions;
            $scope.roleForm.status = role.status;
        };

        function _success() {
            resetForms();
        }

        $scope.submitRole = () => {


            dataFactory.submitRoleData($scope.roleForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.roleForm = {
                id: -1,
                roleName: "",
                descriptions: "",
                status: 0
            };
        }
    }])
    .controller('SysAdminAuthoritiesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.authorities = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.authorities.length / $scope.itemsPerPage;

        $scope.authorityForm = {
            id: -1,
            name: "",
            descriptions: "",
            status: 0
        };


        $scope.$watch('currentPage', function () {
            refreshAuthoritiesTable();
        });

        function refreshAuthoritiesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                authorities: $scope.authorities.slice(begin, end)
            }
        }

        $scope.loadInitialData = function () {

            dataFactory.getAllAuthorities()
                .then(function (res) {
                        $scope.authorities = res.data;
                        refreshAuthoritiesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;

        }

        $scope.editAuthority = (authority) => {
            $scope.authorityForm.id = authority.id;
            $scope.authorityForm.name = authority.name;
            $scope.authorityForm.descriptions = authority.descriptions;
            $scope.authorityForm.status = authority.status;
        };

        function _success() {
            resetForms();
        }

        $scope.submitRole = () => {
            dataFactory.submitAuthorityData($scope.authorityForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.authorityForm = {
                id: -1,
                name: "",
                descriptions: "",
                status: 0
            };

        }
    }])
    .controller('SysAdminTitlesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.titles = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.titles.length / $scope.itemsPerPage;

        $scope.titleForm = {
            id: -1,
            title: "",
            remarks: "",
            status: 0
        };


        $scope.$watch('currentPage', function () {
            refreshAuthoritiesTable();
        });

        function refreshAuthoritiesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                titles: $scope.titles.slice(begin, end)
            }
        }

        $scope.loadInitialData = function () {

            dataFactory.getAllTitles()
                .then(function (res) {
                        $scope.titles = res.data;
                        refreshAuthoritiesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;

        }

        $scope.editTitle = (title) => {
            $scope.titleForm.id = title.id;
            $scope.titleForm.title = title.title;
            $scope.titleForm.remarks = title.remarks;
            $scope.titleForm.status = title.status;
        };


        function _success() {
            resetForms();
        }

        $scope.submitTitle = () => {
            dataFactory.submitTitleData($scope.titleForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.titleForm = {
                id: -1,
                title: "",
                remarks: "",
                status: 0
            };

        }
    }])
    .controller('SysAdminUserTypesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.userTypes = [];
        $scope.activeRoles = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.userTypes.length / $scope.itemsPerPage;

        $scope.userTypeForm = {
            id: -1,
            typeName: "",
            descriptions: "",
            status: 0,
            defaultRoleId: 0
        };

        loadData();


        $scope.$watch('currentPage', function () {
            refreshUserTypesTable();
        });

        function refreshUserTypesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                userTypes: $scope.userTypes.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllUserTypes()
                .then(function (res) {
                        $scope.userTypes = res.data;
                        refreshUserTypesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
            dataFactory.getActiveRoles($scope.activeStatus)
                .then(function (res) {
                        $scope.userTypes = res.data;
                        refreshUserTypesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
        }

        $scope.loadInitialData = function () {
            loadData();

        }

        $scope.editUserType = (userType) => {
            $scope.userTypeForm.id = userType.id;
            $scope.userTypeForm.typeName = userType.typeName;
            $scope.userTypeForm.descriptions = userType.descriptions;
            $scope.userTypeForm.status = userType.status;
            $scope.userTypeForm.defaultRoleId = userType.defaultRoleId;
        };


        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitUserType = () => {
            dataFactory.submitUserTypeData($scope.userTypeForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.userTypeForm = {
                id: -1,
                typeName: "",
                descriptions: "",
                status: 0,
                defaultRoleId: 0
            };

        }
    }])
    .controller('MasterDataTownsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.towns = [];
        $scope.activeCounties = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.towns.length / $scope.itemsPerPage;

        $scope.townsForm = {
            id: -1,
            town: "",
            countyId: 0,
            status: 0
        };
        loadData();

        $scope.$watch('currentPage', function () {
            refreshTownsTable();
        });

        function refreshTownsTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                towns: $scope.towns.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllTowns()
                .then(function (res) {
                        $scope.towns = res.data;
                        refreshTownsTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );

            dataFactory.getCountiesByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeCounties = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
        }

        $scope.loadInitialData = function () {
            loadData();
        }

        $scope.editTowns = (town) => {
            $scope.townsForm.id = town.id;
            $scope.townsForm.town = town.town;
            $scope.townsForm.countyId = town.countyId;
            $scope.townsForm.status = town.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitTown = () => {
            dataFactory.submitTownsData($scope.townsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {
            $scope.townsForm = {
                id: -1,
                town: "",
                countyId: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataCountiesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.counties = [];
        $scope.activeRegions = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.counties.length / $scope.itemsPerPage;

        $scope.countiesForm = {
            id: -1,
            county: "",
            regionId: 0,
            status: 0
        };

        loadData();

        $scope.$watch('currentPage', function () {
            refreshCountiesTable();

        });

        function refreshCountiesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                counties: $scope.counties.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllCounties()
                .then(function (res) {
                        $scope.counties = res.data;
                        refreshCountiesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getRegionsByStatus($scope.activeStatus)
                .then(function (res) {
                        $scope.activeRegions = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editCounties = (county) => {
            $scope.countiesForm.id = county.id;
            $scope.countiesForm.county = county.county;
            $scope.countiesForm.regionId = county.regionId;
            $scope.countiesForm.status = county.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitCounty = () => {
            dataFactory.submitCountiesData($scope.countiesForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {
            $scope.countyForm = {
                id: -1,
                county: "",
                regionId: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataSubsectionsL1Controller', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.subsectionsL1s = [];
        $scope.activeSections = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.subsectionsL1s.length / $scope.itemsPerPage;

        $scope.subsectionsL1Form = {
            id: -1,
            subSection: "",
            sectionId: 0,
            status: 0
        };
        loadData();

        $scope.$watch('currentPage', function () {
            refreshSubSectionTable();

        });

        function refreshSubSectionTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                subsectionsL1s: $scope.subsectionsL1s.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllSubSectionsL1()
                .then(function (res) {
                        $scope.subsectionsL1s = res.data;
                        refreshSubSectionTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
            dataFactory.getSectionsByStatus($scope.activeStatus)
                .then(function (res) {
                        $scope.activeSections = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editSubSectionL1 = (subSectionL1) => {
            $scope.subsectionsL1Form.id = subSectionL1.id;
            $scope.subsectionsL1Form.subSection = subSectionL1.subSection;
            $scope.subsectionsL1Form.sectionId = subSectionL1.sectionId;
            $scope.subsectionsL1Form.status = subSectionL1.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitSubSectionL1 = () => {
            dataFactory.submitSubSectionsL1Data($scope.subsectionsL1Form).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.subsectionsL1Form = {
                id: -1,
                subSection: "",
                sectionId: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataSubsectionsL2Controller', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.subsectionsL2s = [];
        $scope.activeSubsectionsL1s = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.subsectionsL2s.length / $scope.itemsPerPage;

        $scope.subsectionsL2Form = {
            id: -1,
            subSection: "",
            subSectionL1Id: 0,
            status: 0
        };

        loadData();

        $scope.$watch('currentPage', function () {
            refreshSubSectionTable();

        });

        function refreshSubSectionTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                subsectionsL2s: $scope.subsectionsL2s.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllSubSectionsL2()
                .then(function (res) {
                        $scope.subsectionsL2s = res.data;
                        refreshSubSectionTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
            dataFactory.getSubSectionsL1ByStatus($scope.activeStatus)
                .then(function (res) {
                        $scope.activeSubsectionsL1s = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editSubSectionL2 = (subSectionL2) => {
            $scope.subsectionsL2Form.id = subSectionL2.id;
            $scope.subsectionsL2Form.subSection = subSectionL2.subSection;
            $scope.subsectionsL2Form.subSectionL1Id = subSectionL2.subSectionL1Id;
            $scope.subsectionsL2Form.status = subSectionL2.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitSubSectionL2 = () => {
            dataFactory.submitSubSectionsL2Data($scope.subsectionsL2Form).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.subsectionsL2Form = {
                id: -1,
                subSection: "",
                subSectionL1Id: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataSectionsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.sections = [];
        $scope.activeDivisions = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.sections.length / $scope.itemsPerPage;

        $scope.sectionsForm = {
            id: -1,
            section: "",
            divisionId: 0,
            status: 0
        };

        loadData();

        $scope.$watch('currentPage', function () {
            refreshSectionsTable();

        });

        function refreshSectionsTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                sections: $scope.sections.slice(begin, end)
            }
        }

        function loadData() {

            dataFactory.getAllSections()
                .then(function (res) {
                        $scope.sections = res.data;
                        refreshSectionsTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDivisionsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeDivisions = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
        }

        $scope.loadInitialData = function () {
            loadData();
        }

        $scope.editSections = (section) => {
            $scope.sectionsForm.id = section.id;
            $scope.sectionsForm.section = section.section;
            $scope.sectionsForm.divisionId = section.divisionId;
            $scope.sectionsForm.status = section.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitSection = () => {
            dataFactory.submitSectionsData($scope.sectionsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.sectionsForm = {
                id: -1,
                section: "",
                divisionId: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataSubRegionsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.subRegions = [];
        $scope.activeRegions = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.subRegions.length / $scope.itemsPerPage;

        $scope.subRegionsForm = {
            id: -1,
            subRegion: "",
            regionId: 0,
            status: 0
        };

        loadData();

        $scope.$watch('currentPage', function () {
            refreshSubRegionTable();

        });

        function refreshSubRegionTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                subRegions: $scope.subRegions.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllSubRegions()
                .then(res => {
                        $scope.subRegions = res.data;
                        refreshSubRegionTable();
                    }, error => {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getRegionsByStatus($scope.activeStatus)
                .then(
                    res => {
                        $scope.activeRegions = res.data
                    },
                    error => {
                        dataFactory.errorAlert(error)
                    }
                );
        }

        $scope.loadInitialData = () => {
            loadData();

        }

        $scope.editSubRegion = (subRegion) => {
            $scope.subRegionsForm.id = subRegion.id;
            $scope.subRegionsForm.subRegion = subRegion.subRegion;
            $scope.subRegionsForm.regionId = subRegion.regionId;
            $scope.subRegionsForm.status = subRegion.status;
        };

        function _success() {
            resetForms();
            loadData();
        }

        $scope.submitSubRegion = () => {
            dataFactory.submitSubRegionsData($scope.subRegionsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.subRegionForm = {
                id: -1,
                subRegion: "",
                regionId: 0,
                status: 0
            };
        }
    }])
    .controller('MasterDataRegionsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.regions = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.regions.length / $scope.itemsPerPage;

        $scope.regionsForm = {
            id: -1,
            region: "",
            status: 0
        };
        loadData();

        $scope.$watch('currentPage', function () {
            refreshRegionTable();

        });

        function refreshRegionTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                regions: $scope.regions.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllRegions()
                .then(function (res) {
                        $scope.regions = res.data;
                        refreshRegionTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editRegion = (regions) => {
            $scope.regionsForm.id = regions.id;
            $scope.regionsForm.region = regions.region;
            $scope.regionsForm.status = regions.status;
            $scope.regionsForm.descriptions = regions.descriptions;
        };

        function _success() {
            loadData();
            resetForms();
        }

        $scope.submitRegion = () => {
            dataFactory.submitRegionsData($scope.regionsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.regionsForm = {
                id: -1,
                region: "",
                status: 0
            };
        }
    }])
    .controller('MasterDataDirectoratesController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.directorates = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.directorates.length / $scope.itemsPerPage;

        $scope.directoratesForm = {
            id: -1,
            directorate: "",
            status: false
        };
        loadData();

        $scope.$watch('currentPage', function () {
            refreshDirectoratesTable();

        });

        function refreshDirectoratesTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                directorates: $scope.directorates.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllDirectorates()
                .then(function (res) {
                        $scope.directorates = res.data;
                        refreshDirectoratesTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;

        }

        $scope.loadInitialData = function () {
            loadData();
        }

        $scope.editDirectorate = (directorate) => {
            $scope.directoratesForm.id = directorate.id;
            $scope.directoratesForm.directorate = directorate.directorate;
            $scope.directoratesForm.status = directorate.status;
        };

        function _success() {
            resetForms();
        }

        $scope.submitDirectorate = () => {
            dataFactory.submitDirectorateData($scope.directoratesForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.directoratesForm = {
                id: -1,
                directorate: "",
                status: 0
            };
        }
    }])
    .controller('MasterDataDivisionsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.divisions = [];
        $scope.activeDirectorates = [];
        $scope.selectedDirectorate = -1;
        $scope.activeDepartments = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.divisions.length / $scope.itemsPerPage;

        $scope.divisionsForm = {
            id: -1,
            division: "",
            status: 0,
            departmentId: -1
        };
        loadData();

        $scope.$watch('currentPage', function () {
            refreshDivisionsTable();

        });

        function refreshDivisionsTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                divisions: $scope.divisions.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllDivisions()
                .then(function (res) {
                        $scope.divisions = res.data;
                        refreshDivisionsTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDirectoratesByStatus($scope.activeStatus)
                .then(function (res) {
                    $scope.activeDirectorates = res.data;
                }, function (error) {
                    dataFactory.errorAlert(error)
                });
            dataFactory.getDepartmentsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeDepartments = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error)
                    }
                );
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editDivision = (division) => {
            $scope.divisionsForm.id = division.id;
            $scope.divisionsForm.division = division.division;
            $scope.divisionsForm.departmentId = division.departmentId;
            $scope.divisionsForm.directorateId = division.directorateId;
            $scope.divisionsForm.status = division.status;
        };

        function _success() {
            resetForms();
        }

        $scope.submitDivisions = () => {
            dataFactory.submitDivisionsData($scope.divisionsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.divisionsForm = {
                id: -1,
                division: "",
                status: 0,
                departmentId: -1
            };
        }
    }])
    .controller('MasterDataDepartmentsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.departments = [];
        $scope.activeDirectorates = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.departments.length / $scope.itemsPerPage;

        $scope.departmentsForm = {
            id: -1,
            department: "",
            directorateId: -1,
            status: false
        };

        loadData();
        $scope.$watch('currentPage', function () {
            refreshDepartmentsTable();

        });

        function refreshDepartmentsTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                departments: $scope.departments.slice(begin, end)
            }
        }

        function loadData() {
            dataFactory.getAllDepartments()
                .then(function (res) {
                        $scope.departments = res.data;
                        refreshDepartmentsTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );

            dataFactory.getDirectoratesByStatus($scope.activeStatus)
                .then(function (res) {
                    $scope.activeDirectorates = res.data;
                }, function (error) {
                    dataFactory.errorAlert(error);
                });
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editDepartment = (department) => {
            $scope.departmentsForm.id = department.id;
            $scope.departmentsForm.department = department.department;
            $scope.departmentsForm.directorateId = department.directorateId;
            $scope.departmentsForm.status = department.status;
        };

        function _success() {
            resetForms();
        }

        $scope.submitDepartments = () => {
            dataFactory.submitDepartmentsData($scope.departmentsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.departmentsForm = {
                id: -1,
                department: "",
                directorateId: -1,
                status: 0
            };
        }
    }])
    .controller('MasterDataDesignationsController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.designations = [];
        $scope.activeDirectorates = [];
        $scope.activeStatus = dataFactory.activeStatus
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.designations.length / $scope.itemsPerPage;


        $scope.designationsForm = {
            id: -1,
            designationName: "",
            descriptions: "",
            status: 0,
            directorateId: null
        };

        loadData();


        $scope.$watch('currentPage', function () {
            refreshDesignationsTable();

        });

        function refreshDesignationsTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                designations: $scope.designations.slice(begin, end)
            }
        }


        function loadData() {
            dataFactory.getAllDesignations()
                .then(function (res) {
                        $scope.designations = res.data;
                        refreshDesignationsTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
            dataFactory.getDirectoratesByStatus($scope.activeStatus)
                .then(function (res) {
                    $scope.activeDirectorates = res.data;
                }, function (error) {
                    dataFactory.errorAlert(error);
                })
        }

        $scope.loadInitialData = function () {

            loadData();

        }

        $scope.editDesignation = (designation) => {
            $scope.designationsForm.id = designation.id;
            $scope.designationsForm.designationName = designation.designationName;
            $scope.designationsForm.descriptions = designation.descriptions;
            $scope.designationsForm.status = designation.status;
            $scope.designationsForm.directorateId = designation.directorateId;
        };

        function _success() {
            resetForms();
        }

        $scope.submitDesignations = () => {
            dataFactory.submitDesignationsData($scope.designationsForm).then(_success, dataFactory.errorAlert);
        }

        function resetForms() {

            $scope.designationsForm = {
                id: -1,
                designationName: "",
                descriptions: "",
                status: 0,
                directorateId: null
            };
        }
    }])
    .controller("ErrorController", ['$scope', function ($scope) {
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }])
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('responseObserver');
    })
    .config(function ($routeProvider) {
        let adminBaseUrl = "/api/v2/system/admin";
        let mastersBaseUrl = adminBaseUrl + "/masters";


            $routeProvider
                .when("/rbac-user-roles", {
                    templateUrl: adminBaseUrl + "/ui/rbac-user-roles",
                    controller: "RbacUserRolesController"
                })
                .when("/rbac-role-authorities", {
                    templateUrl: adminBaseUrl + "/ui/rbac-role-authorities",
                    controller: "RBackAdminController"
                })
                .when("/crud-users", {
                    templateUrl: adminBaseUrl + "/ui/users-crud",
                    controller: "SysAdminUsersController"
                })
                .when("/crud-roles", {
                    templateUrl: adminBaseUrl + "/ui/roles-crud",
                    controller: "SysAdminRolesController"
                })
                .when("/crud-authorities", {
                    templateUrl: adminBaseUrl + "/ui/authorities-crud",
                    controller: "SysAdminAuthoritiesController"
                })
                .when("/crud-titles", {
                    templateUrl: adminBaseUrl + "/ui/titles-crud",
                    controller: "SysAdminTitlesController"
                })
                .when("/crud-userTypes", {
                    templateUrl: adminBaseUrl + "/ui/userTypes-crud",
                    controller: "SysAdminUserTypesController"
                })
                .when("/crud-designations", {
                    templateUrl: mastersBaseUrl + "/ui/designations",
                    controller: "MasterDataDesignationsController"
                })
                .when("/crud-directorates", {
                    templateUrl: mastersBaseUrl + "/ui/directorates",
                    controller: "MasterDataDirectoratesController"
                })
                .when("/crud-departments", {
                    templateUrl: mastersBaseUrl + "/ui/departments",
                    controller: "MasterDataDepartmentsController"
                })
                .when("/crud-divisions", {
                    templateUrl: mastersBaseUrl + "/ui/divisions",
                    controller: "MasterDataDivisionsController"
                })
                .when("/crud-sections", {
                    templateUrl: mastersBaseUrl + "/ui/sections",
                    controller: "MasterDataSectionsController"
                })
                .when("/crud-sub-sections-l1", {
                    templateUrl: mastersBaseUrl + "/ui/sub-sections-l1",
                    controller: "MasterDataSubsectionsL1Controller"
                })
                .when("/crud-sub-sections-l2", {
                    templateUrl: mastersBaseUrl + "/ui/sub-sections-l2",
                    controller: "MasterDataSubsectionsL2Controller"
                })
                .when("/crud-regions", {
                    templateUrl: mastersBaseUrl + "/ui/regions",
                    controller: "MasterDataRegionsController"

                })
                .when("/crud-sub-regions", {
                    templateUrl: mastersBaseUrl + "/ui/sub-regions",
                    controller: "MasterDataSubRegionsController"

                })
                .when("/crud-counties", {
                    templateUrl: mastersBaseUrl + "/ui/counties",
                    controller: "MasterDataCountiesController"

                })
                .when("/crud-towns", {
                    templateUrl: mastersBaseUrl + "/ui/towns",
                    controller: "MasterDataTownsController"
                })
                .when("/error", {
                    templateUrl: "/js/error"
                })
                .when("/accessDenied", {
                    templateUrl: "/js/accessDenied"
                })
                .otherwise({
                    template: "<h1>Application Settings</h1><p>No pending tasks found</p>"
                });
        }
    )
    .factory('responseObserver', function responseObserver($q, $window) {
        return {
            'responseError': function (errorResponse) {
                switch (errorResponse.status) {
                    case 401:
                        $window.location = '/auth/login';
                        break;
                    case 403:
                        $window.location = '/js/accessDenied';
                        break;
                    case 500:
                        $window.location = '/js/error';
                        break;
                }
                return $q.reject(errorResponse);
            }
        };
    })
;