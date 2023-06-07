***************************Table USED IN DI*****************************************
select *
from CFG_PAYMENT_METHODS
order by ID desc;


select *
from DAT_KEBS_CD_DEMAND_NOTE
-- where ID = 765
where DEMAND_NOTE_NUMBER = 'DN20210408658D1'
order by ID desc;

select *
from log_stg_payment_reconciliation
-- where ID = 15
-- where DEMAND_NOTE_NUMBER = 'DN#20210311C5A94'
order by id desc;

select *
from DAT_KEBS_QA_BATCH_INVOICE
-- where INVOICE_NUMBER = 'KIMSINVOICE#20210623F90B6'
order by id desc;

select *
from STG_PAYMENT_RECONCILIATION
where REFERENCE_CODE = 'KIMSDN20210811FA71C'
order by id desc;

select *
from DAT_KEBS_INVOICE_BATCH_DETAILS
where BATCH_NUMBER = 'KIMSINVOICE#20210623F90B6'
order by id desc;

select *
from stg_payment_reconciliation
-- where PAYMENT_T
order by ID desc;

alter table CFG_PAYMENT_METHODS
    add PAY_BILL_NO VARCHAR2(20)
/

alter table CFG_PAYMENT_METHODS
    add MPESA_ACC_NO VARCHAR2(20)
/

alter table CFG_PAYMENT_METHODS
    add VAT_NO VARCHAR2(20)
/

alter table CFG_PAYMENT_METHODS
    add PIN_NO VARCHAR2(20)
/

alter table CFG_PAYMENT_METHODS
    add SWIFT_CODE VARCHAR2(20)
/

alter table CFG_PAYMENT_METHODS
    add BANK_NUMBER VARCHAR2(20)
/



****************************Created Tables USED IN DI********************************

create TABLE log_stg_payment_reconciliation
(
    ID                     NUMBER                                      NOT NULL PRIMARY KEY,
    PAYMENT_RECONCILIATION_ID NUMBER                                   NOT NULL REFERENCES stg_payment_reconciliation(ID),
    INVOICE_ID             NUMBER                                      NOT NULL,
    REFERENCE_CODE         VARCHAR2(350 CHAR)                          NOT NULL UNIQUE,
    ACCOUNT_NAME           VARCHAR2(350 CHAR)                          NOT NULL,
    ACCOUNT_NUMBER         VARCHAR2(350 CHAR)                          NOT NULL,
    CURRENCY               VARCHAR2(350 CHAR)                          NOT NULL,
    STATUS_CODE            VARCHAR2(350 CHAR)                          NOT NULL,
    STATUS_DESCRIPTION     VARCHAR2(350 CHAR)                          NOT NULL,
    ADDITIONAL_INFORMATION VARCHAR2(350 CHAR)                          NOT NULL,
    INVOICE_AMOUNT         NUMBER(38, 3)                               NOT NULL,
    PAID_AMOUNT            NUMBER(38, 3)                               NOT NULL,
    OUTSTANDING_AMOUNT     NUMBER(38, 3)                               NOT NULL,
    TRANSACTION_ID         VARCHAR2(350 CHAR),
    TRANSACTION_DATE       DATE                        DEFAULT SYSDATE NOT NULL,
    CUSTOMER_NAME          VARCHAR2(350 CHAR),
    PAYMENT_SOURCE         VARCHAR2(350 CHAR),
    EXTRAS                 VARCHAR2(350 CHAR)                          NOT NULL,
    INVOICE_DATE           DATE                        DEFAULT SYSDATE NOT NULL,
    DESCRIPTION            CHARACTER VARYING(300),
    STATUS                 NUMBER(2, 0)                DEFAULT 0       NOT NULL,
    DESCRIPTIONS           VARCHAR2(3800 CHAR),
    VAR_FIELD_1            VARCHAR2(350 CHAR),
    VAR_FIELD_2            VARCHAR2(350 CHAR),
    VAR_FIELD_3            VARCHAR2(350 CHAR),
    VAR_FIELD_4            VARCHAR2(350 CHAR),
    VAR_FIELD_5            VARCHAR2(350 CHAR),
    VAR_FIELD_6            VARCHAR2(350 CHAR),
    VAR_FIELD_7            VARCHAR2(350 CHAR),
    VAR_FIELD_8            VARCHAR2(350 CHAR),
    VAR_FIELD_9            VARCHAR2(350 CHAR),
    VAR_FIELD_10           VARCHAR2(350 CHAR),
    CREATED_BY             VARCHAR2(100 CHAR)          DEFAULT 'ADMIN' NOT NULL ENABLE,
    CREATED_ON             TIMESTAMP(6) WITH TIME ZONE DEFAULT SYSDATE NOT NULL ENABLE,
    MODIFIED_BY            VARCHAR2(100 CHAR),
    MODIFIED_ON            TIMESTAMP(6) WITH TIME ZONE,
    UPDATE_BY              VARCHAR2(100 CHAR),
    UPDATED_ON             TIMESTAMP(6) WITH TIME ZONE,
    DELETE_BY              VARCHAR2(100 CHAR),
    DELETED_ON             TIMESTAMP(6) WITH TIME ZONE,
    VERSION                NUMBER
)
    PARTITION
    BY
    RANGE
    (
     transaction_date
        )
    INTERVAL
    (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
)
    TABLESPACE qaimssdb_data
;

create sequence log_stg_payment_reconciliation_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_stg_payment_reconciliation_trg
    before
        insert
    on log_stg_payment_reconciliation
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_stg_payment_reconciliation_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create INDEX log_stg_payment_reconciliation_IDX ON log_stg_payment_reconciliation (REFERENCE_CODE,PAYMENT_RECONCILIATION_ID, INVOICE_ID, STATUS,PAYMENT_SOURCE);





create TABLE stg_payment_reconciliation
(
    ID                     NUMBER                                      NOT NULL PRIMARY KEY,
    INVOICE_ID             NUMBER                                      NOT NULL,
    REFERENCE_CODE         VARCHAR2(350 CHAR)                          NOT NULL UNIQUE,
    ACCOUNT_NAME           VARCHAR2(350 CHAR)                          NOT NULL,
    ACCOUNT_NUMBER         VARCHAR2(350 CHAR)                          NOT NULL,
    CURRENCY               VARCHAR2(350 CHAR)                          NOT NULL,
    STATUS_CODE            VARCHAR2(350 CHAR)                          NOT NULL,
    STATUS_DESCRIPTION     VARCHAR2(350 CHAR)                          NOT NULL,
    ADDITIONAL_INFORMATION VARCHAR2(350 CHAR)                          NOT NULL,
    INVOICE_AMOUNT         NUMBER(38, 3)                               NOT NULL,
    PAID_AMOUNT            NUMBER(38, 3)                               NOT NULL,
    OUTSTANDING_AMOUNT     NUMBER(38, 3)                               NOT NULL,
    TRANSACTION_ID         VARCHAR2(350 CHAR),
    TRANSACTION_DATE       DATE                        DEFAULT SYSDATE NOT NULL,
    CUSTOMER_NAME          VARCHAR2(350 CHAR),
    PAYMENT_SOURCE         VARCHAR2(350 CHAR),
    EXTRAS                 VARCHAR2(350 CHAR)                          NOT NULL,
    INVOICE_DATE           DATE                        DEFAULT SYSDATE NOT NULL,
    DESCRIPTION            CHARACTER VARYING(300),
    STATUS                 NUMBER(2, 0)                DEFAULT 0       NOT NULL,
    DESCRIPTIONS           VARCHAR2(3800 CHAR),
    VAR_FIELD_1            VARCHAR2(350 CHAR),
    VAR_FIELD_2            VARCHAR2(350 CHAR),
    VAR_FIELD_3            VARCHAR2(350 CHAR),
    VAR_FIELD_4            VARCHAR2(350 CHAR),
    VAR_FIELD_5            VARCHAR2(350 CHAR),
    VAR_FIELD_6            VARCHAR2(350 CHAR),
    VAR_FIELD_7            VARCHAR2(350 CHAR),
    VAR_FIELD_8            VARCHAR2(350 CHAR),
    VAR_FIELD_9            VARCHAR2(350 CHAR),
    VAR_FIELD_10           VARCHAR2(350 CHAR),
    CREATED_BY             VARCHAR2(100 CHAR)          DEFAULT 'ADMIN' NOT NULL ENABLE,
    CREATED_ON             TIMESTAMP(6) WITH TIME ZONE DEFAULT SYSDATE NOT NULL ENABLE,
    MODIFIED_BY            VARCHAR2(100 CHAR),
    MODIFIED_ON            TIMESTAMP(6) WITH TIME ZONE,
    UPDATE_BY              VARCHAR2(100 CHAR),
    UPDATED_ON             TIMESTAMP(6) WITH TIME ZONE,
    DELETE_BY              VARCHAR2(100 CHAR),
    DELETED_ON             TIMESTAMP(6) WITH TIME ZONE,
    VERSION                NUMBER
)
    PARTITION
    BY
    RANGE
    (
     transaction_date
        )
    INTERVAL
    (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
)
    TABLESPACE qaimssdb_data
;

create sequence stg_payment_reconciliation_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger stg_payment_reconciliation_trg
    before
        insert
    on stg_payment_reconciliation
    for each row
begin
    if inserting then
        if :new.id is null then
            select stg_payment_reconciliation_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create INDEX stg_payment_reconciliation_REFERENCE_CODE_IDX ON stg_payment_reconciliation (REFERENCE_CODE);


select *
from LOG_JWT_TOKENS_REGISTRY ljtr
where LJTR.TRANSACTION_DATE >= trunc(sysdate) - 2;


select *
from LOG_WORKFLOW_TRANSACTIONS lwt
where LWT.TRANSACTION_DATE >= trunc(sysdate) - 2
order by ID;

select *
from stg_payment_reconciliation;

alter table STG_PAYMENT_RECONCILIATION
    add payment_transaction_date DATE;

select count(*)
from REGISTERED_FIRMS;

select *
from MANUFACTURER_TYPE_CODES;

select *
from REGION_CODES;

SELECT *
FROM stg_payment_reconciliation;

ALTER TABLE APOLLO.STG_PAYMENT_RECONCILIATION
    MODIFY PAYMENT_TRANSACTION_DATE TIMESTAMP WITH TIME ZONE;


create table DAT_KEBS_INVOICE_BATCH_DETAILS
(
    id                        NUMBER PRIMARY KEY,
    BATCH_NUMBER              VARCHAR2(200) UNIQUE,
    TOTAL_AMOUNT              NUMBER(38, 2),
    DESCRIPTION               VARCHAR2(200),
    PERMIT_QA_TABLE           NUMBER(2),
    DEMAND_NOTE_DI_TABLE      NUMBER(2),
    REMEDIATION_MS_FUEL_TABLE NUMBER(2),
    status                    NUMBER(2),
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
    created_by                VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by               VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                 VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_INVOICE_BATCH_DETAILS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;


create trigger DAT_KEBS_INVOICE_BATCH_DETAILS_seq_trg
    before
        insert
    on DAT_KEBS_INVOICE_BATCH_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
    select DAT_KEBS_INVOICE_BATCH_DETAILS_seq.nextval
    into :new.id
    from dual;

end if;

end if;
end;

create
index DAT_KEBS_INVOICE_BATCH_DETAILS_idx on DAT_KEBS_INVOICE_BATCH_DETAILS (status, BATCH_NUMBER) TABLESPACE qaimssdb_idx;
/
create table DAT_KEBS_INVOICE_BATCH_DETAILS
(
    id                        NUMBER PRIMARY KEY,
    BATCH_NUMBER              VARCHAR2(200) UNIQUE,
    TOTAL_AMOUNT              NUMBER(38, 2),
    DESCRIPTION               VARCHAR2(200),
    PERMIT_QA_TABLE           NUMBER(2),
    DEMAND_NOTE_DI_TABLE      NUMBER(2),
    REMEDIATION_MS_FUEL_TABLE NUMBER(2),
    status                    NUMBER(2),
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
    created_by                VARCHAR2(100 CHAR) DEFAULT 'admin' NOT NULL ENABLE,
    created_on                TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by               VARCHAR2(100 CHAR) DEFAULT 'admin',
    modified_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                 VARCHAR2(100 CHAR) DEFAULT 'admin',
    deleted_on                TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_INVOICE_BATCH_DETAILS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;


create trigger DAT_KEBS_INVOICE_BATCH_DETAILS_seq_trg
    before
        insert
    on DAT_KEBS_INVOICE_BATCH_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
    select DAT_KEBS_INVOICE_BATCH_DETAILS_seq.nextval
    into :new.id
    from dual;

end if;

end if;
end;

create
index DAT_KEBS_INVOICE_BATCH_DETAILS_idx on DAT_KEBS_INVOICE_BATCH_DETAILS (status, BATCH_NUMBER) TABLESPACE qaimssdb_idx;
/


create sequence DAT_KEBS_INVOICE_BATCH_DETAILS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;



create trigger DAT_KEBS_INVOICE_BATCH_DETAILS_seq_trg
    before
        insert
    on DAT_KEBS_INVOICE_BATCH_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
    select DAT_KEBS_INVOICE_BATCH_DETAILS_seq.nextval
    into :new.id
    from dual;

end if;
alter table DAT_KEBS_CD_DEMAND_NOTE
    add INVOICE_BATCH_NUMBER_ID NUMBER REFERENCES DAT_KEBS_INVOICE_BATCH_DETAILS (ID)
    /

end if;
end;

create
index DAT_KEBS_INVOICE_BATCH_DETAILS_idx on DAT_KEBS_INVOICE_BATCH_DETAILS (status, BATCH_NUMBER) TABLESPACE qaimssdb_idx;
/


create table LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE
(
    id                   NUMBER PRIMARY KEY,
    STG_PAYMENT_ID       NUMBER REFERENCES stg_payment_reconciliation (ID),
    SERVICE_NAME         VARCHAR2(500),
    REQUEST_MESSAGE_ID   VARCHAR2(500),
    CONNECTION_ID        VARCHAR2(500),
    CONNECTION_PASSWORD  VARCHAR2(500),
    REQUEST_DOCUMENT_NO  VARCHAR2(500),
    DOCUMENT_DATE        VARCHAR2(500),
    DOC_TYPE             VARCHAR2(500),
    CURRENCY_CODE        VARCHAR2(500),
    CUSTOMER_CODE        VARCHAR2(500),
    CUSTOMER_NAME        VARCHAR2(500),
    INVOICE_DESC         VARCHAR2(500),
    REVENUE_ACC          VARCHAR2(500),
    REVENUE_ACC_DESC     VARCHAR2(500),
    taxable              VARCHAR2(500),
    INVOICE_AMNT         VARCHAR2(500),
    RESPONSE_MESSAGE_ID  VARCHAR2(500),
    STATUS_CODE          VARCHAR2(500),
    STATUS_DESCRIPTION   VARCHAR2(500),
    RESPONSE_DOCUMENT_NO VARCHAR2(500),
    RESPONSE_DATE        TIMESTAMP,
    DESCRIPTION          VARCHAR2(200),
    status               NUMBER(2),
    var_field_1          VARCHAR2(350 CHAR),
    var_field_2          VARCHAR2(350 CHAR),
    var_field_3          VARCHAR2(350 CHAR),
    var_field_4          VARCHAR2(350 CHAR),
    var_field_5          VARCHAR2(350 CHAR),
    var_field_6          VARCHAR2(350 CHAR),
    var_field_7          VARCHAR2(350 CHAR),
    var_field_8          VARCHAR2(350 CHAR),
    var_field_9          VARCHAR2(350 CHAR),
    var_field_10         VARCHAR2(350 CHAR),
    created_by           VARCHAR2(100 CHAR) DEFAULT 'admin' NOT NULL ENABLE,
    created_on           TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by          VARCHAR2(100 CHAR) DEFAULT 'admin',
    modified_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by            VARCHAR2(100 CHAR) DEFAULT 'admin',
    deleted_on           TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;



create trigger LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_seq_trg
    before
        insert
    on LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE
    for each row
begin
    if inserting then
        if :new.id is null then
    select LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_seq.nextval
    into :new.id
    from dual;

end if;

end if;
end;

create
index LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_idx on LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE (STG_PAYMENT_ID, STATUS) TABLESPACE qaimssdb_idx;
/