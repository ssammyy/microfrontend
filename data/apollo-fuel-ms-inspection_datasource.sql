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

alter table DAT_KEBS_MS_FUEL_INSPECTION add BATCH_ID NUMBER REFERENCES STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION(ID) /
alter table DAT_KEBS_QA_SAMPLE_SUBMISSION add FUEL_INSPECTION_ID NUMBER REFERENCES DAT_KEBS_MS_FUEL_INSPECTION(ID) /
alter table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION add YEAR_NAME_ID NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES(ID) /
alter table DAT_KEBS_MS_FUEL_INSPECTION add USER_TASK_ID NUMBER /
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add LAB_RESULTS_STATUS NUMBER /
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION ALTER SENDERS_DATE DATE /

alter table STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION add YEAR_NAME_ID NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES(ID);
alter table DAT_KEBS_MS_ONSITE_UPLOADS add MS_FUEL_INSPECTION_ID NUMBER REFERENCES STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION(ID);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_BATCH_NUMBER_ID NUMBER REFERENCES DAT_KEBS_INVOICE_BATCH_DETAILS(ID);
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add PAYMENT_STATUS NUMBER;
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_DATE DATE /
alter table DAT_KEBS_MS_SAMPLE_SUBMISSION add SAMPLE_BS_NUMBER_REMARKS VARCHAR2(100 CHAR) /
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add INVOICE_NUMBER VARCHAR2(100 CHAR) /
alter table DAT_KEBS_MS_FUEL_REMEDY_INVOICES add RECEIPT_NO VARCHAR2(100 CHAR) /
alter table DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS add SSF_FILE_STATUS VARCHAR2(100 CHAR) /
alter table DAT_KEBS_MS_ONSITE_UPLOADS add ORDINARY_STATUS NUMBER(2,0) /
alter table DAT_KEBS_MS_ONSITE_UPLOADS add SSF_UPLOADS NUMBER(2,0) /
alter table DAT_KEBS_MS_FUEL_INSPECTION add INSPECTION_COMPLETE_STATUS NUMBER(2,0) /
alter table DAT_KEBS_MS_ONSITE_UPLOADS add VERSION_NUMBER NUMBER /

diofficerincharge@gmail.com
password
