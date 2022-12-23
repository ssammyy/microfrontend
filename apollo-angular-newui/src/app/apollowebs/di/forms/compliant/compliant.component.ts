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
    loading: boolean = false
    corRequest: Boolean = false
    cocRequest: Boolean = false

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        console.log(this.data)
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
        this.corRequest = this.data.corRequest
        this.cocRequest = this.data.cocRequest
        //console.log(this.cocRequest)
        //console.log(this.corRequest)
        this.form = this.fb.group({
            compliantStatus: ['', Validators.required],
            documentType: [null],
            remarks: ['', [Validators.required, Validators.minLength(5)]]
        }, DocumentTypeSelectionValidator)
    }

    processResult(res) {
        this.loading = false
        if (res.responseCode === "00") {
            this.diService.showSuccess(res.message, () => {
                this.dialogRef.close(true)
            })
        } else {
            this.message = res.message
        }
    }

    processError(err: any) {
        this.loading = false
        console.log(err)
    }

    saveRecord() {
        this.loading = true
        if (this.form.value.compliantStatus === '0') {
            this.diService.showConfirmation(`Are you sure you want to reject thi consignment with UCR  ${this.data.ucr}?`, (result) => {
                if (result) {
                    this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "mark-compliant")
                        .subscribe(
                            res => this.processResult(res),
                            error => this.processError(error)
                        )
                } else {
                    this.loading = false
                }
            })
        } else {
            if (this.form.value.documentType && this.form.value.documentType?.length > 0) {
                this.diService.sendConsignmentDocumentAction(this.form.value, this.data.uuid, "mark-compliant")
                    .subscribe(
                        res => this.processResult(res),
                        error => this.processError(error)
                    )
            } else {
                this.loading = false;
                this.message = "Please select the certificate to issue"
                this.diService.showError(this.message)
            }
        }
    }
}
