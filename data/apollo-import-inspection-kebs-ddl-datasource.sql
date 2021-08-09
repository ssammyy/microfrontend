create table DAT_KEBS_MAIN_RFC_COC(
  id               number   not null primary key,
  country_of_destination          VARCHAR2(100) not null,
  application_type                VARCHAR2(50) not null,
  idf_number                      VARCHAR2(50) null,
  ucr_number                      VARCHAR2(50) not null,
  route_used                      VARCHAR2(50) null ,
  sor_sol_ref                     VARCHAR2(50) null ,
  importer_name                   VARCHAR2(1000) not null,
  importer_pin                    VARCHAR2(100) not null,
  importer_address_1              VARCHAR2(1000) not null,
  importer_address_2              VARCHAR2(1000) null,
  importer_city                   VARCHAR2(50) not null,
  importer_country                VARCHAR2(100) not null,
  importer_zip_code               VARCHAR2(100) not null,
  importer_telephone_number       VARCHAR2(100) not null,
  importer_fax_number             VARCHAR2(100) null,
  importer_email                  VARCHAR2(100) not null ,
  exporter_name                   VARCHAR2(1000) not null,
  exporter_pin                    VARCHAR2(100) not null,
  exporter_address_1              VARCHAR2(1000) not null,
  exporter_address_2              VARCHAR2(1000) null,
  exporter_city                   VARCHAR2(50) not null,
  exporter_country                VARCHAR2(100) not null,
  exporter_zip_code               VARCHAR2(100) not null,
  exporter_telephone_number       VARCHAR2(100) not null,
  exporter_fax_number             VARCHAR2(100) null,
  exporter_email                  VARCHAR2(100) not null ,
  third_party_name               VARCHAR2(1000) not null,
  third_party_pin                 VARCHAR2(100) not null,
  third_party_address_1           VARCHAR2(1000) not null,
  third_party_address_2           VARCHAR2(1000) null,
  third_party_city                VARCHAR2(50) not null,
  third_party_country             VARCHAR2(100) not null,
  third_party_zip_code            VARCHAR2(100) not null,
  third_party_telephone_number    VARCHAR2(100) not null,
  third_party_fax_number          VARCHAR2(100) null,
  third_party_email               VARCHAR2(100) not null ,
  applicant_name                 VARCHAR2(1000) not null,
  applicant_pin                   VARCHAR2(100) not null,
  applicant_address_1             VARCHAR2(1000) not null,
  applicant_address_2             VARCHAR2(1000) null,
  applicant_city                  VARCHAR2(50) not null,
  applicant_country               VARCHAR2(100) not null,
  applicant_zip_code              VARCHAR2(100) not null,
  applicant_telephone_number      VARCHAR2(100) not null,
  applicant_fax_number            VARCHAR2(100) null,
  applicant_email                 VARCHAR2(100) not null ,
  place_of_inspection_name        VARCHAR2(200) not null ,
  place_of_inspection_address     VARCHAR2(100) not null ,
  place_of_inspection_email       VARCHAR2(120) not null ,
  place_of_inspection_contacts    VARCHAR2(20) not null ,
  shipping_method                 VARCHAR2(50) not null ,
  port_of_loading                 VARCHAR2(100) null ,
  port_of_discharge               VARCHAR2(100) not null ,
  country_of_supply               VARCHAR2(100) not null ,
  goods_condition                 VARCHAR2(250) not null ,
  assembly_state                  VARCHAR2(250) null ,
  list_of_documents_attached      VARCHAR2(4000) not null ,
  partner          VARCHAR2(50) not null ,
  status           NUMBER(2, 0),
  descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_MAIN_RFC_COC_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MAIN_RFC_COC_trg
    before
        insert
    on DAT_KEBS_MAIN_RFC_COC
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MAIN_RFC_COC_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MAIN_RFC_COI(
  id               number   not null primary key,
  country_of_destination          VARCHAR2(100) not null,
  application_type                VARCHAR2(50) not null,
  idf_number                      VARCHAR2(50) null,
  ucr_number                      VARCHAR2(50) not null,
  route_used                      VARCHAR2(50) null ,
  importer_name                   VARCHAR2(1000) not null,
  importer_pin                    VARCHAR2(100) not null,
  importer_address_1              VARCHAR2(1000) not null,
  importer_address_2              VARCHAR2(1000) null,
  importer_city                   VARCHAR2(50) not null,
  importer_country                VARCHAR2(100) not null,
  importer_zip_code               VARCHAR2(100) not null,
  importer_telephone_number       VARCHAR2(100) not null,
  importer_fax_number             VARCHAR2(100) null,
  importer_email                  VARCHAR2(100) not null ,
  exporter_name                   VARCHAR2(1000) not null,
  exporter_pin                    VARCHAR2(100) not null,
  exporter_address_1              VARCHAR2(1000) not null,
  exporter_address_2              VARCHAR2(1000) null,
  exporter_city                   VARCHAR2(50) not null,
  exporter_country                VARCHAR2(100) not null,
  exporter_zip_code               VARCHAR2(100) not null,
  exporter_telephone_number       VARCHAR2(100) not null,
  exporter_fax_number             VARCHAR2(100) null,
  exporter_email                  VARCHAR2(100) not null ,
  place_of_inspection_name        VARCHAR2(200) not null ,
  place_of_inspection_address     VARCHAR2(100) not null ,
  place_of_inspection_email       VARCHAR2(120) not null ,
  place_of_inspection_contacts    VARCHAR2(20) not null ,
  shipping_method                 VARCHAR2(50) not null ,
  port_of_loading                 VARCHAR2(100) null ,
  port_of_discharge               VARCHAR2(100) not null ,
  country_of_supply               VARCHAR2(100) not null ,
  goods_condition                 VARCHAR2(250) not null ,
  assembly_state                  VARCHAR2(250) null ,
  list_of_documents_attached      VARCHAR2(4000) not null ,
  partner          VARCHAR2(50) not null ,
  status           NUMBER(2, 0),
  descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_MAIN_RFC_COI_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MAIN_RFC_COI_trg
    before
        insert
    on DAT_KEBS_MAIN_RFC_COI
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MAIN_RFC_COI_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MAIN_RFC_COI_ITEMS(
id               number   not null primary key,
RFC_ID              NUMBER  REFERENCES DAT_KEBS_MAIN_RFC_COI(ID),
DECLARED_HS_CODE    VARCHAR2(100 char) not null,
ITEM_QUANTITY       VARCHAR2(1000 char),
PRODUCT_DESCRIPTION VARCHAR2(1000 char),
OWNER_PIN           VARCHAR2(100 char),
OWNER_NAME          VARCHAR2(1000 char),
status           NUMBER(2, 0),
descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_MAIN_RFC_COI_ITEMS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MAIN_RFC_COI_ITEMS_trg
    before
        insert
    on DAT_KEBS_MAIN_RFC_COI_ITEMS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MAIN_RFC_COI_ITEMS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_KEBS_MAIN_RFC_COR(
  id               number   not null primary key,
  country_of_destination          VARCHAR2(100) not null,
  application_type                VARCHAR2(50) not null,
  idf_number                      VARCHAR2(50) null,
  ucr_number                      VARCHAR2(50) not null,
  route_used                      VARCHAR2(50) null ,
  sor_sol_ref                     VARCHAR2(50) null ,
  importer_name                   VARCHAR2(1000) not null,
  importer_pin                    VARCHAR2(100) not null,
  importer_address_1              VARCHAR2(1000) not null,
  importer_address_2              VARCHAR2(1000) null,
  importer_city                   VARCHAR2(50) not null,
  importer_country                VARCHAR2(100) not null,
  importer_zip_code               VARCHAR2(100) not null,
  importer_telephone_number       VARCHAR2(100) not null,
  importer_fax_number             VARCHAR2(100) null,
  importer_email                  VARCHAR2(100) not null ,
  exporter_name                   VARCHAR2(1000) not null,
  exporter_pin                    VARCHAR2(100) not null,
  exporter_address_1              VARCHAR2(1000) not null,
  exporter_address_2              VARCHAR2(1000) null,
  exporter_city                   VARCHAR2(50) not null,
  exporter_country                VARCHAR2(100) not null,
  exporter_zip_code               VARCHAR2(100) not null,
  exporter_telephone_number       VARCHAR2(100) not null,
  exporter_fax_number             VARCHAR2(100) null,
  exporter_email                  VARCHAR2(100) not null ,
  applicant_name                 VARCHAR2(1000) not null,
  applicant_pin                   VARCHAR2(100) not null,
  applicant_address_1             VARCHAR2(1000) not null,
  applicant_address_2             VARCHAR2(1000) null,
  applicant_city                  VARCHAR2(50) not null,
  applicant_country               VARCHAR2(100) not null,
  applicant_zip_code              VARCHAR2(100) not null,
  applicant_telephone_number      VARCHAR2(100) not null,
  applicant_fax_number            VARCHAR2(100) null,
  applicant_email                 VARCHAR2(100) not null ,
  place_of_inspection             VARCHAR2(200) not null ,
  place_of_inspection_address     VARCHAR2(100) not null ,
  place_of_inspection_email       VARCHAR2(120) not null ,
  place_of_inspection_contacts    VARCHAR2(20) not null ,
  shipping_method                 VARCHAR2(50) not null ,
  port_of_loading                 VARCHAR2(100) null ,
  port_of_discharge               VARCHAR2(100) not null ,
  country_of_supply               VARCHAR2(100) not null ,
  goods_condition                 VARCHAR2(250) not null ,
  assembly_state                  VARCHAR2(250) null ,
  list_of_documents_attached      VARCHAR2(4000) not null ,
  rfc_date                        DATE not null,
  preferred_date_of_inspection    DATE not null ,
  make          VARCHAR2(50) not null ,
  model          VARCHAR2(50) not null ,
  chassis_number          VARCHAR2(50) not null ,
  engine_number          VARCHAR2(50) not null ,
  engine_capacity          VARCHAR2(10) not null ,
  year_of_manufacture          VARCHAR2(10) not null ,
  year_of_first_registration          VARCHAR2(10) not null ,
  partner          VARCHAR2(50) not null ,
  status           NUMBER(2, 0),
  descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_MAIN_RFC_COR_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_MAIN_RFC_COR_trg
    before
        insert
    on DAT_KEBS_MAIN_RFC_COR
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_MAIN_RFC_COR_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/



create table DAT_KEBS_RFC_DOCUMENTS_DETAILS(
id               number   not null primary key,
rfc_number                      VARCHAR2(50) null unique,
rfc_date                        DATE null,
idf_number                      VARCHAR2(50) null,
ucr_number                      VARCHAR2(50) not null,
country_of_destination          VARCHAR2(100) not null,
application_type                VARCHAR2(50) not null,
sor_reference                    VARCHAR2(50) null ,
sol_reference                    VARCHAR2(50) null ,
importer_name                   VARCHAR2(1000) not null,
importer_pin                    VARCHAR2(100) not null,
importer_address_1              VARCHAR2(1000) not null,
importer_address_2              VARCHAR2(1000) null,
importer_city                   VARCHAR2(50) not null,
importer_country                VARCHAR2(100) not null,
importer_zip_code               VARCHAR2(100) not null,
importer_telephone_number       VARCHAR2(100) not null,
importer_fax_number             VARCHAR2(100) null,
importer_email                  VARCHAR2(100) not null ,
exporter_name                   VARCHAR2(1000) not null,
exporter_pin                    VARCHAR2(100) not null,
exporter_address_1              VARCHAR2(1000) not null,
exporter_address_2              VARCHAR2(1000) null,
exporter_city                   VARCHAR2(50) not null,
exporter_country                VARCHAR2(100) not null,
exporter_zip_code               VARCHAR2(100) not null,
exporter_telephone_number       VARCHAR2(100) not null,
exporter_fax_number             VARCHAR2(100) null,
exporter_email                  VARCHAR2(100) not null ,
third_party_name               VARCHAR2(1000) not null,
third_party_pin                 VARCHAR2(100) not null,
third_party_address_1           VARCHAR2(1000) not null,
third_party_address_2           VARCHAR2(1000) null,
third_party_city                VARCHAR2(50) not null,
third_party_country             VARCHAR2(100) not null,
third_party_zip_code            VARCHAR2(100) not null,
third_party_telephone_number    VARCHAR2(100) not null,
third_party_fax_number          VARCHAR2(100) null,
third_party_email               VARCHAR2(100) not null ,
applicant_name                 VARCHAR2(1000) not null,
applicant_pin                   VARCHAR2(100) not null,
applicant_address_1             VARCHAR2(1000) not null,
applicant_address_2             VARCHAR2(1000) null,
applicant_city                  VARCHAR2(50) not null,
applicant_country               VARCHAR2(100) not null,
applicant_zip_code              VARCHAR2(100) not null,
applicant_telephone_number      VARCHAR2(100) not null,
applicant_fax_number            VARCHAR2(100) null,
applicant_email                 VARCHAR2(100) not null ,
place_of_inspection             VARCHAR2(200) not null ,
place_of_inspection_address     VARCHAR2(100) not null ,
place_of_inspection_email       VARCHAR2(120) not null ,
place_of_inspection_contacts    VARCHAR2(20) not null ,
port_of_loading                 VARCHAR2(100) null ,
port_of_discharge               VARCHAR2(100) not null ,
shipping_method                 VARCHAR2(50) not null ,
country_of_supply               VARCHAR2(100) not null ,
route                           VARCHAR2(50) null ,
goods_condition                 VARCHAR2(250) not null ,
assembly_state                  VARCHAR2(250) null ,
list_to_attached_documents      VARCHAR2(4000) not null ,
preferred_date_of_inspection    DATE not null ,
make          VARCHAR2(50) not null ,
model          VARCHAR2(50) not null ,
chassis_number          VARCHAR2(50) not null ,
engine_number          VARCHAR2(50) not null ,
engine_capacity          VARCHAR2(10) not null ,
year_of_manufacture          VARCHAR2(10) not null ,
year_of_first_registration          VARCHAR2(10) not null ,
partner          VARCHAR2(50) not null ,
importer_id      NUMBER REFERENCES DAT_KEBS_USERS(ID),
rfc_type_id      NUMBER REFERENCES CFG_KEBS_RFC_TYPES(ID),
status           NUMBER(2),
descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_RFC_DOCUMENTS_DETAILS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_RFC_DOCUMENTS_DETAILS_trg
    before
        insert
    on DAT_KEBS_RFC_DOCUMENTS_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_RFC_DOCUMENTS_DETAILS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create index DAT_KEBS_RFC_DOCUMENTS_DETAILS_idx on DAT_KEBS_RFC_DOCUMENTS_DETAILS (rfc_type_id,importer_id, status) TABLESPACE qaimssdb_idx;
/

create sequence CFG_KEBS_RFC_TYPES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_RFC_TYPES_SEQ_TRG
    before
        insert
    on CFG_KEBS_RFC_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_RFC_TYPES_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_RFC_TYPES_SEQ_idx on CFG_KEBS_RFC_TYPES (HAS_ITEMS,TYPE_NAME, status) TABLESPACE qaimssdb_idx;
/

create table CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES
(
    id               NUMBER PRIMARY KEY,
    TYPE_NAME   VARCHAR2(200),
    MARK   VARCHAR2(200),
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

create sequence CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES_SEQ_TRG
    before
        insert
    on CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES
    for each row
begin
    if inserting then
        if :new.id is null then
            select CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES_SEQ.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES_SEQ_idx on CFG_KEBS_IMPORTER_DI_APPLICATIONS_TYPES (TYPE_NAME, status) TABLESPACE qaimssdb_idx;
/

create table DAT_KEBS_RFC_COI_ITEMS_DETAILS(
id               number   not null primary key,
RFC_ID              NUMBER  REFERENCES DAT_KEBS_RFC_DOCUMENTS_DETAILS(ID),
DECLARED_HS_CODE    VARCHAR2(100 char) not null,
ITEM_QUANTITY       VARCHAR2(1000 char),
PRODUCT_DESCRIPTION VARCHAR2(1000 char),
OWNER_PIN           VARCHAR2(100 char),
OWNER_NAME          VARCHAR2(1000 char),
status           NUMBER(2),
descriptions     VARCHAR2(3800 CHAR),
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
)TABLESPACE qaimssdb_data
;

create sequence DAT_KEBS_RFC_COI_ITEMS_DETAILS_SEQ minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger DAT_KEBS_RFC_COI_ITEMS_DETAILS_trg
    before
        insert
    on DAT_KEBS_RFC_COI_ITEMS_DETAILS
    for each row
begin
    if inserting then
        if :new.id is null then
            select DAT_KEBS_RFC_COI_ITEMS_DETAILS_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create index DAT_KEBS_RFC_COI_ITEMS_DETAILS_idx on DAT_KEBS_RFC_COI_ITEMS_DETAILS (RFC_ID, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_temporary_import_applications
(
    id               NUMBER PRIMARY KEY,
    ENTRY_NUMBER   VARCHAR2(200),
    ENTRY_POINT   VARCHAR2(200),
    BILL_OF_LANDING_AIRWAY_BILL   VARCHAR2(200),
    PRODUCT_DESCRIPTION   VARCHAR2(200),
    DATE_SHIPPED_ON_Board   DATE,
    IDF_NO   VARCHAR2(200),
    UCR_NO   VARCHAR2(200),
    KRA_BOND   VARCHAR2(200),
    TRANSACTION_DATE  DATE,
    IMPORTER_ID NUMBER REFERENCES DAT_KEBS_USERS(ID),
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

create sequence dat_kebs_temporary_import_applications_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_temporary_import_applications_seq_trg
    before
        insert
    on dat_kebs_temporary_import_applications
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_temporary_import_applications_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_temporary_import_applications_idx on dat_kebs_temporary_import_applications (IMPORTER_ID, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_temporary_import_applications_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    DESCRIPTION_NAME      VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    TEMPORARY_IMPORT_APPLICATION_ID      NUMBER REFERENCES dat_kebs_temporary_import_applications (ID),
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

create sequence dat_kebs_temporary_import_applications_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_temporary_import_applications_uploads_seq_trg
    before
        insert
    on dat_kebs_temporary_import_applications_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_temporary_import_applications_uploads_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_temporary_import_applications_uploads_idx on dat_kebs_temporary_import_applications_uploads (TEMPORARY_IMPORT_APPLICATION_ID, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cs_approval_applications
(
    id               NUMBER PRIMARY KEY,
    ENTRY_NUMBER   VARCHAR2(200),
    ENTRY_POINT   VARCHAR2(200),
    BILL_OF_LANDING_AIRWAY_BILL   VARCHAR2(200),
    PRODUCT_DESCRIPTION   VARCHAR2(200),
    IDF_NO   VARCHAR2(200),
    UCR_NO   VARCHAR2(200),
    TRANSACTION_DATE  DATE,
    IMPORTER_ID NUMBER REFERENCES DAT_KEBS_USERS(ID),
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

create sequence dat_kebs_cs_approval_applications_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cs_approval_applications_seq_trg
    before
        insert
    on dat_kebs_cs_approval_applications
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cs_approval_applications_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cs_approval_applications_idx on dat_kebs_cs_approval_applications (IMPORTER_ID, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cs_approval_applications_uploads
(
    id               NUMBER PRIMARY KEY,
    FILEPATH         VARCHAR2(200),
    DESCRIPTION_NAME      VARCHAR2(200),
    NAME             VARCHAR2(50),
    FILE_TYPE        VARCHAR2(200),
    DOCUMENT_TYPE    VARCHAR2(200),
    DOCUMENT         BLOB,
    TRANSACTION_DATE DATE,
    CS_APPROVAL_APPLICATION_ID      NUMBER REFERENCES dat_kebs_cs_approval_applications (ID),
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

create sequence dat_kebs_cs_approval_applications_uploads_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cs_approval_applications_uploads_seq_trg
    before
        insert
    on dat_kebs_cs_approval_applications_uploads
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cs_approval_applications_uploads_seq.nextval
            into :new.id
            from dual;

end if;

end if;
end;

create
index dat_kebs_cs_approval_applications_uploads_idx on dat_kebs_cs_approval_applications_uploads (CS_APPROVAL_APPLICATION_ID, status) TABLESPACE qaimssdb_idx;
/



CFG_KEBS_CDCFS_USERCFS