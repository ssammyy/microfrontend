import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-compliant',
    templateUrl: './compliant.component.html',
    styleUrls: ['./compliant.component.css']
})
export class CompliantComponent implements OnInit {

    public form: FormGroup;
    message: any

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        const DocumentTypeSelectionValidator: ValidatorFn = (fg: FormGroup) => {
            const status = fg.get('compliantStatus').value;
            if (status && status === "1") {
                const documentType = fg.get("documentType").value
                if (!documentType || documentType.length <= 0) {
                    return {documentType: true}
                }
            }
            return null
        };
        this.form = this.fb.group({
            compliantStatus: ['', Validators.required],
            documentType: [''],
            remarks: ['', [Validators.required,Validators.minLength(5)]]
        }, DocumentTypeSelectionValidator)
    }

    saveRecord() {
        this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "mark-compliant")
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message,()=>{
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                }
            )
    }
}
