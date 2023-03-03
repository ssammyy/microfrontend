import {Component, OnInit} from '@angular/core';
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import {
    AllInspectionDetailsApplyDto,
    HaccpImplementationDetailsApplyDto,
    InspectionDetailsDto,
    InspectionDetailsDtoB,
    OperationProcessAndControlsDetailsApplyDto,
    PermitEntityDto,
    ProductLabellingDto,
    StandardizationMarkSchemeDto,
    TechnicalDetailsDto
} from "../../../core/store/data/qa/qa.model";
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import * as CryptoJS from 'crypto-js';
import swal from "sweetalert2";
import {selectUserInfo} from "../../../core/store";

declare const $: any;

@Component({
    selector: 'app-new-inspection-report',
    templateUrl: './view-inspection-report.component.html',
    styleUrls: ['./view-inspection-report.component.css']
})
export class ViewInspectionReportComponent implements OnInit {
    currDivLabel!: string;
    currDiv!: string;
    inspectionReportId: any;
    roles: string[];

    loading = false;
    loadingText: string;
    technicalDetails: TechnicalDetailsDto;
    inspectionDetailsDto: InspectionDetailsDto
    inspectionDetailsDtoB: InspectionDetailsDtoB
    productLabellingDtos: ProductLabellingDto[] = [];
    standardizationMarkSchemeDto: StandardizationMarkSchemeDto
    operationProcessAndControlsDetailsDtos: OperationProcessAndControlsDetailsApplyDto[] = [];
    haccpImplementationDetailsApplyDto: HaccpImplementationDetailsApplyDto

    permitEntityDto: PermitEntityDto

    allInspectionReportDetails: AllInspectionDetailsApplyDto;

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
            this.inspectionReportId = decrypted.toString(CryptoJS.enc.Utf8);
        });
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.roles = u.roles;
        });
        this.checkIfInspectionReportExists(this.inspectionReportId)


    }

    id: any = '';

    accordion(ids: any) {
        if (this.id == ids) {
            this.id = '';
        } else {
            this.id = ids;
        }
    }

    openModalAddDetails(divVal: string): void {

        const arrHead = ['addTechnical', 'permitCompleteness', 'assignOfficer', 'addStandardsDetails', 'scheduleInspectionDate'];

        const arrHeadSave = ['Add Technical: Quality Procedures,Standards And Management Systems', 'Is The Permit Complete', 'Select An officer', 'Add Standard details', 'Set The Date of Inspection'];

        for (let h = 0; h < arrHead.length; h++) {
            if (divVal === arrHead[h]) {
                this.currDivLabel = arrHeadSave[h];
            }
        }

        this.currDiv = divVal;
    }


    private checkIfInspectionReportExists(inspectionReportId: string) {
        this.loading = true
        this.loadingText = "Retrieving Inspection Report"
        this.SpinnerService.show();

        this.internalService.getInspectionReport(inspectionReportId).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    this.allInspectionReportDetails = data?.data as AllInspectionDetailsApplyDto;
                    this.technicalDetails = this.allInspectionReportDetails.technicalDetailsDto;

                    this.inspectionDetailsDto = this.allInspectionReportDetails.inspectionDetailsDto
                    this.inspectionDetailsDtoB = this.allInspectionReportDetails.inspectionDetailsDtoB
                    this.productLabellingDtos = this.allInspectionReportDetails.productLabelling
                    this.standardizationMarkSchemeDto = this.allInspectionReportDetails.standardizationMarkScheme
                    this.operationProcessAndControlsDetailsDtos = this.allInspectionReportDetails.operationProcessAndControls
                    this.haccpImplementationDetailsApplyDto = this.allInspectionReportDetails.haccpImplementationDetails
                    this.permitEntityDto = this.allInspectionReportDetails.permitDetails

                    this.SpinnerService.hide()
                    this.loading = false


                }
            },
        );


    }


    gotoPermitDetails(permitId: string) {
        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        this.router.navigate(['/permit-details-admin', encrypted])


    }

    gotoInspectionReport(permitId: string) {
        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        this.router.navigate(['/new-inspection-report', encrypted])

    }

    submitInspectionReport(inspectionReportId: bigint) {

        this.loading = true
        this.loadingText = "Submitting Inspection Report"
        this.SpinnerService.show();
        this.internalService.inspectionReportFinalSave(String(this.allInspectionReportDetails.permitDetails.id), String(this.allInspectionReportDetails.id), this.allInspectionReportDetails).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {

                    this.SpinnerService.hide();
                    this.loading = false
                    //(data);
                    this.gotoPermitDetails(this.allInspectionReportDetails.permitDetails.id.toString())

                    swal.fire({
                        title: 'Inspection Report Submitted!',
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
