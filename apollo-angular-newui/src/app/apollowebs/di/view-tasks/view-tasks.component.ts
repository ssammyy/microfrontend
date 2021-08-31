import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {MatDialog} from "@angular/material/dialog";
import {ProcessRejectionComponent} from "../forms/process-rejection/process-rejection.component";

@Component({
    selector: 'app-view-tasks',
    templateUrl: './view-tasks.component.html',
    styleUrls: ['./view-tasks.component.css']
})
export class ViewTasksComponent implements OnInit {
    active="consignment"
    tasks: any[]
    consignmentId: any
    consignment: any
    attachments: any[]=[]

    constructor(private dialog: MatDialog,private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadMyTasks()
    }
    approveRejectTasks(taskId: any, docUuid: any){
        this.dialog.open(ProcessRejectionComponent,{
            data:{
                cdUuid: docUuid,
                taskId: taskId
            }
        })
    }
    loadMyTasks() {
        this.diService.loadMyTasks()
            .subscribe(res => {
                if (res.responseCode == "00") {
                    this.tasks = res.data
                    if (this.tasks.length > 0) {
                        this.consignmentId = this.tasks[0].map.cdUuid
                        this.loadSelectedConsignmentDetails()
                    }
                }
            })
    }

    loadSelectedConsignmentDetails(){
        if(this.consignmentId) {
            this.diService.getConsignmentDetails(this.consignmentId)
                .subscribe(
                    res => {
                        if (res.responseCode == "00") {
                            this.consignment = res.data
                        } else {
                            swal.fire({
                                title: res.message,
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                                },
                                icon: 'error'
                            })
                        }
                    }
                )
        }
    }

}
