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
create or replace trigger sd_nwa_wd_uploads_seq_trg
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
