import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ApproveRejectConsignmentComponent} from './approve-reject-consignment/approve-reject-consignment.component';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {AttachmentDialogComponent} from "./attachment-dialog/attachment-dialog.component";

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
    consignmentItems: any[]
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
                this.loadUiConfigurations()
            }
        )

    }

    viewCoR() {

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
    viewIdfDocument() {
        this.router.navigate(["/di/idf/details", this.consignment.cd_details.uuid])
    }

    viewDeclarationDocument() {
        this.router.navigate(["/di/declaration/document", this.consignment.cd_details.uuid])
    }

    goBackHome() {
        this.router.navigateByUrl("/di")
    }
    uploadAttachment() {
        let ref=this.dialog.open(AttachmentDialogComponent,{
            data: {
                id: this.consignmentId
            }
        })
        ref.afterClosed()
            .subscribe(
                uploaded=>{
                    if(uploaded){
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


    loadConsignmentDetails() {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {
                    if (response.responseCode === "00") {
                        this.consignment = response.data
                        this.consignmentItems = this.consignment.items_cd
                        this.listConsignmentAttachments()
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

    approveRejectConsignment() {
        let ref = this.dialog.open(ApproveRejectConsignmentComponent, {
            data: {
                uuid: this.consignmentId,
                configurations: this.configurations,
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {

                    }
                }
            )
    }
}
