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
    add DIRECTOR_RECOMMENDATION_REMARKS_STATUS NUMBER(2)
/