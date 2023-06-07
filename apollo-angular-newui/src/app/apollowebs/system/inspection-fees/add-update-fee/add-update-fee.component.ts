import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-add-update-fee',
    templateUrl: './add-update-fee.component.html',
    styleUrls: ['./add-update-fee.component.css']
})
export class AddUpdateFeeComponent implements OnInit {
    currencyCodes = [
        {
            name: 'KSH',
            description: 'Kenya Shilling'
        },
        {
            name: 'USD',
            description: 'US Dolar'
        }
    ]
    rateTypes = [
        {
            name: 'FIXED',
            description: 'fixed'
        },
        {
            name: 'PERCENTAGE',
            description: 'Percentage'
        },
        {
            name: 'RANGE',
            description: 'Range'
        }
    ]
    feeStatuses = [
        {
            name: '1',
            description: 'Active'
        },
        {
            name: '0',
            description: 'Disabled'
        }
    ]
    form: FormGroup
    message: any
    loading = false

    constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public  data: any, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {

        this.form = this.fb.group({
            currencyCode: [this.data ? this.data.currencyCode : '', [Validators.required, Validators.minLength(2)]],
            minimumAmount: [this.data ? this.data.minimumAmount : '0.0'],
            maximumAmount: [this.data ? this.data.maximumAmount : '0.0'],
            rate: [this.data ? this.data.rate : '0.0',],
            rateType: [this.data ? this.data.rateType : '', [Validators.required]],
            name: [this.data ? this.data.name : '', [Validators.required]],
            goodCode: [this.data ? this.data.goodCode : '', [Validators.required]],
            status: [this.data ? this.data.status : '', [Validators.required]],
            description: [this.data ? this.data.description : '', [Validators.required]]
        })
    }

    addLaboratory() {
        this.loading = true
        let call = this.diService.saveInspectionFee(this.form.value)
        if (this.data) {
            call = this.diService.updateInspectionFee(this.form.value, this.data.id)
        }
        call.subscribe(
            res => {
                if (res.responseCode === "00") {
                    this.diService.showSuccess(res.message, () => {
                        this.dialogRef.close(true)
                    })
                } else {
                    this.diService.showError(res.message, () => {
                        this.message = res.message;
                    })
                }
                this.loading = false
            },
            error => {
                console.log(error)
                this.message = "Failed to send message"
                this.loading = false
            }
        )
    }

}
