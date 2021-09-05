import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ApproveRejectConsignmentComponent} from './approve-reject-consignment/approve-reject-consignment.component';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {AttachmentDialogComponent} from "./attachment-dialog/attachment-dialog.component";
import {GenerateLocalCocComponent} from "../forms/generate-local-coc/generate-local-coc.component";
import {GenerateLocalCorComponent} from "../forms/generate-local-cor/generate-local-cor.component";
import {ManualAssignOfficerComponent} from "../forms/manual-assign-officer/manual-assign-officer.component";
import {ReAssignOfficerComponent} from "../forms/re-assign-officer/re-assign-officer.component";
import {AssignPortComponent} from "../forms/assign-port/assign-port.component";
import {AssignOfficerComponent} from "../forms/assign-officer/assign-officer.component";
import {CompliantComponent} from "../forms/compliant/compliant.component";
import {SendCoiComponent} from "../forms/send-coi/send-coi.component";
import {TargetItemComponent} from "../forms/target-item/target-item.component";
import {TargetSupervisorComponent} from "../forms/target-supervisor/target-supervisor.component";
import {SendDemandNoteTokwsComponent} from "../forms/send-demand-note-tokws/send-demand-note-tokws.component";
import {BlacklistComponent} from "../forms/blacklist/blacklist.component";

@Component({
    selector: 'app-view-single-consignment-document',
    templateUrl: './view-single-consignment-document.component.html',
    styleUrls: ['./view-single-consignment-document.component.css']
})
export class ViewSingleConsignmentDocumentComponent implements OnInit {
    active = 'consignee';
    consignmentId: string;
    consignment: any;
    attachments: any[];
    comments: any[];
    consignmentItems: any[]
    paymentFees: any[]
    configurations: any[]

    constructor(private diService: DestinationInspectionService,
                private dialog: MatDialog,
                private activatedRoute: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            rs => {
                this.consignmentId = rs.get("id")
                this.loadConsignmentDetails()
                this.loadFees()
                this.loadUiConfigurations()

            }
        )

    }

    generateDemandNote() {
        this.dialog.open(SendDemandNoteTokwsComponent, {
            data: {
                uuid: this.consignmentId,
                items: this.consignmentItems,
                paymentFees: this.paymentFees
            }
        })
    }

    viewCoR() {

    }

    viewCoC() {

    }

    loadUiConfigurations() {
        this.diService.getInspectionUiConfigurations()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.configurations = res.data
                    } else {
                        this.configurations = null
                    }
                }
            )
    }

    loadFees() {
        this.diService.demandNoteFees()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.paymentFees = res.data
                    } else {
                        this.paymentFees = []
                    }
                }
            )
    }

    viewIdfDocument() {
        this.router.navigate(["/di/idf/details", this.consignment.cd_details.uuid])
    }

    loadComments() {
        this.diService.getAuditComments(this.consignment.cd_details.id)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.comments = res.data
                    } else {
                        this.comments = []
                    }
                }
            )
    }

    viewDeclarationDocument() {
        this.router.navigate(["/di/declaration/document", this.consignment.cd_details.uuid])
    }

    goBackHome() {
        this.router.navigateByUrl("/di")
    }

    uploadAttachment() {
        let ref = this.dialog.open(AttachmentDialogComponent, {
            data: {
                id: this.consignmentId
            }
        })
        ref.afterClosed()
            .subscribe(
                uploaded => {
                    if (uploaded) {
                        this.listConsignmentAttachments()
                    }
                }
            )
    }

    listConsignmentAttachments() {
        this.diService.getConsignmentAttachments(this.consignmentId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.attachments = res.data
                    } else {
                        this.attachments = []
                    }
                }
            )
    }

    isVehicle(): Boolean {
        if (this.consignment) {
            return this.consignment.cd_details.cdTypeCategory === "VEHICLES"
        }
        return false
    }

    downloadDemandNote() {
        // let d=341
        this.diService.downloadDocument("/api/v1/download/demand/note/"+this.consignment.cd_details.id)
    }

    loadConsignmentDetails() {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {
                    if (response.responseCode === "00") {
                        this.consignment = response.data
                        this.consignmentItems = this.consignment.items_cd
                        this.listConsignmentAttachments()
                        this.loadComments()
                    } else {
                        swal.fire({
                            title: response.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(this.goBackHome);
                        console.log(response)
                    }
                }
            )
    }

    generateLocalCoC() {
        let ref = this.dialog.open(GenerateLocalCocComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    generateLocalCoR() {
        let ref = this.dialog.open(GenerateLocalCorComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    manualAssignInspectionOfficer() {
        let ref = this.dialog.open(ManualAssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    assignInspectionOfficer() {
        let ref = this.dialog.open(AssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if(res){
                        this.loadConsignmentDetails()
                    }

                }
            )
    }

    blacklistUser(){
        let ref = this.dialog.open(BlacklistComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    supervisorTargetConsignment() {
        let ref = this.dialog.open(TargetSupervisorComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    targetConsignment() {
        let ref = this.dialog.open(TargetItemComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    sendCertificateOfCompliance() {
        let ref = this.dialog.open(SendCoiComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    markAsCompliant() {
        let ref = this.dialog.open(CompliantComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    assignConsignmentPort() {
        let ref = this.dialog.open(AssignPortComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    reassignInspectionOfficer() {
        let ref = this.dialog.open(ReAssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    approveRejectConsignment() {
        let ref = this.dialog.open(ApproveRejectConsignmentComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
                complianceStatus: this.consignment.cd_details.complianceStatus
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }
}
