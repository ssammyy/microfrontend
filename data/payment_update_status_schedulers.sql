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
alter table DAT_KEBS_CD_DEMAND_NOTE
    modify (PAYMENT_STATUS number(2, 0));



select *
from CFG_INTEGRATION_CONFIGURATION
where ID = 25
-- where INVOICE_ID = 'DN2021040521BBB'
;

select *
from LOG_WORKFLOW_TRANSACTIONS
-- where ID = 25
-- where INVOICE_ID = 'DN2021040521BBB'
order by id desc;/

select *
from LOG_WORKFLOW_TRANSACTIONS
-- where ID = 25
-- where INVOICE_ID = 'DN2021040521BBB'
order by id desc;/

select *
from CFG_BATCH_JOB_DETAILS
where INTEGRATION_ID = 25
-- where INVOICE_ID = 'DN2021040521BBB'
;


select *
from DAT_KEBS_MPESA_TRANSACTION
-- where ID = 15
where INVOICE_ID = 'DN2021040521BBB'
;

select *
from DAT_KEBS_CD_DEMAND_NOTE
-- where ID = 802
where DEMAND_NOTE_NUMBER = 'DN2021042045B72'
;

select *
from STG_PAYMENT_RECONCILIATION --DN20210407C4C3D--DN20210331288A1
-- where REFERENCE_CODE = 'DM#202104290CF'
--                            ||
--                        'DN20210422E1513' ||
--                        'DN202104229B7D1'
-- where PAYMENT_TABLES_UPDATED_STATUS = 1
order by id desc;/

select *
from LOG_STG_PAYMENT_RECONCILIATION
where REFERENCE_CODE = 'DM#20210428DE4'
order by id desc;/

select * from DAT_KEBS_INVOICE_BATCH_DETAILS
where BATCH_NUMBER = 'DM#202104290CF'
order by id desc;/

/*Permit INVOICE*/
select * from DAT_KEBS_INVOICE
-- where id = 2
where INVOICE_NUMBER = 'DM#202104290CF'
order by id desc;


select FUN_UPDATE_INVOICE_WHEN_PAYMENTS_MADE(1)
from dual;

select count(*), PAYMENT_TABLES_UPDATED_STATUS
from STG_PAYMENT_RECONCILIATION
group by PAYMENT_TABLES_UPDATED_STATUS
;
/
create unique index STG_PAYMENT_RECONCILIATION_TRANSACTION_ID_UINDEX
    on STG_PAYMENT_RECONCILIATION (TRANSACTION_ID)
/

SELECT *
FROM STG_PAYMENT_RECONCILIATION s
where s.PAYMENT_TABLES_UPDATED_STATUS in (1);

alter table LOG_STG_PAYMENT_RECONCILIATION
    add REFERENCE_CODE VARCHAR2(350) not null
/

alter table LOG_STG_PAYMENT_RECONCILIATION modify TRANSACTION_ID not null
/

create unique index LOG_STG_PAYMENT_RECONCILIATION_TRANSACTION_ID_UINDEX
    on LOG_STG_PAYMENT_RECONCILIATION (TRANSACTION_ID)
/
****************************Created Tables USED IN DI********************************

create or replace procedure proc_update_invoice_when_payment_done(PAID_STATUS number)
as
begin
    -- We create a savepoint here
--     SAVEPOINT sp_transactions;
    insert into LOG_STG_PAYMENT_RECONCILIATION
    (PAYMENT_RECONCILIATION_ID, INVOICE_ID, REFERENCE_CODE, ACCOUNT_NAME,
     ACCOUNT_NUMBER, CURRENCY, STATUS_CODE, STATUS_DESCRIPTION,
     ADDITIONAL_INFORMATION, INVOICE_AMOUNT, PAID_AMOUNT, OUTSTANDING_AMOUNT,
     TRANSACTION_ID, TRANSACTION_DATE, CUSTOMER_NAME, PAYMENT_SOURCE, EXTRAS,
     INVOICE_DATE, DESCRIPTION, ACTUAL_AMOUNT, STATUS)
    SELECT id,
           INVOICE_ID,
           REFERENCE_CODE,
           ACCOUNT_NAME,
           ACCOUNT_NUMBER,
           CURRENCY,
           STATUS_CODE,
           STATUS_DESCRIPTION,
           ADDITIONAL_INFORMATION,
           INVOICE_AMOUNT,
           PAID_AMOUNT,
           OUTSTANDING_AMOUNT,
           TRANSACTION_ID,
           TRANSACTION_DATE,
           CUSTOMER_NAME,
           PAYMENT_SOURCE,
           EXTRAS,
           INVOICE_DATE,
           DESCRIPTION,
           ACTUAL_AMOUNT,
           STATUS
    from STG_PAYMENT_RECONCILIATION
    where PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS);

    FOR X IN (SELECT * FROM STG_PAYMENT_RECONCILIATION s where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS))
        LOOP
            case when (x.PAID_AMOUNT) = (x.INVOICE_AMOUNT) then
                update DAT_KEBS_INVOICE_BATCH_DETAILS d
                set d.STATUS         = 1,
                    d.RECEIPT_NUMBER = x.TRANSACTION_ID,
                    paid_amount      = x.PAID_AMOUNT
                where d.ID = x.INVOICE_ID
                  and STATUS != 1;
                when (x.PAID_AMOUNT) < (x.INVOICE_AMOUNT) then
                    update STG_PAYMENT_RECONCILIATION a
                    set a.PAYMENT_TABLES_UPDATED_STATUS = null,
                        a.TRANSACTION_ID                = x.TRANSACTION_ID,
                        a.TRANSACTION_DATE              = default,
                        a.PAYMENT_SOURCE                = null,
                        a.EXTRAS                        = null,
                        a.PAYMENT_TRANSACTION_DATE      = null,
                        a.INVOICE_AMOUNT=((x.INVOICE_AMOUNT) - (x.PAID_AMOUNT)),
                        a.PAID_AMOUNT                   = null
                    where a.INVOICE_ID = x.INVOICE_ID;
                end case;
            --             update DAT_KEBS_INVOICE_BATCH_DETAILS d set d.STATUS = 1 ,d.RECEIPT_NUMBER = x.TRANSACTION_ID, paid_amount = x.PAID_AMOUNT  where d.ID = x.INVOICE_ID and STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update STG_PAYMENT_RECONCILIATION ss
    set ss.PAYMENT_TABLES_UPDATED_STATUS = 10
    where ss.INVOICE_ID in (SELECT s.INVOICE_ID
                            FROM STG_PAYMENT_RECONCILIATION s
                            where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS)
                              and (s.PAID_AMOUNT) in (s.INVOICE_AMOUNT));
    -- We roll back to the savepoint.
-- EXCEPTION WHEN OTHERS THEN
    -- We roll back to the savepoint.
--     ROLLBACK TO sp_transactions;

    -- And of course we raise again,
    -- since we don't want to hide the error.
    -- Not raising here is an error!
--     RAISE;
--     update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = null,ss.INVOICE_AMOUNT=((ss.INVOICE_AMOUNT)-(ss.PAID_AMOUNT))  where ss.INVOICE_ID in (SELECT s.INVOICE_ID FROM STG_PAYMENT_RECONCILIATION s where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS) and (s.PAID_AMOUNT) < (s.INVOICE_AMOUNT) );

end;
/

DN20210415B58FA
create or replace procedure proc_update_demand_note_when_payment_done(PAID_STATUS number, TABLE_SOURCE_NAME varchar2)
as
DECLARE MY_TRANSACTION_IDS VARCHAR2;
begin

    FOR X IN (SELECT * FROM DAT_KEBS_INVOICE_BATCH_DETAILS s  where s.STATUS in (PAID_STATUS) and s.TABLE_SOURCE in (TABLE_SOURCE_NAME))
        LOOP
            FOR Y IN (SELECT * FROM LOG_STG_PAYMENT_RECONCILIATION a  where a.INVOICE_ID in (x.ID))
                LOOP
                        MY_TRANSACTION_IDS = concat('|',MY_TRANSACTION_IDS,'|',y.TRANSACTION_ID,'|');
                end loop;
            update DAT_KEBS_CD_DEMAND_NOTE d
            set d.PAYMENT_STATUS = 1,
                d.RECEIPT_NO     = MY_TRANSACTION_IDS
            where d.INVOICE_BATCH_NUMBER_ID = x.id
              and d.TOTAL_AMOUNT = x.PAID_AMOUNT
              and d.PAYMENT_STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update DAT_KEBS_INVOICE_BATCH_DETAILS ss set ss.STATUS = 10 where ss.id in (SELECT s.id FROM DAT_KEBS_INVOICE_BATCH_DETAILS s where s.STATUS in (PAID_STATUS));
end;
/



create or replace procedure proc_update_permit_invoice_when_payment_done(PAID_STATUS number, TABLE_SOURCE_NAME varchar2)
as
begin
    FOR X IN (SELECT * FROM DAT_KEBS_INVOICE_BATCH_DETAILS s  where s.STATUS in (PAID_STATUS) and s.TABLE_SOURCE in (TABLE_SOURCE_NAME))
        LOOP
            update DAT_KEBS_INVOICE d
            set d.PAYMENT_STATUS = 1,
                d.RECEIPT_NO     = x.RECEIPT_NUMBER
            where d.INVOICE_BATCH_NUMBER_ID = x.id
--               and d.TOTAL_AMOUNT = x.PAID_AMOUNT
              and d.PAYMENT_STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update DAT_KEBS_INVOICE_BATCH_DETAILS ss set ss.STATUS = 10 where ss.id in (SELECT s.id FROM DAT_KEBS_INVOICE_BATCH_DETAILS s where s.STATUS in (PAID_STATUS));
end;
/

create or replace procedure proc_update_permit_when_permit_invoice_payment_done(PAID_STATUS number)
as
begin
    FOR X IN (SELECT * FROM DAT_KEBS_INVOICE s  where s.PAYMENT_STATUS in (PAID_STATUS))
        LOOP
            update DAT_KEBS_PERMIT_TRANSACTION d
            set d.PAID_STATUS = 1
            where d.ID = x.PERMIT_ID
--               and d.TOTAL_AMOUNT = x.PAID_AMOUNT
              and d.PAID_STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update DAT_KEBS_INVOICE ss set ss.PAYMENT_STATUS = 10 where ss.id in (SELECT s.id FROM DAT_KEBS_INVOICE s where s.PAYMENT_STATUS in (PAID_STATUS));
end;
/




****************old working************************

create or replace procedure proc_update_demand_note_when_payment_done(PAID_STATUS number, TABLE_SOURCE_NAME varchar2)
as
begin
    FOR X IN (SELECT * FROM DAT_KEBS_INVOICE_BATCH_DETAILS s  where s.STATUS in (PAID_STATUS) and s.TABLE_SOURCE in (TABLE_SOURCE_NAME))
        LOOP
            update DAT_KEBS_CD_DEMAND_NOTE d
            set d.PAYMENT_STATUS = 1,
                d.RECEIPT_NO     = x.RECEIPT_NUMBER
            where d.INVOICE_BATCH_NUMBER_ID = x.id
              and d.TOTAL_AMOUNT = x.PAID_AMOUNT
              and d.PAYMENT_STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update DAT_KEBS_INVOICE_BATCH_DETAILS ss set ss.STATUS = 10 where ss.id in (SELECT s.id FROM DAT_KEBS_INVOICE_BATCH_DETAILS s where s.STATUS in (PAID_STATUS));
end;
/

create or replace procedure proc_update_invoice_when_payment_done(PAID_STATUS number)
as
begin
    FOR X IN (SELECT * FROM STG_PAYMENT_RECONCILIATION s where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS))
        LOOP
            update DAT_KEBS_INVOICE_BATCH_DETAILS d
            set d.STATUS         = 1,
                d.RECEIPT_NUMBER = x.TRANSACTION_ID,
                paid_amount      = x.PAID_AMOUNT
            where d.ID = x.INVOICE_ID
              and d.TOTAL_AMOUNT = x.PAID_AMOUNT
              and STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update STG_PAYMENT_RECONCILIATION ss
    set ss.PAYMENT_TABLES_UPDATED_STATUS = 10
    where ss.INVOICE_ID in (SELECT s.INVOICE_ID
                            FROM STG_PAYMENT_RECONCILIATION s
                            where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS)
                              and ss.INVOICE_AMOUNT = ss.PAID_AMOUNT);
end;
/

BEGIN
    DBMS_SCHEDULER.CREATE_PROGRAM(
            program_name => 'PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            program_type => 'STORED_PROCEDURE',
            program_action => 'PROC_UPDATE_DEMAND_NOTE_WHEN_PAYMENT_DONE',
            number_of_arguments =>0,
            enabled => TRUE,
            comments => '1 min update on demand note payment details'
        );
END;


BEGIN
    DBMS_SCHEDULER.CREATE_PROGRAM(
            program_name => 'PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
            program_type => 'STORED_PROCEDURE',
            program_action => 'PROC_UPDATE_PERMIT_INVOICE_WHEN_PAYMENT_DONE',
            number_of_arguments =>0,
            enabled => TRUE,
            comments => '1 min update on permit invoice payment details'
        );
END;

BEGIN
    DBMS_SCHEDULER.CREATE_PROGRAM(
            program_name => 'PROG_UPDATE_INVOICE_WHEN_PAYMENT_DONE',
            program_type => 'STORED_PROCEDURE',
            program_action => 'PROC_UPDATE_INVOICE_WHEN_PAYMENT_DONE',
            number_of_arguments =>0,
            enabled => TRUE,
            comments => '5 min update on invoice payment'
        );
END;

BEGIN
    DBMS_SCHEDULER.CREATE_PROGRAM(
            program_name => 'PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE',
            program_type => 'STORED_PROCEDURE',
            program_action => 'PROC_UPDATE_PERMIT_WHEN_PERMIT_INVOICE_PAYMENT_DONE',
            number_of_arguments =>0,
            enabled => TRUE,
            comments => '5 min update on invoice payment'
        );
END;
/


BEGIN
    DBMS_SCHEDULER.CREATE_SCHEDULE(
            Schedule_name => 'SCHEDULE_UPDATE_PAYMENT_DETAILS',
            Start_date => SYSTIMESTAMP,
            Repeat_interval =>'FREQ=MINUTELY; INTERVAL=1',
            Comments => '5 min update on invoice payment'
        );
END;

BEGIN
    DBMS_SCHEDULER.CREATE_SCHEDULE(
            Schedule_name => 'SCHEDULE_UPDATE_PAYMENT_1MIN_DETAILS',
            Start_date => SYSTIMESTAMP,
            Repeat_interval =>'FREQ=MINUTELY; INTERVAL=1',
            Comments => '1 min update on table'
        );
END;

BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'JOB_UPDATE_DEMAND_NOTE_WHEN_PAYMENT_DONE',
            program_name => 'PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            schedule_name => 'SCHEDULE_UPDATE_PAYMENT_DETAILS',
            enabled => TRUE,
            comments => 'update on demand note payment is done'
        );
END;

BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'JOB_UPDATE_PERMIT_INVOICE_WHEN_PAYMENT_DONE',
            program_name => 'PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
            schedule_name => 'SCHEDULE_UPDATE_PAYMENT_DETAILS',
            enabled => TRUE,
            comments => 'update on permit invoice payment is done'
        );
END;
proc_update_permit_when_permit_invoice_payment_done
BEGIN
    DBMS_SCHEDULER.CREATE_JOB(
            job_name => 'JOB_UPDATE_PERMIT_WHEN_PAYMENT_DONE',
            program_name => 'PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE',
            schedule_name => 'SCHEDULE_UPDATE_PAYMENT_1MIN_DETAILS',
            enabled => TRUE,
            comments => 'update on permit payment is done'
        );
END;

BEGIN
    DBMS_SCHEDULER.DROP_PROGRAM(
            program_name => 'PROG_UPDATE_PAYMENT_DETAILS',
            force => TRUE
        );
END;
BEGIN
    dbms_scheduler.enable(name => 'PROG_UPDATE_PAYMENT_DETAILS');
END;/

DBMS_SCHEDULER.enable('PROG_UPDATE_PAYMENT_DETAILS')



BEGIN
    DBMS_SCHEDULER.DISABLE(name=>'APOLLO.PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE', force=> TRUE);

    DBMS_SCHEDULER.drop_program_argument(program_name => 'APOLLO.PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE',
                                         argument_position => 1);

    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE',
            argument_name => 'PAID_STATUS',
            argument_position => 1,
            argument_type => 'NUMBER',
            default_value => '1',
            out_argument => FALSE);


    DBMS_SCHEDULER.ENABLE(name=>'APOLLO.PROG_UPDATE_PERMIT_WHEN_PAYMENT_DONE');
END;

BEGIN
    DBMS_SCHEDULER.DISABLE(name=>'APOLLO.PROG_UPDATE_INVOICE_WHEN_PAYMENT_DONE', force=> TRUE);

    DBMS_SCHEDULER.drop_program_argument(program_name => 'APOLLO.PROG_UPDATE_INVOICE_WHEN_PAYMENT_DONE',
                                         argument_position => 1);

    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_INVOICE_WHEN_PAYMENT_DONE',
            argument_name => 'PAID_STATUS',
            argument_position => 1,
            argument_type => 'NUMBER',
            default_value => '1',
            out_argument => FALSE);


    DBMS_SCHEDULER.ENABLE(name=>'APOLLO.PROG_UPDATE_INVOICE_WHEN_PAYMENT_DONE');
END;


BEGIN
    DBMS_SCHEDULER.DISABLE(name=>'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS', force=> TRUE);

    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
                                 attribute => 'program_action',
                                 value => 'APOLLO.PROC_UPDATE_DEMAND_NOTE_WHEN_PAYMENT_DONE');
    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
                                 attribute => 'number_of_arguments', value => '2');


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            argument_name => 'PAID_STATUS',
            argument_position => 1,
            argument_type => 'NUMBER',
            default_value => '1',
            out_argument => FALSE);


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            argument_name => 'TABLE_SOURCE_NAME',
            argument_position => 2,
            argument_type => 'VARCHAR2',
            default_value => 'DN',
            out_argument => FALSE);


    DBMS_SCHEDULER.ENABLE(name=>'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS');
END;


BEGIN
    DBMS_SCHEDULER.DISABLE(name=>'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS', force=> TRUE);

    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
                                 attribute => 'program_action',
                                 value => 'APOLLO.PROC_UPDATE_DEMAND_NOTE_WHEN_PAYMENT_DONE');
    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
                                 attribute => 'number_of_arguments', value => '2');


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            argument_name => 'PAID_STATUS',
            argument_position => 1,
            argument_type => 'NUMBER',
            default_value => '1',
            out_argument => FALSE);


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS',
            argument_name => 'TABLE_SOURCE_NAME',
            argument_position => 2,
            argument_type => 'VARCHAR2',
            default_value => 'DN',
            out_argument => FALSE);


    DBMS_SCHEDULER.ENABLE(name=>'APOLLO.PROG_UPDATE_DEMAND_NOTE_PAYMENT_DETAILS');
END;

BEGIN
    DBMS_SCHEDULER.DISABLE(name=>'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS', force=> TRUE);

    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
                                 attribute => 'program_action',
                                 value => 'APOLLO.PROC_UPDATE_PERMIT_INVOICE_WHEN_PAYMENT_DONE');
    DBMS_SCHEDULER.set_attribute(name => 'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
                                 attribute => 'number_of_arguments', value => '2');


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
            argument_name => 'PAID_STATUS',
            argument_position => 1,
            argument_type => 'NUMBER',
            default_value => '1',
            out_argument => FALSE);


    DBMS_SCHEDULER.define_program_argument(
            program_name => 'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS',
            argument_name => 'TABLE_SOURCE_NAME',
            argument_position => 2,
            argument_type => 'VARCHAR2',
            default_value => 'PM',
            out_argument => FALSE);


    DBMS_SCHEDULER.ENABLE(name=>'APOLLO.PROG_UPDATE_PERMIT_INVOICE_PAYMENT_DETAILS');
END;

create or replace function fun_update_invoice_WHEN_payments_made(PAID_STATUS IN NUMBER)
    RETURN VARCHAR IS
    RESPONSE_code varchar2(350);
begin

    --     declare
--         CURSOR paid_for_records IS SELECT * FROM STG_PAYMENT_RECONCILIATION s where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS);
--     BEGIN
--         open paid_for_records;

--         for paid_for_record in paid_for_records loop
    FOR X IN (SELECT * FROM STG_PAYMENT_RECONCILIATION s where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS))
        LOOP
            update DAT_KEBS_INVOICE_BATCH_DETAILS d
            set d.STATUS    = 1,
                paid_amount = x.PAID_AMOUNT
            where BATCH_NUMBER = x.INVOICE_ID
              and STATUS != 1;
--         update STG_PAYMENT_RECONCILIATION ss set ss.PAYMENT_TABLES_UPDATED_STATUS = 10 where ss.INVOICE_ID = x.INVOICE_ID;
            commit;
        end loop;

    update STG_PAYMENT_RECONCILIATION ss
    set ss.PAYMENT_TABLES_UPDATED_STATUS = 10
    where ss.INVOICE_ID in (SELECT s.INVOICE_ID
                            FROM STG_PAYMENT_RECONCILIATION s
                            where s.PAYMENT_TABLES_UPDATED_STATUS in (PAID_STATUS));


--             end loop;
    return 'successfuly processed ';
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'Failed processing';
--             end ;


end;
/