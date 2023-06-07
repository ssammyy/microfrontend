SET FOREIGN_KEY_CHECKS=0;
truncate table CFG_KEBS_PVOC_PARTNERS_REGION CASCADE;
truncate table CFG_KEBS_PVOC_PARTNERS_COUNTRIES CASCADE;
--- Partner regions
INSERT INTO CFG_KEBS_PVOC_PARTNERS_REGION (ID, REGION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                           VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                           VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                           DELETED_ON, COUNTRY_ID)
VALUES (3, 'Central Europe', null, '1', null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_REGION (ID, REGION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                           VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                           VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                           DELETED_ON, COUNTRY_ID)
VALUES (1, 'Americas ', null, '1', null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_REGION (ID, REGION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                           VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                           VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                           DELETED_ON, COUNTRY_ID)
VALUES (2, 'UAE', null, '1', null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_REGION (ID, REGION_NAME, DESCRIPTION, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                                           VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                                           VAR_FIELD_10, CREATED_BY, CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY,
                                           DELETED_ON, COUNTRY_ID)
VALUES (4, 'Far East', null, '1', null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
-- Partner Countries
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (1, 'United Arab Emirates ', null, 1, 2, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (2, 'USA', null, 1, 1, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (3, 'Canada', null, 1, 1, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (4, 'Germany', null, 1, 3, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (5, 'France', null, 1, 3, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (6, 'Austria', null, 1, 3, null, null, null, null, null, null, null, null, null, null, 'admin',
        CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, 'admin', null, null);
INSERT INTO CFG_KEBS_PVOC_PARTNERS_COUNTRIES (ID, COUNTRY_NAME, DESCRIPTION, STATUS, REGION_ID, VAR_FIELD_1,
                                              VAR_FIELD_2, VAR_FIELD_3, VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6,
                                              VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9, VAR_FIELD_10, CREATED_BY,
                                              CREATED_ON, MODIFIED_BY, MODIFIED_ON, DELETE_BY, DELETED_ON, ABBREVIATION)
VALUES (7, 'Japan', null, 1, 4, null, null, null, null, null, null, null, null, null, null, 'admin', CURRENT_TIMESTAMP,
        'admin', CURRENT_TIMESTAMP, 'admin', null, 'JP');

SET FOREIGN_KEY_CHECKS=1;