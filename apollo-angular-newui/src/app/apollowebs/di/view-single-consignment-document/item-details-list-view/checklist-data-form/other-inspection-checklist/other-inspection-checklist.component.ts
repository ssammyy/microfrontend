import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-other-inspection-checklist',
    templateUrl: './other-inspection-checklist.component.html',
    styleUrls: ['./other-inspection-checklist.component.css']
})
export class OtherInspectionChecklistComponent implements OnInit {
    @Output() private otherDetails = new EventEmitter<any>();
    otherItemChecklist: FormGroup

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.otherItemChecklist = this.fb.group({
            serialNumber: ['', [Validators.required, Validators.maxLength(256)]],
            brand: ['', Validators.maxLength(256)],
            ksEasApplicable: ['', Validators.maxLength(256)],
            quantityVerified: ['', Validators.maxLength(256)],
            packagingLabelling: ['', Validators.maxLength(256)],
            physicalCondition: ['', Validators.maxLength(256)],
            defects: ['', Validators.maxLength(256)],
            presenceAbsenceBanned: ['', Validators.maxLength(256)],
            documentation: ['', Validators.maxLength(256)],
            sampled: ['', Validators.required],
            remarks: ['', Validators.required]
        })

        this.otherItemChecklist.statusChanges
            .subscribe(
                res=>{
                    if(this.otherItemChecklist.valid){
                        this.otherDetails.emit(this.otherItemChecklist.value)
                    } else{
                        this.otherDetails.emit(null)
                    }
                }
            )
    }

}
