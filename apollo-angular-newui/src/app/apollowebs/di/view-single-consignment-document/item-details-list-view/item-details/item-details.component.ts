import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {MatDialog} from "@angular/material/dialog";
import {MinistryInspectionRequestComponent} from "../ministry-inspection-request/ministry-inspection-request.component";

@Component({
    selector: 'app-item-details',
    templateUrl: './item-details.component.html',
    styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {
    itemUuid: any
    cdUuid: any
    itemDetails: any
    checkLists: any[]

    constructor(private activatedRoute: ActivatedRoute, private diService: DestinationInspectionService,
                private router: Router, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.itemUuid = res.get("id")
                this.cdUuid = res.get("cdUuid")
                this.loadItemDetails()
                this.loadChecklists()
            }
        )
    }

    loadChecklists() {
        this.diService.loadChecklists(this.itemUuid)
            .subscribe(
                res=>{
                    if(res.responseCode==="00"){
                        this.checkLists=res.data
                    }else{
                        console.log(res.message)
                    }
                }
            )
    }

    submitMinistryInspection() {
        this.dialog.open(MinistryInspectionRequestComponent,{
            data: {
                uuid: this.itemUuid,
                reinspection: false,
            }
        })
    }

    resubmitMinistryInspection() {
        this.dialog.open(MinistryInspectionRequestComponent,{
            data: {
                uuid: this.itemUuid,
                reinspection: true,
            }
        })
    }

    loadItemDetails() {
        this.diService.loadItemDetails(this.itemUuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.itemDetails = res.data
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/di/", this.cdUuid])
                        });
                    }
                }
            )
    }

}
