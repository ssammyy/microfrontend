import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-item-checklist',
    templateUrl: './item-checklist.component.html',
    styleUrls: ['./item-checklist.component.css']
})
export class ItemChecklistComponent implements OnInit {
    complianceStatus: any[]
    categories: any[]
    form: FormGroup
    itemId: any
    message: any

    constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.itemId = this.data.itemId
        this.complianceStatus=this.data.complianceStatus
        this.categories = this.data.categories
        console.log(this.categories)
        let formData = this.data.checklist
        this.form = this.fb.group({
            brand: [formData ? formData.branch : '', Validators.maxLength(256)],
            ksEasApplicable: [formData ? formData.ksEasApplicable : '', Validators.maxLength(256)],
            quantityVerified: [formData ? formData.quantityVerified : '', Validators.maxLength(256)],
            dateMfgPackaging: [formData ? formData.dateMfgPackaging : '', Validators.maxLength(256)],
            dateExpiry: [formData ? formData.dateExpiry : '', Validators.maxLength(256)],
            mfgName: [formData ? formData.mfgName : '', Validators.maxLength(256)],
            mfgAddress: [formData ? formData.mfgAddress : '', Validators.maxLength(256)],
            compositionIngredients: [formData ? formData.compositionIngredients : '', Validators.maxLength(256)],
            storageCondition: [formData ? formData.storageCondition : '', Validators.maxLength(256)],
            appearance: [formData ? formData.appearance : '', Validators.maxLength(256)],
            certMarksPvocDoc: [formData ? formData.certMarksPvocDoc : '', Validators.maxLength(256)],
            compliant: [formData ? formData.compliant : '', Validators.required],
            category: [formData ? formData.category : '',],
            sampled: [formData ? formData.sampled : '', Validators.required],
            remarks: [formData ? formData.remarks : '', Validators.required]
        })
    }

    addCompliance() {
        this.dialogRef.close(this.form.value)
    }

}
