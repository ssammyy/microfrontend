import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-compliance-update-form',
    templateUrl: './compliance-update-form.component.html',
    styleUrls: ['./compliance-update-form.component.css']
})
export class ComplianceUpdateFormComponent implements OnInit {

    complianceStatus = [
        {
            name: 'COMPLIANCE',
            description: 'Compliant',
        },
        {
            name: 'NON-COMPLIANCE',
            description: 'Non-Compliant',
        }
    ]
    form: FormGroup
    message: any

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<any>, private fb: FormBuilder, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            bsNumber: ['', [Validators.required, Validators.maxLength(80)]],
            submissionDate: ['', Validators.required],
            remarks: ['', Validators.maxLength(256)],
        })
    }

    saveSsfResult() {
        this.message=null
        this.diService.updateSSFResults(this.form.value, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
