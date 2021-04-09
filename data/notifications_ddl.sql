

select *
from LOG_WORKFLOW_TRANSACTIONS
where id > 520;

select *
from CFG_NOTIFICATIONS;

select CURRENT_TIMESTAMP
from dual;

update CFG_NOTIFICATIONS
set NOTIFICATION_TYPE   = 1,
    EVENT_STATUS        = 30,
    BEAN_PROCESSOR_NAME = 'sendSms',
    REQUEST_TOPIC       = 'kebs-registration',
    METHOD_NAME='sendSmsUsingKtorClient',
    CREATED_ON          =CURRENT_TIMESTAMP,
    CREATED_BY='admin'
where BEAN_PROCESSOR_NAME is null;

update CFG_NOTIFICATIONS set SERVICE_MAP_ID = CFG_NOTIFICATIONS.service_map_id -1  where SERVICE_MAP_ID < 127;

rollback;

commit ;

alter table CFG_NOTIFICATIONS add service_map_id number default 127 references CFG_SERVICE_MAPS(ID)  not null;

alter table CFG_NOTIFICATIONS add service_request_status number default 30 not null;

CFG_SER

alter table CFG_NOTIFICATION_TYPES add INTEGRATION_id number references CFG_INTEGRATION_CONFIGURATION(ID);

alter table CFG_INTEGRATION_CONFIGURATION add primary key (ID);
alter table CFG_SERVICE_MAPS_WORKFLOWS add primary key (ID);

select * from CFG_SERVICE_MAPS_WORKFLOWS order by id;

select * from CFG_INTEGRATION_CONFIGURATION;





select * from CFG_NOTIFICATIONS order by id;



create unique index CFG_NOTIFICATIONS_TYPE_SERVICE_MAP_REQUEST_STATUS_STATUS_IDX on CFG_NOTIFICATIONS (NOTIFICATION_TYPE,SERVICE_MAP_ID,SERVICE_REQUEST_STATUS, STATUS);
create unique index CFG_REQUEST_TOPIC_SERVICE_MAP_id_REQUEST_STATUS_STATUS_IDX on CFG_NOTIFICATIONS (NOTIFICATION_TYPE,SERVICE_MAP_ID,SERVICE_REQUEST_STATUS, STATUS);

--alter table CFG_SERVICE_MAPS add NOTIFICATION_SELECTION_TOPIC

alter table APOLLO.LOG_SERVICE_REQUESTS add ATTACHMENT_PATH varchar2(350 char );

select * from DAT_KEBS_NOTIFICATIONS order by ID;

update DAT_KEBS_NOTIFICATIONS set id = rownum ;


select DAT_KEBS_NOTIFICATIONS_SEQ.nextval from dual;

create sequence DAT_KEBS_NOTIFICATIONS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_NOTIFICATIONS_trg
    before
        insert
    on DAT_KEBS_NOTIFICATIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_NOTIFICATIONS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create sequence CFG_NOTIFICATIONS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 21 cache 20 noorder nocycle;

create trigger CFG_NOTIFICATIONS_TRG
    before
        insert
    on CFG_NOTIFICATIONS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_NOTIFICATIONS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

alter table DAT_KEBS_NOTIFICATIONS add RESPONSE_MESSAGE varchar2(3500);
alter table DAT_KEBS_NOTIFICATIONS add NOTIFICATION_ID number;
alter table DAT_KEBS_NOTIFICATIONS add RESPONSE_STATUS varchar2(350);
alter table CFG_NOTIFICATION_TYPES add actor_class varchar2(350 char);
update CFG_NOTIFICATION_TYPES set actor_class ='notificationBufferProcessingActor' where actor_class is null;

commit ;

select * from CFG_NOTIFICATION_TYPES;

update CFG_NOTIFICATION_TYPES set INTEGRATION_ID = 4 where id=2;

select * from CFG_INTEGRATION_CONFIGURATION;

select CFG_NOTIFICATIONS_SEQ.nextval from dual;


insert into CFG_NOTIFICATIONS(NOTIFICATION_TYPE, STATUS, SENDER,SPEL_PROCESSOR,SUBJECT,BEAN_PROCESSOR_NAME, METHOD_NAME,SERVICE_MAP_ID,ACTOR_CLASS, EVENT_STATUS, DESCRIPTION)
values(1,1,'alerts@bskglobaltech.com','127.0.0.1,8005,/api/auth/signup/activate,token,123456,#firstName,#eventBusSubmitDate', 'KEBS ACCOUNT ACTIVATION', 'sendEmail','sendMail',128, 'notificationBufferProcessingActor',20, 'Dear {5},
 You requested access to KEBS kims platform on {6}. Kindly click the link below to confirm
http://{0}:{1}{2}?{3}={4}
 regards,
 KEBS Team');

 update CFG_NOTIFICATIONS set REQUEST_TOPIC = 'select-notifications-use-case' where id = 21;
 commit ;

 select * from CFG_NOTIFICATIONS
-- where SERVICE_MAP_ID =127
 order by ID;

 select * from CFG_NOTIFICATION_TYPES order by id;

 update CFG_NOTIFICATION_TYPES set BEAN_PREFIX_REPLACEMENT = ' ';


 select *from LOG_SERVICE_REQUESTS where id = 188 order by id;

 update LOG_SERVICE_REQUESTS set NAMES ='Kenneth Muhia', NOTIFICATION_TYPE =1 where id = 188;

select * from DAT_KEBS_NOTIFICATIONS order by id;

select * from DAT_KEBS_USER_VERIFICATION_TOKEN order by id;

select * from CFG_SERVICE_MAPS order by id;

update CFG_NOTIFICATIONS set SPEL_PROCESSOR ='127.0.0.1_8005_/api/auth/signup/activate_token_#transactionReference_#names_#eventBusSubmitDate' where id in (21,22);

update CFG_NOTIFICATIONS set BEAN_PROCESSOR_NAME ='sendEmailService', METHOD_NAME='sendEmail' where BEAN_PROCESSOR_NAME ='sendEmail' or id = 4;

commit ;

select * from LOG_SERVICE_REQUESTS where  id >= 188 order by ID;