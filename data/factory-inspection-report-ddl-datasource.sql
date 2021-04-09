create TABLE dat_factory_inspection_report
(
    id           NUMBER PRIMARY KEY,
    status       NUMBER(2, 0),
    quality_procedure_findings  VARCHAR(4000),
    quality_procedure_remarks   VARCHAR2(4000),

    availability_of_product_standards_findings   VARCHAR(4000),
    availability_of_product_standards_remarks   VARCHAR(4000),

    quality_management_systems_findings   VARCHAR(4000),
    quality_management_systems_remarks   VARCHAR(4000),

    haccp_findings   VARCHAR(4000),
    haccp_remarks   VARCHAR(4000),

    testing_facility_findings   VARCHAR(4000),
    testing_facility_remarks   VARCHAR(4000),

    quality_control_personnel_qualifications_findings  VARCHAR(4000),
    quality_control_personnel_qualifications_remarks  VARCHAR(4000),

    equipment_calibration_findings  VARCHAR(4000),
    equipment_calibration_remarks  VARCHAR(4000),

    quality_records_findings  VARCHAR(4000),
    quality_records_remarks  VARCHAR(4000),

    product_labeling_and_identification_findings  VARCHAR(4000),
    product_labeling_and_identification_remarks  VARCHAR(4000),

    validity_of_smark_permit_findings    VARCHAR(4000),
    validity_of_smark_permit_remarks    VARCHAR(4000),

    use_of_the_smark_findings    VARCHAR(4000),
    use_of_the_smark_remarks    VARCHAR(4000),

    changes_affecting_product_certification_findings    VARCHAR(4000),
    changes_affecting_product_certification_remarks     VARCHAR(4000),

    communication_of_the_changes_to_kebs_findings        VARCHAR(4000),
    communication_of_the_changes_to_kebs_remarks        VARCHAR(4000),

    samples_drawn_findings        VARCHAR(4000),
    samples_drawn_remarks        VARCHAR(4000),

    design_and_facilitates_construction_and_layout_of_buildings_findings        VARCHAR(4000),
    design_and_facilitates_construction_and_layout_of_buildings_remarks        VARCHAR(4000),

    control_of_operations_findings        VARCHAR(4000),
    control_of_operations_remarks        VARCHAR(4000),

    maintenance_and_sanitation_cleaning_programs_findings        VARCHAR(4000),
    maintenance_and_sanitation_cleaning_programs_remarks        VARCHAR(4000),

    personal_hygene_findings        VARCHAR(4000),
    personal_hygene_remarks        VARCHAR(4000),

    transportation_conveyance_and_bulk_container_findings        VARCHAR(4000),
    transportation_conveyance_and_bulk_container_remarks        VARCHAR(4000),

    product_information_labeling_findings       VARCHAR(4000),
    product_information_labeling_remarks       VARCHAR(4000),

    training_of_management_and_employees_findings    VARCHAR(4000),
    training_of_management_and_employees_remarks    VARCHAR(4000),

    use_of_appropriate_sector_codes_of_hygene_practice_findings  VARCHAR(4000),
    use_of_appropriate_sector_codes_of_hygene_practice_remarks  VARCHAR(4000),

    establishment_of_haccp_plans_findings   VARCHAR(4000),
    establishment_of_haccp_plans_remarks   VARCHAR(4000),

    product_flow_diagram_findings   VARCHAR(4000),
    product_flow_diagram_remarks   VARCHAR(4000),

    evidence_of_hazard_analysis_done_at_each_step_of_production_findings VARCHAR(4000),
    evidence_of_hazard_analysis_done_at_each_step_of_production_remarks VARCHAR(4000),

    establishment_of_Critical_control_points_to_address_hazards_findings     VARCHAR(4000),
    establishment_of_Critical_control_points_to_address_hazards_remarks     VARCHAR(4000),

    establishment_of_monitoring_and_control_of_the_ccp_findings       VARCHAR(4000),
    establishment_of_monitoring_and_control_of_the_ccp_remarks       VARCHAR(4000),

    evidence_of_corrective_actions_for_each_ccp_findings         VARCHAR(4000),
    evidence_of_corrective_actions_for_each_ccp_remarks         VARCHAR(4000),

    evidence_for_verification_to_confirm_haccp_system_is_working_correctly_findings  VARCHAR(4000),
    evidence_for_verification_to_confirm_haccp_system_is_working_correctly_remarks  VARCHAR(4000),

    record_keeping_of_documents_appropriate_to_the_principle_findings        VARCHAR(4000),
    record_keeping_of_documents_appropriate_to_the_principle_remarks        VARCHAR(4000),

    recommendations         VARCHAR(4000),

    inspector_comments      VARCHAR(4000),

    supervisor_comments     VARCHAR(4000),

    permit_application_id   NUMBER,

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

create sequence dat_factory_inspection_report_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

select dat_factory_inspection_report_seq.nextval
from dual;

create trigger dat_factory_inspection_report_trg
    before
        insert
    on dat_factory_inspection_report
    for each row
begin
    if inserting then
        if :new.id is null then
            select dat_factory_inspection_report_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;

