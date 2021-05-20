
testSdl.controller("sldController", ['$scope', 'ExceptionService',
    function($scope,  Sdl_Service){
        $scope.factortReportDetails = {
            purpose : '',
            personMet: '',
            actionTaken : '',
            remarks: ''
        }

        /*
        ** Redirecting
        */

        function goCNN(currentPage, pageSize, filters, fromDate, toDate){
            window.location.href=`/api/di/pvoc/officer?currentPage=${currentPage}&pageSize=${pageSize}&filter=${filters}&fromDate=${fromDate}&toDate=${toDate}`;
        }

        function goCNN2(currentPage, pageSize, filters, fromDate, toDate){
            window.location.href=`/api/di/pvoc/application/unfinished?currentPage=${currentPage}&pageSize=${pageSize}&filter=${filters}&fromDate=${fromDate}&toDate=${toDate}`;
        }


        $scope.saveReport = function (){
            console.log("Clicked")
            $scope.factortReportDetails = {
                purpose : $scope.purpose,
                personMet : $scope.personMet,
                actionTaken : $scope.actionTaken,
                remarks : $scope.remarks
            }

            console.log($scope.factortReportDetails)
        }
        // $scope.factortReportDetails = {}//purpose, personMet,actionTaken, remarks//saveReport
    }]);

