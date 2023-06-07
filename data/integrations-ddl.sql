create table log_kra_pin_validations
(
    id                number                                      not null primary key,
    response_code     varchar2(100 char),
    response_status   varchar2(100 char),
    response_message  varchar2(3990 char),
    response_payload  varchar2(4000 char),
    transaction_date  DATE                        default sysdate not null,
    transmission_date TIMESTAMP(6) WITH TIME ZONE,
    DESCRIPTION       VARCHAR2(300),
    status            NUMBER(2),
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
    VERSION           NUMBER
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence log_kra_pin_validations_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_kra_pin_validations_trg
    before
        insert
    on log_kra_pin_validations
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_kra_pin_validations_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table log_kra_pin_validations
    add request_reference varchar2(350 char) not null unique;
alter table log_kra_pin_validations
    add kra_pin varchar2(350 char) not null unique;
alter table LOG_KRA_PIN_VALIDATIONS
    modify response_payload varchar2(32760);
--
-- alter table LOG_KRA_PIN_VALIDATIONS
--     drop constraint SYS_C0010718;

select *
from APOLLO.CFG_INTEGRATION_CONFIGURATION
order by ID;

alter table CFG_INTEGRATION_CONFIGURATION
    add secret_value varchar2(2000 char);


select CLIENT_AUTHENTICATION_REALM
from APOLLO.CFG_INTEGRATION_CONFIGURATION;

select *
from CFG_BATCH_JOB_DETAILS
where INTEGRATION_ID = 27
order by ID;


select *
from dat_kebs_permit
where STATUS = 1
order by ID
;


create table stg_certified_products_details
(
    id                   number                                      not null primary key,
    product_name         varchar2(1000 char),
    brand_name           varchar2(1000 char),
    country_of_origin    varchar2(100 char),
    hs_code              varchar2(100 char),
    standard_governing   varchar2(1000 char),
    date_mark_issued     DATE,
    date_marked_expires  DATE,
    product_description  varchar2(3000),
    permit_number        varchar2(300),
    product_reference    varchar2(300),
    agency               varchar2(300),
    regulation_status    varchar2(300),
    product_state        varchar2(300),
    eac_response_code    varchar2(100 char),
    eac_response_status  varchar2(100 char),
    eac_response_message varchar2(3990 char),
    eac_response_payload varchar2(4000 char),
    transaction_date     DATE                        default sysdate not null,
    transmission_date    TIMESTAMP(6) WITH TIME ZONE,
    DESCRIPTION          VARCHAR2(300),
    status               NUMBER(2),
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
    last_modified_by     VARCHAR2(100 CHAR),
    last_modified_on     TIMESTAMP(6) WITH TIME ZONE,
    update_by            VARCHAR2(100 CHAR),
    updated_on           TIMESTAMP(6) WITH TIME ZONE,
    delete_by            VARCHAR2(100 CHAR),
    deleted_on           TIMESTAMP(6) WITH TIME ZONE,
    VERSION              NUMBER
) PARTITION BY RANGE (transaction_date) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence stg_certified_products_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger stg_certified_products_details_trg
    before
        insert
    on stg_certified_products_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select stg_certified_products_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


alter table STG_CERTIFIED_PRODUCTS_DETAILS
    add eac_product_reference varchar2(350);

select *
from STG_CERTIFIED_PRODUCTS_DETAILS
order by id
;

update STG_CERTIFIED_PRODUCTS_DETAILS
set status            =0,
    HS_CODE='2309.90.10',
    STANDARD_GOVERNING='KS2325'
where status = 0;

select *
from CFG_BRAND_DETAILS
order by ID;

select *
from CFG_PRODUCTS_DETAILS
order by ID;

insert into STG_CERTIFIED_PRODUCTS_DETAILS(PRODUCT_NAME, BRAND_NAME, COUNTRY_OF_ORIGIN, HS_CODE, STANDARD_GOVERNING,
                                           DATE_MARK_ISSUED, DATE_MARKED_EXPIRES, PRODUCT_DESCRIPTION, PERMIT_NUMBER,
                                           PRODUCT_REFERENCE, AGENCY, REGULATION_STATUS, PRODUCT_STATE,
                                           TRANSACTION_DATE)
select TRADE_MARK,
       'High Yield Dairy Feed',
       NVL(COUNTRY, 'KENYA'),
       '2309.90.10',
       KS_NUMBER,
       DATE_AWARDED,
       EXPIRY_DATE,
       NVL(DESCRIPTIONS, 'Dummy Descriptions'),
       PERMIT_NUMBER,
       'KEBS_REFERENCE',
       'KEBS',
       'certified',
       'approved',
       sysdate
from dat_kebs_permit
where STATUS = 1
-- and KEBS_PERMIT_NO is not null
  and DATE_AWARDED is not null
order by ID
;

commit;

select *
from STG_CERTIFIED_PRODUCTS_DETAILS
order by id;


select *
from STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER
order by ID
;
