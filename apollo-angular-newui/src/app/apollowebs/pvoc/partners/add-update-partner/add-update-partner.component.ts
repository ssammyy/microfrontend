import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
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
    countries = [
        {
            name: "Kenya",
            code: "KE"
        },
        {
            name: "England",
            code: "UK"
        },
        {
            name: "United States",
            code: "US"
        },
        {
            name: "China",
            code: "CN"
        },
        {
            name: "Japan",
            code: "JA"
        },
    ]

    constructor(private fb: FormBuilder, private dialog: MatDialogRef<any>,@Inject(MAT_DIALOG_DATA) public data: any, private pvocService: PVOCService) {
    }

    ngOnInit(): void {

        this.form = this.fb.group({
            partnerRefNo: [this.data ? this.data.partnerRefNo : null, [Validators.required, Validators.minLength(2)]],
            partnerName: [this.data ? this.data.partnerName : null, [Validators.required, Validators.minLength(2)]],
            partnerPin: [this.data ? this.data.partnerPin : null, [Validators.required, Validators.minLength(2)]],
            partnerEmail: [this.data ? this.data.partnerEmail : null, [Validators.required, Validators.email]],
            partnerAddress1: [this.data ? this.data.partnerAddress1 : null],
            partnerAddress2: [this.data ? this.data.partnerAddress2 : null],
            partnerCity: [this.data ? this.data.partnerCity : "Nairobi",[Validators.required]],
            partnerCountry: [this.data ? this.data.partnerCountry : "Kenya",[Validators.required]],
            partnerZipcode: [this.data ? this.data.partnerZipcode : null],
            partnerTelephoneNumber: [this.data ? this.data.partnerTelephoneNumber : null, [Validators.required]],
            partnerFaxNumber: [this.data ? this.data.partnerFaxNumber : null]
        })
        // this.form.valueChanges.subscribe(
        //     res=>{
        //         console.log(this.form.errors)
        //     }
        // )
    }

    addPartner() {
        var result = null;
        this.message = null
        this.loading = true
        if (this.data) {
            result = this.pvocService.updatePartner(this.form.value, this.data.partnerId)
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
