drop table cfg_contact_types;

create TABLE CFG_SAMPLE_STANDARDS
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    standard_title  VARCHAR2(350 CHAR) null ,
    ics             VARCHAR2(350 CHAR) null ,
    hs_code         VARCHAR2(350 CHAR) null ,
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

create sequence CFG_SAMPLE_STANDARDS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select CFG_SAMPLE_STANDARDS_seq.nextval
from dual;

create trigger CFG_SAMPLE_STANDARDS_trg
    before
        insert
    on CFG_SAMPLE_STANDARDS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SAMPLE_STANDARDS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table dat_kebs_sd_nwa_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    NWA_DOCUMENT_ID      NUMBER REFERENCES SD_NWA_JUSTIFICATION (ID),

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
create sequence dat_kebs_sd_nwa_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger dat_kebs_sd_nwa_uploads_seq_trg
    before
        insert
    on dat_kebs_sd_nwa_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
select dat_kebs_sd_nwa_uploads_seq.nextval
into :new.id
from dual;

end if;

end if;
end;

create index dat_kebs_sd_nwa_uploads_idx on dat_kebs_sd_nwa_uploads (NWA_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_is_documents_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    IS_DOCUMENT_ID      NUMBER REFERENCES SD_ADOPTION_PROPOSAL (ID),

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
create sequence sd_is_documents_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_is_documents_uploads_seq_trg
    before
        insert
    on sd_is_documents_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
select sd_is_documents_uploads_seq.nextval
into :new.id
from dual;

end if;

end if;
end;

create index sd_is_documents_uploads_idx on sd_is_documents_uploads (IS_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_di_justification_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    DI_DOCUMENT_ID      NUMBER REFERENCES SD_NWA_DISDT_JUSTIFICATION (ID),

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
create sequence sd_di_justification_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_di_justification_uploads_seq_trg
    before
        insert
    on sd_di_justification_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_di_justification_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_di_justification_uploads_idx on sd_di_justification_uploads (DI_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/
create table sd_nwa_pd_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    PD_DOCUMENT_ID      NUMBER REFERENCES SD_NWA_PRELIMINARY_DRAFT (ID),

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
create sequence sd_nwa_pd_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_nwa_pd_uploads_seq_trg
    before
        insert
    on sd_nwa_pd_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
select sd_nwa_pd_uploads_seq.nextval
into :new.id
from dual;

end if;

end if;
end;

create index sd_nwa_pd_uploads_idx on sd_nwa_pd_uploads (PD_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_nwa_wd_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    WD_DOCUMENT_ID      NUMBER REFERENCES SD_NWA_WORKSHOP_DRAFT (ID),

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
create sequence sd_nwa_wd_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_nwa_wd_uploads_seq_trg
    before
        insert
    on sd_nwa_wd_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_nwa_wd_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_nwa_wd_uploads_idx on sd_nwa_wd_uploads (WD_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_nwa_standard_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    STD_DOCUMENT_ID      NUMBER REFERENCES SD_NWA_STANDARD_TB (ID),

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
create sequence sd_nwa_standard_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_nwa_standard_uploads_seq_trg
    before
        insert
    on sd_nwa_standard_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_nwa_standard_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_nwa_standard_uploads_idx on sd_nwa_standard_uploads (STD_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_com_jc_justification_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    COM_JC_DOCUMENT_ID      NUMBER REFERENCES SD_COM_JC_JUSTIFICATION (ID),

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
create sequence sd_com_jc_justification_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_com_jc_justification_uploads_seq_trg
    before
        insert
    on sd_com_jc_justification_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_com_jc_justification_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_com_jc_justification_uploads_idx on sd_com_jc_justification_uploads (COM_JC_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table sd_com_std_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    COM_STD_DOCUMENT_ID      NUMBER REFERENCES SD_COM_STANDARD (ID),

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
create sequence sd_com_std_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_com_std_uploads_seq_trg
    before
        insert
    on sd_com_std_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_com_std_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_com_std_uploads_idx on sd_com_std_uploads (COM_STD_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/
alter table sd_com_standard_draft
    ADD
        (

    TITLE         VARCHAR2(200),
    SCOPE             VARCHAR2(50),
    NORMATIVE_REFERENCE        VARCHAR2(200),
    SYMBOLS_ABBREVIATED_TERMS    VARCHAR2(200),
    CLAUSE         VARCHAR2(200),
    SPECIAL             VARCHAR2(50),


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
) ;
create sequence sd_com_standard_draft_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
/
create table sd_is_justification_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    IS_JS_DOCUMENT_ID      NUMBER REFERENCES SD_ADOPTION_PROPOSAL_JUSTIFICATION (ID),

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
create sequence sd_is_justification_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_is_justification_uploads_seq_trg
    before
        insert
    on sd_is_justification_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_is_justification_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_is_justification_uploads_idx on sd_is_justification_uploads (IS_JS_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

/
create table sd_is_gazette_notice_uploads
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    IS_GN_DOCUMENT_ID      NUMBER REFERENCES SD_IS_GAZETTE_NOTICE (ID),

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
create sequence sd_is_gazette_notice_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger sd_is_gazette_notice_uploads_seq_trg
    before
        insert
    on sd_is_gazette_notice_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select sd_is_gazette_notice_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index sd_is_gazette_notice_uploads_idx on sd_is_gazette_notice_uploads (IS_GN_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/
alter table SD_NWA_JUSTIFICATION
    add CDN NUMBER ;
/
alter table SD_NWA_PRELIMINARY_DRAFT
    add STATUS NUMBER ;
/
alter table SD_STANDARD_DRAFT
    add DRAFT_ID NUMBER ;
/
alter table SD_STANDARD_TBL
    add SDN NUMBER ;
/
alter table SD_ADOPTION_PROPOSAL_JUSTIFICATION
    add PROPOSAL_ID NUMBER ;
/


alter table SD_NWA_DISDT_JUSTIFICATION
    add ASSIGNED_TO NUMBER ;
/
alter table SD_NWA_DISDT_JUSTIFICATION
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_NWA_PRELIMINARY_DRAFT
    add ASSIGNED_TO NUMBER ;
/
alter table SD_NWA_PRELIMINARY_DRAFT
    add PROCESS_ID VARCHAR(350 char) ;
/

alter table SD_NWA_WORKSHOP_DRAFT
    add ASSIGNED_TO NUMBER ;
/
alter table SD_NWA_WORKSHOP_DRAFT
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_NWA_STANDARD_TB
    add ASSIGNED_TO NUMBER ;
/
alter table SD_NWA_STANDARD_TB
    add PROCESS_ID VARCHAR(350 char) ;
/

alter table SD_NWA_GAZETTE_NOTICE
    add ASSIGNED_TO NUMBER ;
/
alter table SD_NWA_GAZETTE_NOTICE
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_COM_STANDARD_REQUEST
    add ASSIGNED_TO NUMBER ;
/

alter table SD_COM_STANDARD_ASSIGNEE
add PROCESS_ID NUMBER ;
/
create  table DAT_KEBS_COM_REMARKS
(
    id                   number   not null primary key,
    PROCESS_ID            VARCHAR2(350 CHAR),
    REMARKS           varchar(350 char),
    STATUS           varchar(350 char),
    REMARK_BY            varchar(350),
    ROLE          VARCHAR2(350 CHAR),
    DESCRIPTION          VARCHAR2(350 CHAR),
    DATE_OF_REMARK          VARCHAR2(350 CHAR)
)
/

create  table DAT_KEBS_SITE_VISIT_REMARKS
(
    id                   number   not null primary key,
    PROCESS_ID            VARCHAR2(350 CHAR),
    APPROVAL_ID            NUMBER,
    REMARKS           varchar(350 char),
    STATUS           varchar(350 char),
    REMARK_BY            varchar(350),
    ROLE          VARCHAR2(350 CHAR),
    DESCRIPTION          VARCHAR2(350 CHAR),
    DATE_OF_REMARK          VARCHAR2(350 CHAR)
)
/
create sequence DAT_KEBS_COM_REMARKS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger DAT_KEBS_COM_REMARKS_trg
    before
        insert
    on DAT_KEBS_COM_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_COM_REMARKS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/
create  table DAT_KEBS_INT_STD_REMARKS
(
    id                   number   not null primary key,
    PROPOSAL_ID            NUMBER,
    REMARKS           varchar(350 char),
    STATUS           varchar(350 char),
    REMARK_BY            varchar(350),
    ROLE          VARCHAR2(350 CHAR),
    DESCRIPTION          VARCHAR2(350 CHAR),
    DATE_OF_REMARK          VARCHAR2(350 CHAR)
)
/
create sequence DAT_KEBS_INT_STD_REMARKS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger DAT_KEBS_INT_STD_REMARKS_trg
    before
        insert
    on DAT_KEBS_INT_STD_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_INT_STD_REMARKS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/
alter table SD_ADOPTION_PROPOSAL_JUSTIFICATION
    add ASSIGNED_TO NUMBER ;
/

alter table SD_ADOPTION_PROPOSAL_JUSTIFICATION
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_ADOPTION_PROPOSAL
    add ASSIGNED_TO NUMBER ;
/
alter table SD_ADOPTION_PROPOSAL_COMMENTS
    add PROPOSAL_ID NUMBER ;
/

alter table SD_IS_STANDARD_TB
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_IS_STANDARD_TB
    add ASSIGNED_TO NUMBER ;
/
alter table SD_IS_GAZETTE_NOTICE
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_IS_GAZETTE_NOTICE
    add ASSIGNED_TO NUMBER ;
/

alter table SD_IS_GAZETTEMENT
    add PROCESS_ID VARCHAR(350 char) ;
/
alter table SD_IS_GAZETTEMENT
    add ASSIGNED_TO NUMBER ;
/
alter table SD_STANDARD_REVIEW
  add SCOPE VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add SCOPE VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add NORMATIVE_REFERENCE VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add SYMBOLS_ABBREVIATED_TERMS VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add CLAUSE VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add SPECIAL VARCHAR(350 char) ;
/
alter table SD_STANDARD_REVIEW
    add STANDARD_TYPE VARCHAR(350 char) ;
/
alter table SD_IS_STANDARD_TB
    add STATUS NUMBER ;
/
alter table SD_STANDARD_TBL
    add ISDN NUMBER ;
/


create  table DAT_KEBS_REVIEW_STD_REMARKS
(
    id                   number   not null primary key,
    PROPOSAL_ID            NUMBER,
    REMARKS           varchar(350 char),
    STATUS           varchar(350 char),
    REMARK_BY            varchar(350),
    ROLE          VARCHAR2(350 CHAR),
    DESCRIPTION          VARCHAR2(350 CHAR),
    DATE_OF_REMARK          TIMESTAMP
)
/
create sequence DAT_KEBS_REVIEW_STD_REMARKS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger DAT_KEBS_REVIEW_STD_REMARKS_trg
    before
        insert
    on DAT_KEBS_REVIEW_STD_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_REVIEW_STD_REMARKS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table SD_COM_REQUEST_COMMITMENT_LETTER
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    SD_COM_REQUEST_ID      NUMBER REFERENCES SD_COM_STANDARD_REQUEST (ID),

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
create sequence SD_COM_REQUEST_COMMITMENT_LETTER_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger SD_COM_REQUEST_COMMITMENT_LETTER_seq_trg
    before
        insert
    on SD_COM_REQUEST_COMMITMENT_LETTER
    for each row
begin
    if inserting then
        if :new.id is null then
            select SD_COM_REQUEST_COMMITMENT_LETTER_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index SD_COM_REQUEST_COMMITMENT_LETTER_idx on SD_COM_REQUEST_COMMITMENT_LETTER (SD_COM_REQUEST_ID, status) TABLESPACE qaimssdb_idx;
/
create  table DAT_KEBS_COM_STD_REMARKS
(
    id                   number   not null primary key,
    REQUEST_ID            NUMBER,
    REMARKS           varchar(350 char),
    STATUS           varchar(350 char),
    REMARK_BY            varchar(350),
    ROLE          VARCHAR2(350 CHAR),
    DESCRIPTION          VARCHAR2(350 CHAR),
    DATE_OF_REMARK          VARCHAR2(350 CHAR)
)
/
create sequence DAT_KEBS_COM_STD_REMARKS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger DAT_KEBS_COM_STD_REMARKS_trg
    before
        insert
    on DAT_KEBS_COM_STD_REMARKS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_COM_STD_REMARKS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

/
create  table DAT_KEBS_COM_JOINT_COMMITTEE
(
    id                   number   not null primary key,
    REQUEST_ID            NUMBER,
    NAME           varchar(350 char),
    EMAIL           varchar(350 char),
    TELEPHONE            varchar(350),
    DATE_OF_CREATION          TIMESTAMP
)
/
create sequence DAT_KEBS_COM_JOINT_COMMITTEE_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger DAT_KEBS_COM_JOINT_COMMITTEE_trg
    before
        insert
    on DAT_KEBS_COM_JOINT_COMMITTEE
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_COM_JOINT_COMMITTEE_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;



create  table SD_COM_STD_DRAFT
(
    ID                   number   primary key,
    TITLE            varchar(1000 char),
    SCOPE           varchar(4000 char),
    NORMATIVE_REFERENCE           varchar(4000 char),
    SYMBOLS_ABBREVIATED_TERMS            varchar(4000 char),
    CLAUSE            varchar(4000 char),
    SPECIAL            varchar(4000 char),
    UPLOAD_DATE          TIMESTAMP,
    DEADLINE_DATE          TIMESTAMP,
    DRAFT_NUMBER          varchar(350 char),
    UPLOADED_BY          NUMBER,
    CREATED_BY          varchar(350 char),
    REMARKS          varchar(4000 char),
    ACCENT_TO          varchar(350 char),
    TASK_ID          varchar(350 char),
    PROCESS_ID          varchar(350 char),
    ASSIGNED_TO          NUMBER,
    REQUEST_NUMBER          varchar(350 char),
    REQUEST_ID          NUMBER,
    STATUS          NUMBER,
    COM_STANDARD_NUMBER          varchar(350 char),
    DEPARTMENT          NUMBER,
    SUBJECT          varchar(350 char),
    DESCRIPTION          varchar(4000 char),
    CONTACT_ONE_FULL_NAME  varchar(4000 char),
    CONTACT_ONE_TELEPHONE  varchar(4000 char),
    CONTACT_ONE_EMAIL  varchar(4000 char),
    CONTACT_TWO_FULL_NAME  varchar(4000 char),
    CONTACT_TWO_TELEPHONE  varchar(4000 char),
    CONTACT_TWO_EMAIL  varchar(4000 char),
    CONTACT_THREE_FULL_NAME  varchar(4000 char),
    CONTACT_THREE_TELEPHONE  varchar(4000 char),
    CONTACT_THREE_EMAIL  varchar(4000 char),
    COMPANY_NAME  varchar(4000 char),
    COMPANY_PHONE  varchar(4000 char),
    COMMENT_COUNT  NUMBER


)
/
create sequence SD_COM_STD_DRAFT_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger SD_COM_STD_DRAFT_trg
    before
        insert
    on SD_COM_STD_DRAFT
    for each row
begin
    if inserting then
        if :new.id is null then
            select SD_COM_STD_DRAFT_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

/
create table SD_COM_STD_DRAFT_UPLOADS
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    COM_DRAFT_DOCUMENT_ID      NUMBER REFERENCES SD_COM_STD_DRAFT (ID),

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
create sequence SD_COM_STD_DRAFT_UPLOADS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger SD_COM_STD_DRAFT_UPLOADS_seq_trg
    before
        insert
    on SD_COM_STD_DRAFT_UPLOADS
    for each row
begin
    if inserting then
        if :new.id is null then
            select SD_COM_STD_DRAFT_UPLOADS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index SD_COM_STD_DRAFT_UPLOADS_idx on SD_COM_STD_DRAFT_UPLOADS (COM_DRAFT_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table SD_NEP_DOCUMENT_UPLOADS
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    NEP_DOCUMENT_ID      NUMBER REFERENCES SD_NEP_NOTIFICATION (ID),

    DESCRIPTION      VARCHAR2(200),
    status           NUMBER(2),
    created_by       VARCHAR2(100 CHAR)          DEFAULT 'admin' NOT NULL ENABLE,
    created_on       TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE,
    modified_by      VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on      TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by        VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on       TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;
create sequence SD_NEP_DOCUMENT_UPLOADS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger SD_NEP_DOCUMENT_UPLOADS_seq_trg
    before
        insert
    on SD_NEP_DOCUMENT_UPLOADS
    for each row
begin
    if inserting then
        if :new.id is null then
            select SD_NEP_DOCUMENT_UPLOADS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index SD_NEP_DOCUMENT_UPLOADS_idx on SD_NEP_DOCUMENT_UPLOADS (DI_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/

create table SD_STD_DRAFT_SAC_UPLOADS
(

    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    COM_DRAFT_DOCUMENT_ID      NUMBER REFERENCES SD_COM_STD_DRAFT (ID),

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
create sequence SD_STD_DRAFT_SAC_UPLOADS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger SD_STD_DRAFT_SAC_UPLOADS_seq_trg
    before
        insert
    on SD_STD_DRAFT_SAC_UPLOADS
    for each row
begin
    if inserting then
        if :new.id is null then
            select SD_STD_DRAFT_SAC_UPLOADS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index SD_STD_DRAFT_SAC_UPLOADS_idx on SD_STD_DRAFT_SAC_UPLOADS (COM_DRAFT_DOCUMENT_ID, status) TABLESPACE qaimssdb_idx;
/
alter table SD_COM_STANDARD
    ADD
        (
        DRAFT_STATUS VARCHAR2(200),
        COVER_PAGE_STATUS VARCHAR2(200)
        );
/
alter table SD_COM_STANDARD
    ADD
        (
        ASSIGNED_TO NUMBER(2)
        );
/
alter table SD_COM_STD_DRAFT
    ADD
        (
        DRAFT_STATUS VARCHAR2(200),
        COVER_PAGE_STATUS VARCHAR2(200)
        );
/
create  table CFG_SD_CATEGORIES
(
    id                   number   not null primary key,
    NAME           varchar(350 char),
    CREATED_ON          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE
)
/
create sequence CFG_SD_CATEGORIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger CFG_SD_CATEGORIES_trg
    before
        insert
    on CFG_SD_CATEGORIES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SD_CATEGORIES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create  table CFG_SD_SUB_CATEGORIES
(
    id                   number   not null primary key,
    NAME           varchar(350 char),
    STATUS           NUMBER(2),
    CATEGORY_ID      NUMBER REFERENCES CFG_SD_CATEGORIES (ID),
    CREATED_ON          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE
)
/
create sequence CFG_SD_SUB_CATEGORIES_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger CFG_SD_SUB_CATEGORIES_trg
    before
        insert
    on CFG_SD_SUB_CATEGORIES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SD_SUB_CATEGORIES_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_SD_SUB_CATEGORIES_idx on CFG_SD_SUB_CATEGORIES (CATEGORY_ID, status) TABLESPACE qaimssdb_idx;


create  table CFG_SD_STAKE_HOLDERS
(
    id                   number   not null primary key,
    NAME           varchar(350 char),
    EMAIL           varchar(350 char),
    TELEPHONE           varchar(350 char),
    STATUS           NUMBER(2),
    SUB_CATEGORY_ID      NUMBER REFERENCES CFG_SD_SUB_CATEGORIES (ID),
    CREATED_ON          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate NOT NULL ENABLE
)
/
create sequence CFG_SD_STAKE_HOLDERS_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;
create or replace trigger CFG_SD_STAKE_HOLDERS_trg
    before
        insert
    on CFG_SD_STAKE_HOLDERS
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_SD_STAKE_HOLDERS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_SD_STAKE_HOLDERS_idx on CFG_SD_STAKE_HOLDERS (SUB_CATEGORY_ID, status) TABLESPACE qaimssdb_idx;






