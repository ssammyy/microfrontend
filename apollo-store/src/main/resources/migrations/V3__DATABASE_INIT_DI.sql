-- SET FOREIGN_KEY_CHECKS=0;
-- Service Maps
alter table CFG_SERVICE_MAPS
    add HTTP_FAILURE_RESPONSE_MESSAGE varchar(2256);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (101, 'SHA-512', 'SHA1PRNG', 15, 'kebs_registration', null, null, 'RegistrationProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, 'CertificateOfConformities', 'DAT_KEBS_EMPLOYEES', 1, 0,
        5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        'redirect:/api/auth/signup/notification/success', '/api/auth/signup/manufacturer', 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (102, 'SHA-512', 'SHA1PRNG', 20, 'test-topic-1', 'BaseRequest', 'BaseRequest', 'DIApplicationProcessingActor', 1,
        2, 2, 4, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'usd.', null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, 'Consignments', 'DAT_CONSIGNMENT_DOCUMENT',
        null, null, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (104, 'SHA-512', 'SHA1PRNG', 30, 'kebs_inspection', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0, null,
        null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (105, 'SHA-512', 'SHA1PRNG', 35, 'kebs_qa_modified', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (106, 'SHA-512', 'SHA1PRNG', 40, 'kebs_qa_review', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0, null,
        null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (117, 'SHA-512', 'SHA1PRNG', 20, 'std-req-hof-remarks', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardsRequest', 'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (122, 'SHA-512', 'SHA1PRNG', 55, 'kebs_cd_coc', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 15, 20,
        30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null,
        null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (128, 'SHA-512', 'SHA1PRNG', 15, 'user-validation', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        '/api/auth/signup/manufacturer', 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (114, 'SHA-512', 'SHA1PRNG', 20, 'sms-topic', 'BaseRequest', 'BaseRequest', 'serviceRequestProcessingActor', 1,
        2, 0, 4, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0, '_', 'Ksh.', null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, 'NotificationsEntity', 'NotificationsEntity',
        1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (115, 'SHA-512', 'SHA1PRNG', 20, 'email-topic', 'BaseRequest', 'BaseRequest', 'serviceRequestProcessingActor', 1,
        2, 0, 4, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0, '_', 'Ksh.', null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, 'NotificationsEntity', 'NotificationsEntity',
        1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (129, 'SHA-512', 'SHA1PRNG', 7, 'ms-complaints-management', null, null, 'serviceRequestProcessingActor', 1, 2, 0,
        10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        '/api/auth/signup/manufacturer', 24, null, 'ms-complaint-process', 'KEBS-MS');
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (103, 'SHA-512', 'SHA1PRNG', 25, 'kebs_qa_application', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (107, 'SHA-512', 'SHA1PRNG', 30, 'pvoc_integration', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 20,
        10, 20, 30, 25, -1, null, null, 100, '0', '90', '05', '00', 200, 0, 0, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, null, 5, 4, null, null, null, 0,
        'Message Sent Successfully', 'Message Sending Failed', null, null, 10, 8, null, null, 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (116, 'SHA-512', 'SHA1PRNG', 20, 'std-req-hod-remarks', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardsRequest', 'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (108, 'SHA-512', 'SHA1PRNG', 45, 'kebs_qa_awarding', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (111, 'SHA-512', 'SHA1PRNG', 15, 'submit-std-request', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'org.kebs.app.kotlin.store.model.StandardsRequest',
        'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, null,
        'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (110, 'SHA-512', 'SHA1PRNG', 50, 'sampleForm', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20,
        30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null,
        null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (124, 'SHA-512', 'SHA1PRNG', 35, 'kebs-CD-Exempt-PVoc', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (125, 'SHA-512', 'SHA1PRNG', 55, 'kebs_CD_resubmit', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (126, 'SHA-512', 'SHA1PRNG', 55, 'kebs_CD_supervisor', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (119, 'SHA-512', 'SHA1PRNG', 20, 'std-nsc-sec-remarks', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardsRequest', 'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (120, 'SHA-512', 'SHA1PRNG', 20, 'std-justification', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardJustificationEntity', 'StandardJustificationDto', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (112, 'SHA-512', 'SHA1PRNG', 50, 'batchNo', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30,
        25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null,
        null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (118, 'SHA-512', 'SHA1PRNG', 20, 'std-req-assign-tc', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardsRequest', 'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (121, 'SHA-512', 'SHA1PRNG', 20, 'std-tc-sec-remarks', 'BaseRequest', 'BaseRequest',
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'org.kebs.app.kotlin.store.model.StandardsRequest', 'StandardRequest', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (123, 'SHA-512', 'SHA1PRNG', 56, 'kebs_cd_risk_check', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (127, 'SHA-512', 'SHA1PRNG', 5, 'user-registration', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        'redirect:/api/auth/signup/user', 24, 'QA Officer', 'registration', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (207, 'SHA-512', 'SHA1PRNG', 55, 'destination-inspection', null, null, 'serviceRequestProcessingActor', 1, 2, 0,
        10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, '=', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        'redirect:/api/auth/signup/manufacturer', 24, 'DI', null, null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (208, 'SHA-512', 'SHA1PRNG', 40, 'kebs-qa-processes', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, '=', 1, 0, 5, 4, 'basic', 'digest', null, 0,
        null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        'redirect:/api/auth/signup/manufacturer', 24, 'Manufacturer', null, null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (210, 'SHA-512', 'SHA1PRNG', 15, 'invoice_transactions', null, null, 'serviceRequestProcessingActor', 1, 2, 0,
        10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'CertificateOfConformities', 'DAT_KEBS_EMPLOYEES',
        1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        'redirect:/api/auth/signup/notification/success', '/api/auth/signup/manufacturer', 24, null, null, null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (220, 'SHA-512', 'SHA1PRNG', 15, 'consignment_document_xml_transactions', null, null,
        'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0,
        '_', 'Ksh.', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        'ConsignmentDocumentDetails', 'CD_XML', 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        'redirect:/api/auth/signup/notification/success', '/api/auth/signup/manufacturer', 24, null, null, null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (206, 'SHA-512', 'SHA1PRNG', 56, 'qa-fuel-inspection', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 1,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '0', 200, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'PetroleumInstallationInspectionEntity',
        'PetroleumInstallationInspectionEntity', 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        null, null, 24, null, 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (130, 'SHA-512', 'SHA1PRNG', 6, 'ms-complaint', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10, 20,
        30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic', 'digest',
        null, 0, null, null, null, null, 10, 8, 'redirect:/api/auth/signup/notification/success',
        '/api/auth/signup/manufacturer', 24, null, 'ms-complaint-process', 'KEBS-MS');
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (150, 'SHA-512', 'SHA1PRNG', 56, 'permit-application', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 200, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'PermitApplicationEntity',
        'PermitApplicationEntity', 1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8, null, null,
        24, 'Manufacturer', 'complainManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (199, 'SHA-512', 'SHA1PRNG', 56, 'user-management', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '0', 200, 0, 0, '-', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, 'HOD', 'userManagement', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (205, 'SHA-512', 'SHA1PRNG', 56, 'ministry-inspection', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '0', 200, 0, 0, '-', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'UsersEntity', 'UsersEntity', 1, 0, 5, 4, 'basic',
        'digest', null, 0, null, null, null, null, 10, 8, null, null, 24, 'DI Officer', 'ministryInspection', null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (211, 'SHA-512', 'SHA1PRNG', 15, 'risk_profile', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10, 10,
        20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, 'CertificateOfConformities', 'DAT_KEBS_EMPLOYEES', 1, 0,
        5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        'redirect:/api/auth/signup/notification/success', '/api/auth/signup/manufacturer', 24, null, null, null);
INSERT INTO CFG_SERVICE_MAPS (ID, MESSAGE_DIGEST_ALGORITHM, SECURE_RANDOM, TRANSACTION_REF_LENGTH, SERVICE_TOPIC,
                              REQUEST_CLASS, RESPONSE_CLASS, ACTOR_CLASS, ACTIVE_STATUS, TEST_STATUS, INACTIVE_STATUS,
                              INIT_STATUS, WORKING_STATUS, FAILED_STATUS, SUCCESS_STATUS, EXCEPTION_STATUS,
                              INVALID_STATUS, INVALID_STATUS_KRA, INVALID_STATUS_REGISTRAR, VALID_STATUS, INIT_STAGE,
                              EXCEPTION_STATUS_CODE, FAILED_STATUS_CODE, SUCCESS_STATUS_CODE, HTTP_SUCCESS_RESPONSE,
                              HTTP_FAILURE_RESPONSE, TEST_CODE_STATUS, SEPARATOR, CURRENCY_SYMBOL, VAR_FIELD_1,
                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                              VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, DELETED_BY, DELETED_ON,
                              OBJECT_MAPPED_TO, OBJECT_MAPPED_FROM, STATUS, TO_MAP, EMPLOYEE_USER_TYPE,
                              MANUFACTURER_USER_TYPE, BASIC_AUTHENTICATION_VALUE, DIGEST_AUTHENTICATION_VALUE,
                              MANUFACTURER_USER_TABLE, MAIN_VERSION_ID, HTTP_SUCCESS_RESPONSE_MESSAGE,
                              HTTP_FAILURE_RESPONSE_MESSAGE, SUB_REGION_ID, DESIGNATION_ID, UI_PAGE_SIZE,
                              PASSWORD_LENGTH, SUCCESS_NOTIFICATION_URL, FAILURE_NOTIFICATION_URL, TOKEN_EXPIRY_HOURS,
                              ROLE_NAME, BPMN_PROCESS_KEY, TRANSACTION_REF_PREFIX)
VALUES (212, 'SHA-512', 'SHA1PRNG', 15, 'lims_transactions', null, null, 'serviceRequestProcessingActor', 1, 2, 0, 10,
        10, 20, 30, 25, -1, -2, -3, 100, '0', '90', '05', '00', 0, 0, 0, '_', 'Ksh.', null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, 'CertificateOfConformities', 'DAT_KEBS_EMPLOYEES',
        1, 0, 5, 4, 'basic', 'digest', null, 0, null, null, null, null, 10, 8,
        'redirect:/api/auth/signup/notification/success', '/api/auth/signup/manufacturer', 24, null, null, null);
-- Process Stages
INSERT INTO CFG_KEBS_PROCESSES_STAGES (ID, SERVICE_MAP_ID, PROCESS_1, PROCESS_2, PROCESS_3, PROCESS_4, PROCESS_5,
                                       PROCESS_6, PROCESS_7, PROCESS_8, PROCESS_9, PROCESS_10, PROCESS_11, PROCESS_12,
                                       PROCESS_13, PROCESS_14, PROCESS_15, PROCESS_16, PROCESS_17, PROCESS_18,
                                       PROCESS_19, PROCESS_20, PROCESS_21, PROCESS_22, PROCESS_23, PROCESS_24,
                                       PROCESS_25, PROCESS_26, PROCESS_27, PROCESS_28, PROCESS_29, PROCESS_30,
                                       DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                       VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                       CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (1, 220, 'CREATION OF CD', 'ADD CD Standard DETAILS', 'ADD CD Importer DETAILS', 'ADD CD Transport DETAILS',
        'ADD CD Exporter DETAILS', 'ADD CD Consignee DETAILS', 'ADD CD Consignor DETAILS', 'ADD CD Header One DETAILS',
        'ADD CD Standard Two DETAILS', 'ADD CD Items DETAILS',
        'ADD CD freightStation, cluster and port Of Arrival DETAILS', 'ADD CD Approval History Details DETAILS',
        'ADD CD Transport Container DETAILS', 'ADD CD PGA Header Fields Details', 'ADD CD Header Two Details',
        'ADD CD Third Party Details', 'ADD CD Applicant Defined Third Party Details', 'ADD CD Processing Fee Details',
        'ADD CD Document Fee Details', 'ADD CD Risk Assessment Details', null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PROCESSES_STAGES (ID, SERVICE_MAP_ID, PROCESS_1, PROCESS_2, PROCESS_3, PROCESS_4, PROCESS_5,
                                       PROCESS_6, PROCESS_7, PROCESS_8, PROCESS_9, PROCESS_10, PROCESS_11, PROCESS_12,
                                       PROCESS_13, PROCESS_14, PROCESS_15, PROCESS_16, PROCESS_17, PROCESS_18,
                                       PROCESS_19, PROCESS_20, PROCESS_21, PROCESS_22, PROCESS_23, PROCESS_24,
                                       PROCESS_25, PROCESS_26, PROCESS_27, PROCESS_28, PROCESS_29, PROCESS_30,
                                       DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                       VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                       CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (21, 122, 'ASSIGN PORT IF NOT THERE', 'ASSIGN OFFICER', null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null);
-- COC Types
INSERT INTO CFG_COC_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                           VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                           CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (2, 'F', 'Foreign', null, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_COC_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                           VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                           CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (1, 'L', 'Local ', null, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null);
-- Consignment document type/categories
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (1, 'Foreign COC/COI', 'Imported Goods with CoC/COI', 1, 'FOREIGN_COC_COI', null, null, null, null, null, null,
        null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 0, 0,
        '5a015375-5661-46cf-9ea4-cc4823c7e80e', 0, 'DN', 'GOODS', 0, 0, null, null, 'FOREIGN');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (5, 'Courier Goods', 'Courier Goods', 1, 'COURIER_GOODS', null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 0, 1,
        'a14b10d9-544a-45b9-a4a0-9d1f4b3789b3', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (2, 'Local COC/COI', 'Imported Goods without(COC/COI) from Countries without PVOC', 1, 'LOCAL_COC_COI', null,
        null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin',
        null, 0, 1, 'f00a8877-e227-4b8d-8155-166618ef3a65', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (3, 'NCR', 'Imported Goods With NCR', 1, 'NCR', null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 0, '79b42d8f-d262-4c87-a667-83671cecfad4', 0,
        'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (4, 'Foreign COR', 'Imported Vehicles With CoR', 1, 'FOREIGN_COR', null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 0, 0,
        '8aa168fa-287f-4ee2-abf3-ef9db660ac89', 0, 'DN', 'VEHICLES', 0, 0, null, null, 'FOREIGN');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (6, 'Local COC/COI(With PVOC)', 'Imported Goods With no CoC From Countries with PVoC', 0, 'NO_COC_PVOC', null,
        null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin',
        null, 0, 1, 'c657ba3b-2eab-47bd-9fe6-27225af5d373', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (8, 'Local COR', 'Imported Vehicles Without CoR without PVOC', 1, 'LOCAL_COR', null, null, null, null, null,
        null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 0,
        'b4eb80cc-5044-478c-acb8-4c7a99d53b53', 1, 'DN', 'VEHICLES', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (7, 'Temporary Imports ', 'Imported Goods with Temporary Import', 1, 'TEMPORARY_IMPORTS', null, null, null, null,
        null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 1,
        'e143e2a7-5e5d-4f13-96dc-9419e0e9fb4c', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (10, 'Auction Goods', 'Auction Goods', 1, 'AUCTION_GOODS', null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 0, 0,
        '5d4bc06e-f8d8-4efb-be8f-d14fb8e337bb', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (12, 'Re-Import', 'Re-Imports', 1, 'RE_IMPORT', null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 1, 'c56891be-e776-451c-b642-dfc33c8f5128', 0,
        'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (61, 'Local COC/COI', 'Imported Goods without COC/COI', 0, 'NO_COC', null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 1,
        '73a70be2-d3a4-41a4-a492-d30916acf4a3', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (15, 'Vehicle Exempted COR', 'Exempted Vehicles', 1, 'EXEMPTED_COR', null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 0,
        'c489f645-497e-470c-a1e1-9141fcf6c129', 1, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (13, 'Local COR(With PVOC)', 'Imported Vehicles Without CoR from countries with PVOC', 1, 'NO_COR_PVOC', null,
        null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin',
        null, 0, 0, '6c4f8757-daa4-4fdb-bab1-542638d1b1c3', 1, 'DN', 'VEHICLE', 0, 1, 'DES_INS', null, 'LOCAL');
INSERT INTO CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                                 VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7,
                                                 VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                                 MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, INSPECTION_STATUS,
                                                 LOCAL_COC_STATUS, UUID, LOCAL_COR_STATUS, DEMAND_NOTE_PREFIX, CATEGORY,
                                                 AUTO_REJECTED_STATUS, AUTO_TARGET_STATUS, AUTO_TARGET_CONDITION,
                                                 AUTO_REJECT_CONDITION, DOCUMENT_TYPE)
VALUES (14, 'Goods Exempted COC', 'Exempted Goods', 1, 'EXEMPTED_COC', null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 1, 1,
        '84095b9f-c5c2-4cfd-9dbe-5434267889d8', 0, 'DN', 'GOODS', 0, 1, 'DES_INS', null, 'LOCAL');
-- Local COC types
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (1, 'EXE', 'Exemption', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (2, 'PGES', 'Power Generators equipment/spares', null, 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (3, 'MEP', 'Medical Equipment and parts', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (4, 'DG', 'Diplomatic goods', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (5, 'DMSG', 'Diamond Mark Scheme goods', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (6, 'EAC', 'EAC goods', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (7, 'UPERR', 'Used personal effects for returning residents', null, 1, null, null, null, null, null, null, null,
        null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (8, 'ISGCDG', 'Inflight/ships general consumables/duty free goods ', null, 1, null, null, null, null, null, null,
        null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (9, 'RIMP', 'Re-import', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (10, 'TIMP', 'Temporary imports', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 7);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (11, 'MWG', 'Ministerial waiver goods', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (12, 'TIDF', 'Transition IDF', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
INSERT INTO CFG_KEBS_LOCAL_COC_TYPES (ID, COC_TYPE_CODE, COC_TYPE_DESC, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2,
                                      VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                      VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                      DELETE_BY, DELETED_ON, CD_TYPE)
VALUES (21, 'DES_INS', 'Destination Inspection', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 2);
-- Checklist Categories
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (1, 'Normal', 'Normal', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (3, 'Waiver', 'Waiver', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (4, 'Exemption', 'Exemption', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (5, 'D/Mark', 'D/Mark', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (6, 'Others', 'Others', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_CD_CHECKLIST_CATEGORY (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8,
                                            VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON,
                                            DELETE_BY, DELETED_ON)
VALUES (2, 'Used Motor Vehicle', 'Used Motor Vehicle', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
-- Ministry Stations
INSERT INTO CFG_KEBS_MINISTRY_STATION (ID, STATION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                       VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                       VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                       DELETED_ON)
VALUES (1, 'NAIROBI', 'NAIROBI STATION', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_MINISTRY_STATION (ID, STATION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                       VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                       VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                       DELETED_ON)
VALUES (2, 'MOMBASA', 'MOMBASA STATION', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
-- Integration configurations
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (4, 'JSON', 'config', 1000, 1000, '99', '99', 0, null, 5, 5, 200, 399,
        'uNhgze7tdeLQIg0WSEgs7WJFaQOE+DHA/LlkjXFbyYU=', 'request', 'messageBody|sender', '|', '{0}', 'messageBody',
        null, 'KEBS', 43, 1000, 'https://api.onfonmedia.co.ke/v1/sms/SendBulkSMS',
        'f02b824c-034e-4be0-bd6b-0ed4c951851c', 1, 'Kebs SMS Gateway', 'a90sWeB5TJUbYCJWxtVWSTVt8MtnQpMH', null, null,
        null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'NA', null, 'POST', 'dd mm yyyy',
        'kebsMtSmsResponse', '00', null, null, null, null, null, null, null, null, null, 0, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (3, 'RAW', 'config', 15000, 15000, '99', '99', 0, null, 5, 5, 200, 399,
        'aba5wfJUvBzLVB7W+yXk+FtinvclL4oeBe5MGBF7qfARXniGEWiXLn1+eszPZdZ8', 'request', 'registration_number', '|',
        '{0}', 'user.manufacturer.registrationNumber', null, null, 2, 15000,
        'https://brs.ecitizen.go.ke/api/businesses', 'EBGJTnTDdj2aWGU3k7VhnKylDyalEppf+pBxvjaFGUQ=', 1,
        'BRS Company Name lookup integration', null, null, null, null, null, null, null, null, null, null, 'admin',
        'admin', 'admin', 'basic', null, 'GET', 'dd mm yyyy', 'brsLookUpResponse', '00', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, null, null, null, null, null, null, null, 0, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (26, 'JSON', 'config', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399, 'rQy9ADpAIX7P+8CTnXb2TbjJCBTpr4An',
        'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 85, 10000,
        'https://196.61.52.30/KEBS/kebs', '3quEL7IksNEsX665v62Wi5rh+c2uYWbc', 1, 'KESWS Consignment Document Gateway',
        null, null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST',
        'dd-MM-yyyy''T''HH:mm:ss', 'kebsMtSmsResponse', '90000', null, null, null, null, null, null, null, null, null,
        0, 22, null, 'SHA-256', null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (22, 'JSON', 'config', 1000, 1000, '99', '05', 0, null, 5, 5, 200, 399, 'E3stc8Uj+wwT/ERp5vypzA==', 'request',
        'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 43, 1000,
        'https://10.10.0.191:9026//integ/gateway/anon/6/validation/json', 'Aae8OgnF1ZMTYVhogXN3uA==', 1,
        'Kappa Payment Gateway', null, null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin',
        'NA', null, 'POST', 'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, null, null, null, null, null,
        null, 0, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (25, 'JSON', 'config', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399,
        'aG0/Y6I4F9vLSLhsytMK4k7BWpg1h279L79/R5y2U1Y=', 'request', 'messageBody|sender', '|', '{0}', 'messageBody',
        null, 'KEBS', 43, 15000, 'https://10.10.0.191:9026/auth/login', 'bwlUAd74mNo3qJv76OlpnA==', 1,
        'Kappa Payment Gateway', null, null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin',
        'basic', null, 'POST', 'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, '804700', 'TESTS',
        '03cb4549893eh',
        'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2NDY4MTM4NjEsImV4cCI6MTY0NjgxOTg2MSwiZXhwaXJlc0luIjo2MDAwLCJhZGRyZXNzIjoiMTAuMTAuMC4xNDkiLCJ1c2VyQWdlbnQiOiJbS3RvciBjbGllbnRdIiwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.LWQezXFuK3Zb8JoHifoyGXcJWcIpBJVjl2QE6lrP-KlOE7nZWT0sFYZ8fumNbUcL4UHhkAMFEEoM8B_dTfiJew',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (32, 'JSON', 'config', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399, 'zD/nQzxw3bxU6rPBfWa40kSHu3J9hdBA',
        'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 43, 15000,
        'https://kebsmpesa.kebs.org/m-api/urls/postinvoice.php', 'uZ5O67OffCtfFhfmRZ139R0ZuQqgKKy6', 1,
        'For KIMS- SAGE 300 Integration, Below is the Callback for posting invoices to be paid by Mpesa.', null, null,
        null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST', 'dd mm yyyy',
        'kebsMtSmsResponse', '00', null, null, null, '600754', 'TESTS', '03cb4549893eh',
        'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2Mjc5Nzk4ODUsImV4cCI6MTYyNzk4NTg4NSwiZXhwaXJlc0luIjo2MDAwLCJhZGRyZXNzIjoiMTAuMTAuMC4xNDkiLCJ1c2VyQWdlbnQiOiJbS3RvciBjbGllbnRdIiwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.6ldBsygo6xU2p6eUZy9EHNcZKZjyVPruHZU4f18YgIejPXfxDpP584ej0iDEJGJEiui7iiqpSkh3wqiKLwWChw',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (41, 'X-WWW-FORM-URLENCODED', 'config', 1000, 1000, '99', '99', 0, null, 5, 5, 200, 399,
        'AbqmSDf+HCN58dVia0M7jA==', 'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', null,
        1000, 'https://intranet.kebs.org/sms_kernel/bulk_smssend.php/sms_kernel/bulk_smssend.php',
        'AbqmSDf+HCN58dVia0M7jA==', 1, 'KIMS SMS GATEWAY', null, null, null, null, null, null, null, null, null, null,
        'admin', 'admin', 'admin', 'basic', null, 'POST', 'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null,
        null, null, null, null, null, null, 0, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (29, 'JSON', 'config', 1000, 1000, '99', '05', 0, null, 5, 5, 200, 399, 'cAxmtm9gxdAoQdNXUb1BRQ==', 'request',
        'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 43, 1000,
        'https://intranet.kebs.org/lims/get_result.php', 'cAxmtm9gxdAoQdNXUb1BRQ==', 1, 'Lims Get Lab Results', null,
        null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST',
        'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, '600754', 'TESTS', '03cb4549893eh',
        'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2MjEwODgzMDYsImV4cCI6MTYyMTA5NDMwNiwiZXhwaXJlc0luIjo2MDAwLCJhZGRyZXNzIjoiMTk3LjI0OC4xODcuMTAiLCJ1c2VyQWdlbnQiOiJbS3RvciBjbGllbnRdIiwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.uY8ar3iDDnB0sS19MSwn4lf7m9P0zzIRZGrBTdQawqY2Ae8qm-ZZbavr5_S3MdHAKbAn3H_5z4AYjMrDBtfcWA',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (30, 'JSON', 'config', 1000, 1000, '99', '05', 0, null, 5, 5, 200, 399, 'cAxmtm9gxdAoQdNXUb1BRQ==', 'request',
        'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 43, 1000,
        'https://intranet.kebs.org/lims/get_filelist.php', 'cAxmtm9gxdAoQdNXUb1BRQ==', 1, 'Lims Get Lab Results', null,
        null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST',
        'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, '600754', 'TESTS', '03cb4549893eh',
        'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2MjEwODgzMDYsImV4cCI6MTYyMTA5NDMwNiwiZXhwaXJlc0luIjo2MDAwLCJhZGRyZXNzIjoiMTk3LjI0OC4xODcuMTAiLCJ1c2VyQWdlbnQiOiJbS3RvciBjbGllbnRdIiwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.uY8ar3iDDnB0sS19MSwn4lf7m9P0zzIRZGrBTdQawqY2Ae8qm-ZZbavr5_S3MdHAKbAn3H_5z4AYjMrDBtfcWA',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (27, 'JSON', 'config', 1000, 1000, '99', '99', 0, null, 5, 5, 200, 399, 'c/5R2hPoTq+GEQtkyRgc8FP7bEngIYSj',
        'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', null, 1000,
        'http://212.22.169.22:9092/api/eacisp', 'E+mVC3D2wMy+Z1qTQtMvQfvXrYipKfRHU/kGzl3M254=', 1, 'EAC Endpoint', null,
        null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', '/user/autheticate',
        'POST', 'dd mm yyyy', 'kebsMtSmsResponse', '202', null, null, null, null, null, null,
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2MTg4MTA3MjYsImV4cCI6MTYxODgxNjcyNiwiYWRkcmVzcyI6IjE5Ni4yMDIuMjA1LjE0NiIsInVzZXJBZ2VudCI6IltLdG9yIGNsaWVudF0iLCJyb2xlcyI6IkMyQl9SRUdJU1RFUl9VUkwsQ1JFQVRFX0VYUFJFU1NfUkVRVUVTVCxRVUVSWV9FWFBSRVNTX1JFUVVFU1QifQ.Gie7e2igGX9tanDnCkGNDq9wwCcEVkSj-mqLC_3aLcNyCcbrV-fjqfWaQ4MTbAgwrLnI1wqShqWlI987ryneDg',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 0, 22, null, null,
        'EeENSFztlMs40UH1G5lUzmUuZAehP2d1zoqAuDR/77e1Kp4mfeRFHSTzL2I2cWba', null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (28, 'JSON', 'config', 1000, 1000, '99', '99', 0, null, 5, 5, 200, 399, 'c/5R2hPoTq+GEQtkyRgc8FP7bEngIYSj',
        'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', null, 1000,
        'http://212.22.169.22:9092/api/eacisp', 'E+mVC3D2wMy+Z1qTQtMvQfvXrYipKfRHU/kGzl3M254=', 1, 'EAC Endpoint', null,
        null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', '/user/autheticate',
        'GET', 'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, null, null, null, null, null, null, 0, 22,
        null, null, 'EeENSFztlMs40UH1G5lUzmUuZAehP2d1zoqAuDR/77e1Kp4mfeRFHSTzL2I2cWba', null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (31, 'JSON', 'config', 1000, 1000, '99', '05', 0, null, 5, 5, 200, 399, 'cAxmtm9gxdAoQdNXUb1BRQ==', 'request',
        'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS', 43, 1000,
        'https://intranet.kebs.org/lims/get_pdf.php', 'cAxmtm9gxdAoQdNXUb1BRQ==', 1, 'Lims Get Lab Results', null, null,
        null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST', 'dd mm yyyy',
        'kebsMtSmsResponse', '00', null, null, null, '600754', 'TESTS', '03cb4549893eh',
        'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2MjEwODgzMDYsImV4cCI6MTYyMTA5NDMwNiwiZXhwaXJlc0luIjo2MDAwLCJhZGRyZXNzIjoiMTk3LjI0OC4xODcuMTAiLCJ1c2VyQWdlbnQiOiJbS3RvciBjbGllbnRdIiwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.uY8ar3iDDnB0sS19MSwn4lf7m9P0zzIRZGrBTdQawqY2Ae8qm-ZZbavr5_S3MdHAKbAn3H_5z4AYjMrDBtfcWA',
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (33, 'JSON', 'SAGE_API_CLIENT', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399,
        'KwLvLB0ThS5xt11tB6cV83bRqNR/MoB8', 'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS',
        43, 15000, 'http://10.10.1.148/bsk/', '2p+s9k57dJSuM3ys23xKew==', 1, 'For KIMS- SAGE 300 Integration 2', null,
        null, null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST',
        'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, 'BSK', null, '03cb4549893eh', null,
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (35, 'JSON', 'KRA_API_CLIENT', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399,
        'pvZ2SyhaUVXmbnp6RoA1bHur3YtDxosZ', 'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS',
        43, 15000, 'https://196.61.52.30/KEBS/kebs/sendEntryDetails', 'O134CSw9ixfPvOFTI1+AbwRRWjFA35FL', 1,
        'For KIMS- KRA Entry Number API', null, null, null, null, null, null, null, null, null, null, 'admin', 'admin',
        'admin', 'basic', null, 'POST', 'dd mm yyyy', 'kebsMtSmsResponse', '00', null, null, null, 'BSK', null,
        '03cb4549893eh', null, TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, '256', null, null);
INSERT INTO CFG_INTEGRATION_CONFIGURATION (ID, BODY_PARAMS, CONFIG_KEYWORD, CONNECTION_REQUEST_TIMEOUT, CONNECT_TIMEOUT,
                                           EXCEPTION_CODE, FAILURE_CODE, FOLLOW_REDIRECTS, HEADER_PARAMS,
                                           MAX_CONN_PER_ROUTE, MAX_CONN_TOTAL, OK_LOWER, OK_UPPER, PASSWORD,
                                           REQUEST_KEYWORD, REQUEST_PARAMS, REQUEST_PARAMS_SEPARATOR,
                                           REQUEST_PARAMS_SPEL, REQUEST_PARAMS_VALUES, RESPONSE_PLACE_HOLDER, SENDER,
                                           WORKFLOW_ID, SOCKET_TIMEOUT, URL, USERNAME, STATUS, DESCRIPTIONS,
                                           VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                           VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, MODIFIED_BY,
                                           DELETE_BY, CLIENT_AUTHENTICATION, CLIENT_AUTHENTICATION_REALM, CLIENT_METHOD,
                                           GSON_DATE_FORMT, GSON_OUTPUT_BEAN, SUCCESS_CODE, CREATED_ON, MODIFIED_ON,
                                           DELETED_ON, ACCOUNT, ACCOUNT_REFERENCE, TRANSACTION_REFERENCE, TOKEN,
                                           TOKEN_TIME_GENERATED, TOKEN_TIME_EXPIRES, TOKEN_TIME_LAPSE, PORT_NUMBER,
                                           UNKNOWN_HOST_KEY, HASHING_ALGORITHM, SECRET_VALUE, CALLBACK_URL)
VALUES (62, 'JSON', 'SAGE_INVOICE_API_CLIENT', 15000, 15000, '99', '05', 0, null, 5, 5, 200, 399,
        '6pgmJIa7ymiYNP6nTTqDwUzhn4IEb7dJ', 'request', 'messageBody|sender', '|', '{0}', 'messageBody', null, 'KEBS',
        43, 15000, 'http://10.10.1.148', '2p+s9k57dJSuM3ys23xKew==', 1, 'For KIMS- SAGE 300 Integration 2', null, null,
        null, null, null, null, null, null, null, null, 'admin', 'admin', 'admin', 'basic', null, 'POST', 'dd mm yyyy',
        'kebsMtSmsResponse', '00', null, null, null, 'BSK', null, '03cb4549893eh', null,
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'),
        TO_TIMESTAMP(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, 0, null, null, null, null);
-- CD Status Types
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (1, 'Normal Release', 'Normal Release', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'NORMAL_RELEASE', 'AP', 0, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (2, 'Partial Release', 'Partial Release', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PARTIAL_RELEASE', 'AP', 0, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (3, 'Conditional Release', 'Conditional Release', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'CONDITIONAL_RELEASE', 'AP', 0, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (4, 'On-Hold', 'On-Hold', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'ONHOLD', 'OH', 1, 0, 0);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (5, 'Rejected', 'Consignment Rejected', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'REJECT', 'RJ', 0, 1, 0);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (6, 'Approved', 'Consignment Approved', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'APPROVE', 'AP', 0, 1, 0);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (7, 'Awaiting Payment', 'Awaiting Payment', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PAYMENT_APPROVE', null, 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (8, 'Payment Made', 'Payment Made', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PAYMENT_MADE', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (9, 'Demand note rejected', 'Demand note rejected', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PROCESS_REJECT', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (10, 'Rejected for amendment', 'Rejected for amendment', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'AMENDMENT', 'RJA', 0, 0, 0);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (11, 'Query', 'Query', 1, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, 'QUERY', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (30, 'LOCAL COC GENERATED', 'LOCAL COC GENERATED', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COC_ISSUED', 'QY', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (31, 'LOCAL COI GENERATED', 'LOCAL COI GENERATED', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COI_ISSUED', 'QY', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (32, 'LOCAL COR GENERATED', 'LOCAL COR GENERATED', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COR_ISSUED', 'QY', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (35, 'Ministry Report Request', 'Checklist filled, waiting for ministry report', 1, null, null, null, null, null,
        null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null,
        'MINISTRY_REQUEST', 'MI', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (36, 'Targeting  Rejected', 'Targeting Rejected', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'TARGET_REJECTED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (37, 'Self-Assigned IO', 'Self-Assigned IO', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'IO_SELF_ASSIGNED', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (38, 'New Consignment', 'New Consignment', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'NEW_CD', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (39, 'Revised Consignment', 'Revised Consignment', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'REVISED_CD', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (40, 'Compliance Request', 'Compliance Request', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COMPLIANCE_REQ', 'CR', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (41, 'Compliance Approved', 'Compliance Approved', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COMPLIANCE_APPROVE', null, 0, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (42, 'Compliance Rejected', 'Compliance Rejected', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COMPLIANCE_REJECTED', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (43, 'Payment Request', 'Payment Requested', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PAYMENT_REQUEST', 'PR', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (29, 'Approve Inspection Report', 'Approve Inspection Report', 1, null, null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'APPROVE_INSPECTION', 'QY',
        1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (28, 'Generate Inspection Report', 'Generate Inspection Report', 1, null, null, null, null, null, null, null,
        null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null,
        'GENERATE_INSPECTION_REPORT', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (27, 'Lab Results Not Complaint', 'Lab Results Not Complaint', 1, null, null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'NON_COMPLIANCE', 'AP', 0, 1,
        1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (26, 'Lab Results Complaint', 'Lab Results Complaint', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'COMPLIANCE', 'AP', 0, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (25, 'Received Lab Results', 'Received Lab Results', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'LAB_RESULT', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (24, 'Awaiting Lab Results', 'Checklist filled, awaiting Lab Results', 1, null, null, null, null, null, null,
        null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'LAB_REQUEST',
        'LB', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (23, 'BS Number Received', 'BS Number Received', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'BS_NUMBER_FILLED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (22, 'SSF filled', 'Sample Submission filled', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'SSF_FILLED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (21, 'SCF filled', 'Sample Collection filled', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'SCF_FILLED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (20, 'checklist filled', 'Inspection checklist filled', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'CHECKLIST_FILLED', 'CF', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (19, 'Inspection Ended', 'Inspection Ended', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'CHECKLIST_COMPLETED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (18, 'Inspection Started', 'Inspection Started', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'CHECKLIST_STARTED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (17, 'KRA Verification Schedule Received', 'KRA Verification Schedule Received', 1, null, null, null, null, null,
        null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null,
        'KRA_VERIFICATION_REQUEST', 'TG', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (16, 'Awaiting KRA Verification Schedule', 'Awaiting KRA Verification Schedule', 1, null, null, null, null, null,
        null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null,
        'KRA_VERIFICATION', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (15, 'Targeting Approved', 'Targeting Approved', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'TARGET_APPROVED', 'QY', 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (14, 'Awaiting targeting Approval', 'Awaiting targeting Approval', 1, null, null, null, null, null, null, null,
        null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'TARGET_REQUEST', 'TG',
        0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (13, 'Reassigned IO', 'Reassigned IO', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'IO_REASSIGNED', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (12, 'Assigned IO', 'Assigned IO', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'IO_ASSIGNED', null, 1, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (33, 'Ministry Report Uploaded', 'Ministry Report Uploaded', 1, null, null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'MINISTRY_UPLOAD', 'MU', 0,
        0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (44, 'Old Consignment', 'Old Consignment', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'OLD_CD', null, 1, 1, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (45, 'Submitted for rejection', 'Submitted for rejection', 1, null, null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'REJECT_REQUEST', 'RA', 0, 0,
        1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, CATEGORY,
                                 STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS, APPLICATION_STATUS)
VALUES (46, 'Submitted for approval', 'Submitted for approval', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'APPROVE_REQUEST', 'AR', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                 VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                 VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                 DELETED_ON, CATEGORY, STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS,
                                 APPLICATION_STATUS)
VALUES (47, 'Partial Payment Made', 'Partial Payment Made', 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null,
        'PARTIAL_PAYMENT_MADE', 'PP', 0, 0, 1);
INSERT INTO CFG_CD_STATUS_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                 VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                 VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                 DELETED_ON, CATEGORY, STATUS_CODE, MODIFICATION_ALLOWED, FINAL_STATUS,
                                 APPLICATION_STATUS)
VALUES (48, 'Payment Billed', 'Payment Billed', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, 'PAYMENT_BILLED', 'PB', 1,
        0, 1);
-- Consignment Targets
INSERT INTO CFG_CD_TRAGET_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (1, 'EXPORTER', 'Exported to be targeted', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_CD_TRAGET_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (2, 'IMPORTER', 'Importer to be targeted', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_CD_TRAGET_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (3, 'CONSIGNOR', 'Consignor to be targeted', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_CD_TRAGET_TYPES (ID, TYPE_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4,
                                 VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                                 CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (4, 'CONSIGNEE', 'Consignee to be targeted', 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
-- Ports of entry
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (1, 'ASV', 'Amboseli', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (2, 'BMQ', 'Bamburi', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (3, 'EDL', 'Eldoret', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (4, 'EYS', 'Eliye Springs', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (5, 'FER', 'Fergusons Gulf', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (6, 'GAS', 'Garissa', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (7, 'HOA', 'Hola', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (8, 'NBO', 'Jomo Kenyatta International Airport', 'A', 'KE', null, 1, null, null, null, null, null, null, null,
        null, null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (9, 'KLK', 'Kalokol', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (10, 'KEY', 'Kericho', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (11, 'KRV', 'Kerio Valley', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (12, 'ILU', 'Kilaguni', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (13, 'KIS', 'Kisumu', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (14, 'KEEMK', 'EMBAKASI', 'L', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (15, 'KEISB', 'ISEBANIA', 'L', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (16, 'KETVT', 'TAVETA', 'L', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (17, 'KERNA', 'NAIVASHA', 'L', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (18, 'KTL', 'Kitale', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (19, 'KIU', 'Kiunga', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (20, 'KWY', 'Kiwayu', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (21, 'LBN', 'Lake Baringo', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (22, 'LKU', 'Lake Rudolf', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (23, 'LAU', 'Lamu', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (24, 'LBK', 'Liboi', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (25, 'LOK', 'Lodwar', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (26, 'LKG', 'Lokichoggio', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (27, 'LOY', 'Loyangalani', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (28, 'MYD', 'Malindi', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (29, 'NDE', 'Mandera', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (30, 'MRE', 'Mara Lodges', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (31, 'RBT', 'Marsabit', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (32, 'MBA', 'Moi International Airport', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null,
        null, null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (33, 'OYL', 'Moyale', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (34, 'MUM', 'Mumias', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (35, 'NUU', 'Nakuru', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (36, 'NYK', 'Nanyuki', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (37, 'NYE', 'Nyeri', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (38, 'NZO', 'Nzoia', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (39, 'UAS', 'Samburu', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (40, 'UKA', 'Ukunda', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (41, 'WJR', 'Wajir', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (42, 'WIL', 'Wilson', 'A', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (43, 'KELKG', 'Lokichoggio', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (44, 'KELOY', 'Loyangalani', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (45, 'KEMAL', 'Malaba', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (46, 'KEMYD', 'Malindi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (47, 'KENDE', 'Mandera', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (48, 'KEMRE', 'Mara Lodges', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (49, 'KERBT', 'Marsabit', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (50, 'KEMBA', 'Mombasa', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (51, 'KEOYL', 'Moyale', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (52, 'KEMUM', 'Mumias', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (53, 'KENBO', 'Nairobi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (54, 'KENUU', 'Nakuru', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (55, 'KENYK', 'Nanyuki', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (56, 'KENYE', 'Nyeri', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (57, 'KENZO', 'Nzoia', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (58, 'KEUAS', 'Samburu', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (59, 'KESOT', 'Sotik', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (60, 'KETHK', 'Thika', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (61, 'KEUKA', 'Ukunda', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (62, 'KEWJR', 'Wajir', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (63, 'KEWIL', 'Wilson Apt/Nairobi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null,
        null, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (64, 'KEASV', 'Amboseli', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (65, 'KEBMQ', 'Bamburi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (66, 'KEEDL', 'Eldoret', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (67, 'KEEYS', 'Eliye Springs', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (68, 'KEEMB', 'Embakasi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (69, 'KEFER', 'Fergusons Gulf', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (70, 'KEGAS', 'Garissa', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (71, 'KEHOA', 'Hola', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (72, 'KEKLK', 'Kalokol', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (73, 'KEKEY', 'Kericho', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (74, 'KEKRV', 'Kerio Valley', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (75, 'KEILU', 'Kilaguni', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (76, 'KEKIL', 'Kilindini', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (77, 'KEKIS', 'Kisumu', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (78, 'KEKTL', 'Kitale', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (79, 'KEKIU', 'Kiunga', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (80, 'KEKWY', 'Kiwayu', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (81, 'KELBN', 'Lake Baringo', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (82, 'KELKU', 'Lake Rudolf', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null,
        'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (83, 'KELAU', 'LAMU', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (84, 'KELBK', 'Liboi', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (85, 'KELOK', 'Lodwar', 'S', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_KEBS_PORTS_TYPE_CODES (ID, PORT_CODE, PORT_NAME, MODE_TPT, COUNTRY_CODE, DESCRIPTION, STATUS,
                                       VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                       VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                       MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (86, 'KENMA', 'NAMANGA', 'R', 'KE', null, 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
-- Payment Methods
INSERT INTO CFG_PAYMENT_METHODS (ID, METHOD, TILL_OR_ACCOUNT_NUMBER, MPESA_MENU, LIPA_NA_MPESA, MPESA_BUY_GOODS,
                                 MPESA_TILL_NUMBER, ENTER_AMOUNT, WAIT_FOR_MESSAGE, CLICK_PAY_NOW, BANK_NAME,
                                 BANK_ACCOUNT_NAME, BANK_ACCOUNT_NUMBER, BANK_BRANCH, REFERENCE_NUMBER, STATUS,
                                 VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                 VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                 LAST_MODIFIED_BY, LAST_MODIFIED_ON, UPDATE_BY, UPDATED_ON, DELETE_BY, DELETED_ON,
                                 VERSION, CURRENCY_CODE, PAY_BILL_NO, MPESA_ACC_NO, VAT_NO, PIN_NO,
                                 BANK_ACCOUNT_KES_NUMBER, BANK_ACCOUNT_USD_NUMBER, BANK_CODE, BRANCH_CODE, SWIFT_CODE)
VALUES (1, 'MPESA', 'Till Number', '1. Go to M-Pesa menu', '2. Click on Lipa na M-Pesa',
        '3. Click on Buy Goods and Services ', '4. Enter till no 804700', '5. Enter amount',
        '6. Wait for the M-Pesa message', ' 7. Click Pay Now.', null, null, 'DN-209060', null,
        'Reference Number: 68525', 1, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, null, null, null, null, null, null, null, null, '804700', 'DN-209077', '0130253A',
        'PO51092837Y', null, null, null, null, null);
INSERT INTO CFG_PAYMENT_METHODS (ID, METHOD, TILL_OR_ACCOUNT_NUMBER, MPESA_MENU, LIPA_NA_MPESA, MPESA_BUY_GOODS,
                                 MPESA_TILL_NUMBER, ENTER_AMOUNT, WAIT_FOR_MESSAGE, CLICK_PAY_NOW, BANK_NAME,
                                 BANK_ACCOUNT_NAME, BANK_ACCOUNT_NUMBER, BANK_BRANCH, REFERENCE_NUMBER, STATUS,
                                 VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                 VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                 LAST_MODIFIED_BY, LAST_MODIFIED_ON, UPDATE_BY, UPDATED_ON, DELETE_BY, DELETED_ON,
                                 VERSION, CURRENCY_CODE, PAY_BILL_NO, MPESA_ACC_NO, VAT_NO, PIN_NO,
                                 BANK_ACCOUNT_KES_NUMBER, BANK_ACCOUNT_USD_NUMBER, BANK_CODE, BRANCH_CODE, SWIFT_CODE)
VALUES (2, 'Bank Transfer', 'Account Number', null, null, null, null, null, null, null, 'National Bank Of Kenya',
        'Kenya Burea of Standards', '0100-302-830-604', 'Harambee Avenue', null, 1, null, null, null, null, null, null,
        null, null, null, null, 'admin', CURRENT_TIMESTAMP, null, null, null, null, null, null, null, 'KSH', null, null,
        null, null, '01003-002-830-604', '02003-002-830-600', '12', '067', 'NBKEKENXXXX');
INSERT INTO CFG_PAYMENT_METHODS (ID, METHOD, TILL_OR_ACCOUNT_NUMBER, MPESA_MENU, LIPA_NA_MPESA, MPESA_BUY_GOODS,
                                 MPESA_TILL_NUMBER, ENTER_AMOUNT, WAIT_FOR_MESSAGE, CLICK_PAY_NOW, BANK_NAME,
                                 BANK_ACCOUNT_NAME, BANK_ACCOUNT_NUMBER, BANK_BRANCH, REFERENCE_NUMBER, STATUS,
                                 VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                 VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                 LAST_MODIFIED_BY, LAST_MODIFIED_ON, UPDATE_BY, UPDATED_ON, DELETE_BY, DELETED_ON,
                                 VERSION, CURRENCY_CODE, PAY_BILL_NO, MPESA_ACC_NO, VAT_NO, PIN_NO,
                                 BANK_ACCOUNT_KES_NUMBER, BANK_ACCOUNT_USD_NUMBER, BANK_CODE, BRANCH_CODE, SWIFT_CODE)
VALUES (3, 'Bank Transfer', 'Account Number', null, null, null, null, null, null, null, 'Co-operative Bank Of Kenya',
        'Kenya Burea of Standards', '0100-302-830-604', 'Mombasa Road Branch', null, 1, null, null, null, null, null,
        null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, null, null, null, null, null, null, null, 'KSH', null,
        null, null, null, '01-141-504-454-700', '02-120-504-454-700', '1100', '11127', 'KCOOKENA');
INSERT INTO CFG_PAYMENT_METHODS (ID, METHOD, TILL_OR_ACCOUNT_NUMBER, MPESA_MENU, LIPA_NA_MPESA, MPESA_BUY_GOODS,
                                 MPESA_TILL_NUMBER, ENTER_AMOUNT, WAIT_FOR_MESSAGE, CLICK_PAY_NOW, BANK_NAME,
                                 BANK_ACCOUNT_NAME, BANK_ACCOUNT_NUMBER, BANK_BRANCH, REFERENCE_NUMBER, STATUS,
                                 VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                 VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY, CREATED_ON,
                                 LAST_MODIFIED_BY, LAST_MODIFIED_ON, UPDATE_BY, UPDATED_ON, DELETE_BY, DELETED_ON,
                                 VERSION, CURRENCY_CODE, PAY_BILL_NO, MPESA_ACC_NO, VAT_NO, PIN_NO,
                                 BANK_ACCOUNT_KES_NUMBER, BANK_ACCOUNT_USD_NUMBER, BANK_CODE, BRANCH_CODE, SWIFT_CODE)
VALUES (4, 'Bank Transfer', 'Account Number', null, null, null, null, null, null, null,
        'Kenya Commercial Bank Of Kenya', 'Kenya Burea of Standards', '0100-302-830-604', 'Moi Avenue', null, 1, null,
        null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP, null, null, null, null, null,
        null, null, 'KSH', null, null, null, null, '110-761-8355', null, '01', '100', 'KCBLKNEX');
-- Risk Types
INSERT INTO CFG_RISK_TYPES (ID, TYPE_NAME, COLOUR, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                            CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (1, 'High Risk', 'Red', 'Highly Risky', '1', null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
INSERT INTO CFG_RISK_TYPES (ID, TYPE_NAME, COLOUR, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                            VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10,
                            CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON)
VALUES (2, 'Not Risk', 'Green', 'Not Risky', '1', null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null);
-- SET FOREIGN_KEY_CHECKS=1;