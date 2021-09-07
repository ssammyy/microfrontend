import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-ssf-details-form',
    templateUrl: './ssf-details-form.component.html',
    styleUrls: ['./ssf-details-form.component.css']
})
export class SsfDetailsFormComponent implements OnInit {
    message: any
    form: FormGroup

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>, private fb: FormBuilder, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            description: ['', Validators.required],
            ssfNo: ['', [Validators.required, Validators.maxLength(25)]],
            ssfSubmissionDate: ['', Validators.required],
            bsNumber: ['', [Validators.required, Validators.maxLength(80)]],
            brandName: ['', [Validators.required, Validators.maxLength(150)]],
            productDescription: ['', [Validators.required, Validators.maxLength(150)]]
        })
    }

    saveRecord() {
        this.message = null
        this.diService.saveSSFDetails(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.dialogRef.close(false)
                    } else {
                        this.message = res.message
                    }
                }
            )
    }
}
