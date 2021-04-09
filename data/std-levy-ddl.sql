drop table LOG_JWT_TOKENS_REGISTRY;

create table LOG_JWT_TOKENS_REGISTRY
(
    id                   number                                      not null primary key,
    user_name            varchar2(350 char)                          not null,
    ip_address           varchar2(350 char)                          not null,
    user_agent           varchar2(350 char)                          not null,
    token_start          timestamp,
    token_end            timestamp,
    raw_token            varchar(3950)                               not null,
    forwarded_ip_address varchar(500),
    transaction_date     date                        default sysdate not null,
    description          character varying(300),
    status               NUmber(2, 0)                DEFAULT 0       NOT NULL,
    descriptions         VARCHAR2(3800 CHAR),
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
    created_by           VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on           TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by          VARCHAR2(100 CHAR),
    modified_on          TIMESTAMP(6) WITH TIME ZONE,
    update_by            VARCHAR2(100 CHAR),
    updated_on           TIMESTAMP(6) WITH TIME ZONE,
    delete_by            VARCHAR2(100 CHAR),
    deleted_on           TIMESTAMP(6) WITH TIME ZONE,
    VERSION              NUMBER
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
) TABLESPACE qaimssdb_data;



create sequence LOG_JWT_TOKENS_REGISTRY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger LOG_JWT_TOKENS_REGISTRY_trg
    before
        insert
    on LOG_JWT_TOKENS_REGISTRY
    for each row
begin
    if inserting then
        if :new.id is null then
            select LOG_JWT_TOKENS_REGISTRY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

--SELECT * FROM USER_PART_COL_STATISTICS WHERE table_name IN('LOG_JWT_TOKENS_REGISTRY');


select *
from LOG_JWT_TOKENS_REGISTRY ljtr
--WHERE LJTR.RAW_TOKEN  ='eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDgxMzU1NzcsImV4cCI6MTYwODEzNjE3NywiYWRkcmVzcyI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6IltjdXJsLzcuNzEuMV0iLCJyb2xlcyI6IkFVVEhPUklUSUVTX1JFQUQsQVVUSE9SSVRJRVNfV1JJVEUsQ0RfU1VQRVJWSVNPUl9ERUxFVEUsQ0RfU1VQRVJWSVNPUl9NT0RJRlksQ0RfU1VQRVJWSVNPUl9SRUFELE1TX0NPTVBMQUlOVF9BQ0NFUFQsTVNfSE9EX01PRElGWSxNU19IT0RfUkVBRCxQVk9DX0FQUExJQ0FUSU9OX1JFQUQsVVNFUl9NQU5BR0VNRU5UX0RFTEVURSxVU0VSX01BTkFHRU1FTlRfTU9ESUZZLFVTRVJfTUFOQUdFTUVOVF9SRUFEIn0.yNprQQUmh20_f4KuK-DKFCY3YmX0WLBx0X6S7hXoF3vze0f9wQKavfLfCOafVJhpf6kWeFRfQH_HkE2d7_fUUA'
order by ID
;


update LOG_JWT_TOKENS_REGISTRY
set STATUS =1
where VERSION is null
;

commit;


create INDEX LOG_JWT_TOKENS_REGISTRy_user_name_status_token_end_idx ON LOG_JWT_TOKENS_REGISTRY (user_name, status, token_end);


select *
from DAT_KEBS_RFC_COI
where PARTNER = 65;


--TRUNCATE TABLE LOG_JWT_TOKENS_REGISTRY ;

select *
from DAT_KEBS_PVOC_PARTNERS;



select distinct CUP.*
from CFG_ROLES_PRIVILEGES rp,
     CFG_USER_ROLES cur,
     CFG_USER_PRIVILEGES cup
where CUP.ID = rp.PRIVILEGE_ID
  and CUR.ID = rp.ROLES_ID
  and rp.ROLES_ID in (2, 40, 41, 1)
  and rp.STATUS = 1
;

select *
from DAT_KEBS_USERS u
where u.USER_NAME = 'vmuriuki';

select *
from CFG_USER_ROLES_ASSIGNMENTS cura
where CURA.USER_ID = 54
  and STATUS = 1;

select *
from DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS
order by ID;


select *
from DAT_KEBS_STDLVY_PENALTIES
order by id;


-- select *
-- from USER_TABLES t
-- where t.table_name like '%DEPART%';

select *
from KEBS_MANUFACTURER_DEPARTMENTS;

select *
from DAT_KEBS_MANUFACTURERS dkm
where ENTRY_NUMBER is not null;



drop table STG_STANDARDS_LEVY_KRA_ENTRY;

create TABLE STG_STANDARDS_LEVY_KRA_ENTRY
(
    id                NUMBER                                      NOT NULL PRIMARY KEY,
    ENTRY_NUMBER      VARCHAR2(128 BYTE),
    KRA_PIN           VARCHAR2(128 BYTE),
    POSTAL_CODE       VARCHAR2(128 BYTE),
    MANUFACTURER_NAME VARCHAR2(128 BYTE),
    ADDRESS           VARCHAR2(128 BYTE),
    PHYSICAL_ADDRESS  VARCHAR2(128 BYTE),
    STREET            VARCHAR2(128 BYTE),
    TOWN              VARCHAR2(128 BYTE),
    COUNTY            VARCHAR2(128 BYTE),
    DEPARTMENT_CODE   VARCHAR2(128 BYTE),
    TELEPHONE         VARCHAR2(128 BYTE),
    EMAIL             VARCHAR2(128 BYTE),
    REGISTRATION_DATE DATE,
    COMMENCEMENT_DATE DATE,
    ACTIVATION_STATUS VARCHAR2(128 BYTE) CHECK ( ACTIVATION_STATUS IN ('ACTIVE',
                                                                       'INACTIVE',
                                                                       'CLOSED')) ENABLE,
    REGION            VARCHAR2(128 BYTE),
    transaction_date  DATE                        DEFAULT sysdate NOT NULL enable,
    status            NUMBER(2,
                          0)                      DEFAULT 0       NOT NULL,
    descriptions      VARCHAR2(3800 CHAR),
    var_field_1       VARCHAR2(350 CHAR),
    var_field_2       VARCHAR2(350 CHAR),
    var_field_3       VARCHAR2(350 CHAR),
    var_field_4       VARCHAR2(350 CHAR),
    var_field_5       VARCHAR2(350 CHAR),
    var_field_6       VARCHAR2(350 CHAR),
    var_field_7       VARCHAR2(350 CHAR),
    var_field_8       VARCHAR2(350 CHAR),
    var_field_9       VARCHAR2(350 CHAR),
    var_field_10      VARCHAR2(350 CHAR),
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    UPDATED_BY        VARCHAR2(128 BYTE),
    UPDATED_AT        DATE,
    LAST_MODIFIED_BY  VARCHAR2(128 BYTE),
    LAST_MODIFIED_AT  DATE,
    modified_by       VARCHAR2(100 CHAR),
    modified_on       TIMESTAMP(6) WITH TIME ZONE,
    update_by         VARCHAR2(100 CHAR),
    updated_on        TIMESTAMP(6) WITH TIME ZONE,
    delete_by         VARCHAR2(100 CHAR),
    deleted_on        TIMESTAMP(6) WITH TIME ZONE,
    VERSION           NUMBER
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;



create sequence STG_STANDARDS_LEVY_KRA_ENTRY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger STG_STANDARDS_LEVY_KRA_ENTRY_trg
    before
        insert
    on STG_STANDARDS_LEVY_KRA_ENTRY
    for each row
begin
    if inserting then
        if :new.id is null then
            select STG_STANDARDS_LEVY_KRA_ENTRY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/



select *
from LOG_WORKFLOW_TRANSACTIONS lwt
where LWT.INTEGRATION_RESPONSE is not null
order by LWT.ID;

select *
from CFG_INTEGRATION_CONFIGURATION cic
order by id;

alter table CFG_INTEGRATION_CONFIGURATION
    add SUCCESS_CODE varchar2(10 char) DEFAULT '00' NOT NULL;

select *
from CFG_SERVICE_MAPS_WORKFLOWS csmw;

select CSM.BASIC_AUTHENTICATION_VALUE, CSM.DIGEST_AUTHENTICATION_VALUE
from CFG_SERVICE_MAPS csm;

select *
from LOG_JWT_TOKENS_REGISTRY ljtr;

commit;


update CFG_INTEGRATION_CONFIGURATION
set CREATED_ON  = null,
    MODIFIED_ON = null,
    DELETED_ON  = null
where created_on is null
;



alter table APOLLO.CFG_INTEGRATION_CONFIGURATION
    add DELETED_ON TIMESTAMP(6) WITH TIME ZONE;

alter table APOLLO.CFG_INTEGRATION_CONFIGURATION
    drop column DELETED_ON;

alter table APOLLO.CFG_INTEGRATION_CONFIGURATION
    modify MODIFIED_ON TIMESTAMP;
alter table APOLLO.CFG_INTEGRATION_CONFIGURATION
    modify DELETED_ON TIMESTAMP;

select *
from LOG_WORKFLOW_TRANSACTIONS lwt
where LWT.TRANSACTION_DATE > trunc(sysdate);

select *
from DAT_KEBS_INVOICE_TRANSACTIONS;



--CREATE TABLE LOG_INTEGRATION_HEADER 
--( id NUMBER NOT NULL PRIMARY KEY,
--integration_hash varchar2(350 char) ,
--transmission_date DATE ,
--kra_pin varchar2(350 char) ,
--manufacturer_name varchar2(350 char),
--payment_slip_NUMBER varchar2(350 char),
--payment_slip_date date,	
--payment_type  varchar2(350 char),
--payment_date date,	
--total_levy_declared NUMBER(38,3),	
--total_penalty_declared NUMBER(38,3),
--total_payment_amount NUMBER(38,3),
--bank varchar2(350 char),
--bank_ref_no vharchar2(350 char),
--
--raw_token varchar(3950) NOT NULL,
--forwarded_ip_address varchar(500),
--transaction_date DATE DEFAULT sysdate NOT NULL,
--description CHARACTER VARYING(300) ,
--status NUMBER(2,
--0) DEFAULT 0 NOT NULL,
--descriptions VARCHAR2(3800 CHAR),
--var_field_1 VARCHAR2(350 CHAR),
--var_field_2 VARCHAR2(350 CHAR),
--var_field_3 VARCHAR2(350 CHAR),
--var_field_4 VARCHAR2(350 CHAR),
--var_field_5 VARCHAR2(350 CHAR),
--var_field_6 VARCHAR2(350 CHAR),
--var_field_7 VARCHAR2(350 CHAR),
--var_field_8 VARCHAR2(350 CHAR),
--var_field_9 VARCHAR2(350 CHAR),
--var_field_10 VARCHAR2(350 CHAR),
--created_by VARCHAR2(100 CHAR) DEFAULT 'admin' NOT NULL ENABLE,
--created_on TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
--modified_by VARCHAR2(100 CHAR),
--modified_on TIMESTAMP(6) WITH TIME ZONE,
--update_by VARCHAR2(100 CHAR),
--updated_on TIMESTAMP(6) WITH TIME ZONE,
--delete_by VARCHAR2(100 CHAR),
--deleted_on TIMESTAMP(6) WITH TIME ZONE,
--VERSION NUMBER ) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY')) ( PARTITION keb_20200315
--VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' ) ) TABLESPACE qaimssdb_data


create sequence LOG_JWT_TOKENS_REGISTRY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger LOG_JWT_TOKENS_REGISTRY_trg
    before
        insert
    on LOG_JWT_TOKENS_REGISTRY
    for each row
begin
    if inserting then
        if :new.id is null then
            select LOG_JWT_TOKENS_REGISTRY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/



select *
from CFG_STATUS_VALUES;

create TABLE cfg_batch_JOB_TYPES
(
    id               NUMBER                                      NOT NULL PRIMARY KEY,
    name             varchar2(350 char)                          NOT NULL UNIQUE,
    DESCRIPTION      VARCHAR2(300),
    status           NUMBER(2, 0),
    descriptions     VARCHAR2(3800 CHAR),
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
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
    update_by        VARCHAR2(100 CHAR),
    updated_on       TIMESTAMP(6) WITH TIME ZONE,
    delete_by        VARCHAR2(100 CHAR),
    deleted_on       TIMESTAMP(6) WITH TIME ZONE,
    VERSION          NUMBER
) TABLESPACE qaimssdb_data
;


create sequence CFG_BATCH_JOB_TYPES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_BATCH_JOB_TYPES_trg
    before
        insert
    on CFG_BATCH_JOB_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_BATCH_JOB_TYPES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create TABLE cfg_batch_job_details
(
    id                    NUMBER                                      NOT NULL PRIMARY KEY,
    job_type_id           NUMBER                                      NOT NULL REFERENCES cfg_batch_JOB_TYPES (id),
    integration_ID        NUMBER                                      NOT NULL REFERENCES CFG_INTEGRATION_CONFIGURATION (id),
    start_status          number(2, 0)                DEFAULT 0       NOT NULL,
    end_success_status    number(2, 0)                DEFAULT 30      NOT NULL,
    end_pending_status    number(2, 0)                DEFAULT 10      NOT NULL,
    end_failure_status    number(2, 0)                DEFAULT 20      NOT NULL,
    end_exception_status  number(2, 0)                DEFAULT 25      NOT NULL,
    retries               number(2, 0)                DEFAULT 3       NOT NULl,
    retried               number(2, 0)                DEFAULT 0       NOT NULL,
    processing_actor_bean varchar2(350 char),
    DESCRIPTION           VARCHAR2(300),
    status                NUMBER(2, 0),
    descriptions          VARCHAR2(3800 CHAR),
    var_field_1           VARCHAR2(350 CHAR),
    var_field_2           VARCHAR2(350 CHAR),
    var_field_3           VARCHAR2(350 CHAR),
    var_field_4           VARCHAR2(350 CHAR),
    var_field_5           VARCHAR2(350 CHAR),
    var_field_6           VARCHAR2(350 CHAR),
    var_field_7           VARCHAR2(350 CHAR),
    var_field_8           VARCHAR2(350 CHAR),
    var_field_9           VARCHAR2(350 CHAR),
    var_field_10          VARCHAR2(350 CHAR),
    created_by            VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on            TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by      VARCHAR2(100 CHAR),
    last_modified_on      TIMESTAMP(6) WITH TIME ZONE,
    update_by             VARCHAR2(100 CHAR),
    updated_on            TIMESTAMP(6) WITH TIME ZONE,
    delete_by             VARCHAR2(100 CHAR),
    deleted_on            TIMESTAMP(6) WITH TIME ZONE,
    VERSION               NUMBER
) TABLESPACE qaimssdb_data
;

create sequence CFG_BATCH_JOB_DETAILS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_BATCH_JOB_DETAILS_trg
    before
        insert
    on CFG_BATCH_JOB_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_BATCH_JOB_DETAILS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS
order by ID;

create TABLE STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
(
    id                NUMBER                                      NOT NULL PRIMARY KEY,
    manufacturer_id   NUMBER REFERENCES DAT_KEBS_MANUFACTURERS (id),
    kra_pin           varchar2(350 char),
    manufacturer_name varchar2(350 char),
    record_status     varchar2(35 char),
    transaction_date  DATE                        DEFAULT sysdate NOT NULL enable,
    transmission_date TIMESTAMP(6) WITH TIME ZONE,
    DESCRIPTION       VARCHAR2(300),
    status            NUMBER(2,
                          0),
    descriptions      VARCHAR2(3800 CHAR),
    var_field_1       VARCHAR2(350 CHAR),
    var_field_2       VARCHAR2(350 CHAR),
    var_field_3       VARCHAR2(350 CHAR),
    var_field_4       VARCHAR2(350 CHAR),
    var_field_5       VARCHAR2(350 CHAR),
    var_field_6       VARCHAR2(350 CHAR),
    var_field_7       VARCHAR2(350 CHAR),
    var_field_8       VARCHAR2(350 CHAR),
    var_field_9       VARCHAR2(350 CHAR),
    var_field_10      VARCHAR2(350 CHAR),
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by  VARCHAR2(100 CHAR),
    last_modified_on  TIMESTAMP(6) WITH TIME ZONE,
    update_by         VARCHAR2(100 CHAR),
    updated_on        TIMESTAMP(6) WITH TIME ZONE,
    delete_by         VARCHAR2(100 CHAR),
    deleted_on        TIMESTAMP(6) WITH TIME ZONE,
    VERSION           NUMBER,
    CONSTRAINT check_record_status CHECK (record_status IN ('Active',
                                                            'Cancelled'))
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;


create sequence STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER_trg
    before
        insert
    on STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
    for each row
begin
    if inserting then
        if :new.id is null then
            select STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER;

alter table STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
    add retries NUMBER DEFAULT 3 NOT NULL;

alter table STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
    add retried NUMBER DEFAULT 0 NOT NULL;

alter table CFG_BATCH_JOB_DETAILS
    add registration_date DATE default sysdate NOT NULL;


alter table CFG_BATCH_JOB_DETAILS
    add standard_date_format varchar2(50 char) default 'dd-MMM-yyyy HH:mm:ss' NOT NULL;


alter table CFG_BATCH_JOB_DETAILS
    add JOB_URI varchar2(50 char) default '127.0.0.1' NOT NULL;

-- alter table APOLLO.STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER drop CONSTRAINT SYS_C0053165;


create INDEX STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER_manufacturer_id_status_idx ON STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER (manufacturer_id, status);



select ENTRY_NO, MANUFACTURERS_NAME, KRA_PIN, 'Active', REGISTRATION_DATE, trunc(sysdate), 0
from STAGING_FIRMS_DETAILS sfd
where REGISTRATION_DATE is not null
  and KRA_PIN is not null
  and rownum < 30;

insert into STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER (MANUFACTURER_ID, MANUFACTURER_NAME, KRA_PIN, RECORD_STATUS,
                                                          REGISTRATION_DATE, TRANSACTION_DATE, STATUS)
select ENTRY_NO, MANUFACTURERS_NAME, KRA_PIN, 'Active', REGISTRATION_DATE, trunc(sysdate), 0
from STAGING_FIRMS_DETAILS sfd
where REGISTRATION_DATE is not null
  and KRA_PIN is not null
  and rownum < 30;

commit;

select *
from STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER sslmen
where SSLMEN.STATUS != 0
order by ID;

update STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
set STATUS =0
where STATUS != 0
  and rownum < 11;

select *
from STG_PAYMENT_RECONCILIATION spr;

--4633L, 4989L, 9982L, 11695L, 14303L, 14380L


select *
from CFG_BATCH_JOB_DETAILS cbjd;
update CFG_BATCH_JOB_DETAILS
set INITIAL_PAGE = 0
where id = 2;

--DELETE FROM STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER;


select *
from LOG_WORKFLOW_TRANSACTIONS lwt
where LWT.TRANSACTION_DATE > trunc(sysdate)
order by ID;


select *
from CFG_USER_PRIVILEGES cup
where CUP.NAME = 'SL_APPROVE_VISIT_REPORT';

select *
from CFG_ROLES_PRIVILEGES crp
where PRIVILEGE_ID = 224;

select *
from CFG_USER_ROLES_ASSIGNMENTS cura
where USER_ID = 54
order by ID;

select *
from DAT_KEBS_USERS dku
where USER_NAME = 'vmuriuki';


select *
from DAT_KEBS_PVOC_WAIVERS_STATUS pvocwaiver0_
--where pvocwaiver0_.ID=?
;

SELECT *
FROM CFG_USER_ROLES cur
WHERE ROLE_NAME = 'Kenneth'
;


SELECT max(id)
FROM CFG_USER_PRIVILEGES cup;

-- SELECT CFG_USER_.nextval
-- FROM dual;

-- alter sequence CFG_USER_PRIVILEGES_SEQ increment by 1;

SELECT id,
       FIRST_NAME,
       LAST_NAME,
       USER_NAME,
       EMAIL,
       ENABLED,
       ACCOUNT_EXPIRED,
       ACCOUNT_LOCKED,
       CREDENTIALS_EXPIRED,
       STATUS,
       REGISTRATION_DATE,
       USER_TYPE,
       TITLE
FROM DAT_KEBS_USERS dku
WHERE dku.EMAIL = 'musyokah@kebs.org'
ORDER BY dku.ID;

SELECT *
FROM REF_TITLES rt;

SELECT *
FROM CFG_USER_TYPES cut;


SELECT DISTINCT Cur.ID, CUR.DESCRIPTIONS
FROM CFG_ROLES_PRIVILEGES crp,
     CFG_USER_ROLES cur
WHERE crp.ROLES_ID = cur.ID
ORDER BY 1;

SELECT DISTINCT CUR.*
FROM CFG_ROLES_PRIVILEGES p,
     CFG_USER_ROLES cur
WHERE p.STATUS = 1
  AND cur.STATUS = 1
  AND p.ROLES_ID IN (181)
;

SELECT CUP.*
FROM CFG_ROLES_PRIVILEGES crp,
     CFG_USER_PRIVILEGES cup
WHERE crp.PRIVILEGE_ID = CUP.ID
  AND crp.ROLES_ID = 613
  AND crp.STATUS = 1;


SELECT DISTINCT CUR.*
FROM CFG_ROLES_PRIVILEGES p,
     CFG_USER_ROLES cur
WHERE P.ROLES_ID = cur.ID
  and p.STATUS = 1
  AND cur.STATUS = 1
  AND p.ROLES_ID IN (181);


ALTER TABLE APOLLO.CFG_ROLES_PRIVILEGES
    ADD CONSTRAINT CFG_ROLES_PRIVILEGES_UN UNIQUE (ROLES_ID, PRIVILEGE_ID, STATUS)
        ENABLE;

-- select *
-- from USER_CONSTRAINTS
-- where CONSTRAINT_NAME = 'SYS_C0016969';

select CFG_ROLES_PRIVILEGES_SEQ.nextval
from dual;

-- alter sequence CFG_KEBS_COUNTRY_TYPE_CODES_SEQ
alter sequence CFG_ROLES_PRIVILEGES_SEQ increment by 1;


alter table CFG_ROLES_PRIVILEGES
    drop constraint CFG_ROLES_PRIVILEGES_UN;

SELECT count(*), ROLES_ID, PRIVILEGE_ID, STATUS
FROM CFG_ROLES_PRIVILEGES crp
--WHERE crp.ROLES_ID = 40
GROUP BY ROLES_ID, PRIVILEGE_ID, STATUS
HAVING count(*) > 1
;

SELECT max(id)
FROM CFG_ROLES_PRIVILEGES crp;

SELECT *
FROM CFG_USER_ROLES cur
WHERE cur.id NOT IN (
--SELECT id FROM CFG_USER_ROLES cur 
--MINUS 
--(
    SELECT crp.ROLES_ID FROM CFG_ROLES_PRIVILEGES crp WHERE ROLES_ID NOT IN (613))
--)
;


SELECT count(*)
FROM CFG_USER_PRIVILEGES cup;


SELECT *
FROM CFG_ROLES_PRIVILEGES crp
WHERE crp.ROLES_ID = 101
  and PRIVILEGE_ID = 3
;

select *
from CFG_USER_PRIVILEGES
where NAME IN ('PERMIT_RENEWAL', 'CD_INSPECTION_MODIFY')
;

SELECT cur.ID
FROM CFG_USER_ROLES cur
WHERE cur.ROLE_NAME = 'MS HOF'
;


SELECT CFG_ROLES_PRIVILEGES_SEQ.nextval
FROM dual;
--
-- SELECT *
-- FROM USER_SEQUENCES
-- WHERE SEQUENCE_name = 'CFG_USER_PRIVILEGES_SEQ';


create sequence CFG_ROLES_PRIVILEGES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_ROLES_PRIVILEGES_trg
    before
        insert
    on CFG_ROLES_PRIVILEGES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_ROLES_PRIVILEGES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


select count(*)
from DAT_KEBS_USERS;


SELECT count(*)
FROM CFG_USER_ROLES_ASSIGNMENTS cura
where ROLE_ID != 1;

SELECT u.*
FROM CFG_USER_ROLES_ASSIGNMENTS cura,
     DAT_KEBS_USERS u
where cura.ROLE_ID != 1
  and cura.USER_ID = u.ID
  and u.STATUS = 1
  and cura.STATUS = 1
;


select *
from CFG_USER_ROLES_ASSIGNMENTS
order by ID;

SELECT r.*
FROM CFG_USER_ROLES_ASSIGNMENTS UR,
     CFG_USER_ROLES R
WHERE UR.ROLE_ID = R.ID
  AND UR.STATUS = 1
  AND UR.USER_ID = 54
order by r.ID;


SELECT *
FROM LOG_WORKFLOW_TRANSACTIONS
WHERE TRANSACTION_DATE > trunc(SYSDATE) - 30
ORDER BY ID
;

SELECT *
FROM CFG_STATUS_VALUES;

SELECT *
FROM CFG_BATCH_JOB_DETAILS cbjd;



select r.ID as REGION_ID, r.REGION, c.id as COUNTY_ID, c.COUNTY, t.id as TOWN_id, t.TOWN
from CFG_KEBS_REGIONS r
         right outer join CFG_KEBS_COUNTIES c on c.REGION_ID = r.ID
         right outer join CFG_KEBS_TOWNS t on t.COUNTY_ID = c.ID
where t.COUNTY_ID is not null
  and c.COUNTY = 'BUNGOMA'
order by REGION, COUNTY, TOWN;

select *
from CFG_KEBS_TOWNS
where id = 1023;

select *
from CFG_KEBS_COUNTIES
where COUNTY is null
;


create table log_sl2_payments_header
(
    id                               number                                      not null primary key,
    request_header_transmissionDate  timestamp,
    request_header_entry_No          varchar2(350 char),
    request_header_kra_pin           varchar2(350 char),
    request_header_manufacturer_name varchar2(350 char),
    request_header_payment_Slip_No   varchar2(350 char),
    request_header_payment_Slip_date date,
    request_header_payment_Type      varchar2(350),
    request_header_TOTAL_DECL_AMT    number(18, 3),
    request_header_TOTAL_PENALTY_AMT number(18, 3),
    request_header_TOTAL_PAYMENT_AMT number(18, 3),
    request_header_BANK              varchar2(350),
    request_BANK_REF_NO              varchar2(350),
    transaction_date                 DATE                        default sysdate not null,
    DESCRIPTION                      VARCHAR2(300),
    status                           NUMBER(2, 0),
    descriptions                     VARCHAR2(3800 CHAR),
    var_field_1                      VARCHAR2(350 CHAR),
    var_field_2                      VARCHAR2(350 CHAR),
    var_field_3                      VARCHAR2(350 CHAR),
    var_field_4                      VARCHAR2(350 CHAR),
    var_field_5                      VARCHAR2(350 CHAR),
    var_field_6                      VARCHAR2(350 CHAR),
    var_field_7                      VARCHAR2(350 CHAR),
    var_field_8                      VARCHAR2(350 CHAR),
    var_field_9                      VARCHAR2(350 CHAR),
    var_field_10                     VARCHAR2(350 CHAR),
    created_by                       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by                 VARCHAR2(100 CHAR),
    last_modified_on                 TIMESTAMP(6) WITH TIME ZONE,
    update_by                        VARCHAR2(100 CHAR),
    updated_on                       TIMESTAMP(6) WITH TIME ZONE,
    delete_by                        VARCHAR2(100 CHAR),
    deleted_on                       TIMESTAMP(6) WITH TIME ZONE,
    VERSION                          NUMBER
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence log_sl2_payments_header_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_sl2_payments_header_trg
    before
        insert
    on log_sl2_payments_header
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_sl2_payments_header_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table log_sl2_payments_details
(
    id               number                                      not null primary key,
    header_ID        number references log_sl2_payments_header (id),
    transaction_type varchar2(350),
    commodity_Type   varchar2(350),
    penalty_Order_No varchar2(350),
    PERIOD_FROM      date,
    PERIOD_TO        date,
    QTY_MANF         number(19, 3),
    EX_FACT_VAL      number(19, 3),
    LEVY_PAID        number(19, 3),
    penalty_Paid     number(19, 3),
    transaction_date DATE                        default sysdate not null,
    DESCRIPTION      VARCHAR2(300),
    status           NUMBER(2, 0),
    descriptions     VARCHAR2(3800 CHAR),
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
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
    update_by        VARCHAR2(100 CHAR),
    updated_on       TIMESTAMP(6) WITH TIME ZONE,
    delete_by        VARCHAR2(100 CHAR),
    deleted_on       TIMESTAMP(6) WITH TIME ZONE,
    VERSION          NUMBER
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence log_sl2_payments_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_sl2_payments_details_trg
    before
        insert
    on log_sl2_payments_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_sl2_payments_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table LOG_SL2_PAYMENTS_HEADER rename column REQUEST_HEADER_TRANSMISSIONDATE to REQUEST_HEADER_TRANSMISSION_DATE;

select *
from LOG_SL2_PAYMENTS_HEADER
;

select *
from LOG_WORKFLOW_TRANSACTIONS
where TRANSACTION_DATE > trunc(sysdate)
-- and TRANSACTION_REFERENCE ='e3ef23ba38e4'
order by ID
;
select *
from LOG_SL2_PAYMENTS_DETAILS
order by id
;


select *
from LOG_JWT_TOKENS_REGISTRY
where transaction_date > trunc(sysdate)
order by id
;

select *
from DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS
order by ID
;

select *
from DAT_KEBS_STD_LEVY_NOTIFICATION_FORM
order by ID
;

select *
from LOG_SL2_PAYMENTS_HEADER
order by id
;


select *
from STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
-- where status = 0
order by id;

commit;

select *
from CFG_BATCH_JOB_DETAILS
order by id
;

rollback ;

alter system kill session '629,1178,@1';

alter system kill session '874,51420#';

-- select inst_id,sid,serial# from gv$session where username='APOLLO' and sid = '629';




select *
from CFG_INTEGRATION_CONFIGURATION
order by ID
;
-- LOCKING TRANSACTION/SESSION
-- select
--     c.owner,
--     c.object_name,
--     c.object_type,
--     b.sid,
--     b.serial#,
--     b.status,
--     b.osuser,
--     b.machine
-- from
--     v$locked_object a ,
--     v$session b,
--     dba_objects c
-- where
--         b.sid = a.session_id
--   and
--         a.object_id = c.object_id;

UPDATE APOLLO.CFG_INTEGRATION_CONFIGURATION t
SET t.UNKNOWN_HOST_KEY = null
WHERE t.ID = 26;

commit;



alter system kill session '874,51420#';

SELECT c.sid,
       substr(object_name, 1, 20)                                   OBJECT,
       c.username,
       substr(c.program, length(c.program) - 20, length(c.program)) image,
       DECODE(b.type,
              'MR', 'Media Recovery',
              'RT', 'Redo Thread',
              'UN', 'User Name',
              'TX', 'Transaction',
              'TM', 'DML',
              'UL', 'PL/SQL User Lock',
              'DX', 'Distributed Xaction',
              'CF', 'Control File',
              'IS', 'Instance State',
              'FS', 'File Set',
              'IR', 'Instance Recovery',
              'ST', 'Disk Space Transaction',
              'TS', 'Temp Segment',
              'IV', 'Library Cache Invalidation',
              'LS', 'Log Start or Switch',
              'RW', 'Row Wait',
              'SQ', 'Sequence Number',
              'TE', 'Extend Table',
              'TT', 'Temp Table',
              b.type)                                               lock_type,
       DECODE(b.lmode,
              0, 'None', /* Mon Lock equivalent */
              1, 'Null', /* NOT */
              2, 'Row-SELECT (SS)', /* LIKE */
              3, 'Row-X (SX)', /* R */
              4, 'Share', /* SELECT */
              5, 'SELECT/Row-X (SSX)', /* C */
              6, 'Exclusive', /* X */
              to_char(b.lmode))                                     mode_held,
       DECODE(b.request,
              0, 'None', /* Mon Lock equivalent */
              1, 'Null', /* NOT */
              2, 'Row-SELECT (SS)', /* LIKE */
              3, 'Row-X (SX)', /* R */
              4, 'Share', /* SELECT */
              5, 'SELECT/Row-X (SSX)', /* C */
              6, 'Exclusive', /* X */
              to_char(b.request))                                   mode_requested
FROM sys.dba_objects a,
     sys.v_$lock b,
     sys.v_$session c
WHERE a.object_id = b.id1
  AND b.sid = c.sid
  AND OWNER NOT IN ('SYS', 'SYSTEM');



rollback;

commit;


select *
from LOG_WORKFLOW_TRANSACTIONS
where TRANSACTION_DATE >= trunc(sysdate)
order by ID;

-- select *
-- from USER_tables where table_name like '%PEN%';


select *
from DAT_KEBS_STDLVY_PENALTIES
order by ID;

select *
from LOG_SL2_PAYMENTS_DETAILS
order by id;


create TABLE STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
(
    id                      NUMBER                                      NOT NULL PRIMARY KEY,
    manufacturer_id         NUMBER REFERENCES DAT_KEBS_MANUFACTURERS (id),
    kra_pin                 varchar2(350 char),
    manufacturer_name       varchar2(350 char),
    PERIOD_FROM             date,
    PERIOD_TO               date,
    record_status           varchar2(35 char),
    transaction_date        DATE                        DEFAULT sysdate NOT NULL enable,
    penalty_generation_date TIMESTAMP(6) WITH TIME ZONE,
    penalty_payable         NUMBER(24, 3),
    transmission_date       TIMESTAMP(6) WITH TIME ZONE,
    DESCRIPTION             VARCHAR2(300),
    status                  NUMBER(2,
                                0),
    descriptions            VARCHAR2(3800 CHAR),
    var_field_1             VARCHAR2(350 CHAR),
    var_field_2             VARCHAR2(350 CHAR),
    var_field_3             VARCHAR2(350 CHAR),
    var_field_4             VARCHAR2(350 CHAR),
    var_field_5             VARCHAR2(350 CHAR),
    var_field_6             VARCHAR2(350 CHAR),
    var_field_7             VARCHAR2(350 CHAR),
    var_field_8             VARCHAR2(350 CHAR),
    var_field_9             VARCHAR2(350 CHAR),
    var_field_10            VARCHAR2(350 CHAR),
    created_by              VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by        VARCHAR2(100 CHAR),
    last_modified_on        TIMESTAMP(6) WITH TIME ZONE,
    update_by               VARCHAR2(100 CHAR),
    updated_on              TIMESTAMP(6) WITH TIME ZONE,
    delete_by               VARCHAR2(100 CHAR),
    deleted_on              TIMESTAMP(6) WITH TIME ZONE,
    VERSION                 NUMBER,
    CONSTRAINT check_record_status_penalty CHECK (record_status IN ('Active',
                                                                    'Cancelled'))
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;


create sequence STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_trg
    before
        insert
    on STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
    for each row
begin
    if inserting then
        if :new.id is null then
            select STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

-- select *
-- from user_constraints where constraint_name ='SYS_C0010623';
--
-- alter table STG_STANDARDS_LEVY_MANUFACTURER_PENALTY drop constraint SYS_C0010623;


alter table STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
    add retries number(2, 0);
alter table STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
    add retried number(2, 0);
--
-- insert into STG_STANDARDS_LEVY_MANUFACTURER_PENALTY(MANUFACTURER_ID, MANUFACTURER_NAME, KRA_PIN, RECORD_STATUS, STATUS,
--                                                     PERIOD_FROM, PERIOD_TO)
-- select manufacturer_id, manufacturer_name, kra_pin, record_status, status, trunc(sysdate - 24), trunc(sysdate + 6)
-- from STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
-- order by id;

commit;

select *
from STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
order by id
;

commit;

select *
from STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
order by id
;


update STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
set PENALTY_PAYABLE         = 10000,
    penalty_generation_date = sysdate - 1
where id < 100
;

update STG_STANDARDS_LEVY_MANUFACTURER_PENALTY
set status = 0
where status = 20
  and TRANSMISSION_DATE is not null;

rollback;

commit;
--a0dcc0c573ada27408d5e4a9c2e9c154aab8aece596d18f73363fdcfe1ddeb72
--a0dcc0c573ada27408d5e4a9c2e9c154aab8aece596d18f73363fdcfe1ddeb72


SELECT *
FROM CFG_INTEGRATION_CONFIGURATION cic
ORDER BY ID;

-- UPDATE APOLLO.CFG_INTEGRATION_CONFIGURATION t
-- SET t.URL             = 'https://196.61.52.30/KEBS/kebs',
--     HASHING_ALGORITHM = 'SHA-256'
-- WHERE t.ID = 26;

UPDATE CFG_INTEGRATION_CONFIGURATION
SET USERNAME ='',
    PASSWORD =''
WHERE id = 26;

ALTER TABLE CFG_INTEGRATION_CONFIGURATION
    ADD HASHING_ALGORITHM varchar2(25);
-- ALTER TABLE CFG_INTEGRATION_CONFIGURATION  DROP COLUMN HASHING_ALOGORITHM;

COMMIT;
