import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-manual-assign-officer',
    templateUrl: './manual-assign-officer.component.html',
    styleUrls: ['./manual-assign-officer.component.css']
})
export class ManualAssignOfficerComponent implements OnInit {
    message: any;
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            supervisorId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "manual-pick")
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dialogRef.close(this.form.value)
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