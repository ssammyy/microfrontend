import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-add-update-limit',
    templateUrl: './add-update-limit.component.html',
    styleUrls: ['./add-update-limit.component.css']
})
export class AddUpdateLimitComponent implements OnInit {

    form: FormGroup
    regions: any
    message: any
    loading: any = false
    billingSchedule = [
        {
            name: 'MONTHLY',
            description: 'Monthly Billing'
        },
        {
            name: 'BI_MONTHLY',
            description: 'Bi-Monthly Billing'
        },
        {
            name: 'DATE_RANGE',
            description: 'Custom Date Range'
        }
    ]
    penaltyTypes = [
        {
            name: 'FIXED',
            description: 'Fixed Amount'
        },
        {
            name: 'PERCENTAGE',
            description: 'Percentage of the Bill amount'
        }
    ]
    cfsStatuses = [
        {
            name: '1',
            description: 'Active'
        },
        {
            name: '0',
            description: 'Disabled'
        }
    ]
    billTypes = [
        {
            name: 'COURIER',
            description: 'Courier billing'
        },
        {
            name: 'PVOC',
            description: 'PVOC billing'
        },
        {
            name: 'OTHER',
            description: 'Other billing'
        }
    ]

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any, private diService: DestinationInspectionService, private fiService: FinanceInvoiceService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            billType: [this.data ? this.data.billType : "", [Validators.required]],
            corporateType: [this.data ? this.data.corporateType : "", [Validators.required]],
            billReceiptPrefix: [this.data ? this.data.billReceiptPrefix : "", [Validators.required]],
            billDateType: [this.data ? this.data.billDateType : "", [Validators.required]],
            billEndDay: [this.data ? this.data.billEndDay : "0"],
            billStartDate: [this.data ? this.data.billEndDay : "0"],
            billPaymentDay: [this.data ? this.data.billPaymentDay : "0"],
            penaltyType: [this.data ? this.data.penaltyType : "", [Validators.required]],
            penaltyAmount: [this.data ? this.data.penaltyAmount : "", [Validators.required]],
            maxBillAmount: [this.data ? this.data.maxBillAmount : "", [Validators.required]],
            status: [this.data ? this.data.status : "", [Validators.required]]
        })
    }

    saveBillLimit() {
        this.loading = true
        let call = this.fiService.saveBillType(this.form.value)
        if (this.data) {
            call = this.fiService.updateBillType(this.form.value, this.data.id)
        }
        call.subscribe(
            res => {
                if (res.responseCode === "00") {
                    this.diService.showSuccess(res.message, () => {
                        this.dialogRef.close(true)
                    })
                } else {
                    this.message = res.message
                    this.diService.showError(res.message)
                }
                this.loading = false;
            },
            error => {
                console.error(error)
                this.loading = false
            }
        )

    }

}
