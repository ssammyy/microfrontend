import {Component, OnInit} from '@angular/core';
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import * as CryptoJS from 'crypto-js';
import swal from "sweetalert2";
import {
    AllInspectionDetailsApplyDto,
    HaccpImplementationDetailsApplyDto,
    InspectionDetailsDto,
    InspectionDetailsDtoB,
    InspectionReportProcessStepDto,
    OperationProcessAndControlsDetailsApplyDto,
    ProductLabellingDto,
    TechnicalDetailsDto
} from "../../../core/store/data/qa/qa.model";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";

declare const $: any;

@Component({
    selector: 'app-inspection-report-inspection-tesing',
    templateUrl: './inspection-report.html',
    styleUrls: ['./inspection-report.css']
})
export class InspectionReport implements OnInit {
    stepSoFar: | undefined;
    step = 1;
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);
    private permit_id: string;
    permitId: any;

    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    FMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
    draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;

    status: number;
    loading = false;
    loadingText: string;

    setCloned = false;

    hideCloneButton = false;

    technicalForm: FormGroup;
    inspectionDetails: FormGroup;
    inspectionDetailsB: FormGroup;
    productLabelling: FormGroup;
    operationProcessAndControlsDetailsApply: FormGroup;
    haccpImplementationDetailsApply: FormGroup;

    technicalDetails: TechnicalDetailsDto;
    inspectionDetailsDto: InspectionDetailsDto

    inspectionDetailsDtoB: InspectionDetailsDtoB
    productLabellingDto: ProductLabellingDto
    operationProcessAndControlsDetailsApplyDto: OperationProcessAndControlsDetailsApplyDto
    haccpImplementationDetailsApplyDto: HaccpImplementationDetailsApplyDto

    inspectionReportProcessStep: InspectionReportProcessStepDto | undefined;

    currBtn = 'A';
    allInspectionReportDetails: AllInspectionDetailsApplyDto;


    constructor(private formBuilder: FormBuilder,
                public router: Router,
                private route: ActivatedRoute,
                private store$: Store<any>,
                private qaService: QaService,
                private SpinnerService: NgxSpinnerService,
                private internalService: QaInternalService,
    ) {
    }

    ngOnInit(): void {

        this.route.paramMap.subscribe(paramMap => {
            let key = '11A1764225B11AA1';
            const encrypted = paramMap.get('id');
            key = CryptoJS.enc.Utf8.parse(key);
            const decrypted = CryptoJS.AES.decrypt({ciphertext: CryptoJS.enc.Hex.parse(encrypted)}, key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.ZeroPadding,
            });
            this.permitId = decrypted.toString(CryptoJS.enc.Utf8);

        });

        this.checkIfInspectionReportExists(this.permitId);

        this.technicalForm = this.formBuilder.group({
            id: [''],
            firmImplementedAnyManagementSystem: ['', Validators.required],
            firmImplementedAnyManagementSystemRemarks: ['', Validators.required],
            indicateRelevantProductStandardCodes: ['', Validators.required],
            indicateRelevantProductStandardCodesRemarks: ['', Validators.required],

        });
        this.inspectionDetails = this.formBuilder.group({
            complianceApplicableStatutory: ['', Validators.required],
            complianceApplicableStatutoryRemarks: ['', Validators.required],
            plantHouseKeeping: ['', Validators.required],
            plantHouseKeepingRemarks: ['', Validators.required],
            handlingComplaints: ['', Validators.required],
            handlingComplaintsRemarks: ['', Validators.required],
            qualityControlPersonnel: ['', Validators.required],
            qualityControlPersonnelRemarks: ['', Validators.required],
            testingFacility: ['', Validators.required],
            testingFacilityRemarks: ['', Validators.required],
        });

        this.inspectionDetailsB = this.formBuilder.group({
            equipmentCalibration: ['', Validators.required],
            equipmentCalibrationRemarks: ['', Validators.required],
            qualityRecords: ['', Validators.required],
            qualityRecordsRemarks: ['', Validators.required],
            recordsNonconforming: ['', Validators.required],
            recordsNonconformingRemarks: ['', Validators.required],
            productRecallRecords: ['', Validators.required],
            productRecallRecordsRemarks: ['', Validators.required],
            productLabelling: ['', Validators.required],

        });

        this.productLabelling = this.formBuilder.group({
            standardMarking: ['', Validators.required],
            findings: ['', Validators.required],
        });

        this.operationProcessAndControlsDetailsApply = this.formBuilder.group({
            processFlow: ['', Validators.required],
            operations: ['', Validators.required],
            qualityChecks: ['', Validators.required],
            frequency: ['', Validators.required],
            records: ['', Validators.required],
            findings: ['', Validators.required],
        });

        this.haccpImplementationDetailsApply = this.formBuilder.group({
            designFacilitiesConstructionLayout: ['', Validators.required],
            designFacilitiesConstructionLayoutRemarks: ['', Validators.required],
            maintenanceSanitationCleaningPrograms: ['', Validators.required],
            maintenanceSanitationCleaningProgramsRemarks: ['', Validators.required],
            personnelHygiene: ['', Validators.required],
            personnelHygieneRemarks: ['', Validators.required],
            transportationConveyance: ['', Validators.required],
            transportationConveyanceRemarks: ['', Validators.required],
            determinationCriticalParameters: ['', Validators.required],
            determinationCriticalParametersRemarks: ['', Validators.required],
            evidenceCorrectiveActions: ['', Validators.required],
            evidenceCorrectiveActionsRemarks: ['', Validators.required],


        });


    }

    onClickPrevious() {
        if (this.step > 1) {
            this.step = this.step - 1;
        } else {
            this.step = 1;
        }
    }

    clickNextToTest() {
        this.step += 1;

    }

    onClickUpdateStep(stepNumber: number) {
        if (this.technicalDetails) {
            this.clickNextToTest();
        }
    }

    onClickNext(valid: boolean) {
        if (valid) {
            switch (this.step) {
                case 1:
                    // this.stepSoFar = {...this.sta1Form?.value};
                    break;
                case 2:
                    //   this.stepSoFar = {...this.sta10Form?.value};
                    break;
                case 3:
                    //   this.stepSoFar = {...this.sta10FormA?.value};
                    break;
                case 4:
                    //    this.stepSoFar = {...this.sta10FormB?.value};
                    break;
                case 5:
                    //  this.stepSoFar = {...this.sta10FormC?.value};
                    break;
                case 6:
                    // this.stepSoFar = {...this.sta10FormD?.value};
                    break;
                case 7:
                    //     this.stepSoFar = {...this.sta10FormE?.value};
                    break;
                case 8:
                    //   this.stepSoFar = {...this.sta10FormF?.value};
                    break;
                case 9:
                    //     this.stepSoFar = {...this.sta1Form?.value};
                    break;
            }
            this.step += 1;
            //(`Clicked and step = ${this.step}`);
        }
    }

    get formTechnicalForm(): any {
        return this.technicalForm.controls;
    }

    get formInspectionDetailsForm(): any {
        return this.inspectionDetails.controls;
    }

    get formInspectionDetailsBForm(): any {
        return this.inspectionDetailsB.controls;
    }

    get formProductLabellingForm(): any {
        return this.productLabelling.controls;
    }

    get formOperationProcessAndControlsDetailsApplyForm(): any {
        return this.operationProcessAndControlsDetailsApply.controls;
    }

    get formHaccpImplementationDetailsApply(): any {
        return this.haccpImplementationDetailsApply.controls;
    }


    onClickSaveInspectionReportTechnicalDetails(valid: boolean) {
        if (valid) {
            if (this.technicalDetails == null || this.setCloned == true) {
                this.SpinnerService.show();
                this.internalService.saveInspectionReportTechnicalDetails(this.permitId, this.technicalForm.value).subscribe(
                    (data: ApiResponseModel) => {
                        if (data.responseCode === '00') {
                            this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                            this.technicalDetails = this.allInspectionReportDetails.technicalDetailsDto;
                            this.SpinnerService.hide();
                            //(data);
                            this.step += 1;
                            this.currBtn = 'B';
                            swal.fire({
                                title: 'Technical Report Saved!',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                                },
                                icon: 'success'
                            });
                        } else {
                            this.SpinnerService.hide();
                            this.qaService.showError(data.message);
                        }
                    },
                );
            } else {


                this.SpinnerService.show();
                this.technicalForm.controls['id'].setValue(this.technicalDetails.id);
                this.internalService.saveInspectionReportTechnicalDetails(String(this.permitId), this.technicalForm.value).subscribe(
                    (data) => {
                        this.technicalDetails = data;
                        this.step += 1;
                        this.SpinnerService.hide();
                        //(data);
                        swal.fire({
                            title: 'Technical Report Updated!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            }
        }
    }


    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'Notification'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }


    private checkIfInspectionReportExists(permitId: string) {
        if(permitId)
        {

        }

    }
}
