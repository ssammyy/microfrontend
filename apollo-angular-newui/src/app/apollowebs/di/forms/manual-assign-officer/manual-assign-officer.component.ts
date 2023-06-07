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
    loading=false
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            // supervisorId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }

    saveRecord() {
        this.loading=true
        this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "manual-pick")
            .subscribe(
                res => {
                    this.loading=false
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message,()=>{
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading=false
                    console.log(error)
                }
            )
    }
}