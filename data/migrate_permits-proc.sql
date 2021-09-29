CALL PROC_MIGRATE_NEW_USER_PERMITS(2046, '10333', 378);

SELECT *
FROM DAT_KEBS_PERMIT_TRANSACTION
WHERE PERMIT_REF_NUMBER = '10333';
DELETE DAT_KEBS_PERMIT_TRANSACTION
WHERE PERMIT_REF_NUMBER = '10333';

UPDATE APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
SET MIGRATED_STATUS = 0
WHERE PERMIT_NUMBER = 10333;

COMMIT;

create PROCEDURE PROC_MIGRATE_NEW_USER_PERMITS(
    VAR_USER_ID NUMBER,
    VAR_PERMIT_NUMBER VARCHAR2,
    VAR_ATTACHED_PLANT_ID NUMBER
)
as
--     RESPONSE_code varchar2(350);
BEGIN
    declare
        exists_count number;
        e_already_migrated exception ;
        CURSOR c_permits IS SELECT DATE_OF_ISSUE,
                                   EFFECTIVE_DATE,
                                   KS_NUMBER,
                                   1                                     as THE_PERMIT_TYPE,
                                   PRODUCT_NAME,
                                   TRADE_MARK,
                                   VAR_USER_ID                           as the_user_id,
                                   PERMIT_NUMBER,
                                   DATE_OF_EXPIRY,
                                   DECODE(SECTION_ID, 'MECHANICAL', 76, 'TEXTILE', 78, 'FOOD', 71, 'CHEMICAL', 65,
                                          'CIVIL',
                                          77,
                                          'ELECTRICAL', 79, 'AGRICULTURE', 73, 'WATER', 75, 'SOFT DRINK', 120,
                                          120)                           as SECTION_ID,
                                   COMPANY_NAME,
                                   POSTAL_CODE || '-' || ADDRESS         AS POSTAL_ADDRESS,
                                   PHONE,
                                   FAX,
                                   EMAIL,
                                   PHYSICAL_ADDRESS,
                                   VAT_NO2,
                                   REGION,
                                   DECODE(SUSPENSION_STATUS, 'NO', 1, 0) as PERMITS_STATUS,
                                   CURRENT_TIMESTAMP                     as CREATED_ON,
                                   VAR_ATTACHED_PLANT_ID                 as plant_id
                            FROM APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
                            WHERE FIRM_ID = (SELECT FIRM_ID
                                             FROM APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
                                             WHERE PERMIT_NUMBER = VAR_PERMIT_NUMBER)
                              and MIGRATED_STATUS = 0;
        C_PERMIT     c_permits%ROWTYPE;
        qry          varchar2(4000);
    begin
        --     FOR X IN c_permits
        dbms_output.put_line('Opening the cursor');
        OPEN c_permits;
        LOOP
            dbms_output.put_line('Looping through the cursor');
            FETCH c_permits INTO C_PERMIT;
            dbms_output.put_line('Value of the permit_number is: ' || C_PERMIT.PERMIT_NUMBER);
            qry := 'select count(*) from DAT_KEBS_PERMIT_TRANSACTION
	        where upper(trim(PERMIT_REF_NUMBER)) = ''' || C_PERMIT.PERMIT_NUMBER || '''';
            dbms_output.put_line('Getting the count of entries using query: ' || qry);
            EXECUTE immediate (qry) INTO exists_count;
            dbms_output.put_line('Value of the exists_count is: ' || exists_count);
            if exists_count > 0 THEN
                qry := 'update APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
						 set MIGRATED_STATUS   = 99,
		                MIGRATION_REMARKS = ''_Record already migrated to plant id =' || C_PERMIT.plant_id || ''',
		                MIGRATION_BY      = ' || C_PERMIT.the_user_id || ',
		                MIGRATION_ON      = ''' || to_char(CURRENT_TIMESTAMP, 'DD-MON-YYYY HH:MI:SSxFF AM TZH:TZM') ||
                       ''' where PERMIT_NUMBER = ' || C_PERMIT.PERMIT_NUMBER;
                dbms_output.put_line('Updating already existing record: ' || qry);
                execute immediate (qry);
                -- update APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
-- set MIGRATED_STATUS   = 99,
--    MIGRATION_REMARKS = MIGRATION_REMARKS || '_Record already migrated',
--    MIGRATION_BY      = VAR_USER_ID,
--    MIGRATION_ON      = current_timestamp
-- where PERMIT_NUMBER = C_PERMIT.PERMIT_NUMBER;
                commit;
                dbms_output.put_line('Done with commit for already existing record.');
            else
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
                execute immediate (qry);
                commit;
                dbms_output.put_line('Done with commit for inserting new record.');
                --log results
                --update APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
                --set MIGRATED_STATUS   = 1,
                --MIGRATION_REMARKS = 'Successfully migrated',-
                --MIGRATION_BY      = VAR_USER_ID,
                --  MIGRATION_ON      = C_PERMIT.CREATED_ON
                --where PERMIT_NUMBER = C_PERMIT.PERMIT_NUMBER;
                --commit;
                qry := 'update APOLLO_DAT_KEBS_PERMIT_TRANSACTION_20210917
						 set MIGRATED_STATUS   = 1,
		                MIGRATION_REMARKS = ''Successfully migrated'',
		                MIGRATION_BY      = ' || C_PERMIT.the_user_id || ',
		                MIGRATION_ON      = ''' || to_char(CURRENT_TIMESTAMP, 'DD-MON-YYYY HH:MI:SSxFF AM TZH:TZM') ||
                       ''' where PERMIT_NUMBER = ' || C_PERMIT.PERMIT_NUMBER;
                --dbms_output.put_line(qry);
                dbms_output.put_line('Updating log for non-existing record: ' || qry);
                execute immediate (qry);
                commit;
                dbms_output.put_line('Done with update for a new record.');
            end if;
            EXIT WHEN c_permits%NOTFOUND;
        end loop;
        CLOSE c_permits;


        --             end loop;
--return 'successfuly processed ';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            NULL;
        --RETURN 'Failed processing';
        --             end ;


    end;
END;
/

