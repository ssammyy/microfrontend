import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-add-update-corporate-customer',
    templateUrl: './add-update-corporate-customer.component.html',
    styleUrls: ['./add-update-corporate-customer.component.css']
})
export class AddUpdateCorporateCustomerComponent implements OnInit {
    form: FormGroup
    errors: any
    loading: boolean = false
    message: string
    mouDaysList = [
        {
            name: "MONTHLY",
            code: "1"
        },
        {
            name: "BIMONTHLY",
            code: "2"
        }
    ]
    coporateType = [
        {
            name: "Courier Goods",
            code: "COURIER"
        },
        {
            name: "Other",
            code: "OTHER"
        },
    ]
    cooporateGroupCodes = [
        {
            name: "Trading - Coast Region Customers",
            code: "TRACRC"
        },
        {
            name: "Trading - Foreign Customers",
            code: "TRAFOR"
        },
        {
            name: "Trading - Head Office Customers",
            code: "TRAHQS"
        },
        {
            name: "Trading - Lake Region Customers",
            code: "TRALRC"
        },
        {
            name: "Trading - Mount Kenya Customers",
            code: "TRAMKR"
        },
        {
            name: "Trading - North Eastern Customers",
            code: "TRANER"
        },
        {
            name: "Trading - Nairobi Region Customers",
            code: "TRANRB"
        },
        {
            name: "Trading - North Rift Customers",
            code: "TRANRR"
        },
        {
            name: "Trading - Rift Valley Customers",
            code: "TRARVR"
        },
        {
            name: "Trading - South Rift Region Customers",
            code: "TRASRR"
        },
    ]
    billingTypes: any

    constructor(private fb: FormBuilder, private dialog: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any, private fiService: FinanceInvoiceService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            corporateIdentifier: [this.data ? this.data.corporateIdentifier : null, [Validators.required, Validators.minLength(2)]],
            corporateName: [this.data ? this.data.corporateName : null, [Validators.required, Validators.minLength(2)]],
            groupCode: [this.data ? this.data.corporateGroupCode : null, [Validators.required, Validators.minLength(2)]],
            corporateType: [this.data ? this.data.corporateType : null, [Validators.required]],
            corporateEmail: [this.data ? this.data.corporateEmail : null, [Validators.required, Validators.email]],
            corporatePhone: [this.data ? this.data.corporatePhone : null, Validators.required],
            contactName: [this.data ? this.data.contactName : "", [Validators.required]],
            contactPhone: [this.data ? this.data.contactPhone : "", [Validators.required]],
            contactEmail: [this.data ? this.data.contactEmail : null, [Validators.required, Validators.email]],
            isCiakMember: [this.data ? this.data.isCiakMember : null, [Validators.required]],
            billId: [this.data ? this.data.billId : null, [Validators.required]],
            mouDays: [this.data ? this.data.mouDays : null, Validators.required]
        })
        this.loadBillingTypes()
    }

    loadBillingTypes() {
        this.fiService.loadBillTypes()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.billingTypes = res.data
                    }
                }
            )
    }

    addCorporate() {
        var result = null;
        this.message = null
        this.loading = true
        if (this.data) {
            result = this.fiService.updateCorporateCustomer(this.form.value, this.data.corporateId)
        } else {
            result = this.fiService.addCorporateCustomer(this.form.value)
        }
        result.subscribe(
            res => {
                if (res.responseCode === "00") {
                    this.fiService.showSuccess(res.message, () => {
                        this.dialog.close(true)
                    })

                } else {
                    this.message = res.message
                    this.fiService.showError(res.message)
                    this.errors = res.errors
                }
                this.loading = false
            },
            error => {
                this.loading = false
                this.fiService.showError(error.message)
            }
        )

    }
}
