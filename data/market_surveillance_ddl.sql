/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
***************************Table USED IN DI*****************************************
select *
from DAT_KEBS_MS_COMPLAINT
-- where  UUID = '0c06289c-d7cc-4cd0-a74e-9f9f4528222b'
where ID = 485
-- where REFERENCE_NUMBER = 'CP#2021032036B3147'
order by id desc
;

select * from DAT_KEBS_MS_COMPLAINT_CUSTOMERS
-- where  UUID = '0c06289c-d7cc-4cd0-a74e-9f9f4528222b'
-- where ID = 402
order by id desc
;

select * from DAT_KEBS_MS_COMPLAINT_LOCATION
-- where  UUID = '0c06289c-d7cc-4cd0-a74e-9f9f4528222b'
-- where ID = 402
order by id desc
;



select * from DAT_KEBS_MS_SAMPLE_COLLECTION
-- where  UUID = '0c06289c-d7cc-4cd0-a74e-9f9f4528222b'
where MS_FUEL_INSPECTION_ID = 202
order by id desc
;

select * from CFG_KEBS_MS_TYPES
-- where ID = 2055
-- where  UUID = '0c06289c-d7cc-4cd0-a74e-9f9f4528222b'
order by id desc
;

select * from CFG_SERVICE_MAPS
where ID = 130
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc
;
select * from CFG_WORKPLAN_YEARS_CODES
-- where ID = 2055
order by id desc
;
select * from DAT_WORKPLAN_CREATED
-- where ID = 2055184
order by id desc
;

select * from DAT_KEBS_MS_FUEL_INSPECTION
-- where ID = 2055184
order by id desc
;

select *
from DAT_KEBS_MS_SAMPLE_COLLECTION
-- where ID = 2055
order by id desc
;
select *
from DAT_KEBS_MS_WORKPLAN_GENARATED
where UUID = '706cd0c7-d7f2-43b0-84a7-4891e11a64d7'
-- where ID = 2055
order by id desc
;
select *
from CFG_NOTIFICATIONS--122
where SERVICE_MAP_ID = 101
--     where UUID = '1b474c60-c7f4-4ccb-9cce-efca7ead8439'
order by ID desc;

select *
from CFG_SERVICE_MAPS
-- where id = 207
order by ID desc
;

alter table DAT_KEBS_MS_COMPLAINT
    modify DIVISION NUMBER REFERENCES CFG_KEBS_DIVISIONS (ID)
/



select *
from DAT_KEBS_USERS
where USER_NAME = '254safaris@gmail.com';
select *
from DAT_KEBS_USERS
where USER_NAME = 'informatic2016@gmail.com';
select *
from DAT_KEBS_USERS
where USER_NAME = 'paul.kalenda@bskglobaltech.com';
select *
from DAT_KEBS_USERS
where USER_NAME = 'paul.kalenda@bskglobaltech.com';

select * from DAT_KEBS_USERS where USER_NAME = 'testUpdate2@gmail.com';

****************************Created Tables USED IN DI********************************

create table CFG_KEBS_MS_TYPES
(
    id               NUMBER PRIMARY KEY,
    TYPE_NAME        VARCHAR2(200),
    MARK_REF         VARCHAR2(50),
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

create sequence CFG_KEBS_MS_TYPES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_MS_TYPES_seq_trg
    before
        insert
    on CFG_KEBS_MS_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_MS_TYPES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_MS_TYPES_idx on CFG_KEBS_MS_TYPES (TYPE_NAME,status) TABLESPACE qaimssdb_idx;
/


 create table DAT_WORKPLAN_CREATED
(
    id               NUMBER PRIMARY KEY,
    YEAR_NAME_ID        NUMBER REFERENCES CFG_WORKPLAN_YEARS_CODES (ID) ,
    USER_CREATED_ID        NUMBER REFERENCES DAT_KEBS_USERS (ID) ,
    CREATED_DATE        DATE,
    CREATED_STATUS        NUMBER(2),
     ENDED_DATE        DATE,
    ENDED_STATUS        NUMBER(2),
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

create sequence DAT_WORKPLAN_CREATED_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_WORKPLAN_CREATED_seq_trg
    before
        insert
    on DAT_WORKPLAN_CREATED
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_WORKPLAN_CREATED_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index DAT_WORKPLAN_CREATED_idx on DAT_WORKPLAN_CREATED (YEAR_NAME_ID,USER_CREATED_ID, CREATED_DATE, ENDED_DATE,status) TABLESPACE qaimssdb_idx;
/


create table CFG_WORKPLAN_YEARS_CODES
(
    id               NUMBER PRIMARY KEY,
    YEAR_NAME        VARCHAR2(200) UNIQUE ,
        WORKPLAN_CREATION_START_DATE,
    WORKPLAN_CREATION_END_DATE DATE,
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

create sequence CFG_WORKPLAN_YEARS_CODES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_WORKPLAN_YEARS_CODES_seq_trg
    before
        insert
    on CFG_WORKPLAN_YEARS_CODES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_WORKPLAN_YEARS_CODES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_WORKPLAN_YEARS_CODES_idx on CFG_WORKPLAN_YEARS_CODES (YEAR_NAME,status) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_SURVEY_ITEMS
(
    id               number                                      not null primary key,
    product_id       number references CFG_PRODUCTS_DETAILS (id),
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

create sequence DAT_KEBS_SURVEY_ITEMS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_SURVEY_ITEMS_trg
    before
        insert
    on DAT_KEBS_SURVEY_ITEMS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_SURVEY_ITEMS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


select *
from CFG_KEBS_SUB_SECTIONS_LEVEL2;

select *
from CFG_KEBS_SECTIONS;

CREATE OR REPLACE VIEW ms_sample_collection AS
SELECT sampCollParam.PRODUCT_BRAND_NAME, sampCollParam.batch_size, sampCollParam.batch_no, sampCollParam.sample_size, sampCollParam.SAMPLE_COLLECTION_ID,
    sampCollect.name_manufacturer_trader, sampCollect.ADDRESS_MANUFACTURE_TRADER, sampCollect.any_remarks, sampCollect.name_officer_collecting_sample,
       sampCollect.date_officer_collecting_sample, sampCollect.designation_officer_collecting_sample, sampCollect.date_witness, sampCollect.designation_witness, sampCollect.name_witness,
       sampCollect.reasons_collecting_samples, sampCollect.sampling_method, sampCollect.status
FROM DAT_KEBS_MS_SAMPLE_COLLECTION sampCollect
                  INNER JOIN DAT_KEBS_MS_COLLECTION_PARAMETERS sampCollParam ON sampCollect.ID = sampCollParam.SAMPLE_COLLECTION_ID;

select *
from CFG_KEBS_DIVISIONS;

select *
from CFG_KEBS_SUB_REGIONS;

create table dat_kebs_user_profiles
(
    id               number                                      not null primary key,
    user_id          number references DAT_KEBS_USERS (id),
    sub_region_id    number references CFG_KEBS_SUB_REGIONS (ID) null,
    designation_id   number,
    subsection_id    number references CFG_KEBS_SECTIONS (ID),
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

create sequence dat_kebs_user_profiles_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_user_profiles_trg
    before
        insert
    on dat_kebs_user_profiles
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_user_profiles_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table dat_kebs_user_profiles
    add foreign key (designation_id) references CFG_KEBS_DESIGNATIONS (ID);

alter table APOLLO.CFG_SERVICE_MAPS
    add sub_region_id number;
alter table APOLLO.CFG_SERVICE_MAPS
    add designation_id number;
alter table APOLLO.CFG_SERVICE_MAPS
    add UI_PAGE_SIZE number(2, 0);

select *
from APOLLO.DAT_KEBS_USERS;

commit;

select *
from CFG_USER_ROLES_PRIVILEGES;

select *
from CFG_USER_ROLES;

select *
from CFG_USER_PRIVILEGES;


select *
from LOG_SERVICE_REQUESTS
;

select *
from LOG_WORKFLOW_TRANSACTIONS
where TRANSACTION_DATE >= trunc(sysdate)
order by ID;

select *
from DAT_KEBS_INSPECTION_HACCP_IMPLEMENTATION_FORM
;


create table DAT_KEBS_MS_WORKPLAN
(
    id                  number                                      not null primary key,
    activity_name       varchar2(350 char),
    activity_reference  varchar2(350 char)                          not null unique,
    region_id           number references CFG_KEBS_REGIONS (id),
    sub_county          varchar2(350 char),
    scheduled_for       date,
    report_submitted_by date,
    town_id             number references CFG_KEBS_TOWNS (id),
    latest_approval     number,
    transaction_date    date                        default sysdate not null,
    status              NUMBER(2, 0),
    remarks             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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

create sequence DAT_KEBS_MS_WORKPLAN_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORKPLAN_trg
    before
        insert
    on DAT_KEBS_MS_WORKPLAN
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORKPLAN_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_WORK_PLAN_APPROVALS
(
    id               number                                      not null primary key,
    WORK_PLAN_ID     number references DAT_KEBS_MS_WORKPLAN (id) not null,
    work_plan_stage  number(1, 0)                default 0       not null,
    approved_by      VARCHAR2(100 CHAR),
    approved_on      TIMESTAMP(6) WITH TIME ZONE,
    transaction_date date                        default sysdate not null,
    status           NUMBER(2, 0),
    approval_remarks VARCHAR2(3800 CHAR),
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

create sequence DAT_KEBS_MS_WORK_PLAN_APPROVALS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_APPROVALS_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_APPROVALS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_APPROVALS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MS_WORK_PLAN_USERS
(
    id               number                                      not null primary key,
    WORK_PLAN_ID     number references DAT_KEBS_MS_WORKPLAN (id) not null,
    USER_ID          number(1, 0) references DAT_KEBS_USERS (id),
    transaction_date date                        default sysdate not null,
    status           NUMBER(2, 0),
    remarks          VARCHAR2(3800 CHAR),
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

create sequence DAT_KEBS_MS_WORK_PLAN_USERS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_USERS_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_USERS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_USERS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MS_WORK_PLAN_RESOURCES
(
    id               number                                      not null primary key,
    WORK_PLAN_ID     number references DAT_KEBS_MS_WORKPLAN (id) not null,
    resources_id     number(1, 0) references CFG_KEBS_SURVEILLANCE_RESOURCES (id),
    transaction_date date                        default sysdate not null,
    status           NUMBER(2, 0),
    remarks          VARCHAR2(3800 CHAR),
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

create sequence DAT_KEBS_MS_WORK_PLAN_RESOURCES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_RESOURCES_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_RESOURCES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_RESOURCES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_WORK_PLAN_ITEMS
(
    id                 number                                      not null primary key,
    WORK_PLAN_ID       number references DAT_KEBS_MS_WORKPLAN (id) not null,
    survey_items_id    number(1, 0) references DAT_KEBS_SURVEY_ITEMS (id),
    scheduled_for      date,
    surveyed_on        TIMESTAMP(6) WITH TIME ZONE,
    approval_remarks   varchar2(3500),
    approval_by        varchar2(350 char),
    approval_date      date,
    surveyed_by        varchar2(350 char),
    surveyed_date      date,
    survey_remarks     varchar2(3500 char),
    transaction_date   date                        default sysdate not null,
    documentation_link varchar2(900 char),
    status             NUMBER(2, 0),
    remarks            VARCHAR2(3800 CHAR),
    var_field_1        VARCHAR2(350 CHAR),
    var_field_2        VARCHAR2(350 CHAR),
    var_field_3        VARCHAR2(350 CHAR),
    var_field_4        VARCHAR2(350 CHAR),
    var_field_5        VARCHAR2(350 CHAR),
    var_field_6        VARCHAR2(350 CHAR),
    var_field_7        VARCHAR2(350 CHAR),
    var_field_8        VARCHAR2(350 CHAR),
    var_field_9        VARCHAR2(350 CHAR),
    var_field_10       VARCHAR2(350 CHAR),
    created_by         VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    last_modified_by   VARCHAR2(100 CHAR),
    last_modified_on   TIMESTAMP(6) WITH TIME ZONE,
    update_by          VARCHAR2(100 CHAR),
    updated_on         TIMESTAMP(6) WITH TIME ZONE,
    delete_by          VARCHAR2(100 CHAR),
    deleted_on         TIMESTAMP(6) WITH TIME ZONE,
    VERSION            NUMBER
)
    PARTITION BY RANGE (
                        transaction_date
    ) INTERVAL (numtodsinterval(1, 'DAY'))
(
    PARTITION keb_20200315
        VALUES LESS THAN ( TIMESTAMP ' 2020-03-14 00:00:00' )
) TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_MS_WORK_PLAN_ITEMS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_ITEMS_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_ITEMS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_ITEMS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table DAT_KEBS_MS_WORKPLAN
    add USER_ID number references DAT_KEBS_USERS (ID);


create table DAT_KEBS_MS_WORK_PLAN_BUDGET_LINES
(
    ID                   NUMBER                                             NOT NULL PRIMARY KEY,
    workplan_id          number references DAT_KEBS_MS_WORKPLAN (ID),
    TRANSACTION_DATE     DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    budget_line_item     varchar2(350 char),
    line_item_quantity   number(28, 2),
    line_item_unit_cost  number(28, 2),
    line_item_total_cost number(28, 2),
    status               NUMBER(2, 0),
    remarks              VARCHAR2(3800 CHAR),
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
    created_by           VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on           TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by     VARCHAR2(100 CHAR),
    last_modified_on     TIMESTAMP(6) WITH TIME ZONE,
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
) TABLESPACE qaimssdb_data
;


create sequence DAT_KEBS_MS_WORK_PLAN_BUDGET_LINES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_WORK_PLAN_BUDGET_LINES_trg
    before
        insert
    on DAT_KEBS_MS_WORK_PLAN_BUDGET_LINES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_WORK_PLAN_BUDGET_LINES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_COMPLAINTS
(
    ID                NUMBER                                      not null
        primary key,
    COMPLAINT_DETAILS VARCHAR2(4000 char),
    REVISION          NUMBER(2),
    STATUS            NUMBER(2),
    SUBMISSION_DATE   TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    REVISION_DATE     TIMESTAMP(6) WITH TIME ZONE,
    VAR_FIELD_1       VARCHAR2(350 char),
    VAR_FIELD_2       VARCHAR2(350 char),
    VAR_FIELD_3       VARCHAR2(350 char),
    VAR_FIELD_4       VARCHAR2(350 char),
    VAR_FIELD_5       VARCHAR2(350 char),
    VAR_FIELD_6       VARCHAR2(350 char),
    VAR_FIELD_7       VARCHAR2(350 char),
    VAR_FIELD_8       VARCHAR2(350 char),
    VAR_FIELD_9       VARCHAR2(350 char),
    VAR_FIELD_10      VARCHAR2(350 char),
    CREATED_BY        VARCHAR2(100 char)          default 'admin' not null,
    CREATED_ON        TIMESTAMP(6) WITH TIME ZONE default sysdate not null,
    MODIFIED_BY       VARCHAR2(100 char),
    MODIFIED_ON       TIMESTAMP(6) WITH TIME ZONE,
    DELETE_BY         VARCHAR2(100 char),
    DELETED_ON        TIMESTAMP(6) WITH TIME ZONE,
    transaction_date  Date                        default sysdate not null
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

--EXEC DBMS_REDEFINITION.can_redef_table(USER, 'DAT_KEBS_MS_COMPLAINT');

-- Alter parallelism to desired level for large tables.
--ALTER SESSION FORCE PARALLEL DML PARALLEL 8;
--ALTER SESSION FORCE PARALLEL QUERY PARALLEL 8;

-- BEGIN
--     DBMS_REDEFINITION.start_redef_table(
--             uname => USER,
--             orig_table => 'DAT_KEBS_MS_COMPLAINT',
--             int_table => 'DAT_KEBS_MS_COMPLAINTS');
-- END;
-- /
--
--
-- -- Optionally synchronize new table with interim data before index creation
-- BEGIN
--     dbms_redefinition.sync_interim_table(
--             uname => USER,
--             orig_table => 'DAT_KEBS_MS_COMPLAINT',
--             int_table => 'DAT_KEBS_MS_COMPLAINTS');
-- END;
-- /


-- SET SERVEROUTPUT ON
-- DECLARE
--     l_errors NUMBER;
-- BEGIN
--     DBMS_REDEFINITION.copy_table_dependents(
--             uname => USER,
--             orig_table => 'DAT_KEBS_MS_COMPLAINT',
--             int_table => 'DAT_KEBS_MS_COMPLAINTS',
--             copy_indexes => DBMS_REDEFINITION.cons_orig_params,
--             copy_triggers => TRUE,
--             copy_constraints => TRUE,
--             copy_privileges => TRUE,
--             ignore_errors => FALSE,
--             num_errors => l_errors,
--             copy_statistics => FALSE,
--             copy_mvlog => FALSE);
--
--     DBMS_OUTPUT.put_line('Errors=' || l_errors);
-- END;
-- /

-- BEGIN
--     dbms_redefinition.finish_redef_table(
--             uname => USER,
--             orig_table => 'DAT_KEBS_MS_COMPLAINT',
--             int_table => 'DAT_KEBS_MS_COMPLAINTS');
-- END;
-- /

SELECT *
FROM user_tables
WHERE table_name = 'DAT_KEBS_USERS';

select *
FROM user_tab_partitions
WHERE table_name = 'LOG_SERVICE_REQUESTS'
;

select *
from USER_CONSTRAINTS
where CONSTRAINT_TYPE = 'R';

-- alter table DAT_KEBS_MS_COMPLAINT_OFFICER rename column "COMPLAINT_id" to COMPLAINT_ID;


SELECT
--        'alter table '||CHILD.TABLE_NAME ||' add foreign key ('||CHILD.COLUMN_NAME||') references '||PARENT.TABLE_NAME||' (ID);' QUERY_1,
--        'alter table '||CHILD.TABLE_NAME ||' drop constraint '||CHILD.CONSTRAINT_NAME||';' query_2,
    PARENT.TABLE_NAME         "PARENT TABLE_NAME"
     , PARENT.CONSTRAINT_NAME "PARENT PK CONSTRAINT"
     , '->'                   " "
     , CHILD.TABLE_NAME       "CHILD TABLE_NAME"
     , CHILD.COLUMN_NAME      "CHILD COLUMN_NAME"
     , CHILD.CONSTRAINT_NAME  "CHILD CONSTRAINT_NAME"
FROM ALL_CONS_COLUMNS CHILD
   , ALL_CONSTRAINTS CT
   , ALL_CONSTRAINTS PARENT
WHERE CHILD.OWNER = CT.OWNER
  AND CT.CONSTRAINT_TYPE = 'R'
  AND CHILD.CONSTRAINT_NAME = CT.CONSTRAINT_NAME
  AND CT.R_OWNER = PARENT.OWNER
  AND CT.R_CONSTRAINT_NAME = PARENT.CONSTRAINT_NAME
  AND PARENT.TABLE_NAME = 'DAT_KEBS_MS_COMPLAINTS' -- table name variable
  AND CT.OWNER = 'APOLLO';

select *
from ALL_CONS_COLUMNS
where TABLE_NAME = 'DAT_KEBS_MS_COMPLAINT';

-- select * from DAT_KEBS_MS_COMPLAINTS;

-- drop trigger DAT_KEBS_MS_COMPLAINTS_TRG;


-- drop table DAT_KEBS_MS_COMPLAINTS;

create trigger DAT_KEBS_MS_COMPLAINTS_TRG
    before insert
    on DAT_KEBS_MS_COMPLAINTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create table DAT_KEBS_MS_COMPLAINT_REMEDIES
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID     number references DAT_KEBS_MS_COMPLAINT (ID),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    remedy           VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_COMPLAINT_REMEDIES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_REMEDIES_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_REMEDIES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_REMEDIES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;


create table DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID     number references DAT_KEBS_MS_COMPLAINT (ID),
    KEBS_OFFICE      VARCHAR2(1350 CHAR),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table DAT_KEBS_MS_COMPLAINT_KEBS_REMARKS rename column kebs_office to kebs_officer;

create table DAT_KEBS_MS_COMPLAINT_HANDLERS
(
    ID                     NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID           number references DAT_KEBS_MS_COMPLAINT (ID),
    KEBS_OFFICER           VARCHAR2(1350 CHAR),
    OFFICER_DESIGNATION    VARCHAR2(1350 CHAR),
    KEBS_SUPERVISOR        VARCHAR2(1350 CHAR),
    SUPERVISOR_DESIGNATION VARCHAR2(1350 CHAR),
    TRANSACTION_DATE       DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status                 NUMBER(2, 0),
    REMARKS                VARCHAR2(3800 CHAR),
    var_field_1            VARCHAR2(350 CHAR),
    var_field_2            VARCHAR2(350 CHAR),
    var_field_3            VARCHAR2(350 CHAR),
    var_field_4            VARCHAR2(350 CHAR),
    var_field_5            VARCHAR2(350 CHAR),
    var_field_6            VARCHAR2(350 CHAR),
    var_field_7            VARCHAR2(350 CHAR),
    var_field_8            VARCHAR2(350 CHAR),
    var_field_9            VARCHAR2(350 CHAR),
    var_field_10           VARCHAR2(350 CHAR),
    created_by             VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on             TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by       VARCHAR2(100 CHAR),
    last_modified_on       TIMESTAMP(6) WITH TIME ZONE,
    update_by              VARCHAR2(100 CHAR),
    updated_on             TIMESTAMP(6) WITH TIME ZONE,
    delete_by              VARCHAR2(100 CHAR),
    deleted_on             TIMESTAMP(6) WITH TIME ZONE,
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
) TABLESPACE qaimssdb_data
;


create sequence DAT_KEBS_MS_COMPLAINT_HANDLERS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_HANDLERS_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_HANDLERS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_HANDLERS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_COMPLAINT_ACCEPT_WORK_FLOW
(
    ID                            NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID                  number references DAT_KEBS_MS_COMPLAINT (ID),
    REVIEWING_OFFICER             VARCHAR2(1350 CHAR),
    REVIEWING_OFFICER_DESIGNATION VARCHAR2(1350 CHAR),
    APPROVING_OFFICER             VARCHAR2(1350 CHAR),
    APPROVING_OFFICER_DESIGNATION VARCHAR2(1350 CHAR),
    REVIEW_DATE                   DATE,
    APPROVAL                      DATE,
    TRANSACTION_DATE              DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status                        NUMBER(2, 0),
    REMARKS                       VARCHAR2(3800 CHAR),
    var_field_1                   VARCHAR2(350 CHAR),
    var_field_2                   VARCHAR2(350 CHAR),
    var_field_3                   VARCHAR2(350 CHAR),
    var_field_4                   VARCHAR2(350 CHAR),
    var_field_5                   VARCHAR2(350 CHAR),
    var_field_6                   VARCHAR2(350 CHAR),
    var_field_7                   VARCHAR2(350 CHAR),
    var_field_8                   VARCHAR2(350 CHAR),
    var_field_9                   VARCHAR2(350 CHAR),
    var_field_10                  VARCHAR2(350 CHAR),
    created_by                    VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on                    TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by              VARCHAR2(100 CHAR),
    last_modified_on              TIMESTAMP(6) WITH TIME ZONE,
    update_by                     VARCHAR2(100 CHAR),
    updated_on                    TIMESTAMP(6) WITH TIME ZONE,
    delete_by                     VARCHAR2(100 CHAR),
    deleted_on                    TIMESTAMP(6) WITH TIME ZONE,
    VERSION                       NUMBER
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


create sequence DAT_KEBS_MS_COMPLAINT_ACCEPT_WORK_FLOW_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_ACCEPT_WORK_FLOW_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_ACCEPT_WORK_FLOW
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_ACCEPT_WORK_FLOW_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_COMPLAINT_WITNESS
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID     number references DAT_KEBS_MS_COMPLAINT (ID),
    FIRST_NAME       VARCHAR2(1350 CHAR),
    LAST_NAME        VARCHAR2(1350 CHAR),
    PHONE            VARCHAR2(1350 CHAR),
    ID_NUMBER        VARCHAR2(1350 CHAR),
    EMAIL_ADDRESS    VARCHAR2(1350 CHAR),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_COMPLAINT_WITNESS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_WITNESS_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_WITNESS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_WITNESS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MS_COMPLAINT_COMPANY
(
    ID                  NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID        number references DAT_KEBS_MS_COMPLAINT (ID),
    name_of_outlet      VARCHAR2(1350 CHAR),
    business_address    VARCHAR2(1350 CHAR),
    telePHONE_number    VARCHAR2(1350 CHAR),
    mobile_PHONE_number VARCHAR2(1350 CHAR),
    EMAIL_ADDRESS       VARCHAR2(1350 CHAR),
    PHYSICAL_ADDRESS    VARCHAR2(1350 CHAR),
    TRANSACTION_DATE    DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status              NUMBER(2, 0),
    REMARKS             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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


create sequence DAT_KEBS_MS_COMPLAINT_COMPANY_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_COMPANY_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_COMPANY
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_COMPANY_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

ALTER TABLE DAT_KEBS_MS_COMPLAINT_WITNESS
    ADD POSTAL_ADDRESS VARCHAR2(1350 CHAR);

create table DAT_KEBS_MS_COMPLAINT_CUSTOMERS
(
    ID                  NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID        number references DAT_KEBS_MS_COMPLAINT (ID),
    FIRST_NAME          VARCHAR2(1350 CHAR),
    LAST_NAME           VARCHAR2(1350 CHAR),
    MOBILE_PHONE_NUMBER VARCHAR2(1350 CHAR),
    ID_NUMBER           VARCHAR2(1350 CHAR),
    EMAIL_ADDRESS       VARCHAR2(1350 CHAR),
    POSTAL_ADDRESS      VARCHAR2(1350 CHAR),
    TRANSACTION_DATE    DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status              NUMBER(2, 0),
    REMARKS             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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


create sequence DAT_KEBS_MS_COMPLAINT_CUSTOMERS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_CUSTOMERS_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_CUSTOMERS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_CUSTOMERS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MS_REPORT_OBSERVATIONS
(
    ID                    NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID          number references DAT_KEBS_MS_COMPLAINT (ID),
    PRODUCT_TYPE          VARCHAR2(1350 CHAR),
    PRODUCT_BRAND         VARCHAR2(1350 CHAR),
    PRODUCT_NAME          VARCHAR2(1350 CHAR),
    COUNTRY_OF_SUPPLY     VARCHAR2(1350 CHAR),
    INSPECTION_COMPLIANCE VARCHAR2(1350 CHAR),
    MEASUREMENT_RESULTS   VARCHAR2(1350 CHAR),
    TRANSACTION_DATE      DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status                NUMBER(2, 0),
    REMARKS               VARCHAR2(3800 CHAR),
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
    created_by            VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on            TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by      VARCHAR2(100 CHAR),
    last_modified_on      TIMESTAMP(6) WITH TIME ZONE,
    update_by             VARCHAR2(100 CHAR),
    updated_on            TIMESTAMP(6) WITH TIME ZONE,
    delete_by             VARCHAR2(100 CHAR),
    deleted_on            TIMESTAMP(6) WITH TIME ZONE,
    VERSION               NUMBER
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


create sequence DAT_KEBS_MS_REPORT_OBSERVATIONS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_REPORT_OBSERVATIONS_trg
    before
        insert
    on DAT_KEBS_MS_REPORT_OBSERVATIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_REPORT_OBSERVATIONS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


select *
from APOLLO.CFG_SERVICE_MAPS;

alter table DAT_KEBS_MS_COMPLAINT_OFFICER
    add transaction_date date default sysdate not null;
alter table DAT_KEBS_MS_COMPLAINT_DOCUMENTS
    add transaction_date date default sysdate not null;
alter table DAT_KEBS_MS_COMPLAINT_REMARKS
    add transaction_date date default sysdate not null;



create table DAT_KEBS_MS_FUEL_INSPECTION
(
    ID                        NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_date           date,
    company                   VARCHAR2(1350 CHAR),
    petroleum_product         VARCHAR2(1350 CHAR),
    physical_location         VARCHAR2(1350 CHAR),
    inspection_request_source VARCHAR2(1350 CHAR),
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


create sequence DAT_KEBS_MS_FUEL_INSPECTION_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_INSPECTION_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_INSPECTION
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_INSPECTION_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_INSPECTION_RESULTS
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id    number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    results_date     date,
    results          VARCHAR2(3350 CHAR),
    recommendation   VARCHAR2(3350 CHAR),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_FUEL_INSPECTION_RESULTS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_INSPECTION_RESULTS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_INSPECTION_RESULTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_INSPECTION_RESULTS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MS_FUEL_REMEDY_INVOICES
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id    number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    AMOUNT           NUMBER(30, 2),
    INVOICE_date     date,
    PAYMENT_date     date,
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_FUEL_REMEDY_INVOICES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create OR REPLACE trigger DAT_KEBS_MS_FUEL_REMEDY_INVOICES_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_REMEDY_INVOICES
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_REMEDY_INVOICES_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id    number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    OFFICER          VARCHAR2(1350 CHAR),
    DESIGNATION      VARCHAR2(1350 CHAR),
    PARTY            VARCHAR2(350 CHAR),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_REPORT_WORKFLOWS
(
    ID                  NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id       number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    REPORT              VARCHAR2(3350 CHAR),
    KEBS_OFFICER        VARCHAR2(1350 CHAR),
    OFFICER_DESIGNATION VARCHAR2(1350 CHAR),
    PREPARATION_DATE    TIMESTAMP WITH TIME ZONE,
    SUBMISSION_DATE     TIMESTAMP WITH TIME ZONE,
    TRANSACTION_DATE    DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status              NUMBER(2, 0),
    REMARKS             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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


create sequence DAT_KEBS_MS_FUEL_REPORT_WORKFLOWS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_REPORT_WORKFLOWS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_REPORT_WORKFLOWS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_REPORT_WORKFLOWS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_INSPECTION_VISITS
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id    number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    company          VARCHAR2(3350 CHAR),
    KEBS_OFFICER     VARCHAR2(1350 CHAR),
    location         VARCHAR2(1350 CHAR),
    visit_DATE       TIMESTAMP WITH TIME ZONE,
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_FUEL_INSPECTION_VISITS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_INSPECTION_VISITS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_INSPECTION_VISITS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_INSPECTION_VISITS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS
(
    ID                  NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id       number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    KEBS_OFFICER        VARCHAR2(1350 CHAR),
    OFFICER_DESIGNATION VARCHAR2(1350 CHAR),
    CURRENT_APPROVER    VARCHAR2(1350 CHAR),
    APPROVER_EMAIL      VARCHAR2(1350 CHAR),
    APPROVAL            VARCHAR2(1350 CHAR),
    APPROVAL_STATUS     VARCHAR2(50 CHAR),
    TRANSACTION_DATE    DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status              NUMBER(2, 0),
    REMARKS             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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

RENAME DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS TO DAT_KEBS_MS_FUEL_REPORT_APPROVALS;
RENAME DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS_SEQ TO DAT_KEBS_MS_FUEL_REPORT_APPROVALS_SEQ;
RENAME DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS_TRG TO DAT_KEBS_MS_FUEL_REPORT_APPROVALS_TRG;


create sequence DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_REPORT_APPROVALS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_REPORT_APPROVALS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_REPORT_APPROVALS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table DAT_KEBS_MS_FUEL_REPORT_APPROVALS
(
    ID                  NUMBER                                             NOT NULL PRIMARY KEY,
    inspection_id       number references DAT_KEBS_MS_FUEL_INSPECTION (id),
    KEBS_OFFICER        VARCHAR2(1350 CHAR),
    OFFICER_DESIGNATION VARCHAR2(1350 CHAR),
    CURRENT_APPROVER    VARCHAR2(1350 CHAR),
    APPROVER_EMAIL      VARCHAR2(1350 CHAR),
    APPROVAL            VARCHAR2(1350 CHAR),
    APPROVAL_STATUS     VARCHAR2(50 CHAR),
    TRANSACTION_DATE    DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status              NUMBER(2, 0),
    REMARKS             VARCHAR2(3800 CHAR),
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
    created_by          VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by    VARCHAR2(100 CHAR),
    last_modified_on    TIMESTAMP(6) WITH TIME ZONE,
    update_by           VARCHAR2(100 CHAR),
    updated_on          TIMESTAMP(6) WITH TIME ZONE,
    delete_by           VARCHAR2(100 CHAR),
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    VERSION             NUMBER
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


create sequence DAT_KEBS_MS_FUEL_REPORT_APPROVALS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS_TRG
    before
        insert
    on DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_FUEL_FUEL_REPORT_APPROVALS_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


alter table APOLLO.CFG_SERVICE_MAPS
    add PASSWORD_LENGTH number(2, 0) default 8 not null;

select *
from DAT_KEBS_USERS;

alter table DAT_KEBS_MANUFACTURERS
    add company_email varchar2(350 char);
alter table DAT_KEBS_MANUFACTURERS
    add postal_address varchar2(350 char);
alter table DAT_KEBS_MANUFACTURERS
    add company_telephone varchar2(350 char);



create table DAT_KEBS_MS_COMPLAINT_LOCATION
(
    ID               NUMBER                                             NOT NULL PRIMARY KEY,
    COMPLAINT_ID     number references DAT_KEBS_MS_COMPLAINT (ID),
    TOWN             VARCHAR2(1350 CHAR),
    MARKET_CENTER    VARCHAR2(1350 CHAR),
    BUILDING         VARCHAR2(1350 CHAR),
    PRODUCT_BRAND    VARCHAR2(1350 CHAR),
    TRANSACTION_DATE DATE                        DEFAULT TRUNC(SYSDATE) NOT NULL,
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin'        NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate        NOT NULL ENABLE,
    last_modified_by VARCHAR2(100 CHAR),
    last_modified_on TIMESTAMP(6) WITH TIME ZONE,
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
;


create sequence DAT_KEBS_MS_COMPLAINT_LOCATION_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MS_COMPLAINT_LOCATION_trg
    before
        insert
    on DAT_KEBS_MS_COMPLAINT_LOCATION
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MS_COMPLAINT_LOCATION_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from DAT_KEBS_MS_COMPLAINT;

select *
from APOLLO.DAT_KEBS_USERS;--54

select *
from DAT_KEBS_USER_PROFILES;

select *
from CFG_KEBS_REGIONS;--2

select *
from CFG_KEBS_DESIGNATIONS
where id = 1;--1

select *
from CFG_KEBS_SECTIONS;--5
commit;

select *
from CFG_USER_ROLES_PRIVILEGES;

select *
from CFG_USER_PRIVILEGES;

select *
from DAT_KEBS_USER_PROFILES
where user_id = 54
  and status = 1;

select *
from DAT_KEBS_USER_PROFILES userprofil0_
         left join DAT_KEBS_USERS usersentit1_ on userprofil0_.user_id = usersentit1_.ID
-- where usersentit1_.ID = 54
-- where p.user_id = u.ID
;

select *
from DAT_KEBS_USER_PROFILES p
-- where p.USER_ID = 54
--   and p.STATUS = 1
;

commit;



create table REF_TITLES
(
    id               number                                      not null primary key,
    title            varchar2(50 char),
    status           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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


create sequence REF_TITLES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger REF_TITLES_trg
    before
        insert
    on REF_TITLES
    for each row
begin
    if inserting then
        if :new.id is null then
            select REF_TITLES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


-- alter table DAT_KEBS_EMPLOYEES rename column userid to user_id;

alter table DAT_KEBS_EMPLOYEES
    add foreign key (USER_ID) references DAT_KEBS_USERS (id);

alter table DAT_KEBS_EMPLOYEES
    modify DEPARTMENT NUMBER;

select *
from DAT_KEBS_EMPLOYEES;

select *
from CFG_SERVICE_MAPS
order by ID;

commit;

select *
from REF_TITLES;

select id, DESIGNATION_NAME
from CFG_KEBS_DESIGNATIONS
where STATUS = 1;

select *
from DAT_KEBS_EMPLOYEES
order by ID;

select *
from DAT_KEBS_USER_PROFILES;

select *
from DAT_KEBS_USERS
order by id;

select *
from LOG_SERVICE_REQUESTS
order by ID;

select LOG_SERVICE_REQUESTS_SEQ.nextval
from dual;

select *
from DAT_KEBS_MANUFACTURERS;

select *
from DAT_KEBS_MANUFACTURER_ADDRESSES;

select *
from DAT_KEBS_MANUFACTURER_CONTACTS;

alter table CFG_SERVICE_MAPS
    add primary key (id);

alter table CFG_SERVICE_MAPS_WORKFLOWS
    add foreign key (SERVICE_MAPS_ID) references CFG_SERVICE_MAPS;

alter table DAT_KEBS_USERS
    modify APPROVED_DATE null;

alter table DAT_KEBS_MANUFACTURERS
    modify ENTRY_NUMBER null;

select *
from CFG_SERVICE_MAPS
order by ID;

select *
from CFG_SERVICE_MAPS_WORKFLOWS
where SERVICE_MAPS_ID in (101, 124);

alter table DAT_KEBS_MANUFACTURER_CONTACTS
    drop column DELETED_ON;

alter table DAT_KEBS_MANUFACTURER_CONTACTS
    add DELETED_ON TIMESTAMP with time zone;



create table DAT_KEBS_USER_VERIFICATION_TOKEN
(
    id                number                                      not null primary key,
    user_id           number references DAT_KEBS_USERS (ID),
    token             varchar2(350 char),
    token_expiry_date timestamp with time zone,
    transaction_Date  date                        default sysdate not null,
    STATUS            NUMBER(2, 0),
    REMARKS           VARCHAR2(3800 CHAR),
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


create sequence DAT_KEBS_USER_VERIFICATION_TOKEN_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_USER_VERIFICATION_TOKEN_trg
    before
        insert
    on DAT_KEBS_USER_VERIFICATION_TOKEN
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_USER_VERIFICATION_TOKEN_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table CFG_SERVICE_MAPS
    add TOKEN_EXPIRY_HOURS number default 24 not null;

select *
from LOG_SERVICE_REQUESTS
where NOTIFICATION_TYPE is not null;

select *
from DAT_KEBS_NOTIFICATIONS
where id = 30;

create unique index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS on DAT_KEBS_USER_VERIFICATION_TOKEN (USER_ID, STATUS) TABLESPACE qaimssdb_idx;
create unique index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS on DAT_KEBS_USER_VERIFICATION_TOKEN (TOKEN, STATUS) TABLESPACE qaimssdb_idx;

select *
from DAT_KEBS_USERS;

select *
from CFG_USER_TYPES;

commit;

-- select * from CFG_USER_ROLES_PRIVILEGES;
--
-- alter table CFG_USER_ROLES_PRIVILEGES drop column USER_ID;
--
-- alter table CFG_USER_ROLES_PRIVILEGES rename to CFG_ROLES_PRIVILEGES;

create table cfg_user_roles_assignments
(
    id               number                                      not null primary key,
    user_id          number references DAT_KEBS_USERS (ID),
    role_id          number references CFG_USER_ROLES (ID),
    STATUS           NUMBER(2, 0),
    REMARKS          VARCHAR2(3800 CHAR),
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



create sequence cfg_user_roles_assignments_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_roles_assignments_trg
    before
        insert
    on cfg_user_roles_assignments
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_roles_assignments_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from CFG_USER_ROLES;

select *
from CFG_USER_TYPES;

alter table CFG_USER_TYPES
    add default_role number references CFG_USER_ROLES (ID);

commit;

select *
from CFG_NOTIFICATION_TYPES;
select *
from CFG_NOTIFICATIONS;

alter table CFG_NOTIFICATIONS
    add foreign key (NOTIFICATION_TYPE) references CFG_NOTIFICATION_TYPES (ID);



select SERVICE_TOPIC
from CFG_SERVICE_MAPS
where id = 127
order by ID;

update cfg_service_maps
set employee_user_type    = 5,
    manufacturer_user_type=4
where employee_user_type is null;


update CFG_SERVICE_MAPS
set OBJECT_MAPPED_TO ='serviceRequestProcessingActor'
where ACTOR_CLASS = 'ServiceRequestProcessingActor';
update CFG_SERVICE_MAPS
set OBJECT_MAPPED_TO   ='UsersEntity',
    OBJECT_MAPPED_FROM ='UsersEntity'
where id in (127, 128);

commit;



commit;

select *
from DAT_KEBS_USERS
where id = 101
ORDER BY ID;

select *
from LOG_SERVICE_REQUESTS
where id = 191
order by ID;

select *
from CFG_SERVICE_MAPS_WORKFLOWS
order by ID;

select *
from CFG_NOTIFICATIONS;



select *
from LOG_WORKFLOW_TRANSACTIONS
--where SERVICE_REQUEST = 184
order by id
;

select *
from CRM_SERVICE_MAPS_WORKFLOWS_FUNCTIONS_VW
where SERVICE_MAP = 128
;

select LOG_WORKFLOW_TRANSACTIONS_SEQ.nextval
from dual;


select *
from DAT_KEBS_USER_VERIFICATION_TOKEN;

update DAT_KEBS_USER_VERIFICATION_TOKEN
set USER_ID = 101
where id = 4;

commit;


create sequence CFG_SERVICE_MAPS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 130 cache 20 noorder nocycle;

create trigger cfg_service_maps_trg
    before
        insert
    on cfg_service_maps
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_service_maps_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table dat_kebs_ms_charge_sheet
(
    id                           NUMBER PRIMARY KEY,
    christian_name               VARCHAR2(20),
    surname                      VARCHAR2(20),
    sex                          VARCHAR2(10),
    nationality                  VARCHAR2(20),
    age                          NUMBER,
    address_district             VARCHAR2(20),
    address_location             VARCHAR2(20),
    first_count                  VARCHAR2(20),
    particulars_offence_one      VARCHAR2(200),
    second_count                 VARCHAR2(20),
    particulars_offence_second   VARCHAR2(200),
    date_arrest                  Date,
    with_warrant                 VARCHAR2(20),
    application_made_summons_sue VARCHAR2(200),
    Date_apprehension_court      Date,
    bond_bail_amount             NUMBER,
    remanded_adjourned           VARCHAR2(200),
    complainant_name             VARCHAR2(200),
    complainant_address          VARCHAR2(200),
    prosecutor                   VARCHAR2(200),
    witnesses                    VARCHAR2(200),
    sentence                     VARCHAR2(200),
    fine_paid                    VARCHAR2(200),
    court_name                   VARCHAR2(200),
    court_date                   Date,
    status                       VARCHAR2(20),
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
    modified_by                  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                   TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_ms_charge_sheet_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

drop  trigger dat_kebs_ms_charge_sheet_seq_trg;

create trigger dat_kebs_ms_charge_sheet_seq_trg
    before
        insert
    on dat_kebs_cd_inspection_scheduled_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_ms_charge_sheet_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_ms_charge_sheet_details_idx on dat_kebs_ms_charge_sheet (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_ms_data_report
(
    id                             NUMBER PRIMARY KEY,
    reference_number               VARCHAR2(20),
    inspection_date                Date,
    inspector_name                 VARCHAR2(20),
    function                       VARCHAR2(20),
    department                     VARCHAR2(20),
    region                         VARCHAR2(20),
    town                           VARCHAR2(20),
    market_center                  VARCHAR2(20),
    outlet_details                 VARCHAR2(200),
    person_met                     VARCHAR2(20),
    summary_findings_actions_taken VARCHAR2(500),
    final_action_seized_goods      VARCHAR2(20),
    status                         VARCHAR2(20),
    var_field_1                    VARCHAR2(350 CHAR),
    var_field_2                    VARCHAR2(350 CHAR),
    var_field_3                    VARCHAR2(350 CHAR),
    var_field_4                    VARCHAR2(350 CHAR),
    var_field_5                    VARCHAR2(350 CHAR),
    var_field_6                    VARCHAR2(350 CHAR),
    var_field_7                    VARCHAR2(350 CHAR),
    var_field_8                    VARCHAR2(350 CHAR),
    var_field_9                    VARCHAR2(350 CHAR),
    var_field_10                   VARCHAR2(350 CHAR),
    created_by                     VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                    TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                     TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_ms_data_report_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

drop  trigger dat_kebs_ms_data_report_seq_trg;

create trigger dat_kebs_ms_data_report_seq_trg
    before
        insert
    on  dat_kebs_ms_data_report
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_ms_data_report_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_ms_data_report_idx on dat_kebs_ms_data_report (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_ms_data_report_parameters
(
    id                             NUMBER PRIMARY KEY,
    data_report_id               NUMBER REFERENCES DAT_KEBS_MS_DATA_REPORT(id),
    type_brand_name                VARCHAR2(200),
    local_import                 VARCHAR2(20),
    compliance_inspection_parameter                       NUMBER,
    measurements_results                     VARCHAR2(200),
    remarks                         VARCHAR2(20),
    status                         VARCHAR2(20),
    var_field_1                    VARCHAR2(350 CHAR),
    var_field_2                    VARCHAR2(350 CHAR),
    var_field_3                    VARCHAR2(350 CHAR),
    var_field_4                    VARCHAR2(350 CHAR),
    var_field_5                    VARCHAR2(350 CHAR),
    var_field_6                    VARCHAR2(350 CHAR),
    var_field_7                    VARCHAR2(350 CHAR),
    var_field_8                    VARCHAR2(350 CHAR),
    var_field_9                    VARCHAR2(350 CHAR),
    var_field_10                   VARCHAR2(350 CHAR),
    created_by                     VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                    TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                     TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_ms_data_report_parameters_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_ms_data_report_parameters_seq_trg
    before
        insert
    on dat_kebs_ms_data_report_parameters
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_ms_data_report_parameters_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_ms_data_report_parameters_idx on dat_kebs_ms_data_report_parameters (data_report_id, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_ms_inspection_investigation_report
(
    id                            NUMBER PRIMARY KEY,
    report_reference              VARCHAR2(20),
    report_to                     VARCHAR2(20),
    report_through                VARCHAR2(20),
    report_from                   VARCHAR2(20),
    report_subject                VARCHAR2(20),
    report_title                  VARCHAR2(20),
    report_date                   date,
    report_region                 VARCHAR2(20),
    report_department             VARCHAR2(20),
    report_function               VARCHAR2(20),
    background_information        VARCHAR2(500),
    objective_investigation       VARCHAR2(500),
    date_investigation_inspection DATE,
    KEBS_inspectors               VARCHAR2(500),
    methodology_employed          VARCHAR2(500),
    conclusion                    VARCHAR2(500),
    recommendations               VARCHAR2(500),
    status_activity               VARCHAR2(100),
    final_remark_HOD              VARCHAR2(100),
    status                        VARCHAR2(20),
    var_field_1                   VARCHAR2(350 CHAR),
    var_field_2                   VARCHAR2(350 CHAR),
    var_field_3                   VARCHAR2(350 CHAR),
    var_field_4                   VARCHAR2(350 CHAR),
    var_field_5                   VARCHAR2(350 CHAR),
    var_field_6                   VARCHAR2(350 CHAR),
    var_field_7                   VARCHAR2(350 CHAR),
    var_field_8                   VARCHAR2(350 CHAR),
    var_field_9                   VARCHAR2(350 CHAR),
    var_field_10                  VARCHAR2(350 CHAR),
    created_by                    VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on                    TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by                   VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on                   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                     VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on                    TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_ms_inspection_investigation_report_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;


create trigger dat_kebs_ms_inspection_investigation_report_seq_trg
    before
        insert
    on dat_kebs_ms_inspection_investigation_report
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_ms_inspection_investigation_report_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_ms_inspection_investigation_report_idx on dat_kebs_ms_data_report (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_ms_onsite_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    DESCRIPTION      VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    WORKPLAN_ID      NUMBER REFERENCES DAT_KEBS_MS_WORKPLAN_GENARATED (ID),
    status           VARCHAR2(20),
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

create sequence dat_kebs_ms_onsite_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_ms_onsite_uploads_seq_trg
    before
        insert
    on dat_kebs_ms_onsite_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_ms_onsite_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_ms_onsite_uploads_idx on dat_kebs_ms_onsite_uploads (WORKPLAN_ID, status) TABLESPACE qaimssdb_idx;
/