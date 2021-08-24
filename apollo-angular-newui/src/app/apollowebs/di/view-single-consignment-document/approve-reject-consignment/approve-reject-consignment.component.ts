import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-approve-reject-consignment',
    templateUrl: './approve-reject-consignment.component.html',
    styleUrls: ['./approve-reject-consignment.component.css']
})
export class ApproveRejectConsignmentComponent implements OnInit {
    public statuses: any[]
    public form: FormGroup
    message: String;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder,
                @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        console.log(this.data)
        this.statuses = this.data.configurations.CDStatusTypes
        this.form = this.fb.group({
            cdStatusTypeId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.diService.approveReject(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }
}
