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
from DAT_KEBS_USER_VERIFICATION_TOKEN
-- where TOKEN =
;

select *
from DAT_KEBS_NOTIFICATIONS
-- where TOKEN =
order by id desc;
select * from CFG_ROLES_PRIVILEGES --the role is 40 and prRole ID = 774
-- where PRIVILEGE_ID =52425
order by id desc;

select *
from CFG_KEBS_DESIGNATIONS --the role is 40 and prRole ID = 774
-- where PRIVILEGE_ID =52425
order by id desc;

select *
from CFG_KEBS_REGIONS --the role is 40 and prRole ID = 774
where id = 4
order by id desc;


select *
from LOG_BRS_LOOKUP_MANUFACTURER_PARTNERS;

select *
from DAT_KEBS_COMPANY_PROFILE --the role is 40 and prRole ID = 774
-- where PRIVILEGE_ID =52425
order by id desc;

select *
from CFG_USER_ROLES--512
--where id like '%5%'
-- where id = 9
order by id desc;

select * from CFG_USER_PRIVILEGES--QA_OFFICER_READ
-- where id like '%521%'
order by id desc;--MS

-- _MP_MODIFY MS_MP_READ AUTHORITIES_WRITE 29
select *
from CFG_ROLES_PRIVILEGES
where ROLES_ID = 9

order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from CFG_USER_ROLES_ASSIGNMENTS
-- where USER_ID = 54
order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from DAT_KEBS_USER_VERIFICATION_TOKEN

order by id desc;--

select *
from DAT_KEBS_NOTIFICATIONS

order by id desc;--MS_MP_MODIFY MS_MP_READ

select *
from DAT_KEBS_USERS
-- where ID = 1622

-- where USER_PIN_ID_NUMBER = '13869968'
-- where EMAIL = 'omarh@kebs.org'
-- where USER_NAME = 'Muriithig'
-- where USER_PIN_ID_NUMBER = '0715668934'
order by id desc
;

select *
from DAT_KEBS_USER_PROFILES
where USER_ID = 1522

-- where USER_PIN_ID_NUMBER = '13869968'
-- where EMAIL = 'njinei@kebs.org'
-- where USER_PIN_ID_NUMBER = '0715668934'
order by id desc
;
SELECT *
from CFG_TURNOVER_RATES;

select *
from DAT_KEBS_COMPANY_PROFILE
-- where ID = 1622

-- where USER_TYPE = 5
-- where EMAIL like '254saf'
-- where USER_NAME = '0715668934'
order by id desc
;21384

select *
from DAT_KEBS_USER_PROFILES
-- where REGION_ID = 4 and DESIGNATION_ID = 304
where USER_ID = 1523
order by id desc;

alter table DAT_KEBS_USERS
    add TYPE_OF_USER NUMBER(2)
/


select *
from CFG_USER_TYPES
-- where STATUS = 1
order by ID desc;

select *
from dat_user_requests
-- where ID = 127
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from cfg_user_request_types
-- where ID = 127
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from CFG_SERVICE_MAPS
where ID = 127
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from CFG_NOTIFICATIONS--122
where SERVICE_MAP_ID = 208
--     where UUID = '1b474c60-c7f4-4ccb-9cce-efca7ead8439'
order by ID desc;


select *
from USER_CONSTRAINTS
where TABLE_NAME = 'DAT_KEBS_USER_VERIFICATION_TOKEN'
-- where CONSTRAINT_NAME = 'DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS'
;

select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
where TOKEN = '21790a243e60d20'
-- where USER_ID = 1391
;

select *
from LOG_JWT_TOKENS_REGISTRY
where USER_NAME = '254safaris@gmail.com'
;

SELECT DISTINCT CUP.*
FROM CFG_ROLES_PRIVILEGES rp,
     CFG_USER_ROLES cur,
     CFG_USER_PRIVILEGES cup
WHERE CUP.ID = rp.PRIVILEGE_ID
  AND CUR.ID = rp.ROLES_ID
  AND rp.ROLES_ID IN (-1)
  and rp.STATUS = 1;

drop index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_OD_STATUS;

create unique index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_ID_STATUS on DAT_KEBS_USER_VERIFICATION_TOKEN (USER_ID, STATUS) *******************************************Created Tables USED IN DI********************************


create table dat_user_requests
(
    ID                 NUMBER primary key,
    REQUEST_ID         NUMBER references CFG_USER_REQUEST_TYPES (id),
    USER_ID            NUMBER references DAT_KEBS_USERS (id),
    USER_ROLE_ASSIGNED NUMBER references CFG_USER_PRIVILEGES (id),
    REQUEST_STATUS     NUMBER(2),
    DESCRIPTION        VARCHAR2(200),
    status             NUMBER(2),
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
    modified_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by          VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on         TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_user_requests_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_user_requests_seq_trg
    before
        insert
    on dat_user_requests
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_user_requests_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_user_requests_idx on dat_user_requests (status, REQUEST_ID, USER_ID, REQUEST_STATUS) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_company_profile_commodities_manufacture
(
    ID                 NUMBER primary key,
    company_profile_ID      NUMBER references dat_kebs_company_profile (id),
    COMMODITY_NAME  VARCHAR2(200),
    COMMODITY_DATE_COMMENCE  DATE,
    DESCRIPTION        VARCHAR2(200),
    status             NUMBER(2),
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
    modified_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by          VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on         TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_company_profile_commodities_manufacture_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_company_profile_commodities_manufacture_seq_trg
    before
        insert
    on dat_user_requests
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_company_profile_commodities_manufacture_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_company_profile_commodities_manufacture_idx on dat_kebs_company_profile_commodities_manufacture (status, company_profile_ID) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_company_profile_contracts_undertaken
(
    ID                 NUMBER primary key,
    company_profile_ID      NUMBER references dat_kebs_company_profile (id),
    contracts_NAME  VARCHAR2(200),
    contracts_DATE_COMMENCE  DATE,
    DESCRIPTION        VARCHAR2(200),
    status             NUMBER(2),
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
    modified_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by          VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on         TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_company_profile_contracts_undertaken_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_company_profile_contracts_undertaken_seq_trg
    before
        insert
    on dat_user_requests
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_company_profile_contracts_undertaken_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_company_profile_contracts_undertaken_idx on dat_kebs_company_profile_contracts_undertaken (status, company_profile_ID) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_company_profile_directors
(
    ID                 NUMBER primary key,
    company_profile_ID      NUMBER references dat_kebs_company_profile (id),
    DIRECTOR_NAME  VARCHAR2(200),
    DIRECTOR_ID  VARCHAR2(200),
    DESCRIPTION        VARCHAR2(200),
    status             NUMBER(2),
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
    modified_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by          VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on         TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_company_profile_directors_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_company_profile_directors_seq_trg
    before
        insert
    on dat_user_requests
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_company_profile_directors_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_company_profile_directors_idx on dat_kebs_company_profile_directors (status, company_profile_ID) TABLESPACE qaimssdb_idx;
/

create table cfg_user_request_types
(
    ID           NUMBER primary key,
    USER_REQUEST VARCHAR2(200),
    DESCRIPTION  VARCHAR2(200),
    status       NUMBER(2),
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
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_user_request_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_user_request_types_seq_trg
    before
        insert
    on cfg_user_request_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_user_request_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_user_request_types_idx on cfg_user_request_types (status, USER_REQUEST) TABLESPACE qaimssdb_idx;
/


create table cfg_users_cfs_ASSIGNMENTS
(
    ID              NUMBER primary key,
    USER_PROFILE_ID NUMBER references DAT_KEBS_USER_PROFILES (id),
    CFS_ID          NUMBER references CFG_KEBS_SUB_SECTIONS_LEVEL2 (ID),
    DESCRIPTION     VARCHAR2(200),
    status          NUMBER(2),
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
    created_by      VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by     VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on     TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by       VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on      TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_users_cfs_ASSIGNMENTS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_users_cfs_ASSIGNMENTS_seq_trg
    before
        insert
    on cfg_users_cfs_ASSIGNMENTS
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_users_cfs_ASSIGNMENTS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_users_cfs_ASSIGNMENTS_idx on cfg_users_cfs_ASSIGNMENTS (status, USER_PROFILE_ID, CFS_ID) TABLESPACE qaimssdb_idx;
/
alter table DAT_KEBS_COMPANY_PROFILE rename column KRAPIN to KRA_PIN
/

alter table DAT_KEBS_COMPANY_PROFILE rename column REGISTRATIONNUMBER to REGISTRATION_NUMBER
/

alter table DAT_KEBS_COMPANY_PROFILE rename column POSTALADDRESS to POSTAL_ADDRESS
/

alter table DAT_KEBS_COMPANY_PROFILE rename column COMPANYEMAIL to COMPANY_EMAIL
/

alter table DAT_KEBS_COMPANY_PROFILE rename column COMPANYTELEPHONE to COMPANY_TELEPHONE
/

alter table DAT_KEBS_COMPANY_PROFILE rename column YEARLYTURNOVER to YEARLY_TURNOVER
/

alter table DAT_KEBS_COMPANY_PROFILE rename column BUSINESSLINES to BUSINESS_LINES
/

alter table DAT_KEBS_COMPANY_PROFILE rename column BUSINESSNATURES to BUSINESS_NATURES
/

alter table DAT_KEBS_COMPANY_PROFILE rename column BUILDINGNAME to BUILDING_NAME
/

alter table DAT_KEBS_COMPANY_PROFILE rename column STREETNAME to STREET_NAME
/


create table dat_kebs_company_profile
(
    ID                 NUMBER primary key,
    name               VARCHAR2(200),
    kraPin             VARCHAR2(200),
    registrationNumber VARCHAR2(200),
    postalAddress      VARCHAR2(200),
    companyEmail       VARCHAR2(200),
    companyTelephone   VARCHAR2(200),
    yearlyTurnover     VARCHAR2(200),
    businessLines      NUMBER references CFG_KEBS_BUSINESS_LINES (ID),
    businessNatures    NUMBER references CFG_KEBS_BUSINESS_NATURE (ID),
    buildingName       VARCHAR2(200),
    streetName         VARCHAR2(200),
    region             NUMBER references CFG_KEBS_REGIONS (ID),
    county             NUMBER references CFG_KEBS_COUNTIES (ID),
    town               NUMBER references CFG_KEBS_TOWNS (ID),
    DESCRIPTION        VARCHAR2(200),
    status             NUMBER(2),
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
    modified_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on        TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by          VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on         TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_company_profile_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_company_profile_seq_trg
    before
        insert
    on dat_kebs_company_profile
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_company_profile_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_company_profile_idx on dat_kebs_company_profile (status, kraPin,
                                                                       registrationNumber,
                                                                       businessLines,
                                                                       businessNatures,
                                                                       region,
                                                                       county,
                                                                       town) TABLESPACE qaimssdb_idx;
/