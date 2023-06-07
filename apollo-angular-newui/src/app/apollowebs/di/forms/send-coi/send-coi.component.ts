import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-send-coi',
    templateUrl: './send-coi.component.html',
    styleUrls: ['./send-coi.component.css']
})
export class SendCoiComponent implements OnInit {

    public form: FormGroup;
    message: any;
    loading = false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            sendCoiStatus: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.loading = true
        this.diService.sendCertificateOfInspection(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode === "00") {
                        this.dialogRef.close(true)
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading = false
                }
            )
    }
}