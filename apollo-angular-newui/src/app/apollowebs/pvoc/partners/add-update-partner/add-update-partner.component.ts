import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-add-update-partner',
    templateUrl: './add-update-partner.component.html',
    styleUrls: ['./add-update-partner.component.css']
})
export class AddUpdatePartnerComponent implements OnInit {
    form: FormGroup
    errors: any
    loading: boolean = false
    message: string
    countries: any[]
    billingTypes: any[]

    constructor(private fb: FormBuilder, private dialog: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any,private fiService: FinanceInvoiceService, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            partnerRefNo: [this.data.partner ? this.data.partner.partnerRefNo : null, [Validators.required, Validators.minLength(2)]],
            partnerName: [this.data.partner ? this.data.partner.partnerName : null, [Validators.required, Validators.minLength(2)]],
            partnerPin: [this.data.partner ? this.data.partner.partnerPin : null, [Validators.required, Validators.minLength(2)]],
            partnerEmail: [this.data.partner ? this.data.partner.partnerEmail : null, [Validators.required, Validators.email]],
            partnerAddress1: [this.data.partner ? this.data.partner.partnerAddress1 : null],
            partnerAddress2: [this.data.partner ? this.data.partner.partnerAddress2 : null],
            partnerCity: [this.data.partner ? this.data.partner.partnerCity : "Nairobi", [Validators.required]],
            partnerCountry: [this.data.partner ? this.data.partner.partnerCountry : "Kenya", [Validators.required]],
            partnerZipcode: [this.data.partner ? this.data.partner.partnerZipcode : null],
            partnerTelephoneNumber: [this.data.partner ? this.data.partner.partnerTelephoneNumber : null, [Validators.required]],
            partnerFaxNumber: [this.data.partner ? this.data.partner.partnerFaxNumber : null],
            billingContactName: [null,Validators.required],
            billingContactPhone: [null,Validators.required],
            billingContactEmail: [null, [Validators.required,Validators.email]],
            billingLimitId: [null, Validators.required]
        })
        this.countries = this.data.countries
        this.loadInvoiceDetails()
        // this.form.valueChanges.subscribe(
        //     res=>{
        //         console.log(this.form.errors)
        //     }
        // )
    }

    loadInvoiceDetails(){
        this.fiService.loadBillTypes()
            .subscribe(
                res=>{
                    if(res.responseCode==="00"){
                        this.billingTypes=res.data
                    }
                }
            )
    }

    addPartner() {
        var result = null;
        this.message = null
        this.loading = true
        if (this.data.partner) {
            result = this.pvocService.updatePartner(this.form.value, this.data.partner.partnerId)
        } else {
            result = this.pvocService.addPartner(this.form.value)
        }
        result.subscribe(
            res => {
                if (res.responseCode === "00") {
                    this.pvocService.showSuccess(res.message, () => {
                        this.dialog.close(true)
                    })

                } else {
                    this.message = res.message
                    this.pvocService.showError(res.message)
                    this.errors = res.errors
                }
                this.loading = false
            },
            error => {
                this.loading = false
                this.pvocService.showError(error.message)
            }
        )

    }
}
