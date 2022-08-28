import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

    masterCountryList: any[];
    masterCurrencyList: any[];
    masterDescriptionList: any[]
    public productDetails: FormGroup
    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder,@Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.masterCountryList=this.data.countries;
        this.masterCurrencyList=this.data.currencies;
        this.masterDescriptionList=this.data.descriptions;
        this.productDetails=this.fb.group({
            hsCode: ['', Validators.required],
            productDescription: ['', Validators.required],
            productUnit: ['', Validators.required],
            countryOfOrigin: ['', Validators.required],
            quantity: [0, Validators.required],
            currency: ['', Validators.required],
            totalAmount: ['', Validators.required],
        })
    }
    //Transfer current marsterlistdata to masterlist array pool
    addNewMasterSheet() {
        this.dialogRef.close(this.productDetails.value)
    }
}
