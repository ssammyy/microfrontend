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



create sequence DAT_KEBS_PVOC_COR_TIMELINES_DATA_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COR_TIMELINES_DATA_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COR_TIMELINES_DATA_trg
    before
        insert
    on DAT_KEBS_PVOC_COR_TIMELINES_DATA
    for each row
begin
    if inserting then
        if :new.ID is null then
    select DAT_KEBS_PVOC_COR_TIMELINES_DATA_seq.nextval
    into :new.ID
    from dual;

end if;

end if;
end;


-- cor timeline data start

-- auto-generated definition
create table DAT_KEBS_PVOC_COI_TIMELINES_DATA
(
    ID                               NUMBER                      not null
        primary key,
    COI_NUMBER                       VARCHAR2(50 char),
    UCR_NUMBER                       VARCHAR2(50 char)           not null,
    RFC_DATE                         TIMESTAMP(6) WITH TIME ZONE not null,
    DATE_OF_INSPECTION               TIMESTAMP(6) WITH TIME ZONE not null,
    COI_ISSUE_DATE                   TIMESTAMP(6) WITH TIME ZONE,
    REQUEST_DATE_OF_INSPECTION       TIMESTAMP(6) WITH TIME ZONE not null,
    COI_CONFIRMATION_DATE            TIMESTAMP(6) WITH TIME ZONE not null,
    RFC_TO_INSPECTION_DAYS           NUMBER                      not null,
    INSPECTION_TO_ISSUANCE_DAYS      NUMBER                      not null,
    RFC_TO_ISSUANCE_DAYS             NUMBER                      not null,
    ACC_DOCUMENTS_TO_ISSUANCE_DAYS   NUMBER                      not null,
    PAYMENT_TO_ISSUANCE_DAYS         NUMBER                      not null,
    FINAL_DOCUMENTS_TO_ISSUANCE_DAYS NUMBER                      not null,
    ACC_DOCUMENTS_SUBMISSION_DATE    TIMESTAMP(6) WITH TIME ZONE,
    FINAL_DOCUMENTS_SUBMISSION_DATE  TIMESTAMP(6) WITH TIME ZONE,
    PAYMENT_DATE                     TIMESTAMP(6) WITH TIME ZONE,
    STATUS                           NUMBER(2),
    VAR_FIELD_1                      VARCHAR2(350 char),
    VAR_FIELD_2                      VARCHAR2(350 char),
    VAR_FIELD_3                      VARCHAR2(350 char),
    VAR_FIELD_4                      VARCHAR2(350 char),
    VAR_FIELD_5                      VARCHAR2(350 char),
    VAR_FIELD_6                      VARCHAR2(350 char),
    VAR_FIELD_7                      VARCHAR2(350 char),
    VAR_FIELD_8                      VARCHAR2(350 char),
    VAR_FIELD_9                      VARCHAR2(350 char),
    VAR_FIELD_10                     VARCHAR2(350 char),
    CREATED_BY                       VARCHAR2(100 char)          default 'admin',
    CREATED_ON                       TIMESTAMP(6) WITH TIME ZONE default sysdate,
    MODIFIED_BY                      VARCHAR2(100 char),
    MODIFIED_ON                      TIMESTAMP(6) WITH TIME ZONE,
    DELETE_BY                        VARCHAR2(100 char),
    DELETED_ON                       TIMESTAMP(6) WITH TIME ZONE,
    HOD_STATUS                       VARCHAR2(200 char)          default 'Pending',
    MPVOC_AGENT                      NUMBER
        references DAT_KEBS_USERS
            on delete cascade,
    PVOC_MONIT_STATUS                NUMBER                      default 0,
    PVOC_MONIT_STARTED_ON            TIMESTAMP(6),
    PVOC_MONIT_COMPLETED_ON          TIMESTAMP(6),
    PVOC_MONIT_PROCESS_INSTANCE_ID   VARCHAR2(50)
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COI_TIMELINES_DATA_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COI_TIMELINES_DATA_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COI_TIMELINES_DATA_trg
    before
        insert
    on DAT_KEBS_PVOC_COI_TIMELINES_DATA
    for each row
begin
    if inserting then
        if :new.ID is null then
    select DAT_KEBS_PVOC_COI_TIMELINES_DATA_seq.nextval
    into :new.ID
    from dual;

end if;

end if;
end;
    /



-- cor timeline data end

create table DAT_KEBS_HS_CODES
(
    id                number  not null primary key,
    superCode             varchar2(4000 char ) ,
    superDesc             varchar2(350 char),
    super2Desc             varchar2(350 char),
    hscode             varchar2(350 char),
    hscodeDesc             varchar2(350 char),
    unit             varchar2(350 char),
    rate             varchar2(350 char),
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
;


create sequence DAT_KEBS_HS_CODES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_HS_CODES_trg
    before
        insert
    on DAT_KEBS_HS_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_HS_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;




create table DAT_KEBS_PVOC_COMPLAIN_STATUS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    APPROVE      VARCHAR2(4000 char),
    REJECT      VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAIN_STATUS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAIN_STATUS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAIN_STATUS_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAIN_STATUS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAIN_STATUS_seq.nextval
            into :new.ID
            from dual;
        end if;

    end if;
end;

-- INVOICE RESPONCE STATUS

create table DAT_KEBS_INVOICE_RESPONCE_STATUS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,KEBS_STANDARDS_REQUEST_REMARKS
    SUCCESS      VARCHAR2(4000 char),
    NOT_FOUND      VARCHAR2(4000 char),
    DUPLICATE_TRANSACTION      VARCHAR2(4000 char),
    INTERNAL_SYSTEM_ERROR      VARCHAR2(4000 char),
    ERROR      VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_INVOICE_RESPONCE_STATUS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_INVOICE_RESPONCE_STATUS_seq.nextval
from dual;

create trigger DAT_KEBS_INVOICE_RESPONCE_STATUS_trg
    before
        insert
    on DAT_KEBS_INVOICE_RESPONCE_STATUS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_INVOICE_RESPONCE_STATUS_seq.nextval
            into :new.ID
            from dual;
        end if;

    end if;
end;


-- PVOC COMPLIANT TABLES
create table DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    ENABLED        NUMBER(2)   default 0,
    EMAIL      VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    ENABLED        NUMBER(2)   default 0,
    EMAIL_ID    NUMBER references DAT_KEBS_PVOC_COMPLAINTS_EMAIL_VERIFICATION(ID) on delete cascade,
    TOKEN      VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char) default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    EXPIRY_DATE     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAINTS_TOKEN_VERIFICATION_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;




-- End email confirmation email

create table DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    HS_CODE      VARCHAR2(4000 char),
    INDUSTRIAL_SPARES           VARCHAR2(4000 char),
    COUNTRY_OF_ORIGIN           VARCHAR2(4000 char),
    MACHINE_TO_BE_FITTED           NUMBER (4),
    EXCEPTION_ID      NUMBER references DAT_KEBS_PVOC_APPLICATION(ID) on delete cascade,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY_trg
    before
        insert
    on DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_EXCEPTION_INDUSTRIAL_SPARES_CATEGORY_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    MACHINE_DESCRIPTION      VARCHAR2(4000 char),
    MAKE_MODEL           VARCHAR2(4000 char),
    HS_CODE           VARCHAR2(4000 char),
    DUTY_RATE           NUMBER (4),
    EXCEPTION_ID      NUMBER references DAT_KEBS_PVOC_APPLICATION(ID) on delete cascade,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY_trg
    before
        insert
    on DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_EXCEPTION_MAIN_MACHINERY_CATEGORY_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;



create table DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)   default 0,
    RAW_MATERIAL_DESCRIPTION      VARCHAR2(4000 char),
    END_PRODUCT           VARCHAR2(4000 char),
    DUTY_RATE           NUMBER (3),
    EXCEPTION_ID      NUMBER references DAT_KEBS_PVOC_APPLICATION(ID) on delete cascade,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY_trg
    before
        insert
    on DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_EXCEPTION_RAW_MATERIAL_CATEGORY_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


--Exceptions types/categories tables goes here


create table DAT_KEBS_PVOC_AGENT_CONTRACT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME            VARCHAR2(200 CHAR),
    SERVICE_RENDERED_ID NUMBER REFERENCES DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED(ID) ON DELETE CASCADE ,
    APPLICABLE_ROYALTY            VARCHAR2(500 CHAR),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_AGENT_CONTRACT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_AGENT_CONTRACT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_AGENT_CONTRACT_trg
    before
        insert
    on DAT_KEBS_PVOC_AGENT_CONTRACT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_AGENT_CONTRACT_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME            VARCHAR2(200 CHAR),
    DESCRIPTION            VARCHAR2(500 CHAR),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED_trg
    before
        insert
    on DAT_KEBS_PVOC_REVENUE_REPORT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_AGENT_SERVICE_RENDERED_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_RECONCILIATION_REPORT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    ROUTE_TYPE           VARCHAR2(4000 char),
    CERTIFICATE_NO           VARCHAR2(4000 char),
    ROUTE           VARCHAR2(4000 char),
    KEC_NO           VARCHAR2(4000 char),
    CERTIFICATION_FEE           VARCHAR2(4000 char),
    IDF_NO           VARCHAR2(4000 char),
    FOB         DATE,
    FOB_VALUE           VARCHAR2(4000 char),
    CHARGE_FOR_VERICATION           VARCHAR2(4000 char),
    IMPOTER_NAME           VARCHAR2(4000 char),
    EXMPOTER_NAME           VARCHAR2(4000 char),
    INSPECTION_FEE           VARCHAR2(4000 char),
    VERIFICATION_FEE           VARCHAR2(4000 char),
    DIFF           VARCHAR2(4000 char),
    TEST           VARCHAR2(4000 char),
    ROYALTIES_TO_KEBS           VARCHAR2(4000 char),
    COUNTRY_OF_SUPPLY           VARCHAR2(4000 char),
    INSUANCE_DATE           VARCHAR2(4000 char),
    SEAL_NO           VARCHAR2(4000 char),
    REMARKS           VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_RECONCILIATION_REPORT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_RECONCILIATION_REPORT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_RECONCILIATION_REPORT_trg
    before
        insert
    on DAT_KEBS_PVOC_RECONCILIATION_REPORT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_RECONCILIATION_REPORT_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_REVENUE_REPORT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    ROUTE_TYPE           VARCHAR2(4000 char),
    COC_NO           VARCHAR2(4000 char),
    ROUTE           VARCHAR2(4000 char),
    KEC_NO           VARCHAR2(4000 char),
    CERTIFICATION_FEE           VARCHAR2(4000 char),
    IDF_NO           VARCHAR2(4000 char),
    FOB         DATE,
    FOB_VALUE           VARCHAR2(4000 char),
    CHARGE_FOR_VERICATION           VARCHAR2(4000 char),
    IMPOTER_NAME           VARCHAR2(4000 char),
    EXMPOTER_NAME           VARCHAR2(4000 char),
    INSPECTION_FEE           VARCHAR2(4000 char),
    VERIFICATION_FEE           VARCHAR2(4000 char),
    DIFF           VARCHAR2(4000 char),
    TEST           VARCHAR2(4000 char),
    ROYALTIES_TO_KEBS           VARCHAR2(4000 char),
    COUNTRY_OF_SUPPLY           VARCHAR2(4000 char),
    INSUANCE_DATE           VARCHAR2(4000 char),
    SEAL_NO           VARCHAR2(4000 char),
    REMARKS           VARCHAR2(4000 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
    ADDRESS        VARCHAR2(4000 char),
    REFERENCE      VARCHAR2(1000 char),
    BOTTOM_ADDRESS VARCHAR2(4000 char)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_REVENUE_REPORT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_REVENUE_REPORT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_REVENUE_REPORT_trg
    before
        insert
    on DAT_KEBS_PVOC_REVENUE_REPORT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_REVENUE_REPORT_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE
(
    ID             NUMBER                                      not null
        primary key,
    STATUS         NUMBER(2)                   default 0,
    BODY           VARCHAR2(4000 char),
    COC_ID      NUMBER references DAT_KEBS_COCS_BAK(ID) on delete cascade,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    REVIEW_STATUS  VARCHAR2(200 char)          default 'Pending',
    ADDRESS        VARCHAR2(4000 char),
    REFERENCE      VARCHAR2(1000 char),
    BOTTOM_ADDRESS VARCHAR2(4000 char)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE_trg
    before
        insert
    on DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_TIMELINE_DATA_PENALTY_INVOICE_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_AGENT_MONITORING_STATUS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(350 char),
    DESCRIPTION       VARCHAR2(350 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_AGENT_MONITORING_STATUS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_AGENT_MONITORING_STATUS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_AGENT_MONITORING_STATUS_trg
    before
        insert
    on DAT_KEBS_PVOC_AGENT_MONITORING_STATUS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_AGENT_MONITORING_STATUS_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_COC_SEALING_REPORT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    COCNO       VARCHAR2(350 char),
    COUNTRY_OF_SUPPLY       VARCHAR2(350 char),
    SHIPMENTMODE       VARCHAR2(350 char),
    ROUTE       VARCHAR2(350 char),
    SHIPMEMT_SEAL_NUMBERS       VARCHAR2(350 char),
    SHIPMENT_LINE_QUANTITY       VARCHAR2(350 char),
    SHIPMENT_LINE_UNIT_OF_MEASURE       VARCHAR2(350 char),
    SHIPMENT_LINE_DESCRIPTION       VARCHAR2(350 char),
    DATEEXTRACTED       TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    CONCLUSION       VARCHAR2(350 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COC_SEALING_REPORT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COC_SEALING_REPORT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COC_SEALING_REPORT_trg
    before
        insert
    on DAT_KEBS_PVOC_COC_SEALING_REPORT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COC_SEALING_REPORT_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_COC_TIMELINES_REPORT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    COCNO       VARCHAR2(350 char),
    COUNTRY_OF_SUPPLY       VARCHAR2(350 char),
    SHIPMENTMODE       VARCHAR2(350 char),
    IMPORTER_EMAIL       VARCHAR2(350 char),
    ROUTE       VARCHAR2(350 char),
    RFCDATE       VARCHAR2(350 char),
    DATEOFINSPECTION       VARCHAR2(350 char),
    COCISSUEDATE       VARCHAR2(350 char),
    RFTTOINS       VARCHAR2(350 char),
    INSTOISS       VARCHAR2(350 char),
    RFCTOISS       VARCHAR2(350 char),
    DATEEXTRACTED       VARCHAR2(350 char),
    INIT_ANALYSIS_ON_RFCTOINS       VARCHAR2(350 char),
    ACTION_DATE       VARCHAR2(350 char),
    RESPONSE_3DAYS_YESNO       VARCHAR2(350 char),
    IMPORTER_RESPONSE_ANALYSIS       VARCHAR2(350 char),
    PVOC_PARTINER_RESPONSE_ANALYSIS_AND_RECOMMENDATION       VARCHAR2(350 char),
    CONCLUSION       VARCHAR2(350 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COC_TIMELINES_REPORT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COC_TIMELINES_REPORT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COC_TIMELINES_REPORT_trg
    before
        insert
    on DAT_KEBS_PVOC_COC_TIMELINES_REPORT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COC_TIMELINES_REPORT_seq.nextval
            into :new.ID
            from dual;
        end if;

    end if;
end;


create table DAT_KEBS_PVOC_COMPLAINT
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    COMPLAINT_NAME       VARCHAR2(350 char),
    PHONE_NO       VARCHAR2(350 char),
    ADDRESS       VARCHAR2(350 char),
    PVOC_AGENT       VARCHAR2(350 char),
    COMPLIANT_NATURE       VARCHAR2(350 char),
    COMPLIANT_CATEGORY REFERENCES DAT_KEBS_PVOC_COMPLAINT_CATEGORY(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAINT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAINT_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAINT_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAINT
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAINT_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_COMPLAINT_CATEGORY
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(350 char),
    DESCRIPTION       VARCHAR2(350 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAINT_CATEGORY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAINT_CATEGORY_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAINT_CATEGORY_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAINT_CATEGORY
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAINT_CATEGORY_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_COMPLAINT_CERTIFICATION_SUB_CATEGORIES
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(350 char),
    DESCRIPTION       VARCHAR2(350 char),
    COMPLAIN_CATEGORY_ID REFERENCES DAT_KEBS_PVOC_COMPLAINT_CATEGORY(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_COMPLAINT_CERTIFICATION_SUB_CATEGORIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_COMPLAINT_CERTIFICATION_SUB_CATEGORIES_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_COMPLAINT_CERTIFICATION_SUB_CATEGORIES_trg
    before
        insert
    on DAT_KEBS_PVOC_COMPLAINT_CATEGORY
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_COMPLAINT_CERTIFICATION_SUB_CATEGORIES_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;

-- PVOC COMPLIANT TABLES

create table DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(350 char),
    DESCRIPTION       VARCHAR2(350 char),
    CATEGORY_ID  REFERENCES DAT_KEBS_PVOC_WAIVERS_CATEGORIES(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS_trg
    before
        insert
    on DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_WAIVERS_CATEGORIES_DOCUMENTS_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_WAIVERS_CATEGORIES
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(350 char),
    DESCRIPTION       VARCHAR2(350 char),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_WAIVERS_CATEGORIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_WAIVERS_CATEGORIES_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_WAIVERS_CATEGORIES_trg
    before
        insert
    on DAT_KEBS_PVOC_WAIVERS_CATEGORIES
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_WAIVERS_CATEGORIES_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_WAIVERS_REQUEST_LETTER
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    BODY        VARCHAR2(700 CHAR),
    WAIVER_ID  NUMBER REFERENCES DAT_KEBS_PVOC_WAIVERS_APPLICATION(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create table DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(300 char),
    DATE_HELD       VARCHAR2(200 char),
    START_TIME       VARCHAR2(200 char),
    LOCATION       VARCHAR2(200 char),
    CALLED_BY       VARCHAR2(200 char),
    MEETING_TYPE       VARCHAR2(200 char),
    CHAIR_PERSON       VARCHAR2(200 char),
    SECRETARY       VARCHAR2(200 char),
    ATTENDANCE       VARCHAR2(600 char),
    AGENDA        VARCHAR2(700 char),
    PREVIOUS_MINUTES       VARCHAR2(400 char),
    PREVIOUS_MEETING_DISCUSSION       VARCHAR2(700 char),
    CURRENT_MINUTE       VARCHAR2(400 char),
    CURRENT_MEETING_DISCUSSION       VARCHAR2(800 char),
    REVIEW_STATUS       VARCHAR2(200 char),
    PREPARED_BY       VARCHAR2(200 char),
    CONFIRMED_BY       VARCHAR2(200 char),
    WAIVER_ID  NUMBER REFERENCES DAT_KEBS_PVOC_WAIVERS_APPLICATION(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES_trg
    before
        insert
    on DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_WAIVERS_WETC_MINUTES_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_LAB_REPORTS

(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    LAB_NAME       VARCHAR2(350 char),
    LAB_REPORT_FILE_PATH        VARCHAR(350 char),
    APPROVAL_STATUS NUMBER(2) DEFAULT 0,
    DEFFERAL_STATUS NUMBER(2) DEFAULT 0,
    REJECTION_STATUS NUMBER(2) DEFAULT 0,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_LAB_REPORTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_LAB_REPORTS_seq.nextval
from dual;

create trigger DAT_KEBS_LAB_REPORTS_trg
    before
        insert
    on DAT_KEBS_LAB_REPORTS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_LAB_REPORTS_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_WAIVERS_STATUS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    APPROVAL       VARCHAR2(200 char),
    DEFFERAL VARCHAR2(200 char ) ,
    REJECTION VARCHAR2(200 char ) ,
    WETC_CHAIRMAN VARCHAR2(200 char ) ,
    SECRETARY_NCS VARCHAR2(200 char ) ,
    CS VARCHAR2(200 char ) ,
    APPROVAL_STATUS NUMBER(2) DEFAULT 0,
    DEFFERAL_STATUS NUMBER(2) DEFAULT 0,
    REJECTION_STATUS NUMBER(2) DEFAULT 0,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_WAIVERS_STATUS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_WAIVERS_STATUS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_WAIVERS_STATUS_trg
    before
        insert
    on DAT_KEBS_PVOC_WAIVERS_STATUS
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_WAIVERS_STATUS_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_MASTER_LIST
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    SERIAL_NO       VARCHAR2(200 char),
    APPLICANT VARCHAR2(200 char ) ,
    PRODUCT_DESCRIPTION VARCHAR2(200 char ) ,
    UNIT VARCHAR2(200 char ) ,
    QUANTITY VARCHAR2(200 char ) ,
    ORIGIN VARCHAR2(200 char ) ,
    CURRENCY VARCHAR2(200 char ) ,
    TOTAL_AMOUNT VARCHAR2(200 char ) ,
    DOCUMENTATION VARCHAR2(200 char ) ,
    WAIVERS_APPLICATION_ID REFERENCES DAT_KEBS_PVOC_WAIVERS_APPLICATION(ID) ON DELETE CASCADE ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_MASTER_LIST_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_MASTER_LIST_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_MASTER_LIST_trg
    before
        insert
    on DAT_KEBS_PVOC_MASTER_LIST
    for each row
begin
    if inserting then
        if :new.ID is null then
            select DAT_KEBS_PVOC_MASTER_LIST_seq.nextval
            into :new.ID
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_WAIVERS_APPLICATION
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    APPLICANT_NAME       VARCHAR2(200 char),
    PHONE_NUMBER VARCHAR2(200 char ) ,
    EMAIL_ADDRESS VARCHAR2(200 char ) ,
    KRA_PIN VARCHAR2(200 char ) ,
    ADDRESS VARCHAR2(200 char ) ,
    CATEGORY VARCHAR2(200 char ) ,
    JUSTIFICATION VARCHAR2(200 char ) ,
    PRODUCT_DESCRIPTION VARCHAR2(200 char ) ,
    DOCUMENTATION VARCHAR2(200 char ) ,
    REVIEW_STATUS VARCHAR2(200 char ) default 'PENDING REVIEW',
    SERIAL_NO VARCHAR2(200 char ) ,
    CS_RESPONCE_CODE VARCHAR2(200 char ) ,
    CS_RESPONCE_MESSAGE VARCHAR2(200 char ) ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_WAIVERS_APPLICATION_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_WAIVERS_APPLICATION_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_WAIVERS_APPLICATION_trg
    before
        insert
    on DAT_KEBS_PVOC_WAIVERS_APPLICATION
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_WAIVERS_APPLICATION_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS
(
    id             NUMBER  not null primary key,
    map_id         NUMBER,
    status         NUMBER(2)  default 0,
    initial_status       VARCHAR2(200 char),
    differed_status VARCHAR2(200 char ) ,
    exception_status      VARCHAR2(200 char ),
    rejected_status      VARCHAR2(200 char ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS_trg
    before
        insert
    on DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create table CFG_KEBS_PERMIT_PAYMENT_UNITS
(
    id             NUMBER  not null primary key,
    status         NUMBER(2)                   default 0,
    standard_application_cost       VARCHAR2(200 char),
    standard_standard_cost VARCHAR2(200 char ) ,
    standard_inspection_cost      VARCHAR2(200 char ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence CFG_KEBS_PERMIT_PAYMENT_UNITS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select CFG_KEBS_PERMIT_PAYMENT_UNITS_SEQ.nextval
from dual;

create trigger CFG_KEBS_PERMIT_PAYMENT_UNITS_TRG
    before
        insert
    on CFG_KEBS_PERMIT_PAYMENT_UNITS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_PERMIT_PAYMENT_UNITS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(200 char),
    DESCRIPTION VARCHAR2(200 char ) ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY_trg
    before
        insert
    on DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_APPLICATION_ORIGNIN_COUNTRY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(200 char),
    DESCRIPTION VARCHAR2(200 char ) ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES_trg
    before
        insert
    on DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_APPLICATION_EXCEPTION_CATEGORIES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create table DAT_KEBS_PVOC_APPLICATION_TYPE
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    NAME       VARCHAR2(200 char),
    DESCRIPTION VARCHAR2(200 char ) ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_APPLICATION_TYPE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_APPLICATION_TYPE_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_APPLICATION_TYPE_trg
    before
        insert
    on DAT_KEBS_PVOC_APPLICATION_TYPE
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_APPLICATION_TYPE_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_PVOC_APPLICATION
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    USER_ID       NUMBER REFERENCES dat_kebs_users(id) ON DELETE CASCADE,
    CONPANY_NAME VARCHAR2(200 char ) ,
    COMPANY_PIN_NO      VARCHAR2(200 char ),
    EMAIL         VARCHAR2(200 CHAR ),
    TELEPHONE_NO         VARCHAR2(200 CHAR ),
    POSTAL_AADRESS         VARCHAR2(200 CHAR ),
    PHYSICAL_LOCATION         VARCHAR2(200 CHAR ),
    CONTACT_PERSORN         VARCHAR2(200 CHAR ),
    ADDRESS         VARCHAR2(200 CHAR ),
    EXCEPTION_CATEGORY         VARCHAR2(200 CHAR ),
    HS_CODE         VARCHAR2(200 CHAR ),
    COUNTRY_OF_ORIGIN         VARCHAR2(200 CHAR ),
    INDUSTRIAL_SPARES_DESCRIPTION         VARCHAR2(200 CHAR ),
    MACHINE_TO_BE_FITTED         VARCHAR2(200 CHAR ),
    RAW_MATERIAL_DESCRIPTION         VARCHAR2(200 CHAR ),
    END_PRODUCT         VARCHAR2(200 CHAR ),
    DUTY_RATE         VARCHAR2(200 CHAR ),
    MACHINE_DESCRIPTION         VARCHAR2(200 CHAR ),
    SUPERVISOR_ID     NUMBER REFERENCES dat_kebs_users(id) on delete cascade ,
    SECTION_ID_OFFICER   NUMBER REFERENCES dat_kebs_users(id) on delete cascade ,
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_APPLICATION_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_APPLICATION_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_APPLICATION_trg
    before
        insert
    on DAT_KEBS_PVOC_APPLICATION
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_APPLICATION_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create table DAT_KEBS_PVOC_APPLICATION_PRODUCTS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    PVOC_APPLICATION_ID    NUMBER REFERENCES DAT_KEBS_PVOC_APPLICATION(ID) ON DELETE CASCADE,
    PRODUCT_NAME VARCHAR2(200 char ) ,
    BRAND      VARCHAR2(200 char ),
    KEBS_STANDARDIZATION_MARK_PERMIT VARCHAR2(200 CHAR ),
    EXPIRELY_DATE         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PVOC_APPLICATION_PRODUCTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PVOC_APPLICATION_PRODUCTS_seq.nextval
from dual;

create trigger DAT_KEBS_PVOC_APPLICATION_PRODUCTS_trg
    before
        insert
    on DAT_KEBS_PVOC_APPLICATION_PRODUCTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PVOC_APPLICATION_PRODUCTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    OWNER_NAME       VARCHAR2(200 char),
    MAKE_OF_VEHICLE VARCHAR2(200 char ) ,
    ENGINE_CAPACITY      VARCHAR2(200 char ),
    MANUFACTURE_DATE         VARCHAR2(200 CHAR ),
    ENGINE_NO         VARCHAR2(200 CHAR ),
    TYPE_OF_BODY         VARCHAR2(200 CHAR ),
    REG_NO         VARCHAR2(200 CHAR ),
    INSURANCE_NO         VARCHAR2(200 CHAR ),
    ADDRESS         VARCHAR2(200 CHAR ),
    MODEL         VARCHAR2(200 CHAR ),
    ODOMETER_READING         VARCHAR2(200 CHAR ),
    FIRST_REGISTRATION_DATE         VARCHAR2(200 CHAR ),
    CHASSIS_NO         VARCHAR2(200 CHAR ),
    CARRYING_CAPACITY         VARCHAR2(200 CHAR ),
    EXPIRY_DATE         VARCHAR2(200 CHAR ),
    PLACE_OF_INSPECTION         VARCHAR2(200 CHAR ),
    RECEIPT_NO         VARCHAR2(200 CHAR ),
    INSPECTION_NO         VARCHAR2(200 CHAR ),
    INSPECTION_START_TIME         VARCHAR2(200 CHAR ),
    INSPECTION_END_TIME         VARCHAR2(200 CHAR ),
    INSPECTION_PERIOD         VARCHAR2(200 CHAR ),
    INSPECTED_BY         VARCHAR2(200 CHAR ),
    CHECKED_BY         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    GENERAR_INSPECTION NUMBER REFERENCES DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL(ID) ON DELETE CASCADE ,
    OVERALL_APPEARANCE       VARCHAR2(200 char),
    OVERALL_APPEARANCE_STATUS       VARCHAR2(200 char),
    OVERALL_APPEARANCE_REMARKS       VARCHAR2(200 char),
    CONDITION_OF_PAINT VARCHAR2(200 char ) ,
    CONDITION_OF_PAINT_STATUS VARCHAR2(200 char ) ,
    CONDITION_OF_PAINT_REMARKS VARCHAR2(200 char ) ,
    DOORS      VARCHAR2(200 char ),
    DOORS_STATUS      VARCHAR2(200 char ),
    DOORS_REMARKS      VARCHAR2(200 char ),
    WINDOWS         VARCHAR2(200 CHAR ),
    WINDOWS_STATUS         VARCHAR2(200 CHAR ),
    WINDOWS_REMARKS         VARCHAR2(200 CHAR ),
    SUNROOF         VARCHAR2(200 CHAR ),
    SUNROOF_STATUS         VARCHAR2(200 CHAR ),
    SUNROOF_REMARKS         VARCHAR2(200 CHAR ),
    EXTERNAL_MIRRORS         VARCHAR2(200 CHAR ),
    EXTERNAL_MIRRORS_STATUS         VARCHAR2(200 CHAR ),
    EXTERNAL_MIRRORS_REMARKS         VARCHAR2(200 CHAR ),
    GLASSES         VARCHAR2(200 CHAR ),
    GLASSES_STATUS         VARCHAR2(200 CHAR ),
    GLASSES_REMARKS         VARCHAR2(200 CHAR ),
    WIPERS_AND_WASHERS         VARCHAR2(200 CHAR ),
    WIPERS_AND_WASHERS_STATUS         VARCHAR2(200 CHAR ),
    WIPERS_AND_WASHERS_REMARKS         VARCHAR2(200 CHAR ),
    SEATS         VARCHAR2(200 CHAR ),
    SEATS_STATUS         VARCHAR2(200 CHAR ),
    SEATS_REMARKS         VARCHAR2(200 CHAR ),
    MOULDING         VARCHAR2(200 CHAR ),
    MOULDING_STATUS         VARCHAR2(200 CHAR ),
    MOULDING_REMARKS         VARCHAR2(200 CHAR ),
    SAFETY_BELTS         VARCHAR2(200 CHAR ),
    SAFETY_BELTS_STATUS         VARCHAR2(200 CHAR ),
    SAFETY_BELTS_REMARKS         VARCHAR2(200 CHAR ),
    STEERING_WHEEL         VARCHAR2(200 CHAR ),
    STEERING_WHEEL_STATUS         VARCHAR2(200 CHAR ),
    STEERING_WHEEL_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_PEDAL         VARCHAR2(200 CHAR ),
    BRAKE_PEDAL_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_PEDAL_REMARKS         VARCHAR2(200 CHAR ),
    CLUTCH_PEDAL         VARCHAR2(200 CHAR ),
    CLUTCH_PEDAL_STATUS         VARCHAR2(200 CHAR ),
    CLUTCH_PEDAL_REMARKS         VARCHAR2(200 CHAR ),
    PARKING_BRAKE_LEVER         VARCHAR2(200 CHAR ),
    PARKING_BRAKE_LEVER_STATUS         VARCHAR2(200 CHAR ),
    PARKING_BRAKE_LEVER_REMARKS         VARCHAR2(200 CHAR ),
    HEADLIGHTS         VARCHAR2(200 CHAR ),
    HEADLIGHTS_STATUS         VARCHAR2(200 CHAR ),
    HEADLIGHTS_REMARKS         VARCHAR2(200 CHAR ),
    PARKING_LIGHTS         VARCHAR2(200 CHAR ),
    PARKING_LIGHTS_STATUS         VARCHAR2(200 CHAR ),
    PARKING_LIGHTS_REMARKS         VARCHAR2(200 CHAR ),
    DIRECTION_INDICATORS         VARCHAR2(200 CHAR ),
    DIRECTION_INDICATORS_STATUS         VARCHAR2(200 CHAR ),
    DIRECTION_INDICATORS_REMARKS         VARCHAR2(200 CHAR ),
    REVERSING_LIGHT         VARCHAR2(200 CHAR ),
    REVERSING_LIGHT_STATUS         VARCHAR2(200 CHAR ),
    REVERSING_LIGHT_REMARKS         VARCHAR2(200 CHAR ),
    COURTESY_LIGHT         VARCHAR2(200 CHAR ),
    COURTESY_LIGHT_STATUS         VARCHAR2(200 CHAR ),
    COURTESY_LIGHT_REMARKS         VARCHAR2(200 CHAR ),
    REAR_NO_PLATE_LIGHT         VARCHAR2(200 CHAR ),
    REAR_NO_PLATE_LIGHT_REMARKS         VARCHAR2(200 CHAR ),
    REAR_NO_PLATE_LIGHT_STATUS         VARCHAR2(200 CHAR ),
    STOP_LIGHTS         VARCHAR2(200 CHAR ),
    STOP_LIGHTS_STATUS         VARCHAR2(200 CHAR ),
    STOP_LIGHTS_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_BUMPER         VARCHAR2(200 CHAR ),
    FRONT_BUMPER_STATUS         VARCHAR2(200 CHAR ),
    FRONT_BUMPER_REMARKS         VARCHAR2(200 CHAR ),
    ROOF_RACK         VARCHAR2(200 CHAR ),
    ROOF_RACK_STATUS         VARCHAR2(200 CHAR ),
    ROOF_RACK_REMARKS         VARCHAR2(200 CHAR ),
    ANTENNA         VARCHAR2(200 CHAR ),
    ANTENNA_STATUS         VARCHAR2(200 CHAR ),
    ANTENNA_REMARKS         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create table DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    GENERAR_INSPECTION NUMBER REFERENCES DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL(ID) ON DELETE CASCADE ,
    BONNET       VARCHAR2(200 char),
    BONNET_STATUS       VARCHAR2(200 char),
    BONNET_REMARKS       VARCHAR2(200 char),
    ENGINE VARCHAR2(200 char ) ,
    ENGINE_STATUS VARCHAR2(200 char ) ,
    ENGINE_REMARKS VARCHAR2(200 char ) ,
    BATTERY      VARCHAR2(200 char ),
    BATTERY_STATUS      VARCHAR2(200 char ),
    BATTERY_REMARKS      VARCHAR2(200 char ),
    BATTERY_CARRIER         VARCHAR2(200 CHAR ),
    BATTERY_CARRIER_STATUS         VARCHAR2(200 CHAR ),
    BATTERY_CARRIER_REMARKS         VARCHAR2(200 CHAR ),
    WIRING_HARNESS         VARCHAR2(200 CHAR ),
    WIRING_HARNESS_STATUS         VARCHAR2(200 CHAR ),
    WIRING_HARNESS_REMARKS         VARCHAR2(200 CHAR ),
    STARTER_MOTOR         VARCHAR2(200 CHAR ),
    STARTER_MOTOR_STATUS         VARCHAR2(200 CHAR ),
    STARTER_MOTOR_REMARKS         VARCHAR2(200 CHAR ),
    ALTERNATOR         VARCHAR2(200 CHAR ),
    ALTERNATOR_STATUS         VARCHAR2(200 CHAR ),
    ALTERNATOR_REMARKS         VARCHAR2(200 CHAR ),
    RADIATOR         VARCHAR2(200 CHAR ),
    RADIATOR_STATUS         VARCHAR2(200 CHAR ),
    RADIATOR_REMARKS         VARCHAR2(200 CHAR ),
    RADIOTOR_HOSES         VARCHAR2(200 CHAR ),
    RADIOTOR_HOSES_STATUS         VARCHAR2(200 CHAR ),
    RADIOTOR_HOSES_REMARKS         VARCHAR2(200 CHAR ),
    WATER_PUMP         VARCHAR2(200 CHAR ),
    WATER_PUMP_STATUS         VARCHAR2(200 CHAR ),
    WATER_PUMP_REMARKS         VARCHAR2(200 CHAR ),
    CARBURETOR         VARCHAR2(200 CHAR ),
    CARBURETOR_STATUS         VARCHAR2(200 CHAR ),
    CARBURETOR_REMARKS         VARCHAR2(200 CHAR ),
    HIGH_TENSION_CABLES         VARCHAR2(200 CHAR ),
    HIGH_TENSION_CABLES_STATUS         VARCHAR2(200 CHAR ),
    HIGH_TENSION_CABLES_REMARKS         VARCHAR2(200 CHAR ),
    AC_CONDENSER         VARCHAR2(200 CHAR ),
    AC_CONDENSER_STATUS         VARCHAR2(200 CHAR ),
    AC_CONDENSER_REMARKS         VARCHAR2(200 CHAR ),
    POWER_STEERING         VARCHAR2(200 CHAR ),
    POWER_STEERING_STATUS         VARCHAR2(200 CHAR ),
    POWER_STEERING_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_MASTER_CYLINDER         VARCHAR2(200 CHAR ),
    BRAKE_MASTER_CYLINDER_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_MASTER_CYLINDER_REMARKS         VARCHAR2(200 CHAR ),
    CLUTCH_MASTER_CYLINDER         VARCHAR2(200 CHAR ),
    CLUTCH_MASTER_CYLINDER_STATUS         VARCHAR2(200 CHAR ),
    CLUTCH_MASTER_CYLINDER_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_SYSTEM         VARCHAR2(200 CHAR ),
    BRAKE_SYSTEM_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_SYSTEM_REMARKS         VARCHAR2(200 CHAR ),
    FUEL_PIPES         VARCHAR2(200 CHAR ),
    FUEL_PIPES_STATUS         VARCHAR2(200 CHAR ),
    FUEL_PIPES_REMARKS         VARCHAR2(200 CHAR ),
    FLEXIBLE_BRAKE_PIPES         VARCHAR2(200 CHAR ),
    FLEXIBLE_BRAKE_PIPES_STATUS         VARCHAR2(200 CHAR ),
    FLEXIBLE_BRAKE_PIPES_REMARKS         VARCHAR2(200 CHAR ),
    WINDSCREEN_WASHER_BOTTLE         VARCHAR2(200 CHAR ),
    WINDSCREEN_WASHER_BOTTLE_STATUS         VARCHAR2(200 CHAR ),
    WINDSCREEN_WASHER_BOTTLE_REMARKS         VARCHAR2(200 CHAR ),
    BOOT_LID         VARCHAR2(200 CHAR ),
    BOOT_LID_STATUS         VARCHAR2(200 CHAR ),
    BOOT_LID_REMARKS         VARCHAR2(200 CHAR ),
    JACK_AND_HANDLE         VARCHAR2(200 CHAR ),
    JACK_AND_HANDLE_STATUS         VARCHAR2(200 CHAR ),
    JACK_AND_HANDLE_REMARKS         VARCHAR2(200 CHAR ),
    WHEEL_WRENCH         VARCHAR2(200 CHAR ),
    WHEEL_WRENCH_STATUS         VARCHAR2(200 CHAR ),
    WHEEL_WRENCH_REMARKS         VARCHAR2(200 CHAR ),
    TOOL_KIT         VARCHAR2(200 CHAR ),
    TOOL_KIT_STATUS         VARCHAR2(200 CHAR ),
    TOOL_KIT_REMARKS         VARCHAR2(200 CHAR ),
    LIFE_SAVER         VARCHAR2(200 CHAR ),
    LIFE_SAVER_STATUS         VARCHAR2(200 CHAR ),
    LIFE_SAVER_REMARKS         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;




create table DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    GENERAR_INSPECTION NUMBER REFERENCES DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL(ID) ON DELETE CASCADE ,
    RADIATOR_WATER       VARCHAR2(200 char),
    RADIATOR_WATER_STATUS       VARCHAR2(200 char),
    RADIATOR_WATER_REMARKS       VARCHAR2(200 char),
    ENGINE_OIL VARCHAR2(200 char ) ,
    ENGINE_OIL_STATUS VARCHAR2(200 char ) ,
    ENGINE_OIL_REMARKS VARCHAR2(200 char ) ,
    STARTING_ENGINE      VARCHAR2(200 char ),
    STARTING_ENGINE_STATUS      VARCHAR2(200 char ),
    STARTING_ENGINE_REMARKS      VARCHAR2(200 char ),
    IDLE_SPEED_NOISES         VARCHAR2(200 CHAR ),
    IDLE_SPEED_NOISES_STATUS         VARCHAR2(200 CHAR ),
    IDLE_SPEED_NOISES_REMARKS         VARCHAR2(200 CHAR ),
    HIGH_SPEED_NOISES         VARCHAR2(200 CHAR ),
    HIGH_SPEED_NOISES_STATUS         VARCHAR2(200 CHAR ),
    HIGH_SPEED_NOISES_REMARKS         VARCHAR2(200 CHAR ),
    OIL_LEAKS         VARCHAR2(200 CHAR ),
    OIL_LEAKS_STATUS         VARCHAR2(200 CHAR ),
    OIL_LEAKS_REMARKS         VARCHAR2(200 CHAR ),
    WATER_LEAKS         VARCHAR2(200 CHAR ),
    WATER_LEAKS_STATUS         VARCHAR2(200 CHAR ),
    WATER_LEAKS_REMARKS         VARCHAR2(200 CHAR ),
    COOLING_SYSTEM_FUNCTION         VARCHAR2(200 CHAR ),
    COOLING_SYSTEM_FUNCTION_STATUS         VARCHAR2(200 CHAR ),
    COOLING_SYSTEM_FUNCTION_REMARKS         VARCHAR2(200 CHAR ),
    ENGINE_OIL_PRESSURE         VARCHAR2(200 CHAR ),
    ENGINE_OIL_PRESSURE_STATUS         VARCHAR2(200 CHAR ),
    ENGINE_OIL_PRESSURE_REMARKS         VARCHAR2(200 CHAR ),
    CHARGING_SYSTEM         VARCHAR2(200 CHAR ),
    CHARGING_SYSTEM_STATUS         VARCHAR2(200 CHAR ),
    CHARGING_SYSTEM_REMARKS         VARCHAR2(200 CHAR ),
    AC_OPERATION         VARCHAR2(200 CHAR ),
    AC_OPERATION_STATUS         VARCHAR2(200 CHAR ),
    AC_OPERATION_REMARKS         VARCHAR2(200 CHAR ),
    POWER_STEERING_OPERATION         VARCHAR2(200 CHAR ),
    POWER_STEERING_OPERATION_STATUS         VARCHAR2(200 CHAR ),
    POWER_STEERING_OPERATION_REMARKS         VARCHAR2(200 CHAR ),
    FUEL_PUMP         VARCHAR2(200 CHAR ),
    FUEL_PUMP_STATUS         VARCHAR2(200 CHAR ),
    FUEL_PUMP_REMARKS         VARCHAR2(200 CHAR ),
    VACUUM_PUMP         VARCHAR2(200 CHAR ),
    VACUUM_PUMP_STATUS         VARCHAR2(200 CHAR ),
    VACUUM_PUMP_REMARKS         VARCHAR2(200 CHAR ),
    ENGINE_STOPPER         VARCHAR2(200 CHAR ),
    ENGINE_STOPPER_STATUS         VARCHAR2(200 CHAR ),
    ENGINE_STOPPER_REMARKS         VARCHAR2(200 CHAR ),
    EXHAUST_EMISSION         VARCHAR2(200 CHAR ),
    EXHAUST_EMISSION_STATUS         VARCHAR2(200 CHAR ),
    EXHAUST_EMISSION_REMARKS         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create table DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    GENERAR_INSPECTION NUMBER REFERENCES DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL(ID) ON DELETE CASCADE ,
    LEAF_SPRINGS       VARCHAR2(200 char),
    LEAF_SPRINGS_STATUS       VARCHAR2(200 char),
    LEAF_SPRINGS_REMARKS       VARCHAR2(200 char),
    U_BOLTS VARCHAR2(200 char ) ,
    U_BOLTS_STATUS VARCHAR2(200 char ) ,
    U_BOLTS_REMARKS VARCHAR2(200 char ) ,
    SPRING_BUSHES      VARCHAR2(200 char ),
    SPRING_BUSHES_STATUS      VARCHAR2(200 char ),
    SPRING_BUSHES_REMARKS      VARCHAR2(200 char ),
    SPRING_PINS         VARCHAR2(200 CHAR ),
    SPRING_PINS_STATUS         VARCHAR2(200 CHAR ),
    SPRING_PINS_REMARKS         VARCHAR2(200 CHAR ),
    COIL_SPRINGS         VARCHAR2(200 CHAR ),
    COIL_SPRINGS_STATUS         VARCHAR2(200 CHAR ),
    COIL_SPRINGS_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_SHOCK_ABSORBERS         VARCHAR2(200 CHAR ),
    FRONT_SHOCK_ABSORBERS_STATUS         VARCHAR2(200 CHAR ),
    FRONT_SHOCK_ABSORBERS_REMARKS         VARCHAR2(200 CHAR ),
    REAR_SHOCK_ABSORBERS         VARCHAR2(200 CHAR ),
    REAR_SHOCK_ABSORBERS_STATUS         VARCHAR2(200 CHAR ),
    REAR_SHOCK_ABSORBERS_REMARKS         VARCHAR2(200 CHAR ),
    SUB_FRAME_MOUNTINGS         VARCHAR2(200 CHAR ),
    SUB_FRAME_MOUNTINGS_STATUS         VARCHAR2(200 CHAR ),
    SUB_FRAME_MOUNTINGS_REMARKS         VARCHAR2(200 CHAR ),
    ENGINE_MOUNTINGS         VARCHAR2(200 CHAR ),
    ENGINE_MOUNTINGS_STATUS         VARCHAR2(200 CHAR ),
    ENGINE_MOUNTINGS_REMARKS         VARCHAR2(200 CHAR ),
    GEAR_BOX_MOUNTINGS         VARCHAR2(200 CHAR ),
    GEAR_BOX_MOUNTINGS_STATUS         VARCHAR2(200 CHAR ),
    GEAR_BOX_MOUNTINGS_REMARKS         VARCHAR2(200 CHAR ),
    STABILIZER_BUSHES         VARCHAR2(200 CHAR ),
    STABILIZER_BUSHES_STATUS         VARCHAR2(200 CHAR ),
    STABILIZER_BUSHES_REMARKS         VARCHAR2(200 CHAR ),
    FUEL_TANK         VARCHAR2(200 CHAR ),
    FUEL_TANK_STATUS         VARCHAR2(200 CHAR ),
    FUEL_TANK_REMARKS         VARCHAR2(200 CHAR ),
    FUEL_LINES         VARCHAR2(200 CHAR ),
    FUEL_LINES_STATUS         VARCHAR2(200 CHAR ),
    FUEL_LINES_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_LINES         VARCHAR2(200 CHAR ),
    BRAKE_LINES_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_LINES_REMARKS         VARCHAR2(200 CHAR ),
    EXHAUST_SYSTEM         VARCHAR2(200 CHAR ),
    EXHAUST_SYSTEM_STATUS         VARCHAR2(200 CHAR ),
    EXHAUST_SYSTEM_REMARKS         VARCHAR2(200 CHAR ),
    TRANSMISSION         VARCHAR2(200 CHAR ),
    TRANSMISSION_STATUS         VARCHAR2(200 CHAR ),
    TRANSMISSION_REMARKS         VARCHAR2(200 CHAR ),
    STEERING_BOX         VARCHAR2(200 CHAR ),
    STEERING_BOX_STATUS     VARCHAR2(200 CHAR ),
    STEERING_BOX_REMARKS     VARCHAR2(200 CHAR ),
    CHASSIS         VARCHAR2(200 CHAR ),
    CHASSIS_STATUS         VARCHAR2(200 CHAR ),
    CHASSIS_REMARKS         VARCHAR2(200 CHAR ),
    MONO_BLOCK_BODY         VARCHAR2(200 CHAR ),
    MONO_BLOCK_BODY_STATUS         VARCHAR2(200 CHAR ),
    MONO_BLOCK_BODY_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_SUB_FRAME         VARCHAR2(200 CHAR ),
    FRONT_SUB_FRAME_STATUS         VARCHAR2(200 CHAR ),
    FRONT_SUB_FRAME_REMARKS         VARCHAR2(200 CHAR ),
    REAR_SUB_FRAME         VARCHAR2(200 CHAR ),
    REAR_SUB_FRAME_STATUS         VARCHAR2(200 CHAR ),
    REAR_SUB_FRAME_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_AXLE         VARCHAR2(200 CHAR ),
    FRONT_AXLE_STATUS         VARCHAR2(200 CHAR ),
    FRONT_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    REAR_AXLE         VARCHAR2(200 CHAR ),
    REAR_AXLE_STATUS         VARCHAR2(200 CHAR ),
    REAR_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    SECOND_REAR_AXLE         VARCHAR2(200 CHAR ),
    SECOND_REAR_AXLE_STATUS         VARCHAR2(200 CHAR ),
    SECOND_REAR_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    WHEEL_GEOMETRY         VARCHAR2(200 CHAR ),
    WHEEL_GEOMETRY_STATUS         VARCHAR2(200 CHAR ),
    WHEEL_GEOMETRY_REMARKS         VARCHAR2(200 CHAR ),
    KING_PIN_BUSHES         VARCHAR2(200 CHAR ),
    KING_PIN_BUSHES_STATUS         VARCHAR2(200 CHAR ),
    KING_PIN_BUSHES_REMARKS         VARCHAR2(200 CHAR ),
    BALL_JOINTS         VARCHAR2(200 CHAR ),
    BALL_JOINTS_STATUS         VARCHAR2(200 CHAR ),
    BALL_JOINTS_REMARKS         VARCHAR2(200 CHAR ),
    STEERING_SYSTEM         VARCHAR2(200 CHAR ),
    STEERING_SYSTEM_STATUS         VARCHAR2(200 CHAR ),
    STEERING_SYSTEM_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_DISC         VARCHAR2(200 CHAR ),
    BRAKE_DISC_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_DISC_REMARKS         VARCHAR2(200 CHAR ),
    CALIPERS         VARCHAR2(200 CHAR ),
    CALIPERS_STATUS         VARCHAR2(200 CHAR ),
    CALIPERS_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_PAD_LIFE         VARCHAR2(200 CHAR ),
    BRAKE_PAD_LIFE_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_PAD_LIFE_REMARKS         VARCHAR2(200 CHAR ),
    BRAKE_DRUMS         VARCHAR2(200 CHAR ),
    BRAKE_DRUMS_STATUS         VARCHAR2(200 CHAR ),
    BRAKE_DRUMS_REMARKS         VARCHAR2(200 CHAR ),
    WHEEL_CYLINDERS         VARCHAR2(200 CHAR ),
    WHEEL_CYLINDERS_STATUS         VARCHAR2(200 CHAR ),
    WHEEL_CYLINDERS_REMARKS         VARCHAR2(200 CHAR ),
    REAR_OIL_SEALS         VARCHAR2(200 CHAR ),
    REAR_OIL_SEALS_STATUS         VARCHAR2(200 CHAR ),
    REAR_OIL_SEALS_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_LH_DRIVING_SHAFT         VARCHAR2(200 CHAR ),
    FRONT_LH_DRIVING_SHAFT_STATUS         VARCHAR2(200 CHAR ),
    FRONT_LH_DRIVING_SHAFT_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_RF_DRIVING_SHAFT         VARCHAR2(200 CHAR ),
    FRONT_RF_DRIVING_SHAFT_STATUS         VARCHAR2(200 CHAR ),
    FRONT_RF_DRIVING_SHAFT_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_DIFFERENTIAL         VARCHAR2(200 CHAR ),
    FRONT_DIFFERENTIAL_STATUS         VARCHAR2(200 CHAR ),
    FRONT_DIFFERENTIAL_REMARKS         VARCHAR2(200 CHAR ),
    REAR_DIFFERENTIAL         VARCHAR2(200 CHAR ),
    REAR_DIFFERENTIAL_STATUS         VARCHAR2(200 CHAR ),
    REAR_DIFFERENTIAL_REMARKS         VARCHAR2(200 CHAR ),
    TRANSFER_CASE         VARCHAR2(200 CHAR ),
    TRANSFER_CASE_STATUS         VARCHAR2(200 CHAR ),
    TRANSFER_CASE_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_PROPELLER_SHAFT         VARCHAR2(200 CHAR ),
    FRONT_PROPELLER_SHAFT_STATUS         VARCHAR2(200 CHAR ),
    FRONT_PROPELLER_SHAFT_REMARKS         VARCHAR2(200 CHAR ),
    CENTER_BEARING         VARCHAR2(200 CHAR ),
    CENTER_BEARING_STATUS         VARCHAR2(200 CHAR ),
    CENTER_BEARING_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_REAR_OILS_SEALS         VARCHAR2(200 CHAR ),
    FRONT_REAR_OILS_SEALS_STATUS         VARCHAR2(200 CHAR ),
    FRONT_REAR_OILS_SEALS_REMARKS         VARCHAR2(200 CHAR ),
    REAR_LH_DRIVE_SHAFT         VARCHAR2(200 CHAR ),
    REAR_LH_DRIVE_SHAFT_STATUS         VARCHAR2(200 CHAR ),
    REAR_LH_DRIVE_SHAFT_REMARKS         VARCHAR2(200 CHAR ),
    REAR_RH_DRIVE_SHAFT         VARCHAR2(200 CHAR ),
    REAR_RH_DRIVE_SHAFT_STATUS         VARCHAR2(200 CHAR ),
    REAR_RH_DRIVE_SHAFT_REMARKS         VARCHAR2(200 CHAR ),
    HAND_BRAKE_CABLES         VARCHAR2(200 CHAR ),
    HAND_BRAKE_CABLES_STATUS         VARCHAR2(200 CHAR ),
    HAND_BRAKE_CABLES_REMARKS         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;




create table DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE
(
    ID             NUMBER  not null primary key,
    STATUS         NUMBER(2)                   default 0,
    GENERAR_INSPECTION NUMBER REFERENCES DAT_KEBS_MMINISTRY_INSPECTION_GENERRAL(ID) ON DELETE CASCADE ,
    DRIVING_SEAT_ADJUSTMENTS       VARCHAR2(200 char),
    DRIVING_SEAT_ADJUSTMENTS_STATUS       VARCHAR2(200 char),
    DRIVING_SEAT_ADJUSTMENTS_REMARKS       VARCHAR2(200 char),
    DRIVING_PERFORMANCES       VARCHAR2(200 char),
    DRIVING_PERFORMANCES_STATUS       VARCHAR2(200 char),
    DRIVING_PERFORMANCES_REMARKS       VARCHAR2(200 char),
    EMERGENCY_BRAKE       VARCHAR2(200 char),
    EMERGENCY_BRAKE_STATUS       VARCHAR2(200 char),
    EMERGENCY_BRAKE_REMARKS       VARCHAR2(200 char),
    CLUTCH_PERFORMANCE VARCHAR2(200 char ) ,
    CLUTCH_PERFORMANCE_STATUS VARCHAR2(200 char ) ,
    CLUTCH_PERFORMANCE_REMARKS VARCHAR2(200 char ) ,
    GEAR_SHIFT VARCHAR2(200 char ) ,
    GEAR_SHIFT_STATUS VARCHAR2(200 char ) ,
    GEAR_SHIFT_REMARKS VARCHAR2(200 char ) ,
    STEERING_STABILITY VARCHAR2(200 char ) ,
    STEERING_STABILITY_STATUS VARCHAR2(200 char ) ,
    STEERING_STABILITY_REMARKS VARCHAR2(200 char ) ,
    FRONT_SUSPENSION      VARCHAR2(200 char ),
    FRONT_SUSPENSION_STATUS      VARCHAR2(200 char ),
    FRONT_SUSPENSION_REMARKS      VARCHAR2(200 char ),
    REAR_SUSPENSION      VARCHAR2(200 char ),
    REAR_SUSPENSION_STATUS      VARCHAR2(200 char ),
    REAR_SUSPENSION_REMAKRS      VARCHAR2(200 char ),
    GAUGES_AND_INSTRUMENTS      VARCHAR2(200 char ),
    GAUGES_AND_INSTRUMENTS_STATUS      VARCHAR2(200 char ),
    GAUGES_AND_INSTRUMENTS_REMARKS      VARCHAR2(200 char ),
    ODOMETER         VARCHAR2(200 CHAR ),
    ODOMETER_STATUS         VARCHAR2(200 CHAR ),
    ODOMETER_REMARKS         VARCHAR2(200 CHAR ),
    HEATER         VARCHAR2(200 CHAR ),
    HEATER_STATUS         VARCHAR2(200 CHAR ),
    HEATER_REMARKS         VARCHAR2(200 CHAR ),
    DEFROSTER         VARCHAR2(200 CHAR ),
    DEFROSTER_STATUS         VARCHAR2(200 CHAR ),
    DEFROSTER_REMARKS         VARCHAR2(200 CHAR ),
    AIR_CON         VARCHAR2(200 CHAR ),
    AIR_CON_STATUS         VARCHAR2(200 CHAR ),
    AIR_CON_REMAKRS         VARCHAR2(200 CHAR ),
    WINDSCREEN_WIPERS         VARCHAR2(200 CHAR ),
    WINDSCREEN_WIPERS_STATUS         VARCHAR2(200 CHAR ),
    WINDSCREEN_WIPERS_REMARKS         VARCHAR2(200 CHAR ),
    WASHERS         VARCHAR2(200 CHAR ),
    WASHERS_STATUS         VARCHAR2(200 CHAR ),
    WASHERS_REMARKS         VARCHAR2(200 CHAR ),
    HORN         VARCHAR2(200 CHAR ),
    HORN_STATUS         VARCHAR2(200 CHAR ),
    HORN_REMARKS         VARCHAR2(200 CHAR ),
    WHEEL_ALIGNMENT         VARCHAR2(200 CHAR ),
    WHEEL_ALIGNMENT_STATUS         VARCHAR2(200 CHAR ),
    WHEEL_ALIGNMENT_REMARKS         VARCHAR2(200 CHAR ),
    PARKING_BRAKE         VARCHAR2(200 CHAR ),
    PARKING_BRAKE_STATUS         VARCHAR2(200 CHAR ),
    PARKING_BRAKE_REMARKS         VARCHAR2(200 CHAR ),
    FRONT_TYRES         VARCHAR2(200 CHAR ),
    FRONT_TYRES_STATUS         VARCHAR2(200 CHAR ),
    FRONT_TYRES_REMARKS         VARCHAR2(200 CHAR ),
    REAR_TYRES_1ST_AXLE         VARCHAR2(200 CHAR ),
    REAR_TYRES_1ST_AXLE_STATUS         VARCHAR2(200 CHAR ),
    REAR_TYRES_1ST_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    REAR_TYRES_2ND_AXLE         VARCHAR2(200 CHAR ),
    REAR_TYRES_2ND_AXLE_STATUS         VARCHAR2(200 CHAR ),
    REAR_TYRES_2ND_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    REAR_TYRES_3RD_AXLE         VARCHAR2(200 CHAR ),
    REAR_TYRES_3RD_AXLE_STATUS         VARCHAR2(200 CHAR ),
    REAR_TYRES_3RD_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    REAR_TYRES_4TH_AXLE         VARCHAR2(200 CHAR ),
    REAR_TYRES_4TH_AXLE_STATUS         VARCHAR2(200 CHAR ),
    REAR_TYRES_4TH_AXLE_REMARKS         VARCHAR2(200 CHAR ),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE_seq.nextval
from dual;

create trigger DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE_trg
    before
        insert
    on DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;










 create table DAT_KEBS_PERMIT_APPLICATION_REMARKS
(
    ID             NUMBER                                      not null
        primary key,
    STATUS         NUMBER(2)                   default 0,
    PERMITID       NUMBER,
    MANUFACTURERID NUMBER,
    HOFID          NUMBER,
    REMARK         VARCHAR2(4000),
    VAR_FIELD_1    VARCHAR2(350 char),
    VAR_FIELD_2    VARCHAR2(350 char),
    VAR_FIELD_3    VARCHAR2(350 char),
    VAR_FIELD_4    VARCHAR2(350 char),
    VAR_FIELD_5    VARCHAR2(350 char),
    VAR_FIELD_6    VARCHAR2(350 char),
    VAR_FIELD_7    VARCHAR2(350 char),
    VAR_FIELD_8    VARCHAR2(350 char),
    VAR_FIELD_9    VARCHAR2(350 char),
    VAR_FIELD_10   VARCHAR2(350 char),
    CREATED_BY     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY      VARCHAR2(100 char)          default 'admin',
    DELETED_ON     TIMESTAMP(6) WITH TIME ZONE,
    SECTION        VARCHAR2(400)
)
    TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_PERMIT_APPLICATION_REMARKS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select DAT_KEBS_PERMIT_APPLICATION_REMARKS_SEQ.nextval
from dual;

create trigger DAT_KEBS_PERMIT_APPLICATION_REMARKS_TRG
    before
        insert
    on DAT_KEBS_PERMIT_APPLICATION_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_PERMIT_APPLICATION_REMARKS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create table CFG_SCHEME_OF_SUPERVISION
(
    ID                                             NUMBER                                      not null
        primary key,
    STATUS                                         NUMBER(2),
    REQUIREMENTS                                   VARCHAR2(4000),
    INTRODUCTION                                   VARCHAR2(4000),
    QUALITY_MANUAL                                 VARCHAR2(4000),
    STANDARDS_LEVY_PAYMENT                         VARCHAR2(4000),
    SAMPLES                                        VARCHAR2(4000),
    INSPECTION_AND_TESTING                         VARCHAR2(4000),
    CALIBRATION                                    VARCHAR2(4000),
    QUALITY_RECORDS                                VARCHAR2(4000),
    LABELLING                                      VARCHAR2(4000),
    STANDARDIZATION_MARK                           VARCHAR2(4000),
    CONDITIONS_FOR_USE_OF_THE_STANDARDIZATION_MARK VARCHAR2(4000),
    INDEMITY                                       VARCHAR2(4000),
    CHANGES_AFFECTING_CERTIFICATION                VARCHAR2(4000),
    TERMINATION_REDUCTION                          VARCHAR2(4000),
    OPERATION_PROCESS_AND_CONTROL                  VARCHAR2(4000),
    AGREEMENT_TO_COMPLY_WITH_SCHEME_OF_SUPERVISION VARCHAR2(4000),
    VAR_FIELD_1                                    VARCHAR2(350 char),
    VAR_FIELD_2                                    VARCHAR2(350 char),
    VAR_FIELD_3                                    VARCHAR2(350 char),
    VAR_FIELD_4                                    VARCHAR2(350 char),
    VAR_FIELD_5                                    VARCHAR2(350 char),
    VAR_FIELD_6                                    VARCHAR2(350 char),
    VAR_FIELD_7                                    VARCHAR2(350 char),
    VAR_FIELD_8                                    VARCHAR2(350 char),
    VAR_FIELD_9                                    VARCHAR2(350 char),
    VAR_FIELD_10                                   VARCHAR2(350 char),
    CREATED_BY                                     VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON                                     TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY                                    VARCHAR2(100 char)          default 'admin',
    MODIFIED_ON                                    TIMESTAMP(6) WITH TIME ZONE default sysdate,
    DELETE_BY                                      VARCHAR2(100 char)          default 'admin',
    DELETED_ON                                     TIMESTAMP(6) WITH TIME ZONE
)
TABLESPACE qaimssdb_data;

create sequence CFG_SCHEME_OF_SUPERVISION_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select CFG_SCHEME_OF_SUPERVISION_SEQ.nextval
from dual;

create trigger CFG_SCHEME_OF_SUPERVISION_TRG
    before
        insert
    on CFG_SCHEME_OF_SUPERVISION
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SCHEME_OF_SUPERVISION_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create TABLE DAT_KEBS_MINISTRY_INSPECTION_MOTOR
(
    id                NUMBER       NOT NULL PRIMARY KEY,


    OVERALL_APPEARANCE VARCHAR2(200 CHAR),
    OVERALL_APPEARANCE_STATUS VARCHAR2(200 CHAR),
    OVERALL_APPEARANCE_REMARKS VARCHAR2(200 CHAR),
    CONDITION_OF_PAINT VARCHAR2(200 CHAR),
    CONDITION_OF_PAINT_STATUS VARCHAR2(200 CHAR),
    CONDITION_OF_PAINT_REMARKS VARCHAR2(200 CHAR),
    DOORS VARCHAR2(200 CHAR),
    DOORS_STATUS VARCHAR2(200 CHAR),
    DOORS_REMARKS VARCHAR2(200 CHAR),
    WINDOWS VARCHAR2(200 CHAR),
    WINDOWS_STATUS VARCHAR2(200 CHAR),
    WINDOWS_REMARKS VARCHAR2(200 CHAR),
    SUNROOF VARCHAR2(200 CHAR),
    SUNROOF_REMARKS VARCHAR2(200 CHAR),
    SUNROOF_STATUS VARCHAR2(200 CHAR),
    EXTERNAL_MIRRORS VARCHAR2(200 CHAR),
    EXTERNAL_MIRRORS_STATUS VARCHAR2(200 CHAR),
    EXTERNAL_MIRRORS_REMARKS VARCHAR2(200 CHAR),
    GLASSES VARCHAR2(200 CHAR),
    GLASSES_STATUS VARCHAR2(200 CHAR),
    GLASSES_REMARKS VARCHAR2(200 CHAR),
    WIPERS_WASHERS VARCHAR2(200 CHAR),
    WIPERS_WASHERS_STATUS VARCHAR2(200 CHAR),
    WIPERS_WASHERS_REMARKS VARCHAR2(200 CHAR),
    SEATS VARCHAR2(200 CHAR),
    SEATS_STATUS VARCHAR2(200 CHAR),
    SEATS_REMARKS VARCHAR2(200 CHAR),
    TRIM_MOULDING VARCHAR2(200 CHAR),
    TRIM_MOULDING_STATUS VARCHAR2(200 CHAR),
    TRIM_MOULDING_REMARKS VARCHAR2(200 CHAR),
    SAFETY_BELTS VARCHAR2(200 CHAR),
    SAFETY_BELTS_STATUS VARCHAR2(200 CHAR),
    SAFETY_BELTS_REMARKS VARCHAR2(200 CHAR),
    STEERING_WHEEL VARCHAR2(200 CHAR),
    STEERING_WHEEL_STATUS VARCHAR2(200 CHAR),
    STEERING_WHEEL_REMARKS VARCHAR2(200 CHAR),
    BRAKE_PEDAL VARCHAR2(200 CHAR),
    BRAKE_PEDAL_STATUS VARCHAR2(200 CHAR),
    BRAKE_PEDAL_REMARKS VARCHAR2(200 CHAR),
    CLUTCH_PEDAL VARCHAR2(200 CHAR),
    CLUTCH_PEDAL_STATUS VARCHAR2(200 CHAR),
    CLUTCH_PEDAL_REMARKS VARCHAR2(200 CHAR),
    PARKING_BRAKE_LEVER VARCHAR2(200 CHAR),
    PARKING_BRAKE_LEVER_STATUS VARCHAR2(200 CHAR),
    PARKING_BRAKE_LEVER_REMARKS VARCHAR2(200 CHAR),
    HEADLIGHTS VARCHAR2(200 CHAR),
    HEADLIGHTS_STATUS VARCHAR2(200 CHAR),
    HEADLIGHTS_REMARKS VARCHAR2(200 CHAR),
    PARKING_LIGHTS VARCHAR2(200 CHAR),
    PARKING_LIGHTS_STATUS VARCHAR2(200 CHAR),
    PARKING_LIGHTS_REMARKS VARCHAR2(200 CHAR),
    DIRECTION_INDICATORS VARCHAR2(200 CHAR),
    DIRECTION_INDICATORS_STATUS VARCHAR2(200 CHAR),
    DIRECTION_INDICATORS_REMARKS VARCHAR2(200 CHAR),
    REVERSING_LIGHT VARCHAR2(200 CHAR),
    REVERSING_LIGHT_STATUS VARCHAR2(200 CHAR),
    REVERSING_LIGHT_REMARKS VARCHAR2(200 CHAR),
    COURTESY_LIGHT VARCHAR2(200 CHAR),
    COURTESY_LIGHT_STATUS VARCHAR2(200 CHAR),
    COURTESY_LIGHT_REMARKS VARCHAR2(200 CHAR),
    REAR_NO_PLATE_LIGHT VARCHAR2(200 CHAR),
    REAR_NO_PLATE_LIGHT_STATUS VARCHAR2(200 CHAR),
    REAR_NO_PLATE_LIGHT_REMARKS VARCHAR2(200 CHAR),
    STOP_LIGHTS VARCHAR2(200 CHAR),
    STOP_LIGHTS_STATUS VARCHAR2(200 CHAR),
    STOP_LIGHTS_REMARKS VARCHAR2(200 CHAR),
    FRONT_BUMBER VARCHAR2(200 CHAR),
    FRONT_BUMBER_STATUS VARCHAR2(200 CHAR),
    FRONT_BUMBER_REMARKS VARCHAR2(200 CHAR),
    ROOF_RACK VARCHAR2(200 CHAR),
    ROOF_RACK_STATUS VARCHAR2(200 CHAR),
    ROOF_RACK_REMARKS VARCHAR2(200 CHAR),
    ANTENNA VARCHAR2(200 CHAR),
    ANTENNA_STATUS VARCHAR2(200 CHAR),
    ANTENNA_REMARKS VARCHAR2(200 CHAR),
    BONNET VARCHAR2(200 CHAR),
    BONNET_STATUS VARCHAR2(200 CHAR),
    BONNET_REMARKS VARCHAR2(200 CHAR),
    ENGINE VARCHAR2(200 CHAR),
    ENGINE_STATUS VARCHAR2(200 CHAR),
    ENGINE_REMARKS VARCHAR2(200 CHAR),
    BATTERY VARCHAR2(200 CHAR),
    BATTERY_STATUS VARCHAR2(200 CHAR),
    BATTERY_REMARKS VARCHAR2(200 CHAR),
    BATTERY_CARRIER VARCHAR2(200 CHAR),
    BATTERY_CARRIER_STATUS VARCHAR2(200 CHAR),
    BATTERY_CARRIER_REMARKS VARCHAR2(200 CHAR),
    WIRING_HARNESS VARCHAR2(200 CHAR),
    WIRING_HARNESS_STATUS VARCHAR2(200 CHAR),
    WIRING_HARNESS_REMARKS VARCHAR2(200 CHAR),
    STARTER_MOTOR VARCHAR2(200 CHAR),
    STARTER_MOTOR_STATUS VARCHAR2(200 CHAR),
    STARTER_MOTOR_REMARKS VARCHAR2(200 CHAR),
    ALTERNATOR_GENERATOR VARCHAR2(200 CHAR),
    ALTERNATOR_GENERATOR_STATUS VARCHAR2(200 CHAR),
    ALTERNATOR_GENERATOR_REMARKS VARCHAR2(200 CHAR),
    RADIATOR VARCHAR2(200 CHAR),
    RADIATOR_STATUS VARCHAR2(200 CHAR),
    RADIATOR_REMARKS VARCHAR2(200 CHAR),
    RADIATOR_HOSES VARCHAR2(200 CHAR),
    RADIATOR_HOSES_STATUS VARCHAR2(200 CHAR),
    RADIATOR_HOSES_REMARKS VARCHAR2(200 CHAR),
    WATER_PUMP VARCHAR2(200 CHAR),
    WATER_PUMP_STATUS VARCHAR2(200 CHAR),
    WATER_PUMP_REMARKS VARCHAR2(200 CHAR),
    CARBURETOR VARCHAR2(200 CHAR),
    CARBURETOR_STATUS VARCHAR2(200 CHAR),
    CARBURETOR_STATUS VARCHAR2(200 CHAR),
    HIGH_TENSION_CABLES VARCHAR2(200 CHAR),
    HIGH_TENSION_CABLES_STATUS VARCHAR2(200 CHAR),
    HIGH_TENSION_CABLES_REMARKS VARCHAR2(200 CHAR),
    AC_CONDENSER VARCHAR2(200 CHAR),
    AC_CONDENSER_STATUS VARCHAR2(200 CHAR),
    AC_CONDENSER_REMARKS VARCHAR2(200 CHAR),
    POWER_STEERING VARCHAR2(200 CHAR),
    POWER_STEERING_STATUS VARCHAR2(200 CHAR),
    POWER_STEERING_REMARKS VARCHAR2(200 CHAR),
    BRAKE_MASTER_CYLINDER VARCHAR2(200 CHAR),
    BRAKE_MASTER_CYLINDER_STATUS VARCHAR2(200 CHAR),
    BRAKE_MASTER_CYLINDER_REMARKS VARCHAR2(200 CHAR),
    CLUTCH_MASTER_CYLIDER VARCHAR2(200 CHAR),
    CLUTCH_MASTER_CYLIDER_STATUS VARCHAR2(200 CHAR),
    CLUTCH_MASTER_CYLIDER_REMARKS VARCHAR2(200 CHAR),
    BRAKE_SYSTEM VARCHAR2(200 CHAR),
    BRAKE_SYSTEM_STATUS VARCHAR2(200 CHAR),
    BRAKE_SYSTEM_REMARKS VARCHAR2(200 CHAR),
    FUEL_PIPES VARCHAR2(200 CHAR),
    FUEL_PIPES_STATUS VARCHAR2(200 CHAR),
    FUEL_PIPES_REMARKS VARCHAR2(200 CHAR),
    FLEXIBLE_BRAKE_PIPES VARCHAR2(200 CHAR),
    FLEXIBLE_BRAKE_PIPES_STATUS VARCHAR2(200 CHAR),
    FLEXIBLE_BRAKE_PIPES_REMARKS VARCHAR2(200 CHAR),
    WINDSCREEN_WASHER_BOTTLE VARCHAR2(200 CHAR),
    WINDSCREEN_WASHER_BOTTLE_STATUS VARCHAR2(200 CHAR),
    WINDSCREEN_WASHER_BOTTLE_REMARKS VARCHAR2(200 CHAR),
    BOOT_LID VARCHAR2(200 CHAR),
    BOOT_LID_STATUS VARCHAR2(200 CHAR),
    BOOT_LID_REMARKS VARCHAR2(200 CHAR),
    LIFE_SAVER VARCHAR2(200 CHAR),
    LIFE_SAVER_STATUS VARCHAR2(200 CHAR),
    LIFE_SAVER_REMARKS VARCHAR2(200 CHAR),
    RADIOTOR_WATER VARCHAR2(200 CHAR),
    RADIOTOR_WATER_STATUS VARCHAR2(200 CHAR),
    RADIOTOR_WATER_REMARKS VARCHAR2(200 CHAR),
    ENGINE_OIL VARCHAR2(200 CHAR),
    ENGINE_OIL_STATUS VARCHAR2(200 CHAR),
    ENGINE_OIL_REMARKS VARCHAR2(200 CHAR),
    STARTING_ENGINE VARCHAR2(200 CHAR),
    STARTING_ENGINE_STATUS VARCHAR2(200 CHAR),
    STARTING_ENGINE_REMARKS VARCHAR2(200 CHAR),
    IDELE_SPEED_NOISES VARCHAR2(200 CHAR),
    IDELE_SPEED_NOISES_STATUS VARCHAR2(200 CHAR),
    IDELE_SPEED_NOISES_REMARKS VARCHAR2(200 CHAR),
    HIGH_SPEED_NOISES VARCHAR2(200 CHAR),
    HIGH_SPEED_NOISES_STATUS VARCHAR2(200 CHAR),
    HIGH_SPEED_NOISES_REMARKS VARCHAR2(200 CHAR),
    OIL_LEAKS VARCHAR2(200 CHAR),
    OIL_LEAKS_STATUS VARCHAR2(200 CHAR),
    OIL_LEAKS_REMARKS VARCHAR2(200 CHAR),
    WATER_LEAKS VARCHAR2(200 CHAR),
    WATER_LEAKS_STATUS VARCHAR2(200 CHAR),
    WATER_LEAKS_REMARKS VARCHAR2(200 CHAR),
    COOLING_SYSTEM_FUNCTION VARCHAR2(200 CHAR),
    COOLING_SYSTEM_FUNCTION_STATUA VARCHAR2(200 CHAR),
    COOLING_SYSTEM_FUNCTION_REMARKS VARCHAR2(200 CHAR),
    ENGINE_OIL_PRESSURE VARCHAR2(200 CHAR),
    ENGINE_OIL_PRESSURE_STATUS VARCHAR2(200 CHAR),
    ENGINE_OIL_PRESSURE_REMARKS VARCHAR2(200 CHAR),
    CHARGING_SYSTEM VARCHAR2(200 CHAR),
    CHARGING_SYSTEM_STATUS VARCHAR2(200 CHAR),
    CHARGING_SYSTEM_REMARKS VARCHAR2(200 CHAR),
    AC_OPERATION VARCHAR2(200 CHAR),
    AC_OPERATION_STATUS VARCHAR2(200 CHAR),
    AC_OPERATION_REMARKS VARCHAR2(200 CHAR),
    POWER_STEERING_OPERATION VARCHAR2(200 CHAR),
    POWER_STEERING_OPERATION_STATUS VARCHAR2(200 CHAR),
    POWER_STEERING_OPERATION_REMARKS VARCHAR2(200 CHAR),
    FUEL_PUMP VARCHAR2(200 CHAR),
    FUEL_PUMP_STATUS VARCHAR2(200 CHAR),
    FUEL_PUMP_REMARKS VARCHAR2(200 CHAR),
    VACUUM_PUMP VARCHAR2(200 CHAR),
    VACUUM_PUMP_STATUS VARCHAR2(200 CHAR),
    VACUUM_PUMP_REMARKS VARCHAR2(200 CHAR),
    ENGINE_STOPPER VARCHAR2(200 CHAR),
    ENGINE_STOPPER_STATUS VARCHAR2(200 CHAR),
    ENGINE_STOPPER_REMARKS VARCHAR2(200 CHAR),
    EXHAUST_EMISSION VARCHAR2(200 CHAR),
    EXHAUST_EMISSION_STATUS VARCHAR2(200 CHAR),
    EXHAUST_EMISSION_REMARKS VARCHAR2(200 CHAR),
    LEAF_SPRINGS VARCHAR2(200 CHAR),
    LEAF_SPRINGS_STATUS VARCHAR2(200 CHAR),
    LEAF_SPRINGS_REMARKS VARCHAR2(200 CHAR),
    U_BOLTS VARCHAR2(200 CHAR),
    U_BOLTS_STATUS VARCHAR2(200 CHAR),
    U_BOLTS_REMARKS VARCHAR2(200 CHAR),
    SPRING_BUSHES VARCHAR2(200 CHAR),
    SPRING_BUSHES_STATUS VARCHAR2(200 CHAR),
    SPRING_BUSHES_REMARKS VARCHAR2(200 CHAR),
    SPRING_PINS VARCHAR2(200 CHAR),
    SPRING_PINS_STATUS VARCHAR2(200 CHAR),
    SPRING_PINS_REMARKS VARCHAR2(200 CHAR),
    COIL_SPRINGS VARCHAR2(200 CHAR),
    COIL_SPRINGS_STAUS VARCHAR2(200 CHAR),
    COIL_SPRINGS_REMARKS VARCHAR2(200 CHAR),
    FRONT_SHOCK_ABSORBERS VARCHAR2(200 CHAR),
    FRONT_SHOCK_ABSORBERS_STATUS VARCHAR2(200 CHAR),
    FRONT_SHOCK_ABSORBERS_REMARKS VARCHAR2(200 CHAR),
    REAR_SHOCK_ABSORBERS VARCHAR2(200 CHAR),
    REAR_SHOCK_ABSORBERS_STATUS VARCHAR2(200 CHAR),
    REAR_SHOCK_ABSORBERS_REMARKS VARCHAR2(200 CHAR),
    SUBFRAME_MOUNTINGS VARCHAR2(200 CHAR),
    SUBFRAME_MOUNTINGS_STATUS VARCHAR2(200 CHAR),
    SUBFRAME_MOUNTINGS_REMARKS VARCHAR2(200 CHAR),
    ENGINE_MOUNTINGS VARCHAR2(200 CHAR),
    ENGINE_MOUNTINGS_STATUS VARCHAR2(200 CHAR),
    ENGINE_MOUNTINGS_REMARKS VARCHAR2(200 CHAR),
    GEAR_BOX_MOUNTINGS VARCHAR2(200 CHAR),
    GEAR_BOX_MOUNTINGS_STATUS VARCHAR2(200 CHAR),
    GEAR_BOX_MOUNTINGS_REMARKS VARCHAR2(200 CHAR),
    STABILIZER_BUSHES VARCHAR2(200 CHAR),
    STABILIZER_BUSHES_STATUS VARCHAR2(200 CHAR),
    STABILIZER_BUSHES_REMARKS VARCHAR2(200 CHAR),
    FUEL_TANK VARCHAR2(200 CHAR),
    FUEL_TANK_STATUS VARCHAR2(200 CHAR),
    FUEL_TANK_REMARKS VARCHAR2(200 CHAR),
    FUEL_LINES VARCHAR2(200 CHAR),
    FUEL_LINES_STATUS VARCHAR2(200 CHAR),
    FUEL_LINES_REMARKS VARCHAR2(200 CHAR),
    BRAKE_LINES VARCHAR2(200 CHAR),
    BRAKE_LINES_STATUS VARCHAR2(200 CHAR),
    BRAKE_LINES_REMARKS VARCHAR2(200 CHAR),
    EXHAUST_SYSTEM VARCHAR2(200 CHAR),
    EXHAUST_SYSTEM_STATUS VARCHAR2(200 CHAR),
    EXHAUST_SYSTEM_REMARKS VARCHAR2(200 CHAR),
    TRANSMISSION VARCHAR2(200 CHAR),
    TRANSMISSION_STATUS VARCHAR2(200 CHAR),
    TRANSMISSION_REMARKS VARCHAR2(200 CHAR),
    STEERING_BOX VARCHAR2(200 CHAR),
    STEERING_BOX_STATUS VARCHAR2(200 CHAR),
    STEERING_BOX_REMARKS VARCHAR2(200 CHAR),
    CHASSIS VARCHAR2(200 CHAR),
    CHASSIS_STATUS VARCHAR2(200 CHAR),
    CHASSIS_REMARKS VARCHAR2(200 CHAR),
    MONOBLACK_BODY VARCHAR2(200 CHAR),
    MONOBLACK_BODY_STATUS VARCHAR2(200 CHAR),
    MONOBLACK_BODY_REMARKS VARCHAR2(200 CHAR),
    FRONT_SUB_FRAME VARCHAR2(200 CHAR),
    FRONT_SUB_FRAME_STATUS VARCHAR2(200 CHAR),
    FRONT_SUB_FRAME_REMARKS VARCHAR2(200 CHAR),
    REAR_SUB_FRAME VARCHAR2(200 CHAR),
    REAR_SUB_FRAME_STATUS VARCHAR2(200 CHAR),
    REAR_SUB_FRAME_REMARKS VARCHAR2(200 CHAR),
    FRONT_AXLE VARCHAR2(200 CHAR),
    FRONT_AXLE_STATUS VARCHAR2(200 CHAR),
    FRONT_AXLE_REMARKS VARCHAR2(200 CHAR),
    REAR_AXLE VARCHAR2(200 CHAR),
    REAR_AXLE_STATUS VARCHAR2(200 CHAR),
    REAR_AXLE_REMARKS VARCHAR2(200 CHAR),
    SECOND_REAR_AXLE VARCHAR2(200 CHAR),
    SECOND_REAR_AXLE_STATUS VARCHAR2(200 CHAR),
    SECOND_REAR_AXLE_REMARKS VARCHAR2(200 CHAR),
    WHEEL_GEOMETRY VARCHAR2(200 CHAR),
    WHEEL_GEOMETRY_STATUS VARCHAR2(200 CHAR),
    WHEEL_GEOMETRY_REMARKS VARCHAR2(200 CHAR),
    KINGPIN_BUSHES VARCHAR2(200 CHAR),
    KINGPIN_BUSHES_STATUS VARCHAR2(200 CHAR),
    KINGPIN_BUSHES_REMARKS VARCHAR2(200 CHAR),
    BALL_JOINTS VARCHAR2(200 CHAR),
    BALL_JOINTS_STATUS VARCHAR2(200 CHAR),
    BALL_JOINTS_REMARKS VARCHAR2(200 CHAR),
    STEERING_SYSTEM VARCHAR2(200 CHAR),
    STEERING_SYSTEM_STATUS VARCHAR2(200 CHAR),
    STEERING_SYSTEM_REMARKS VARCHAR2(200 CHAR),
    STEERING_SYSTEM_STATUS VARCHAR2(200 CHAR),
    STEERING_SYSTEM_REMARKS VARCHAR2(200 CHAR),
    BRAKE_DISC VARCHAR2(200 CHAR),
    BRAKE_DISC_STATUS VARCHAR2(200 CHAR),
    BRAKE_DISC_REMARKS VARCHAR2(200 CHAR),
    CALIPERS VARCHAR2(200 CHAR),
    CALIPERS_STATUS VARCHAR2(200 CHAR),
    CALIPERS_REMARKS VARCHAR2(200 CHAR),
    BRAKE_PAD_LIFE VARCHAR2(200 CHAR),
    BRAKE_PAD_LIFE_STATUS VARCHAR2(200 CHAR),
    BRAKE_PAD_LIFE_REMARKS VARCHAR2(200 CHAR),
    BRAKE_DRUMS VARCHAR2(200 CHAR),
    BRAKE_DRUMS_STATUS VARCHAR2(200 CHAR),
    BRAKE_DRUMS_REMARKS VARCHAR2(200 CHAR),
    WHEEL_CYLINDERS VARCHAR2(200 CHAR),
    WHEEL_CYLINDERS_STATUS VARCHAR2(200 CHAR),
    WHEEL_CYLINDERS_REMARKS VARCHAR2(200 CHAR),
    REAR_OIL_SEALS VARCHAR2(200 CHAR),
    REAR_OIL_SEALS_STATUS VARCHAR2(200 CHAR),
    REAR_OIL_SEALS_REMARKS VARCHAR2(200 CHAR),
    FRONT_LH_DRIVING_SHAFT VARCHAR2(200 CHAR),
    FRONT_LH_DRIVING_SHAFT_STATUS VARCHAR2(200 CHAR),
    FRONT_LH_DRIVING_SHAFT_REMARKS VARCHAR2(200 CHAR),
    FRONT_RH_DRIVING_SHAFT VARCHAR2(200 CHAR),
    FRONT_RH_DRIVING_SHAFT_STATUS VARCHAR2(200 CHAR),
    FRONT_RH_DRIVING_SHAFT_REMARKS VARCHAR2(200 CHAR),
    FRONT_DIFFERENCIAL VARCHAR2(200 CHAR),
    FRONT_DIFFERENCIAL_STATUS VARCHAR2(200 CHAR),
    FRONT_DIFFERENCIAL_REMARKS VARCHAR2(200 CHAR),
    REAR_DIFFERENCIAL VARCHAR2(200 CHAR),
    REAR_DIFFERENCIAL_STATUS VARCHAR2(200 CHAR),
    REAR_DIFFERENCIAL_REMARKS VARCHAR2(200 CHAR),
    TRANSFER_CASE VARCHAR2(200 CHAR),
    TRANSFER_CASE_STATUS VARCHAR2(200 CHAR),
    TRANSFER_CASE_REMARKS VARCHAR2(200 CHAR),
    FRONT_PROPELLER_SHAFT VARCHAR2(200 CHAR),
    FRONT_PROPELLER_SHAFT_STATUS VARCHAR2(200 CHAR),
    FRONT_PROPELLER_SHAFT_REMARKS VARCHAR2(200 CHAR),
    REAR_PROPELLER_SHAFT VARCHAR2(200 CHAR),
    REAR_PROPELLER_SHAFT_STATUS VARCHAR2(200 CHAR),
    REAR_PROPELLER_SHAFT_REMARKS VARCHAR2(200 CHAR),
    CENTRE_BEARING VARCHAR2(200 CHAR),
    CENTRE_BEARING_STATUS VARCHAR2(200 CHAR),
    CENTRE_BEARING_REMARKS VARCHAR2(200 CHAR),
    FRONT_REAR_OIL_SEALS VARCHAR2(200 CHAR),
    FRONT_REAR_OIL_SEALS_STATUS VARCHAR2(200 CHAR),
    FRONT_REAR_OIL_SEALS_REMARKS VARCHAR2(200 CHAR),
    REAR_LH_DRIVE_SHAFT VARCHAR2(200 CHAR),
    REAR_LH_DRIVE_SHAFT_STATUS VARCHAR2(200 CHAR),
    REAR_LH_DRIVE_SHAFT_REMARKS VARCHAR2(200 CHAR),
    REAR_RH_DRIVE_SHAFT VARCHAR2(200 CHAR),
    REAR_RH_DRIVE_SHAFT_STATUS VARCHAR2(200 CHAR),
    REAR_RH_DRIVE_SHAFT_REMARKS VARCHAR2(200 CHAR),
    HAND_BRAKE_CABLES VARCHAR2(200 CHAR),
    HAND_BRAKE_CABLES_STATUS VARCHAR2(200 CHAR),
    HAND_BRAKE_CABLES_REMARKS VARCHAR2(200 CHAR),
    DRIVING_SEAT_ADJUSTMENTS VARCHAR2(200 CHAR),
    DRIVING_SEAT_ADJUSTMENTS_STATUS VARCHAR2(200 CHAR),
    DRIVING_SEAT_ADJUSTMENTS_REMARKS VARCHAR2(200 CHAR),
    DRIVING_PERFORMANCES VARCHAR2(200 CHAR),
    DRIVING_PERFORMANCES_STATUS VARCHAR2(200 CHAR),
    DRIVING_PERFORMANCES_REMARKS VARCHAR2(200 CHAR),
    EMERGENCY_BRAKE VARCHAR2(200 CHAR),
    EMERGENCY_BRAKE_STATUS VARCHAR2(200 CHAR),
    EMERGENCY_BRAKE_REMARKS VARCHAR2(200 CHAR),
    GEAR_SHIFT VARCHAR2(200 CHAR),
    GEAR_SHIFT_STATUS VARCHAR2(200 CHAR),
    GEAR_SHIFT_REMARKS VARCHAR2(200 CHAR),
    STEERING_STABILITY VARCHAR2(200 CHAR),
    STEERING_STABILITY_STATUS VARCHAR2(200 CHAR),
    STEERING_STABILITY_REMARKS VARCHAR2(200 CHAR),
    FRONT_SUSPENSION VARCHAR2(200 CHAR),
    FRONT_SUSPENSION_STATUS VARCHAR2(200 CHAR),
    FRONT_SUSPENSION_REMARKS VARCHAR2(200 CHAR),
    REAR_SUSPENSION VARCHAR2(200 CHAR),
    REAR_SUSPENSION_STATUS VARCHAR2(200 CHAR),
    REAR_SUSPENSION_REMARKS VARCHAR2(200 CHAR),
    GAUGES_AND_INSTRUMENTS VARCHAR2(200 CHAR),
    GAUGES_AND_INSTRUMENTS_STATUS VARCHAR2(200 CHAR),
    GAUGES_AND_INSTRUMENTS_REMARKS VARCHAR2(200 CHAR),
    ODEMETER VARCHAR2(200 CHAR),
    ODEMETER_STATUS VARCHAR2(200 CHAR),
    ODEMETER_REMARKS VARCHAR2(200 CHAR),
    HEATER VARCHAR2(200 CHAR),
    HEATER_STATUS VARCHAR2(200 CHAR),
    HEATER_REMARKS VARCHAR2(200 CHAR),
    DEFROSTER VARCHAR2(200 CHAR),
    DEFROSTER_STATUS VARCHAR2(200 CHAR),
    DEFROSTER_REMARKS VARCHAR2(200 CHAR),
    AIRCON VARCHAR2(200 CHAR),
    AIRCON_STATUS VARCHAR2(200 CHAR),
    AIRCON_REMARKS VARCHAR2(200 CHAR),
    WINDSCREEN_WIPERS VARCHAR2(200 CHAR),
    WINDSCREEN_WIPERS_STATUS VARCHAR2(200 CHAR),
    WINDSCREEN_WIPERS_REMARKS VARCHAR2(200 CHAR),
    WASHERS VARCHAR2(200 CHAR),
    WASHERS_STATUS VARCHAR2(200 CHAR),
    WASHERS_REMARKS VARCHAR2(200 CHAR),
    HORN VARCHAR2(200 CHAR),
    HORN_STATUS VARCHAR2(200 CHAR),
    HORN_REMARKS VARCHAR2(200 CHAR),
    WHEEL_ALIGNMENT VARCHAR2(200 CHAR),
    WHEEL_ALIGNMENT_STATUS VARCHAR2(200 CHAR),
    WHEEL_ALIGNMENT_REMARKS VARCHAR2(200 CHAR),
    PARKING_BRAKE VARCHAR2(200 CHAR),
    PARKING_BRAKE_STATUS VARCHAR2(200 CHAR),
    PARKING_BRAKE_REMARKS VARCHAR2(200 CHAR),
    FRONT_TYRES VARCHAR2(200 CHAR),
    FRONT_TYRES_STATUS VARCHAR2(200 CHAR),
    FRONT_TYRES_REMARKS VARCHAR2(200 CHAR),
    REAR_TYRES_1ST_AXLE VARCHAR2(200 CHAR),
    REAR_TYRES_1ST_AXLE_STATUS VARCHAR2(200 CHAR),
    REAR_TYRES_1ST_AXLE_REMARKS VARCHAR2(200 CHAR),
    REAR_TYRES_2ST_AXLE VARCHAR2(200 CHAR),
    REAR_TYRES_2ST_AXLE_STATUS VARCHAR2(200 CHAR),
    REAR_TYRES_2ST_AXLE_REMARKS VARCHAR2(200 CHAR),
    REAR_TYRES_3ST_AXLE VARCHAR2(200 CHAR),
    REAR_TYRES_3ST_AXLE_STATUS VARCHAR2(200 CHAR),
    REAR_TYRES_3ST_AXLE_REMARKS VARCHAR2(200 CHAR),
    REAR_TYRES_4ST_AXLE VARCHAR2(200 CHAR),
    REAR_TYRES_4ST_AXLE_STATUS VARCHAR2(200 CHAR),
    REAR_TYRES_4ST_AXLE_REMARKS VARCHAR2(200 CHAR),
    INSPECTION_DATE DATE                        DEFAULT sysdate,
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
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                   NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                   NOT NULL ENABLE,
    modified_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on        TIMESTAMP(6) WITH TIME ZONE
)
    PARTITION BY RANGE (
                        INSPECTION_DATE
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence DAT_KEBS_MINISTRY_INSPECTION_MOTOR_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MINISTRY_INSPECTION_MOTOR_trg
    before
        insert
    on DAT_KEBS_MINISTRY_INSPECTION_MOTOR
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MINISTRY_INSPECTION_MOTOR_seq.nextval
      into :new.id
      from dual;

end if;

end if;
end;


 CREATE OR REPLACE PROCEDURE update_ncrs_status AS
    v_ncrs_status varchar2(200 char);
BEGIN
    update DAT_KEBS_CONSIGNMENT_DOCUMENT set CD_STATUS='REJECTED', APPROVAL_STATUS='REJECTED' where NCR_ID is not null ;
    commit;
END update_ncrs_status;

BEGIN
DBMS_SCHEDULER.CREATE_PROGRAM (
  program_name      => 'PROG_UPDATE_NCR_STATUS',
program_action     => 'REJECT_NCR_STATUS',
program_type      => 'STORED_PROCEDURE');
END;

BEGIN
    dbms_scheduler.enable('PROG_UPDATE_NCR_STATUS');
end;

BEGIN
DBMS_SCHEDULER.CREATE_SCHEDULE (
 schedule_name   => 'my_ncr_30min_schedule',
 start_date    => SYSTIMESTAMP,
 repeat_interval  => 'FREQ=MINUTELY; INTERVAL=30; BYDAY=SAT,SUN,MON,TUE,WED,THUR,FRI',
 end_date     => SYSTIMESTAMP + INTERVAL '30' day,
 comments     => 'Every 30 minutes');
END;


BEGIN
    DBMS_SCHEDULER.CREATE_JOB (
            job_name     => 'my_ncr_reject_job',
            program_name   => 'PROG_UPDATE_NCR_STATUS',
            schedule_name   => 'my_ncr_30min_schedule');
END;


drop table cfg_contact_types;

create TABLE cfg_contact_types
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    retries      NUMBER(2, 0)                DEFAULT 0
        NOT NULL ENABLE,
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
        NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
        NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_contact_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select cfg_contact_types_seq.nextval
from dual;

create trigger cfg_contact_types_trg
    before
        insert
    on cfg_contact_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_contact_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create TABLE cfg_user_types
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    retries      NUMBER(2, 0)                DEFAULT 0
        NOT NULL ENABLE,
    descriptions VARCHAR2(3800 CHAR),
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
        NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
        NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_user_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_types_trg
    before
        insert
    on cfg_user_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table dat_kebs_users cascade constraints;

create TABLE dat_kebs_users
(
    id                  NUMBER       NOT NULL
        PRIMARY KEY ENABLE,
    first_name          VARCHAR2(25 CHAR)
                                     NOT NULL ENABLE,
    last_name           VARCHAR2(25 CHAR)
                                     NOT NULL ENABLE,
    user_name           VARCHAR2(60 CHAR)
                                     NOT NULL ENABLE,
    email               VARCHAR2(60 CHAR)
                                     NOT NULL ENABLE,
    enabled             NUMBER(2, 0) NOT NULL,
    account_expired     NUMBER(2, 0) NOT NULL,
    account_locked      NUMBER(2, 0) NOT NULL,
    credentials_expired NUMBER(2, 0) NOT NULL,
    status              NUMBER
                                     NOT NULL ENABLE,
    registration_date   DATE                        DEFAULT sysdate
                                     NOT NULL ENABLE,
    approved_date       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                     NOT NULL ENABLE,
    user_type           NUMBER
        REFERENCES cfg_user_types (id)
            ON delete CASCADE,
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                     NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                     NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
)
    PARTITION BY RANGE (
                        registration_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

alter table qiamssadm.dat_kebs_users
    add credentials VARCHAR2(500 CHAR);

create sequence dat_kebs_users_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_users_trg
    before
        insert
    on dat_kebs_users
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_users_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from all_cons_columns
where table_name = 'CFG_CONTACT_TYPES'
  and column_name = 'ID';

create TABLE dat_user_contact_details
(
    id                NUMBER       NOT NULL PRIMARY KEY,
    user_id           NUMBER
        REFERENCES dat_kebs_users (id),
    contact_type      NUMBER
        REFERENCES cfg_contact_types (id),
    phone_number      VARCHAR2(20 CHAR),
    cell_phone_number VARCHAR2(20 CHAR),
    address           VARCHAR2(3000 CHAR),
    email             VARCHAR2(65 CHAR),
    version           NUMBER(8, 0) NOT NULL,
    status            NUMBER
                                   NOT NULL ENABLE,
    registration_date DATE                        DEFAULT sysdate,
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
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                   NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                   NOT NULL ENABLE,
    modified_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on        TIMESTAMP(6) WITH TIME ZONE
)
    PARTITION BY RANGE (
                        registration_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence dat_user_contact_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_user_contact_details_trg
    before
        insert
    on dat_user_contact_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_user_contact_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

-- select * from CFG_KEBS_ROLES;

-- select * from CFG_KEBS_PRIVILEGES;

-- drop table CFG_KEBS_PRIVILEGES cascade constraints ;

-- drop table CFG_KEBS_ROLES cascade constraints ;

create TABLE cfg_user_privileges
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    retries      NUMBER(2, 0)                DEFAULT 0
        NOT NULL ENABLE,
    descriptions VARCHAR2(3800 CHAR),
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
        NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
        NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

alter table cfg_user_privileges
    add name VARCHAR2(200 CHAR) DEFAULT 'priv_1' NOT NULL UNIQUE;

create sequence cfg_user_privileges_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_privileges_trg
    before
        insert
    on cfg_user_privileges
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_privileges_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create TABLE cfg_user_roles
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    role_name    VARCHAR2(100 CHAR) NOT NULL UNIQUE,
    retries      NUMBER(2, 0)                DEFAULT 0
                                    NOT NULL ENABLE,
    descriptions VARCHAR2(3800 CHAR),
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                    NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                    NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_user_roles_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_roles_trg
    before
        insert
    on cfg_user_roles
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_roles_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table cfg_user_roles_privileges;

create TABLE cfg_user_roles_privileges
(
    id           NUMBER PRIMARY KEY,
    roles        NUMBER
        REFERENCES cfg_user_roles (id),
    privilege    NUMBER
        REFERENCES cfg_user_privileges (id),
    status       NUMBER(2, 0),
    descriptions VARCHAR2(3800 CHAR),
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
        NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
        NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

alter table qiamssadm.cfg_user_roles_privileges
    add user_id NUMBER
        REFERENCES qiamssadm.dat_kebs_users (id);

create sequence cfg_user_roles_privileges_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_roles_privileges_trg
    before
        insert
    on cfg_user_roles_privileges
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_roles_privileges_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table cfg_kebs_business_lines cascade constraints;

create TABLE cfg_kebs_business_lines
(
    id           NUMBER PRIMARY KEY,
    name         VARCHAR2(100 CHAR) NOT NULL UNIQUE,
    status       NUMBER(2, 0),
    descriptions VARCHAR2(3800 CHAR),
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                    NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                    NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_kebs_business_lines_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_business_lines_trg
    before
        insert
    on cfg_kebs_business_lines
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_business_lines_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table cfg_kebs_business_nature cascade constraints;

create TABLE cfg_kebs_business_nature
(
    id                NUMBER PRIMARY KEY,
    business_lines_id NUMBER
        REFERENCES cfg_kebs_business_lines (id),
    name              VARCHAR2(100 CHAR) NOT NULL UNIQUE,
    status            NUMBER(2, 0),
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
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                         NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                         NOT NULL ENABLE,
    modified_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on        TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_kebs_business_nature_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_business_nature_trg
    before
        insert
    on cfg_kebs_business_nature
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_business_nature_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table dat_kebs_manufacturers cascade constraints;

create TABLE dat_kebs_manufacturers
(
    id                  NUMBER               NOT NULL PRIMARY KEY,
    name                VARCHAR2(200)
                                             NOT NULL ENABLE,
    kra_pin             VARCHAR2(20)
                                             NOT NULL ENABLE,
    registration_number VARCHAR2(20)
                                             NOT NULL ENABLE,
    entry_number        VARCHAR2(20)
                                             NOT NULL ENABLE,
    postal_address      VARCHAR2(20)
                                             NOT NULL ENABLE,
    status              NUMBER
                                             NOT NULL ENABLE,
    registration_date   DATE DEFAULT sysdate NOT NULL,
    var_field1          VARCHAR2(200),
    var_field2          VARCHAR2(200),
    var_field3          VARCHAR2(200),
    var_field4          VARCHAR2(200),
    var_field5          VARCHAR2(200),
    var_field6          VARCHAR2(200),
    var_field7          VARCHAR2(200),
    var_field8          VARCHAR2(200),
    var_field9          VARCHAR2(600),
    var_field10         VARCHAR2(600),
    created_by          NUMBER,
    created_on          DATE,
    modified_by         NUMBER,
    modified_date       DATE,
    user_id             NUMBER
        REFERENCES dat_kebs_users (id),
    business_nature_id  NUMBER
        REFERENCES cfg_kebs_business_nature (id)
)
    PARTITION BY RANGE (
                        registration_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_manufacturers_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_manufacturers_trg
    before
        insert
    on dat_kebs_manufacturers
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_manufacturers_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table dat_kebs_manufacturer_addresses;

create TABLE dat_kebs_manufacturer_addresses
(
    id                NUMBER               NOT NULL PRIMARY KEY,
    name              VARCHAR2(200)        NOT NULL UNIQUE,
    longitude         VARCHAR2(200),
    latitude          VARCHAR2(200),
    street_name       VARCHAR2(200)        NOT NULL,
    building_name     VARCHAR2(200)        NOT NULL,
    registration_date DATE DEFAULT sysdate NOT NULL,
    var_field1        VARCHAR2(200),
    var_field2        VARCHAR2(200),
    var_field3        VARCHAR2(200),
    var_field4        VARCHAR2(200),
    var_field5        VARCHAR2(200),
    var_field6        VARCHAR2(200),
    var_field7        VARCHAR2(200),
    var_field8        VARCHAR2(200),
    var_field9        VARCHAR2(600),
    var_field10       VARCHAR2(600),
    created_by        NUMBER,
    created_on        DATE,
    modified_by       NUMBER,
    modified_date     DATE,
    manufacturer      NUMBER
        REFERENCES dat_kebs_manufacturers (id)
)
    PARTITION BY RANGE (
                        registration_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_manufacturer_addresses_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_manufacturer_addresses_trg
    before
        insert
    on dat_kebs_manufacturer_addresses
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_manufacturer_addresses_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

drop table DAT_KEBS_MANUFACTURER_CONTACTS cascade constraints;

create TABLE dat_kebs_manufacturer_contacts
(
    id                NUMBER                                      NOT NULL PRIMARY KEY,
    manufacturer_id   NUMBER
        REFERENCES dat_kebs_manufacturers (id),
    first_name        VARCHAR2(100 CHAR)                          NOT NULL,
    last_name         VARCHAR2(100 CHAR)                          NOT NULL,
    email_address     VARCHAR2(100 CHAR)                          NOT NULL,
    cell_phone        VARCHAR2(100 CHAR)                          NOT NULL,
    registration_date DATE                        DEFAULT sysdate NOT NULL,
    status            NUMBER(2, 0),
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
    created_by        VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                                                  NOT NULL ENABLE,
    created_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                                                  NOT NULL ENABLE,
    modified_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on        TIMESTAMP(6) WITH TIME ZONE
)
    PARTITION BY RANGE (
                        registration_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_manufacturer_contacts_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_manufacturer_contacts_trg
    before
        insert
    on dat_kebs_manufacturer_contacts
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_manufacturer_contacts_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table DAT_KEBS_MANUFACTURER_CONTACTS
    add versions NUMBER(2, 0) DEFAULT 0 NOT NULL;

drop index kebs_manufacturer_contacts_idx;

create UNIQUE INDEX kebs_manufacturer_contacts_idx ON
    DAT_KEBS_MANUFACTURER_CONTACTS (
                                    manufacturer_id,
                                    status,
                                    versions
        )
    TABLESPACE qaimssdb_idx;

create index DAT_KEBS_MANUFACTURERS_USER_ID_STATUS_IDX on DAT_KEBS_MANUFACTURERS (USER_ID, STATUS) TABLESPACE qaimssdb_idx;

select *
from ALL_INDEXES
where INDEX_NAME = 'DAT_KEBS_MANUFACTURERS_USER_ID_IDX';

alter table qiamssadm.cfg_service_maps
    add employee_user_type NUMBER(2, 0);

alter table qiamssadm.cfg_service_maps
    add manufacturer_user_type NUMBER(2, 0);

alter table cfg_service_maps
    modify (
        id NOT NULL
        );

alter table cfg_service_maps
    add CONSTRAINT cfg_service_maps_pk PRIMARY KEY (id) ENABLE;

create TABLE log_service_requests
(
    id                    NUMBER                                      NOT NULL PRIMARY KEY,
    kafka_partition_id    NUMBER(5, 0),
    kafka_offset          NUMBER,
    kafka_topic           VARCHAR2(350 CHAR),
    kafka_message_key     VARCHAR2(350 CHAR),
    kafka_timestamp_type  VARCHAR2(350 CHAR),
    kafka_timestamp       NUMBER,
    response_status       VARCHAR2(350 CHAR),
    response_message      VARCHAR2(350 CHAR),
    request_date          DATE                        DEFAULT sysdate NOT NULL,
    event_bus_submit_date TIMESTAMP(6) WITH TIME ZONE,
    processing_start_date TIMESTAMP(6) WITH TIME ZONE,
    processing_end_date   TIMESTAMP(6) WITH TIME ZONE,
    service_maps_id       NUMBER
        REFERENCES cfg_service_maps (id),
    payload               VARCHAR2(3950),
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
    created_by            VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                                                      NOT NULL ENABLE,
    created_on            TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                                                      NOT NULL ENABLE,
    modified_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on           TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by             VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on            TIMESTAMP(6) WITH TIME ZONE
)
    PARTITION BY RANGE (
                        request_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;

alter table qiamssadm.log_service_requests
    add transaction_reference VARCHAR2(350 CHAR);

alter table qiamssadm.log_service_requests
    add previous_stage NUMBER(2, 0);

alter table qiamssadm.log_service_requests
    add current_stage NUMBER(2, 0);

alter table qiamssadm.log_service_requests
    add next_stage NUMBER(2, 0);

alter table qiamssadm.log_service_requests
    add previous_sequence NUMBER(2, 0);

alter table qiamssadm.log_service_requests
    add current_sequence NUMBER(2, 0);

alter table qiamssadm.log_service_requests
    add next_sequence NUMBER(2, 0);

create sequence log_service_requests_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_service_requests_trg
    before
        insert
    on log_service_requests
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_service_requests_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create TABLE cfg_service_maps_workflows
(
    id              NUMBER             NOT NULL PRIMARY KEY,
    service_maps_id number references cfg_service_maps (Id),
    bean_name       VARCHAR2(350 CHAR) NOT NULL,
    method_name     VARCHAR2(350 CHAR) NOT NULL,
    status          NUMBER(2, 0),
    descriptions    VARCHAR2(3800 CHAR),
    var_field_1     VARCHAR2(350 CHAR),
    var_field_2     VARCHAR2(350 CHAR),
    var_field_3     VARCHAR2(350 CHAR),
    var_field_4     VARCHAR2(350 CHAR),
    var_field_5     VARCHAR2(350 CHAR),
    var_field_6     VARCHAR2(350 CHAR),
    var_field_7     VARCHAR2(350 CHAR),
    var_field_8     VARCHAR2(350 CHAR),
    var_field_9     VARCHAR2(350 CHAR),
    var_field_10    VARCHAR2(350 CHAR),
    created_by      VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                       NOT NULL ENABLE,
    created_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                       NOT NULL ENABLE,
    modified_by     VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on      TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence cfg_service_maps_workflows_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_service_maps_workflows_trg
    before
        insert
    on cfg_service_maps_workflows
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_service_maps_workflows_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create TABLE cfg_service_maps_workflow_events
(
    id              NUMBER             NOT NULL PRIMARY KEY,
    workflow_id     number REFERENCES cfg_service_maps_workflows (id),
    function_name   VARCHAR2(350 CHAR) NOT NULL,
    sequence_number NUMBER(2, 0)       NOT NULL,
    execution_order NUMBER(2, 0)       not null,
    status          NUMBER(2, 0),
    descriptions    VARCHAR2(3800 CHAR),
    var_field_1     VARCHAR2(350 CHAR),
    var_field_2     VARCHAR2(350 CHAR),
    var_field_3     VARCHAR2(350 CHAR),
    var_field_4     VARCHAR2(350 CHAR),
    var_field_5     VARCHAR2(350 CHAR),
    var_field_6     VARCHAR2(350 CHAR),
    var_field_7     VARCHAR2(350 CHAR),
    var_field_8     VARCHAR2(350 CHAR),
    var_field_9     VARCHAR2(350 CHAR),
    var_field_10    VARCHAR2(350 CHAR),
    created_by      VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                       NOT NULL ENABLE,
    created_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                       NOT NULL ENABLE,
    modified_by     VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on      TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;

create sequence CFG_SERVICE_MAPS_WORKFLOW_EVENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_SERVICE_MAPS_WORKFLOW_EVENTS_trg
    before
        insert
    on CFG_SERVICE_MAPS_WORKFLOW_EVENTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SERVICE_MAPS_WORKFLOW_EVENTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table cfg_service_maps_workflows
    add period_before_duplicates number(2, 0);

select *
from cfg_service_maps_workflow_events;

create or replace view crm_service_maps_workflows_functions_vw
as
select w.id,
       w.SERVICE_MAPS_ID             service_map,
       w.id                          workflow_id,
       w.bean_name,
       w.method_name,
       w.period_before_duplicates,
       nvl(e.id, 0)                  event_id,
       nvl(e.function_name, 'dummy') function_name,
       w.workflow_stage              EXECUTION_ORDER,
       ROWNUM                        SEQUENCE_NUMBER,
       nvl(c.id, 0)                  integration_id,
       nvl(c.URL, 'dummy')           url,
       nvl(c.DESCRIPTIONS, 'dummy')  DESCRIPTIONS
from cfg_service_maps_workflow_events e,
     cfg_service_maps_workflows w,
     CFG_SERVICE_MAPS m,
     cfg_integration_configuration c
where e.workflow_id(+) = w.id
  and w.service_maps_id = m.ID
  and c.workflow_id(+) = w.id
--   and e.STATUS = 1
  and w.status = 1
  and m.STATUS = 1
--   and c.status = 1
order by m.ID, w.WORKFLOW_STAGE
;


select *
from cfg_service_maps_workflow_events e,
     cfg_service_maps_workflows w,
     CFG_SERVICE_MAPS m,
     cfg_integration_configuration c
where e.workflow_id(+) = w.id
  and w.service_maps_id = m.ID
  and c.workflow_id(+) = w.id
  and w.status = 1
  and m.STATUS = 1
;

select *
from CRM_SERVICE_MAPS_WORKFLOWS_FUNCTIONS_VW;


create table cfg_integration_configuration
(
    id                         number primary key,
    body_params                varchar2(500 char),
    config_keyword             varchar2(50 char),
    connection_request_timeout number(12, 0)               default 1000    not null,
    connect_timeout            number(12, 0)               default 1000    not null,
    exception_code             varchar2(500 char)          default '99'    not null,
    failure_code               varchar2(500 char)          default '99'    not null,
    follow_redirects           number(2, 0)                default 0       not null,
    header_params              varchar2(3500 char),
    max_conn_per_route         number(12, 0)               default 5       not null,
    max_conn_total             number(12, 0)               default 5       not null,
    ok_lower                   number(12, 0)               default 200,
    ok_upper                   number(12, 0)               default 399,
    password                   varchar2(500 char),
    request_keyword            varchar2(500 char),
    request_params             varchar2(3500 char),
    request_params_separator   varchar2(500 char),
    request_params_spel        varchar2(500 char),
    request_params_values      varchar2(3500 char),
    response_place_holder      varchar2(3500 char),
    sender                     varchar2(500 char),
    workflow_id                number references CFG_SERVICE_MAPS_WORKFLOWS (id),
    socket_timeout             number(12, 0)               default 1000    not null,
    url                        varchar2(500 char),
    username                   varchar2(500 char),
    status                     NUMBER(2, 0),
    descriptions               VARCHAR2(3800 CHAR),
    var_field_1                VARCHAR2(350 CHAR),
    var_field_2                VARCHAR2(350 CHAR),
    var_field_3                VARCHAR2(350 CHAR),
    var_field_4                VARCHAR2(350 CHAR),
    var_field_5                VARCHAR2(350 CHAR),
    var_field_6                VARCHAR2(350 CHAR),
    var_field_7                VARCHAR2(350 CHAR),
    var_field_8                VARCHAR2(350 CHAR),
    var_field_9                VARCHAR2(350 CHAR),
    var_field_10               VARCHAR2(350 CHAR),
    created_by                 VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                 TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                 TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;


create sequence cfg_integration_configuration_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_integration_configuration_trg
    before
        insert
    on cfg_integration_configuration
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_integration_configuration_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from CFG_SERVICE_MAPS;

select *
from CFG_SERVICE_MAPS_WORKFLOWS;

select *
from cfg_integration_configuration;

select *
from cfg_service_maps_workflow_events;


select *
from crm_service_maps_workflows_functions_vw;

commit;

select *
from LOG_SERVICE_REQUESTS;


alter table QIAMSSADM.CFG_INTEGRATION_CONFIGURATION
    add CLIENT_AUTHENTICATION varchar2(350 char);
alter table QIAMSSADM.CFG_INTEGRATION_CONFIGURATION
    add CLIENT_AUTHENTICATION_REALM varchar2(350 char);
alter table QIAMSSADM.CFG_INTEGRATION_CONFIGURATION
    add CLIENT_METHOD varchar2(350 char) default 'POST' not null;
alter table QIAMSSADM.CFG_SERVICE_MAPS
    add BASIC_AUTHENTICATION_VALUE varchar2(350 char);
alter table QIAMSSADM.CFG_SERVICE_MAPS
    add DIGEST_AUTHENTICATION_VALUE varchar2(350 char);

update CFG_SERVICE_MAPS
set BASIC_AUTHENTICATION_VALUE  = 'basic',
    DIGEST_AUTHENTICATION_VALUE = 'digest'
where id > 0;

update CFG_INTEGRATION_CONFIGURATION
set CLIENT_AUTHENTICATION = 'basic'
where id = 3;

commit;



create table log_workflow_transactions
(
    id                         number                      not null primary key enable,
    service_request            number references log_service_requests (id),
    transaction_date           date                        not null enable,
    transaction_status         number(7, 0),
    transaction_reference      varchar2(750 char),
    retries                    number(3, 0),
    retried                    number(3, 0),
    response_reference         varchar2(350 char),
    last_updated               date,
    method_id                  number references cfg_service_maps_workflows (id) enable,
    integration_id             number references CFG_INTEGRATION_CONFIGURATION (id),
    current_sequence           number(3, 0),
    execution_order            number(3, 0),
    cb_update_remote_date      varchar2(250 char),
    integration_request        varchar2(3990 char),
    integration_response       varchar2(3990 char),
    response_status            VARCHAR2(350 CHAR),
    response_message           VARCHAR2(350 CHAR),
    transaction_start_date     TIMESTAMP(6) WITH TIME ZONE,
    transaction_completed_date TIMESTAMP(6) WITH TIME ZONE,
    var_field_1                varchar2(350 char),
    var_field_2                varchar2(350 char),
    var_field_3                varchar2(350 char),
    var_field_4                varchar2(350 char),
    var_field_5                varchar2(350 char),
    var_field_6                varchar2(350 char),
    var_field_7                varchar2(350 char),
    var_field_8                varchar2(350 char),
    var_field_9                varchar2(350 char),
    var_field_10               varchar2(350 char),
    created_by                 varchar2(100 char)          not null enable,
    created_on                 TIMESTAMP(6) WITH TIME ZONE not null enable,
    modified_by                varchar2(100 char),
    modified_on                TIMESTAMP(6) WITH TIME ZONE,
    delete_by                  varchar2(100 char),
    delete_don                 TIMESTAMP(6) WITH TIME ZONE

)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;


create sequence log_workflow_transactions_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_workflow_transactions_trg
    before
        insert
    on log_workflow_transactions
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_workflow_transactions_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table QIAMSSADM.CFG_USER_TYPES
    add type_name varchar2(350) default 'dummy' not null unique;
alter table QIAMSSADM.CFG_CONTACT_TYPES
    add type_name varchar2(350) default 'dummy' not null unique;
alter table QIAMSSADM.CFG_CONTACT_TYPES
    add decriptions varchar2(350) default 'dummy' not null unique;

select *
from CFG_USER_TYPES;

select *
from CFG_SERVICE_MAPS;


select *
from LOG_SERVICE_REQUESTS;



select *
from LOG_WORKFLOW_TRANSACTIONS
order by id;

select *
from CFG_SERVICE_MAPS_WORKFLOWS;

select *
from all_tab_partitions
where table_name = 'LOG_WORKFLOW_TRANSACTIONS'
;


select *
from DAT_KEBS_EMPLOYEES;

update log_service_requests
set status           = 10,
    RESPONSE_MESSAGE = null
where id = 3;

commit;

alter table CFG_SERVICE_MAPS
    modify SUCCESS_STATUS_CODE VARCHAR2(350 char);

update CFG_SERVICE_MAPS
set SUCCESS_STATUS      = 30,
    SUCCESS_STATUS_CODE = '00'
where id > 100;

select *
from CFG_SERVICE_MAPS;

select *
from USER_ALL_TABLES
where TABLE_NAME like '%CONSIGNMENT%'
;

-- select *
-- from DAT_CONSIGNMENT_DOCUMENT;

alter table QIAMSSADM.CFG_SERVICE_MAPS_WORKFLOWS
    add workflow_stage number(2, 0) default 0 not null;

create unique index CFG_SERVICE_MAPS_WORKFLOWS_UNQ_MAP_STATUS_STAGE on QIAMSSADM.CFG_SERVICE_MAPS_WORKFLOWS (service_maps_id, status, workflow_stage) TABLESPACE qaimssdb_idx;

select *
from CFG_SERVICE_MAPS_WORKFLOWS
order by id;

commit;

select *
from CRM_SERVICE_MAPS_WORKFLOWS_FUNCTIONS_VW;



--------------------------------------------------------
--  DDL for Tables and Process for STANDARDS LEVY
--------------------------------------------------------

CREATE TABLE KEBS_MANUFACTURER_DEPARTMENTS
(
    ID               NUMBER                                      not null primary key,
    DEPARTMENT_CODE  VARCHAR2(128 BYTE),
    DEPARTMENT_NAME  VARCHAR2(128 BYTE),
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
)
    TABLESPACE qaimssdb_data;


create sequence KEBS_MANUFACTURER_DEPARTMENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger KEBS_MANUFACTURER_DEPARTMENTS_trg
    before
        insert
    on KEBS_MANUFACTURER_DEPARTMENTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_workflow_transactions_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


alter table dat_kebs_manufacturers
    add DEPARTMENT_CODE number references KEBS_MANUFACTURER_DEPARTMENTS (ID);
alter table dat_kebs_manufacturers
    add version number default 0 not null;


CREATE TABLE CFG_PAYMENT_TYPES
(
    ID               NUMBER                                      not null primary key,
    TYPE_CODE        VARCHAR2(128 BYTE),
    TYPE_NAME        VARCHAR2(128 BYTE),
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
)
    TABLESPACE qaimssdb_data;


create sequence CFG_PAYMENT_TYPES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_PAYMENT_TYPES_trg
    before
        insert
    on CFG_PAYMENT_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_PAYMENT_TYPES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

CREATE TABLE CFG_PAYMENT_MODE
(
    ID               NUMBER                                      not null primary key,
    mode_CODE        VARCHAR2(128 BYTE),
    mode_NAME        VARCHAR2(128 BYTE),
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
)
    TABLESPACE qaimssdb_data;


create sequence CFG_PAYMENT_MODE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_PAYMENT_MODE_trg
    before
        insert
    on CFG_PAYMENT_MODE
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_PAYMENT_MODE_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


CREATE TABLE DAT_PAYMENTS_LOG
(
    ID                    NUMBER                                        not null primary key,
    MANUFACTURER          NUMBER references dat_kebs_manufacturers (id) null,
    user_id               NUMBER references DAT_KEBS_USERS (id)         null,
    ESLIP_NUMBER          VARCHAR2(528 BYTE),
    ESLIP_GENERATION_DATE DATE,
    PERIOD_FROM           DATE,
    PERIOD_TO             DATE,
    PAYMENT_TYPE          NUMBER references CFG_PAYMENT_TYPES (id),
    payment_mode          number references CFG_PAYMENT_MODE (ID),
    COMMODITY             VARCHAR2(528 BYTE),
    QUANTITY_MANUFACTURED NUMBER,
    SKU                   VARCHAR2(700 char),
    TOTAL_VALUE           NUMBER(30, 3),
    LEVY_PAYABLE          NUMBER(30, 3),
    PAYMENT_DATE          DATE,
    BANK                  VARCHAR2(528 BYTE),
    BANK_CODE             VARCHAR2(528 BYTE),
    BANK_REFERENCE_NUMBER VARCHAR2(528 BYTE),
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
    created_by            VARCHAR2(100 CHAR)          DEFAULT 'admin'   NOT NULL ENABLE,
    created_on            TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate   NOT NULL ENABLE,
    last_modified_by      VARCHAR2(100 CHAR),
    last_modified_on      TIMESTAMP(6) WITH TIME ZONE,
    update_by             VARCHAR2(100 CHAR),
    updated_on            TIMESTAMP(6) WITH TIME ZONE,
    delete_by             VARCHAR2(100 CHAR),
    deleted_on            TIMESTAMP(6) WITH TIME ZONE,
    VERSION               NUMBER

)
    PARTITION BY RANGE (
                        PAYMENT_DATE
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data;



create sequence DAT_PAYMENTS_LOG_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_PAYMENTS_LOG_trg
    before
        insert
    on DAT_PAYMENTS_LOG
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_PAYMENTS_LOG_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

CREATE TABLE DAT_KEBS_STDLVY_PENALTIES
(
    id                    number                                      not null primary key,
    STDLVY_MANUFACTURER   NUMBER,
    PERIOD_FROM           DATE,
    PERIOD_TO             DATE,
    COMMODITY             VARCHAR2(128 BYTE),
    QUANTITY_MANUFACTURED NUMBER,
    SKU                   VARCHAR2(128 BYTE),
    TOTAL_VALUE           FLOAT(126),
    LEVY_PAYABLE          FLOAT(126),
    PENALTY_DATE          DATE                        DEFAULT SYSDATE NOT NULL,
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

)
    PARTITION BY RANGE (
                        PENALTY_DATE
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;


create sequence DAT_KEBS_STDLVY_PENALTIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_STDLVY_PENALTIES_trg
    before
        insert
    on DAT_KEBS_STDLVY_PENALTIES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_STDLVY_PENALTIES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


alter table DAT_KEBS_STDLVY_PENALTIES
    add constraint DAT_KEBS_STDLVY_PENALTIES_DAT_KEBS_MANUFACTURERS_ID_FK
        foreign key (MANUFACTURER_ID) references DAT_KEBS_MANUFACTURERS;


select *
from LOG_WORKFLOW_TRANSACTIONS
order by id;

alter table CFG_INTEGRATION_CONFIGURATION
    add gson_date_formt varchar2(100 char);
alter table CFG_INTEGRATION_CONFIGURATION
    add gson_output_bean varchar2(350 char);

select *
from CFG_INTEGRATION_CONFIGURATION;

commit;


create table log_brs_lookup_manufacturer_data
(
    id                  number                                        not null primary key,
    manufacturer_id     number references dat_kebs_manufacturers (id) null,
    transaction_date    date                        default sysdate   not null,
    registration_number varchar2(350 char),
    registration_date   date,
    postal_address      varchar2(350 char),
    physical_address    varchar2(3000 char),
    phone_number        varchar2(100 char),
    brs_id              varchar2(350 char),
    email               varchar2(350 char),
    kra_pin             varchar2(350 char),
    business_name       varchar2(350 char),
    status              NUMBER(2, 0),
    descriptions        VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'   NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate   NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER

)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;

create sequence log_brs_lookup_manufacturer_data_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

drop trigger log_brs_lookup_manufacturer_data_trg;


create trigger log_brs_lookup_manufacturer_data_trg
    before
        insert
    on log_brs_lookup_manufacturer_data
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_brs_lookup_manufacturer_data_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table log_brs_lookup_manufacturer_partners
(
    id               number                                      not null primary key,
    manufacturer_id  number references log_brs_lookup_manufacturer_data (id),
    transaction_date date                        default sysdate not null,
    names            varchar2(350 char),
    id_type          date,
    id_number        varchar2(350 char),
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

)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;


create sequence log_brs_lookup_manufacturer_partners_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger log_brs_lookup_manufacturer_partners_trg
    before
        insert
    on log_brs_lookup_manufacturer_partners
    for each row
begin
    if inserting then
        if :new.id is null then
            select log_brs_lookup_manufacturer_partners_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create index MANUFACTURER_REGISTRATION_NUMBER_IDX on dat_kebs_manufacturers (REGISTRATION_NUMBER) TABLESPACE qaimssdb_idx;
create index MANUFACTURER_REGISTRATION_NUMBER_STATUS_IDX on dat_kebs_manufacturers (REGISTRATION_NUMBER, STATUS) TABLESPACE qaimssdb_idx;
create index log_brs_lookup_manufacturer_data_REGISTRATION_NUMBER_IDX on log_brs_lookup_manufacturer_data (REGISTRATION_NUMBER) TABLESPACE qaimssdb_idx;
create index LOG_BRS_LOOKUP_MANUFACTURER_PARTNERS_MANUFACTURER_ID_status_IDX on LOG_BRS_LOOKUP_MANUFACTURER_PARTNERS (MANUFACTURER_ID, STATUS) TABLESPACE qaimssdb_idx;

create index dat_kebs_manufacturers_user_id_idx on dat_kebs_manufacturers (USER_ID) tablespace qaimssdb_idx;

create unique index dat_kebs_manufacturers_user_id_status__unq_idx on dat_kebs_manufacturers (USER_ID, STATUS) tablespace qaimssdb_idx;

create unique index dat_kebs_manufacturer_addresses_MANUFACTURER_status_versions_unq_idx on dat_kebs_manufacturer_addresses (MANUFACTURER, STATUS, VERSIONS) tablespace qaimssdb_idx;

alter table DAT_KEBS_MANUFACTURER_ADDRESSES
    add version number(2, 0);
alter table DAT_KEBS_MANUFACTURER_ADDRESSES
    add status number(2, 0);

alter table DAT_KEBS_MANUFACTURER_ADDRESSES rename column version to versions;

alter table LOG_BRS_LOOKUP_MANUFACTURER_DATA
    add brs_status varchar2(350 char);

alter table LOG_BRS_LOOKUP_MANUFACTURER_PARTNERS
    modify ID_TYPE VARCHAR2(350 char);

select *
from log_brs_lookup_manufacturer_partners
order by id;

select *
from LOG_BRS_LOOKUP_MANUFACTURER_PARTNERS
order by id
;

select *
from CFG_INTEGRATION_CONFIGURATION;

commit;

select *
from CFG_SERVICE_MAPS_WORKFLOWS
order by id;

select *
from log_service_requests
order by id;

select *
from LOG_WORKFLOW_TRANSACTIONS
order by id
;

alter table log_service_requests
    add request_id number;
alter table log_service_requests
    add request_identifier varchar2(1350 char);

alter table CFG_SERVICE_MAPS
    add MANUFACTURER_USER_TABLE varchar2(1000 char);

alter table DAT_KEBS_MANUFACTURER_ADDRESSES
    add plot_number varchar2(350 char);


Select a.name nature_of_business, b.name line_of_business, c.name type_of_business
from CFG_KEBS_BUSINESS_NATURE a,
     cfg_kebs_business_lines b,
     cfg_kebs_business_types c
where a.business_lines_id = b.id
  and a.business_type_id = c.id;

select *
from CFG_KEBS_BUSINESS_NATURE
order by id;


create table dat_KEBS_SDL_SL1_FORMS
(
    id                           number                                      not null primary key,
    manufacturer_id              number references dat_kebs_manufacturers (id),
    business_registration_number varchar2(350 char),
    business_type                varchar2(1000 char),
    business_name                varchar2(1000 char),
    plot_number                  varchar2(100 char),
    postal_address               varchar2(100 char),
    telephone                    varchar2(100 char),
    admin_location               varchar2(1000 char),
    partners                     varchar2(1000 char),
    transaction_date             date                        default sysdate not null,
    status                       NUMBER(2, 0),
    descriptions                 VARCHAR2(3800 CHAR),
    var_field_1                  VARCHAR2(350 CHAR),
    var_field_2                  VARCHAR2(350 CHAR),
    var_field_3                  VARCHAR2(350 CHAR),
    var_field_4                  VARCHAR2(350 CHAR),
    var_field_5                  VARCHAR2(350 CHAR),
    var_field_6                  VARCHAR2(350 CHAR),
    var_field_7                  VARCHAR2(350 CHAR),
    var_field_8                  VARCHAR2(350 CHAR),
    var_field_9                  VARCHAR2(350 CHAR),
    var_field_10                 VARCHAR2(350 CHAR),
    created_by                   VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by             VARCHAR2(100 CHAR),
    last_modified_on             TIMESTAMP(6) WITH TIME ZONE,
    update_by                    VARCHAR2(100 CHAR),
    updated_on                   TIMESTAMP(6) WITH TIME ZONE,
    delete_by                    VARCHAR2(100 CHAR),
    deleted_on                   TIMESTAMP(6) WITH TIME ZONE,
    VERSION                      NUMBER

)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;

alter table DAT_KEBS_SDL_SL1_FORMS
    add locations varchar2(1000 char);


create sequence DAT_KEBS_SDL_SL1_FORMS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_SDL_SL1_FORMS_trg
    before
        insert
    on DAT_KEBS_SDL_SL1_FORMS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_SDL_SL1_FORMS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table CFG_SERVICE_MAPS
    add main_Version_id number(2, 0) default 0 not null;

EXEC DBMS_UTILITY.compile_schema('APOLLO', compile_all => false);

select *
from CFG_SERVICE_MAPS;

select *
from CRM_SERVICE_MAPS_WORKFLOWS_FUNCTIONS_VW;



create table REF_KEBS_LOCATIONS
(
    id               number                                      not null primary key,
    country          varchar2(350 char),
    county           varchar2(350 char),
    sub_county       varchar2(350 char),
    town             varchar2(350 char),
    market           varchar2(350 char),
    transaction_date date                        default sysdate not null,
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

)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;

create sequence REF_KEBS_LOCATIONS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger REF_KEBS_LOCATIONS_trg
    before
        insert
    on REF_KEBS_LOCATIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select REF_KEBS_LOCATIONS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table dat_kebs_manufacturer_addresses
    add location number references REF_KEBS_LOCATIONS (id);



alter table dat_kebs_users
    add location_id number null references REF_KEBS_LOCATIONS (id);

alter table DAT_KEBS_USERS
    drop column location_id;


alter table REF_KEBS_LOCATIONS
    add kebs_region varchar(500 char);
alter table REF_KEBS_LOCATIONS
    add sub_kebs_region varchar(500 char);
-- alter table REF_KEBS_LOCATIONS rename column sub_kebs_region to kebs_sub_region;
-- alter table DAT_KEBS_MANUFACTURER_ADDRESSES rename column location to location_id;


alter table DAT_KEBS_INSPECTION_GENERAL_FORM
    add status number(2, 0);

select *
from DAT_MANUFACTURE_BRAND_PERMIT;

select *
from CFG_BRAND_DETAILS;

alter table CFG_PRODUCTS_DETAILS
    add foreign key (MANUFACTURER_ID) references DAT_KEBS_MANUFACTURERS;

alter table CFG_BRAND_DETAILS
    add foreign key (PRODUCT_ID) references CFG_PRODUCTS_DETAILS (ID);

select *
from CFG_BRANCH_DETAILS;



create table cfg_kebs_designations
(
    id               number                                      not null primary key,
    designation_name varchar2(500 char),
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

create sequence cfg_kebs_designations_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_designations_trg
    before
        insert
    on cfg_kebs_designations
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_designations_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/