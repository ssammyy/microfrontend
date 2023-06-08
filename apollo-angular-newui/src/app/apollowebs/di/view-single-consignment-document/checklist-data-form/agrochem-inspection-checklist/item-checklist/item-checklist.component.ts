import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ValidationService} from "../../../../../../core/services/errors/validation.service";

@Component({
    selector: 'app-item-checklist',
    templateUrl: './item-checklist.component.html',
    styleUrls: ['./item-checklist.component.css']
})
export class ItemChecklistComponent implements OnInit {
    complianceStatus: any[]
    agrochemicalSections: any[]
    categories: any[]
    form: FormGroup
    itemId: any
    message: any
    errors: any
    fieldNames = {
        brand: "Brand",
        section: "Section",
        ksEasApplicable: "KsEas",
        quantityVerified: "Verified quantity",
        dateMfgPackaging: "Date of manufacture",
        quantityVerifiedUnit: "Verified unit",
        dateExpiry: "Expiry Date",
        mfgName: "Manufacture Name",
        mfgAddress: "Manufacturer Address",
        compositionIngredients: "Composition Ingredients",
        storageCondition: "Storage Condition",
        appearance: "Appearance",
        certMarksPvocDoc: "Cert Mark PVOC",
        compliant: "Compliant",
        category: "Category",
        sampled: "Sampled",
        remarks: "Remarks"
    }

    constructor(private fb: FormBuilder, private validate: ValidationService, public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.itemId = this.data.itemId
        this.complianceStatus = this.data.complianceStatus
        this.agrochemicalSections = this.data.agrochemicalSections
        this.categories = this.data.categories
        let formData = this.data.checklist
        this.form = this.fb.group({
            brand: [formData ? formData.brand : '', Validators.maxLength(256)],
            section: [formData ? formData.section : '', Validators.required],
            ksEasApplicable: [formData ? formData.ksEasApplicable : '', Validators.maxLength(256)],
            quantityVerified: [formData ? formData.quantityVerified : '', Validators.maxLength(256)],
            dateMfgPackaging: [formData ? formData.dateMfgPackaging : null, Validators.maxLength(256)],
            quantityVerifiedUnit: [formData ? formData.quantityVerifiedUnit : '', Validators.maxLength(25)],
            dateExpiry: [formData ? formData.dateExpiry : null],
            mfgName: [formData ? formData.mfgName : null, Validators.maxLength(256)],
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
        this.form.valueChanges.subscribe(() => {
            this.errors = this.validate.validateForm(this.form, this.fieldNames)
            console.log(this.errors)
        })
    }

    addCompliance() {
        this.dialogRef.close(this.form.value)
    }

}
