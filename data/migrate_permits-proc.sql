CALL PROC_MIGRATE_NEW_USER_PERMITS(2046, '10333', 378);

SELECT *
FROM DAT_KEBS_PERMIT_TRANSACTION
WHERE PERMIT_REF_NUMBER = '10333';

DELETE DAT_KEBS_PERMIT_TRANSACTION
WHERE PERMIT_REF_NUMBER = '10333';

UPDATE DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
SET MIGRATED_STATUS = 0
WHERE PERMIT_NUMBER = 10333;

select *
from DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
WHERE FIRM_ID = 89
;

COMMIT;

CREATE OR REPLACE PROCEDURE PROC_MIGRATE_NEW_USER_PERMITS(
    VAR_USER_ID NUMBER,
    VAR_PERMIT_NUMBER VARCHAR2,
    VAR_ATTACHED_PLANT_ID NUMBER
    --     ,
--     RESPONSE_MESSAGE OUT VARCHAR2(3500)
)
AS
--     RESPONSE_code varchar2(350);
BEGIN
    DECLARE
        exists_count NUMBER;

        e_already_migrated EXCEPTION ;
        CURSOR c_permits IS
            SELECT DATE_OF_ISSUE,
                   EFFECTIVE_DATE,
                   KS_NUMBER,
                   1                                     AS THE_PERMIT_TYPE,
                   PRODUCT_NAME,
                   TRADE_MARK,
                   VAR_USER_ID                           AS the_user_id,
                   PERMIT_NUMBER,
                   DATE_OF_EXPIRY,
                   DECODE(SECTION_ID, 'MECHANICAL', 76, 'TEXTILE', 78, 'FOOD', 71, 'CHEMICAL', 65,
                          'CIVIL',
                          77,
                          'ELECTRICAL', 79, 'AGRICULTURE', 73, 'WATER', 75, 'SOFT DRINK', 120,
                          120)                           AS SECTION_ID,
                   COMPANY_NAME,
                   POSTAL_CODE || '-' || ADDRESS         AS POSTAL_ADDRESS,
                   PHONE,
                   FAX,
                   EMAIL,
                   PHYSICAL_ADDRESS,
                   VAT_NO2,
                   REGION,
                   DECODE(SUSPENSION_STATUS, 'NO', 1, 0) AS PERMITS_STATUS,
                   CURRENT_TIMESTAMP                     AS CREATED_ON,
                   VAR_ATTACHED_PLANT_ID                 AS plant_id
            FROM DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
            WHERE FIRM_ID = (
                SELECT FIRM_ID
                FROM DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
                WHERE PERMIT_NUMBER = VAR_PERMIT_NUMBER)
              AND MIGRATED_STATUS = 0;
        C_PERMIT     c_permits%ROWTYPE;
        qry          varchar2(4000);

    BEGIN
        --     FOR X IN c_permits
        dbms_output.put_line('Opening the cursor');

        OPEN c_permits;

        LOOP


            SAVEPOINT update_permit;

            dbms_output.put_line('Looping through the cursor');

            FETCH c_permits
                INTO
                C_PERMIT;
            EXIT
                WHEN c_permits%NOTFOUND;

            dbms_output.put_line('Value of the permit_number is: ' || C_PERMIT.PERMIT_NUMBER);

            qry := 'select count(*) from DAT_KEBS_PERMIT_TRANSACTION
	        where upper(trim(PERMIT_REF_NUMBER)) = ''' || C_PERMIT.PERMIT_NUMBER || '''';

            dbms_output.put_line('Getting the count of entries using query: ' || qry);

            EXECUTE IMMEDIATE (qry)
                INTO
                exists_count;

            dbms_output.put_line('Value of the exists_count is: ' || exists_count);

            IF exists_count > 0 THEN
                qry := 'update DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
						 set MIGRATED_STATUS   = 99,
		                MIGRATION_REMARKS = ''_Record already migrated to plant id =' || C_PERMIT.plant_id || ''',
		                MIGRATION_BY      = ' || C_PERMIT.the_user_id || ',
		                MIGRATION_ON      = ''' || to_char(CURRENT_TIMESTAMP, 'DD-MON-YYYY HH:MI:SSxFF AM TZH:TZM') ||
                       ''' where PERMIT_NUMBER = ' || C_PERMIT.PERMIT_NUMBER;

                dbms_output.put_line('Updating already existing record: ' || qry);

                EXECUTE IMMEDIATE (qry);
                -- update DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
-- set MIGRATED_STATUS   = 99,
--    MIGRATION_REMARKS = MIGRATION_REMARKS || '_Record already migrated',
--    MIGRATION_BY      = VAR_USER_ID,
--    MIGRATION_ON      = current_timestamp
-- where PERMIT_NUMBER = C_PERMIT.PERMIT_NUMBER;
                COMMIT;

                dbms_output.put_line('Done with commit for already existing record.');
            ELSE
                qry := 'INSERT INTO APOLLO.DAT_KEBS_PERMIT_TRANSACTION (DATE_OF_ISSUE, EFFECTIVE_DATE, KS_NUMBER,
		                                                            PERMIT_TYPE, PRODUCT_NAME, TRADE_MARK, USER_ID,
		                                                            PERMIT_REF_NUMBER, DATE_OF_EXPIRY, SECTION_ID,
		                                                            FIRM_NAME, POSTAL_ADDRESS, TELEPHONE_NO, FAX_NO,
		                                                            EMAIL, PHYSICAL_ADDRESS, VAT_NO, REGION, STATUS,
		                                                            COMMODITY_DESCRIPTION,
		                                                            ATTACHED_PLANT_ID)
		            values (''' || C_PERMIT.DATE_OF_ISSUE || ''',''' || C_PERMIT.EFFECTIVE_DATE || ''',''' ||
                       C_PERMIT.KS_NUMBER || ''',' ||
                       C_PERMIT.THE_PERMIT_TYPE || ',''' || C_PERMIT.PRODUCT_NAME || ''',''' || C_PERMIT.TRADE_MARK ||
                       ''',' ||
                       C_PERMIT.the_user_id || ',''' || C_PERMIT.PERMIT_NUMBER || ''',''' || C_PERMIT.DATE_OF_EXPIRY ||
                       ''',' || C_PERMIT.SECTION_ID
                    || ',''' || C_PERMIT.COMPANY_NAME || ''',''' || REPLACE(C_PERMIT.POSTAL_ADDRESS, '''', '''''') ||
                       ''',''' || C_PERMIT.PHONE || ''',''' || C_PERMIT.FAX || ''',''' || C_PERMIT.EMAIL
                    || ''',''' || C_PERMIT.PHYSICAL_ADDRESS || ''',''' || C_PERMIT.VAT_NO2 || ''',''' ||
                       C_PERMIT.REGION || ''',' || C_PERMIT.PERMITS_STATUS
                    || ',''' || C_PERMIT.PRODUCT_NAME || ''',' || C_PERMIT.plant_id || ')';

                dbms_output.put_line('Inserting previously non existent permit: ' || qry);

                EXECUTE IMMEDIATE (qry);

                COMMIT;

                dbms_output.put_line('Done with commit for inserting new record.');
                --log results
--update APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
--set MIGRATED_STATUS   = 1,
--MIGRATION_REMARKS = 'Successfully migrated',-
--MIGRATION_BY      = VAR_USER_ID,
--  MIGRATION_ON      = C_PERMIT.CREATED_ON
--where PERMIT_NUMBER = C_PERMIT.PERMIT_NUMBER;
--commit;
                qry := 'update DAT_KEBS_PERMIT_TRANSACTION_MIGRATION
						 set MIGRATED_STATUS   = 1,
		                MIGRATION_REMARKS = ''Successfully migrated'',
		                MIGRATION_BY      = ' || C_PERMIT.the_user_id || ',
		                MIGRATION_ON      = ''' || to_char(CURRENT_TIMESTAMP, 'DD-MON-YYYY HH:MI:SSxFF AM TZH:TZM') ||
                       ''' where PERMIT_NUMBER = ' || C_PERMIT.PERMIT_NUMBER;
--dbms_output.put_line(qry);
                dbms_output.put_line('Updating log for non-migrated record: ' || qry);

                EXECUTE IMMEDIATE (qry);

                COMMIT;

                dbms_output.put_line('Done with update for a new record.');
            END IF;
        END LOOP;

        CLOSE c_permits;
        --             end loop;
--return 'successfuly processed ';
--         RESPONSE_MESSAGE = 'Successfully processed';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK TO update_permit;
        --                 RESPONSE_MESSAGE = 'Processing failed';
        --RETURN 'Failed processing';
--             end ;
    END;
END;
/

