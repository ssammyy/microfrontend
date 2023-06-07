import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ValidationService} from "../../../../../../core/services/errors/validation.service";

@Component({
    selector: 'app-other-item-checklist',
    templateUrl: './other-item-checklist.component.html',
    styleUrls: ['./other-item-checklist.component.css']
})
export class OtherItemChecklistComponent implements OnInit {

    complianceStatus: any[]
    categories: any[]
    form: FormGroup
    itemId: any
    message: any
    fieldNames = {
        brand: "Brand",
        compliant: "Compliant",
        category: "Category",
        sampled: "Sampled",
        ksEasApplicable: "KS Applicable",
        quantityVerified: "Verified quantity",
        quantityVerifiedUnit: "Verified unit",
        packagingLabelling: "Package labling",
        physicalCondition: "Physical condition",
        defects: "Defects",
        presenceAbsenceBanned: "Banned items",
        documentation: "Product documentation",
        remarks: "Remarks"
    }
    errors: any

    constructor(private fb: FormBuilder, private validate: ValidationService, public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.itemId = this.data.itemId
        this.complianceStatus = this.data.complianceStatus
        this.categories = this.data.categories
        let formData = this.data.checklist
        this.form = this.fb.group({
            brand: ['', Validators.maxLength(256)],
            compliant: [formData ? formData.compliant : '', Validators.required],
            category: [formData ? formData.category : '',],
            sampled: [formData ? formData.sampled : '', Validators.required],
            ksEasApplicable: [formData ? formData.ksEasApplicable : '', Validators.maxLength(256)],
            quantityVerified: [formData ? formData.quantityVerified : '', Validators.maxLength(256)],
            quantityVerifiedUnit: [formData ? formData.quantityVerifiedUnit : '', Validators.maxLength(25)],
            packagingLabelling: [formData ? formData.packagingLabelling : '', Validators.maxLength(256)],
            physicalCondition: [formData ? formData.physicalCondition : '', Validators.maxLength(256)],
            defects: [formData ? formData.defects : '', Validators.maxLength(256)],
            presenceAbsenceBanned: [formData ? formData.presenceAbsenceBanned : '', Validators.maxLength(256)],
            documentation: [formData ? formData.documentation : '', Validators.maxLength(256)],
            remarks: [formData ? formData.remarks : '', Validators.required]
        })

        this.form.valueChanges.subscribe(() => {
            this.errors = this.validate.validateForm(this.form, this.fieldNames)
        })
    }

    addCompliance() {
        this.dialogRef.close(this.form.value)
    }
}
