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
    add PRODUCT_BRAND VARCHAR2(255)
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

create OR REPLACE view MS_SAMPLE_SUBMISSION as
SELECT nvl(TO_CHAR(samp.id),'N/A') AS id,
       nvl(samp.name_product,'N/A') AS name_product,
       nvl(samp.packaging,'N/A') AS packaging,
       nvl(samp.labelling_identification,'N/A') AS labelling_identification,
       nvl(samp.file_ref_number,'N/A') AS file_ref_number,
       nvl(samp.references_standards,'N/A') AS references_standards,
       nvl(samp.size_test_sample,'N/A') AS size_test_sample,
       nvl(samp.size_ref_sample,'N/A') AS size_ref_sample,
       nvl(samp.condition,'N/A') AS condition,
       nvl(samp.sample_references,'N/A') AS sample_references,
       nvl(samp.senders_name,'N/A') AS senders_name,
       nvl(samp.designation,'N/A') AS designation,
       nvl(samp.address,'N/A') AS address,
       nvl(TO_CHAR(TRUNC(samp.senders_date),'DD/MM/YYYY'),'N/A') AS senders_date,
       nvl(samp.receivers_name,'N/A') AS receivers_name,
       nvl(TO_CHAR(samp.test_charges_ksh),'N/A') AS test_charges_ksh,
       nvl(samp.receipt_lpo_number,'N/A') AS receipt_lpo_number,
       nvl(samp.invoice_number,'N/A') AS invoice_number,
       nvl(samp.disposal,'N/A') AS disposal,
       nvl(samp.remarks,'N/A') AS remarks,
       nvl(samp.country_of_origin,'N/A') AS country_of_origin,
       nvl(TO_CHAR(samp.sample_collection_number),'N/A') AS sample_collection_number,
       nvl(samp.bs_number,'N/A') AS bs_number,
       nvl(samp.COC_NUMBER,'N/A') AS COC_NUMBER,
       nvl(samp.LB_ID_ANY_AOMARKING,'N/A') AS LB_ID_ANY_AOMARKING,
       nvl(samp.LB_ID_BATCH_NO,'N/A') AS LB_ID_BATCH_NO,
       nvl(samp.LB_ID_CONT_DECL,'N/A') AS LB_ID_CONT_DECL,
       nvl(TO_CHAR(TRUNC(samp.LB_ID_DATE_OF_MANF),'DD/MM/YYYY'),'N/A') AS LB_ID_DATE_OF_MANF,
       nvl(TO_CHAR(TRUNC(samp.LB_ID_EXPIRY_DATE),'DD/MM/YYYY'),'N/A') AS LB_ID_EXPIRY_DATE,
       nvl(TO_CHAR(TRUNC(samp.RECEIVERS_DATE),'DD/MM/YYYY'),'N/A') AS RECEIVERS_DATE,
       nvl(samp.LB_ID_TRADE_MARK,'N/A') AS LB_ID_TRADE_MARK,
       nvl(samp.NOTE_TRANS_RESULTS,'N/A') AS NOTE_TRANS_RESULTS,
       nvl(samp.SCF_NO,'N/A') AS SCF_NO,
       TO_CHAR(samp.CREATED_USER_ID) AS CREATED_USER_ID,
       param.parameters,
       param.laboratory_name
FROM DAT_KEBS_MS_SAMPLE_SUBMISSION samp
         INNER JOIN DAT_KEBS_MS_LABORATORY_PARAMETERS param ON samp.ID = param.SAMPLE_SUBMISSION_ID;
/

create OR REPLACE view MS_FIELD_REPORT_VIEW as
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
        nvl(TO_CHAR(TRUNC(rep.START_DATE_INVESTIGATION_INSPECTION),'DD/MM/YYYY'),'N/A') AS START_DATE_INVESTIGATION_INSPECTION,
        nvl(TO_CHAR(TRUNC(rep.END_DATE_INVESTIGATION_INSPECTION),'DD/MM/YYYY'),'N/A') AS END_DATE_INVESTIGATION_INSPECTION,
        nvl(rep.METHODOLOGY_EMPLOYED,'N/A') AS METHODOLOGY_EMPLOYED,
        nvl(rep.SUMMARY_OF_FINDINGS,'N/A') AS SUMMARY_OF_FINDINGS,
        nvl(rep.ADDITIONAL_INFORMATION,'N/A') AS ADDITIONAL_INFORMATION,
        nvl(rep.CONCLUSION,'N/A') AS CONCLUSION,
        nvl(rep.RECOMMENDATIONS,'N/A') AS RECOMMENDATIONS,
        nvl(rep.STATUS_ACTIVITY,'N/A') AS STATUS_ACTIVITY,
        nvl(rep.FINAL_REMARK_HOD,'N/A') AS FINAL_REMARK_HOD,
        nvl(rep.KEBS_INSPECTORS,'N/A') AS KEBS_INSPECTORS,
        nvl(rep.VAR_FIELD_1,'N/A') AS VAR_FIELD_1,
        nvl(TO_CHAR(TRUNC(rep.CREATED_ON),'DD/MM/YYYY'),'N/A') AS CREATED_ON,
        nvl(rep.CREATED_BY,'N/A') AS CREATED_BY,
        nvl(rep.MODIFIED_BY,'N/A') AS MODIFIED_BY,
        nvl(TO_CHAR(TRUNC(rep.MODIFIED_ON),'DD/MM/YYYY'),'N/A') AS MODIFIED_ON,
        nvl(rep.REPORT_CLASSIFICATION,'N/A') AS REPORT_CLASSIFICATION
--         nvl(rep.CHANGES_MADE,'N/A') AS CHANGES_MADE
FROM DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT rep
/

create or REPLACE view OUTLET_VISITED_AND_SUMMARY_OF_FINDINGS_VIEW as
SELECT nvl(TO_CHAR(dat.INSPECTION_DATE),'N/A') AS INSPECTION_DATE,
       nvl(TO_CHAR(dat.OUTLET_NAME),'N/A') AS OUTLET_NAME,
       nvl(TO_CHAR(dat.OUTLET_DETAILS), 'N/A') AS OUTLET_DETAILS,
       nvl(TO_CHAR(dat.PHONE_NUMBER), 'N/A') AS PHONE_NUMBER,
       nvl(TO_CHAR(dat.EMAIL_ADDRESS), 'N/A') AS EMAIL_ADDRESS,
       nvl(TO_CHAR(dat.PERSON_MET), 'N/A') AS PERSON_MET,
       nvl(TO_CHAR(dat.SUMMARY_FINDINGS_ACTIONS_TAKEN), 'N/A') AS SUMMARY_FINDINGS_ACTIONS_TAKEN,
       nvl(TO_CHAR(dat.REMARKS), 'N/A') AS REMARKS,
       nvl(TO_CHAR(dat.MS_WORKPLAN_GENERATED_ID), 'N/A') AS MS_WORKPLAN_GENERATED_ID,
       nvl(TO_CHAR(dat.ID), 'N/A') AS ID
FROM APOLLO.DAT_KEBS_MS_DATA_REPORT dat;

create or REPLACE view OUTLET_VISITED_AND_SUMMARY_OF_FINDINGS_VIEW as
SELECT nvl(TO_CHAR(dat.INSPECTION_DATE),'N/A') AS INSPECTION_DATE,
       nvl(TO_CHAR(dat.OUTLET_NAME),'N/A') AS OUTLET_NAME,
       nvl(TO_CHAR(dat.OUTLET_DETAILS), 'N/A') AS OUTLET_DETAILS,
       nvl(TO_CHAR(dat.PHONE_NUMBER), 'N/A') AS PHONE_NUMBER,
       nvl(TO_CHAR(dat.EMAIL_ADDRESS), 'N/A') AS EMAIL_ADDRESS,
       nvl(TO_CHAR(dat.PERSON_MET), 'N/A') AS PERSON_MET,
       nvl(TO_CHAR(dat.SUMMARY_FINDINGS_ACTIONS_TAKEN), 'N/A') AS SUMMARY_FINDINGS_ACTIONS_TAKEN,
       nvl(TO_CHAR(dat.REMARKS), 'N/A') AS REMARKS,
       nvl(TO_CHAR(dat.MS_WORKPLAN_GENERATED_ID), 'N/A') AS MS_WORKPLAN_GENERATED_ID,
       nvl(TO_CHAR(dat.ID), 'N/A') AS ID
FROM APOLLO.DAT_KEBS_MS_DATA_REPORT dat;

create or REPLACE view SUMMARY_OF_SAMPLES_DRAWN_VIEW as
SELECT nvl(TO_CHAR(szd.FILE_REF_NUMBER),'N/A') AS FILE_REF_NUMBER,
       nvl(TO_CHAR(szd.DATA_REPORT_ID),'N/A') AS DATA_REPORT_ID,
       nvl(TO_CHAR(szd.NAME_PRODUCT), 'N/A') AS PRODUCT_NAME,
       nvl(TO_CHAR(szd.LB_ID_TRADE_MARK), 'N/A') AS PRODUCT_BRAND,
       nvl(TO_CHAR(szd.ADDRESS), 'N/A') AS ADDRESS,
       nvl(TO_CHAR(szd.COUNTRY_OF_ORIGIN), 'N/A') AS COUNTRY_OF_ORIGIN,
       nvl(TO_CHAR(szd.LB_ID_EXPIRY_DATE), 'N/A') AS EXPIRY_DATE,
       nvl(TO_CHAR(szd.LB_ID_BATCH_NO), 'N/A') AS BATCH_NUMBER,
       nvl(TO_CHAR(szd.MS_WORKPLAN_GENERATED_ID), 'N/A') AS MS_WORKPLAN_GENERATED_ID,
       nvl(TO_CHAR(szd.SENDERS_NAME), 'N/A') AS SENDERS_NAME,
       nvl(TO_CHAR(szd.ID), 'N/A') AS ID,
       nvl(TO_CHAR(szd.SAMPLE_COLLECTION_DATE), 'N/A') AS SAMPLE_COLLECTION_DATE,
       nvl(TO_CHAR(szd.SENDERS_DATE), 'N/A') AS DATE_SUBMITTED,
FROM APOLLO.DAT_KEBS_MS_SAMPLE_SUBMISSION szd;



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
       b.NAME_PRODUCT,b.BS_NUMBER,c.FAILED_PARAMETERS,c.RESULTS_ANALYSIS,c.ANALYSIS_DONE,c.COMPLIANCE_REMARKS,
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


create OR REPLACE view MS_COMPLAINT_PDF_GENERATION as
SELECT a.ID,a.REFERENCE_NUMBER,FIRST_NAME,LAST_NAME,MOBILE_PHONE_NUMBER,ID_NUMBER,c.POSTAL_ADDRESS,c.PHYSICAL_ADDRESS,c.EMAIL_ADDRESS,
       b.BUILDING,b.BUSINESS_ADDRESS,b.TELEPHONE_NUMBER,b.PHONE_NUMBER,b.EMAIL,b.NAME_CONTACT_PERSON,nvl(c.PHYSICAL_ADDRESS,'N/A') AS PHYSICAL_ADDRESS_CUSTOMER,
       a.COMPLAINT_DETAILS,a.COMPLAINT_SAMPLE_DETAILS,a.REMEDY_SOUGHT,nvl(TO_CHAR(TRUNC(a.CREATED_ON),'DD/MM/YYYY'),'N/A') AS CREATED_ON, a.CREATED_BY
FROM DAT_KEBS_MS_COMPLAINT a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
         JOIN  DAT_KEBS_MS_COMPLAINT_CUSTOMERS c ON a.id= c.COMPLAINT_ID;


create table DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS
(
    ID                       NUMBER PRIMARY KEY,
    COUNTY_ID              NUMBER REFERENCES CFG_KEBS_COUNTIES(ID),
    TOWNS_ID              NUMBER REFERENCES CFG_KEBS_TOWNS(ID),
    WORK_PLAN_ID              NUMBER REFERENCES DAT_KEBS_MS_WORKPLAN_GENARATED(ID),
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

create sequence DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS_SEQ_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS_seq_idx on DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS (STATUS,COUNTY_ID,TOWNS_ID,WORK_PLAN_ID) TABLESPACE qaimssdb_idx;
/

create table CFG_KEBS_MS_OGA
(
    ID                       NUMBER PRIMARY KEY,
    OGA_NAME                VARCHAR2(350 char),
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

create sequence CFG_KEBS_MS_OGA_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_MS_OGA_SEQ_trg
    before
        insert
    on CFG_KEBS_MS_OGA
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_MS_OGA_SEQ.nextval
            into :new.id
            from dual;

        end if;
    end if;
end;


create index CFG_KEBS_MS_OGA_seq_idx on CFG_KEBS_MS_OGA (STATUS) TABLESPACE qaimssdb_idx;
/
-- CONCAT
--create OR REPLACE view CONSUMER_COMPLAINTS_REPORT_VIEW as
SELECT a.ID,a.REFERENCE_NUMBER,nvl(c.FIRST_NAME||' '||c.LAST_NAME,'N/A') AS COMPLAINANT,nvl(a.COMPLAINT_SAMPLE_DETAILS,'N/A') AS NATURE_COMPLAINT,
       (SELECT dv.DEPARTMENT FROM CFG_KEBS_DEPARTMENTS dv WHERE dv.ID = a.COMPLAINT_DEPARTMENT) AS SECTOR,
       (SELECT d.DIVISION FROM CFG_KEBS_DIVISIONS d WHERE d.ID = a.DIVISION) AS DIVISION,
       (SELECT r.REGION FROM CFG_KEBS_REGIONS r WHERE r.ID = b.REGION) AS REGION,
       nvl(TO_CHAR(TRUNC(a.TRANSACTION_DATE),'DD/MM/YYYY'),'N/A') AS DATE_RECEIVED,
       CASE
           WHEN a.APPROVED = 1 THEN nvl(TO_CHAR(TRUNC(a.APPROVED_DATE),'DD/MM/YYYY'),'N/A')
           ELSE 'NOT YET ACKNOWLEDGED/DECLINED' END AS DATE_ACKNOWLEDGED,
       (SELECT nvl(u.FIRST_NAME||' '||u.LAST_NAME,'N/A') AS OFFICER_NAME FROM DAT_KEBS_USERS u WHERE u.ID = a.ASSIGNED_IO) AS INVESTIGATING_OFFICER,
       nvl(TO_CHAR(TRUNC(a.MS_PROCESS_ENDED_ON),'DD/MM/YYYY'),'N/A') AS DATE_COMPLETION_INVESTIGATION,
       nvl(TO_CHAR(TRUNC(a.MS_PROCESS_ENDED_ON),'DD/MM/YYYY'),'N/A') AS DATE_FEEDBACK_COMPLAINANT,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 THEN '1'
           ELSE '0' END AS RESOLUTION,
       CASE
           WHEN a.APPROVED = 1 THEN TO_CHAR(ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE)+1))
           ELSE 'NOT YET ACKNOWLEDGED/DECLINED' END AS TIME_TAKEN_ACKNOWLEDGE,
       CASE
           WHEN a.APPROVED = 1 AND ((ROUND((a.APPROVED_DATE - a.TRANSACTION_DATE))) <= 2) THEN '1'
           ELSE '0' END AS ACKNOWLEDGED_WITHIN2_DAYS_RECEIPT,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 THEN TO_CHAR(ROUND((a.MS_PROCESS_ENDED_ON - a.MS_PROCESS_ENDED_ON)+1))
           ELSE 'NOT YET ENDED' END AS TIME_TAKEN_PROVIDE_FEEDBACK,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 AND ((ROUND((a.MS_PROCESS_ENDED_ON - a.MS_PROCESS_ENDED_ON))) <= 5) THEN '1'
           ELSE '0' END AS FEEDBACK_WITHIN5DAYS_COMP_INVESTIGATION,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 THEN TO_CHAR(ROUND((a.MS_PROCESS_ENDED_ON - a.TRANSACTION_DATE)+1))
           ELSE 'NOT YET ADDRESSED' END AS TIME_TAKEN_ADDRESS_COMPLAINT,
       CASE
           WHEN a.MS_COMPLAINT_ENDED_STATUS = 1 AND ((ROUND((a.MS_PROCESS_ENDED_ON - a.TRANSACTION_DATE))) <= 28) THEN '1'
           ELSE '0' END AS ADDRESSED_WITHIN28_DAYS_RECEIPT,
       a.ASSIGNED_IO, a.TRANSACTION_DATE, a.COMPLAINT_DEPARTMENT
FROM DAT_KEBS_MS_COMPLAINT a
         JOIN  DAT_KEBS_MS_COMPLAINT_LOCATION b ON a.id= b.COMPLAINT_ID
         JOIN  DAT_KEBS_MS_COMPLAINT_CUSTOMERS c ON a.id= c.COMPLAINT_ID;


SELECT a.* from APOLLO.CONSUMER_COMPLAINTS_REPORT_VIEW a where
    (:startDate is null or a.TRANSACTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TRANSACTION_DATE <=TO_DATE(:endDate))
    and (:assignIO is null or a.ASSIGNED_IO =TO_NUMBER(:assignIO)) and
   (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID));


create OR REPLACE view SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW as
SELECT b.ID,b.SENDERS_DATE,b.DATE_VISIT,b.SAMPLE_REFERENCES,c.RESULTS_DATE,c.RESULT_SENT_DATE,a.OFFICER_ID,a.COMPLAINT_DEPARTMENT,
       nvl(TO_CHAR(TRUNC(b.DATE_VISIT),'DD/MM/YYYY'),'N/A') AS DATEOF_VISIT,
       nvl(TO_CHAR(TRUNC(b.SENDERS_DATE),'DD/MM/YYYY'),'N/A') AS SAMPLE_SUBMISSION_DATE,
       nvl(TO_CHAR(TRUNC(b.SAMPLE_COLLECTION_DATE),'DD/MM/YYYY'),'N/A') AS SAMPLE_COLLECTION_DATE,
       b.MARKET_CENTRE,b.NAME_ADDRESS_OUTLET,b.PRODUCT_DESCRIPTION,b.SECTOR,b.UCR_PERMIT_NO,b.SOURCE_PRODUCT_EVIDENCE,
        nvl(b.LB_ID_TRADE_MARK||' / '||b.MANUFACTURE_NAME,'N/A') AS BRAND_AND_MANUFACTURER,
        nvl(b.VAR_FIELD_3,'N/A') AS NO_SAMPLES_TESTED,
        c.FAILED_PARAMETERS,
        nvl(d.VAR_FIELD_1,'N/A') AS ACTIONS_TAKEN,
       nvl(TO_CHAR(TRUNC(c.RESULTS_DATE),'DD/MM/YYYY'),'N/A') AS DATEOF_TEST_REPORT	,
       nvl(TO_CHAR(TRUNC(c.RESULT_SENT_DATE),'DD/MM/YYYY'),'N/A') AS DATEOF_FORWARDING_TEST_RESULTS	,
       CASE
           WHEN c.RESULTS_ANALYSIS = 1 THEN '100'
           WHEN c.RESULTS_ANALYSIS = 0 THEN '0'
           ELSE 'NOT YET ANALYSED' END AS COMPLIANCE_TESTING,
       CASE
           WHEN c.RESULTS_ANALYSIS = 1 THEN TO_CHAR(ROUND((b.VAR_FIELD_3 * 100)))
           WHEN c.RESULTS_ANALYSIS = 0 THEN TO_CHAR(ROUND((b.VAR_FIELD_3 * 0)))
           ELSE 'NOT YET ANALYSED' END AS TCXB,
       CASE
           WHEN a.BS_NUMBER_STATUS = 1 THEN TO_CHAR(ROUND(((b.SENDERS_DATE)-(b.DATE_VISIT))))
           ELSE 'NOT YET SUBMITTED'  END AS TIME_TAKEN_SUBMIT_SAMPLE,
       CASE
           WHEN a.BS_NUMBER_STATUS = 1 AND ((ROUND((b.SENDERS_DATE-b.DATE_VISIT))) <= 2) THEN '1'
           ELSE '0'  END AS SUBMISSION_WITHIN2_DAYS,
       CASE
           WHEN c.RESULTS_SENT = 1 THEN TO_CHAR(ROUND((c.RESULT_SENT_DATE-c.RESULTS_DATE)))
           ELSE 'NOT YET SENT'  END AS TIME_TAKEN_FORWARD_LETTERS,
       CASE
           WHEN c.RESULTS_SENT = 1 AND ((ROUND((c.RESULT_SENT_DATE-c.RESULTS_DATE))) <= 14) THEN '1'
           ELSE '0'  END AS FORWARDING_WITHIN14_DAYS_TESTING,
       nvl(b.LB_ID_BATCH_NO||' / '||TO_CHAR(TRUNC(b.LB_ID_DATE_OF_MANF),'DD/MM/YYYY'),'N/A') AS BATCH_NO_DATE_MANUFACTURE
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
     JOIN  DAT_KEBS_MS_SAMPLE_SUBMISSION b ON a.id= b.MS_WORKPLAN_GENERATED_ID
     JOIN  DAT_KEBS_QA_SAMPLE_SUBMISSION c ON a.id= c.WORKPLAN_GENERATED_ID AND c.BS_NUMBER = b.BS_NUMBER
     JOIN  DAT_KEBS_MS_WORKPLAN_PRODUCTS d ON a.id= d.WORK_PLAN_ID AND b.id = d.SSF_ID
;

SELECT a.* from APOLLO.SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW a where
    (:startDate is null or a.DATE_VISIT >=TO_DATE(:startDate)) and (:endDate is null or a.DATE_VISIT <=TO_DATE(:endDate))
    AND (:sampleReferences is null or a.SAMPLE_REFERENCES =:sampleReferences) and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO))
    AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID));

alter table DAT_KEBS_QA_SAMPLE_SUBMISSION
    add RESULT_SENT_DATE    DATE
/

alter table DAT_KEBS_MS_SAMPLE_SUBMISSION
    add UCR_PERMIT_NO       VARCHAR2(1000)
/

create OR REPLACE view MS_SEIZED_GOODS_REPORT_VIEW as
SELECT c.ID,
       nvl(TO_CHAR(TRUNC(c.DATE_SEIZURE),'DD/MM/YYYY'),'N/A') AS DATEOF_SEIZURE,
       nvl(b.MARKET_TOWN_CENTER,'N/A') AS MARKET_CENTRE,
       nvl(b.PRODUCT_FIELD,'N/A') AS PRODUCT_FIELD,
       nvl(b.NAME_OF_OUTLET,'N/A') AS NAME_OUTLET,
       nvl(c.DESCRIPTION_PRODUCTS_SEIZED,'N/A') AS DESCRIPTION_PRODUCTS_SEIZED,
       nvl(c.BRAND,'N/A') AS BRAND,
       nvl(c.SECTOR,'N/A') AS SECTOR,
       nvl(c.QUANTITY,'N/A') AS QUANTITY,
       nvl(c.UNIT,'N/A') AS UNIT,
       nvl(c.ESTIMATED_COST,'N/A') AS ESTIMATED_COST ,
       nvl(c.CURRENT_LOCATION,'N/A') AS CURRENT_LOCATION_SEIZED_PRODUCTS ,
       nvl(c.PRODUCTS_DESTRUCTION,'N/A') AS PRODUCTS_DUE_FOR_DESTRUCTION ,
       nvl(c.PRODUCTS_RELEASE,'N/A') AS PRODUCTS_DUE_FOR_RELEASE,
       nvl(TO_CHAR(TRUNC(c.DATE_DESTRUCTED),'DD/MM/YYYY'),'N/A') AS DATEOF_DESTRUCTED,
       nvl(TO_CHAR(TRUNC(c.DATE_RELEASE),'DD/MM/YYYY'),'N/A') AS DATEOF_RELEASE,
       c.DATE_SEIZURE AS DATE_SEIZURE,
       c.DATE_DESTRUCTED AS DATE_DESTRUCTED,
       c.DATE_RELEASE AS DATE_RELEASE
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN  DAT_KEBS_MS_SEIZURE_DECLARATION b ON a.id= b.MS_WORKPLAN_GENERATED_ID
         JOIN  DAT_MS_SEIZURE c ON a.id= c.WORKPLAN_GENERATED_ID AND c.MAIN_SEIZURE_ID = b.ID;

SELECT a.* from APOLLO.MS_SEIZED_GOODS_REPORT_VIEW a where
    (:startDate is null or a.DATE_SEIZURE >=TO_DATE(:startDate)) and (:endDate is null or a.DATE_SEIZURE <=TO_DATE(:endDate))
    AND (:sector is null or a.SECTOR =:sector) AND (:brand is null or a.BRAND =:brand) AND (:marketCentre is null or a.MARKET_CENTRE =:marketCentre)
    AND (:nameOutlet is null or a.NAME_OUTLET =:nameOutlet) AND (:productsDueForDestruction is null or a.PRODUCTS_DUE_FOR_DESTRUCTION =:productsDueForDestruction)
    AND (:productsDueForRelease is null or a.PRODUCTS_DUE_FOR_RELEASE =:productsDueForRelease);


-- alter table DAT_MS_SEIZURE
--     add DATE_RELEASE    DATE
-- /
--
-- alter table DAT_MS_SEIZURE
--     add PRODUCTS_RELEASE        VARCHAR2(1000)
-- /

CREATE OR REPLACE view FIELD_INSPECTION_SUMMARY_REPORT_VIEW as
SELECT DISTINCT b.ID,b.INSPECTION_DATE,b.MARKET_CENTER,b.OUTLET_DETAILS,a.COMPLAINT_DEPARTMENT,a.OFFICER_ID,b.TOTAL_COMPLIANCE_SCORE,d.REPORT_DATE,
       nvl(TO_CHAR(TRUNC(b.INSPECTION_DATE),'DD/MM/YYYY'),'N/A') AS DATEOF_VISIT,
       nvl(TO_CHAR(TRUNC(d.REPORT_DATE),'DD/MM/YYYY'),'N/A') AS DATEOF_SURVEILLANCE_REPORT,
       nvl(b.MARKET_CENTER,'N/A') AS MARKET_CENTRE,
       nvl(b.OUTLET_DETAILS,'N/A') AS NAME_OUTLET,
        (SELECT d.DIVISION FROM CFG_KEBS_DIVISIONS d WHERE d.ID = a.DIVISION_ID) AS DIVISION,
       nvl(b.VAR_FIELD_10,'N/A') AS NO_SAMPLES_DRAWN_SUBMITTED,
       nvl(b.TOTAL_COMPLIANCE_SCORE,'N/A') AS COMPLIANCE_PHYSICAL_INSPECTION,
       nvl(b.MOST_RECURRING_NON_COMPLIANT,'N/A') AS MOST_RECURRING_NON_COMPLIANT,
       nvl(TO_CHAR(ROUND(((SELECT COUNT(*) FROM DAT_KEBS_MS_DATA_REPORT_PARAMETERS p WHERE p.DATA_REPORT_ID= b.ID) * b.TOTAL_COMPLIANCE_SCORE))),'N/A')  AS PCXA,
       (SELECT nvl(u.DEPARTMENT,'N/A') AS DIVISION_NAME FROM CFG_KEBS_DEPARTMENTS u WHERE u.ID = a.COMPLAINT_DEPARTMENT) AS SECTOR_NAME,
       (SELECT COUNT(*) FROM DAT_KEBS_MS_DATA_REPORT_PARAMETERS p WHERE p.DATA_REPORT_ID= b.ID) AS NO_OF_SAMPLES_PHYSICALLY_INSPECTED,
        CASE
            WHEN (b.INSPECTION_DATE BETWEEN a.TIME_ACTIVITY_DATE AND a.TIME_ACTIVITY_END_DATE) THEN '1'
            ELSE '0' END AS VISIT_ASPERMS_SCHEDULE,
        CASE
            WHEN a.INVEST_INSPECT_REPORT_STATUS = 1 THEN TO_CHAR(ROUND((d.REPORT_DATE-b.INSPECTION_DATE)))
            ELSE 'NOT YET FILLED'  END AS TIME_TAKEN_FILE_SURVEILLANCE_REPORT,
        CASE
            WHEN a.INVEST_INSPECT_REPORT_STATUS = 1  AND ((ROUND((d.REPORT_DATE-b.INSPECTION_DATE))) <= 1) THEN '1'
            ELSE '0'  END AS FILING_WITHIN1_DAYAFTER_VISIT
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN  DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT d ON a.id= d.MS_WORKPLAN_GENERATED_ID
         JOIN  DAT_KEBS_MS_DATA_REPORT b ON a.id= b.MS_WORKPLAN_GENERATED_ID
         JOIN  DAT_KEBS_MS_DATA_REPORT_PARAMETERS c ON  c.DATA_REPORT_ID = b.ID;

SELECT a.* from APOLLO.FIELD_INSPECTION_SUMMARY_REPORT_VIEW a where
    (:startDate is null or a.INSPECTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE <=TO_DATE(:endDate))
    and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID));

create OR REPLACE view WORK_PLAN_MONITORING_TOOL as
SELECT DISTINCT a.OFFICER_ID,b.REGION_ID,a.COMPLAINT_DEPARTMENT,a.ID,a.REFERENCE_NUMBER,a.TIME_ACTIVITY_DATE,a.TIME_ACTIVITY_END_DATE,
                nvl(TO_CHAR(TRUNC(a.TIME_ACTIVITY_DATE),'DD/MM/YYYY')||' - '||TO_CHAR(TRUNC(a.TIME_ACTIVITY_END_DATE),'DD/MM/YYYY'),'N/A') AS TARGETED_MONTH,
                a.PRODUCT_STRING,
                (SELECT nvl(u.FIRST_NAME||' '||u.LAST_NAME,'N/A') AS OFFICER_NAME FROM DAT_KEBS_USERS u WHERE u.ID = a.OFFICER_ID) AS OFFICERS,
                (SELECT nvl(u.REGION,'N/A') AS REGION_NAME FROM CFG_KEBS_REGIONS u WHERE u.ID = b.REGION_ID) AS REGION,
                (SELECT nvl(u.COUNTY,'N/A') AS COUNTY_NAME FROM CFG_KEBS_COUNTIES u WHERE u.ID = b.COUNTY_ID) AS COUNTY,
                (SELECT nvl(u.TOWN,'N/A') AS TOWN_NAME FROM CFG_KEBS_TOWNS u WHERE u.ID = b.TOWNS_ID) AS TOWN,
                CASE
                    WHEN 'July' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                    from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                    connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS JULY,
                CASE
                    WHEN 'August' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                      from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                      connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS AUGUST,
                CASE
                    WHEN 'September' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                         from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                         connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS SEPTEMBER,
                CASE
                    WHEN 'October' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                       from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                       connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS OCTOBER,
                CASE
                    WHEN 'November' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                        from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                        connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS NOVEMBER,
                CASE
                    WHEN 'December' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                        from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                        connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS DECEMBER,
                CASE
                    WHEN 'January' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                       from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                       connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS JANUARY,
                CASE
                    WHEN 'February' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                        from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                        connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS FEBRUARY,
                CASE
                    WHEN 'March' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                     from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                     connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS MARCH,
                CASE
                    WHEN 'April' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                     from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                     connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS APRIL,
                CASE
                    WHEN 'May' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                   from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                   connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS MAY,
                CASE
                    WHEN 'June' IN (select to_char( add_months( start_date, level-1 ), 'fmMonth' )
                                    from (select a.TIME_ACTIVITY_DATE start_date, a.MS_PROCESS_ENDED_ON end_date from dual)
                                    connect by level <= months_between(trunc(end_date,'MM'),trunc(start_date,'MM') )) THEN '1'
                    ELSE '0' END AS JUNE
FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
         JOIN  DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b ON a.id= b.WORK_PLAN_ID;
/

SELECT DISTINCT a.* from APOLLO.WORK_PLAN_MONITORING_TOOL a WHERE
    (:startDate is null or a.TIME_ACTIVITY_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TIME_ACTIVITY_END_DATE <=TO_DATE(:endDate))
and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))


create OR REPLACE view WORK_PLAN_MONITORING_SAME_TASK_DATE as
Select a.ID, a.WORKPLAN_YEAR_ID, a.TIME_ACTIVITY_END_DATE,b.REGION_ID, a.OFFICER_ID,a.OFFICER_NAME,a.TIME_ACTIVITY_DATE
    b.COUNTY_ID, b.TOWNS_ID FROM DAT_KEBS_MS_WORKPLAN_GENARATED a
                        JOIN   DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b on a.ID = b.WORK_PLAN_ID;


WHERE a.WORKPLAN_YEAR_ID= :workplanYearId AND  a.TIME_ACTIVITY_END_DATE = :timeActivityEndDate
  AND b.REGION_ID=:regionId AND b.COUNTY_ID=:countyId AND b.TOWNS_ID=:townsId;


--create OR REPLACE VIEW WORK_PLAN_VIEW_UCR_NUMBER_ITEMS as
SELECT a.ID as ITEM_ID,b.UCR_NUMBER, a.ITEM_DESCRIPTION,a.QUANTITY,a.PACKAGE_QUANTITY,
       a.ITEM_GROSS_WEIGHT,a.HS_DESCRIPTION,a.ITEM_HS_CODE, a.COUNTRY_OF_ORGIN, b.COC_NUMBER, b.CD_IMPORTER
FROM DAT_KEBS_CD_ITEM_DETAILS a
         JOIN DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS b on b.id = a.CD_DOC_ID
-- WHERE b.UCR_NUMBER = 'UCR2300024593'
ORDER BY a.ID DESC;


SELECT other_workplans.* from
 (
     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID
     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b
     WHERE a.id=b.work_plan_id
       --AND a.id<>
       AND a.WORKPLAN_YEAR_ID =:workPlanYearId
       AND a.OFFICER_ID <>:officerID
       AND a.APPROVED_STATUS=1
       AND a.onsite_end_status!=1
       AND a.COMPLAINT_ID IS NOT NULL
     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')
     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')
 )other_workplans,
 (
     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID
     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,
          DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b
     WHERE a.id=b.work_plan_id
       AND a.OFFICER_ID =:officerID
     --AND a.id=1347
     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')
     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')
 )lookup
WHERE lookup.county_id=other_workplans.county_id
  AND lookup.towns_id=other_workplans.towns_id
  AND trunc(lookup.time_activity_date)=trunc(other_workplans.time_activity_date)


















