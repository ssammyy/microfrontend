import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-engineering-inspection-checklist',
    templateUrl: './engineering-inspection-checklist.component.html',
    styleUrls: ['./engineering-inspection-checklist.component.css']
})
export class EngineeringInspectionChecklistComponent implements OnInit {
    @Output() private engineeringDetails = new EventEmitter<any>();
    engineeringChecklist: FormGroup

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.engineeringChecklist = this.fb.group({
            serialNumber: ['', [Validators.required, Validators.maxLength(256)]],
            brand: ['', Validators.maxLength(256)],
            ksEasApplicable: ['', Validators.maxLength(256)],
            quantityVerified: ['', Validators.maxLength(256)],
            mfgNameAddress: ['', Validators.maxLength(256)],
            batchNoModelTypeRef: ['', Validators.maxLength(256)],
            fiberComposition: ['', Validators.maxLength(256)],
            instructionsUseManual: ['', Validators.maxLength(256)],
            warrantyPeriodDocumentation: ['', Validators.maxLength(256)],
            safetyCautionaryRemarks: ['', Validators.maxLength(256)],
            sizeClassCapacity: ['', Validators.maxLength(256)],
            certMarksPvocDoc: ['', Validators.maxLength(256)],
            disposalInstruction: ['', Validators.required],
            sampled: ['', Validators.required],
            remarks: ['', Validators.required]
        })

        this.engineeringChecklist.statusChanges
            .subscribe(
                res=>{
                    if(this.engineeringChecklist.valid){
                        this.engineeringDetails.emit(this.engineeringChecklist.value)
                    } else {
                        this.engineeringDetails.emit(null)
                    }
                }
            )
    }

}
