import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ApproveRejectConsignmentComponent} from './approve-reject-consignment/approve-reject-consignment.component';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";

@Component({
    selector: 'app-view-single-consignment-document',
    templateUrl: './view-single-consignment-document.component.html',
    styleUrls: ['./view-single-consignment-document.component.css']
})
export class ViewSingleConsignmentDocumentComponent implements OnInit {
    active = 'consignee';
    consignmentId: string;
    consignment: any;
    consignmentItems: any[]

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog, private activatedRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            rs => {
                this.consignmentId = rs.get("id")
                this.loadConsignmentDetails()
            }
        )
    }

    goBackHome() {
        this.router.navigateByUrl("/di")
    }

    loadConsignmentDetails() {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {
                    if (response.responseCode === "00") {
                        this.consignment = response.data
                        this.consignmentItems=this.consignment.items_cd
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
                consignment: this.consignment
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                   if(res){

                   }
                }
            )
    }
}
