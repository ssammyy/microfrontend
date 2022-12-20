create table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION
(
    ID                        NUMBER PRIMARY KEY ,
    region_id             NUMBER references CFG_KEBS_REGIONS (ID),
    county_id             NUMBER references CFG_KEBS_COUNTIES (ID),
    town_id               NUMBER references CFG_KEBS_TOWNS (ID),
    reference_Number         VARCHAR2(350 CHAR) UNIQUE,
    batch_file_year         VARCHAR2(350 CHAR),
    USER_TASK_ID         NUMBER,
    batch_closed            NUMBER(2, 0),
    TRANSACTION_DATE          DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status                    NUMBER(2, 0),
    REMARKS                   VARCHAR2(3800 CHAR),
    var_field_1               VARCHAR2(350 CHAR),
    var_field_2               VARCHAR2(350 CHAR),
    var_field_3               VARCHAR2(350 CHAR),
    var_field_4               VARCHAR2(350 CHAR),
    var_field_5               VARCHAR2(350 CHAR),
    var_field_6               VARCHAR2(350 CHAR),
    var_field_7               VARCHAR2(350 CHAR),
    var_field_8               VARCHAR2(350 CHAR),
    var_field_9               VARCHAR2(350 CHAR),
    var_field_10              VARCHAR2(350 CHAR),
    created_by                VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on                TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by          VARCHAR2(100 CHAR),
    last_modified_on          TIMESTAMP(6) WITH TIME ZONE,
    update_by                 VARCHAR2(100 CHAR),
    updated_on                TIMESTAMP(6) WITH TIME ZONE,
    delete_by                 VARCHAR2(100 CHAR),
    deleted_on                TIMESTAMP(6) WITH TIME ZONE,
    VERSION                   NUMBER
) TABLESPACE qaimssdb_data;
create sequence STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ_trg
    before
        insert
    on STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION
    for each row
begin
    if inserting then
        if :new.id is null then
    select STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ.nextval
    into :new.id
    from dual;

end if;

end if;
end;

create trigger stg_kebs_master_ms_batch_fuel_inspection_seq_trg
    before
        insert
    on stg_kebs_master_ms_batch_fuel_inspection_seq
    for each row
begin
    if inserting then
        if :new.id is null then
    select stg_kebs_master_ms_batch_fuel_inspection_seq.nextval
    into :new.id
    from dual;

end if;

end if;
end;

create index stg_kebs_master_ms_batch_fuel_inspection_seq_idx on stg_kebs_master_ms_batch_fuel_inspection (status, region_id,county_id,town_id,batch_file_name) TABLESPACE qaimssdb_idx;
/

alter table DAT_KEBS_MS_FUEL_INSPECTION add BATCH_ID NUMBER REFERENCES STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION(ID) ;/
alter table DAT_KEBS_QA_SAMPLE_SUBMISSION add FUEL_INSPECTION_ID NUMBER REFERENCES DAT_KEBS_MS_FUEL_INSPECTION(ID) ;/
alter table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION add YEAR_NAME_ID NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES(ID) ;/
alter table DAT_KEBS_MS_FUEL_INSPECTION add USER_TASK_ID NUMBER ;/
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add LAB_RESULTS_STATUS NUMBER; /
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION ALTER SENDERS_DATE DATE ;/

alter table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION add YEAR_NAME_ID NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES(ID);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add MS_FUEL_INSPECTION_ID NUMBER REFERENCES STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION(ID);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_BATCH_NUMBER_ID NUMBER REFERENCES DAT_KEBS_INVOICE_BATCH_DETAILS(ID);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add PAYMENT_STATUS NUMBER;
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_DATE DATE;
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_REMARKS VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_NUMBER VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add RECEIPT_NO VARCHAR2(100 CHAR);
alter table DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS add SSF_FILE_STATUS VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add ORDINARY_STATUS NUMBER(2,0);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add SSF_UPLOADS NUMBER(2,0);
alter table DAT_KEBS_MS_FUEL_INSPECTION add INSPECTION_COMPLETE_STATUS NUMBER(2,0);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add VERSION_NUMBER NUMBER;

diofficerincharge@gmail.com
password


-- alter table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION add YEAR_NAME_ID NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES(ID)
;
-- alter table DAT_KEBS_MS_ONSITE_UPLOADS add MS_FUEL_INSPECTION_ID NUMBER REFERENCES STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION(ID)
;
-- alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_BATCH_NUMBER_ID NUMBER REFERENCES DAT_KEBS_INVOICE_BATCH_DETAILS(ID)
;
-- alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add PAYMENT_STATUS NUMBER;
-- alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_DATE DATE;
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_REMARKS VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_NUMBER VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add RECEIPT_NO VARCHAR2(100 CHAR);
alter table DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS add SSF_FILE_STATUS VARCHAR2(100 CHAR);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add ORDINARY_STATUS NUMBER(2,0);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add SSF_UPLOADS NUMBER(2,0);
alter table DAT_KEBS_MS_FUEL_INSPECTION add INSPECTION_COMPLETE_STATUS NUMBER(2,0);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add VERSION_NUMBER NUMBER


create table DAT_KEBS_MS_UPLOADS
(
    ID                       NUMBER PRIMARY KEY,
    FILEPATH                 VARCHAR2(200),
    DESCRIPTION              VARCHAR2(200),
    NAME                     VARCHAR2(200),
    FILE_TYPE                VARCHAR2(200),
    DOCUMENT_TYPE            VARCHAR2(200),
    DOCUMENT                 BLOB,
    TRANSACTION_DATE         DATE,
    MS_WORKPLAN_GENERATED_ID NUMBER references DAT_KEBS_MS_WORKPLAN_GENARATED (ID),
    MS_FUEL_INSPECTION_ID    NUMBER references DAT_KEBS_MS_FUEL_INSPECTION (ID),
    VERSION_NUMBER           NUMBER,
    ORDINARY_STATUS          NUMBER(2, 0),
    SSF_UPLOADS              NUMBER(2, 0),
    STATUS                   NUMBER(2, 0),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MS_UPLOADS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_UPLOADS_SEQ_trg
    before
        insert
    on DAT_KEBS_MS_UPLOADS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_UPLOADS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_KEBS_MS_UPLOADS_seq_idx on DAT_KEBS_MS_UPLOADS (MS_WORKPLAN_GENERATED_ID,MS_FUEL_INSPECTION_ID,
ORDINARY_STATUS,SSF_UPLOADS,STATUS) TABLESPACE qaimssdb_idx;
/


create table DAT_KEBS_MS_FUEL_TEAMS
(
    ID                       NUMBER PRIMARY KEY,
    TEAM_NAME                VARCHAR2(200),
    START_DATE         DATE,
    END_DATE         DATE,
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MS_FUEL_TEAMS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
-- DAT_KEBS_MS_FUEL_TEAMS_SEQ_TRG



create or replace trigger DAT_KEBS_MS_FUEL_TEAMS_SEQ_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_TEAMS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_TEAMS_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;

create index DAT_KEBS_MS_FUEL_TEAMS_seq_idx on DAT_KEBS_MS_FUEL_TEAMS (TEAM_NAME,STATUS) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_MS_FUEL_TEAMS_COUNTY
(
    ID                       NUMBER PRIMARY KEY,
    region_id             NUMBER references CFG_KEBS_REGIONS (ID),
    county_id             NUMBER references CFG_KEBS_COUNTIES (ID),
    TEAM_ID             NUMBER references DAT_KEBS_MS_FUEL_TEAMS (ID),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MS_FUEL_TEAMS_COUNTY_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_TEAMS_COUNTY_SEQ_trg
    before
        insert
    on DAT_KEBS_MS_FUEL_TEAMS_COUNTY
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_TEAMS_COUNTY_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;

create index DAT_KEBS_MS_FUEL_TEAMS_COUNTY_seq_idx on DAT_KEBS_MS_FUEL_TEAMS_COUNTY (TEAM_ID,region_id,county_id,STATUS) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS
(
    ID                       NUMBER PRIMARY KEY,
    PRODUCT_NAME              VARCHAR2(200),
    EXPORT_MARKER_TEST              VARCHAR2(200),
    DOMESTIC_KEROSENE_MARKER_TEST    VARCHAR2(200),
    SULPHUR_MARKER_TEST    VARCHAR2(200),
    EXPORT_MARKER_TEST_STATUS              NUMBER(2, 0),
    DOMESTIC_KEROSENE_MARKER_TEST_STATUS    NUMBER(2, 0),
    SULPHUR_MARKER_TEST_STATUS    NUMBER(2, 0),
    OVERALL_COMPLIANCE_STATUS    NUMBER(2, 0),
    FUEL_INSPECTION_ID        NUMBER references DAT_KEBS_MS_FUEL_INSPECTION (ID),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ_trg
    before
        insert
    on DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;

create index DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_seq_idx on DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS (OVERALL_COMPLIANCE_STATUS,FUEL_INSPECTION_ID,STATUS) TABLESPACE qaimssdb_idx;
/



alter table DAT_KEBS_MS_WORKPLAN_PRODUCTS
    add REFERENCE_NO VARCHAR2(20)
/


alter table DAT_KEBS_MS_WORKPLAN_PRODUCTS
    add SSF_ID NUMBER REFERENCES DAT_KEBS_MS_SAMPLE_SUBMISSION(ID)
/

alter table DAT_KEBS_MS_FUEL_INSPECTION
    add MS_COUNTY_ID NUMBER REFERENCES DAT_KEBS_MS_FUEL_TEAMS_COUNTY(ID)
/

-- alter table DAT_KEBS_MS_FUEL_INSPECTION
--     add TOWN_ID NUMBER REFERENCES CFG_KEBS_TOWNS(ID)
-- /


create table CFG_MS_PREDEFINED_RESOURCES_REQUIRED
(
    ID                       NUMBER PRIMARY KEY,
    RESOURCE_NAME              VARCHAR2(200),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ_trg
    before
        insert
    on CFG_MS_PREDEFINED_RESOURCES_REQUIRED
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index CFG_MS_PREDEFINED_RESOURCES_REQUIRED_seq_idx on CFG_MS_PREDEFINED_RESOURCES_REQUIRED (STATUS) TABLESPACE qaimssdb_idx;
/

create table DAT_MS_SEIZURE
(
    ID                       NUMBER PRIMARY KEY,
    MARKET_TOWN_CENTER       VARCHAR2(200),
    NAME_OF_OUTLET       VARCHAR2(200),
    DESCRIPTION_PRODUCTS_SEIZED VARCHAR2(200),
    BRAND       VARCHAR2(200),
    SECTOR       VARCHAR2(200),
    REASON_SEIZURE       VARCHAR2(200),
    NAME_SEIZING_OFFICER       VARCHAR2(200),
    SEIZURE_SERIAL        VARCHAR2(200),
    QUANTITY        VARCHAR2(200),
    UNIT        VARCHAR2(200),
    ESTIMATED_COST          VARCHAR2(200),
    CURRENT_LOCATION          VARCHAR2(200),
    PRODUCTS_DESTRUCTION         VARCHAR2(200),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_MS_SEIZURE_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_MS_SEIZURE_SEQ_trg
    before
        insert
    on DAT_MS_SEIZURE
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_MS_SEIZURE_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index DAT_MS_SEIZURE_seq_idx on DAT_MS_SEIZURE (STATUS) TABLESPACE qaimssdb_idx;
/

create table DAT_MS_TASK_NOTIFICATIONS
(
    ID                       NUMBER PRIMARY KEY,
    NOTIFICATION_MSG         VARCHAR2(200),
    NOTIFICATION_BODY       VARCHAR2(200),
    NOTIFICATION_NAME       VARCHAR2(200),
    NOTIFICATION_TYPE       VARCHAR2(200),
    FROM_USER_ID              NUMBER REFERENCES DAT_KEBS_USERS(ID),
    TO_USER_ID              NUMBER REFERENCES DAT_KEBS_USERS(ID),
    READ_STATUS              NUMBER(2, 0),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_MS_TASK_NOTIFICATIONS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_MS_TASK_NOTIFICATIONS_SEQ_trg
    before
        insert
    on DAT_MS_TASK_NOTIFICATIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_MS_TASK_NOTIFICATIONS_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index DAT_MS_TASK_NOTIFICATIONS_SEQ_IDX on DAT_MS_TASK_NOTIFICATIONS (STATUS,TO_USER_ID,FROM_USER_ID,READ_STATUS, NOTIFICATION_TYPE) TABLESPACE qaimssdb_idx;
/


create table DAT_KEBS_MS_WORKPLAN_PRODUCTS
(
    ID                       NUMBER PRIMARY KEY,
    PRODUCT_NAME             VARCHAR2(200),
    RECOMMENDATION       VARCHAR2(3000),
    DESTRUCTION_RECOMMENDED       NUMBER(2, 0),
    HOD_RECOMMENDATION_STATUS       NUMBER(2, 0),
    HOD_RECOMMENDATION_REMARKS VARCHAR2(1000),
    DIRECTOR_RECOMMENDATION_STATUS       NUMBER(2, 0),
    DIRECTOR_RECOMMENDATION_REMARKS VARCHAR2(1000),
    CLIENT_APPEALED        NUMBER(2, 0),
    DESTRUCTION_STATUS        NUMBER(2, 0),
    APPEAL_STATUS        NUMBER(2, 0),
    DESTRUCTION_NOTIFICATION_STATUS        NUMBER(2, 0),
    DESTRUCTION_NOTIFICATION_DOC_ID        NUMBER REFERENCES DAT_KEBS_MS_UPLOADS(ID),
    DESTRUCTION_CLIENT_EMAIL       VARCHAR2(200),
    DESTRUCTION_CLIENT_FULL_NAME       VARCHAR2(200),
    DESTRUCTION_NOTIFICATION_DATE       DATE,
    DESTRUCTION_DOC_ID        NUMBER REFERENCES DAT_KEBS_MS_UPLOADS(ID),
    DESTRUCTED_STATUS        NUMBER(2, 0),
    STATUS                   NUMBER(2, 0),
    DESCRIPTION              VARCHAR2(200),
    VAR_FIELD_1              VARCHAR2(350 char),
    VAR_FIELD_2              VARCHAR2(350 char),
    VAR_FIELD_3              VARCHAR2(350 char),
    VAR_FIELD_4              VARCHAR2(350 char),
    VAR_FIELD_5              VARCHAR2(350 char),
    VAR_FIELD_6              VARCHAR2(350 char),
    VAR_FIELD_7              VARCHAR2(350 char),
    VAR_FIELD_8              VARCHAR2(350 char),
    VAR_FIELD_9              VARCHAR2(350 char),
    VAR_FIELD_10             VARCHAR2(350 char),
    CREATED_BY               VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON               TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY              VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON              TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                VARCHAR2(100 char)          default 'admin',
    DELETED_ON               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ_trg
    before
        insert
    on DAT_KEBS_MS_WORKPLAN_PRODUCTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index DAT_KEBS_MS_WORKPLAN_PRODUCTS_seq_idx on DAT_KEBS_MS_WORKPLAN_PRODUCTS (STATUS,DESTRUCTION_RECOMMENDED,
    CLIENT_APPEALED,
DESTRUCTION_STATUS,
DESTRUCTION_NOTIFICATION_DOC_ID,
DESTRUCTION_DOC_ID,
DESTRUCTED_STATUS) TABLESPACE qaimssdb_idx;
/



alter table DAT_KEBS_MS_WORKPLAN_GENARATED
    add TIME_ACTIVITY_END_DATE DATE;
/


/***************************CREATED VIEWS FOR DOWNLOAD*****************************************/

-- create OR REPLACE view MS_SAMPLE_SUBMISSION as
SELECT nvl(TO_CHAR(samp.id),'N/A') AS id,nvl(samp.name_product,'N/A') AS name_product,nvl(samp.packaging,'N/A') AS packaging,nvl(samp.labelling_identification,'N/A') AS labelling_identification,nvl(samp.file_ref_number,'N/A') AS file_ref_number,nvl(samp.references_standards,'N/A') AS references_standards,nvl(samp.size_test_sample,'N/A') AS size_test_sample,
       nvl(samp.size_ref_sample,'N/A') AS size_ref_sample,nvl(samp.condition,'N/A') AS condition,nvl(samp.sample_references,'N/A') AS sample_references,nvl(samp.senders_name,'N/A') AS senders_name,nvl(samp.designation,'N/A') AS designation,nvl(samp.address,'N/A') AS address,nvl(TO_CHAR(TRUNC(samp.senders_date),'DD/MM/YYYY'),'N/A') AS senders_date,
       nvl(samp.receivers_name,'N/A') AS receivers_name,nvl(TO_CHAR(samp.test_charges_ksh),'N/A') AS test_charges_ksh,nvl(samp.receipt_lpo_number,'N/A') AS receipt_lpo_number,nvl(samp.invoice_number,'N/A') AS invoice_number,nvl(samp.disposal,'N/A') AS disposal,nvl(samp.remarks,'N/A') AS remarks,
       nvl(TO_CHAR(samp.sample_collection_number),'N/A') AS sample_collection_number,nvl(samp.bs_number,'N/A') AS bs_number,param.parameters,param.laboratory_name
FROM DAT_KEBS_MS_SAMPLE_SUBMISSION samp
         INNER JOIN DAT_KEBS_MS_LABORATORY_PARAMETERS param ON samp.ID = param.SAMPLE_SUBMISSION_ID;
/

create OR REPLACE view MS_FIELD_REPORT as
SELECT  nvl(TO_CHAR(rep.MS_WORKPLAN_GENERATED_ID),'N/A') AS MS_WORKPLAN_GENERATED_ID,
        nvl(TO_CHAR(rep.CREATED_USER_ID),'N/A') AS CREATED_USER_ID,
        nvl(TO_CHAR(rep.ID),'N/A') AS ID,
        nvl(rep.REPORT_REFERENCE,'N/A') AS REPORT_REFERENCE,
        nvl(rep.REPORT_TO,'N/A') AS REPORT_TO,
        nvl(rep.REPORT_THROUGH,'N/A') AS REPORT_THROUGH,
        nvl(rep.REPORT_FROM,'N/A') AS REPORT_FROM,
        nvl(rep.REPORT_SUBJECT,'N/A') AS REPORT_SUBJECT,
        nvl(rep.REPORT_TITLE,'N/A') AS REPORT_TITLE,
        nvl(TO_CHAR(TRUNC(rep.REPORT_DATE),'DD/MM/YYYY'),'N/A') AS REPORT_DATE,
        nvl(rep.REPORT_REGION,'N/A') AS REPORT_REGION,
        nvl(rep.REPORT_DEPARTMENT,'N/A') AS REPORT_DEPARTMENT,
        nvl(rep.REPORT_FUNCTION,'N/A') AS REPORT_FUNCTION,
        nvl(rep.BACKGROUND_INFORMATION,'N/A') AS BACKGROUND_INFORMATION,
        nvl(rep.OBJECTIVE_INVESTIGATION,'N/A') AS OBJECTIVE_INVESTIGATION,
        nvl(TO_CHAR(TRUNC(rep.DATE_INVESTIGATION_INSPECTION),'DD/MM/YYYY'),'N/A') AS DATE_INVESTIGATION_INSPECTION,
        nvl(rep.METHODOLOGY_EMPLOYED,'N/A') AS METHODOLOGY_EMPLOYED,
        nvl(rep.ADDITIONAL_INFORMATION,'N/A') AS ADDITIONAL_INFORMATION,
        nvl(rep.CONCLUSION,'N/A') AS CONCLUSION,
        nvl(rep.RECOMMENDATIONS,'N/A') AS RECOMMENDATIONS,
        nvl(rep.STATUS_ACTIVITY,'N/A') AS STATUS_ACTIVITY,
        nvl(rep.FINAL_REMARK_HOD,'N/A') AS FINAL_REMARK_HOD,
        nvl(rep.FINDINGS,'N/A') AS FINDINGS,
        nvl(rep.KEBS_INSPECTORS,'N/A') AS KEBS_INSPECTORS,
        nvl(rep.VAR_FIELD_1,'N/A') AS VAR_FIELD_1,
        nvl(TO_CHAR(TRUNC(rep.CREATED_ON),'DD/MM/YYYY'),'N/A') AS CREATED_ON,
        nvl(rep.CREATED_BY,'N/A') AS CREATED_BY,
        nvl(rep.MODIFIED_BY,'N/A') AS MODIFIED_BY,
        nvl(TO_CHAR(TRUNC(rep.MODIFIED_ON),'DD/MM/YYYY'),'N/A') AS MODIFIED_ON
FROM DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT rep;
/


/***************************TABLES TO GENERATE MS REPORTS MS REPORTS*****************************************/
/******4.	Timeliness of activities ***/

-- create OR REPLACE view MS_ACKNOWLEDGEMENT_TIMELINE_VIEW as
SELECT a.REFERENCE_NUMBER,a.COMPLAINT_TITLE,a.TARGETED_PRODUCTS,a.TRANSACTION_DATE,a.APPROVED_DATE,a.REJECTED_DATE,a.ASSIGNED_IO,
       CASE
           WHEN a.APPROVED = 1 THEN 'APPROVED'
           WHEN a.REJECTED = 1 THEN 'REJECTED'
           WHEN a.MANDATE_FOR_OGA = 1 THEN 'OGA MANDATE'
           WHEN a.AMENDMENT_STATUS = 1 THEN 'REJECTED FOR AMENDMENT'
           ELSE 'PENDING ACKNOWLEDGEMENT' END AS ACKNOWLEDGEMENT_TYPE,
       b.REGION, b.COUNTY,b.TOWN,a.COMPLAINT_DEPARTMENT,a.DIVISION,
   CASE
    WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)))
    WHEN a.REJECTED = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
    WHEN a.MANDATE_FOR_OGA = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
    WHEN a.AMENDMENT_STATUS = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
    ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE))) END AS TIME_TAKEN_FOR_ACKNOWLEDGEMENT
FROM DAT_KEBS_MS_COMPLAINT a
JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID;

create OR REPLACE view MS_REPORT_SUBMITTED_CP_VIEW as
SELECT a.REFERENCE_NUMBER,a.COMPLAINT_TITLE,a.TARGETED_PRODUCTS,a.TRANSACTION_DATE,a.APPROVED_DATE,a.REJECTED_DATE,a.ASSIGNED_IO,
       CASE
           WHEN a.APPROVED = 1 THEN 'APPROVED'
           WHEN a.REJECTED = 1 THEN 'REJECTED'
           WHEN a.MANDATE_FOR_OGA = 1 THEN 'OGA MANDATE'
           WHEN a.AMENDMENT_STATUS = 1 THEN 'REJECTED FOR AMENDMENT'
           ELSE 'PENDING ACKNOWLEDGEMENT' END AS ACKNOWLEDGEMENT_TYPE,
       b.REGION, b.COUNTY,b.TOWN,a.COMPLAINT_DEPARTMENT,a.DIVISION,
       CASE
           WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)))
           WHEN a.REJECTED = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.MANDATE_FOR_OGA = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.AMENDMENT_STATUS = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE))) END AS TIME_TAKEN_FOR_ACKNOWLEDGEMENT,
        CASE
               WHEN c.DATA_REPORT_STATUS = 1 THEN 'YES'
               ELSE 'NO' END AS REPORT_SUBMITED,
       CASE
           WHEN c.DATA_REPORT_STATUS = 1 THEN
               TO_CHAR(ROUND((CAST((SELECT dp.CREATED_ON FROM DAT_KEBS_MS_DATA_REPORT dp WHERE dp.MS_WORKPLAN_GENERATED_ID = c.ID) AS DATE)-(a.TRANSACTION_DATE))))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE)))
           END AS TIME_TAKEN_FOR_REPORT_SUBMISSION
FROM DAT_KEBS_MS_COMPLAINT a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
        JOIN DAT_KEBS_MS_WORKPLAN_GENARATED c ON c.COMPLAINT_ID = a.ID;

create OR REPLACE view MS_SAMPLE_SUBMITTED_CP_VIEW as
SELECT a.REFERENCE_NUMBER,a.COMPLAINT_TITLE,a.TARGETED_PRODUCTS,a.TRANSACTION_DATE,a.APPROVED_DATE,a.REJECTED_DATE,a.ASSIGNED_IO,
       CASE
           WHEN a.APPROVED = 1 THEN 'APPROVED'
           WHEN a.REJECTED = 1 THEN 'REJECTED'
           WHEN a.MANDATE_FOR_OGA = 1 THEN 'OGA MANDATE'
           WHEN a.AMENDMENT_STATUS = 1 THEN 'REJECTED FOR AMENDMENT'
           ELSE 'PENDING ACKNOWLEDGEMENT' END AS ACKNOWLEDGEMENT_TYPE,
       b.REGION, b.COUNTY,b.TOWN,a.COMPLAINT_DEPARTMENT,a.DIVISION,
       CASE
           WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)))
           WHEN a.REJECTED = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.MANDATE_FOR_OGA = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.AMENDMENT_STATUS = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE))) END AS TIME_TAKEN_FOR_ACKNOWLEDGEMENT,
       CASE
           WHEN c.BS_NUMBER_STATUS = 1 THEN 'YES'
           ELSE 'NO' END AS SAMPLE_SUBMITTED_WITH_BS_NUMBER,
       CASE
           WHEN c.BS_NUMBER_STATUS = 1 THEN TO_CHAR(ROUND(((c.BS_NUMBER_DATED_ADDED)-(a.TRANSACTION_DATE))))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE)))
           END AS TIME_TAKEN_FOR_SAMPLE_SUBMISSION
FROM DAT_KEBS_MS_COMPLAINT a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
         JOIN DAT_KEBS_MS_WORKPLAN_GENARATED c ON c.COMPLAINT_ID = a.ID;


-- create OR REPLACE view MS_COMPLAINT_FEEDBACK_VIEW as
SELECT a.REFERENCE_NUMBER,a.COMPLAINT_TITLE,a.TARGETED_PRODUCTS,a.TRANSACTION_DATE,a.MS_PROCESS_ENDED_ON,
       a.APPROVED_DATE,a.REJECTED_DATE,a.ASSIGNED_IO,a.MS_PROCESS_ID,
       CASE
           WHEN a.APPROVED = 1 THEN 'APPROVED'
           WHEN a.REJECTED = 1 THEN 'REJECTED'
           WHEN a.MANDATE_FOR_OGA = 1 THEN 'OGA MANDATE'
           WHEN a.AMENDMENT_STATUS = 1 THEN 'REJECTED FOR AMENDMENT'
           ELSE 'PENDING ACKNOWLEDGEMENT' END AS ACKNOWLEDGEMENT_TYPE,
       b.REGION, b.COUNTY,b.TOWN,a.COMPLAINT_DEPARTMENT,a.DIVISION,
       CASE
           WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)))
           WHEN a.REJECTED = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.MANDATE_FOR_OGA = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.AMENDMENT_STATUS = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE))) END AS TIME_TAKEN_FOR_ACKNOWLEDGEMENT,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 THEN 'YES'
           ELSE 'NO' END AS FEEDBACK_SENT,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1
               THEN TO_CHAR(ROUND((a.MS_PROCESS_ENDED_ON - a.TRANSACTION_DATE)))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE)))
           END AS TIME_TAKEN_FOR_FEEDBACK_SENT
FROM DAT_KEBS_MS_COMPLAINT a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
         JOIN DAT_KEBS_MS_WORKPLAN_GENARATED c ON c.COMPLAINT_ID = a.ID;


-- create OR REPLACE view MS_REPORT_SUBMITTED_CP_VIEW as
SELECT a.REFERENCE_NUMBER
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
         JOIN DAT_KEBS_MS_WORKPLAN_GENARATED c ON c.COMPLAINT_ID = a.ID;

-- create OR REPLACE view MS_ALLOCATED_TASKS_CP_VIEW as
SELECT a.REFERENCE_NUMBER,a.TRANSACTION_DATE,a.TIMELINE_START_DATE,a.TIMELINE_END_DATE,a.MS_PROCESS_ID,
       a.ASSIGNED_IO,a.HOD_ASSIGNED,a.HOF_ASSIGNED,a.USER_TASK_ID,a.MS_COMPLAINT_ENDED_STATUS,
       CASE
           WHEN CURRENT_DATE > a.TIMELINE_END_DATE THEN 'YES'
           ELSE 'NO' END AS TASK_OVER_DUE
FROM DAT_KEBS_MS_COMPLAINT a;

create OR REPLACE view MS_ALLOCATED_TASKS_WP_VIEW as
SELECT a.REFERENCE_NUMBER,b.REFERENCE_NUMBER AS BATCH_REF_NUMBER ,a.CREATED_ON,a.TIMELINE_START_DATE,a.TIMELINE_END_DATE,a.MS_PROCESS_ID,
       a.OFFICER_ID,a.HOD_RM_ASSIGNED,a.HOF_ASSIGNED,a.USER_TASK_ID,a.COMPLAINT_ID,a.MS_PROCESS_ENDED_STATUS,a.BUDGET,a.TIME_ACTIVITY_DATE,a.NAME_ACTIVITY,
       CASE
           WHEN CURRENT_DATE > a.TIMELINE_END_DATE THEN 'YES'
           ELSE 'NO' END AS TASK_OVER_DUE
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
        JOIN DAT_WORKPLAN_CREATED b on a.WORKPLAN_YEAR_ID = b.ID;

-- create OR REPLACE view MS_TASKS_PENDING_ALLOCATION_CP_VIEW as
SELECT a.REFERENCE_NUMBER,a.TRANSACTION_DATE,a.TIMELINE_START_DATE,a.TIMELINE_END_DATE,a.MS_PROCESS_ID,
       a.ASSIGNED_IO,a.HOD_ASSIGNED,a.HOF_ASSIGNED,a.USER_TASK_ID,a.MS_COMPLAINT_ENDED_STATUS,
       CASE
           WHEN CURRENT_DATE > a.TIMELINE_END_DATE THEN 'YES'
           ELSE 'NO' END AS TASK_OVER_DUE
FROM DAT_KEBS_MS_COMPLAINT a;

-- create OR REPLACE view MS_TASKS_PENDING_ALLOCATION_WP_VIEW as
SELECT a.REFERENCE_NUMBER,b.REFERENCE_NUMBER AS BATCH_REF_NUMBER ,a.CREATED_ON,a.TIMELINE_START_DATE,a.TIMELINE_END_DATE,a.OFFICER_ID,a.HOD_RM_ASSIGNED,a.HOF_ASSIGNED,a.USER_TASK_ID,a.COMPLAINT_ID,a.REPORT_PENDING_REVIEW,
       a.FINAL_REPORT_GENERATED,a.INVEST_INSPECT_REPORT_STATUS,a.MS_PRELIMINARY_REPORT_STATUS,a.DATA_REPORT_STATUS,a.FIELD_REPORT_STATUS,a.MS_PROCESS_ID,a.MS_PROCESS_ENDED_STATUS,a.BUDGET,a.TIME_ACTIVITY_DATE,a.NAME_ACTIVITY,
       CASE
           WHEN CURRENT_DATE > a.TIMELINE_END_DATE THEN 'YES'
           ELSE 'NO' END AS TASK_OVER_DUE
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN DAT_WORKPLAN_CREATED b on a.WORKPLAN_YEAR_ID = b.ID;
    --JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
--          JOIN DAT_KEBS_MS_WORKPLAN_GENARATED c ON c.COMPLAINT_ID = a.ID;

-- DROP VIEW MS_ALLOCATED_TASKS_VIEW;

-- create OR REPLACE view MS_COMPLAINTS_INVESTIGATIONS_VIEW as
SELECT a.REFERENCE_NUMBER,a.COMPLAINT_TITLE,a.TARGETED_PRODUCTS,a.TRANSACTION_DATE,a.APPROVED_DATE,a.REJECTED_DATE,a.ASSIGNED_IO,
       CASE
           WHEN a.APPROVED = 1 THEN 'APPROVED'
           WHEN a.REJECTED = 1 THEN 'REJECTED'
           WHEN a.MANDATE_FOR_OGA = 1 THEN 'OGA MANDATE'
           WHEN a.AMENDMENT_STATUS = 1 THEN 'REJECTED FOR AMENDMENT'
           ELSE 'PENDING ACKNOWLEDGEMENT' END AS STATUS,
       b.REGION, b.COUNTY,b.TOWN,a.COMPLAINT_DEPARTMENT,a.DIVISION,
       CASE
           WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)))
           WHEN a.REJECTED = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.MANDATE_FOR_OGA = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           WHEN a.AMENDMENT_STATUS = 1 THEN TO_CHAR(ROUND((a.REJECTED_DATE - a.TRANSACTION_DATE)))
           ELSE TO_CHAR(ROUND((CURRENT_DATE - a.TRANSACTION_DATE))) END AS TIME_TAKEN_FOR_ACKNOWLEDGEMENT
FROM DAT_KEBS_MS_COMPLAINT a
   JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID;

-- create OR REPLACE view MS_PERFORMANCE_OF_SELECTED_PRODUCT_VIEW as
SELECT a.REFERENCE_NUMBER,a.DIVISION_ID,a.COMPLAINT_DEPARTMENT,a.REGION,a.COUNTY,a.TOWN_MARKET_CENTER,
       b.NAME_PRODUCT,b.BS_NUMBER,c.RESULTS_ANALYSIS,c.ANALYSIS_DONE,c.COMPLIANCE_REMARKS,
       CASE
           WHEN c.RESULTS_ANALYSIS = 1 THEN 'PASSED'
           WHEN c.RESULTS_ANALYSIS = 0 THEN 'FAILED'
           ELSE 'NOT YET ANALYSED' END AS STATUS
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
    JOIN  DAT_KEBS_MS_SAMPLE_SUBMISSION b ON a.id= b.MS_WORKPLAN_GENERATED_ID
    JOIN  DAT_KEBS_QA_SAMPLE_SUBMISSION c ON a.id= c.WORKPLAN_GENERATED_ID AND c.BS_NUMBER = b.BS_NUMBER
WHERE c.RESULTS_ANALYSIS IS NOT NULL ;

create OR REPLACE view MS_SEIZED_GOODS_VIEW as
SELECT a.REFERENCE_NUMBER,a.DIVISION_ID,a.COMPLAINT_DEPARTMENT,a.REGION,a.COUNTY,a.TOWN_MARKET_CENTER,
       b.DESCRIPTION_PRODUCTS_SEIZED,b.QUANTITY,b.ESTIMATED_COST,b.CURRENT_LOCATION
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN  DAT_MS_SEIZURE b ON a.id= b.WORKPLAN_GENERATED_ID;

-- create OR REPLACE view MS_COUNT_DONE_VIEW as
SELECT a.REFERENCE_NUMBER,a.DIVISION_ID,a.COMPLAINT_DEPARTMENT,a.REGION,a.COUNTY,a.TOWN_MARKET_CENTER,
       a.OFFICER_ID,a.HOD_RM_ASSIGNED,a.HOF_ASSIGNED,
       (SELECT COUNT(*) FROM DAT_KEBS_MS_SAMPLE_SUBMISSION b WHERE a.id= b.MS_WORKPLAN_GENERATED_ID) AS SAMPLE_SUBMITTED,
       (SELECT COUNT(*) FROM DAT_MS_SEIZURE b WHERE a.id= b.WORKPLAN_GENERATED_ID) AS GOODS_SEIZED
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a;

SELECT * FROM DAT_MS_SEIZURE;
