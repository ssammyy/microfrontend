import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-manage-corporate-customer',
    templateUrl: './manage-corporate-customer.component.html',
    styleUrls: ['./manage-corporate-customer.component.css']
})
export class ManageCorporateCustomerComponent implements OnInit {
    accountStatuses = [
        {
            "code": "BLOCK",
            "description": "Block account"
        },
        {
            "code": "UNBLOCK",
            "description": "Un-Block account"
        },
        {
            "code": "SUSPEND",
            "description": "Suspend account"
        },
        {
            "code": "UNSUSPEND",
            "description": "Un-Suspend account"
        },
        {
            "code": "DELETE",
            "description": "Delete account"
        },
        {
            "code": "UPDATE_LIMIT",
            "description": "Synchronize SAGE limit & details"
        }
    ]
    public form: FormGroup;
    message: any
    loading: boolean = false
    corporateId: any

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private fiService: FinanceInvoiceService) {
    }

    ngOnInit(): void {
        this.corporateId = this.data.corporateId
        this.form = this.fb.group({
            actionCode: ['', Validators.required],
            remarks: ['', [Validators.required, Validators.minLength(5)]]
        })
    }

    saveRecord() {
        this.loading = true
        this.fiService.sendCorporateAction(this.form.value, this.corporateId)
            .subscribe(
                res => {
                    this.loading = false
                    if (res.responseCode === "00") {
                        this.fiService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    console.log(error)
                    this.loading = false
                }
            )
    }

}
