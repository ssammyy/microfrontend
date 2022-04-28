import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ApproveRejectConsignmentComponent} from './approve-reject-consignment/approve-reject-consignment.component';
import {ActivatedRoute, Router} from '@angular/router';
import {DestinationInspectionService} from '../../../core/store/data/di/destination-inspection.service';
import {AttachmentDialogComponent} from './attachment-dialog/attachment-dialog.component';
import {GenerateLocalCocComponent} from '../forms/generate-local-coc/generate-local-coc.component';
import {GenerateLocalCorComponent} from '../forms/generate-local-cor/generate-local-cor.component';
import {ManualAssignOfficerComponent} from '../forms/manual-assign-officer/manual-assign-officer.component';
import {ReAssignOfficerComponent} from '../forms/re-assign-officer/re-assign-officer.component';
import {AssignPortComponent} from '../forms/assign-port/assign-port.component';
import {AssignOfficerComponent} from '../forms/assign-officer/assign-officer.component';
import {CompliantComponent} from '../forms/compliant/compliant.component';
import {SendCoiComponent} from '../forms/send-coi/send-coi.component';
import {TargetItemComponent} from '../forms/target-item/target-item.component';
import {TargetSupervisorComponent} from '../forms/target-supervisor/target-supervisor.component';
import {SendDemandNoteTokwsComponent} from '../forms/send-demand-note-tokws/send-demand-note-tokws.component';
import {BlacklistComponent} from '../forms/blacklist/blacklist.component';
import {ViewDemandNoteComponent} from '../demand-note-list/view-demand-note/view-demand-note.component';
import {ProcessRejectionComponent} from '../forms/process-rejection/process-rejection.component';

@Component({
    selector: 'app-view-single-consignment-document',
    templateUrl: './view-single-consignment-document.component.html',
    styleUrls: ['./view-single-consignment-document.component.css']
})
export class ViewSingleConsignmentDocumentComponent implements OnInit {
    active: Number = 1;
    consignmentId: string;
    consignment: any;
    attachments: any[];
    comments: any[];
    consignmentItems: any[];
    paymentFees: any[];
    configurations: any[];
    demandNotes: any[];
    checkLists: any[];
    supervisorTasks: any[]

    supervisorCharge: boolean = false
    inspectionOfficer: boolean = false

    constructor(private diService: DestinationInspectionService,
                private dialog: MatDialog,
                private activatedRoute: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            rs => {
                this.consignmentId = rs.get('id');
                this.loadConsignmentDetails();
                this.loadFees();
                this.loadUiConfigurations();
            }
        );


    }

    removeTasks(taskId: any) {
        this.diService.deleteTask(taskId)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.loadSupervisorTasks()
                        })
                    } else {
                        this.diService.showError(res.message, () => {

                        })
                    }
                }
            )
    }

    viewDemandNote(demandNoteId: any) {
        this.dialog.open(ViewDemandNoteComponent, {
            data: {
                id: demandNoteId
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails()
                    }
                }
            )
    }

    approveRejectTasks(taskId: any, docUuid: any, taskTitle: any) {
        this.dialog.open(ProcessRejectionComponent, {
            data: {
                cdUuid: docUuid,
                title: taskTitle,
                taskId: taskId
            }
        }).afterClosed()
            .subscribe(res => {
                this.loadSupervisorTasks()
            })
    }

    loadSupervisorTasks() {
        this.diService.loadSupervisorTasks(this.consignment.cd_details.uuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.supervisorTasks = res.data
                    } else {
                        console.log(res.message)
                    }
                }
            )
    }

    createInspectionChecklist() {
        this.router.navigate(['/di/inspection/checklist', this.consignmentId]);
    }

    loadChecklists() {
        this.diService.loadChecklists(this.consignment.cd_details.uuid)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.checkLists = res.data;

                    } else {
                        console.log(res.message);
                    }
                }
            );
    }


    loadDemandNotes(reload: any = null, etype: String = 'others') {
        // Reload consignment
        if (reload) {
            console.log("Demand note: " + reload + " -> " + etype)
            this.loadConsignmentDetails(true);
        }
        this.diService.listDemandNotes(this.consignment.cd_details.id)
            .subscribe(
                res => {
                    if (res.responseCode == '00') {
                        this.demandNotes = res.data;
                    }
                }
            );
    }

    generateDemandNote() {
        this.dialog.open(SendDemandNoteTokwsComponent, {
            data: {
                uuid: this.consignmentId,
                items: this.consignmentItems,
                paymentFees: this.paymentFees
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.active = 13
                        this.loadDemandNotes(true, 'generated')
                        // Open preview page
                        if (res.id) {
                            this.viewDemandNote(res.id)
                        }
                    } else {
                        console.log("Response data:" + res)
                    }
                }
            );
    }

    reloadAttachments(v: boolean) {
        if (v) {
            this.listConsignmentAttachments();
        }
    }

    viewCoR() {
        this.router.navigate(['/di/cor/details', this.consignmentId]);
    }

    viewCoC(docType: string) {
        this.router.navigate(['/di/certificate', docType, 'details', this.consignmentId]);
    }

    loadUiConfigurations() {
        this.diService.getInspectionUiConfigurations()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.configurations = res.data;
                    } else {
                        this.configurations = null;
                    }
                }
            );
    }

    loadFees() {
        this.diService.demandNoteFees()
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.paymentFees = res.data;
                    } else {
                        this.paymentFees = [];
                    }
                }
            );
    }

    viewIdfDocument() {
        this.router.navigate(['/di/idf/details', this.consignment.cd_details.uuid]);
    }

    loadComments() {
        this.diService.getAuditComments(this.consignment.cd_details.id)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.comments = res.data;
                    } else {
                        this.comments = [];
                    }
                }
            );
    }

    viewDeclarationDocument() {
        this.router.navigate(['/di/declaration/document', this.consignment.cd_details.uuid]);
    }

    viewManifestDocument() {
        this.router.navigate(['/di/manifest/document', this.consignment.cd_details.uuid]);
    }

    goBackHome() {
        this.router.navigateByUrl('/di');
    }

    uploadAttachment() {
        const ref = this.dialog.open(AttachmentDialogComponent, {
            data: {
                id: this.consignmentId
            }
        });
        ref.afterClosed()
            .subscribe(
                uploaded => {
                    if (uploaded) {
                        this.listConsignmentAttachments();
                    }
                }
            );
    }

    listConsignmentAttachments() {
        this.diService.getConsignmentAttachments(this.consignmentId)
            .subscribe(
                res => {
                    if (res.responseCode === '00') {
                        this.attachments = res.data;
                    } else {
                        this.attachments = [];
                    }
                }
            );
    }

    isVehicle(): Boolean {
        if (this.consignment) {
            return this.consignment.cd_details.cdTypeCategory === 'VEHICLES';
        }
        return false;
    }

    viewInspectionChecklists() {
        this.router.navigate(['/di/checklist/details/', this.consignment.cd_details.uuid]);
    }

    downloadDemandNote() {
        // let d=341
        this.diService.downloadDocument('/api/v1/download/demand/note/' + this.consignment.cd_details.id);
    }

    goBack() {
        this.router.navigate(['/di']);
    }

    loadConsignmentDetails(demandNoteReload: Boolean = false) {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {

                    if (response.responseCode === '00') {

                        this.consignment = response.data;
                        console.log(this.consignment.cd_consignee.name);
                        this.consignmentItems = this.consignment.items_cd;
                        if (this.consignment.ui.supervisor) {
                            this.supervisorCharge = true
                            this.loadSupervisorTasks()
                        }
                        this.listConsignmentAttachments();
                        this.loadComments();
                        if (!demandNoteReload) {
                            this.loadDemandNotes(false, 'consignment');
                        }
                    } else if (response.responseCode === "000") {
                        // Reload consignment details
                        this.loadConsignmentDetails(demandNoteReload)
                    } else {
                        this.diService.showError(response.message, this.goBackHome)
                    }
                }
            );
    }

    generateLocalCoC() {
        const ref = this.dialog.open(GenerateLocalCocComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    generateLocalCoR() {
        const ref = this.dialog.open(GenerateLocalCorComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    manualAssignInspectionOfficer() {
        const ref = this.dialog.open(ManualAssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    assignInspectionOfficer() {
        const ref = this.dialog.open(AssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }

                }
            );
    }

    blacklistUser() {
        const ref = this.dialog.open(BlacklistComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    supervisorTargetConsignment() {
        const ref = this.dialog.open(TargetSupervisorComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    targetConsignment() {
        const ref = this.dialog.open(TargetItemComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    sendCertificateOfCompliance() {
        const ref = this.dialog.open(SendCoiComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    markAsCompliant() {
        const ref = this.dialog.open(CompliantComponent, {
            data: {
                uuid: this.consignmentId,
                cocRequest: this.consignment.ui.cocRequest,
                corRequest: this.consignment.ui.corRequest,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.router.navigate(['/di']);
                    }
                }
            );
    }

    assignConsignmentPort() {
        const ref = this.dialog.open(AssignPortComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    reassignInspectionOfficer() {
        const ref = this.dialog.open(ReAssignOfficerComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadConsignmentDetails();
                    }
                }
            );
    }

    approveRejectConsignment() {
        const ref = this.dialog.open(ApproveRejectConsignmentComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
                complianceStatus: this.consignment.cd_details.complianceStatus,
                documentType: this.consignment.cd_details.cocType
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.router.navigate(['/di']);
                    }
                }
            );
    }
}
