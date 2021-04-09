drop table dat_kebs_cd cascade constraints;

create table dat_kebs_cd_filter_details
(
    id                    NUMBER  PRIMARY KEY,
    assigned_filter       NUMBER(2),
    not_assigned_filter   NUMBER(2),
    date_from             DATE,
    date_to               DATE,
    description         VARCHAR2(350),
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_filter_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_filter_details_seq_trg
    before
        insert
    on dat_kebs_cd_filter_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_filter_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

create table DAT_CONSIGNMENT_DOCUMENT
(
    id                         NUMBER  PRIMARY KEY,
    references_no               NUMBER        not null,
    VERSIONNO                  NUMBER        not null,
    MASTERAPPROVALVERSIONNO    NUMBER        not null,
    UCRNUMBER                  VARCHAR2(250) not null,
    DOCUMENTTYPE               VARCHAR2(250) not null,
    PROCESS                    VARCHAR2(250) not null,
    APPROVALSTATUS             VARCHAR2(250) not null,
    APPLICATIONDATE            DATE          not null,
    ISSUANCEDATE               DATE          not null,
    EXPIRYDATE                 DATE          not null,
    AMENDEDDATE                DATE          not null,
    USEDSTATUS                 VARCHAR2(20)  not null,
    USEDDATE                   DATE          not null,
    FOREIGNCURRENCYCODE        VARCHAR2(20)  not null,
    FREIGHTFCY                 FLOAT         not null,
    INSURANCENCY               FLOAT         not null,
    FOREXRATE                  FLOAT         not null,
    INSURANCEFCY               VARCHAR2(20)  not null,
    FOBNCY                     VARCHAR2(20)  not null,
    OTHERCHARGESNCY            VARCHAR2(20)  not null,
    FOBFCY                     VARCHAR2(20)  not null,
    OTHERCHARGESFCY            VARCHAR2(20)  not null,
    FREIGHTNCY                 VARCHAR2(20)  not null,
    CIFNCY                     VARCHAR2(20)  not null,
    OGAREMARKS                 VARCHAR2(20)  not null,
    CONDITIONSOFAPPROVAL       VARCHAR2(20)  not null,
    PURPOSEOFIMPORTOREXPORT    VARCHAR2(20)  not null,
    TERMSANDCONDITIONS         VARCHAR2(20)  not null,
    MODEOFTRANSPORT            VARCHAR2(20)  not null,
    PORTOFARRIVAL              VARCHAR2(20)  not null,
    FREIGHTSTATION             VARCHAR2(20)  not null,
    MODEOFTRANSPORTDESCRIPTION VARCHAR2(20)  not null,
    MUSTOMSOFFICE              VARCHAR2(20)  not null,
    CARGOTYPEINDICATOR         VARCHAR2(20)  not null,
    VARFIELD1                  VARCHAR2(250),
    VARFIELD2                  VARCHAR2(250),
    VARFIELD3                  VARCHAR2(250),
    VARFIELD4                  VARCHAR2(250),
    VARFIELD5                  VARCHAR2(250),
    VARFIELD6                  VARCHAR2(250),
    VARFIELD7                  VARCHAR2(250),
    VARFIELD8                  VARCHAR2(250),
    VARFIELD9                  VARCHAR2(250),
    VARFIELD10                 VARCHAR2(250),
    CREATEDBY                  VARCHAR2(100),
    CREATEDON                  TIMESTAMP(6),
    MODIFIEDBY                 VARCHAR2(50),
    DELETEBY                   VARCHAR2(50),
    DELETEDON                  TIMESTAMP(6),
    ITEMID                     NUMBER,
    CONSIGNEEID                NUMBER,
    IMPORTERID                 NUMBER,
    EXPORTERID                 NUMBER,
    DOCUMENTNAME               VARCHAR2(250),
    APPLICANTID                NUMBER
) TABLESPACE apollo_data;

create sequence dat_consignment_document_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_consignment_document_trg
    before
        insert
    on dat_consignment_document
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_consignment_document_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

*******************************************************************************************************************************


create table dat_kebs_cd_laboratory_parameters
(
    id                    NUMBER  PRIMARY KEY,
    laboratory_id         NUMBER REFERENCES dat_kebs_cd_laboratory(id),
    parameters          VARCHAR2(500),
    sample_submission_id NUMBER REFERENCES dat_kebs_cd_sample_submission_items(id),
    description         VARCHAR2(350),
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_laboratory_parameters_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_laboratory_parameters_seq_trg
    before
        insert
    on dat_kebs_cd_laboratory_parameters
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_laboratory_parameters_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_laboratory_parameters_idx on dat_kebs_cd_laboratory_parameters ( laboratory_id, sample_submission_id, status) TABLESPACE qaimssdb_idx;
/


create table cfg_kebs_pvoc_partners_region
(
    id                    NUMBER  PRIMARY KEY,
    region_name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350),
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_kebs_pvoc_partners_region_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_pvoc_partners_region_seq_trg
    before
        insert
    on cfg_kebs_pvoc_partners_region
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_pvoc_partners_region_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_pvoc_partners_region_idx on cfg_kebs_pvoc_partners_region ( region_name, status) TABLESPACE qaimssdb_idx;
/


create table cfg_kebs_pvoc_partners_countries
(
    id                    NUMBER  PRIMARY KEY,
    country_name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350),
    status              VARCHAR2(20),
    region_id           NUMBER,
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_kebs_pvoc_partners_countries_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_pvoc_partners_countries_seq_trg
    before
        insert
    on cfg_kebs_pvoc_partners_countries
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_pvoc_partners_countries_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_pvoc_partners_countries_idx on cfg_kebs_pvoc_partners_countries ( country_name, region_id, status) TABLESPACE qaimssdb_idx;
/


create table cfg_kebs_destination_inspection_fee
(
    id                    NUMBER  PRIMARY KEY,
    description         VARCHAR2(350) NOT NULL ,
    rate                VARCHAR2(20),
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_kebs_destination_inspection_fee_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_destination_inspection_fee_seq_trg
    before
        insert
    on cfg_kebs_destination_inspection_fee
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_destination_inspection_fee_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_destination_inspection_fee_idx on cfg_kebs_destination_inspection_fee ( description, status) TABLESPACE qaimssdb_idx;
/



create table cfg_tariffs_types
(
    id                    NUMBER  PRIMARY KEY,
    heading_no           NUMBER NOT NULL,
    h_s_code_tariff_no   NUMBER NOT NULL,
    description         VARCHAR2(3500) NOT NULL,
    unit_quantity           NUMBER NOT NULL,
    rate                NUMBER NOT NULL ,
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_tariffs_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_tariffs_types_seq_trg
    before
        insert
    on cfg_tariffs_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_tariffs_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_tariffs_types_idx on cfg_tariffs_types ( type_name, status) TABLESPACE qaimssdb_idx;
/

drop table dat_kebs_cd_inspection_checklist;
create table dat_kebs_cd_inspection_checklist
(
    id                      NUMBER  PRIMARY KEY,
    inspection              VARCHAR2(200),
    category                VARCHAR2(200),
    entry_point             VARCHAR2(200),
    CFS                     VARCHAR(200),
    inspection_date                     Date,
    importers_name                  VARCHAR(200),
    clearing_agent                  VARCHAR(200),
    customs_entry_number            VARCHAR(200),
    IDF_number                      VARCHAR(200),
    ucr_number                      VARCHAR(200),
    coc_number                      VARCHAR2(200),
    fee_paid                        VARCHAR2(200),
    receipt_number                  VARCHAR2(200),
    items_name                      VARCHAR2(200),
    serial_number                   VARCHAR2(200),
    product_description             VARCHAR2(200),
    brand                           VARCHAR2(200),
    ks_eas_applicable               VARCHAR2(200),
    quantity_declared               VARCHAR2(200),
    quantity_verified               VARCHAR2(200),
    date_mfg_packaging              Date,
    date_expiry                     Date,
    mfg_name                        VARCHAR2(200),
    mfg_address                     VARCHAR2(200),
    composition_ingredients         VARCHAR2(3800),
    storage_condition               VARCHAR2(200),
    appearance                      VARCHAR2(200),
    cert_marks_pvoc_doc             VARCHAR2(200),
    sampled                         VARCHAR2(200),
    batch_no_model_type_ref         VARCHAR2(200),
    fiber_composition               VARCHAR2(200),
    instructions_use_manual         VARCHAR2(200),
    warranty_period_documentaion  VARCHAR2(200),
    safety_cautionary_remarks      VARCHAR2(200),
    disposal_instruction            VARCHAR2(200),
    size_class_capacity             VARCHAR2(200),
    make_vehicle                    VARCHAR2(200),
    chasis_no                       VARCHAR2(200),
    engine_no_capacity              VARCHAR2(200),
    manufacture_date            Date,
    registartion_date           Date,
    odemetre_reading            VARCHAR2(200),
    drive_rhd_lhd               VARCHAR2(200),
    transmision_auto_manual     VARCHAR2(200),
    colour                      VARCHAR2(200),
    overal_appearance           VARCHAR2(200),
    remarks                     VARCHAR2(200),
    packaging_labellig          VARCHAR2(200),
    physical_condition          VARCHAR2(200),
    defects                     VARCHAR2(200),
    presence_absence_banned     VARCHAR2(200),
    documentation               VARCHAR2(200),
    overal_remarks              VARCHAR2(200),
    kebs_officer                VARCHAR2(200),
    officer_date                Date,
    c_s_name                    VARCHAR2(200),
    c_s_date                    Date,
    o_i_c_name                  VARCHAR2(200),
    o_i_c_date                  Date,
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

drop sequence dat_kebs_cd_inspection_checklist_seq;

create sequence dat_kebs_cd_inspection_checklist_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_inspection_checklist_seq.nextval
from dual;

create trigger dat_kebs_cd_inspection_checklist_seq_trg
    before
        insert
    on dat_kebs_cd_inspection_checklist
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_inspection_checklist_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_sample_submission_items_idx on dat_kebs_cd_sample_submission_items (scf_no,coc_number, ucr_number, status, item_hscode, item_id) TABLESPACE qaimssdb_idx;
/

create table cf_cd_target_types
(
    id                    NUMBER  PRIMARY KEY,
    type_Name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350) NOT NULL,
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_cd_traget_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_cd_traget_types_seq_trg
    before
        insert
    on cfg_cd_traget_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_cd_traget_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_cd_traget_types_idx on cfg_cd_traget_types ( type_name, status) TABLESPACE qaimssdb_idx;
/

create table cfg_cd_status_types
(
    id                    NUMBER  PRIMARY KEY,
    type_Name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350) NOT NULL,
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_cd_status_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_cd_status_types_seq_trg
    before
        insert
    on cfg_cd_status_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_cd_status_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_cd_status_types_idx on cfg_cd_status_types ( id, type_name, status) TABLESPACE qaimssdb_idx;
/

create table cf_cd_process_types
(
    id                    NUMBER  PRIMARY KEY,
    type_Name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350) NOT NULL,
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cf_cd_process_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cf_cd_process_types_seq_trg
    before
        insert
    on cf_cd_process_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cf_cd_process_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cf_cd_process_types_idx on cf_cd_process_types ( id, type_name, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_temporary_imports
(
    id                    NUMBER  PRIMARY KEY,
    KYC                     VARCHAR2(20) ,
    description_goods         VARCHAR2(3800) NOT NULL,
    entry_number        VARCHAR2(350 CHAR),
    ucr_number        VARCHAR2(350 CHAR),
    idf_number        VARCHAR2(350 CHAR),
    bill_of_landing    VARCHAR2(350 CHAR),
    kra_bond           VARCHAR2(350 CHAR),
    importation_doc     VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_temporary_imports_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_temporary_imports_seq_trg
    before
        insert
    on dat_kebs_cd_temporary_imports
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_temporary_imports_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_temporary_imports_idx on dat_kebs_cd_temporary_imports ( type_name, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_demand_note
(
    id                    NUMBER  PRIMARY KEY,
    name_importer           VARCHAR2(200),
    address                 VARCHAR2(200),
    telephone               VARCHAR2(200),
    product                 VARCHAR2(500),
    c_f_value               VARCHAR2(200),
    rate                    VARCHAR2(200),
    amount_payable          VARCHAR2(200),
    entry_abl_number        VARCHAR2(200),
    pro_ide_number          VARCHAR2(200),
    kpa_kahl_number         VARCHAR2(200),
    KYC                     VARCHAR2(20) ,
    date_generated          DATE,
    description_goods       VARCHAR2(3800) NOT NULL,
    ucr_number          VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_demand_note_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_demand_note_seq_trg
    before
        insert
    on dat_kebs_cd_demand_note
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_demand_note_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_demand_note_idx on dat_kebs_cd_demand_note ( ucr_number, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_mpesa_transaction
(
    id                  NUMBER  PRIMARY KEY,
    amount              NUMBER(10,2),
    mpesaReceiptNumber  VARCHAR2(200),
    transactionDate      DATE,
    phoneNumber         VARCHAR2(50),
    status              NUMBER(2,0),
    descriptions        VARCHAR2(350),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_mpesa_transaction_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_mpesa_transaction_seq_trg
    before
        insert
    on dat_kebs_mpesa_transaction
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_mpesa_transaction_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_mpesa_transaction_idx on dat_kebs_mpesa_transaction ( phoneNumber, transactionDate, mpesaReceiptNumber, status) TABLESPACE qaimssdb_idx;
/

create table cfg_status_values
(
    id                  NUMBER  PRIMARY KEY,
    status_for          VARCHAR2(10),
    status_value        NUMBER(2,0),
    status              NUMBER(2,0),
    descriptions        VARCHAR2(350),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_status_values_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_status_values_seq_trg
    before
        insert
    on cfg_status_values
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_status_values_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_status_values_idx on cfg_status_values ( status_for, status_value, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_sample_collection
(
    id                    NUMBER  PRIMARY KEY,
    product_brand_name     VARCHAR2(200),
    batch_number           VARCHAR2(200),
    batch_size              VARCHAR2(200),
    sample_size             VARCHAR2(200),
    sampling_method         varchar2(350),
    reasons_collect_sample  varchar2(3800),
    collect_any_remarks     varchar2(3800),
    ucr_number          VARCHAR2(350),
    manuf_trader_name   VARCHAR2(350),
    manuf_trader_address   VARCHAR2(350),
    item_hscode   VARCHAR2(350),
    item_id   NUMBER,
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 ),
    var_field_2         VARCHAR2(350 ),
    var_field_3         VARCHAR2(350 ),
    var_field_4         VARCHAR2(350 ),
    var_field_5         VARCHAR2(350 ),
    var_field_6         VARCHAR2(350 ),
    var_field_7         VARCHAR2(350 ),
    var_field_8         VARCHAR2(350 ),
    var_field_9         VARCHAR2(350 ),
    var_field_10        VARCHAR2(350 ),
    created_by          VARCHAR2(100 ) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 )          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 )          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_sample_collection_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_sample_collection_seq.nextval
from dual;

create trigger dat_kebs_cd_sample_collection_seq_trg
    before
        insert
    on dat_kebs_cd_sample_collection
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_sample_collection_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_sample_collection_idx on dat_kebs_cd_sample_collection (ucr_number, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_sample_submission
(
    id                    NUMBER  PRIMARY KEY,
    name_product          VARCHAR2(200),
    packaging              VARCHAR(200),
    lb_id_trade_mark       VARCHAR(200),
    lb_id_date_of_manf       VARCHAR(200),
    lb_id_expiry_date       VARCHAR(200),
    lb_id_cont_decl         VARCHAR(200),
    lb_id_batch_no         VARCHAR(200),
    lb_id_any_aomarking       VARCHAR(200),
    size_test_sample       VARCHAR2(200),
    size_ref_sample       VARCHAR2(200),
    customer_name_org           VARCHAR2(200),
    customer_name_address           VARCHAR2(200),
    customer_name_telephone          VARCHAR2(200),
    customer_name_email           VARCHAR2(200),
    customer_name_date           VARCHAR2(200),
    file_reference_no       VARCHAR2(200),
    scf_no                  VARCHAR2(200),
    sample_reference_no       VARCHAR2(200),
    condition_sample         VARCHAR2(200),
    test_request            VARCHAR2(200),
    test_charges            VARCHAR2(200),
    receipt_no              VARCHAR2(200),
    invoice_no              VARCHAR2(200),
    receivers_name          VARCHAR2(200),
    note_trans_results      VARCHAR2(200),
    disposal                VARCHAR2(200),
    destroy_return          VARCHAR2(200),
    coc_number              VARCHAR2(200),
    remarks                 VARCHAR2(3800),
    laboratory              VARCHAR2(200),
    ucr_number          VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;


create sequence dat_kebs_cd_sample_submission_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_sample_submission_seq.nextval
from dual;

create trigger dat_kebs_cd_sample_submission_seq_trg
    before
        insert
    on dat_kebs_cd_sample_submission
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_sample_submission_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_sample_submission_idx on dat_kebs_cd_sample_submission (scf_no,coc_number, ucr_number, status) TABLESPACE qaimssdb_idx;
/






create table dat_kebs_cd_sample_submission_items
(
    id                    NUMBER  PRIMARY KEY,
    item_hscode            VARCHAR2(200),
    item_id            VARCHAR2(200),
    name_product          VARCHAR2(200),
    packaging              VARCHAR2(200),
    lb_id_trade_mark       VARCHAR2(200),
    lb_id_date_of_manf       VARCHAR2(200),
    lb_id_expiry_date       VARCHAR2(200),
    lb_id_cont_decl         VARCHAR2(200),
    lb_id_batch_no         VARCHAR2(200),
    lb_id_any_aomarking       VARCHAR2(200),
    size_test_sample       VARCHAR2(200),
    size_ref_sample       VARCHAR2(200),
    customer_name_org           VARCHAR2(200),
    customer_name_address           VARCHAR2(200),
    customer_name_telephone          VARCHAR2(200),
    customer_name_email           VARCHAR2(200),
    customer_name_date           VARCHAR2(200),
    file_reference_no       VARCHAR2(200),
    scf_no                  VARCHAR2(200),
    sample_reference_no       VARCHAR2(200),
    condition_sample         VARCHAR2(200),
    test_request            VARCHAR2(200),
    test_charges            VARCHAR2(200),
    receipt_no              VARCHAR2(200),
    invoice_no              VARCHAR2(200),
    receivers_name          VARCHAR2(200),
    note_trans_results      VARCHAR2(200),
    disposal                VARCHAR2(200),
    destroy_return          VARCHAR2(200),
    coc_number              VARCHAR2(200),
    remarks                 VARCHAR2(3800),
    laboratory              VARCHAR2(200),
    ucr_number          VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;


create sequence dat_kebs_cd_sample_submission_items_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_sample_submission_items_seq.nextval
from dual;

create trigger dat_kebs_cd_sample_submission_items_seq_trg
    before
        insert
    on dat_kebs_cd_sample_submission_items
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_sample_submission_items_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_sample_submission_items_idx on dat_kebs_cd_sample_submission_items (scf_no,coc_number, ucr_number, status, item_hscode, item_id) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_local_coc
(
    id                    NUMBER  PRIMARY KEY,
    coc_no                  VARCHAR2(200),
    issue_date              DATE,
    entry_no                VARCHAR2(200),
    idf_no                  VARCHAR2(200),
    importer_name           VARCHAR2(200),
    importer_address        VARCHAR2(200),
    importer_pin            varchar2(200),
    clear_agent             varchar2(200),
    port_entry              VARCHAR2(200),
    remarks             varchar2(3800),
    ucr_number          VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_local_coc_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_local_coc_seq_trg
    before
        insert
    on dat_kebs_cd_local_coc
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_local_coc_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_local_coc_idx on dat_kebs_cd_local_coc ( ucr_number, status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_local_coc_items
(
    id                    NUMBER  PRIMARY KEY,
    hscode                  VARCHAR2(200),
    product_description     VARCHAR2(200),
    quantities_declared     VARCHAR2(200),
    local_coc_id            NUMBER(*)
    ucr_number          VARCHAR2(350 CHAR),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_local_coc_items_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger dat_kebs_cd_local_coc_items_seq_trg
    before
        insert
    on dat_kebs_cd_local_coc_items
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_local_coc_items_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_local_coc_items_idx on dat_kebs_cd_local_coc_items (status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_laboratory
(
    id                    NUMBER  PRIMARY KEY,
    lab_name            varchar2(200),
    description         varchar2(350),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_laboratory_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_laboratory_seq.nextval
from dual;

create trigger dat_kebs_cd_laboratory_seq_trg
    before
        insert
    on dat_kebs_cd_laboratory
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_laboratory_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_laboratory_idx on dat_kebs_cd_laboratory ( status) TABLESPACE qaimssdb_idx;
/

create table dat_kebs_cd_checklist
(
    id                    NUMBER  PRIMARY KEY,
    desg_Const_storage_vessels            varchar2(3800),
    desg_Const_branch_manholes          varchar2(3800),
    sizing_select_fiting_lpgas          varchar2(3800),
    electronic_bonding          varchar2(3800),
    cons_fill_withrd_equaliz         varchar2(3800),
    sizing_select_presu_valves         varchar2(3800),
    sele_suitable_storage          varchar2(3800),
    selct_temp_instrume_storage          varchar2(3800),
    sitable_pressuer_storage            varchar2(3800),
    design_cons_mount_supports          varchar2(3800),
    ucrNumber                       varchar2(200)
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_checklist_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_checklist_seq.nextval
from dual;

create trigger dat_kebs_cd_checklist_seq_trg
    before
        insert
    on dat_kebs_cd_checklist
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_checklist_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_checklist_idx on dat_kebs_cd_checklist ( status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_clearing_agent_details
(
    id                 NUMBER not null primary key,
    name              VARCHAR2(200),
    pin                VARCHAR2(200),
    postal_address     VARCHAR2(200),
    physical_address   VARCHAR2(200),
    telephone          VARCHAR2(200),
    application_code   VARCHAR2(200),
    postal_country     VARCHAR2(200),
    physical_country   VARCHAR2(200),
    email              VARCHAR2(200),
    oga_ref_no         VARCHAR2(200),
    fax                VARCHAR2(200),
    sector_of_activity  VARCHAR2(200),
    warehouse_location   VARCHAR2(200),
    date_submitted      DATE,
    var_field_1        VARCHAR2(350 char),
    var_field_2        VARCHAR2(350 char),
    var_field_3        VARCHAR2(350 char),
    var_field_4        VARCHAR2(350 char),
    var_field_5        VARCHAR2(350 char),
    var_field_6        VARCHAR2(350 char),
    var_field_7        VARCHAR2(350 char),
    var_field_8        VARCHAR2(350 char),
    var_field_9        VARCHAR2(350 char),
    var_field_10       VARCHAR2(350 char),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE,
    version            NUMBER,
    warehouse_code     VARCHAR2(200)
) TABLESPACE qaimssdb_data;

create sequence dat_kebs_cd_clearing_agent_details_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_clearing_agent_details_seq.nextval
from dual;

create trigger dat_kebs_cd_clearing_agent_details_seq_trg
    before
        insert
    on dat_kebs_cd_clearing_agent_details
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_clearing_agent_details_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_clearing_agent_details_idx on dat_kebs_cd_clearing_agent_details ( id, pin) TABLESPACE qaimssdb_idx;
/
create table cfg_cd_checklist_types
(
    id                    NUMBER  PRIMARY KEY,
    type_Name           VARCHAR2(20) NOT NULL UNIQUE,
    description         VARCHAR2(350) NOT NULL,
    status              VARCHAR2(20),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_cd_checklist_types_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_cd_checklist_types_seq_trg
    before
        insert
    on cfg_cd_checklist_types
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_cd_checklist_types_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_cd_checklist_types_idx on cfg_cd_checklist_types ( id, type_name, status) TABLESPACE qaimssdb_idx;
/

create table cfg_kebs_cd_checklist_category
(
    id                    NUMBER  PRIMARY KEY,
    type_name            varchar2(200),
    description         varchar2(350),
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_kebs_cd_checklist_category_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select cfg_kebs_cd_checklist_category_seq.nextval
from dual;

create trigger cfg_kebs_cd_checklist_category_seq_trg
    before
        insert
    on cfg_kebs_cd_checklist_category
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_cd_checklist_category_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_cd_checklist_category_idx on cfg_kebs_cd_checklist_category ( type_name, status) TABLESPACE qaimssdb_idx;
/


create table dat_kebs_cd_inspection_checklist
(
    id                      NUMBER  PRIMARY KEY,
    inspection              VARCHAR2(200),
    category                VARCHAR2(200),
    entry_point             VARCHAR2(200),
    CFS                     VARCHAR(200),
    inspection_date                     Date,
    importers_name                  VARCHAR(200),
    clearing_agent                  VARCHAR(200),
    customs_entry_number            VARCHAR(200),
    IDF_number                      VARCHAR(200),
    ucr_number                      VARCHAR(200),
    coc_number                      VARCHAR2(200),
    fee_paid                        VARCHAR2(200),
    receipt_number                  VARCHAR2(200),
    items_name                      VARCHAR2(200),
    serial_number                   VARCHAR2(200),
    product_description             VARCHAR2(200),
    brand                           VARCHAR2(200),
    ks_eas_applicable               VARCHAR2(200),
    quantity_declared               VARCHAR2(200),
    quantity_verified               VARCHAR2(200),
    date_mfg_packaging              Date,
    date_expiry                     Date,
    mfg_name                        VARCHAR2(200),
    mfg_address                     VARCHAR2(200),
    composition_ingredients         VARCHAR2(3800),
    storage_condition               VARCHAR2(200),
    appearance                      VARCHAR2(200),
    cert_marks_pvoc_doc             VARCHAR2(200),
    sampled                         VARCHAR2(200),
    batch_no_model_type_ref         VARCHAR2(200),
    fiber_composition               VARCHAR2(200),
    instructions_use_manual         VARCHAR2(200),
    warranty_period_documentaion  VARCHAR2(200),
    safety_cautionary_remarks      VARCHAR2(200),
    disposal_instruction            VARCHAR2(200),
    size_class_capacity             VARCHAR2(200),
    make_vehicle                    VARCHAR2(200),
    chasis_no                       VARCHAR2(200),
    engine_no_capacity              VARCHAR2(200),
    manufacture_date            Date,
    registartion_date           Date,
    odemetre_reading            VARCHAR2(200),
    drive_rhd_lhd               VARCHAR2(200),
    transmision_auto_manual     VARCHAR2(200),
    colour                      VARCHAR2(200),
    overal_appearance           VARCHAR2(200),
    remarks                     VARCHAR2(200),
    packaging_labellig          VARCHAR2(200),
    physical_condition          VARCHAR2(200),
    defects                     VARCHAR2(200),
    presence_absence_banned     VARCHAR2(200),
    documentation               VARCHAR2(200),
    overal_remarks              VARCHAR2(200),
    kebs_officer                VARCHAR2(200),
    officer_date                Date,
    c_s_name                    VARCHAR2(200),
    c_s_date                    Date,
    o_i_c_name                  VARCHAR2(200),
    o_i_c_date                  Date,
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;


create sequence dat_kebs_cd_inspection_checklist_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_kebs_cd_inspection_checklist_seq.nextval
from dual;

create trigger dat_kebs_cd_inspection_checklist_seq_trg
    before
        insert
    on dat_kebs_cd_inspection_checklist
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_kebs_cd_inspection_checklist_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index dat_kebs_cd_sample_submission_items_idx on dat_kebs_cd_sample_submission_items (scf_no,coc_number, ucr_number, status, item_hscode, item_id) TABLESPACE qaimssdb_idx;
/


create table cfg_kebs_arrival_point
(
    id                    NUMBER  PRIMARY KEY,
    acronym_name         VARCHAR2(500) NOT NULL UNIQUE,
    description         VARCHAR2(350)NOT NULL UNIQUE,
    status              NUMBER(2),
    var_field_1         VARCHAR2(350 CHAR),
    var_field_2         VARCHAR2(350 CHAR),
    var_field_3         VARCHAR2(350 CHAR),
    var_field_4         VARCHAR2(350 CHAR),
    var_field_5         VARCHAR2(350 CHAR),
    var_field_6         VARCHAR2(350 CHAR),
    var_field_7         VARCHAR2(350 CHAR),
    var_field_8         VARCHAR2(350 CHAR),
    var_field_9         VARCHAR2(350 CHAR),
    var_field_10        VARCHAR2(350 CHAR),
    created_by          VARCHAR2(100 CHAR) DEFAULT 'admin'  NOT NULL ENABLE,
    created_on          TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate  NOT NULL ENABLE,
    modified_by         VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on         TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by           VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on          TIMESTAMP(6) WITH TIME ZONE
) TABLESPACE qaimssdb_data;

create sequence cfg_kebs_arrival_point_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_kebs_arrival_point_seq_trg
    before
        insert
    on cfg_kebs_arrival_point
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_kebs_arrival_point_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

create index cfg_kebs_arrival_point_idx on cfg_kebs_arrival_point ( acronym_name, description, status) TABLESPACE qaimssdb_idx;
/