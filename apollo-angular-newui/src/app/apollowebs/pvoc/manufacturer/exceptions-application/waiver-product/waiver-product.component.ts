import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-waiver-product',
    templateUrl: './waiver-product.component.html',
    styleUrls: ['./waiver-product.component.css']
})
export class WaiverProductComponent implements OnInit {
    productForm: FormGroup

    constructor(private fg: FormBuilder, private dialogRef: MatDialogRef<any>) {
    }

    ngOnInit(): void {
        this.productForm = this.fg.group({
            "productName": ['', Validators.required],
            "permitNumber": ['', Validators.required],
            "expriryDate": ['', Validators.required],
            "brandName": ['', Validators.required]
        })
    }

    saveProduct() {
        this.dialogRef.close(this.productForm.value)
    }

}
