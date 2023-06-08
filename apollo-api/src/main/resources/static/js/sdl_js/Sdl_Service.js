'use strict';

angular.module('myApp').factory('ExceptionService', ['$http', '$q', function($http, $q){

    // var PRODUCTS_URI = 'https://kims.kebs.org:8006/api/di/pvoc/rest/permits-products';
    const SDL_FACTORY_VISIT = `/sl/save-data/${manufacturerId}`;
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
        createExemption: createExemption,
        saveExceptionViaSystem:saveExceptionViaSystem
    };

    return factory;

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
}]);
