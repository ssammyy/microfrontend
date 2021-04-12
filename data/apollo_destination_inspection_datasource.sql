***************************Table USED IN DI*****************************************
select * from DAT_KEBS_CD_FILE_XML
-- where CD_ITEM_DETAILS_ID = '281'
;

select *
from CFG_INTEGRATION_CONFIGURATION
-- where id = 207
order by ID desc;

select *
from LOG_JWT_TOKENS_REGISTRY
order by id desc;

select *
from CFG_USER_ROLES_ASSIGNMENTS
order by id desc;

select *
from DAT_KEBS_IDF_DETAILS
where UCR_NO = 'UCR2100006314'
order by id desc;
select *
from DAT_KEBS_CD_ITEM_DETAILS
where UUID = '59572e2d-222a-4850-a256-d04d07a00ca7'
-- where ID = 85
-- where CD_DOC_ID = 184
order by ID desc;
select *
from DAT_KEBS_COCS
-- where UCR_NUMBER = 'UCR2100006322'
-- where id = 283
order by ID desc;

select *
from DAT_KEBS_COC_ITEMS
-- where uuid = 401
where COI_NUMBER = 'KEBSCOI2021040872B57'
order by ID desc;


select *
from dat_kebs_di_uploads
-- where CD_ITEM_DETAILS_ID = '281'
;
select *
from CFG_KEBS_DESIGNATIONS
-- where CD_ITEM_DETAILS_ID = '281'
;

select *
from DAT_KEBS_USER_PROFILES
-- where CD_ITEM_DETAILS_ID = '281'
;

select * from CFG_KEBS_CDCFS_USERCFS
-- where CD_ITEM_DETAILS_ID = '281'
;


select * from CFG_KEBS_CFS_TYPE_CODES
-- where CD_ITEM_DETAILS_ID = '281'
;

select * from CFG_KEBS_PROCESSES_STAGES
-- where CD_ITEM_DETAILS_ID = '281'
;

select * from DAT_KEBS_CD_FILE_XML
-- where CD_ITEM_DETAILS_ID = '281'
order by ID desc;

select * from LOG_KEBS_CD_TRANSACTIONS
-- where CD_ITEM_DETAILS_ID = '281'
order by ID desc;

select * from CFG_INTEGRATION_CONFIGURATION
-- where id = 207
order by ID desc;

select * from CFG_BATCH_JOB_TYPES
order by ID;

select * from CFG_BATCH_JOB_DETAILS
order by ID desc;

select * from CFG_KEBS_COUNTRY_TYPE_CODES
-- where CD_ITEM_DETAILS_ID = '281'
;

select * from LOG_KEBS_CD_TRANSACTIONS
-- where CD_ITEM_DETAILS_ID = '281'
order by ID desc;

select * from CFG_SERVICE_MAPS
where id = 220 ---CD DOWNLOAD?UPLOAD
order by ID desc;

select * from DAT_KEBS_USERS order by ID desc ;


select * from DAT_KEBS_CD_INSPECTION_GENERAL
where CD_ITEM_DETAILS_ID = '281'
;
select * from DAT_KEBS_CD_DEMAND_NOTE order by ID desc ;
select * from CFG_KEBS_DESTINATION_INSPECTION_FEE order by ID desc ;
select * from CFG_MONEY_TYPE_CODES order by ID desc ;
select * from DAT_KEBS_CD_INSPECTION_GENERAL
where id = 102
-- where CD_ITEM_DETAILS_ID = 281
order by ID desc ;
select * from DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST order by ID desc ;
select * from DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST order by ID desc ;
select * from DAT_KEBS_CD_SAMPLE_COLLECTION order by ID desc ;
select *from DAT_KEBS_CD_SAMPLE_SUBMISSION_ITEMS
where ITEM_ID = 281
order by ID desc;
select *
from DAT_KEBS_CD_SAMPLE_SUBMISSION_PARAMATERS
-- where ID = 85
where SAMPLE_SUBMISSION_ID = 461
order by ID desc;


select *
from DAT_KEBS_CD_IMPORTER_DETAILS
where id = 421
order by ID desc;


ALTER TABLE DAT_KEBS_CD_DEMAND_NOTE COLUMN PRODUCT SET DEFAULT 'UN';

select *
from DAT_KEBS_CD_EXPORTER_DETAILS
where id = 361
order by ID desc;
select *
from DAT_KEBS_CD_TRANSPORT_DETAILS
order by ID desc;
select *
from CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES
order by ID desc;
select *
from DAT_KEBS_CD_LOCAL_COC
order by ID desc;
select *
from CFG_KEBS_DESTINATION_INSPECTION_FEE
order by ID desc;
select *
from DAT_KEBS_INVOICE_TRANSACTIONS
order by ID desc;
select *
from CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES
order by ID desc;
select *
from DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS
order by ID desc;
select *
from DAT_KEBS_CS_APPROVAL_APPLICATIONS
order by ID desc;
select *
from DAT_KEBS_CS_APPROVAL_APPLICATIONS_UPLOADS
order by ID desc;
select *
from DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS_UPLOADS
order by ID desc;

select *
from DAT_KEBS_COCS_BAK
-- where UCR_NUMBER = 'UCR2100006345'
-- where id = 283
order by ID desc;

select *
from DAT_KEBS_COC_ITEMS
-- where uuid = 401
-- where COI_NUMBER = 'KEBSCOI2021040872B57'
order by ID desc;

select *
from DAT_KEBS_CD_LOCAL_COC
-- where uuid = 401
order by ID desc;
select *
from DAT_KEBS_CD_LOCAL_COC_ITEMS
-- where uuid = 401
order by ID desc;

select *
from DAT_KEBS_DECLARATION_DETAILS
-- where uuid = 401
order by ID desc;

select *
from DAT_KEBS_DECLARATION_ITEM_DETAILS
-- where uuid = 401
order by ID desc;

select *
from CFG_COC_TYPES
order by ID desc;



select *
from DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS
-- where uuid = 'b8404c30-0286-46f6-9afb-c7347f11f701'
where UCR_number = 'UCR2100006339'
-- where id = 321
order by ID desc;

DeclarationDetailsEntity
DeclarationItemDetailsEntity

select *
from CFG_KEBS_LOCAL_COC_TYPES
order by ID desc;
select *
from CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES
order by ID desc;

184
select *
from dat_kebs_cd_document_header
order by ID desc;

alter table DAT_KEBS_CD_ITEM_DETAILS
    add COUNTRY_OF_ORGIN_DESC VARCHAR2(200)/
alter table DAT_KEBS_CD_ITEM_DETAILS
    add SUPPLEMENTARY_UNIT_OF_QTY VARCHAR2(200)/
alter table DAT_KEBS_CD_ITEM_DETAILS
    add SUPPLEMENTARY_UNIT_OF_QTY_DESC VARCHAR2(200)/
alter table DAT_KEBS_CD_ITEM_DETAILS
    add PRODUCT_BRAND_NAME VARCHAR2(200)/
alter table DAT_KEBS_CD_ITEM_DETAILS
    add PRODUCT_ACTIVE_INGREDIENTS VARCHAR2(200)/
alter table DAT_KEBS_CD_ITEM_DETAILS
    add PRODUCT_PACKAGING_DETAILS VARCHAR2(200)/

****************************Created Tables USED IN DI********************************

create table dat_kebs_cd_demand_note_items_details
(
    ID             NUMBER primary key,
    ITEM_ID        NUMBER references DAT_KEBS_CD_ITEM_DETAILS (ID),
    DEMAND_NOTE_ID NUMBER references DAT_KEBS_CD_DEMAND_NOTE (ID),
    PRODUCT        VARCHAR2(500),
    C_F_VALUE      NUMBER(10, 2),
    RATE           VARCHAR2(20),
    AMOUNT_PAYABLE NUMBER(10, 2),
    DESCRIPTION    VARCHAR2(200),
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

create sequence dat_kebs_cd_demand_note_items_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_demand_note_items_details_seq_trg
    before
        insert
    on dat_kebs_cd_demand_note_items_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_demand_note_items_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_demand_note_items_details_idx on dat_kebs_cd_demand_note_items_details (status, ITEM_ID, DEMAND_NOTE_ID) TABLESPACE qaimssdb_idx;
/



create table dat_kebs_cd_items_currier_details
(
    ID                       NUMBER primary key,
    ITEM_ID                  NUMBER references DAT_KEBS_CD_ITEM_DETAILS,
    CD_PRODUCT_TWO_ID        NUMBER references DAT_KEBS_CD_PRODUCTS2,
    CD_ID                    NUMBER references DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS,
    ITEM_COUNT               VARCHAR2(200),
    CD_ITEM_NON_STANDARDS_ID NUMBER references DAT_KEBS_CD_ITEM_NON_STANDARD,
    CD_ITEM_COMMODITY_ID     NUMBER references DAT_KEBS_CD_ITEM_COMMODITY_DETAILS,
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

create sequence dat_kebs_cd_items_currier_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_items_currier_details_seq_trg
    before
        insert
    on dat_kebs_cd_items_currier_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_items_currier_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_items_currier_details_idx on dat_kebs_cd_items_currier_details (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_risk_details_action_details
(
    id               NUMBER PRIMARY KEY,
    ACTION_CODE   VARCHAR2(200),
    ACTION_NAME   VARCHAR2(200),
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

create sequence dat_kebs_cd_risk_details_action_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_risk_details_action_details_seq_trg
    before
        insert
    on dat_kebs_cd_risk_details_action_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_risk_details_action_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_risk_details_action_details_idx on dat_kebs_cd_risk_details_action_details (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_risk_details_assessment
(
    id               NUMBER PRIMARY KEY,
    PROFILE_CODE   VARCHAR2(200),
    PROFILE_NAME   VARCHAR2(200),
    ASSESSED_LANE   VARCHAR2(200),
    ASSESSED_DATE   VARCHAR2(200),
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

create sequence dat_kebs_cd_risk_details_assessment_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_risk_details_assessment_seq_trg
    before
        insert
    on dat_kebs_cd_risk_details_assessment
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_risk_details_assessment_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_risk_details_assessment_idx on dat_kebs_cd_risk_details_assessment (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_item_commodity_details
(
    id               NUMBER PRIMARY KEY,
    COMMON_NAME   VARCHAR2(200),
    ORGANISM_STRAIN   VARCHAR2(200),
    TREATMENT_INFORMATION   VARCHAR2(200),
    TREATMENT_DATE   VARCHAR2(200),
    DURATIONS_AND_TEMPERATURE   VARCHAR2(200),
    CHEMICALS_ACTIVE_INGREDIENTS      VARCHAR2(200),
    CONCENTRATION_ACTIVE_INGREDIENTS      VARCHAR2(200),
    SEED_REFERENCE_NO      VARCHAR2(200),
    PRODUCER_DETAILS      VARCHAR2(200),
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

create sequence dat_kebs_cd_item_commodity_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_item_commodity_details_seq_trg
    before
        insert
    on dat_kebs_cd_item_commodity_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_item_commodity_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_item_commodity_details_idx on dat_kebs_cd_item_commodity_details (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_products2
(
    id               NUMBER PRIMARY KEY,
    RISK_CLASSIFICATION   VARCHAR2(200),
    RISK_DETAILS   VARCHAR2(200),
    SAFETY_CLASSIFICATION   VARCHAR2(200),
    SAFETY_DETAILS   VARCHAR2(200),
    RISKS_AFETY_REMARKS   VARCHAR2(200),
    SAMPLING_REQUIREMENT      VARCHAR2(200),
    SAMPLING_RESULTS      VARCHAR2(200),
    APPLICANT_REMARKS      VARCHAR2(200),
    MDA_REMARKS      VARCHAR2(200),
    CUSTOMS_REMARKS      VARCHAR2(200),
    MDA_ITEM_APPROVAL_FLAG      VARCHAR2(200),
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

create sequence dat_kebs_cd_products2_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_products2_seq_trg
    before
        insert
    on dat_kebs_cd_products2
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_products2_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_products2_idx on dat_kebs_cd_products2 (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_products2_end_user_details
(
    id               NUMBER PRIMARY KEY,
    REG_NO   VARCHAR2(200),
    NAME   VARCHAR2(200),
    PHYSICAL_ADDRESS   VARCHAR2(200),
    TEL_FAX   VARCHAR2(200),
    USE_GENERAL_DESCRIPTION   VARCHAR2(200),
    USE_DETAILS      VARCHAR2(200),
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

create sequence dat_kebs_cd_products2_end_user_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_products2_end_user_details_seq_trg
    before
        insert
    on dat_kebs_cd_products2_end_user_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_products2_end_user_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_products2_end_user_details_idx on dat_kebs_cd_products2_end_user_details (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_document_fee
(
    id               NUMBER PRIMARY KEY,
    CURRENCY   VARCHAR2(200),
    AMOUNT_FCY   VARCHAR2(200),
    AMOUNT_NCY   VARCHAR2(200),
    PAYMENT_MODE   VARCHAR2(200),
    RECEIPT_NUMBER   VARCHAR2(200),
    RECEIPT_DATE      VARCHAR2(200),

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

create sequence dat_kebs_cd_document_fee_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_document_fee_seq_trg
    before
        insert
    on dat_kebs_cd_document_fee
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_document_fee_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_document_fee_idx on dat_kebs_cd_document_fee (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_processing_fee
(
    id               NUMBER PRIMARY KEY,
    CURRENCY   VARCHAR2(200),
    AMOUNT_FCY   VARCHAR2(200),
    AMOUNT_NCY   VARCHAR2(200),
    PAYMENT_MODE   VARCHAR2(200),
    RECEIPT_NUMBER   VARCHAR2(200),
    RECEIPT_DATE      VARCHAR2(200),
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

create sequence dat_kebs_cd_processing_fee_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_processing_fee_seq_trg
    before
        insert
    on dat_kebs_cd_processing_fee
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_processing_fee_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_processing_fee_idx on dat_kebs_cd_processing_fee (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_applicant_defined_third_party_details
(
    id               NUMBER PRIMARY KEY,
    THIRD_PARTY_CODE   VARCHAR2(200),
    THIRD_PARTY_DESCRIPTION   VARCHAR2(200),
    DISTRIBUTION_METHOD   VARCHAR2(200),
    THIRD_PARTY_MAIL_BOX   VARCHAR2(200),
    THIRD_PARTY_ACCOUNT   VARCHAR2(200),
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

create sequence dat_kebs_cd_applicant_defined_third_party_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_applicant_defined_third_party_details_seq_trg
    before
        insert
    on dat_kebs_cd_applicant_defined_third_party_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_applicant_defined_third_party_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_applicant_defined_third_party_details_idx on dat_kebs_cd_applicant_defined_third_party_details (status) TABLESPACE qaimssdb_idx;
/
create table dat_kebs_cd_third_party_details
(
    id               NUMBER PRIMARY KEY,
    THIRD_PARTY_CODE   VARCHAR2(200),
    THIRD_PARTY_DESCRIPTION   VARCHAR2(200),
    DISTRIBUTION_METHOD   VARCHAR2(200),
    THIRD_PARTY_MAIL_BOX   VARCHAR2(200),
    THIRD_PARTY_ACCOUNT   VARCHAR2(200),
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

create sequence dat_kebs_cd_third_party_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_third_party_details_seq_trg
    before
        insert
    on dat_kebs_cd_third_party_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_third_party_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_third_party_details_idx on dat_kebs_cd_third_party_details (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_header_two_details
(
    id               NUMBER PRIMARY KEY,
    TERMS_OF_PAYMENT   VARCHAR2(200),
    TERMS_OF_PAYMENT_DESC   VARCHAR2(200),
    LOCAL_BANK_CODE   VARCHAR2(200),
    LOCAL_BANK_DESC   VARCHAR2(200),
    RECEIPT_OF_REMITTANCE   VARCHAR2(200),
    REMITTANCE_CURRENCY   VARCHAR2(200),
    REMITTANCE_AMOUNT   VARCHAR2(200),
    REMITTANCE_DATE   VARCHAR2(200),
    REMITTANCE_REFERENCE   VARCHAR2(200),
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

create sequence dat_kebs_cd_header_two_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_header_two_details_seq_trg
    before
        insert
    on dat_kebs_cd_header_two_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_header_two_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_header_two_details_idx on dat_kebs_cd_header_two_details (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_container
(
    id               NUMBER PRIMARY KEY,
    CONTAINER_NUMBERS   VARCHAR2(200),
    CONTAINER_NO_OF_PACKAGES   VARCHAR2(200),
    CONTAINER_SIZE   VARCHAR2(200),
    CONTAINER_SEAL_NO   VARCHAR2(200),
    CONTAINER_LOAD_INDICATOR   VARCHAR2(200),
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

create sequence dat_kebs_cd_container_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_container_seq_trg
    before
        insert
    on dat_kebs_cd_container
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_container_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_container_idx on dat_kebs_cd_container (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_approval_history
(
    id               NUMBER PRIMARY KEY,
    STAGE_NO   VARCHAR2(200),
    STEP_CODE   VARCHAR2(200),
    MDA_CODE   VARCHAR2(200),
    ROLE_CODE   VARCHAR2(200),
    DOC_STATUS   VARCHAR2(200),
    USER_ID   VARCHAR2(200),
    UPDATED_DATE   VARCHAR2(200),
    PREMISE_INSPECTION   VARCHAR2(200),
    EXAMINATION_REQUIRED  VARCHAR2(200),
    TECHNICAL_REJECTION  VARCHAR2(200),
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

create sequence dat_kebs_cd_approval_history_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_approval_history_seq_trg
    before
        insert
    on dat_kebs_cd_approval_history
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_approval_history_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_approval_history_idx on dat_kebs_cd_approval_history (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_document_header
(
    id               NUMBER PRIMARY KEY,
    DOCUMENT_TYPE   VARCHAR2(200),
    DOCUMENT_NAME   VARCHAR2(200),
    DOCUMENT_NUMBER   VARCHAR2(200),
    COMMON_REF_NUMBER   VARCHAR2(200),
    MESSAGE_TYPE   VARCHAR2(200),
    SENDER_ID   VARCHAR2(200),
    REGIME_CODE   VARCHAR2(200),
    CMS_REGIME_CODE   VARCHAR2(200),
    APPROVAL_CATEGORY  VARCHAR2(200),
    RECEIVING_PARTY  VARCHAR2(200),
    NOTIFY_PARTY  VARCHAR2(200),
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

create sequence dat_kebs_cd_document_header_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_document_header_seq_trg
    before
        insert
    on dat_kebs_cd_document_header
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_document_header_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_document_reference_idx on dat_kebs_cd_document_header (status) TABLESPACE qaimssdb_idx;
/

create table CFG_KEBS_PROCESSES_STAGES
(
    id     NUMBER PRIMARY KEY,
    SERVICE_MAP_ID   NUMBER REFERENCES CFG_SERVICE_MAPS(ID),
    process_1      VARCHAR2(350 CHAR),
    process_2      VARCHAR2(350 CHAR),
    process_3      VARCHAR2(350 CHAR),
    process_4      VARCHAR2(350 CHAR),
    process_5      VARCHAR2(350 CHAR),
    process_6      VARCHAR2(350 CHAR),
    process_7      VARCHAR2(350 CHAR),
    process_8      VARCHAR2(350 CHAR),
    process_9      VARCHAR2(350 CHAR),
    process_10     VARCHAR2(350 CHAR),
    process_11      VARCHAR2(350 CHAR),
    process_12      VARCHAR2(350 CHAR),
    process_13      VARCHAR2(350 CHAR),
    process_14      VARCHAR2(350 CHAR),
    process_15      VARCHAR2(350 CHAR),
    process_16      VARCHAR2(350 CHAR),
    process_17      VARCHAR2(350 CHAR),
    process_18      VARCHAR2(350 CHAR),
    process_19      VARCHAR2(350 CHAR),
    process_20     VARCHAR2(350 CHAR),
    process_21      VARCHAR2(350 CHAR),
    process_22      VARCHAR2(350 CHAR),
    process_23      VARCHAR2(350 CHAR),
    process_24      VARCHAR2(350 CHAR),
    process_25      VARCHAR2(350 CHAR),
    process_26      VARCHAR2(350 CHAR),
    process_27      VARCHAR2(350 CHAR),
    process_28      VARCHAR2(350 CHAR),
    process_29      VARCHAR2(350 CHAR),
    process_30      VARCHAR2(350 CHAR),
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

create sequence CFG_KEBS_PROCESSES_STAGES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_PROCESSES_STAGES_seq_trg
    before
        insert
    on CFG_KEBS_PROCESSES_STAGES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_PROCESSES_STAGES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_PROCESSES_STAGES_idx on CFG_KEBS_PROCESSES_STAGES (status,SERVICE_MAP_ID) TABLESPACE qaimssdb_idx;
/

create table LOG_KEBS_CD_TRANSACTIONS
(
    id     NUMBER PRIMARY KEY,
    CD_ID   NUMBER REFERENCES DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS(ID),
    TRANSACTION_DATE DATE,
    REMARKS VARCHAR2(200),
    PROCESS_STAGE VARCHAR2(200),
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

create sequence LOG_KEBS_CD_TRANSACTIONS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger LOG_KEBS_CD_TRANSACTIONS_seq_trg
    before
        insert
    on LOG_KEBS_CD_TRANSACTIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select LOG_KEBS_CD_TRANSACTIONS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index LOG_KEBS_CD_TRANSACTIONS_idx on LOG_KEBS_CD_TRANSACTIONS (status,CD_ID, TRANSACTION_DATE) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_service_provider
(
    id                    NUMBER  PRIMARY KEY,
    application_code    VARCHAR2(200),
    name                VARCHAR2(200),
    tin                VARCHAR2(200),
    physical_address    VARCHAR2(200),
    phy_country    VARCHAR2(200),
    description         VARCHAR2(350),
    status              VARCHAR2(20),
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
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_service_provider_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_service_provider_seq_trg
    before
        insert
    on dat_kebs_cd_service_provider
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_service_provider_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_service_provider_idx on dat_kebs_cd_service_provider (status) TABLESPACE qaimssdb_idx;
/


create table DAT_KEBS_CD_STANDARDS_TWO
(
    id     NUMBER PRIMARY KEY,
    PURPOSE_OF_IMPORT      VARCHAR2(200),
    COC_TYPE      VARCHAR2(2),
    LOCAL_COC_TYPE      VARCHAR2(200),
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

create sequence DAT_KEBS_CD_STANDARDS_TWO_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_STANDARDS_TWO_seq_trg
    before
        insert
    on DAT_KEBS_CD_STANDARDS_TWO
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_STANDARDS_TWO_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_STANDARDS_TWO_idx on DAT_KEBS_CD_STANDARDS_TWO (status) TABLESPACE qaimssdb_idx;
/


create table CFG_KEBS_LOCAL_COC_TYPES
(
    id     NUMBER PRIMARY KEY,
    COC_TYPE_CODE      VARCHAR2(200) UNIQUE,
    COC_TYPE_DESC       VARCHAR2(200),
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

create sequence CFG_KEBS_LOCAL_COC_TYPES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_LOCAL_COC_TYPES_seq_trg
    before
        insert
    on CFG_KEBS_LOCAL_COC_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_LOCAL_COC_TYPES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_LOCAL_COC_TYPES_idx on CFG_KEBS_LOCAL_COC_TYPES (status, COC_TYPE_CODE) TABLESPACE qaimssdb_idx;
/


create table CFG_KEBS_CFS_TYPE_CODES
(
    id               NUMBER PRIMARY KEY,
    CFS_CODE        VARCHAR2(20) UNIQUE ,
    CFS_NAME        VARCHAR2(100),
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

create sequence CFG_KEBS_CFS_TYPE_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_CFS_TYPE_CODES_seq_trg
    before
        insert
    on CFG_KEBS_CFS_TYPE_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_CFS_TYPE_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_CFS_TYPE_CODES_idx on CFG_KEBS_CFS_TYPE_CODES (status, CFS_CODE) TABLESPACE qaimssdb_idx;
/

create table CFG_KEBS_COUNTRY_TYPE_CODES
(
    id               NUMBER PRIMARY KEY,
    COUNTRY_NAME        VARCHAR2(100),
    COUNTRY_CODE        VARCHAR2(20) UNIQUE ,
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

create sequence CFG_KEBS_COUNTRY_TYPE_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_COUNTRY_TYPE_CODES_seq_trg
    before
        insert
    on CFG_KEBS_COUNTRY_TYPE_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_COUNTRY_TYPE_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_COUNTRY_TYPE_CODES_idx on CFG_KEBS_COUNTRY_TYPE_CODES (status, COUNTRY_CODE) TABLESPACE qaimssdb_idx;
/



create table CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES
(
    id               NUMBER PRIMARY KEY,
    CUSTOMS_OFFICE_CODE        VARCHAR2(20) UNIQUE ,
    CUSTOMS_OFFICE_NAME        VARCHAR2(100),
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

create sequence CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES_seq_trg
    before
        insert
    on CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES_idx on CFG_KEBS_CUSTOMS_OFFICE_TYPE_CODES (status, CUSTOMS_OFFICE_CODE) TABLESPACE qaimssdb_idx;
/


create table CFG_KEBS_CDCFS_USERCFS
(
    id     NUMBER PRIMARY KEY,
    USER_CFS       NUMBER REFERENCES CFG_KEBS_SUB_SECTIONS_LEVEL2(ID),
    CD_CFS      NUMBER REFERENCES CFG_KEBS_CFS_TYPE_CODES(ID),
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

create sequence CFG_KEBS_CDCFS_USERCFS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_CDCFS_USERCFS_seq_trg
    before
        insert
    on CFG_KEBS_CDCFS_USERCFS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_CDCFS_USERCFS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_CDCFS_USERCFS_idx on CFG_KEBS_CDCFS_USERCFS (status,CD_CFS, USER_CFS) TABLESPACE qaimssdb_idx;
/


create table CFG_KEBS_CDPORTS_USERPORTS
(
    id     NUMBER PRIMARY KEY,
    USER_PORTS       NUMBER REFERENCES CFG_KEBS_SECTIONS(ID),
    CD_PORTS      NUMBER REFERENCES CFG_KEBS_PORTS_TYPE_CODES(ID),
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

create sequence CFG_KEBS_CDPORTS_USERPORTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_CDPORTS_USERPORTS_seq_trg
    before
        insert
    on CFG_KEBS_CDPORTS_USERPORTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_CDPORTS_USERPORTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_CDPORTS_USERPORTS_idx on CFG_KEBS_CDPORTS_USERPORTS (status,CD_PORTS, USER_PORTS) TABLESPACE qaimssdb_idx;
/


create table CFG_KEBS_PORTS_TYPE_CODES
(
    id               NUMBER PRIMARY KEY,
    PORT_CODE        VARCHAR2(20) UNIQUE ,
    PORT_NAME        VARCHAR2(100),
    MODE_TPT        VARCHAR2(100),
    COUNTRY_CODE        VARCHAR2(20),
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

create sequence CFG_KEBS_PORTS_TYPE_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_PORTS_TYPE_CODES_seq_trg
    before
        insert
    on CFG_KEBS_PORTS_TYPE_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_PORTS_TYPE_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_PORTS_TYPE_CODES_idx on CFG_KEBS_PORTS_TYPE_CODES (status, PORT_CODE,COUNTRY_CODE) TABLESPACE qaimssdb_idx;
/


create table CFG_MONEY_TYPE_CODES
(
    id               NUMBER PRIMARY KEY,
    TYPE_CODE  VARCHAR2(20) UNIQUE ,
    TYPE_CODE_VALUE  VARCHAR2(20),
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

create sequence CFG_MONEY_TYPE_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_MONEY_TYPE_CODES_seq_trg
    before
        insert
    on CFG_MONEY_TYPE_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_MONEY_TYPE_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_MONEY_TYPE_CODES_idx on CFG_MONEY_TYPE_CODES (status, TYPE_CODE) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_CD_FILE_XML
(
    id     NUMBER PRIMARY KEY,
    FILE_PATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
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

create sequence DAT_KEBS_CD_FILE_XML_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_FILE_XML_seq_trg
    before
        insert
    on DAT_KEBS_CD_FILE_XML
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_FILE_XML_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_FILE_XML_idx on DAT_KEBS_CD_FILE_XML (status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_di_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    DESCRIPTION      VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    CD_ID      NUMBER REFERENCES DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS (ID),
    ITEM_ID      NUMBER REFERENCES DAT_KEBS_CD_ITEM_DETAILS (ID),
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

create sequence dat_kebs_di_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_di_uploads_seq_trg
    before
        insert
    on dat_kebs_di_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_di_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_di_uploads_idx on dat_kebs_di_uploads (CD_ID,ITEM_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_standards
(
    id                              NUMBER  PRIMARY KEY,
    service_provider                NUMBER REFERENCES dat_kebs_cd_service_provider(id),
    application_type_code           VARCHAR2(200),
    application_type_description    VARCHAR2(200),
    document_type_code              VARCHAR2(200),
    CMS_document_type_code          VARCHAR2(200),
    document_type_description       VARCHAR2(200),
    consignment_type_code           VARCHAR2(200),
    consignment_type_description    VARCHAR2(200),
    MDA_code                        VARCHAR2(200),
    MDA_description                 VARCHAR2(200),
    document_code                   VARCHAR2(200),
    document_description            VARCHAR2(200),
    process_code                    VARCHAR2(200),
    process_description             VARCHAR2(200),
    application_date                VARCHAR2(200),
    updated_date                    VARCHAR2(200),
    approval_status                 VARCHAR2(200),
    approval_date                   VARCHAR2(200),
    final_approval_date             VARCHAR2(200),
    application_ref_no              VARCHAR2(200),
    version_no                      VARCHAR2(200),
    UCR_number                      VARCHAR2(200),
    declaration_number              VARCHAR2(200),
    description                     VARCHAR2(350),
    status                          NUMBER(2),
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
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_standards_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_standards_seq_trg
    before
        insert
    on dat_kebs_cd_standards
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_standards_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_standards_idx on dat_kebs_cd_standards (UCR_number, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_consignment_document_details
(
    id                              NUMBER  PRIMARY KEY,
    UCR_number                 VARCHAR2(350),
    CD_standard                NUMBER REFERENCES dat_kebs_cd_standards(id),
    CD_importer                NUMBER REFERENCES DAT_KEBS_CD_IMPORTER_DETAILS(id),
    CD_consignee                NUMBER REFERENCES DAT_KEBS_CD_CONSIGNEE_DETAILS(id),
    CD_exporter                NUMBER REFERENCES DAT_KEBS_CD_EXPORTER_DETAILS(id),
    CD_consignor                NUMBER REFERENCES DAT_KEBS_CD_CONSIGNOR_DETAILS(id),
    CD_transport                NUMBER REFERENCES DAT_KEBS_CD_TRANSPORT_DETAILS(id),
    CD_header_one                NUMBER REFERENCES DAT_KEBS_CD_VALUES_HEADER_LEVEL(id),

    description                     VARCHAR2(350),
    status                          NUMBER(2),
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
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_consignment_document_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_consignment_document_details_seq_trg
    before
        insert
    on dat_kebs_consignment_document_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_consignment_document_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_consignment_document_details_idx on dat_kebs_consignment_document_details (UCR_number, status) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_CD_ITEM_NON_STANDARD
(
    id               NUMBER PRIMARY KEY,
    CD_ITEM_DETAILS_ID      NUMBER REFERENCES DAT_KEBS_CD_ITEM_DETAILS (ID),
    CHASSIS_NO   VARCHAR2(200),
    USED_INDICATOR   VARCHAR2(200),
    VEHICLE_YEAR   VARCHAR2(200),
    VEHICLE_MODEL   VARCHAR2(200),
    VEHICLE_MAKE   VARCHAR2(200),
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

create sequence DAT_KEBS_CD_ITEM_NON_STANDARD_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_ITEM_NON_STANDARD_seq_trg
    before
        insert
    on DAT_KEBS_CD_ITEM_NON_STANDARD
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_ITEM_NON_STANDARD_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_ITEM_NON_STANDARD_idx on DAT_KEBS_CD_ITEM_NON_STANDARD (CHASSIS_NO, status) TABLESPACE qaimssdb_idx;

alter table DAT_KEBS_CD_DEMAND_NOTE
    add DEMAND_NOTE_NUMBER varchar2(200);

alter table DAT_KEBS_CD_DEMAND_NOTE
	add PAYMENT_STATUS number;

create table DAT_KEBS_CD_INSPECTION_GENERAL
(
    id  NUMBER PRIMARY KEY,
    CD_ITEM_DETAILS_ID  NUMBER REFERENCES DAT_KEBS_CD_ITEM_DETAILS (ID),
    ITEM_TYPE  NUMBER REFERENCES CFG_CD_CHECKLIST_TYPES (ID),
    INSPECTION  VARCHAR2(200),
    CATEGORY                       VARCHAR2(200),
    ENTRY_POINT                    VARCHAR2(200),
    CFS                            VARCHAR2(200),
    INSPECTION_DATE                DATE,
    IMPORTERS_NAME                 VARCHAR2(200),
    CLEARING_AGENT                 VARCHAR2(200),
    CUSTOMS_ENTRY_NUMBER           VARCHAR2(200),
    IDF_NUMBER                     VARCHAR2(200),
    UCR_NUMBER                     VARCHAR2(200),
    COC_NUMBER                     VARCHAR2(200),
    FEE_PAID                       VARCHAR2(200),
    RECEIPT_NUMBER                 VARCHAR2(200),
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

create sequence DAT_KEBS_CD_INSPECTION_GENERAL_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_GENERAL_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_GENERAL
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_GENERAL_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_INSPECTION_GENERAL_idx on DAT_KEBS_CD_INSPECTION_GENERAL (CD_ITEM_DETAILS_ID, ITEM_TYPE, status) TABLESPACE qaimssdb_idx;

create table DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_GENERAL (ID),
    SERIAL_NUMBER                  VARCHAR2(200),
    PRODUCT_DESCRIPTION            VARCHAR2(3500),
    BRAND                          VARCHAR2(200),
    KS_EAS_APPLICABLE              VARCHAR2(200),
    QUANTITY_DECLARED              VARCHAR2(200),
    QUANTITY_VERIFIED              VARCHAR2(200),
    DATE_MFG_PACKAGING             VARCHAR2(200),
    DATE_EXPIRY                    VARCHAR2(200),
    MFG_NAME                       VARCHAR2(200),
    MFG_ADDRESS                    VARCHAR2(200),
    COMPOSITION_INGREDIENTS        VARCHAR2(3800),
    STORAGE_CONDITION              VARCHAR2(200),
    APPEARANCE                     VARCHAR2(200),
    CERT_MARKS_PVOC_DOC            VARCHAR2(200),
    SAMPLED                        VARCHAR2(200),
    REMARKS                        VARCHAR2(200),
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

create sequence DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST_idx on DAT_KEBS_CD_INSPECTION_AGROCHEM_ITEM_CHECKLIST (INSPECTION_GENERAL_ID, status) TABLESPACE qaimssdb_idx;

create table DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_GENERAL (ID),
    SERIAL_NUMBER                  VARCHAR2(200),
    PRODUCT_DESCRIPTION            VARCHAR2(3500),
    BRAND                          VARCHAR2(200),
    KS_EAS_APPLICABLE              VARCHAR2(200),
    QUANTITY_DECLARED              VARCHAR2(200),
    QUANTITY_VERIFIED              VARCHAR2(200),
    MFG_NAME_ADDRESS               VARCHAR2(200),
    BATCH_NO_MODEL_TYPE_REF        VARCHAR2(200),
    FIBER_COMPOSITION              VARCHAR2(200),
    INSTRUCTIONS_USE_MANUAL        VARCHAR2(200),
    WARRANTY_PERIOD_DOCUMENTATION   VARCHAR2(200),
    SAFETY_CAUTIONARY_REMARKS      VARCHAR2(200),
    DISPOSAL_INSTRUCTION           VARCHAR2(200),
    SIZE_CLASS_CAPACITY            VARCHAR2(200),
    CERT_MARKS_PVOC_DOC            VARCHAR2(200),
    SAMPLED                        VARCHAR2(200),
    REMARKS                        VARCHAR2(200),
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

create sequence DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST_idx on DAT_KEBS_CD_INSPECTION_ENGINEERING_ITEM_CHECKLIST (INSPECTION_GENERAL_ID, status) TABLESPACE qaimssdb_idx;

create table DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_GENERAL (ID),
    SERIAL_NUMBER                  VARCHAR2(200),
    PRODUCT_DESCRIPTION            VARCHAR2(3500),
    BRAND                          VARCHAR2(200),
    KS_EAS_APPLICABLE              VARCHAR2(200),
    QUANTITY_DECLARED              VARCHAR2(200),
    QUANTITY_VERIFIED              VARCHAR2(200),
    PACKAGING_LABELLING             VARCHAR2(200),
    PHYSICAL_CONDITION             VARCHAR2(200),
    DEFECTS                        VARCHAR2(200),
    PRESENCE_ABSENCE_BANNED        VARCHAR2(200),
    DOCUMENTATION                  VARCHAR2(200),
    REMARKS                        VARCHAR2(200),
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

create sequence DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_idx on DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST (INSPECTION_GENERAL_ID, status) TABLESPACE qaimssdb_idx;

create table DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_GENERAL (ID),
    SERIAL_NUMBER                  VARCHAR2(200),
    MAKE_VEHICLE                   VARCHAR2(200),
    CHASSIS_NO                      VARCHAR2(200),
    ENGINE_NO_CAPACITY             VARCHAR2(200),
    MANUFACTURE_DATE               DATE,
    REGISTRATION_DATE              DATE,
    ODEMETRE_READING               VARCHAR2(200),
    DRIVE_RHD_LHD                  VARCHAR2(200),
    TRANSMISSION_AUTO_MANUAL        VARCHAR2(200),
    COLOUR                         VARCHAR2(200),
    OVERALL_APPEARANCE              VARCHAR2(200),
    REMARKS                        VARCHAR2(200),
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

create sequence DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_idx on DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST (INSPECTION_GENERAL_ID, status) TABLESPACE qaimssdb_idx;

alter table DAT_KEBS_CD_INSPECTION_GENERAL
	add OVERALL_REMARKS varchar2(1000)
/

create table DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_MINISTRY_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL (ID),
    OVERALL_APPEARANCE_STATUS    VARCHAR2(200 char),
    OVERALL_APPEARANCE_REMARKS   VARCHAR2(200 char),
    OVERALL_APPEARANCE_FIRST_REINSPECTION   VARCHAR2(200 char),
    OVERALL_APPEARANCE_SECOND_REINSPECTION   VARCHAR2(200 char),
    CONDITION_OF_PAINT_STATUS    VARCHAR2(200 char),
    CONDITION_OF_PAINT_REMARKS   VARCHAR2(200 char),
    CONDITION_OF_PAINT_FIRST_REINSPECTION   VARCHAR2(200 char),
    CONDITION_OF_PAINT_SECOND_REINSPECTION   VARCHAR2(200 char),
    DOORS_STATUS                 VARCHAR2(200 char),
    DOORS_REMARKS                VARCHAR2(200 char),
    DOORS_FIRST_REINSPECTION   VARCHAR2(200 char),
    DOORS_SECOND_REINSPECTION   VARCHAR2(200 char),
    WINDOWS_STATUS               VARCHAR2(200 char),
    WINDOWS_REMARKS              VARCHAR2(200 char),
    WINDOWS_FIRST_REINSPECTION   VARCHAR2(200 char),
    WINDOWS_SECOND_REINSPECTION   VARCHAR2(200 char),
    SUNROOF_STATUS               VARCHAR2(200 char),
    SUNROOF_REMARKS              VARCHAR2(200 char),
    SUNROOF_FIRST_REINSPECTION   VARCHAR2(200 char),
    SUNROOF_SECOND_REINSPECTION   VARCHAR2(200 char),
    EXTERNAL_MIRRORS_STATUS      VARCHAR2(200 char),
    EXTERNAL_MIRRORS_REMARKS     VARCHAR2(200 char),
    EXTERNAL_MIRRORS_FIRST_REINSPECTION   VARCHAR2(200 char),
    EXTERNAL_MIRRORS_SECOND_REINSPECTION   VARCHAR2(200 char),
    GLASSES_STATUS               VARCHAR2(200 char),
    GLASSES_REMARKS              VARCHAR2(200 char),
    GLASSES_FIRST_REINSPECTION   VARCHAR2(200 char),
    GLASSES_SECOND_REINSPECTION   VARCHAR2(200 char),
    WIPERS_AND_WASHERS_STATUS    VARCHAR2(200 char),
    WIPERS_AND_WASHERS_REMARKS   VARCHAR2(200 char),
    WIPERS_AND_WASHERS_FIRST_REINSPECTION   VARCHAR2(200 char),
    WIPERS_AND_WASHERS_SECOND_REINSPECTION   VARCHAR2(200 char),
    SEATS_STATUS                 VARCHAR2(200 char),
    SEATS_REMARKS                VARCHAR2(200 char),
    SEATS_FIRST_REINSPECTION   VARCHAR2(200 char),
    SEATS_SECOND_REINSPECTION   VARCHAR2(200 char),
    MOULDING_STATUS              VARCHAR2(200 char),
    MOULDING_REMARKS             VARCHAR2(200 char),
    MOULDING_FIRST_REINSPECTION   VARCHAR2(200 char),
    MOULDING_SECOND_REINSPECTION   VARCHAR2(200 char),
    SAFETY_BELTS_STATUS          VARCHAR2(200 char),
    SAFETY_BELTS_REMARKS         VARCHAR2(200 char),
    SAFETY_BELTS_FIRST_REINSPECTION   VARCHAR2(200 char),
    SAFETY_BELTS_SECOND_REINSPECTION   VARCHAR2(200 char),
    STEERING_WHEEL_STATUS        VARCHAR2(200 char),
    STEERING_WHEEL_REMARKS       VARCHAR2(200 char),
    STEERING_WHEEL_FIRST_REINSPECTION   VARCHAR2(200 char),
    STEERING_WHEEL_SECOND_REINSPECTION   VARCHAR2(200 char),
    BRAKE_PEDAL_STATUS           VARCHAR2(200 char),
    BRAKE_PEDAL_REMARKS          VARCHAR2(200 char),
    BRAKE_PEDAL_FIRST_REINSPECTION   VARCHAR2(200 char),
    BRAKE_PEDAL_SECOND_REINSPECTION   VARCHAR2(200 char),
    CLUTCH_PEDAL_STATUS          VARCHAR2(200 char),
    CLUTCH_PEDAL_REMARKS         VARCHAR2(200 char),
    CLUTCH_PEDAL_FIRST_REINSPECTION   VARCHAR2(200 char),
    CLUTCH_PEDAL_SECOND_REINSPECTION   VARCHAR2(200 char),
    PARKING_BRAKE_LEVER_STATUS   VARCHAR2(200 char),
    PARKING_BRAKE_LEVER_REMARKS  VARCHAR2(200 char),
    PARKING_BRAKE_FIRST_REINSPECTION   VARCHAR2(200 char),
    PARKING_BRAKE_SECOND_REINSPECTION   VARCHAR2(200 char),
    HEADLIGHTS_STATUS            VARCHAR2(200 char),
    HEADLIGHTS_REMARKS           VARCHAR2(200 char),
    HEADLIGHTS_FIRST_REINSPECTION   VARCHAR2(200 char),
    HEADLIGHTS_SECOND_REINSPECTION   VARCHAR2(200 char),
    PARKING_LIGHTS_STATUS        VARCHAR2(200 char),
    PARKING_LIGHTS_REMARKS       VARCHAR2(200 char),
    PARKING_LIGHTS_FIRST_REINSPECTION   VARCHAR2(200 char),
    PARKING_LIGHTS_SECOND_REINSPECTION   VARCHAR2(200 char),
    DIRECTION_INDICATORS_STATUS  VARCHAR2(200 char),
    DIRECTION_INDICATORS_REMARKS VARCHAR2(200 char),
    DIRECTION_INDICATORS_FIRST_REINSPECTION   VARCHAR2(200 char),
    DIRECTION_INDICATORS_SECOND_REINSPECTION   VARCHAR2(200 char),
    REVERSING_LIGHT_STATUS       VARCHAR2(200 char),
    REVERSING_LIGHT_REMARKS      VARCHAR2(200 char),
    REVERSING_LIGHT_FIRST_REINSPECTION   VARCHAR2(200 char),
    REVERSING_LIGHT_SECOND_REINSPECTION   VARCHAR2(200 char),
    COURTESY_LIGHT_STATUS        VARCHAR2(200 char),
    COURTESY_LIGHT_REMARKS       VARCHAR2(200 char),
    COURTESY_LIGHT_FIRST_REINSPECTION   VARCHAR2(200 char),
    COURTESY_LIGHT_SECOND_REINSPECTION   VARCHAR2(200 char),
    REAR_NO_PLATE_LIGHT_REMARKS  VARCHAR2(200 char),
    REAR_NO_PLATE_LIGHT_STATUS   VARCHAR2(200 char),
    REAR_NO_PLATE_LIGHT_FIRST_REINSPECTION   VARCHAR2(200 char),
    REAR_NO_PLATE_LIGHT_SECOND_REINSPECTION   VARCHAR2(200 char),
    STOP_LIGHTS_STATUS           VARCHAR2(200 char),
    STOP_LIGHTS_REMARKS          VARCHAR2(200 char),
    STOP_LIGHTS_FIRST_REINSPECTION   VARCHAR2(200 char),
    STOP_LIGHTS_SECOND_REINSPECTION   VARCHAR2(200 char),
    FRONT_BUMPER_STATUS          VARCHAR2(200 char),
    FRONT_BUMPER_REMARKS         VARCHAR2(200 char),
    FRONT_BUMPER_FIRST_REINSPECTION   VARCHAR2(200 char),
    FRONT_BUMPER_SECOND_REINSPECTION   VARCHAR2(200 char),
    ROOF_RACK_STATUS             VARCHAR2(200 char),
    ROOF_RACK_REMARKS            VARCHAR2(200 char),
    ROOF_RACK_FIRST_REINSPECTION   VARCHAR2(200 char),
    ROOF_RACK_SECOND_REINSPECTION   VARCHAR2(200 char),
    ANTENNA_STATUS               VARCHAR2(200 char),
    ANTENNA_REMARKS              VARCHAR2(200 char),
    ANTENNA_FIRST_REINSPECTION   VARCHAR2(200 char),
    ANTENNA_SECOND_REINSPECTION   VARCHAR2(200 char),
    VAR_FIELD_1                  VARCHAR2(350 char),
    VAR_FIELD_2                  VARCHAR2(350 char),
    VAR_FIELD_3                  VARCHAR2(350 char),
    VAR_FIELD_4                  VARCHAR2(350 char),
    VAR_FIELD_5                  VARCHAR2(350 char),
    VAR_FIELD_6                  VARCHAR2(350 char),
    VAR_FIELD_7                  VARCHAR2(350 char),
    VAR_FIELD_8                  VARCHAR2(350 char),
    VAR_FIELD_9                  VARCHAR2(350 char),
    VAR_FIELD_10                 VARCHAR2(350 char),
    CREATED_BY                   VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON                   TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY                  VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON                  TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                    VARCHAR2(100 char)          default 'admin',
    DELETED_ON                   TIMESTAMP(6) WITH TIME ZONE
)TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_CD_INSPECTION_MINISTRY_ENGINE_COMPONENTS
(
    id  NUMBER PRIMARY KEY,
    INSPECTION_MINISTRY_GENERAL_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL (ID),
    BONNET_STATUS    VARCHAR2(200 char),
    BONNET_REMARKS   VARCHAR2(200 char),
    BONNET_FIRST_REINSPECTION   VARCHAR2(200 char),
    BONNET_SECOND_REINSPECTION   VARCHAR2(200 char),
    ENGINE_STATUS                    VARCHAR2(200 char),
    ENGINE_REMARKS                   VARCHAR2(200 char),
    ENGINE_FIRST_REINSPECTION   VARCHAR2(200 char),
    ENGINE_SECOND_REINSPECTION   VARCHAR2(200 char),
    BATTERY_STATUS                   VARCHAR2(200 char),
    BATTERY_REMARKS                  VARCHAR2(200 char),
    BATTERY_FIRST_REINSPECTION   VARCHAR2(200 char),
    BATTERY_SECOND_REINSPECTION   VARCHAR2(200 char),
    BATTERY_CARRIER_STATUS           VARCHAR2(200 char),
    BATTERY_CARRIER_REMARKS          VARCHAR2(200 char),
    BATTERY_CARRIER_FIRST_REINSPECTION   VARCHAR2(200 char),
    BATTERY_CARRIER_SECOND_REINSPECTION   VARCHAR2(200 char),
    WIRING_HARNESS_STATUS            VARCHAR2(200 char),
    WIRING_HARNESS_REMARKS           VARCHAR2(200 char),
    WIRING_HARNESS_FIRST_REINSPECTION   VARCHAR2(200 char),
    WIRING_HARNESS_SECOND_REINSPECTION   VARCHAR2(200 char),
    STARTER_MOTOR_STATUS             VARCHAR2(200 char),
    STARTER_MOTOR_REMARKS            VARCHAR2(200 char),
    STARTER_MOTOR_FIRST_REINSPECTION   VARCHAR2(200 char),
    STARTER_MOTOR_SECOND_REINSPECTION   VARCHAR2(200 char),
    ALTERNATOR_STATUS                VARCHAR2(200 char),
    ALTERNATOR_REMARKS               VARCHAR2(200 char),
    ALTERNATOR_FIRST_REINSPECTION   VARCHAR2(200 char),
    ALTERNATOR_SECOND_REINSPECTION   VARCHAR2(200 char),
    RADIATOR_STATUS                  VARCHAR2(200 char),
    RADIATOR_REMARKS                 VARCHAR2(200 char),
    RADIATOR_FIRST_REINSPECTION   VARCHAR2(200 char),
    RADIATOR_SECOND_REINSPECTION   VARCHAR2(200 char),
    RADIATOR_HOSES_STATUS            VARCHAR2(200 char),
    RADIATOR_HOSES_REMARKS           VARCHAR2(200 char),
    RADIATOR_HOSES_FIRST_REINSPECTION   VARCHAR2(200 char),
    RADIATOR_HOSES_SECOND_REINSPECTION   VARCHAR2(200 char),
    WATER_PUMP_STATUS                VARCHAR2(200 char),
    WATER_PUMP_REMARKS               VARCHAR2(200 char),
    WATER_PUMP_FIRST_REINSPECTION   VARCHAR2(200 char),
    WATER_PUMP_SECOND_REINSPECTION   VARCHAR2(200 char),
    CARBURETOR_STATUS                VARCHAR2(200 char),
    CARBURETOR_REMARKS               VARCHAR2(200 char),
    CARBURETOR_FIRST_REINSPECTION   VARCHAR2(200 char),
    CARBURETOR_SECOND_REINSPECTION   VARCHAR2(200 char),
    HIGH_TENSION_CABLES_STATUS       VARCHAR2(200 char),
    HIGH_TENSION_CABLES_REMARKS      VARCHAR2(200 char),
    HIGH_TENSION_CABLES_FIRST_REINSPECTION   VARCHAR2(200 char),
    HIGH_TENSION_CABLES_SECOND_REINSPECTION   VARCHAR2(200 char),
    AC_CONDENSER_STATUS              VARCHAR2(200 char),
    AC_CONDENSER_REMARKS             VARCHAR2(200 char),
    AC_CONDENSER_FIRST_REINSPECTION   VARCHAR2(200 char),
    AC_CONDENSER_SECOND_REINSPECTION   VARCHAR2(200 char),
    POWER_STEERING_STATUS            VARCHAR2(200 char),
    POWER_STEERING_REMARKS           VARCHAR2(200 char),
    POWER_STEERING_FIRST_REINSPECTION   VARCHAR2(200 char),
    POWER_STEERING_SECOND_REINSPECTION   VARCHAR2(200 char),
    BRAKE_MASTER_CYLINDER_STATUS     VARCHAR2(200 char),
    BRAKE_MASTER_CYLINDER_REMARKS    VARCHAR2(200 char),
    BRAKE_MASTER_CYLINDER_FIRST_REINSPECTION   VARCHAR2(200 char),
    BRAKE_MASTER_CYLINDER_SECOND_REINSPECTION   VARCHAR2(200 char),
    CLUTCH_MASTER_CYLINDER_STATUS    VARCHAR2(200 char),
    CLUTCH_MASTER_CYLINDER_REMARKS   VARCHAR2(200 char),
    CLUTCH_MASTER_CYLINDER_FIRST_REINSPECTION   VARCHAR2(200 char),
    CLUTCH_MASTER_CYLINDER_SECOND_REINSPECTION   VARCHAR2(200 char),
    BRAKE_SYSTEM_STATUS              VARCHAR2(200 char),
    BRAKE_SYSTEM_REMARKS             VARCHAR2(200 char),
    BRAKE_SYSTEM_FIRST_REINSPECTION   VARCHAR2(200 char),
    BRAKE_SYSTEM_SECOND_REINSPECTION   VARCHAR2(200 char),
    FUEL_PIPES_STATUS                VARCHAR2(200 char),
    FUEL_PIPES_REMARKS               VARCHAR2(200 char),
    FUEL_PIPES_FIRST_REINSPECTION   VARCHAR2(200 char),
    FUEL_PIPES_SECOND_REINSPECTION   VARCHAR2(200 char),
    FLEXIBLE_BRAKE_PIPES_STATUS      VARCHAR2(200 char),
    FLEXIBLE_BRAKE_PIPES_REMARKS     VARCHAR2(200 char),
    FLEXIBLE_BRAKE_PIPES_FIRST_REINSPECTION   VARCHAR2(200 char),
    FLEXIBLE_BRAKE_PIPES_SECOND_REINSPECTION   VARCHAR2(200 char),
    WINDSCREEN_WASHER_BOTTLE_STATUS  VARCHAR2(200 char),
    WINDSCREEN_WASHER_BOTTLE_REMARKS VARCHAR2(200 char),
    WINDSCREEN_WASHER_BOTTLE_FIRST_REINSPECTION   VARCHAR2(200 char),
    WINDSCREEN_WASHER_BOTTLE_SECOND_REINSPECTION   VARCHAR2(200 char),
    BOOT_LID_STATUS                  VARCHAR2(200 char),
    BOOT_LID_REMARKS                 VARCHAR2(200 char),
    BOOT_LID_FIRST_REINSPECTION   VARCHAR2(200 char),
    BOOT_LID_SECOND_REINSPECTION   VARCHAR2(200 char),
    JACK_AND_HANDLE_STATUS           VARCHAR2(200 char),
    JACK_AND_HANDLE_REMARKS          VARCHAR2(200 char),
    JACK_AND_HANDLE_FIRST_REINSPECTION   VARCHAR2(200 char),
    JACK_AND_HANDLE_SECOND_REINSPECTION   VARCHAR2(200 char),
    WHEEL_WRENCH_STATUS              VARCHAR2(200 char),
    WHEEL_WRENCH_REMARKS             VARCHAR2(200 char),
    WHEEL_WRENCH_FIRST_REINSPECTION   VARCHAR2(200 char),
    WHEEL_WRENCH_SECOND_REINSPECTION   VARCHAR2(200 char),
    TOOL_KIT_STATUS                  VARCHAR2(200 char),
    TOOL_KIT_REMARKS                 VARCHAR2(200 char),
    TOOL_KIT_FIRST_REINSPECTION   VARCHAR2(200 char),
    TOOL_KIT_SECOND_REINSPECTION   VARCHAR2(200 char),
    LIFE_SAVER_STATUS                VARCHAR2(200 char),
    LIFE_SAVER_REMARKS               VARCHAR2(200 char),
    LIFE_SAVER_FIRST_REINSPECTION   VARCHAR2(200 char),
    LIFE_SAVER_SECOND_REINSPECTION   VARCHAR2(200 char),
    VAR_FIELD_1                  VARCHAR2(350 char),
    VAR_FIELD_2                  VARCHAR2(350 char),
    VAR_FIELD_3                  VARCHAR2(350 char),
    VAR_FIELD_4                  VARCHAR2(350 char),
    VAR_FIELD_5                  VARCHAR2(350 char),
    VAR_FIELD_6                  VARCHAR2(350 char),
    VAR_FIELD_7                  VARCHAR2(350 char),
    VAR_FIELD_8                  VARCHAR2(350 char),
    VAR_FIELD_9                  VARCHAR2(350 char),
    VAR_FIELD_10                 VARCHAR2(350 char),
    CREATED_BY                   VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON                   TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY                  VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON                  TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                    VARCHAR2(100 char)          default 'admin',
    DELETED_ON                   TIMESTAMP(6) WITH TIME ZONE
)TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_CD_INSPECTION_MINISTRY_ENGINE_COMPONENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_CD_INSPECTION_MINISTRY_ENGINE_COMPONENTS_seq_trg
    before
        insert
    on DAT_KEBS_CD_INSPECTION_MINISTRY_ENGINE_COMPONENTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_CD_INSPECTION_MINISTRY_ENGINE_COMPONENTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

alter table DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
	add MINISTRY_REPORT_FILE blob
/
alter table DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
	add MINISTRY_REPORT_SUBMIT_STATUS int
/

alter table DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
	add MINISTRY_REPORT_REINSPECTION_STATUS number(2)
/

alter table DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST
	add MINISTRY_REPORT_REINSPECTION_REMARKS varchar2(1000)
/

alter table DAT_KEBS_CD_INSPECTION_GENERAL
	add COMPLIANCE_STATUS number(2)
/
alter table DAT_KEBS_CD_INSPECTION_GENERAL
	add COMPLIANCE_RECOMMENDATIONS varchar2(1000)
/

alter table DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS
	add ASSIGNER number(*)
/

alter table DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS
	add constraint DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS_DAT_KEBS_USERS_ID_FK
		foreign key (ASSIGNER) references DAT_KEBS_USERS
/

create table dat_kebs_cd_local_cor
(
    id                    NUMBER  PRIMARY KEY,
    MV_INSPECTION_CHECKLIST_ID  NUMBER REFERENCES DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST (ID),
    cor_serial_no           VARCHAR2(200),
    cor_issue_date              DATE,
    cor_expiry_date              DATE,
    importer_name                VARCHAR2(200),
    importer_address        VARCHAR2(200),
    importer_phone        VARCHAR2(200),
    exporter_name                VARCHAR2(200),
    exporter_address        VARCHAR2(200),
    exporter_email        VARCHAR2(200),
    chassis_no                VARCHAR2(200),
    engine_no_model                VARCHAR2(200),
    engine_capacity                VARCHAR2(200),
    year_of_manufacture                VARCHAR2(200),
    year_of_first_registration                VARCHAR2(200),
    customs_ie_no                VARCHAR2(200),
    country_of_supply                VARCHAR2(200),
    body_type                VARCHAR2(200),
    fuel                VARCHAR2(200),
    tare_weight                VARCHAR2(200),
    type_of_vehicle                VARCHAR2(200),
    idf_no                  VARCHAR2(200),
    ucr_number          VARCHAR2(350 CHAR),
    make          VARCHAR2(350 CHAR),
    model          VARCHAR2(350 CHAR),
    inspection_mileage          VARCHAR2(350 CHAR),
    units_of_mileage          VARCHAR2(350 CHAR),
    color          VARCHAR2(350 CHAR),
    axle_no          VARCHAR2(350 CHAR),
    transmission          VARCHAR2(350 CHAR),
    no_of_passengers          VARCHAR2(350 CHAR),
    prev_reg_no          VARCHAR2(350 CHAR),
    prev_country_of_reg         VARCHAR2(350 CHAR),
    inspection_details             varchar2(3800),
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
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_local_cor_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_local_cor_seq_trg
    before
        insert
    on dat_kebs_cd_local_cor
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_local_cor_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_local_cor_idx on dat_kebs_cd_local_cor ( MV_INSPECTION_CHECKLIST_ID, status) TABLESPACE qaimssdb_idx;
/