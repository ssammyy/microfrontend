/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */
***************************Table USED IN QA*****************************************
select * from DAT_KEBS_PERMIT_TRANSACTION
-- where id = 61
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc; 1522

SELECT * FROM
    DAT_KEBS_USER_VERIFICATION_TOKEN
WHERE USER_ID = 1464
ORDER BY ID DESC;
-- /****************************
alter table DAT_KEBS_PERMIT_TRANSACTION
    add PERMIT_EXPIRED_STATUS NUMBER(2)
/
-- /***************************/

alter table CFG_PERMIT_TYPES
    add SCHEME_GENERATE NUMBER(2)
/

alter table DAT_KEBS_PERMIT_TRANSACTION
    add RM_ID NUMBER REFERENCES DAT_KEBS_USERS(ID)
/

select * from CFG_TURNOVER_RATES
-- where id = 2
order by id desc;

select * from CFG_KEBS_PERMIT_PAYMENT_UNITS
-- where id = 2
order by id desc;

select * from DAT_KEBS_COMPANY_PROFILE
-- where id = 2
order by id desc;

select * from DAT_KEBS_INVOICE
-- where id = 2
-- where INVOICE_NUMBER = 'DM#20210426B8C'
order by id desc;

alter table DAT_KEBS_QA_PRODUCT modify AVAILABLE NUMBER(2)/

alter table DAT_KEBS_COMPANY_PROFILE modify YEARLY_TURNOVER NUMBER
/


alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1A VARCHAR2(200)
/alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1B VARCHAR2(200)
/alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1C VARCHAR2(200)
/alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1D VARCHAR2(200)
/alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1E VARCHAR2(200)
    /

select * from DAT_KEBS_QA_STA3
-- where id = 43
order by id desc;

select * from LOG_SERVICE_REQUESTS
-- where id = 43
order by id desc;

select * from DAT_KEBS_MANUFACTURE_PLANT_DETAILS
where id = 81
order by id desc;


select * from dat_kebs_qa_sta10
-- where id = 43
order by id desc;

alter table DAT_KEBS_QA_PRODUCT
    add STA10_ID NUMBER REFERENCES DAT_KEBS_QA_STA10(ID)
/

alter table DAT_KEBS_QA_PRODUCT rename column DESCRIPTION to DESCRIPTIONS
/

alter table DAT_KEBS_QA_PRODUCT rename column DEPARTMENT to AVAILABLE
/
alter table DAT_KEBS_PERMIT_TRANSACTION
    add  PRODUCT_STANDARD                          NUMBER
        references CFG_SAMPLE_STANDARDS(ID)
;/
select  sequuence DAT_KEBS_QA_PRODUCT_SEQ




select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
where TOKEN =
;
select * from CFG_ROLES_PRIVILEGES
order by id desc;

select * from CFG_USER_ROLES
-- where  = 1393
order by id desc;

select * from CFG_USER_PRIVILEGES
-- where id like '%18%'
order by id desc;--MS_MP_MODIFY MS_MP_READ AUTHORITIES_WRITE 29
select *
from CFG_ROLES_PRIVILEGES
-- where ROLES_ID = -1
order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from CFG_USER_ROLES_ASSIGNMENTS
where USER_ID = 1393
order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
order by id desc;--MS_MP_MODIFY MS_MP_READ

select * from DAT_KEBS_USERS
-- where USER_TYPE = 5
where EMAIL = '254safaris@gmail.com'
order by id desc;

select *
from DAT_KEBS_USER_PROFILES
-- where USER_ID = 1403
where email = '254safaris@gmail.com'
order by id desc;

alter table DAT_KEBS_USERS
    add TYPE_OF_USER NUMBER(2)
/


select *
from CFG_USER_TYPES
-- where STATUS = 1
order by ID desc;

select *
from dat_user_requests
-- where ID = 127
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from cfg_user_request_types
-- where ID = 127
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from CFG_SERVICE_MAPS
where ID = 208
-- where ID = 199
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from CFG_NOTIFICATIONS--122
where SERVICE_MAP_ID = 127
--     where UUID = '1b474c60-c7f4-4ccb-9cce-efca7ead8439'
order by ID desc;


select *
from USER_CONSTRAINTS
where TABLE_NAME = 'DAT_KEBS_USER_VERIFICATION_TOKEN'
-- where CONSTRAINT_NAME = 'DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS'
;

select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
where TOKEN = '21790a243e60d20'
-- where USER_ID = 1391
;

select *
from LOG_JWT_TOKENS_REGISTRY
where USER_NAME = '254safaris@gmail.com'
;

SELECT DISTINCT CUP.*
FROM CFG_ROLES_PRIVILEGES rp,
     CFG_USER_ROLES cur,
     CFG_USER_PRIVILEGES cup
WHERE CUP.ID = rp.PRIVILEGE_ID
  AND CUR.ID = rp.ROLES_ID
  AND rp.ROLES_ID IN (-1)
  and rp.STATUS = 1;

drop index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS;

create unique index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_ID_STATUS on DAT_KEBS_USER_VERIFICATION_TOKEN (USER_ID, STATUS)

 *******************************************Created Tables USED IN DI********************************


create table dat_kebs_qa_sta10
(
    ID                       NUMBER  primary key,
    permit_id               NUMBER references DAT_KEBS_PERMIT_TRANSACTION(ID),
    firm_name  VARCHAR2(500),
    status_company_business_registration  VARCHAR2(500),
    owner_name_proprietor_director  VARCHAR2(500),
    postal_address  VARCHAR2(500),
    telephone  VARCHAR2(500),
    contact_person VARCHAR2(500),
    email_address  VARCHAR2(500),
    physical_Location_map  VARCHAR2(500),
    region             NUMBER references CFG_KEBS_REGIONS (ID),
    county             NUMBER references CFG_KEBS_COUNTIES (ID),
    town               NUMBER references CFG_KEBS_TOWNS (ID),
    total_number_personnel NUMBER,
    total_number_female NUMBER,
    total_number_male NUMBER,
    total_number_permanent_employees NUMBER,
    total_number_casual_employees NUMBER,
    average_volume_production_month NUMBER,
    handled_manufacturing_process_raw_materials VARCHAR2(500),
    handled_manufacturing_process_inprocess_products VARCHAR2(500),
    handled_manufacturing_process_final_product VARCHAR2(500),
    strategy_inplace_recalling_products VARCHAR2(500),
    state_facility_conditions_raw_materials VARCHAR2(500),
    state_facility_conditions_end_product VARCHAR2(500),
    testing_facilities_exist_specify_equipment VARCHAR2(500),
    testing_facilities_exist_state_parameters_tested VARCHAR2(500),
    testing_facilities_specify_parameters_tested VARCHAR2(500),
    calibration_equipment_last_calibrated VARCHAR2(500),
    handling_consumer_complaints VARCHAR2(500),
    company_representative VARCHAR2(500),
    application_date Date,
    state_adequacy_construction_facility VARCHAR2(500),
    state_adequacy_plant_layout VARCHAR2(500),
    state_adequacy_suitability_location VARCHAR2(500),
    state_adequacy_suitability_equipment VARCHAR2(500),
    hygiene_general_plant_describe VARCHAR2(500),
    staff_provided_necessary_protective_clothing_describe VARCHAR2(500),
    complied_relevant_statutory_regulatory_requirements_describe VARCHAR2(500),
    product_labeled_marked_specify VARCHAR2(500),
    labels_marks_comply_requirements_relevant_standard VARCHAR2(500),
    processes_products_impact_negatively_environment VARCHAR2(500),
    specify_mitigation_measures_undertaken VARCHAR2(500),
    recommendations_conclusions_areas_improvement VARCHAR2(500),
    assessors_recommendation_certification VARCHAR2(500),
    official_fill_date DATE,
    DESCRIPTION              VARCHAR2(200),
    status                   NUMBER(2),
    var_field_1              VARCHAR2(350 CHAR),
    var_field_2              VARCHAR2(350 CHAR),
    var_field_3              VARCHAR2(350 CHAR),
    var_field_4              VARCHAR2(350 CHAR),
    var_field_5              VARCHAR2(350 CHAR),
    var_field_6              VARCHAR2(350 CHAR),
    var_field_7              VARCHAR2(350 CHAR),
    var_field_8              VARCHAR2(350 CHAR),
    var_field_9              VARCHAR2(350 CHAR),
    var_field_10             VARCHAR2(350 CHAR),
    created_by               VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by              VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_sta10_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_qa_sta10_seq_trg
    before
        insert
    on dat_kebs_qa_sta10
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sta10_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sta10_idx on dat_kebs_qa_sta10 (status, permit_id) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_qa_scheme_for_supervision
(
    id               NUMBER PRIMARY KEY,
    PERMIT_ID      NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    process_flow_raw_materials_intake          VARCHAR2(200),
    operations_raw_materials_intake          VARCHAR2(200),
    quality_checks_raw_materials_intake          VARCHAR2(200),
    frequency_raw_materials_intake          VARCHAR2(200),
    records_raw_materials_intake          VARCHAR2(200),

    process_flow_in_process          VARCHAR2(200),
    operations_in_process          VARCHAR2(200),
    quality_checks_in_process          VARCHAR2(200),
    frequency_in_process          VARCHAR2(200),
    records_in_process          VARCHAR2(200),

    process_flow_final_product          VARCHAR2(200),
    operations_final_product           VARCHAR2(200),
    quality_checks_final_product           VARCHAR2(200),
    frequency_final_product           VARCHAR2(200),
    records_final_product           VARCHAR2(200),

    process_flow_packaging          VARCHAR2(200),
    operations_packaging         VARCHAR2(200),
    quality_checks_packaging          VARCHAR2(200),
    frequency_packaging          VARCHAR2(200),
    records_packaging          VARCHAR2(200),

    process_flow_storage          VARCHAR2(200),
    operations_storage          VARCHAR2(200),
    quality_checks_storage          VARCHAR2(200),
    frequency_storage          VARCHAR2(200),
    records_storage          VARCHAR2(200),

    process_flow_dispatch          VARCHAR2(200),
    operations_dispatch          VARCHAR2(200),
    quality_checks_dispatch          VARCHAR2(200),
    frequency_dispatch          VARCHAR2(200),
    records_dispatch          VARCHAR2(200),
    accepted_rejected_status          VARCHAR2(200),
    accepted_rejected_date          DATE,
    DESCRIPTION      VARCHAR2(200),
    status           NUMBER(2),
    var_field_1      VARCHAR2(350 CHAR),
    var_field_2      VARCHAR2(350 CHAR),
    var_field_3      VARCHAR2(350 CHAR),
    var_field_4      VARCHAR2(350 CHAR),
    var_field_5      VARCHAR2(350 CHAR),
    var_field_6      VARCHAR2(350 CHAR),
    var_field_7      VARCHAR2(350 CHAR),
    var_field_8      VARCHAR2(350 CHAR),
    var_field_9      VARCHAR2(350 CHAR),
    var_field_10     VARCHAR2(350 CHAR),
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on       TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_scheme_for_supervision_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_scheme_for_supervision_seq_trg
    before
        insert
    on dat_kebs_qa_scheme_for_supervision
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_scheme_for_supervision_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_scheme_for_supervision_idx on dat_kebs_qa_scheme_for_supervision (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_qa_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    DESCRIPTION      VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    PERMIT_ID      NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    status           NUMBER(2),
    var_field_1      VARCHAR2(350 CHAR),
    var_field_2      VARCHAR2(350 CHAR),
    var_field_3      VARCHAR2(350 CHAR),
    var_field_4      VARCHAR2(350 CHAR),
    var_field_5      VARCHAR2(350 CHAR),
    var_field_6      VARCHAR2(350 CHAR),
    var_field_7      VARCHAR2(350 CHAR),
    var_field_8      VARCHAR2(350 CHAR),
    var_field_9      VARCHAR2(350 CHAR),
    var_field_10     VARCHAR2(350 CHAR),
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on       TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_uploads_seq_trg
    before
        insert
    on dat_kebs_qa_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_uploads_idx on dat_kebs_qa_uploads (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_qa_sample_collection (
    id               NUMBER PRIMARY KEY,
    PERMIT_ID      NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    NAME_OF_MANUFACTURE         VARCHAR2(200),
    ADDRESS_OF_MANUFACTURE         VARCHAR2(200),
    NAME_OF_PRODUCT         VARCHAR2(200),
    BRAND_NAME         VARCHAR2(200),
    BATCH_NO         VARCHAR2(200),
    BATCH_SIZE         VARCHAR2(200),
    SAMPLE_SIZE         VARCHAR2(200),
    SAMPLING_METHOD         VARCHAR2(200),
    REASON_FOR_COLLECTING_SAMPLE        VARCHAR2(200),
    ANY_REMARKS        VARCHAR2(200),
    NAME_OF_OFFICER        VARCHAR2(200),
    OFFICER_DESIGNATION       VARCHAR2(200),
    OFFICER_DATE       DATE,
    NAME_OF_WITNESS        VARCHAR2(200),
    WITNESS_DESIGNATION       VARCHAR2(200),
    WITNESS_DATE       DATE,
    DESCRIPTION      VARCHAR2(200),
    status           NUMBER(2),
    var_field_1      VARCHAR2(350 CHAR),
    var_field_2      VARCHAR2(350 CHAR),
    var_field_3      VARCHAR2(350 CHAR),
    var_field_4      VARCHAR2(350 CHAR),
    var_field_5      VARCHAR2(350 CHAR),
    var_field_6      VARCHAR2(350 CHAR),
    var_field_7      VARCHAR2(350 CHAR),
    var_field_8      VARCHAR2(350 CHAR),
    var_field_9      VARCHAR2(350 CHAR),
    var_field_10     VARCHAR2(350 CHAR),
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on       TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_sample_collection_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_sample_collection_seq_trg
    before
        insert
    on dat_kebs_qa_sample_collection
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sample_collection_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sample_collection_idx on dat_kebs_qa_sample_collection (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_qa_sta3
(
    ID                       NUMBER  primary key,
    permit_id               NUMBER references DAT_KEBS_PERMIT_TRANSACTION(ID),
    produce_Orders_or_Stock  VARCHAR2(500),
    issue_work_order_or_equivalent  VARCHAR2(500),
    identify_batch_as_separate  VARCHAR2(500),
    products_containers_carry_works_order  VARCHAR2(500),
    isolated_case_doubtful_quality  VARCHAR2(500),
    head_qa_qualifications_training  VARCHAR2(500),
    reporting_to  VARCHAR2(500),
    separate_qcid  VARCHAR2(500),
    tests_relevant_standard VARCHAR2(500),
    spo_coming_materials VARCHAR2(500),
    spo_process_operations VARCHAR2(500),
    spo_final_products VARCHAR2(500),
    monitored_qcs VARCHAR2(500),
    qaudit_checks_carried VARCHAR2(500),
    information_qcso VARCHAR2(500),
    main_materials_purchased_specification VARCHAR2(500),
    adopted_receipt_materials VARCHAR2(500),
    storage_facilities_exist VARCHAR2(500),
    steps_manufacture VARCHAR2(500),
    maintenance_system VARCHAR2(500),
    qcs_supplement VARCHAR2(500),
    qm_instructions VARCHAR2(500),
    test_equipment_used VARCHAR2(500),
    indicate_external_arrangement VARCHAR2(500),
    level_defectives_found VARCHAR2(500),
    level_claims_complaints VARCHAR2(500),
    independent_tests  VARCHAR2(500),
    indicate_stage_manufacture  VARCHAR2(500),
    DESCRIPTION              VARCHAR2(200),
    status                   NUMBER(2),
    var_field_1              VARCHAR2(350 CHAR),
    var_field_2              VARCHAR2(350 CHAR),
    var_field_3              VARCHAR2(350 CHAR),
    var_field_4              VARCHAR2(350 CHAR),
    var_field_5              VARCHAR2(350 CHAR),
    var_field_6              VARCHAR2(350 CHAR),
    var_field_7              VARCHAR2(350 CHAR),
    var_field_8              VARCHAR2(350 CHAR),
    var_field_9              VARCHAR2(350 CHAR),
    var_field_10             VARCHAR2(350 CHAR),
    created_by               VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by              VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_sta3_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_qa_sta3_seq_trg
    before
        insert
    on dat_kebs_qa_sta3
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sta3_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sta3_idx on dat_kebs_qa_sta3 (status, permit_id) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_qa_smark_fmark
(
    ID                       NUMBER  primary key,
    SMARK_ID            NUMBER references DAT_KEBS_PERMIT_TRANSACTION(ID),
    FMARK_ID            NUMBER references DAT_KEBS_PERMIT_TRANSACTION(ID),
    DESCRIPTION              VARCHAR2(200),
    status                   NUMBER(2),
    var_field_1              VARCHAR2(350 CHAR),
    var_field_2              VARCHAR2(350 CHAR),
    var_field_3              VARCHAR2(350 CHAR),
    var_field_4              VARCHAR2(350 CHAR),
    var_field_5              VARCHAR2(350 CHAR),
    var_field_6              VARCHAR2(350 CHAR),
    var_field_7              VARCHAR2(350 CHAR),
    var_field_8              VARCHAR2(350 CHAR),
    var_field_9              VARCHAR2(350 CHAR),
    var_field_10             VARCHAR2(350 CHAR),
    created_by               VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by              VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_smark_fmark_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_qa_smark_fmark_seq_trg
    before
        insert
    on dat_kebs_qa_smark_fmark
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_smark_fmark_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_smark_fmark_idx on dat_kebs_qa_smark_fmark (status, SMARK_ID,FMARK_ID) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_qa_manufacturing_process
(
    ID                       NUMBER  primary key,
    TA10_ID NUMBER REFERENCES DAT_KEBS_QA_STA10(ID),
    process_flow_of_production  VARCHAR2(500),
    operations  VARCHAR2(500),
    critical_process_parameters_monitored  VARCHAR2(500),
    frequency  VARCHAR2(500),
    process_monitoring_records  VARCHAR2(500),
    DESCRIPTION              VARCHAR2(200),
    status                   NUMBER(2),
    var_field_1              VARCHAR2(350 CHAR),
    var_field_2              VARCHAR2(350 CHAR),
    var_field_3              VARCHAR2(350 CHAR),
    var_field_4              VARCHAR2(350 CHAR),
    var_field_5              VARCHAR2(350 CHAR),
    var_field_6              VARCHAR2(350 CHAR),
    var_field_7              VARCHAR2(350 CHAR),
    var_field_8              VARCHAR2(350 CHAR),
    var_field_9              VARCHAR2(350 CHAR),
    var_field_10             VARCHAR2(350 CHAR),
    created_by               VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by              VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_manufacturing_process_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_qa_manufacturing_process_seq_trg
    before
        insert
    on dat_kebs_qa_manufacturing_process
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_manufacturing_process_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_manufacturing_process_idx on dat_kebs_qa_manufacturing_process (status, STA10_ID) TABLESPACE qaimssdb_idx;
/

create sequence DAT_KEBS_QA_MACHINE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_QA_MACHINE_seq_trg
    before
        insert
    on DAT_KEBS_QA_MACHINE
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_QA_MACHINE_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_QA_MACHINE_idx on DAT_KEBS_QA_MACHINE (status, STA10_ID) TABLESPACE qaimssdb_idx;
/

create sequence DAT_KEBS_QA_RAW_MATERIAL_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_QA_RAW_MATERIAL_seq_trg
    before
        insert
    on DAT_KEBS_QA_RAW_MATERIAL
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_QA_RAW_MATERIAL_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_QA_RAW_MATERIAL_idx on DAT_KEBS_QA_RAW_MATERIAL (status, STA10_ID) TABLESPACE qaimssdb_idx;
/


create sequence CFG_TURNOVER_RATES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_TURNOVER_RATES_seq_trg
    before
        insert
    on CFG_TURNOVER_RATES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_TURNOVER_RATES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_TURNOVER_RATES_idx on CFG_TURNOVER_RATES (status, FIRM_TYPE) TABLESPACE qaimssdb_idx;
/