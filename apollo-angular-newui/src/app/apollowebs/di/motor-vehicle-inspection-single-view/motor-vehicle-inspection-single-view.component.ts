import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";

//import { ApproveRejectConsignmentComponent } from './approve-reject-consignment/approve-reject-consignment.component';


@Component({
    selector: 'app-motor-vehicle-inspection-single-view',
    templateUrl: './motor-vehicle-inspection-single-view.component.html',
    styleUrls: ['./motor-vehicle-inspection-single-view.component.css']
})
export class MotorVehicleInspectionSingleViewComponent implements OnInit {
    active = 'consignee';
    itemId: any
    itemDetails: any

    constructor(private dialog: MatDialog, private router: Router, private activatedRoute: ActivatedRoute, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.itemId = res.get("id")
                this.loadData(this.itemId)
            }
        )
    }

    loadData(itemId: any) {
        this.diService.getMinistryInspections(itemId)
            .subscribe(
                result => {
                    if (result.responseCode === "00") {
                        this.itemDetails = result.data
                    } else {
                        swal.fire({
                            title: result.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/ministry/inspection"])
                        });
                    }
                }
            )
    }
}
