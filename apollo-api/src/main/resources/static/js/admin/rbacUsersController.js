app
    .controller('SysAdminUsersController', ['$scope', '$log', 'dataFactory', function ($scope, $log, dataFactory) {
        $scope.users = [];
        $scope.activeDirectorates = []
        $scope.departments = []
        $scope.divisions = []
        $scope.sections = []
        $scope.subsectionsL1s = []
        $scope.subsectionsL2s = []
        $scope.activeDesignations = []
        $scope.activeRegions = []
        $scope.subRegions = []
        $scope.activeStatus = dataFactory.activeStatus
        $scope.titles = [];
        $scope.userTypes = [];
        $scope.currentPage = dataFactory.currentPage;
        $scope.itemsPerPage = dataFactory.itemsPerPage;
        $scope.maxSize = dataFactory.maxSize;
        $scope.noOfPages = $scope.users.length / $scope.itemsPerPage;

        initializeUI();
        $scope.searchForm = {
            firstName: null,
            lastName: null,
            userName: null,
            email: null,
        }

        $scope.userForm = {
            id: -1,
            title: 0,
            firstName: "",
            lastName: "",
            userPinIdNumber: "",
            userName: "",
            email: "",
            enabled: 0,
            accountExpired: 0,
            accountLocked: 0,
            credentialsExpired: 0,
            status: 0,
            registrationDate: 0,
            userType: 0,
            directorate: null,
            department: null,
            division: null,
            section: null,
            l1SubSubSection: null,
            l2SubSubSection: null,
            designation: null,
            profileId: null,
            region: null,
            subRegion: null
        };

        $scope.$watch('currentPage', () => {
            refreshUsersTable();
        });

        function refreshUsersTable() {
            let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
            let end = begin + $scope.itemsPerPage;
            $scope.paged = {
                users: $scope.users.slice(begin, end)
            }
        }

        $scope.applyFilter = () => {
            dataFactory.filterUsers($scope.searchForm)
                .then(res => {
                    $scope.users = res.data;
                    refreshUsersTable();
                }, dataFactory.errorAlert);


        }

        function initializeUI() {

            dataFactory.getRegionsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeRegions = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getSubRegionsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.subRegions = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDesignationsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeDesignations = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDirectoratesByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.activeDirectorates = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDepartmentsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.departments = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getDivisionsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.divisions = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getSectionsByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.sections = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getSubSectionsL1ByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.subsectionsL1s = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getSubSectionsL2ByStatus($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.subsectionsL2s = res.data;
                    },
                    function (error) {
                        dataFactory.errorAlert(error);
                    }
                );
            dataFactory.getActiveTitles($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.titles = res.data;
                        return dataFactory.titles;

                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );

            dataFactory.getActiveUserTypes($scope.activeStatus)
                .then(
                    function (res) {
                        $scope.userTypes = res.data;
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                );


            dataFactory.getAllUsers()
                .then(function (res) {
                        $scope.users = res.data;
                        refreshUsersTable();
                    }, function (error) {
                        dataFactory.errorAlert(error);
                    }
                )
            ;
            // $log.log("Info:  :" + $scope.users?.length);
        }

        $scope.editUser = (user) => {
            $scope.userForm.id = user.id;
            $scope.userForm.firstName = user.firstName;
            $scope.userForm.lastName = user.lastName;
            $scope.userForm.userName = user.userName;
            $scope.userForm.userPinIdNumber = user.userPinIdNumber;
            $scope.userForm.email = user.email;
            $scope.userForm.enabled = user.enabled;
            $scope.userForm.accountExpired = user.accountExpired;
            $scope.userForm.accountLocked = user.accountLocked;
            $scope.userForm.credentialsExpired = user.credentialsExpired;
            $scope.userForm.status = user.status;
            $scope.userForm.registrationDate = user.registrationDate;
            $scope.userForm.userType = user.userType;
            $scope.userForm.title = user.title;
            $scope.userForm.directorate = user.directorate;
            $scope.userForm.department = user.department;
            $scope.userForm.division = user.division;
            $scope.userForm.section = user.section;
            $scope.userForm.l1SubSubSection = user.l1SubSubSection;
            $scope.userForm.l2SubSubSection = user.l2SubSubSection;
            $scope.userForm.designation = user.designation;
            $scope.userForm.profileId = user.profileId;
            $scope.userForm.region = user.region;
            $scope.userForm.subRegion = user.subRegion;

        }

        const _success = () => {
            resetForms();
        };

        $scope.submitUser = () => {
            dataFactory.submitUserData($scope.userForm).then(_success, dataFactory.errorAlert);
        }

        $scope.resetUIToNew = () => {
            initializeUI();
            resetForms();


        }


        const resetForms = () => {

            $scope.userForm = {
                id: -1,
                title: 0,
                firstName: "",
                lastName: "",
                userName: "",
                email: "",
                enabled: 0,
                accountExpired: 0,
                accountLocked: 0,
                credentialsExpired: 0,
                status: 0,
                registrationDate: 0,
                userType: 0,
                directorate: null,
                department: null,
                division: null,
                section: null,
                l1SubSubSection: null,
                l2SubSubSection: null,
                designation: null,
                profileId: null
            };
        };
    }])
;