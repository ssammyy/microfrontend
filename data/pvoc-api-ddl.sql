drop table LOG_JWT_TOKENS_REGISTRY ;

create table LOG_JWT_TOKENS_REGISTRY
(
    id                   number                                                       not null primary key,
    user_name            varchar2(350 char)                                                    not null,
    ip_address            varchar2(350 char)                                                     not null,
    user_agent            varchar2(350 char)                                                     not null,
    token_start          timestamp,
    token_end            timestamp,
    raw_token            varchar(3950)                                                   not null,
    forwarded_ip_address varchar(500),
     transaction_date date                        default sysdate not null,
    description          character varying(300)                  ,
    status               NUmber(2,0)                     DEFAULT 0                           NOT NULL,
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
    modified_by VARCHAR2(100 CHAR),
    modified_on TIMESTAMP(6) WITH TIME ZONE,
    update_by        VARCHAR2(100 CHAR),
    updated_on       TIMESTAMP(6) WITH TIME ZONE,
    delete_by        VARCHAR2(100 CHAR),
    deleted_on       TIMESTAMP(6) WITH TIME ZONE,
    VERSION          NUMBER
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
) TABLESPACE qaimssdb_data



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


select * from LOG_JWT_TOKENS_REGISTRY ljtr
--WHERE LJTR.RAW_TOKEN  ='eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDgxMzU1NzcsImV4cCI6MTYwODEzNjE3NywiYWRkcmVzcyI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6IltjdXJsLzcuNzEuMV0iLCJyb2xlcyI6IkFVVEhPUklUSUVTX1JFQUQsQVVUSE9SSVRJRVNfV1JJVEUsQ0RfU1VQRVJWSVNPUl9ERUxFVEUsQ0RfU1VQRVJWSVNPUl9NT0RJRlksQ0RfU1VQRVJWSVNPUl9SRUFELE1TX0NPTVBMQUlOVF9BQ0NFUFQsTVNfSE9EX01PRElGWSxNU19IT0RfUkVBRCxQVk9DX0FQUExJQ0FUSU9OX1JFQUQsVVNFUl9NQU5BR0VNRU5UX0RFTEVURSxVU0VSX01BTkFHRU1FTlRfTU9ESUZZLFVTRVJfTUFOQUdFTUVOVF9SRUFEIn0.yNprQQUmh20_f4KuK-DKFCY3YmX0WLBx0X6S7hXoF3vze0f9wQKavfLfCOafVJhpf6kWeFRfQH_HkE2d7_fUUA'
order by ID
;


update LOG_JWT_TOKENS_REGISTRY  set STATUS =1 
--where VERSION  is null 
;

commit;


create INDEX LOG_JWT_TOKENS_REGISTRy_user_name_status_token_end_idx ON LOG_JWT_TOKENS_REGISTRY(user_name,status,token_end);


select * from DAT_KEBS_RFC_COI where PARTNER =65;


--TRUNCATE TABLE LOG_JWT_TOKENS_REGISTRY ;

select * from DAT_KEBS_PVOC_PARTNERS;



SELECT DISTINCT CUP.* 
FROM CFG_ROLES_PRIVILEGES rp, CFG_USER_ROLES cur , CFG_USER_PRIVILEGES cup
WHERE CUP.ID = rp.PRIVILEGE_ID
  AND CUR.ID = rp.ROLES_ID
  AND rp.ROLES_ID IN (2, 40, 41, 1)
  and rp.STATUS = 1
;

SELECT *
FROM DAT_KEBS_USERS u
WHERE u.USER_NAME = 'vmuriuki';

SELECT *
FROM CFG_USER_ROLES_ASSIGNMENTS cura
WHERE CURA.USER_ID = 54
  AND STATUS = 1;

-- SELECT * FROM KEBS_KRA_PAYMENTS;

select *
from LOG_WORKFLOW_TRANSACTIONS
where TRANSACTION_DATE >= sysdate - 2
order by ID;

select *
from LOG_SL2_PAYMENTS_HEADER
order by ID;


select * from DAT_KEBS_COCS
-- where UCR_NUMBER = 'UCR202100126859'
-- where id = 283
order by ID desc;
