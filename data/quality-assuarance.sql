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
select *
from DAT_KEBS_PERMIT_TRANSACTION
-- where id = 503

-- where ATTACHED_PLANT_ID = 203
-- where PERMIT_NUMBER = 'DM#0954A'
where PERMIT_REF_NUMBER = 'REFSM#202106098701C'
order by id desc;

select *
from dat_kebs_qa_batch_invoice
-- where id = 503

-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;

UPDATE DAT_KEBS_PERMIT_TRANSACTION
SET RESUBMIT_APPLICATION_STATUS = 10,
    SEND_FOR_PCM_REVIEW         = null,
    PCM_APPROVAL_STATUS         = null
WHERE id = 503;

select *
from CFG_PERMIT_TYPES
-- where id = 281

-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522

select *
from CFG_TURNOVER_RATES
-- where id = 350
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522

select *
from cfg_kebs_qa_process_status
-- where id = 350
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;

select *
from DAT_KEBS_QA_STA10
-- where id = 350
where permit_id = 482
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522

select *
from DAT_KEBS_SCHEMES_OF_SUPERVISION
-- where id = 350
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522


select *
from DAT_KEBS_QA_SAMPLE_SUBMISSION
where BS_NUMBER = 'BS202117903'
-- where PERMIT_ID = 362
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522
select *
from DAT_KEBS_QA_SAMPLE_LAB_TEST_PARAMETERS
where ORDER_ID = 'BS202117903'
-- where PERMIT_ID = 362
-- where PERMIT_NUMBER = 'DM#0954A'
order by id desc;
1522

SELECT *
FROM DAT_KEBS_USER_VERIFICATION_TOKEN
WHERE USER_ID = 1464
ORDER BY ID DESC;
-- /****************************
alter table DAT_KEBS_PERMIT_TRANSACTION
    add PERMIT_EXPIRED_STATUS NUMBER(2)
/
-- /***************************/

alter table DAT_KEBS_COMPANY_PROFILE
    add FIRM_CATEGORY NUMBER REFERENCES CFG_TURNOVER_RATES (ID)
/

alter table CFG_PERMIT_TYPES
    add SCHEME_GENERATE NUMBER(2)
/

alter table DAT_KEBS_PERMIT_TRANSACTION
    add COC_ID NUMBER REFERENCES DAT_KEBS_QA_UPLOADS (ID)
/

select *
from DAT_KEBS_QA_UPLOADS
-- where id = 2
order by id desc;

select * from CFG_PRODUCTS
-- where id = 2
order by id desc;

select * from CFG_TURNOVER_RATES
-- where id = 2
order by id desc;

select * from CFG_KEBS_PERMIT_PAYMENT_UNITS
-- where id = 2
order by id desc;

select * from DAT_KEBS_COMPANY_PROFILE
-- where REGISTRATION_NUMBER = 'PVT-9XUZXZB'
where  USER_ID= 1765
-- where id = 2
order by id desc;



select * from DAT_KEBS_COMPANY_PROFILE_DIRECTORS
-- where REGISTRATION_NUMBER = 'PVT-9XUZXZB'
where  COMPANY_PROFILE_ID= 142
-- where id = 2
order by id desc;

select * from LOG_SL2_PAYMENTS_HEADER;

select * from DAT_KEBS_INVOICE
-- where id = 2
-- where PERMIT_ID = 285
order by id desc;PVT-KAUQ22K

select * from DAT_KEBS_COMPANY_PROFILE
-- where id = 2
-- where REGISTRATION_NUMBER = 'PVT-V7URGL2'
order by id desc;

alter table DAT_KEBS_QA_PRODUCT modify AVAILABLE NUMBER(2)/

alter table DAT_KEBS_COMPANY_PROFILE modify YEARLY_TURNOVER NUMBER
/

alter table DAT_KEBS_PERMIT_TRANSACTION
    add END_PRODUCTION_REQUEST_APPROVAL VARCHAR2(200)
/

alter table DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
    add SUPERVISOR_FILLED_STATUS NUMBER(2)
/

alter table DAT_KEBS_PERMIT_TRANSACTION
    add RESUBMIT_REMARKS VARCHAR2(200)
/


alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1A VARCHAR2(200)
/alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1B VARCHAR2(200)
/
alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1C VARCHAR2(200)
/
alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1D VARCHAR2(200)
/
alter table DAT_KEBS_QA_STA10
    add PRODUCT_LABELED_MARKED_SPECIFY_1E VARCHAR2(200)
/

alter table DAT_KEBS_QA_WORKPLAN rename column PERMITS to PERMIT_NUMBER
/

alter table DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
    add FILLED_QPSMS_STATUS NUMBER(2)
/

alter table DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
    add FILLED_INSPECTION_TESTING_STATUS NUMBER(2)
/
alter table DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
    add FILLED_STANDARDIZATION_MARK_SCHEME_STATUS NUMBER(2)
/
alter table DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
    add FILLED_HACCP_IMPLEMENTATION_STATUS NUMBER(2)
/
alter table DAT_KEBS_QA_UPLOADS
    add STA10_STATUS NUMBER(2)
/

alter table DAT_KEBS_QA_WORKPLAN
    modify PERMIT_NUMBER VARCHAR2(200)
/

select *
from DAT_KEBS_QA_SCHEME_FOR_SUPERVISION
-- where id = 43
order by id desc;

select *
from DAT_KEBS_QA_INSPECTION_TECHNICAL
-- where id = 43
where PERMIT_ID = 462
order by id desc;

select *
from DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION
-- where id = 43
where PERMIT_ID = 462
order by id desc;

select *
from DAT_KEBS_QA_INSPECTION_HACCP_IMPLEMENTATION
-- where id = 43
where PERMIT_ID = 462
order by id desc;

select *
from DAT_KEBS_QA_STA3
-- where id = 43
order by id desc;

select *
from LOG_SERVICE_REQUESTS
-- where id = 43
order by id desc;

select *
from DAT_KEBS_MANUFACTURE_PLANT_DETAILS
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

select * from DAT_KEBS_QA_SAMPLE_SUBMISSION
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
-- where EMAIL = '254safaris@gmail.com'
where id = 1785
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


create table dat_kebs_qa_inspection_TECHNICAL
(
    id                                                    NUMBER PRIMARY KEY,
    PERMIT_ID                                             NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    QUALITY_PROCEDURE                                     VARCHAR2(200),
    QUALITY_PROCEDURE_remarks                             VARCHAR2(200),
    AVAILABILITY_PRODUCT_STANDARDS_CODES_PRACTICE         VARCHAR2(200),
    AVAILABILITY_PRODUCT_STANDARDS_CODES_PRACTICE_remarks VARCHAR2(200),
    QUALITY_MANAGEMENT_SYSTEMS                            VARCHAR2(200),
    QUALITY_MANAGEMENT_SYSTEMS_remarks                    VARCHAR2(200),
    haccp_see_annex_ii                                    VARCHAR2(200),
    haccp_see_annex_ii_remarks                            VARCHAR2(200),
    testing_facility                                      VARCHAR2(200),
    testing_facility_remarks                              VARCHAR2(200),
    quality_control_personnel_qualifications              VARCHAR2(200),
    quality_control_personnel_qualifications_remarks      VARCHAR2(200),
    equipment_calibration                                 VARCHAR2(200),
    equipment_calibration_remarks                         VARCHAR2(200),
    quality_records                                       VARCHAR2(200),
    quality_records_remarks                               VARCHAR2(200),
    product_labeling_identification                       VARCHAR2(200),
    product_labeling_identification_remarks               VARCHAR2(200),
    Validity_SMark_permit                                 VARCHAR2(200),
    Validity_SMark_permit_remarks                         VARCHAR2(200),
    use_the_smark                                         VARCHAR2(200),
    use_the_smark_remarks                                 VARCHAR2(200),
    changes_affecting_product_certification               VARCHAR2(200),
    changes_affecting_product_certification_remarks       VARCHAR2(200),
    changes_been_communicated_kebs                        VARCHAR2(200),
    changes_been_communicated_kebs_remarks                VARCHAR2(200),
    Samples_drawn                                         VARCHAR2(200),
    Samples_drawn_remarks                                 VARCHAR2(200),
    DESCRIPTION                                           VARCHAR2(200),
    STATUS                                                NUMBER(2),
    var_field_1                                           VARCHAR2(350 CHAR),
    var_field_2                                           VARCHAR2(350 CHAR),
    var_field_3                                           VARCHAR2(350 CHAR),
    var_field_4                                           VARCHAR2(350 CHAR),
    var_field_5                                           VARCHAR2(350 CHAR),
    var_field_6                                           VARCHAR2(350 CHAR),
    var_field_7                                           VARCHAR2(350 CHAR),
    var_field_8                                           VARCHAR2(350 CHAR),
    var_field_9                                           VARCHAR2(350 CHAR),
    var_field_10                                          VARCHAR2(350 CHAR),
    created_by                                            VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                                            TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                                           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                                           TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                                             VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                                            TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_inspection_TECHNICAL_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_inspection_TECHNICAL_seq_trg
    before
        insert
    on dat_kebs_qa_inspection_TECHNICAL
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_inspection_TECHNICAL_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

/
create index dat_kebs_qa_inspection_TECHNICAL_idx on dat_kebs_qa_inspection_TECHNICAL (PERMIT_ID, status) TABLESPACE qaimssdb_idx;

create table dat_kebs_qa_inspection_haccp_implementation
(
    id                                               NUMBER PRIMARY KEY,
    PERMIT_ID                                        NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    Design_facilities_construction_layout            VARCHAR2(200),
    Design_facilities_construction_layout_remarks    VARCHAR2(200),
    Control_operations                               VARCHAR2(200),
    Control_operations_REMARKS                       VARCHAR2(200),
    Maintenance_sanitation_cleaning_programs         VARCHAR2(200),
    Maintenance_sanitation_cleaning_programs_REMARKS VARCHAR2(200),
    Personnel_hygiene                                VARCHAR2(200),
    Personnel_hygiene_REMARKS                        VARCHAR2(200),
    Transportation_conveyance                        VARCHAR2(200),
    Transportation_conveyance_REMARKS                VARCHAR2(200),
    Product_information_labelling                    VARCHAR2(200),
    Product_information_labelling_REMARKS            VARCHAR2(200),
    Training_management                              VARCHAR2(200),
    Training_management_REMARKS                      VARCHAR2(200),
    appropriate_sector_hygiene_practice              VARCHAR2(200),
    appropriate_sector_hygiene_practice_remarks      VARCHAR2(200),
    Establishment_HACCP_Plan                         VARCHAR2(200),
    Establishment_HACCP_Plan_remarks                 VARCHAR2(200),
    Product_flow_diagram                             VARCHAR2(200),
    Product_flow_diagram_remarks                     VARCHAR2(200),
    Evidence_Hazard_Analysis                         VARCHAR2(200),
    Evidence_Hazard_Analysis_remarks                 VARCHAR2(200),
    Establishment_Critical_Control_Points            VARCHAR2(200),
    Establishment_Critical_Control_Points_remarks    VARCHAR2(200),
    Establishment_Monitoring_control                 VARCHAR2(200),
    Establishment_Monitoring_control_remarks         VARCHAR2(200),
    Evidence_Corrective_Actions                      VARCHAR2(200),
    Evidence_Corrective_Actions_remarks              VARCHAR2(200),
    Evidence_verification_confirm_HACCP              VARCHAR2(200),
    Evidence_verification_confirm_HACCP_REMARKS      VARCHAR2(200),
    Record_keeping_documents_appropriate             VARCHAR2(200),
    Record_keeping_documents_appropriate_remarks     VARCHAR2(200),
    DESCRIPTION                                      VARCHAR2(200),
    status                                           NUMBER(2),
    var_field_1                                      VARCHAR2(350 CHAR),
    var_field_2                                      VARCHAR2(350 CHAR),
    var_field_3                                      VARCHAR2(350 CHAR),
    var_field_4                                      VARCHAR2(350 CHAR),
    var_field_5                                      VARCHAR2(350 CHAR),
    var_field_6                                      VARCHAR2(350 CHAR),
    var_field_7                                      VARCHAR2(350 CHAR),
    var_field_8                                      VARCHAR2(350 CHAR),
    var_field_9                                      VARCHAR2(350 CHAR),
    var_field_10                                     VARCHAR2(350 CHAR),
    created_by                                       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                                       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                                      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                                      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                                        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                                       TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;


create sequence dat_kebs_qa_inspection_haccp_implementation_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger dat_kebs_qa_inspection_haccp_implementation_seq_trg
    before
        insert
    on dat_kebs_qa_inspection_haccp_implementation
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_inspection_haccp_implementation_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_inspection_haccp_implementation_idx on dat_kebs_qa_inspection_haccp_implementation (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/



create table dat_kebs_qa_inspection_OPC
(
    id             NUMBER PRIMARY KEY,
    PROCESS_FLOW   VARCHAR2(200),
    OPERATIONS     VARCHAR2(200),
    QUALITY_CHECKS VARCHAR2(200),
    FREQUENCY      VARCHAR2(200),
    RECORDS        VARCHAR2(200),
    FINDINGS       VARCHAR2(200),
    DESCRIPTION    VARCHAR2(200),
    PERMIT_ID      NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    status         NUMBER(2),
    var_field_1    VARCHAR2(350 CHAR),
    var_field_2    VARCHAR2(350 CHAR),
    var_field_3    VARCHAR2(350 CHAR),
    var_field_4    VARCHAR2(350 CHAR),
    var_field_5    VARCHAR2(350 CHAR),
    var_field_6    VARCHAR2(350 CHAR),
    var_field_7    VARCHAR2(350 CHAR),
    var_field_8    VARCHAR2(350 CHAR),
    var_field_9    VARCHAR2(350 CHAR),
    var_field_10   VARCHAR2(350 CHAR),
    created_by     VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on    TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on     TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_inspection_OPC_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_inspection_OPC_seq_trg
    before
        insert
    on dat_kebs_qa_inspection_OPC
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_inspection_OPC_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_inspection_OPC_idx on dat_kebs_qa_inspection_OPC (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/



create table dat_kerbs_qa_inspection_report_recommendation
(
    id                  NUMBER PRIMARY KEY,
    RECOMMENDATIONS     VARCHAR2(200),
    Inspector_comments  VARCHAR2(200),
    Inspector_NAME      VARCHAR2(200),
    inspector_Date      DATE,
    Supervisor_comments VARCHAR2(200),
    Supervisor_NAME     VARCHAR2(200),
    Supervisor_Date     DATE,
    DESCRIPTION         VARCHAR2(200),
    PERMIT_ID           NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kerbs_qa_inspection_report_recommendation_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kerbs_qa_inspection_report_recommendation_seq_trg
    before
        insert
    on dat_kerbs_qa_inspection_report_recommendation
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kerbs_qa_inspection_report_recommendation_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kerbs_qa_inspection_report_recommendation_idx on dat_kerbs_qa_inspection_report_recommendation (PERMIT_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_qa_batch_invoice
(
    id               NUMBER PRIMARY KEY,
    INVOICE_NUMBER   VARCHAR2(200) UNIQUE,
    TOTAL_AMOUNT     NUMBER(38, 3) NOT NULL,
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

create sequence dat_kebs_qa_batch_invoice_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_batch_invoice_seq_trg
    before
        insert
    on dat_kebs_qa_batch_invoice
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_batch_invoice_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_batch_invoice_idx on dat_kebs_qa_batch_invoice (status) TABLESPACE qaimssdb_idx;
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


alter table DAT_KEBS_QA_STA10 modify AVERAGE_VOLUME_PRODUCTION_MONTH VARCHAR2(200)
/




create table dat_kebs_qa_sample_submission (
    id               NUMBER PRIMARY KEY,
    PERMIT_ID      NUMBER REFERENCES DAT_KEBS_PERMIT_TRANSACTION (ID),
    SSF_NO         VARCHAR2(200),
    SSF_SUBMISSION_DATE         DATE,
    BS_NUMBER         VARCHAR2(200),
    BRAND_NAME         VARCHAR2(200),
    PRODUCT_DESCRIPTION         VARCHAR2(200),
    SAMPLE_STATUS         VARCHAR2(200),
    RESULTS_DATE         DATE,
    RESULTS_ANALYSIS         NUMBER(2),
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

create sequence dat_kebs_qa_sample_submission_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_sample_submission_seq_trg
    before
        insert
    on dat_kebs_qa_sample_submission
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sample_submission_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sample_submission_idx on dat_kebs_qa_sample_submission (PERMIT_ID, status, RESULTS_ANALYSIS) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_qa_sample_lab_test_parameters (
    id               NUMBER PRIMARY KEY,
    ORDER_ID  VARCHAR2(200),
SAMPLE_NUMBER  VARCHAR2(200),
TEST  VARCHAR2(200),
MATRIX  VARCHAR2(200),
METHOD  VARCHAR2(200),
TEST_GROUP  VARCHAR2(200),
PRIORITY  VARCHAR2(200),
CURRENT_DEPARTMENT  VARCHAR2(200),
LAST_DEPARTMENT  VARCHAR2(200),
RE_DEPARTMENT  VARCHAR2(200),
TEST_PRICE  VARCHAR2(200),
CUSTOMER_TEST_PRICE  VARCHAR2(200),
DUE_DATE  VARCHAR2(200),
DUE_DATE_FLAG  VARCHAR2(200),
PREP_DUE_DATE  VARCHAR2(200),
PREP_METHOD  VARCHAR2(200),
ANALYSIS_DUE_DATE  VARCHAR2(200),
ANALYSIS_TIME  VARCHAR2(200),
ANALYSIS_EMPLOYEE  VARCHAR2(200),
KEEP_TEST  VARCHAR2(200),
CANCELLED  VARCHAR2(200),
HAS_RESULTS  VARCHAR2(200),
SUPPLY_RECONCILED  VARCHAR2(200),
CUSTOM_PARAMS  VARCHAR2(200),
PRESERVATIVE  VARCHAR2(200),
BOTTLE_TYPE  VARCHAR2(200),
STORAGE_LOCATION  VARCHAR2(200),
SAMPLE_DETAILS_USER1  VARCHAR2(200),
SAMPLE_DETAILS_USER2  VARCHAR2(200),
SAMPLE_DETAILS_USER3  VARCHAR2(200),
SAMPLE_DETAILS_USER4  VARCHAR2(200),
TS  VARCHAR2(200),
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

create sequence dat_kebs_qa_sample_lab_test_parameters_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_sample_lab_test_parameters_seq_trg
    before
        insert
    on dat_kebs_qa_sample_lab_test_parameters
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sample_lab_test_parameters_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sample_lab_test_parameters_idx on dat_kebs_qa_sample_lab_test_parameters (ORDER_ID, status) TABLESPACE qaimssdb_idx;
/



create table dat_kebs_qa_sample_lab_test_results (
    id               NUMBER PRIMARY KEY,
    ORDER_ID         VARCHAR2(200),
SAMPLE_NUMBER  VARCHAR2(200),
TEST  VARCHAR2(200),
PARAM  VARCHAR2(200),
SORT_ORDER  VARCHAR2(200),
METHOD  VARCHAR2(200),
RESULT  VARCHAR2(200),
NUMERIC_RESULT  VARCHAR2(200),
UNITS  VARCHAR2(200),
DILUTION  VARCHAR2(200),
ANALYSIS_VOLUME  VARCHAR2(200),
SAMPLE_TYPE  VARCHAR2(200),
QUALIFIER  VARCHAR2(200),
REP_LIMIT  VARCHAR2(200),
RETENTION_TIME  VARCHAR2(200),
TIC  VARCHAR2(200),
RESULT_STATUS  VARCHAR2(200),
ENTERED_DATE  VARCHAR2(200),
ENTERED_BY  VARCHAR2(200),
VALIDATED_DATE  VARCHAR2(200),
VALIDATED_BY  VARCHAR2(200),
APPROVED_DATE  VARCHAR2(200),
APPROVED_BY  VARCHAR2(200),
APPROVED_REASON  VARCHAR2(200),
COMMNT  VARCHAR2(200),
MEASURED_RESULT  VARCHAR2(200),
PERCENT_MOISTURE  VARCHAR2(200),
METHOD_VOLUME  VARCHAR2(200),
EXTRACT_VOLUME  VARCHAR2(200),
METHOD_EXTRACT_VOLUME  VARCHAR2(200),
INSTRUMENT  VARCHAR2(200),
RESULTS_USER1  VARCHAR2(200),
RESULTS_USER2  VARCHAR2(200),
RESULTS_USER3  VARCHAR2(200),
RESULTS_USER4  VARCHAR2(200),
REPORT  VARCHAR2(200),
PARAM_ANALYST  VARCHAR2(200),
PARAM_ANALYSIS_DATE_TIME  VARCHAR2(200),
PRINTED  VARCHAR2(200),
PRINTED_AT  VARCHAR2(200),
TS  VARCHAR2(200),
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

create sequence dat_kebs_qa_sample_lab_test_results_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create or replace trigger dat_kebs_qa_sample_lab_test_results_seq_trg
    before
        insert
    on dat_kebs_qa_sample_lab_test_results
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sample_lab_test_results_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sample_lab_test_results_idx on dat_kebs_qa_sample_lab_test_results (ORDER_ID, status) TABLESPACE qaimssdb_idx;
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

create table cfg_kebs_qa_process_status
(
    ID                       NUMBER  primary key,
    PROCESS_STATUS_NAME      VARCHAR2(200) UNIQUE ,
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

create sequence cfg_kebs_qa_process_status_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_qa_process_status_seq_trg
    before
        insert
    on cfg_kebs_qa_process_status
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_qa_process_status_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_qa_process_status_idx on cfg_kebs_qa_process_status (status, PROCESS_STATUS_NAME) TABLESPACE qaimssdb_idx;
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