import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-engineering-item-checklist',
    templateUrl: './engineering-item-checklist.component.html',
    styleUrls: ['./engineering-item-checklist.component.css']
})
export class EngineeringItemChecklistComponent implements OnInit {

    complianceStatus: any[]
    engineeringSections: any[]
    categories: any[]
    form: FormGroup
    itemId: any
    message: any

    constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.itemId = this.data.itemId
        this.complianceStatus = this.data.complianceStatus
        this.engineeringSections = this.data.engineeringSections
        this.categories = this.data.categories
        console.log(this.categories)
        let formData = this.data.checklist
        this.form = this.fb.group({
            brand: [formData ? formData.brand : '', Validators.maxLength(256)],
            section: [formData ? formData.section : '', Validators.required],
            ksEasApplicable: [formData ? formData.ksEasApplicable : '', Validators.maxLength(256)],
            quantityVerified: [formData ? formData.quantityVerified : '', Validators.maxLength(256)],
            quantityVerifiedUnit: [formData ? formData.quantityVerifiedUnit : '', Validators.maxLength(25)],
            mfgNameAddress: [formData ? formData.mfgNameAddress : '', Validators.maxLength(256)],
            batchNoModelTypeRef: [formData ? formData.batchNoModelTypeRef : '', Validators.maxLength(256)],
            fiberComposition: [formData ? formData.fiberComposition : '', Validators.maxLength(256)],
            instructionsUseManual: [formData ? formData.instructionsUseManual : '', Validators.maxLength(256)],
            warrantyPeriodDocumentation: [formData ? formData.warrantyPeriodDocumentation : '', Validators.maxLength(256)],
            safetyCautionaryRemarks: [formData ? formData.safetyCautionaryRemarks : '', Validators.maxLength(256)],
            sizeClassCapacity: [formData ? formData.sizeClassCapacity : '', Validators.maxLength(256)],
            certMarksPvocDoc: [formData ? formData.certMarksPvocDoc : '', Validators.maxLength(256)],
            disposalInstruction: [formData ? formData.disposalInstruction : '', Validators.required],
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
