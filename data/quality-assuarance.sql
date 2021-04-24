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
***************************Table USED IN QA*****************************************
select * from DAT_KEBS_PERMIT_TRANSACTION

order by id desc;


alter table DAT_KEBS_PERMIT_TRANSACTION
    add  PRODUCT_STANDARD                          NUMBER
        references CFG_SAMPLE_STANDARDS(ID)
;/





select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
where TOKEN =
;
select * from CFG_ROLES_PRIVILEGES
order by id desc;

select * from CFG_USER_ROLES
-- where  = 1393
order by id desc;

select * from CFG_USER_PRIVILEGES
-- where id like '%18%'
order by id desc;--MS_MP_MODIFY MS_MP_READ AUTHORITIES_WRITE 29
select *
from CFG_ROLES_PRIVILEGES
-- where ROLES_ID = -1
order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from CFG_USER_ROLES_ASSIGNMENTS
where USER_ID = 1393
order by id desc;--MS_MP_MODIFY MS_MP_READ
select *
from DAT_KEBS_USER_VERIFICATION_TOKEN
order by id desc;--MS_MP_MODIFY MS_MP_READ

select * from DAT_KEBS_USERS
-- where USER_TYPE = 5
where EMAIL = '254safaris@gmail.com'
order by id desc;

select *
from DAT_KEBS_USER_PROFILES
-- where USER_ID = 1403
where email = '254safaris@gmail.com'
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
where ID = 208
-- where  UUID = '266f9769-0a28-491c-8e05-aee12e9db5d0'
order by id desc;

select *
from CFG_NOTIFICATIONS--122
where SERVICE_MAP_ID = 127
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

create unique index DAT_KEBS_USER_VERIFICATION_TOKEN_USER_ID_STATUS on DAT_KEBS_USER_VERIFICATION_TOKEN (USER_ID, STATUS)

 *******************************************Created Tables USED IN DI********************************


create table dat_kebs_qa_sta3
(
    ID                       NUMBER  primary key,
    permit_id               NUMBER references DAT_KEBS_PERMIT_TRANSACTION(ID),
    produce_Orders_or_Stock  VARCHAR2(500),
    issue_work_order_or_equivalent  VARCHAR2(500),
    identify_batch_as_separate  VARCHAR2(500),
    products_containers_carry_works_order  VARCHAR2(500),
    isolated_case_doubtful_quality  VARCHAR2(500),
    head_qa_qualifications_training  VARCHAR2(500),
    reporting_to  VARCHAR2(500),
    separate_qcid  VARCHAR2(500),
    tests_relevant_standard VARCHAR2(500),
    spo_coming_materials VARCHAR2(500),
    spo_process_operations VARCHAR2(500),
    spo_final_products VARCHAR2(500),
    monitored_qcs VARCHAR2(500),
    qaudit_checks_carried VARCHAR2(500),
    information_qcso VARCHAR2(500),
    main_materials_purchased_specification VARCHAR2(500),
    adopted_receipt_materials VARCHAR2(500),
    storage_facilities_exist VARCHAR2(500),
    steps_manufacture VARCHAR2(500),
    maintenance_system VARCHAR2(500),
    qcs_supplement VARCHAR2(500),
    qm_instructions VARCHAR2(500),
    test_equipment_used VARCHAR2(500),
    indicate_external_arrangement VARCHAR2(500),
    level_defectives_found VARCHAR2(500),
    level_claims_complaints VARCHAR2(500),
    independent_tests  VARCHAR2(500),
    indicate_stage_manufacture  VARCHAR2(500),
    DESCRIPTION              VARCHAR2(200),
    status                   NUMBER(2),
    var_field_1              VARCHAR2(350 CHAR),
    var_field_2              VARCHAR2(350 CHAR),
    var_field_3              VARCHAR2(350 CHAR),
    var_field_4              VARCHAR2(350 CHAR),
    var_field_5              VARCHAR2(350 CHAR),
    var_field_6              VARCHAR2(350 CHAR),
    var_field_7              VARCHAR2(350 CHAR),
    var_field_8              VARCHAR2(350 CHAR),
    var_field_9              VARCHAR2(350 CHAR),
    var_field_10             VARCHAR2(350 CHAR),
    created_by               VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on               TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by              VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on              TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by                VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on               TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_qa_sta3_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_qa_sta3_seq_trg
    before
        insert
    on dat_kebs_qa_sta3
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_qa_sta3_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_qa_sta3_idx on dat_kebs_qa_sta3 (status, permit_id) TABLESPACE qaimssdb_idx;
/