SET FOREIGN_KEY_CHECKS=0;
truncate table CFG_NOTIFICATIONS cascade;
SET FOREIGN_KEY_CHECKS=1;
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (2, 'Test', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-16 08:01:31.523040', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, null, null, '30', 'stage-notifications', 'sendSmsService',
        'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (5, 'Another another test', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-16 07:58:33.570492', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null, null,
        null, null, null, null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 124, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (6, 'Congratulations! You have successfully registered.
Click <a href={0}>confirm</a> to activate your account.', 1, 1, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, 'alerts@bskglobaltech.com', 'varField1', '<html lang=''en''>
  <head>
    <!-- Required meta tags -->    <meta charset=''utf-8''>
    <meta name=''viewport'' content=''width=device-width, initial-scale=1, shrink-to-fit=no''>
    <!-- Bootstrap CSS -->
    <link rel=''stylesheet'' href=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css'' integrity=''sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh'' crossorigin=''anonymous''>
  </head>
  <body>
    <div class=''container''>
      <div class=''jumbotron''>
         <p>Templates</p>
      </div>
    </div>
    <style>
       .jumbotron {
           text-align: center;
    <style>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src=''https://code.jquery.com/jquery-3.4.1.slim.min.js'' integrity=''sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n'' crossorigin=''anonymous''></script>
    <script src=''https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js'' integrity=''sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo'' crossorigin=''anonymous''></script>
    <script src=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js'' integrity=''sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6'' crossorigin=''anonymous''></script>
  </body>
</html>', 'Activate Account', null, '30', 'stage-notifications', 'sendEmailService', 'sendEmail', 125, 30, null, null,
        null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (3, 'Another Test', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-16 08:01:31.523040', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, null, null, '30', 'stage-notifications', 'sendSmsService',
        'sendSmsUsingKtorClient', 122, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (4, 'Congratulations! You have successfully registered.
Click <a href={0}>confirm</a> to activate your account.', 1, 1, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, 'alerts@bskglobaltech.com', 'varField1', '<html lang=''en''>
  <head>
    <!-- Required meta tags -->    <meta charset=''utf-8''>
    <meta name=''viewport'' content=''width=device-width, initial-scale=1, shrink-to-fit=no''>
    <!-- Bootstrap CSS -->
    <link rel=''stylesheet'' href=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css'' integrity=''sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh'' crossorigin=''anonymous''>
  </head>
  <body>
    <div class=''container''>
      <div class=''jumbotron''>
         <p>Templates</p>
      </div>
    </div>
    <style>
       .jumbotron {
           text-align: center;
    <style>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src=''https://code.jquery.com/jquery-3.4.1.slim.min.js'' integrity=''sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n'' crossorigin=''anonymous''></script>
    <script src=''https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js'' integrity=''sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo'' crossorigin=''anonymous''></script>
    <script src=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js'' integrity=''sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6'' crossorigin=''anonymous''></script>
  </body>
</html>', 'Activate Account', null, '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 123, 30, null, null,
        null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (600, 'Dear {0}

Your OTP ({1}) for activation on KIMS PLATFORM. 

Yours sincerely 

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#otpGenerated', null, 'KEBS ACCOUNT ACTIVATION', null, '20',
        'stage-notifications', 'sendEmailService', 'sendEmail', 127, 30, 'notificationBufferProcessingActor',
        '964ac816-1082-4b15-96eb-0bb61723594b', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (22, 'Dear {5},
 You requested access to KEBS kims platform on {6}. Kindly click the link below to confirm
http://{0}:{1}{2}?{3}={4}
 regards,
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-18 19:52:20.478895', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '127.0.0.1_8005_/api/auth/signup/activate_token_#transactionReference_#names_#eventBusSubmitDate', null,
        'KEBS ACCOUNT ACTIVATION', null, '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 128, 30,
        'notificationBufferProcessingActor', null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (41, 'Dear Team,

An new complaint has been assigned to your group.

Click below to open the complaint for review
http://{0}:{1}/{2}/{3}/{4}', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-18 19:52:20.478895', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '127.0.0.1_8005_api/ms/complaints_#id_#mapId', null, 'KEBS ACCOUNT ACTIVATION',
        null, '10', 'stage-notifications', 'sendEmailService', 'sendEmail', 129, 10, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (7, 'A new standard request [Request Number: {0}] has been submitted and is awaiting your review.', 1, 1, null,
        null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-05-21 08:49:09.980368', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', 'varField1', '<html lang=''en''>
  <head>
    <!-- Required meta tags -->    <meta charset=''utf-8''>
    <meta name=''viewport'' content=''width=device-width, initial-scale=1, shrink-to-fit=no''>
    <!-- Bootstrap CSS -->
    <link rel=''stylesheet'' href=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css'' integrity=''sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh'' crossorigin=''anonymous''>
  </head>
  <body>
    <div class=''container''>
      <div class=''jumbotron''>
         <p>Templates</p>
      </div>
    </div>
    <style>
       .jumbotron {
           text-align: center;
    <style>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src=''https://code.jquery.com/jquery-3.4.1.slim.min.js'' integrity=''sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n'' crossorigin=''anonymous''></script>
    <script src=''https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js'' integrity=''sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo'' crossorigin=''anonymous''></script>
    <script src=''https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js'' integrity=''sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6'' crossorigin=''anonymous''></script>
  </body>
</html>', 'Receipt Standard Request Confirmation', null, '30', 'stage-notifications', 'sendEmailService', 'sendEmail',
        126, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (8, 'A new standard request [Request Number: {0}] has been submitted and is awaiting your review.', 2, 0, null,
        null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-05-21 08:49:10.037325', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null, null,
        'varField1', null, null, null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 127, 30,
        null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (145,
        'Dear <first_name> <last_name>,\n\nYou have a new <task_name> task pending under <process_name>.\nPlease login and complete the task.\n\nRegards,\nThe QAMISS Team.',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'NEW TASK RECEIVED', null, null, null, null, null, 150, 30, null, null,
        null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (147, 'Dear Sir / Madam,\n\nSample factory inspection notification\n\nRegards,\nThe QAMISS Team.', null, null,
        null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'NEW TASK RECEIVED', null, null, null, null, null, 199, 30, null, null,
        null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (183,
        'Dear <first_name> <last_name>,\n\nYour task - <task_name> under <process_name> has been pending for over 10 days.\nPlease login and complete the task.\n\nRegards,\nThe QAMISS Team.',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS COMPLAINT UPDATE', null, null, null, null, null, 130, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (224,
        'Dear <first_name> <last_name>,\n<assignee_first_name> <assignee_last_name> has completed the task <task_name>.\n\nRegards,\nThe KEBS team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 129, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (225, 'Dear <first_name> <last_name>,\nThe tasks below are still pending:\n<message>\nRegards,\nThe QAMISS team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 129, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (304, ' Dear {5},

 Your Destination Inspection Application form with Reference Number {9} at KEBS kims platform, has been reviewed on {6} and found to have the stated issues. 
 	issue(s) Stated: {10} 
	
	https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#createdBy_#issuesDate_diApplicationID_#id_#refNumber_#issuesRemarks',
        null, 'DI Application Issue(s)', null, '40', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        207, 30, null, '24f001bd-6d66-48c3-984a-524812022ad6', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (802, 'Dear {6},
  
Motor Inspection Report for vehicle with chassis number: {8} at KEBS kims platform has been approved on {7} with the following comments {9}.

	
 {0}{1}?{2}={3}&{4}={5}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/motor-vehicle-inspection-details_itemId_#id_docType_motorVehicleInspectionDetails_#fullName_#date_#chassisNumber_#comments',
        null, 'Report Disapproved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 122,
        30, null, 'ec14e84c-10d4-4f33-ae46-e6581464d9da', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (127, 'Dear customer,\n\nyour complaint has been submitted\n\nRegards,\nThe KEBS team', null, null, null, null,
        null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS COMPLAINT UPDATE', null, null, null, null, null, 121, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (401, ' Dear {5},
 
 You have been Assigned a Consignment Document(CD) with UCR Number {7} at KEBS kims platform on {6}. With the following Remarks {8} 
	
 https://{0}:{1}{2}?{3}={4}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/di/cd-details_cdID_#uuid_#assignedInspectionOfficer.firstName#assignedInspectionOfficer?.lastName_#assignedDate_#ucrNumber_#assignedRemarks',
        null, 'DI Assigned CD', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 122, 30,
        null, '8e179c55-953b-4a7c-92ba-42645d2e59b3', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (132, 'Dear EPRA,\n\nSample remediation email\n\nRegards,\nThe KEBS team', null, null, null, null, null, null,
        null, null, null, null, null, null, TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'),
        'admin', null, null, null, null, 'alerts@bskglobaltech.com', null, null, 'KEBS REMEDIATION', null, null, null,
        null, null, 123, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (135, 'Dear Customer,\n\nSample customer notification email\n\nRegards,\nThe KEBS team', null, null, null, null,
        null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 126, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (137,
        'Dear Customer,\n\nYour payment of <paymentAmount> has been updated on the platform\n\nRegards,\nThe KEBS team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 128, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (138,
        'Dear <first_name> <last_name>,\n\nThe payment of <paymentAmount> has been updated on the platform\n\nRegards,\nThe KEBS team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 129, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (222,
        'Dear Manufacturer, \n Please remember to log in and perform corrective actions to correct your compliance status.\nKind Regards, \n The QAMISS Team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, null, null, null, null, 122, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (241,
        'Dear <first_name> <last_name>,\nPlease note your lab results are still pending:\n\nRegards,\nThe QAMISS team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'KEBS', null, null, null, null, null, 129, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (261, 'Dear <first_name> <last_name>,\n Your application has been deferred.\nKind Regards, \n The QAMISS Team',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS Notification', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (262,
        'Dear <first_name> <last_name>,\n Your application has been accepted. Please collect your exemption certificate\nKind Regards, \n The QAMISS Team',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS Notification', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (263, 'Dear <first_name> <last_name>,\n Your application has been rejected.\nKind Regards, \n The QAMISS Team',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS Notification', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (303, ' Dear {5},
 Your Destination Inspection Application form with Referance Number {9} at KEBS kims platform and was approved on {6}. WIth the following Remarks: {10} 
	
 https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#createdBy_#approvalDate_diApplicationID_#id_#refNumber_#approvalRemarks',
        null, 'DI Application Approved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        207, 30, null, '528593bd-b226-4e68-a890-045a4aab8624', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (402, 'Dear {6},

The following BS Number: {8}, has been allocated for Sample with the following reference number {9} on {7}.

You can be able to click the link below to see the Details.
	
 {0}{1}?{2}={3}&{4}={5}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/inspection/sample-submission_cdItemUuid_#uuid_docType_#docTypeDetail_#fullName_#date_#bsNumber_#sampleRefNumber',
        null, 'BS Number', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 122, 30, null,
        'e2e1fe10-a8aa-4447-ac94-1bb622d0b63a', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (404, ' Dear {4},

You have a pending task, Awaiting approval of inspection report.

You can click the link below to see the Details
	
 {0}{1}?{2}={3}
 
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#baseUrl_/di/cd-item-details_cdItemUuid_#uuid_#fullName', null,
        'Inspection Report Approval', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        122, 30, null, 'd719d5c5-d49d-4e36-90fd-eb7795626e62', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (405, ' Dear {4},

The Inspection Report with the following Ref Number ({5}), was Approved on {6}, with the following comments "{7}"

You can click the link below to see the Details
	
 {0}{1}?{2}={3}
 
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/cd-item-details_cdItemUuid_#uuid_#fullName_#refNumber_#date_#commentRemarks', null,
        'Inspection Report Approved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        122, 30, null, '66d04881-2c9b-402b-8ab9-dbb9849d32bc', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (406, ' Dear {4},

The Inspection Report with the following Ref Number ({5}), was DisApproved on {6}, with the following comments "{7}"

You can click the link below to see the Details
	
 {0}{1}?{2}={3}
 
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/cd-item-details_cdItemUuid_#uuid_#fullName_#refNumber_#date_#commentRemarks', null,
        'Inspection Report DisApproved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        122, 30, null, '0d5bb8a7-8021-4c3c-a20f-0e8dfd08ed2e', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (50, 'A comment has been made on your permit application.', 1, 1, null, null, null, null, null, null, null, null,
        null, null, TO_TIMESTAMP('2020-07-18 14:35:56.173666', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null,
        null, 'alerts@bskglobaltech.com', null, null, 'Comment on Permit Application', null, '30',
        'stage-notifications', 'sendEmailService', 'sendEmail', 150, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (205,
        'Dear <first_name> <last_name>,\n\nThe task - <task_name> assigned to - <assignee_name> - under <process_name> has been pending for over <days> days.\n\nRegards,\nThe QAMISS Team.',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, null, null, null, null, 206, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (162, 'Dear Sir / Madam,\n\nThis is a sample notification\n\nRegards,\nThe QAMISS Team.', null, null, null, null,
        null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'NEW TASK RECEIVED', null, null, null, null, null, 205, 30, null, null,
        null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (20, 'Dear {5}.

This is to confirm that your complaint has been submitted to KEBS kims platform on {6}, and state that it shall be handled with confidentiality. 

We shall review the complaint and if it can be addressed within the KEBS mandate as stipulated in The Standards Act CAP 496, we will investigate as guided by our Consumer Complaints Handling Procedure. Once complete we will revert back to you with our findings. 

Thank you for your continued support. 

For any further engagement about this complaint, please write to us on marketsurveillance@kebs.org. ', 1, 1, null, null,
        null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/auth/signup/authorize_token_#transactionReference_#names_#eventBusSubmitDate_appId_#requestId',
        null, 'KEBS COMPLAINT ACKNOWLEDGEMENT', null, '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 130,
        30, 'notificationBufferProcessingActor', null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (242,
        'Dear User, \n This is a reminder that permit with id {id} will expire in four months.\nKind Regards, \n The QAMISS Team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, null, null, null, null, 121, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (302, 'Dear {5},
 You have a pending Destination Inspection Application form submitted with Reference Number {9} at KEBS kims platform and was submitted on {6}. Awaits approval
https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#createdBy_#createdOn_diApplicationID_#id_#refNumber',
        null, 'DI Application Await Approval', null, '20', 'stage-notifications', 'sendSmsService',
        'sendSmsUsingKtorClient', 207, 30, null, 'dd338b9b-ea55-4af6-a291-b4beb4cd55ca', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (501, ' Dear {0}.

This is to confirm that your complaint with ref Number ({1}) has been submitted to KEBS on {2}, and state that it shall be handled with confidentiality. 

We shall review the complaint and if it can be addressed within the KEBS mandate as stipulated in The Standards Act CAP 496, we will investigate as guided by our Consumer Complaints Handling Procedure. Once complete we will revert back to you with our findings. 

Thank you for your continued support. 

For any further engagement about this complaint, please write to us on marketsurveillance@kebs.org. 

 


Yours sincerely 

Director, Market Surveillance Directorate ', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#refNumber_#dateSubmitted', null, 'Complaint Acknowledgement', null,
        '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101, 30, null,
        '1d778ecf-f9c7-43c3-aae8-4f4fe6f5bae6', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (502, 'Dear {0}

Your complaint on {1} with Ref Number {2}. was well received and noted. 

We have reviewed it, and found that we cannot address the complaint within KEBS mandate because {3}.

Thank you for your continued support. 

For any further engagement about this complaint, please write to us on marketsurveillance@kebs.org. 

Yours sincerely 

Director, Market Surveillance Directorate', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#complaintTitle_#refNumber_#commentRemarks', null,
        'Complaint Acknowledgement', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101,
        30, null, '4fa03e8d-a148-4dc9-9ede-e11ec31c090d', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (503, 'Dear {4}

You have been assigned a task. To assign an officer to the following complaint with Ref Number {3}. 

{0}{1}?{2}={3}

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/api/v1/ms/ui/complaint/detail/view_refNumber_#refNumber_#fullName_#dateSubmitted', null,
        'Complaint Approved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101, 30,
        null, '6034fc49-bfd7-48a6-9254-e35218936f73', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (504, 'Dear {4}

The following complaint with Ref Number {3} on {5}. was Submitted. 

  {0}{1}?{2}={3}

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/api/v1/ms/ui/complaint/detail/view_refNumber_#refNumber_#fullName_#dateSubmitted', null,
        'Complaint Submitted', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101, 30,
        null, '267ab92b-ccc2-41e2-850d-56bc692494c8', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (505, 'Dear {4}

You have been assigned a complaint with Ref Number {3}. 

{0}{1}?{2}={3}

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/api/v1/ms/ui/complaint/detail/view_refNumber_#refNumber_#fullName_#dateSubmitted', null,
        'Complaint Assigned', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101, 30,
        null, '1b474c60-c7f4-4ccb-9cce-efca7ead8439', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (801, 'Dear {6},
  
Motor Inspection Report for vehicle with chassis number: {8} at KEBS kims platform has been dissaproved on {7} with the following corrective comments {9}.

	
 {0}{1}?{2}={3}&{4}={5}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/motor-vehicle-inspection-details_itemId_#id_docType_motorVehicleInspectionDetails_#fullName_#date_#chassisNumber_#comments',
        null, 'Report Disapproved', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 122,
        30, null, '48531f99-89cf-4b96-a43a-1423d257a266', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (403, 'Dear {6},

Lab results for Parameter({11}), with ID: {10} from the following Lab : {8}, has been sent, for Sample with the following reference number {9} on {7}.

You can be able to click the link below to see the Details
	
 {0}{1}?{2}={3}&{4}={5}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#baseUrl_/di/inspection/sample-submission_cdItemUuid_#uuid_docType_#docTypeDetail_#fullName_#date_#labName_#sampleRefNumber_#id_#parameterName',
        null, 'LAB Result', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 122, 30,
        null, 'bdcf6f3a-7c44-49d3-866d-059fc7c46118', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (101, 'Dear User,
 You requested to reset your credentials on  KEBS KIMS platform on {5}. Kindly click the link below to confirm
http://{0}:{1}{2}?{3}={4}&{6}={7}
 regards,
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-18 19:52:20.478895', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '127.0.0.1_8006_/api/auth/signup/reset_token_#transactionReference_#eventBusSubmitDate_appId_#serviceMapsId.id',
        null, 'KEBS ACCOUNT ACTIVATION', null, '20', 'send-staged-notifications', 'sendEmailService', 'sendEmail', 128,
        40, 'notificationBufferProcessingActor', null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (223,
        'Dear Manufacturer, \n This is a reminder that your permit will expire in four months\n Kindly remember to renew it.\nKind Regards, \n The QAMISS Team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, null, null, null, null, 121, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (243,
        'Dear User, \n This is a reminder that permit with id {id} was marked with a non-compliance status and needs to be acted on ASAP.\nKind Regards, \n The QAMISS Team',
        null, null, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, null, null, null, null, 121, 30, null,
        null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (244, 'Dear User, \n This is a reminder about permit with id {id}.\nKind Regards, \n The QAMISS Team', 1, 1,
        null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS OVERDUE TASK', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (281,
        'Dear <first_name> <last_name>,\n Your application has been successfully received.\nKind Regards, \n The QAMISS Team',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS Notification', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (282,
        'Dear <first_name> <last_name>,\n A new waivers application has been submitted.\nKind Regards, \n The QAMISS Team',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', null, null, 'QAMISS Notification', null, '30', 'stage-notifications',
        'sendSmsService', 'sendSmsUsingKtorClient', 121, 30, null, null, null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (301, 'Dear {5},
 Your application for Destination Inspection Application for Reference Number {9} at KEBS kims platform on {6}. has been submitted successFull Awaiting approval
https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#createdBy_#createdOn_diApplicationID_#id_#refNumber',
        null, 'DI Application Submitted', null, '10', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        207, 30, null, '1ed64622-7b59-4b11-8105-dff8d7b8abae', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (305, ' Dear {5},
 Your Destination Inspection Application form with Reference Number {9} at KEBS kims platform and was rejected on {6}. With the following Remarks {10} 
	
 https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#createdBy_#approvalDate_diApplicationID_#id_#refNumber_#approvalRemarks',
        null, 'DI Application Rejected', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        207, 30, null, 'b2bcc876-a7a9-44dd-a10b-fd28e835a02c', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (306, ' Dear {5},
 You have been Assigned a Destination Inspection Application form with Reference Number {9} at KEBS kims platform on {6}. With the following Remarks {10} 
	
 https://{0}:{1}{2}?{3}={4}&{7}={8}
 regards, 
 KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        'kims.kebs.org_8006_/api/importer/di-application-detail_diApplicationTypeID_#applicationType.id_#assignedUser.firstName#assignedUser?.lastName_#assignedUserDate_diApplicationID_#id_#refNumber_#assignedUserRemarks',
        null, 'DI Application Assigned', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient',
        207, 30, null, '45460cce-bd5f-45ef-b65c-4195a99c1b5c', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (506, 'Dear {0}

Your complaint on {1} with Ref Number {2}. was well received and noted. 

We have reviewed it, and found that we cannot address the complaint within KEBS mandate because {3}. Please refer the complaint to {4} who could be better placed to address the complaint. 

Thank you for your continued support. 

For any further engagement about this complaint, please write to us on marketsurveillance@kebs.org. 

Yours sincerely 

Director, Market Surveillance Directorate ', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-09-10 13:57:34.949722', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#complaintTitle_#refNumber_#commentRemarks_#adviceRemarks', null,
        'Complaint Acknowledgement', null, '30', 'stage-notifications', 'sendSmsService', 'sendSmsUsingKtorClient', 101,
        30, null, '4f0b95ae-c7c7-45de-8c88-150e265b1c49', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (601, 'Dear {0}

Your password on KIMS PLATFORM has been change on {1}, if it wasn''t you please contact KEBS Support Department. 

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#modifiedON', null, 'KEBS PASSWORD CHANGED', null, '20',
        'stage-notifications', 'sendEmailService', 'sendEmail', 127, 30, 'notificationBufferProcessingActor',
        'e31fbf31-12ee-4056-bc73-2708b7f0a387', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (602, 'Dear {0},

Did you request for a password change on {2}, If not please contact KEBS Support Department.

Use the OTP ({1}) for activation on KIMS PLATFORM. 

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#otpGenerated_#otpGeneratedDate', null, 'KEBS PASSWORD RESET', null,
        '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 127, 30, 'notificationBufferProcessingActor',
        'fd6134fa-ff77-4966-ae45-638e7a795cd1', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (603, 'Dear {0},

Your standards levy registration for {3} of Registration Number {5} on {2} is successful. KRA PIN is {4} and Entry number allocated is [{1}].  

Please make payments through KRA Itax system. 

Link to KRA {6}

Thank you. 

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com',
        '#fullName_#entryNumber_#dateSubmitted_#companyName_#kraPin_#registrationNumber_#itaxUrl', null,
        'KEBS STANDARD LEVY ENTRY NUMBER', null, '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 127, 30,
        'notificationBufferProcessingActor', '2ef0499c-0f4a-4728-aa48-a27f5d078276', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (900, 'Dear {0},

Your have a permit application with permit ref number [{1}], awaiting payment on kims platform. 

find below attached invoice details. 

Thank you. 

regards,

KEBS Team', 1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#permitRefNumber', null, 'KEBS PERMIT APPLICATION AWAITING PAYMENT',
        null, '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 208, 30,
        'notificationBufferProcessingActor', '5b53c22b-7f99-48cb-b30f-ebd77164a8c0', null);
INSERT INTO CFG_NOTIFICATIONS (ID, DESCRIPTION, NOTIFICATION_TYPE, STATUS, VAR_FIELD_1, VAR_FIELD_2, VAR_FIELD_3,
                               VAR_FIELD_4, VAR_FIELD_5, VAR_FIELD_6, VAR_FIELD_7, VAR_FIELD_8, VAR_FIELD_9,
                               VAR_FIELD_10, CREATED_ON, CREATED_BY, MODIFIED_ON, MODIFIED_BY, DELETED_ON, DELETED_BY,
                               SENDER, SPEL_PROCESSOR, EMAIL_TEMPLATE, SUBJECT, ATTACHMENT_FILE_NAME, EVENT_STATUS,
                               REQUEST_TOPIC, BEAN_PROCESSOR_NAME, METHOD_NAME, SERVICE_MAP_ID, SERVICE_REQUEST_STATUS,
                               ACTOR_CLASS, UUID, TEMPLATE_REFERENCE_NAME)
VALUES (347,
        'Dear <company_name> you have received feedback on your Standards Levy Site Visit. Please contact KEBS to check your feedback. ',
        1, 1, null, null, null, null, null, null, null, null, null, null,
        TO_TIMESTAMP('2020-06-17 21:40:34.570694', 'YYYY-MM-DD HH24:MI:SS.FF6'), 'admin', null, null, null, null,
        'alerts@bskglobaltech.com', '#fullName_#permitRefNumber', null, 'KEBS STANDARDS LEVY SITE VISIT FEEDBACK', null,
        '20', 'stage-notifications', 'sendEmailService', 'sendEmail', 208, 30, 'notificationBufferProcessingActor',
        null, null);