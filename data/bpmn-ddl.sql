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

select *
from ACT_RE_MODEL;
select *
from ACT_RE_DEPLOYMENT
order by version_;
select *
from ACT_RE_PROCDEF;

select *
from ACT_EVT_LOG;

select *
from ACT_PROCDEF_INFO;

alter table CFG_SERVICE_MAPS
    add role_name varchar2(350 char);

select *
from CFG_NOTIFICATIONS;

select *
from DAT_KEBS_NOTIFICATIONS
order by id;

alter table DAT_KEBS_NOTIFICATIONS
    add transaction_reference varchar2(100 char);

select *
from DAT_KEBS_MS_COMPLAINT;

select *
from CFG_USER_ROLES_ASSIGNMENTS;

select *
from CFG_USER_ROLES;

select *
from DAT_KEBS_USERS;

commit;

update CFG_SERVICE_MAPS
set ROLE_NAME ='QA Officer'
where id = 127;

alter table CFG_SERVICE_MAPS
    add bpmn_process_key varchar2(350 char) default 'complainManagement';

select *
from CFG_SERVICE_MAPS
order by ID;

alter table CFG_SERVICE_MAPS
    modify service_topic varchar2(350 char);

commit;

select CFG_SERVICE_MAPS_SEQ.nextval
from dual;

select *
from CFG_NOTIFICATIONS
where SERVICE_MAP_ID >= 127;

commit;


select *
from USER_SEQUENCES
where SEQUENCE_NAME = 'CFG_NOTIFICATIONS_SEQ';

alter table DAT_KEBS_MS_COMPLAINT
    add service_maps_id number references CFG_SERVICE_MAPS (id);
alter table DAT_KEBS_MS_COMPLAINT
    add reference_number varchar2(350 char) unique;
alter table CFG_SERVICE_MAPS
    add transaction_ref_prefix varchar2(20 char);

commit;


create table CFG_APPROVAL_STATUS
(
    id               number                                      not null primary key,
    approval_status  varchar2(20 char),
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



create sequence CFG_APPROVAL_STATUS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_APPROVAL_STATUS_trg
    before
        insert
    on CFG_APPROVAL_STATUS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_APPROVAL_STATUS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


select *
from CFG_APPROVAL_STATUS
order by id;

select *
from DAT_KEBS_NOTIFICATIONS
order by id;


select *
from CFG_USER_TYPES;

select *
from ACT_RE_PROCDEF;

select *
from CFG_SERVICE_MAPS
-- where id = 129
order by ID
;

-- ActivityId=emailComplaint id=4a22d68f-bba0-11ea-958f-0228f8280ece ProcessInstanceId=4a14579b-bba0-11ea-958f-0228f8280ece
-- ActivityId=emailComplaint id=9cfb43b0-bba2-11ea-8ab6-0228f8280ece ProcessInstanceId=9ceaeffc-bba2-11ea-8ab6-0228f8280ece

select *
from ACT_RU_TASK
where EXECUTION_ID_ = '4a22d68f-bba0-11ea-958f-0228f8280ece'
;


select *
from APOLLO.ACT_RE_PROCDEF;

select *
from DAT_KEBS_MS_COMPLAINT;


select *
from ACT_RU_TASK;

select *
from ACT_RE_PROCDEF;

select *
--        count(*), BUSINESS_KEY_
from ACT_HI_PROCINST
where business_key_ = '33404fc8a281310'
-- group by BUSINESS_KEY_
;

commit;

select *
from ACT_HI_TASKINST
where PROC_INST_ID_ = '0767bad3-e190-11ea-a126-f8b46aa764f2'
;


select *
from ACT_HI_TSK_LOG;
-- a5fa3def-c048-11ea-8697-0228f8280ece
-- a5f9a1ab-c048-11ea-8697-0228f8280ece
-- a5fcd605-c048-11ea-8697-0228f8280ece


select *
from ACT_RU_ACTINST
where PROC_INST_ID_ = '0767bad3-e190-11ea-a126-f8b46aa764f2'
order by START_TIME_
;

select *
from DAT_KEBS_NOTIFICATIONS
order by ID;

select *
from ACT_RU_TASK
where PROC_INST_ID_ = '0767bad3-e190-11ea-a126-f8b46aa764f2'
;

select *
from ACT_RU_JOB;

select *
from ACT_RU_VARIABLE
where PROC_INST_ID_ = '5d2bb5bc-c2c6-11ea-b577-0228f8280ece'
;

select *
from ACT_HI_VARINST
where PROC_INST_ID_ = '5d2bb5bc-c2c6-11ea-b577-0228f8280ece'
;

select count(*), PROC_INST_ID_
from ACT_RU_ACTINST
group by PROC_INST_ID_;


select BPMN_PROCESS_KEY
from CFG_SERVICE_MAPS
where id = 127
order by id;

commit;

select *
from CFG_USER_TYPES;

select *
from REF_TITLES;

select *
from CFG_KEBS_DEPARTMENTS;

select *
from DAT_KEBS_USERS;

select *
from DAT_KEBS_EMPLOYEES
order by ID;

select *
from DAT_KEBS_USER_PROFILES
order by ID;

select *
from LOG_SERVICE_REQUESTS
order by ID;

select LOG_WORKFLOW_TRANSACTIONS_SEQ.nextval
from dual;

ALTER SEQUENCE LOG_WORKFLOW_TRANSACTIONS_SEQ INCREMENT BY 300;



ALTER SEQUENCE LOG_WORKFLOW_TRANSACTIONS_SEQ INCREMENT BY 1;

select *
from ACT_HI_TSK_LOG;

select *
from ACT_HI_PROCINST;

select *
from ACT_RU_EXECUTION;

select *
from DAT_KEBS_USERS
order by id;

select *
from DAT_KEBS_NOTIFICATIONS
order by CREATED_ON;

select *
from CFG_NOTIFICATIONS
order by ID;

select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
order by id;

commit;

select *
from LOG_SERVICE_REQUESTS
where id >= 448;

select *
from CFG_SERVICE_MAPS
where id = 127;

select *
from ACT_HI_PROCINST
-- where BUSINESS_KEY_ = 'ebd67dcad8b8def'
;

select *
from act select *
from DAT_KEBS_USERS
where id = 64;

commit;

select *
from user_constraints
where constraint_name = 'SYS_C0016806'
;

select max(id)
from LOG_WORKFLOW_TRANSACTIONS;

select LOG_WORKFLOW_TRANSACTIONS_SEQ.nextval
from dual;

select *
from LOG_SERVICE_REQUESTS
-- where TRANSACTION_REFERENCE ='36570485c048967'
where id >= 448
order by ID
;

select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
-- where TOKEN = '36570485c048967'
;

select *
from DAT_KEBS_NOTIFICATIONS
order by ID;

commit;

select *
from CFG_SERVICE_MAPS;

select *
from DAT_KEBS_MANUFACTURERS
where USER_ID = 100;
select *
from DAT_KEBS_USERS
where id in (54, 100)
order by id;

select FACTORY_INSPECTION_REPORT_STATUS, USER_ID
from DAT_KEBS_PERMIT;

update CFG_SERVICE_MAPS
set UI_PAGE_SIZE = 10
where UI_PAGE_SIZE is null;

commit;



select 'DROP TABLE ' || table_name || ' CASCADE CONSTRAINTS;'
from user_tables
where substr(table_name, 1, 3) in ('ACT', 'FLW');