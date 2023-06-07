'use strict';

angular.module('myApp').factory('ExceptionService', ['$http', '$q', function($http, $q){

   // var PRODUCTS_URI = 'https://kims.kebs.org:8006/api/di/pvoc/rest/permits-products';
    const PRODUCTS_URI = '/api/di/pvoc/rest/permits-products';
    const MANUFACTURER_URI = '/api/di/pvoc/rest/manufacturer-details';
    // var MANUFACTURER_URI = 'https://kims.kebs.org:8006/api/di/pvoc/rest/manufacturer-details';
    var EXCEL_DOWNLOAD_URL = '/file'
    //var EXCEL_DOWNLOAD_URL = 'https://localhost:8006/file'
   // var EXEMPTION_UPLOAD_URI = 'https://localhost:8006/api/di/pvoc/rest/application-exception2'
    var EXEMPTION_UPLOAD_URI = '/api/di/pvoc/rest/application-exception2'
    var EXEMPTION_SAVE_URI = '/api/di/pvoc/rest/application-exception3'
    // var EXEMPTION_SAVE_URI = 'https://localhost:8006/api/di/pvoc/rest/application-exception3'
    // var EXEMPTION_PARTIAL_SAVE_URI = 'https://localhost:8006/api/di/pvoc/rest/application-exception4'
    var EXEMPTION_PARTIAL_SAVE_URI = '/api/di/pvoc/rest/application-exception4'
    //var EXEMPTION_SAVE_URI = 'https://localhost:8006/api/di/pvoc/rest/application-exception3'

    var factory = {
        fetchAllPermitProducts: fetchAllPermitProducts,
        fetchManufacturer : fetchManufacturer,
        getExceptionExcel : getExceptionExcel,
        createExemption: createExemption,
        uploadExemption: uploadExemption,
        saveExceptionViaSystem:saveExceptionViaSystem,
        saveExceptionPartially: saveExceptionPartially
    };

    return factory;

    function fetchAllPermitProducts() {
        var deferred = $q.defer();
        $http.get(PRODUCTS_URI)
            .then(
                function (response) {
                    //console.log(response.data)
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Products', errResponse);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function getExceptionExcel() {
        var deferred = $q.defer();
        $http.get(EXCEL_DOWNLOAD_URL)
            .then(
                function (response) {
                    //console.log(response.data)
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Excel File');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchManufacturer() {
        var deferred = $q.defer();
        $http.get(MANUFACTURER_URI)
            .then(
                function (response) {
                   // console.log(response.data)
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Manufacturer',  errResponse);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createExemption(exemption) {
        var deferred = $q.defer();
        $http.post(EXEMPTION_URI, exemption)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function uploadExemption(manufacturer, file) {
        //FormData, object of key/value pair for form fields and values
        const payload = new FormData();
        payload.append('uploadedfile', file)
        for (const key in manufacturer ) {
            payload.append(key, manufacturer[key]);
        }

        console.log(payload)

        const request = {
            "url": EXEMPTION_UPLOAD_URI,
            "method": "POST",
            "data": payload,
            "headers": {
                'Content-Type': undefined // important
            }
        };

        const deffered = $q.defer();

        $http(request).success(function(data){
            deffered.resolve(data);
        }).error(function(error){
            deffered.reject(error);
        });
        return deffered.promise;
    }


    function saveExceptionViaSystem(exemptionApp) {
        console.log(exemptionApp)
        const request = {
            "url": EXEMPTION_SAVE_URI,
            "method": "POST",
            "data": exemptionApp,
            "headers": {
                'Content-Type': 'application/json' // important
            }
        };

        const deffered = $q.defer();

        $http(request).success(function(data){
            deffered.resolve(data);
        }).error(function(error){
            deffered.reject(error);
        });
        return deffered.promise;
    }

    function saveExceptionPartially(exemptionApp){
        console.log(exemptionApp)
        const request = {
            "url": EXEMPTION_PARTIAL_SAVE_URI,
            "method": "POST",
            "data": exemptionApp,
            "headers": {
                'Content-Type': 'application/json' // important
            }
        };

        const deffered = $q.defer();

        $http(request).success(function(data){
            deffered.resolve(data);
        }).error(function(error){
            deffered.reject(error);
        });
        return deffered.promise;
    }

}]);
