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
    FilesListDto,
    HaccpImplementationDetailsApplyDto,
    InspectionDetailsDto,
    InspectionDetailsDtoB,
    InspectionReportProcessStepDto,
    InspectionReportToBeClonedDto,
    OperationProcessAndControlsDetailsApplyDto,
    ProductLabellingDto,
    StandardizationMarkSchemeDto,
    TechnicalDetailsDto
} from "../../../core/store/data/qa/qa.model";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import {NotificationService} from "../../../core/store/data/std/notification.service";

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

    standardizationMarkSchemeFormGroup: FormGroup;

    productLabelling: FormGroup;
    operationProcessAndControlsDetailsApply: FormGroup;
    haccpImplementationDetailsApplyFormGroup: FormGroup;

    recommendationsFormGroup: FormGroup;


    technicalDetails: TechnicalDetailsDto;
    inspectionDetailsDto: InspectionDetailsDto

    inspectionDetailsDtoB: InspectionDetailsDtoB
    productLabellingDto: ProductLabellingDto

    productLabellingDtos: ProductLabellingDto[] = [];

    standardizationMarkSchemeDto: StandardizationMarkSchemeDto

    operationProcessAndControlsDetailsApplyDto: OperationProcessAndControlsDetailsApplyDto

    operationProcessAndControlsDetailsDtos: OperationProcessAndControlsDetailsApplyDto[] = [];


    haccpImplementationDetailsApplyDto: HaccpImplementationDetailsApplyDto

    inspectionReportProcessStep: InspectionReportProcessStepDto | undefined;

    currBtn = 'A';
    allInspectionReportDetails: AllInspectionDetailsApplyDto;
    allInspectionReportDetailsToBeCloned: InspectionReportToBeClonedDto[] = [];
    selectedPermitIdInspectionReport: number;
    cloned: boolean;

    sta10FilesList: FilesListDto[] = [];
    public uploadedFiles: FileList;


    constructor(private formBuilder: FormBuilder,
                public router: Router,
                private route: ActivatedRoute,
                private store$: Store<any>,
                private qaService: QaService,
                private notifyService: NotificationService,
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
        this.getInspectionReportsFullyFilled()

        this.technicalForm = this.formBuilder.group({
            id: [''],
            firmImplementedAnyManagementSystem: ['', Validators.required],
            firmImplementedAnyManagementSystemRemarks: [''],
            indicateRelevantProductStandardCodes: ['', Validators.required],
            indicateRelevantProductStandardCodesRemarks: [''],

        });
        this.inspectionDetails = this.formBuilder.group({
            id: [''],
            inspectionRecommendationId: [''],
            complianceApplicableStatutory: ['', Validators.required],
            complianceApplicableStatutoryRemarks: [''],
            plantHouseKeeping: ['', Validators.required],
            plantHouseKeepingRemarks: [''],
            handlingComplaints: ['', Validators.required],
            handlingComplaintsRemarks: [''],
            qualityControlPersonnel: ['', Validators.required],
            qualityControlPersonnelRemarks: [''],
            testingFacility: ['', Validators.required],
            testingFacilityRemarks: [''],
        });

        this.inspectionDetailsB = this.formBuilder.group({
            id: [''],
            inspectionRecommendationId: [''],
            equipmentCalibration: ['', Validators.required],
            equipmentCalibrationRemarks: [''],
            qualityRecords: ['', Validators.required],
            qualityRecordsRemarks: [''],
            recordsNonconforming: ['', Validators.required],
            recordsNonconformingRemarks: [''],
            productRecallRecords: ['', Validators.required],
            productRecallRecordsRemarks: [''],

        });

        this.productLabelling = this.formBuilder.group({
            standardMarking: [''],
            findings: [''],
            statusOfCompliance: ['']
        });

        this.standardizationMarkSchemeFormGroup = this.formBuilder.group({
            id: [''],
            inspectionRecommendationId: [''],
            validitySmarkPermit: ['', Validators.required],
            validitySmarkPermitRemarks: [''],
            useTheSmark: ['', Validators.required],
            useTheSmarkRemarks: [''],
            changesAffectingProductCertification: ['', Validators.required],
            changesAffectingProductCertificationRemarks: [''],
            changesBeenCommunicatedKebs: ['', Validators.required],
            changesBeenCommunicatedKebsRemarks: [''],
            samplesDrawn: ['', Validators.required],
            samplesDrawnRemarks: [''],

        });

        this.operationProcessAndControlsDetailsApply = this.formBuilder.group({
            processFlow: [''],
            qualityChecks: [''],
            frequency: [''],
            records: [''],
            findings: [''],
        });

        this.haccpImplementationDetailsApplyFormGroup = this.formBuilder.group({
            id: [''],
            inspectionRecommendationId: [''],
            designFacilitiesConstructionLayout: ['', Validators.required],
            designFacilitiesConstructionLayoutRemarks: [''],
            maintenanceSanitationCleaningPrograms: ['', Validators.required],
            maintenanceSanitationCleaningProgramsRemarks: [''],
            personnelHygiene: ['', Validators.required],
            personnelHygieneRemarks: [''],
            transportationConveyance: ['', Validators.required],
            transportationConveyanceRemarks: [''],
            determinationCriticalParameters: ['', Validators.required],
            determinationCriticalParametersRemarks: [''],
            evidenceCorrectiveActions: ['', Validators.required],
            evidenceCorrectiveActionsRemarks: [''],


        });

        this.recommendationsFormGroup = this.formBuilder.group((
            {
                id: [''],
                followPreviousRecommendationsNonConformities: ['', Validators.required],
                recommendations: ['', Validators.required],
                inspectorComments: ['', Validators.required],

            }
        ))

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
        return this.haccpImplementationDetailsApplyFormGroup.controls;
    }


    onClickSaveInspectionReportTechnicalDetails(valid: boolean) {
        if (valid) {
            if (this.technicalDetails == null) {
                this.loading = true
                this.loadingText = "Saving Technical Report"
                this.SpinnerService.show();
                this.internalService.saveInspectionReportTechnicalDetails(this.permitId, this.technicalForm.value).subscribe(
                    (data: ApiResponseModel) => {
                        if (data.responseCode === '00') {
                            this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                            this.technicalDetails = this.allInspectionReportDetails.technicalDetailsDto;
                            this.SpinnerService.hide();
                            this.loading = false
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
                            this.loading = false
                            this.qaService.showError(data.message);
                        }
                    },
                );
            } else {

                this.loading = true
                this.loadingText = "Updating Technical Report"
                this.SpinnerService.show();
                this.technicalForm.controls['id'].setValue(this.technicalDetails.id);
                this.internalService.saveInspectionReportTechnicalDetails(String(this.permitId), this.technicalForm.value).subscribe(
                    (data) => {
                        this.technicalDetails = data;
                        this.step += 1;
                        this.loading = false

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

    onClickUpdateInspectionReportTechnicalDetails(valid: boolean) {
        if (valid) {
            this.loading = true
            this.loadingText = "Updating Technical Report"
            this.SpinnerService.show();
            this.inspectionDetails.controls['id'].setValue(this.allInspectionReportDetails.technicalDetailsDto.id)
            this.inspectionDetails.controls['inspectionRecommendationId'].setValue(this.allInspectionReportDetails.id)
            this.internalService.updateInspectionReportTechnicalDetails(this.permitId, this.inspectionDetails.value).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        this.inspectionDetailsDto = this.allInspectionReportDetails.inspectionDetailsDto;
                        this.SpinnerService.hide();
                        this.loading = false
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
                        swal.fire({
                            title: 'Technical Report Updated!',
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
        }

    }

    onClickUpdateInspectionReportTechnicalDetailsB(valid: boolean) {
        if (valid) {
            this.loading = true
            this.loadingText = "Updating Technical Report"
            this.SpinnerService.show();
            this.inspectionDetailsB.controls['id'].setValue(this.allInspectionReportDetails.technicalDetailsDto.id)
            this.inspectionDetailsB.controls['inspectionRecommendationId'].setValue(this.allInspectionReportDetails.id)
            this.internalService.updateInspectionReportTechnicalDetailsB(this.permitId, this.inspectionDetailsB.value).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        this.inspectionDetailsDtoB = this.allInspectionReportDetails.inspectionDetailsDtoB;
                        this.SpinnerService.hide();
                        this.loading = false
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
                        swal.fire({
                            title: 'Technical Report Updated!',
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
        }

    }

    onClickSaveProductLabelling(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.productLabellingDtos.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                this.internalService.updateInspectionReportProductLabelling(this.permitId, this.allInspectionReportDetails.id.toString(), this.productLabellingDtos).subscribe(
                    (data) => {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;
                        this.productLabellingDtos = this.allInspectionReportDetails.productLabelling;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false
                        swal.fire({
                            title: 'Product Labelling Details Saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                swal.fire({
                    title: 'Product Labelling Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });
            }
        }
    }

    onClickAddProductLabelling() {
        if (this.productLabelling?.get('standardMarking')?.value == null) {
            this.showToasterError("Error", "Please Enter Standard Marking")
        } else if (this.productLabelling?.get('findings')?.value == null) {
            this.showToasterError("Error", "Please Enter Findings")
        } else if (this.productLabelling?.get('statusOfCompliance')?.value == null) {
            this.showToasterError("Error", "Please Enter Compliance Status")
        } else {


            this.productLabellingDto = this.productLabelling.value;
            this.productLabellingDtos.push(this.productLabellingDto);
            this.productLabelling?.get('standardMarking')?.reset();
            this.productLabelling?.get('findings')?.reset();
            this.productLabelling?.get('statusOfCompliance')?.reset();
            console.log(this.productLabellingDtos.length)
        }
        // this.sta10FormA.reset();
    }

    removeProductLabelling(index) {
        //(index);
        if (index === 0) {
            this.productLabellingDtos.splice(index, 1);
        } else {
            this.productLabellingDtos.splice(index, index);
        }
    }


    onClickUpdateStandardizationMarkScheme(valid: boolean) {
        if (valid) {
            this.loading = true
            this.loadingText = "Updating Standardization Mark Scheme"
            this.SpinnerService.show();
            this.standardizationMarkSchemeFormGroup.controls['inspectionRecommendationId'].setValue(this.allInspectionReportDetails.id)
            this.internalService.updateInspectionReportStandardization(this.permitId, this.standardizationMarkSchemeFormGroup.value).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        this.standardizationMarkSchemeDto = this.allInspectionReportDetails.standardizationMarkScheme;
                        this.SpinnerService.hide();
                        this.loading = false
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
                        swal.fire({
                            title: 'Standardization Mark Scheme Updated!',
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
        }

    }


    onClickSaveOperationProcess(valid: boolean) {
        if (valid) {
            this.loading = true

            if (this.operationProcessAndControlsDetailsDtos.length > 0) {
                this.SpinnerService.show();
                //(this.sta10Details.id.toString());
                this.internalService.updateInspectionReportOperation(this.permitId, this.allInspectionReportDetails.id.toString(), this.operationProcessAndControlsDetailsDtos).subscribe(
                    (data) => {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        this.operationProcessAndControlsDetailsDtos = this.allInspectionReportDetails.operationProcessAndControls;
                        this.onClickUpdateStep(this.step);
                        this.SpinnerService.hide();
                        this.loading = false
                        swal.fire({
                            title: 'Operations Process And Control Details Saved!',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    },
                );
            } else {
                this.loading = false

                swal.fire({
                    title: 'Operations Process And Control Details missing!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'error'
                });
            }
        }
    }

    onClickAddOperationProcess() {
        if (this.operationProcessAndControlsDetailsApply?.get('processFlow')?.value == null) {
            this.showToasterError("Error", "Please Enter Process Flow")
        } else if (this.operationProcessAndControlsDetailsApply?.get('qualityChecks')?.value == null) {
            this.showToasterError("Error", "Please Enter Quality Checks")
        } else if (this.operationProcessAndControlsDetailsApply?.get('frequency')?.value == null) {
            this.showToasterError("Error", "Please Enter Frequency")
        } else if (this.operationProcessAndControlsDetailsApply?.get('records')?.value == null) {
            this.showToasterError("Error", "Please Enter Records")
        } else if (this.operationProcessAndControlsDetailsApply?.get('findings')?.value == null) {
            this.showToasterError("Error", "Please Enter Findings")
        } else {


            this.operationProcessAndControlsDetailsApplyDto = this.operationProcessAndControlsDetailsApply.value;
            this.operationProcessAndControlsDetailsDtos.push(this.operationProcessAndControlsDetailsApplyDto);
            this.operationProcessAndControlsDetailsApply?.get('processFlow')?.reset();
            this.operationProcessAndControlsDetailsApply?.get('qualityChecks')?.reset();
            this.operationProcessAndControlsDetailsApply?.get('frequency')?.reset();
            this.operationProcessAndControlsDetailsApply?.get('records')?.reset();
            this.operationProcessAndControlsDetailsApply?.get('findings')?.reset();

            console.log(this.productLabellingDtos.length)
        }
        // this.sta10FormA.reset();
    }

    removeOperationProcess(index) {
        //(index);
        if (index === 0) {
            this.operationProcessAndControlsDetailsDtos.splice(index, 1);
        } else {
            this.operationProcessAndControlsDetailsDtos.splice(index, index);
        }
    }


    onClickHaccpImplementation(valid: boolean) {
        if (valid) {
            this.loading = true
            this.loadingText = "Updating Haccp Implementation"
            this.SpinnerService.show();
            this.haccpImplementationDetailsApplyFormGroup.controls['inspectionRecommendationId'].setValue(this.allInspectionReportDetails.id)
            this.internalService.updateInspectionReportHaccp(this.permitId, String(this.allInspectionReportDetails.id), this.haccpImplementationDetailsApplyFormGroup.value).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        this.standardizationMarkSchemeDto = this.allInspectionReportDetails.standardizationMarkScheme;
                        this.SpinnerService.hide();
                        this.loading = false
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
                        swal.fire({
                            title: 'Haccp Implementation Updated!',
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
        }

    }


    onClickRecommendationSave(valid: boolean) {
        if (valid) {
            this.loading = true
            this.loadingText = "Saving Recommendations"
            this.SpinnerService.show();
            this.recommendationsFormGroup.controls['id'].setValue(this.allInspectionReportDetails.id)
            this.internalService.recommendationsSave(this.permitId, String(this.allInspectionReportDetails.id), this.recommendationsFormGroup.value).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;
                        this.SpinnerService.hide();
                        this.loading = false
                        //(data);
                        this.step += 1;
                        this.currBtn = 'B';
                        swal.fire({
                            title: 'Recommendations Saved!',
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
        if (permitId) {
            this.loading = true
            this.loadingText = "Loading"
            this.SpinnerService.show();

            this.internalService.checkIfInspectionReportExists(this.permitId).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                        if (this.allInspectionReportDetails.technicalDetailsDto != null) {
                            this.technicalDetails = this.allInspectionReportDetails.technicalDetailsDto;
                            this.technicalForm.patchValue(this.technicalDetails);
                        }
                        if (this.allInspectionReportDetails.inspectionDetailsDto != null) {

                            this.inspectionDetailsDto = this.allInspectionReportDetails.inspectionDetailsDto
                            this.inspectionDetails.patchValue(this.inspectionDetailsDto);
                        }
                        if (this.allInspectionReportDetails.inspectionDetailsDtoB != null) {

                            this.inspectionDetailsDtoB = this.allInspectionReportDetails.inspectionDetailsDtoB
                            this.inspectionDetailsB.patchValue(this.inspectionDetailsDtoB);
                        }
                        this.productLabellingDtos = this.allInspectionReportDetails.productLabelling
                        if (this.allInspectionReportDetails.standardizationMarkScheme != null) {
                            this.standardizationMarkSchemeDto = this.allInspectionReportDetails.standardizationMarkScheme
                            this.standardizationMarkSchemeFormGroup.patchValue(this.standardizationMarkSchemeDto);
                        }
                        this.operationProcessAndControlsDetailsDtos = this.allInspectionReportDetails.operationProcessAndControls

                        if (this.allInspectionReportDetails.haccpImplementationDetails != null) {
                            this.haccpImplementationDetailsApplyDto = this.allInspectionReportDetails.haccpImplementationDetails
                            this.haccpImplementationDetailsApplyFormGroup.patchValue(this.haccpImplementationDetailsApplyDto);
                        }
                        this.recommendationsFormGroup.patchValue(this.allInspectionReportDetails);


                        this.SpinnerService.hide()
                        this.loading = false
                    }
                    this.SpinnerService.hide()
                    this.loading = false
                },
            );
        }
        this.SpinnerService.hide()
        this.loading = false

    }


    private getInspectionReportsFullyFilled() {
        this.internalService.getFullyFilledInspectionReport().subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    this.allInspectionReportDetailsToBeCloned = data?.data as InspectionReportToBeClonedDto[];
                }

            },
        );


    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    goToInspectionReportPage(inspectionReportId: string) {
        var text = inspectionReportId;
        var key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        var encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        console.log('encrypted', encrypted);
        this.router.navigate(['/inspection-report', encrypted])

    }

    setClonedMethod() {
        this.hideCloneButton = true;
    }

    cloneInspectionReport() {
        this.loading = true
        this.loadingText = "Cloning"
        this.SpinnerService.show();

        this.internalService.checkIfInspectionReportExists(this.selectedPermitIdInspectionReport.toString()).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;

                    this.technicalForm.patchValue(this.allInspectionReportDetails.technicalDetailsDto);
                    this.inspectionDetails.patchValue(this.allInspectionReportDetails.inspectionDetailsDto);
                    this.inspectionDetailsB.patchValue(this.allInspectionReportDetails.inspectionDetailsDtoB);
                    this.productLabellingDtos = this.allInspectionReportDetails.productLabelling
                    this.standardizationMarkSchemeFormGroup.patchValue(this.allInspectionReportDetails.standardizationMarkScheme);
                    this.operationProcessAndControlsDetailsDtos = this.allInspectionReportDetails.operationProcessAndControls
                    this.haccpImplementationDetailsApplyFormGroup.patchValue(this.allInspectionReportDetails.haccpImplementationDetails);
                    this.recommendationsFormGroup.patchValue(this.allInspectionReportDetails);

                    this.technicalForm.controls['id'].setValue('')

                    this.SpinnerService.hide()
                    this.loading = false
                    this.hideCloneButton = false

                }
                this.SpinnerService.hide()
                this.loading = false
                this.hideCloneButton = false

            },
        );
        this.SpinnerService.hide()
        this.loading = false
        this.hideCloneButton = false
    }

    onClickSaveInspectionReportDocs() {
        if (this.sta10FilesList.length > 0) {
            if (this?.uploadedFiles) {
                this.fileListSaveDetails();
            }
        } else if (this.uploadedFiles?.length > 0) {
            this.fileListSaveDetails();
        }

    }

    fileListSaveDetails() {
        if (this.uploadedFiles.length > 0) {
            this.SpinnerService.show();
            const file = this.uploadedFiles;
            const formData = new FormData();
            formData.append('permitID', String(this.permitId));
            formData.append('permitID', String(this.inspectionDetails));
            formData.append('inspectionReportId', this.allInspectionReportDetails.id.toString())
            formData.append('data', 'INSPECTION_REPORT'); //will be saved under varField1
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.qaService.qaSaveInspectionReport(formData).subscribe(
                (data: ApiResponseModel) => {
                    if (data.responseCode === '00') {
                        this.SpinnerService.hide();
                        this.qaService.showSuccess('Additional Documents Uploaded Successfully', () => {
                            // this.loadPermitDetails(data);
                            this.loading = false
                        });
                    } else {
                        this.SpinnerService.hide();
                        this.qaService.showError(data.message);
                    }
                },
                error => {
                    this.SpinnerService.hide();
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        } else {
            this.qaService.showError('NO FILE IS UPLOADED FOR SAVING');
        }
    }

}
